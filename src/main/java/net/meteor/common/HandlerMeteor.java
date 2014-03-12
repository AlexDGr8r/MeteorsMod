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
import net.minecraft.util.MathHelper;
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
	private MeteorShieldSavedData shieldSavedData;

	private static Random random = new Random();

	public ArrayList<GhostMeteor> ghostMets = new ArrayList<GhostMeteor>();
	public ArrayList<CrashedChunkSet> crashedChunks = new ArrayList<CrashedChunkSet>();
	public ArrayList<IMeteorShield> meteorShields = new ArrayList<IMeteorShield>();

	public static EnumMeteor defaultType;

	public HandlerMeteor(WorldEvent.Load event) {
		if (!(event.world instanceof WorldServer) || event.world.provider.dimensionId != 0) {
			return;
		}
		MeteorsMod.instance.setClientStartConfig();
		this.theWorld = (WorldServer) event.world;
		this.gMetData = GhostMeteorData.forWorld(theWorld, this);
		this.ccSetData = CrashedChunkSetData.forWorld(theWorld, this);
		this.shieldSavedData = MeteorShieldSavedData.forWorld(theWorld, this);
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
						IMeteorShield shield = getClosestShieldInRange(gMeteor.x, gMeteor.z);
						if (shield != null) {
							String owner = shield.getOwner();
							EntityPlayer player = theWorld.func_73046_m().getConfigurationManager().getPlayerForUsername(owner);
							if (player != null) {
								player.addChatMessage(ClientHandler.createMessage(LangLocalization.get("MeteorShield.meteorBlocked"), EnumChatFormatting.GREEN));
								player.addStat(HandlerAchievement.meteorBlocked, 1);
							}
							MeteorsMod.proxy.lastMeteorPrevented.put(owner, gMeteor.type);
							MeteorsMod.packetPipeline.sendToAll(new PacketShieldUpdate(owner));
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
					IMeteorShield shield = getClosestShieldInRange(x, z);
					if (shield != null) {
						String owner = shield.getOwner();
						EntityPlayer playerOwner = theWorld.func_73046_m().getConfigurationManager().getPlayerForUsername(owner);
						if (playerOwner != null) {
							playerOwner.addChatMessage(ClientHandler.createMessage(LangLocalization.get("MeteorShield.meteorBlocked"), EnumChatFormatting.GREEN));
							playerOwner.addStat(HandlerAchievement.meteorBlocked, 1);
						}
						MeteorsMod.proxy.lastMeteorPrevented.put(owner, EnumMeteor.KITTY);
						MeteorsMod.packetPipeline.sendToAll(new PacketShieldUpdate(owner));
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

	private double getDistance(int x1, int z1, int x2, int z2) {
		int x = x1 - x2;
		int z = z1 - z2;
		return MathHelper.sqrt_double(x * x + z * z);
	}
	
	public void addShield(IMeteorShield shield) {
		for (int i = 0; i < meteorShields.size(); i++) {
			IMeteorShield shield2 = meteorShields.get(i);
			if (shield.equals(shield2)) {
				if (!shield2.isTileEntity()) {
					meteorShields.remove(i);
					meteorShields.add(shield);
					MeteorsMod.log.info("METEOR SHIELD REPLACED X:" + shield.getX() + " Y:" + shield.getY() + " Z:" + shield.getZ() + " O:" + shield.getOwner());
					return;
				}
			}
		}
		meteorShields.add(shield);
		MeteorsMod.log.info("METEOR SHIELD ADDED X:" + shield.getX() + " Y:" + shield.getY() + " Z:" + shield.getZ() + " O:" + shield.getOwner());
	}

	public IMeteorShield getClosestShield(int x, int z) {
		Iterator<IMeteorShield> iter = this.meteorShields.iterator();
		IMeteorShield closest = null;
		double distance = -1;
		while (iter.hasNext()) {
			IMeteorShield shield = iter.next();
			if (closest == null) {
				closest = shield;
				distance = getDistance(x, z, shield.getX(), shield.getZ());
			} else {
				double d = getDistance(x, z, shield.getX(), shield.getZ());
				if (d < distance) {
					distance = d;
					closest = shield;
				}
			}
		}
		return closest;
	}

	public IMeteorShield getClosestShieldInRange(int x, int z) {
		IMeteorShield shield = getClosestShield(x, z);
		if (shield != null) {
			double distance = getDistance(x, z, shield.getX(), shield.getZ());
			return distance <= shield.getRange() ? shield : null;
		}
		return null;
	}

	public List<IMeteorShield> getShieldsInRange(int x, int z) {
		List<IMeteorShield> shields = new ArrayList<IMeteorShield>();
		Iterator<IMeteorShield> iter = this.meteorShields.iterator();
		while (iter.hasNext()) {
			IMeteorShield shield = iter.next();
			double d = getDistance(x, z, shield.getX(), shield.getZ());
			if (d <= shield.getRange()) {
				shields.add(shield);
			}
		}
		return shields;
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
