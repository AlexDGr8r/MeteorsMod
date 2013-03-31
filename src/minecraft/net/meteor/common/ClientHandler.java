package net.meteor.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

public class ClientHandler
implements IConnectionHandler, IPacketHandler
{
	public static ChunkCoordinates lastCrashLocation = null;
	public static ChunkCoordinates nearestTimeLocation = null;
	private static ArrayList<ChunkCoordinates> ghostMetLocs = new ArrayList<ChunkCoordinates>();

	public static ChunkCoordinates getClosestIncomingMeteor(double pX, double pZ) {
		ChunkCoordinates coords = null;
		double y = 50.0D;
		for (int i = 0; i < ghostMetLocs.size(); i++) {
			if (coords != null) {
				ChunkCoordinates loc = ghostMetLocs.get(i);
				double var1 = getDistance(pX, y, pZ, loc.posX, y, loc.posZ);
				double var2 = getDistance(pX, y, pZ, coords.posX, y, coords.posZ);
				if (var1 < var2)
					coords = loc;
			}
			else {
				coords = ghostMetLocs.get(i);
			}
		}
		return coords;
	}

	private static double getDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
		double var7 = x1 - x2;
		double var9 = y1 - y2;
		double var11 = z1 - z2;
		return MathHelper.sqrt_double(var7 * var7 + var9 * var9 + var11 * var11);
	}

	@Override
	public void playerLoggedIn(Player player, NetHandler netHandler, INetworkManager manager)
	{
		sendSettingsPacket(player);
		sendMetChunksPacket(player);
		sendLastCrashPacket(player);
		sendNearestTimePacket(player);
		MeteorsMod.proxy.meteorHandler.sendGhostMeteorPackets(player);
		for (String o : MeteorsMod.proxy.lastMeteorPrevented.keySet())
			sendShieldProtectUpdate(o);
	}

	private void sendSettingsPacket(Player player)
	{
		MeteorsMod mod = MeteorsMod.instance;
		ByteArrayOutputStream bos = new ByteArrayOutputStream(14);
		DataOutputStream outputStream = new DataOutputStream(bos);
		try {
			outputStream.writeBoolean(mod.allowSummonedMeteorGrief);
			outputStream.writeBoolean(mod.meteorsFallOnlyAtNight);
			outputStream.writeInt(mod.MinMeteorSize);
			outputStream.writeInt(mod.MaxMeteorSize);
			outputStream.writeInt(mod.ShieldRadiusMultiplier);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "MetSettings";
		packet.data = bos.toByteArray();
		packet.length = bos.size();
		PacketDispatcher.sendPacketToPlayer(packet, player);
	}

	private void sendMetChunksPacket(Player player) {
		Iterator iter = MeteorsMod.proxy.meteorHandler.shieldData.iterator();
		while (iter.hasNext()) {
			ShieldData sData = (ShieldData)iter.next();
			int size = 15 + sData.owner.getBytes().length;
			ByteArrayOutputStream bos = new ByteArrayOutputStream(size);
			DataOutputStream outputStream = new DataOutputStream(bos);
			try {
				outputStream.writeInt(sData.x);
				outputStream.writeInt(sData.z);
				outputStream.writeInt(sData.radius);
				outputStream.writeBoolean(sData.tbr);
				Packet.writeString(sData.owner, outputStream);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = "MetChunk";
			packet.data = bos.toByteArray();
			packet.length = bos.size();
			PacketDispatcher.sendPacketToPlayer(packet, player);
		}
	}

	private void sendLastCrashPacket(Player player) {
		ChunkCoordinates coords = MeteorsMod.proxy.meteorHandler.getLastCrashLocation();
		if (coords == null) return;
		ByteArrayOutputStream bos = new ByteArrayOutputStream(12);
		DataOutputStream outputStream = new DataOutputStream(bos);
		try {
			outputStream.writeInt(coords.posX);
			outputStream.writeInt(coords.posY);
			outputStream.writeInt(coords.posZ);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "MetNewCrash";
		packet.data = bos.toByteArray();
		packet.length = bos.size();
		PacketDispatcher.sendPacketToPlayer(packet, player);
	}

	private void sendNearestTimePacket(Player player) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			GhostMeteor gMeteor = MeteorsMod.proxy.meteorHandler.getNearestTimeMeteor();
			ChunkCoordinates coords = null;
			if (gMeteor != null)
				coords = new ChunkCoordinates(gMeteor.x, 0, gMeteor.z);
			else {
				return;
			}
			ByteArrayOutputStream bos = new ByteArrayOutputStream(16);
			DataOutputStream outputStream = new DataOutputStream(bos);
			try {
				outputStream.writeInt(coords.posX);
				outputStream.writeInt(coords.posY);
				outputStream.writeInt(coords.posZ);
				outputStream.writeInt(gMeteor.getRemainingTicks());
			} catch (Exception e) {
				e.printStackTrace();
			}
			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = "MetNewTime";
			packet.data = bos.toByteArray();
			packet.length = bos.size();
			PacketDispatcher.sendPacketToPlayer(packet, player);
		}
	}

	public static void sendShieldProtectUpdate(String owner) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			EnumMeteor type = (EnumMeteor)MeteorsMod.proxy.lastMeteorPrevented.get(owner);
			if (type == null) {
				MeteorsMod.log.info("type is null");
				type = EnumMeteor.METEORITE;
			}
			ByteArrayOutputStream bos = new ByteArrayOutputStream(6 + owner.getBytes().length);
			DataOutputStream outputStream = new DataOutputStream(bos);
			try {
				outputStream.writeInt(type.getID());
				Packet.writeString(owner, outputStream);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = "MetShield";
			packet.data = bos.toByteArray();
			packet.length = bos.size();
			PacketDispatcher.sendPacketToAllPlayers(packet);
		}
	}

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
	{
		if (MeteorsMod.loggable) MeteorsMod.log.info("packet received: " + packet.channel);
		if (packet.channel.equalsIgnoreCase("MetChunk"))
			handleSafeChunkUpdate(packet);
		else if (packet.channel.equalsIgnoreCase("MetSettings"))
			handleModSettings(packet);
		else if (packet.channel.equalsIgnoreCase("MetNewCrash"))
			handleNewCrash(packet);
		else if (packet.channel.equalsIgnoreCase("MetNewTime"))
			handleNewTime(packet);
		else if (packet.channel.equalsIgnoreCase("MetGhostAdd"))
			handleGhostMetAdd(packet);
		else if (packet.channel.equalsIgnoreCase("MetGhostRem"))
			handleGhostMetRemove(packet);
		else if (packet.channel.equalsIgnoreCase("MetShield"))
			handleShieldUpdate(packet);
	}

	private void handleSafeChunkUpdate(Packet250CustomPayload packet)
	{
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) { 
			DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
			int x;
			int z;
			int radius;
			boolean remove;
			String owner;
			try { x = inputStream.readInt();
			z = inputStream.readInt();
			radius = inputStream.readInt();
			remove = inputStream.readBoolean();
			owner = Packet.readString(inputStream, 16);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			MeteorsMod.proxy.meteorHandler.updateQueue.add(new ShieldData(x, z, radius, owner, remove));
		}
	}

	private void handleNewCrash(Packet250CustomPayload packet)
	{
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) { 
			DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
			int x;
			int y;
			int z;
			try { x = inputStream.readInt();
			y = inputStream.readInt();
			z = inputStream.readInt();
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			lastCrashLocation = new ChunkCoordinates(x, y, z); 
		}
	}

	private void handleNewTime(Packet250CustomPayload packet)
	{
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) { 
			DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
			int x;
			int y;
			int z;
			int tr;
			try { x = inputStream.readInt();
			y = inputStream.readInt();
			z = inputStream.readInt();
			tr = inputStream.readInt();
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			if ((x == 0) && (y == 0) && (z == 0)) {
				nearestTimeLocation = null;
				MeteorsMod.proxy.nearestTimeLeft = 0;
			} else {
				MeteorsMod.log.info("Should've received workable time location");
				nearestTimeLocation = new ChunkCoordinates(x, y, z);
				MeteorsMod.proxy.nearestTimeLeft = tr;
			} 
		}
	}

	private void handleGhostMetAdd(Packet250CustomPayload packet)
	{
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) { 
			DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
			int x;
			int y;
			int z;
			try { x = inputStream.readInt();
			y = inputStream.readInt();
			z = inputStream.readInt();
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			ChunkCoordinates coords = new ChunkCoordinates(x, y, z);
			ghostMetLocs.add(coords); 
		}
	}

	private void handleGhostMetRemove(Packet250CustomPayload packet)
	{
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) { 
			DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
			int x;
			int y;
			int z;
			try { x = inputStream.readInt();
			y = inputStream.readInt();
			z = inputStream.readInt();
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			ChunkCoordinates coords = new ChunkCoordinates(x, y, z);
			ghostMetLocs.remove(coords); 
		}
	}

	private void handleModSettings(Packet250CustomPayload packet)
	{
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			lastCrashLocation = null;
			nearestTimeLocation = null;
			ghostMetLocs = new ArrayList();
			MeteorsMod mod = MeteorsMod.instance;
			DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
			try {
				mod.allowSummonedMeteorGrief = inputStream.readBoolean();
				mod.meteorsFallOnlyAtNight = inputStream.readBoolean();
				mod.MinMeteorSize = inputStream.readInt();
				mod.MaxMeteorSize = inputStream.readInt();
				mod.ShieldRadiusMultiplier = inputStream.readInt();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void handleShieldUpdate(Packet250CustomPayload packet) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
		{
			DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
			try {
				EnumMeteor type = EnumMeteor.getTypeFromID(inputStream.readInt());
				String owner = Packet.readString(inputStream, 16);
				MeteorsMod.proxy.lastMeteorPrevented.put(owner, type);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public String connectionReceived(NetLoginHandler netHandler,
			INetworkManager manager) {
		return null;
	}

	@Override
	public void connectionOpened(NetHandler netClientHandler, String server,
			int port, INetworkManager manager) {}

	@Override
	public void connectionOpened(NetHandler netClientHandler,
			MinecraftServer server, INetworkManager manager) {}

	@Override
	public void connectionClosed(INetworkManager manager) {}

	@Override
	public void clientLoggedIn(NetHandler clientHandler, INetworkManager manager,
			Packet1Login login) {}

}