package net.meteor.common;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.network.packet.Packet3Chat;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class HandlerMeteor
{
	private WorldServer theWorld;
	private String ghostMetsFilePath;
	private String crashedChunksFilePath;
	private String folderPath;
	private File worldDir;
	private ArrayList<GhostMeteor> ghostMets = new ArrayList<GhostMeteor>();
	private ArrayList<CrashedChunkSet> crashedChunks = new ArrayList<CrashedChunkSet>();

	public List<ChunkCoordIntPair> safeChunks = new ArrayList<ChunkCoordIntPair>();
	public List<SafeChunkCoordsIntPair> safeChunksWithOwners = new ArrayList<SafeChunkCoordsIntPair>();
	public List<ShieldData> shieldData = new ArrayList<ShieldData>();
	public List<ShieldData> updateQueue = new ArrayList<ShieldData>();
	private static HandlerMeteor instance;
	private static Random random = new Random();
	public static EnumMeteor defaultType;

	@ForgeSubscribe
	public void onWorldLoad(WorldEvent.Load event)
	{
		if (!event.world.provider.isSurfaceWorld()) return;
		if (!(event.world instanceof WorldServer)) return;
		instance = this;
		MeteorsMod.instance.setClientStartConfig();
		this.theWorld = (WorldServer) event.world;
		MinecraftServer server = this.theWorld.getMinecraftServer();
		this.worldDir = getWorldSaveLocation(this.theWorld);
		this.folderPath = this.worldDir.getAbsolutePath();
		this.ghostMetsFilePath = (this.folderPath + File.separator + "ghostMets.bin");
		this.crashedChunksFilePath = (this.folderPath + File.separator + "crashedMetChunks.bin");
		this.ghostMets = loadGhostMeteors();
		this.crashedChunks = loadCrashedChunks();
	}

	@SuppressWarnings("resource")
	private ArrayList<GhostMeteor> loadGhostMeteors() {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(this.ghostMetsFilePath));
			Object result = ois.readObject();
			return (ArrayList<GhostMeteor>)result;
		}
		catch (Exception e) {
		}
		return new ArrayList<GhostMeteor>();
	}

	private void saveGhostMeteors() {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(this.ghostMetsFilePath));
			oos.writeObject(this.ghostMets);
			oos.flush();
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("resource")
	private ArrayList loadCrashedChunks() {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(this.crashedChunksFilePath));
			Object result = ois.readObject();
			return (ArrayList)result;
		}
		catch (Exception e) {
		}
		return new ArrayList();
	}

	private void saveCrashedChunks() {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(this.crashedChunksFilePath));
			oos.writeObject(this.crashedChunks);
			oos.flush();
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static File getWorldSaveLocation(World world)
	{
		ISaveHandler worldsaver = world.getSaveHandler();
		IChunkLoader loader = worldsaver.getChunkLoader(world.provider);
		if ((loader instanceof AnvilChunkLoader)) {
			return ((AnvilChunkLoader)loader).chunkSaveLocation;
		}

		return null;
	}

	@ForgeSubscribe
	public void onWorldSave(WorldEvent.Save event) {
		if (!event.world.provider.isSurfaceWorld()) return;
		if (!(event.world instanceof WorldServer)) return;
		saveGhostMeteors();
		saveCrashedChunks();
	}

	public void updateMeteors() {
		if (this.theWorld == null) {
			return;
		}

		if (!this.updateQueue.isEmpty()) {
			ShieldData d = (ShieldData)this.updateQueue.get(0);
			if (d.tbr)
				net_removeSafeChunks(d.x, d.z, d.radius, d.owner);
			else {
				net_addSafeChunks(d.x, d.z, d.radius, d.owner);
			}
			this.updateQueue.remove(Integer.valueOf(0));
		}

		for (int i = 0; i < this.ghostMets.size(); i++)
			if ((!this.theWorld.isDaytime()) || (!MeteorsMod.instance.meteorsFallOnlyAtNight)) {
				GhostMeteor gMeteor = (GhostMeteor)this.ghostMets.get(i);
				ChunkCoordIntPair coords = this.theWorld.getChunkFromBlockCoords(gMeteor.x, gMeteor.z).getChunkCoordIntPair();
				if (!canSpawnNewMeteorAt(coords)) {
					sendGhostMeteorRemovePacket(gMeteor);
					this.ghostMets.remove(i);
					updateNearestTimeForClients();
				}
				else {
					gMeteor.update();
					if (gMeteor.ready) {
						if (meteorInProtectedZone(gMeteor.x, gMeteor.z)) {
							List safeCoords = getSafeChunkCoords(gMeteor.x, gMeteor.z);
							for (int j = 0; j < safeCoords.size(); j++) {
								SafeChunkCoordsIntPair sc = (SafeChunkCoordsIntPair)safeCoords.get(j);
								MeteorsMod.proxy.meteorProtectCheck(sc.getOwner());
								MeteorsMod.proxy.lastMeteorPrevented.put(sc.getOwner(), gMeteor.type);
								ClientHandler.sendShieldProtectUpdate(sc.getOwner());
							}
						} else if (gMeteor.type == EnumMeteor.KITTY) {
							kittyAttack();
						} else {
							EntityMeteor meteor = new EntityMeteor(this.theWorld, gMeteor.size, gMeteor.x, gMeteor.z, gMeteor.type, false);
							this.theWorld.spawnEntityInWorld(meteor);
							applyMeteorCrash(gMeteor.x, this.theWorld.getFirstUncoveredBlock(gMeteor.x, gMeteor.z), gMeteor.z);
							MeteorsMod.proxy.playCrashSound(this.theWorld, meteor);
						}
						sendGhostMeteorRemovePacket(gMeteor);
						this.ghostMets.remove(i);
						updateNearestTimeForClients();
					}
				}
			}
	}

	public void kittyAttack() {
		this.theWorld.getMinecraftServer().getConfigurationManager().sendPacketToAllPlayers(new Packet3Chat("§4Comet Kitties incoming!!!")); // TODO
		for (int i = 0; i < this.theWorld.playerEntities.size(); i++) {
			EntityPlayer player = (EntityPlayer) this.theWorld.playerEntities.get(i);
			if (player != null)
				for (int r = random.nextInt(64) + 50; r >= 0; r--) {
					int x = random.nextInt(64);
					int z = random.nextInt(64);
					if (random.nextBoolean()) x = -x;
					if (random.nextBoolean()) z = -z;
					x = (int)(x + player.posX);
					z = (int)(z + player.posZ);
					EntityMeteor fKitty = new EntityMeteor(this.theWorld, 1, x, z, EnumMeteor.KITTY, false);
					fKitty.spawnPauseTicks = random.nextInt(100);
					this.theWorld.spawnEntityInWorld(fKitty);
				}
		}
	}

	public void applyMeteorCrash(int x, int y, int z) {
		ArrayList tbr = new ArrayList();

		for (int i = 0; i < this.crashedChunks.size(); i++) {
			CrashedChunkSet set = (CrashedChunkSet)this.crashedChunks.get(i);
			set.age += 1;
			if (set.age >= 20) {
				tbr.add(set);
			}
		}
		for (int i = 0; i < tbr.size(); i++) {
			this.crashedChunks.remove(tbr.get(i));
		}

		ChunkCoordIntPair coords = this.theWorld.getChunkFromBlockCoords(x, z).getChunkCoordIntPair();
		this.crashedChunks.add(new CrashedChunkSet(coords.chunkXPos, coords.chunkZPos, x, y, z));

		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream(12);
			DataOutputStream outputStream = new DataOutputStream(bos);
			try {
				outputStream.writeInt(x);
				outputStream.writeInt(y);
				outputStream.writeInt(z);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = "MetNewCrash";
			packet.data = bos.toByteArray();
			packet.length = bos.size();
			PacketDispatcher.sendPacketToAllPlayers(packet);

			if (MeteorsMod.instance.textNotifyCrash)
				this.theWorld.getMinecraftServer().getConfigurationManager().sendPacketToAllPlayers(new Packet3Chat("A Meteor has Crashed!")); // TODO
		}
	}

	public boolean canSpawnNewMeteor()
	{
		return this.ghostMets.size() < 3;
	}

	public boolean canSpawnNewMeteorAt(ChunkCoordIntPair coords) {
		for (int i = 0; i < this.crashedChunks.size(); i++) {
			if (this.crashedChunks.get(i).containsChunk(coords)) {
				return false;
			}
		}
		return true;
	}

	private boolean meteorInProtectedZone(int x, int z) {
		Chunk chunk = this.theWorld.getChunkFromBlockCoords(x, z);
		return this.safeChunks.contains(new ChunkCoordIntPair(chunk.xPosition, chunk.zPosition));
	}

	public List getSafeChunkCoords(int x, int z) {
		if (meteorInProtectedZone(x, z)) {
			List cList = new ArrayList();
			List owners = new ArrayList();
			Chunk chunk = this.theWorld.getChunkFromBlockCoords(x, z);
			Iterator iter = this.safeChunksWithOwners.iterator();
			while (iter.hasNext()) {
				SafeChunkCoordsIntPair coords = (SafeChunkCoordsIntPair)iter.next();
				if ((coords.hasCoords(chunk.xPosition, chunk.zPosition)) && (!owners.contains(coords.getOwner()))) {
					cList.add(coords);
					owners.add(coords.getOwner());
				}
			}
			return cList;
		}
		return null;
	}

	public void readyNewMeteor(int x, int z, int size, int tGoal, EnumMeteor type) {
		if (canSpawnNewMeteor()) {
			GhostMeteor gMeteor = new GhostMeteor(x, z, size, tGoal, type);
			this.ghostMets.add(gMeteor);
			sendGhostMeteorAddPacket(gMeteor);
			updateNearestTimeForClients();
			if (type == EnumMeteor.KITTY) {
				this.theWorld.getMinecraftServer().getConfigurationManager().sendPacketToAllPlayers(new Packet3Chat("§4" + LangLocalization.get("Meteor.kittiesDetected.one")));
				this.theWorld.getMinecraftServer().getConfigurationManager().sendPacketToAllPlayers(new Packet3Chat("§4" + LangLocalization.get("Meteor.kittiesDetected.two")));
			}
		}
	}

	public GhostMeteor getNearestTimeMeteor() {
		if (this.theWorld == null) return null;
		GhostMeteor closestMeteor = null;
		for (int i = 0; i < this.ghostMets.size(); i++) {
			if (closestMeteor != null) {
				if (((GhostMeteor)this.ghostMets.get(i)).type != EnumMeteor.KITTY) {
					int var1 = closestMeteor.getRemainingTicks();
					int var2 = ((GhostMeteor)this.ghostMets.get(i)).getRemainingTicks();
					if (var2 < var1)
						closestMeteor = (GhostMeteor)this.ghostMets.get(i);
				}
			} else if (((GhostMeteor)this.ghostMets.get(i)).type != EnumMeteor.KITTY) {
				closestMeteor = (GhostMeteor)this.ghostMets.get(i);
			}

		}

		return closestMeteor;
	}

	private void updateNearestTimeForClients() {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			GhostMeteor gMeteor = getNearestTimeMeteor();
			ChunkCoordinates coords = null;
			if (gMeteor != null) {
				coords = new ChunkCoordinates(gMeteor.x, this.theWorld.getFirstUncoveredBlock(gMeteor.x, gMeteor.z), gMeteor.z);
			}
			if (coords == null) coords = new ChunkCoordinates(0, 0, 0);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(16);
			DataOutputStream outputStream = new DataOutputStream(bos);
			try {
				outputStream.writeInt(coords.posX);
				outputStream.writeInt(coords.posY);
				outputStream.writeInt(coords.posZ);
				outputStream.writeInt(gMeteor != null ? gMeteor.getRemainingTicks() : 0);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = "MetNewTime";
			packet.data = bos.toByteArray();
			packet.length = bos.size();
			PacketDispatcher.sendPacketToAllPlayers(packet);
		}
	}

	public ChunkCoordinates getNearestIncomingMeteor(EntityPlayer player) {
		if (this.theWorld == null) return null;
		GhostMeteor closestMeteor = null;
		for (int i = 0; i < this.ghostMets.size(); i++) {
			if (closestMeteor != null) {
				if (((GhostMeteor)this.ghostMets.get(i)).type != EnumMeteor.KITTY) {
					double pX = player.posX;
					double pY = player.posY;
					double pZ = player.posZ;
					GhostMeteor meteor = (GhostMeteor)this.ghostMets.get(i);
					double var1 = getDistance(pX, pY, pZ, meteor.x, this.theWorld.getFirstUncoveredBlock(meteor.x, meteor.z), meteor.z);
					double var2 = getDistance(pX, pY, pZ, closestMeteor.x, this.theWorld.getFirstUncoveredBlock(closestMeteor.x, closestMeteor.z), closestMeteor.z);
					if (var1 < var2)
						closestMeteor = meteor;
				}
			} else if (((GhostMeteor)this.ghostMets.get(i)).type != EnumMeteor.KITTY) {
				closestMeteor = (GhostMeteor)this.ghostMets.get(i);
			}
		}

		if (closestMeteor != null) {
			return new ChunkCoordinates(closestMeteor.x, this.theWorld.getFirstUncoveredBlock(closestMeteor.x, closestMeteor.z), closestMeteor.z);
		}
		return null;
	}

	private double getDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
		double var7 = x1 - x2;
		double var9 = y1 - y2;
		double var11 = z1 - z2;
		return MathHelper.sqrt_double(var7 * var7 + var9 * var9 + var11 * var11);
	}

	public ChunkCoordinates getLastCrashLocation() {
		if (this.theWorld == null) return null;
		for (int i = 0; i < this.crashedChunks.size(); i++) {
			if (((CrashedChunkSet)this.crashedChunks.get(i)).age == 0) {
				return ((CrashedChunkSet)this.crashedChunks.get(i)).getCrashCoords();
			}
		}
		return null;
	}

	public static HandlerMeteor getInstance() {
		return instance;
	}

	public static int getMeteorSize() {
		int r = random.nextInt(26);
		int maxSize = MeteorsMod.instance.MaxMeteorSize;
		int minSize = MeteorsMod.instance.MinMeteorSize;
		if ((maxSize == 3) && (minSize == 1)) {
			if (r == 25)
				return 3;
			if (r > 15) {
				return 2;
			}
			return 1;
		}
		if ((maxSize == 3) && (minSize == 2)) {
			if (r > 15) {
				return 3;
			}
			return 2;
		}
		if ((minSize == 1) && (maxSize == 2)) {
			if (r > 15) {
				return 2;
			}
			return 1;
		}

		return minSize;
	}

	public static EnumMeteor getMeteorType() {
		int r = random.nextInt(63);
		MeteorsMod mod = MeteorsMod.instance;
		if ((r >= 60) && (mod.unknownEnabled))
			return EnumMeteor.UNKNOWN;
		if ((r >= 52) && (mod.kreknoriteEnabled))
			return EnumMeteor.KREKNORITE;
		if ((r >= 35) && (mod.frezariteEnabled)) {
			return EnumMeteor.FREZARITE;
		}
		return defaultType;
	}

	@SideOnly(Side.CLIENT)
	public void net_addSafeChunks(int x, int z, int radius, String shieldOwner)
	{
		if (radius < 0) return;
		if (radius == 0) {
			SafeChunkCoordsIntPair sPair = new SafeChunkCoordsIntPair(x, z, shieldOwner);
			this.safeChunksWithOwners.add(sPair);
			this.safeChunks.add(new ChunkCoordIntPair(x, z));
			return;
		}
		for (int sX = x - radius; sX <= x + radius; sX++) {
			SafeChunkCoordsIntPair sPair = new SafeChunkCoordsIntPair(sX, z, shieldOwner);
			this.safeChunksWithOwners.add(sPair);
			this.safeChunks.add(new ChunkCoordIntPair(sX, z));
		}
		for (int sZ = z - radius; sZ <= z + radius; sZ++) {
			SafeChunkCoordsIntPair sPair = new SafeChunkCoordsIntPair(x, sZ, shieldOwner);
			this.safeChunksWithOwners.add(sPair);
			this.safeChunks.add(new ChunkCoordIntPair(x, sZ));
		}
		if (radius <= 1) return;

		int max = x + (z + radius);
		for (int sX = x + radius - 1; sX >= x + 1; sX--) {
			for (int sZ = z + radius - 1; sZ >= z + 1; sZ--) {
				if (sX + sZ <= max) {
					SafeChunkCoordsIntPair sPair = new SafeChunkCoordsIntPair(sX, sZ, shieldOwner);
					this.safeChunksWithOwners.add(sPair);
					this.safeChunks.add(new ChunkCoordIntPair(sX, sZ));
				}
			}
		}

		max = x + (z - radius);
		for (int sX = x - radius + 1; sX <= x - 1; sX++) {
			for (int sZ = z - radius + 1; sZ <= z - 1; sZ++) {
				if (sX + sZ >= max) {
					SafeChunkCoordsIntPair sPair = new SafeChunkCoordsIntPair(sX, sZ, shieldOwner);
					this.safeChunksWithOwners.add(sPair);
					this.safeChunks.add(new ChunkCoordIntPair(sX, sZ));
				}
			}
		}

		max = z + radius - x;
		for (int sZ = z + radius - 1; sZ >= z + 1; sZ--) {
			for (int sX = x - radius + 1; sX <= x - 1; sX++) {
				if (sZ - sX <= max) {
					SafeChunkCoordsIntPair sPair = new SafeChunkCoordsIntPair(sX, sZ, shieldOwner);
					this.safeChunksWithOwners.add(sPair);
					this.safeChunks.add(new ChunkCoordIntPair(sX, sZ));
				}
			}
		}

		max = x + radius - z;
		for (int sZ = z - radius + 1; sZ <= z - 1; sZ++)
			for (int sX = x + radius - 1; sX >= x + 1; sX--)
				if (sX - sZ <= max) {
					SafeChunkCoordsIntPair sPair = new SafeChunkCoordsIntPair(sX, sZ, shieldOwner);
					this.safeChunksWithOwners.add(sPair);
					this.safeChunks.add(new ChunkCoordIntPair(sX, sZ));
				}
	}

	public void addSafeChunks(int x, int z, int radius, String shieldOwner)
	{
		sendAddChunkPacket(x, z, radius, shieldOwner);
		this.shieldData.add(new ShieldData(x, z, radius, shieldOwner, false));
		if (radius < 0) return;
		if (radius == 0) {
			SafeChunkCoordsIntPair sPair = new SafeChunkCoordsIntPair(x, z, shieldOwner);
			this.safeChunksWithOwners.add(sPair);
			this.safeChunks.add(new ChunkCoordIntPair(x, z));
			return;
		}
		for (int sX = x - radius; sX <= x + radius; sX++) {
			SafeChunkCoordsIntPair sPair = new SafeChunkCoordsIntPair(sX, z, shieldOwner);
			this.safeChunksWithOwners.add(sPair);
			this.safeChunks.add(new ChunkCoordIntPair(sX, z));
		}
		for (int sZ = z - radius; sZ <= z + radius; sZ++) {
			SafeChunkCoordsIntPair sPair = new SafeChunkCoordsIntPair(x, sZ, shieldOwner);
			this.safeChunksWithOwners.add(sPair);
			this.safeChunks.add(new ChunkCoordIntPair(x, sZ));
		}
		if (radius <= 1) return;

		int max = x + (z + radius);
		for (int sX = x + radius - 1; sX >= x + 1; sX--) {
			for (int sZ = z + radius - 1; sZ >= z + 1; sZ--) {
				if (sX + sZ <= max) {
					SafeChunkCoordsIntPair sPair = new SafeChunkCoordsIntPair(sX, sZ, shieldOwner);
					this.safeChunksWithOwners.add(sPair);
					this.safeChunks.add(new ChunkCoordIntPair(sX, sZ));
				}
			}
		}

		max = x + (z - radius);
		for (int sX = x - radius + 1; sX <= x - 1; sX++) {
			for (int sZ = z - radius + 1; sZ <= z - 1; sZ++) {
				if (sX + sZ >= max) {
					SafeChunkCoordsIntPair sPair = new SafeChunkCoordsIntPair(sX, sZ, shieldOwner);
					this.safeChunksWithOwners.add(sPair);
					this.safeChunks.add(new ChunkCoordIntPair(sX, sZ));
				}
			}
		}

		max = z + radius - x;
		for (int sZ = z + radius - 1; sZ >= z + 1; sZ--) {
			for (int sX = x - radius + 1; sX <= x - 1; sX++) {
				if (sZ - sX <= max) {
					SafeChunkCoordsIntPair sPair = new SafeChunkCoordsIntPair(sX, sZ, shieldOwner);
					this.safeChunksWithOwners.add(sPair);
					this.safeChunks.add(new ChunkCoordIntPair(sX, sZ));
				}
			}
		}

		max = x + radius - z;
		for (int sZ = z - radius + 1; sZ <= z - 1; sZ++)
			for (int sX = x + radius - 1; sX >= x + 1; sX--)
				if (sX - sZ <= max) {
					SafeChunkCoordsIntPair sPair = new SafeChunkCoordsIntPair(sX, sZ, shieldOwner);
					this.safeChunksWithOwners.add(sPair);
					this.safeChunks.add(new ChunkCoordIntPair(sX, sZ));
				}
	}

	@SideOnly(Side.CLIENT)
	public void net_removeSafeChunks(int x, int z, int radius, String shieldOwner)
	{
		if (shieldOwner == null) MeteorsMod.log.info("String was null at net_removeSafeChunks");
		if (radius < 0) return;
		if (radius == 0) {
			SafeChunkCoordsIntPair sPair = new SafeChunkCoordsIntPair(x, z, shieldOwner);
			this.safeChunksWithOwners.remove(sPair);
			this.safeChunks.remove(new ChunkCoordIntPair(x, z));
			return;
		}
		for (int sX = x - radius; sX <= x + radius; sX++) {
			SafeChunkCoordsIntPair sPair = new SafeChunkCoordsIntPair(sX, z, shieldOwner);
			this.safeChunksWithOwners.remove(sPair);
			this.safeChunks.remove(new ChunkCoordIntPair(sX, z));
		}
		for (int sZ = z - radius; sZ <= z + radius; sZ++) {
			SafeChunkCoordsIntPair sPair = new SafeChunkCoordsIntPair(x, sZ, shieldOwner);
			this.safeChunksWithOwners.remove(sPair);
			this.safeChunks.remove(new ChunkCoordIntPair(x, sZ));
		}
		if (radius <= 1) return;

		int max = x + (z + radius);
		for (int sX = x + radius - 1; sX >= x + 1; sX--) {
			for (int sZ = z + radius - 1; sZ >= z + 1; sZ--) {
				if (sX + sZ <= max) {
					SafeChunkCoordsIntPair sPair = new SafeChunkCoordsIntPair(sX, sZ, shieldOwner);
					this.safeChunksWithOwners.remove(sPair);
					this.safeChunks.remove(new ChunkCoordIntPair(sX, sZ));
				}
			}
		}

		max = x + (z - radius);
		for (int sX = x - radius + 1; sX <= x - 1; sX++) {
			for (int sZ = z - radius + 1; sZ <= z - 1; sZ++) {
				if (sX + sZ >= max) {
					SafeChunkCoordsIntPair sPair = new SafeChunkCoordsIntPair(sX, sZ, shieldOwner);
					this.safeChunksWithOwners.remove(sPair);
					this.safeChunks.remove(new ChunkCoordIntPair(sX, sZ));
				}
			}
		}

		max = z + radius - x;
		for (int sZ = z + radius - 1; sZ >= z + 1; sZ--) {
			for (int sX = x - radius + 1; sX <= x - 1; sX++) {
				if (sZ - sX <= max) {
					SafeChunkCoordsIntPair sPair = new SafeChunkCoordsIntPair(sX, sZ, shieldOwner);
					this.safeChunksWithOwners.remove(sPair);
					this.safeChunks.remove(new ChunkCoordIntPair(sX, sZ));
				}
			}
		}

		max = x + radius - z;
		for (int sZ = z - radius + 1; sZ <= z - 1; sZ++)
			for (int sX = x + radius - 1; sX >= x + 1; sX--)
				if (sX - sZ <= max) {
					SafeChunkCoordsIntPair sPair = new SafeChunkCoordsIntPair(sX, sZ, shieldOwner);
					this.safeChunksWithOwners.remove(sPair);
					this.safeChunks.remove(new ChunkCoordIntPair(sX, sZ));
				}
	}

	public void removeSafeChunks(int x, int z, int radius, String shieldOwner)
	{
		sendRemoveChunkPacket(x, z, radius, shieldOwner);
		this.shieldData.remove(new ShieldData(x, z, radius, shieldOwner, false));
		if (shieldOwner == null) MeteorsMod.log.info("String was null at removeSafeChunks");
		if (radius < 0) return;
		if (radius == 0) {
			SafeChunkCoordsIntPair sPair = new SafeChunkCoordsIntPair(x, z, shieldOwner);
			this.safeChunksWithOwners.remove(sPair);
			this.safeChunks.remove(new ChunkCoordIntPair(x, z));
			return;
		}
		for (int sX = x - radius; sX <= x + radius; sX++) {
			SafeChunkCoordsIntPair sPair = new SafeChunkCoordsIntPair(sX, z, shieldOwner);
			this.safeChunksWithOwners.remove(sPair);
			this.safeChunks.remove(new ChunkCoordIntPair(sX, z));
		}
		for (int sZ = z - radius; sZ <= z + radius; sZ++) {
			SafeChunkCoordsIntPair sPair = new SafeChunkCoordsIntPair(x, sZ, shieldOwner);
			this.safeChunksWithOwners.remove(sPair);
			this.safeChunks.remove(new ChunkCoordIntPair(x, sZ));
		}
		if (radius <= 1) return;

		int max = x + (z + radius);
		for (int sX = x + radius - 1; sX >= x + 1; sX--) {
			for (int sZ = z + radius - 1; sZ >= z + 1; sZ--) {
				if (sX + sZ <= max) {
					SafeChunkCoordsIntPair sPair = new SafeChunkCoordsIntPair(sX, sZ, shieldOwner);
					this.safeChunksWithOwners.remove(sPair);
					this.safeChunks.remove(new ChunkCoordIntPair(sX, sZ));
				}
			}
		}

		max = x + (z - radius);
		for (int sX = x - radius + 1; sX <= x - 1; sX++) {
			for (int sZ = z - radius + 1; sZ <= z - 1; sZ++) {
				if (sX + sZ >= max) {
					SafeChunkCoordsIntPair sPair = new SafeChunkCoordsIntPair(sX, sZ, shieldOwner);
					this.safeChunksWithOwners.remove(sPair);
					this.safeChunks.remove(new ChunkCoordIntPair(sX, sZ));
				}
			}
		}

		max = z + radius - x;
		for (int sZ = z + radius - 1; sZ >= z + 1; sZ--) {
			for (int sX = x - radius + 1; sX <= x - 1; sX++) {
				if (sZ - sX <= max) {
					SafeChunkCoordsIntPair sPair = new SafeChunkCoordsIntPair(sX, sZ, shieldOwner);
					this.safeChunksWithOwners.remove(sPair);
					this.safeChunks.remove(new ChunkCoordIntPair(sX, sZ));
				}
			}
		}

		max = x + radius - z;
		for (int sZ = z - radius + 1; sZ <= z - 1; sZ++)
			for (int sX = x + radius - 1; sX >= x + 1; sX--)
				if (sX - sZ <= max) {
					SafeChunkCoordsIntPair sPair = new SafeChunkCoordsIntPair(sX, sZ, shieldOwner);
					this.safeChunksWithOwners.remove(sPair);
					this.safeChunks.remove(new ChunkCoordIntPair(sX, sZ));
				}
	}

	private void sendAddChunkPacket(int x, int z, int radius, String shieldOwner)
	{
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			int size = 15 + shieldOwner.getBytes().length;
			ByteArrayOutputStream bos = new ByteArrayOutputStream(size);
			DataOutputStream outputStream = new DataOutputStream(bos);
			try {
				outputStream.writeInt(x);
				outputStream.writeInt(z);
				outputStream.writeInt(radius);
				outputStream.writeBoolean(false);
				Packet.writeString(shieldOwner, outputStream);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = "MetChunk";
			packet.data = bos.toByteArray();
			packet.length = bos.size();
			PacketDispatcher.sendPacketToAllPlayers(packet);
		}
	}

	private void sendRemoveChunkPacket(int x, int z, int radius, String shieldOwner) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			int size = 15 + shieldOwner.getBytes().length;
			ByteArrayOutputStream bos = new ByteArrayOutputStream(size);
			DataOutputStream outputStream = new DataOutputStream(bos);
			try {
				outputStream.writeInt(x);
				outputStream.writeInt(z);
				outputStream.writeInt(radius);
				outputStream.writeBoolean(true);
				Packet.writeString(shieldOwner, outputStream);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = "MetChunk";
			packet.data = bos.toByteArray();
			packet.length = bos.size();
			PacketDispatcher.sendPacketToAllPlayers(packet);
		}
	}

	public void sendGhostMeteorPackets(Player player) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			ArrayList mets = this.ghostMets;
			for (int i = 0; i < mets.size(); i++) {
				GhostMeteor met = (GhostMeteor)mets.get(i);
				ByteArrayOutputStream bos = new ByteArrayOutputStream(12);
				DataOutputStream outputStream = new DataOutputStream(bos);
				try {
					outputStream.writeInt(met.x);
					outputStream.writeInt(this.theWorld.getFirstUncoveredBlock(met.x, met.z));
					outputStream.writeInt(met.z);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Packet250CustomPayload packet = new Packet250CustomPayload();
				packet.channel = "MetGhostAdd";
				packet.data = bos.toByteArray();
				packet.length = bos.size();
				PacketDispatcher.sendPacketToPlayer(packet, player);
			}
		}
	}

	private void sendGhostMeteorAddPacket(GhostMeteor met) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream(12);
			DataOutputStream outputStream = new DataOutputStream(bos);
			try {
				outputStream.writeInt(met.x);
				outputStream.writeInt(this.theWorld.getFirstUncoveredBlock(met.x, met.z));
				outputStream.writeInt(met.z);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = "MetGhostAdd";
			packet.data = bos.toByteArray();
			packet.length = bos.size();
			PacketDispatcher.sendPacketToAllPlayers(packet);
		}
	}

	private void sendGhostMeteorRemovePacket(GhostMeteor met) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream(12);
			DataOutputStream outputStream = new DataOutputStream(bos);
			try {
				outputStream.writeInt(met.x);
				outputStream.writeInt(this.theWorld.getFirstUncoveredBlock(met.x, met.z));
				outputStream.writeInt(met.z);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = "MetGhostRem";
			packet.data = bos.toByteArray();
			packet.length = bos.size();
			PacketDispatcher.sendPacketToAllPlayers(packet);
		}
	}
}