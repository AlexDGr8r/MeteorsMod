package net.meteor.common;

import java.util.EnumSet;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class HandlerMeteorTick
implements ITickHandler
{
	private static Random random = new Random();
	private int ticks = 0;
	private int tickGoal = -1;

	public void tickStart(EnumSet type, Object... tickData)
	{
	}

	public void tickEnd(EnumSet type, Object... tickData) {
		if (this.tickGoal == -1) {
			this.tickGoal = getNewTickGoal();
		}
		MinecraftServer server = MinecraftServer.getServer();
		World world = server.worldServerForDimension(0);
		MeteorsMod mod = MeteorsMod.instance;
		HandlerMeteor meteorHandler = MeteorsMod.proxy.meteorHandler;
		meteorHandler.updateMeteors();
		if (((!world.isDaytime()) || (!mod.meteorsFallOnlyAtNight)) && (meteorHandler.canSpawnNewMeteor())) {
			this.ticks += 1;
		}
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
					if (random.nextInt(100) < MeteorsMod.instance.kittyAttackChance)
						meteorHandler.readyNewMeteor(x, z, HandlerMeteor.getMeteorSize(), 2500, EnumMeteor.KITTY);
					else {
						meteorHandler.readyNewMeteor(x, z, HandlerMeteor.getMeteorSize(), random.nextInt(mod.RandTicksUntilMeteorCrashes + 1) + mod.MinTicksUntilMeteorCrashes, HandlerMeteor.getMeteorType());
					}
				}

			}

			this.ticks = 0;
			this.tickGoal = getNewTickGoal();
		}
	}

	public EnumSet ticks()
	{
		return EnumSet.of(TickType.SERVER);
	}

	public String getLabel()
	{
		return "Meteor";
	}

	private int getNewTickGoal() {
		return random.nextInt(MeteorsMod.instance.RandTicksUntilMeteorSpawn + 1) + MeteorsMod.instance.MinTicksUntilMeteorSpawn;
	}
}