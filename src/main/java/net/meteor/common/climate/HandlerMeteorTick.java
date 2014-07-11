package net.meteor.common.climate;

import java.util.HashMap;

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