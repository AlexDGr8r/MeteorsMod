package net.meteor.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
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
	public static ArrayList<ChunkCoordinates> ghostMetLocs = new ArrayList<ChunkCoordinates>(); // TODO Privatize

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
	
	public static void sendPacketToAllInWorld(World world, Packet packet) {
		Iterator<EntityPlayer> iter = world.playerEntities.iterator();
		while (iter.hasNext()) {
			PacketDispatcher.sendPacketToPlayer(packet, (Player)iter.next());
		}
	}
	
	public static ChatMessageComponent createMessage(String s, EnumChatFormatting ecf) {
		return ChatMessageComponent.createFromText(s).setColor(ecf);
	}

	@Override
	public void playerLoggedIn(Player player, NetHandler netHandler, INetworkManager manager)
	{
		sendSettingsPacket(player);
		for (String o : MeteorsMod.proxy.lastMeteorPrevented.keySet()) {
			sendShieldProtectUpdate(o);
		}
	}
	
	@ForgeSubscribe
	public void entityJoinWorld(EntityJoinWorldEvent event) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			if (event.entity instanceof EntityPlayer) {
				Player player = (Player)event.entity;
				HandlerMeteor metHandler = MeteorsMod.proxy.metHandlers.get(event.world.provider.dimensionId);
				sendEmptyGhostMeteorPacket(player);
				metHandler.sendGhostMeteorPackets(player);
				sendLastCrashPacket(player, metHandler);
				sendNearestTimePacket(player, metHandler);
			}
		}
	}
	
	private void sendEmptyGhostMeteorPacket(Player player) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(12);
		DataOutputStream outputStream = new DataOutputStream(bos);
		try {
			outputStream.writeInt(-1);
			outputStream.writeInt(-1);
			outputStream.writeInt(-1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "MetGhostRem";
		packet.data = bos.toByteArray();
		packet.length = bos.size();
		PacketDispatcher.sendPacketToPlayer(packet, player);
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

	private void sendLastCrashPacket(Player player, HandlerMeteor handler) {
		ChunkCoordinates coords = handler.getLastCrashLocation();
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

	private void sendNearestTimePacket(Player player, HandlerMeteor handler) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			GhostMeteor gMeteor = handler.getNearestTimeMeteor();
			ChunkCoordinates coords = null;
			if (gMeteor != null)
				coords = new ChunkCoordinates(gMeteor.x, 0, gMeteor.z);
			else {
				return;
			}
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
				//MeteorsMod.log.info("type is null");
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
		if (packet.channel.equalsIgnoreCase("MetSettings")) {
			handleModSettings(packet);
		} else if (packet.channel.equalsIgnoreCase("MetNewCrash")) {
			handleNewCrash(packet);
		} else if (packet.channel.equalsIgnoreCase("MetNewTime")) {
			handleNewTime(packet);
		} else if (packet.channel.equalsIgnoreCase("MetGhostAdd")) {
			handleGhostMetAdd(packet);
		} else if (packet.channel.equalsIgnoreCase("MetGhostRem")) {
			handleGhostMetRemove(packet);
		} else if (packet.channel.equalsIgnoreCase("MetShield")) {
			handleShieldUpdate(packet);
		}
	}

	private void handleNewCrash(Packet250CustomPayload packet)
	{
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) { 
			DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
			int x;
			int y;
			int z;
			try { 
				x = inputStream.readInt();
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
			try { 
				x = inputStream.readInt();
				y = inputStream.readInt();
				z = inputStream.readInt();
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			if ((x == 0) && (y == 0) && (z == 0)) {
				nearestTimeLocation = null;
			} else {
				nearestTimeLocation = new ChunkCoordinates(x, y, z);
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
			try { 
				x = inputStream.readInt();
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
			try { 
				x = inputStream.readInt();
				y = inputStream.readInt();
				z = inputStream.readInt();
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			ChunkCoordinates coords = new ChunkCoordinates(x, y, z);
			if (coords.posX == -1 && coords.posY == -1 && coords.posZ == -1) {
				ghostMetLocs.clear();
			} else {
				for (int i = 0; i < ghostMetLocs.size(); i++) {
					ChunkCoordinates cc = ghostMetLocs.get(i);
					if (cc.posX == coords.posX && cc.posZ == coords.posZ) {
						ghostMetLocs.remove(i);
						break;
					}
				}
			}
		}
	}

	private void handleModSettings(Packet250CustomPayload packet)
	{
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
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