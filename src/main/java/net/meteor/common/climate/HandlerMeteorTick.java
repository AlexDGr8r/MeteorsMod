package net.meteor.common.climate;

import java.util.List;
import java.util.Random;

import net.meteor.common.EnumMeteor;
import net.meteor.common.IMeteorShield;
import net.meteor.common.MeteorsMod;
import net.meteor.common.entity.EntityComet;
import net.meteor.common.tileentity.TileEntityMeteorShield;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;

public class HandlerMeteorTick
{
	private static Random random = new Random();
	private int ticks = 0;
	private int tickGoal = -1;
	private String worldName;
	private HandlerMeteor meteorHandler;
	private long lastTick = 0L;
	
	private boolean wasDay = true;
	
	public HandlerMeteorTick(HandlerMeteor metHand, String wName) {
		this.meteorHandler = metHand;
		this.worldName = wName;
	}

	@SubscribeEvent
	public void onWorldTick(WorldTickEvent event) {
		World world = event.world;
		if (!world.isRemote) {
			if (world.getGameRules().getGameRuleBooleanValue("meteorsFall")) {
				long wTime = world.getTotalWorldTime();
				if (wTime % 20L == 0L && wTime != lastTick && world.getWorldInfo().getWorldName().equalsIgnoreCase(worldName)) {
					lastTick = wTime;
					if (this.tickGoal == -1) {
						this.tickGoal = getNewTickGoal();
					}
					MeteorsMod mod = MeteorsMod.instance;
					meteorHandler.updateMeteors();
					if ((world.getWorldTime() % 24000L >= 12000L || (!mod.meteorsFallOnlyAtNight))) {
						this.ticks++;
						if (this.ticks >= this.tickGoal) {
							if ((meteorHandler.canSpawnNewMeteor()) && (world.playerEntities.size() > 0)) {
								int x = world.rand.nextInt(mod.meteorFallDistance);
								int z = world.rand.nextInt(mod.meteorFallDistance);
								if (world.rand.nextBoolean()) x = -x;
								if (world.rand.nextBoolean()) z = -z;
								EntityPlayer player = (EntityPlayer) world.playerEntities.get(world.rand.nextInt(world.playerEntities.size()));
								x = (int)(x + player.posX);
								z = (int)(z + player.posZ);
								ChunkCoordIntPair coords = world.getChunkFromBlockCoords(x, z).getChunkCoordIntPair();
								if (meteorHandler.canSpawnNewMeteorAt(coords)) {
									if (random.nextInt(100) < MeteorsMod.instance.kittyAttackChance) {
										meteorHandler.readyNewMeteor(x, z, HandlerMeteor.getMeteorSize(), 90, EnumMeteor.KITTY);
									} else {
										meteorHandler.readyNewMeteor(x, z, HandlerMeteor.getMeteorSize(), random.nextInt(mod.RandTicksUntilMeteorCrashes + 1) + mod.MinTicksUntilMeteorCrashes, HandlerMeteor.getMeteorType());
									}
								}
							}
							
							if (random.nextInt(100) < MeteorsMod.instance.cometFallChance && world.playerEntities.size() > 0) {
								int x = world.rand.nextInt(mod.meteorFallDistance / 4);
								int z = world.rand.nextInt(mod.meteorFallDistance / 4);
								if (world.rand.nextBoolean()) x = -x;
								if (world.rand.nextBoolean()) z = -z;
								EntityPlayer player = (EntityPlayer) world.playerEntities.get(world.rand.nextInt(world.playerEntities.size()));
								x = (int)(x + player.posX);
								z = (int)(z + player.posZ);
								EntityComet comet = new EntityComet(world, x, z, HandlerMeteor.getCometType());
								
								List<IMeteorShield> shields = meteorHandler.getShieldManager().getShieldsInRange(x, z);
								for (int i = 0; i < shields.size(); i++) {
									IMeteorShield ims = shields.get(i);
									TileEntityMeteorShield shield = (TileEntityMeteorShield)world.getTileEntity(ims.getX(), ims.getY(), ims.getZ());
									if (shield != null) {
										shield.detectComet(comet);
									}
								}
								
								world.spawnEntityInWorld(comet);
							}

							this.ticks = 0;
							this.tickGoal = getNewTickGoal();
						}
					}
				}
			}
		}
		
	}

	private int getNewTickGoal() {
		return random.nextInt(MeteorsMod.instance.RandTicksUntilMeteorSpawn + 1) + MeteorsMod.instance.MinTicksUntilMeteorSpawn;
	}
	
	public int getSecondsUntilNewMeteor() {
		return tickGoal - ticks;
	}

}