package net.meteor.common.climate;

import java.util.HashMap;
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
	private HashMap<Integer, ClimateUpdater> climateUpdaters = new HashMap<Integer, ClimateUpdater>();

	@SubscribeEvent
	public void onWorldTick(WorldTickEvent event) {
		int dim = event.world.provider.dimensionId;
		if (climateUpdaters.containsKey(dim)) {
			climateUpdaters.get(dim).onWorldTick(event);
		}
	}
	
	public void registerUpdater(int dimension, ClimateUpdater updater) {
		climateUpdaters.put(dimension, updater);
	}

}