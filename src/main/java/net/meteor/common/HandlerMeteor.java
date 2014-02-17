package net.meteor.common;

import ibxm.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.meteor.common.entity.EntityMeteor;
import net.meteor.common.packets.PacketGhostMeteor;
import net.meteor.common.packets.PacketLastCrash;
import net.meteor.common.packets.PacketShieldUpdate;
import net.meteor.common.packets.PacketSoonestMeteor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.WorldEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class HandlerMeteor
{
	private WorldServer theWorld;
	private String worldName;
	private HandlerMeteorTick tickHandler;
	private GhostMeteorData gMetData;
	private CrashedChunkSetData ccSetData;
	
	private static Random random = new Random();
	
	public ArrayList<GhostMeteor> ghostMets = new ArrayList<GhostMeteor>();
	public ArrayList<CrashedChunkSet> crashedChunks = new ArrayList<CrashedChunkSet>();
	public List<ChunkCoordIntPair> safeChunks = new ArrayList<ChunkCoordIntPair>();
	public List<SafeChunkCoordsIntPair> safeChunksWithOwners = new ArrayList<SafeChunkCoordsIntPair>();
	
	public static EnumMeteor defaultType;

	public HandlerMeteor(WorldEvent.Load event) {
		if (!(event.world instanceof WorldServer) || event.world.provider.dimensionId != 0) {
			return;
		}
		MeteorsMod.instance.setClientStartConfig();
		this.theWorld = (WorldServer) event.world;
		this.gMetData = GhostMeteorData.forWorld(theWorld, this);
		this.ccSetData = CrashedChunkSetData.forWorld(theWorld, this);
		this.worldName = theWorld.getWorldInfo().getWorldName();
		this.tickHandler = new HandlerMeteorTick(this, worldName);
		FMLCommonHandler.instance().bus().register(tickHandler);
		//TickRegistry.registerTickHandler(this.tickHandler, Side.SERVER); TODO
	}

	public void updateMeteors() {
		if ((this.theWorld == null) || this.theWorld.provider.dimensionId != 0) {
			return;
		}

		for (int i = 0; i < this.ghostMets.size(); i++) {
			if (theWorld.getWorldTime() % 24000L >= 12000L || (!MeteorsMod.instance.meteorsFallOnlyAtNight)) {
				GhostMeteor gMeteor = (GhostMeteor)this.ghostMets.get(i);
				ChunkCoordIntPair coords = this.theWorld.getChunkFromBlockCoords(gMeteor.x, gMeteor.z).getChunkCoordIntPair();
				if (!canSpawnNewMeteorAt(coords)) {
					sendGhostMeteorRemovePacket(gMeteor);
					this.ghostMets.remove(i);
					updateNearestTimeForClients();
				} else {
					gMeteor.update();
					if (gMeteor.ready) {
						if (meteorInProtectedZone(gMeteor.x, gMeteor.z)) {
							List<SafeChunkCoordsIntPair> safeCoords = getSafeChunkCoords(gMeteor.x, gMeteor.z);
							for (int j = 0; j < safeCoords.size(); j++) {
								SafeChunkCoordsIntPair sc = safeCoords.get(j);
								EntityPlayer player = theWorld.func_73046_m().getConfigurationManager().getPlayerForUsername(sc.getOwner());
								if (player != null) {
									player.addChatMessage(ClientHandler.createMessage(LangLocalization.get("MeteorShield.meteorBlocked"), EnumChatFormatting.GREEN));
									player.addStat(HandlerAchievement.meteorBlocked, 1);
								}
								MeteorsMod.proxy.lastMeteorPrevented.put(sc.getOwner(), gMeteor.type);
								MeteorsMod.packetPipeline.sendToAll(new PacketShieldUpdate(sc.getOwner()));
							}
						} else if (gMeteor.type == EnumMeteor.KITTY) {
							kittyAttack();
						} else {
							EntityMeteor meteor = new EntityMeteor(this.theWorld, gMeteor.size, gMeteor.x, gMeteor.z, gMeteor.type, false);
							this.theWorld.spawnEntityInWorld(meteor);
							applyMeteorCrash(gMeteor.x, this.theWorld.getTopSolidOrLiquidBlock(gMeteor.x, gMeteor.z), gMeteor.z);
							playCrashSound(meteor);
						}
						sendGhostMeteorRemovePacket(gMeteor);
						this.ghostMets.remove(i);
						updateNearestTimeForClients();
					}
				}
			}
		}
	}

	public void kittyAttack() {
		theWorld.func_73046_m().getConfigurationManager().sendChatMsg(ClientHandler.createMessage(LangLocalization.get("Meteor.kittiesIncoming"), EnumChatFormatting.DARK_RED));
		for (int i = 0; i < this.theWorld.playerEntities.size(); i++) {
			EntityPlayer player = (EntityPlayer) this.theWorld.playerEntities.get(i);
			if (player != null) {
				for (int r = random.nextInt(64) + 50; r >= 0; r--) {
					int x = random.nextInt(64);
					int z = random.nextInt(64);
					if (random.nextBoolean()) x = -x;
					if (random.nextBoolean()) z = -z;
					x = (int)(x + player.posX);
					z = (int)(z + player.posZ);
					if (meteorInProtectedZone(x, z)) {
						List<SafeChunkCoordsIntPair> safeCoords = getSafeChunkCoords(x, z);
						for (int j = 0; j < safeCoords.size(); j++) {
							SafeChunkCoordsIntPair sc = safeCoords.get(j);
							EntityPlayer playerOwner = theWorld.func_73046_m().getConfigurationManager().getPlayerForUsername(sc.getOwner());
							if (playerOwner != null) {
								playerOwner.addChatMessage(ClientHandler.createMessage(LangLocalization.get("MeteorShield.meteorBlocked"), EnumChatFormatting.GREEN));
								playerOwner.addStat(HandlerAchievement.meteorBlocked, 1);
							}
							MeteorsMod.proxy.lastMeteorPrevented.put(sc.getOwner(), EnumMeteor.KITTY);
							MeteorsMod.packetPipeline.sendToAll(new PacketShieldUpdate(sc.getOwner()));
						}
					} else {
						EntityMeteor fKitty = new EntityMeteor(this.theWorld, 1, x, z, EnumMeteor.KITTY, false);
						fKitty.spawnPauseTicks = random.nextInt(100);
						this.theWorld.spawnEntityInWorld(fKitty);
					}
				}
				player.addStat(HandlerAchievement.kittyEvent, 1);
			}	
		}
	}

	public void applyMeteorCrash(int x, int y, int z) {
		ArrayList<CrashedChunkSet> tbr = new ArrayList();

		for (int i = 0; i < this.crashedChunks.size(); i++) {
			CrashedChunkSet set = this.crashedChunks.get(i);
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
			MeteorsMod.packetPipeline.sendToDimension(new PacketLastCrash(new ChunkCoordinates(x, y, z)), theWorld.provider.dimensionId);
			if (MeteorsMod.instance.textNotifyCrash) {
				theWorld.func_73046_m().getConfigurationManager().sendChatMsg(new ChatComponentText(LangLocalization.get("Meteor.crashed")));
			}
		}
	}

	public boolean canSpawnNewMeteor() {
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

	public List<SafeChunkCoordsIntPair> getSafeChunkCoords(int x, int z) {
		if (meteorInProtectedZone(x, z)) {
			List<SafeChunkCoordsIntPair> cList = new ArrayList<SafeChunkCoordsIntPair>();
			List<String> owners = new ArrayList<String>();
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
		return new ArrayList<SafeChunkCoordsIntPair>();
	}

	public void readyNewMeteor(int x, int z, int size, int tGoal, EnumMeteor type) {
		if (canSpawnNewMeteor()) {
			GhostMeteor gMeteor = new GhostMeteor(x, z, size, tGoal, type);
			this.ghostMets.add(gMeteor);
			sendGhostMeteorAddPacket(gMeteor);
			updateNearestTimeForClients();
			if (type == EnumMeteor.KITTY) {
				Iterator<EntityPlayer> iter = theWorld.playerEntities.iterator();
				while (iter.hasNext()) {
					EntityPlayer player = iter.next();
					player.addChatMessage(ClientHandler.createMessage(LangLocalization.get("Meteor.kittiesDetected.one"), EnumChatFormatting.DARK_RED));
					player.addChatMessage(ClientHandler.createMessage(LangLocalization.get("Meteor.kittiesDetected.two"), EnumChatFormatting.DARK_RED));
				}
			}
		}
	}

	public GhostMeteor getNearestTimeMeteor() {
		if (this.theWorld == null) return null;
		GhostMeteor closestMeteor = null;
		for (int i = 0; i < this.ghostMets.size(); i++) {
			if (closestMeteor != null) {
				if (this.ghostMets.get(i).type != EnumMeteor.KITTY) {
					int var1 = closestMeteor.getRemainingTicks();
					int var2 = this.ghostMets.get(i).getRemainingTicks();
					if (var2 < var1)
						closestMeteor = this.ghostMets.get(i);
				}
			} else if (this.ghostMets.get(i).type != EnumMeteor.KITTY) {
				closestMeteor = this.ghostMets.get(i);
			}

		}

		return closestMeteor;
	}

	private void updateNearestTimeForClients() {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			GhostMeteor gMeteor = getNearestTimeMeteor();
			MeteorsMod.packetPipeline.sendToDimension(new PacketSoonestMeteor(gMeteor), theWorld.provider.dimensionId);
		}
	}

	public ChunkCoordinates getLastCrashLocation() {
		if (this.theWorld == null) return null;
		for (int i = 0; i < this.crashedChunks.size(); i++) {
			if (this.crashedChunks.get(i).age == 0) {
				return this.crashedChunks.get(i).getCrashCoords();
			}
		}
		return null;
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
		if ((r >= 60) && (mod.unknownEnabled)) {
			return EnumMeteor.UNKNOWN;
		}
		if ((r >= 52) && (mod.kreknoriteEnabled)) {
			return EnumMeteor.KREKNORITE;
		}
		if ((r >= 35) && (mod.frezariteEnabled)) {
			return EnumMeteor.FREZARITE;
		}
		return defaultType;
	}

	public void addSafeChunks(int x, int z, int radius, String shieldOwner)
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
		for (int sZ = z - radius + 1; sZ <= z - 1; sZ++) {
			for (int sX = x + radius - 1; sX >= x + 1; sX--) {
				if (sX - sZ <= max) {
					SafeChunkCoordsIntPair sPair = new SafeChunkCoordsIntPair(sX, sZ, shieldOwner);
					this.safeChunksWithOwners.add(sPair);
					this.safeChunks.add(new ChunkCoordIntPair(sX, sZ));
				}
			}
		}
	}

	public void removeSafeChunks(int x, int z, int radius, String shieldOwner)
	{
		if (shieldOwner == null) shieldOwner = "null";
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
		for (int sZ = z - radius + 1; sZ <= z - 1; sZ++) {
			for (int sX = x + radius - 1; sX >= x + 1; sX--) {
				if (sX - sZ <= max) {
					SafeChunkCoordsIntPair sPair = new SafeChunkCoordsIntPair(sX, sZ, shieldOwner);
					this.safeChunksWithOwners.remove(sPair);
					this.safeChunks.remove(new ChunkCoordIntPair(sX, sZ));
				}
			}
		}
	}

	public void sendGhostMeteorPackets(EntityPlayerMP player) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			ArrayList<GhostMeteor> mets = this.ghostMets;
			for (int i = 0; i < mets.size(); i++) {
				GhostMeteor met = mets.get(i);
				MeteorsMod.packetPipeline.sendTo(new PacketGhostMeteor(true, met), player);
			}
		}
	}

	private void sendGhostMeteorAddPacket(GhostMeteor met) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			MeteorsMod.packetPipeline.sendToDimension(new PacketGhostMeteor(true, met), theWorld.provider.dimensionId);
		}
	}

	private void sendGhostMeteorRemovePacket(GhostMeteor met) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			MeteorsMod.packetPipeline.sendToDimension(new PacketGhostMeteor(false, met), theWorld.provider.dimensionId);
		}
	}
	
	private void playCrashSound(EntityMeteor meteor) {
		Iterator<EntityPlayer> iter = theWorld.playerEntities.iterator();
		while (iter.hasNext()) {
			EntityPlayer player = iter.next();
			double xDiff = meteor.posX - player.posX;
			double zDiff = meteor.posZ - player.posZ;
			double xMod = xDiff / 128.0D * 4.0D;
			double zMod = zDiff / 128.0D * 4.0D;
			theWorld.playSoundEffect(player.posX + xMod, player.posY + 1.0D, player.posZ + zMod, MeteorsMod.MOD_ID + ":meteor.crash", 1.0F, 1.0F);
		}
	}
}
