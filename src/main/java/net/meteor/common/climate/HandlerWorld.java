package net.meteor.common.climate;

import net.meteor.common.MeteorsMod;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class HandlerWorld {
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event) {
		addRules(event.world);
		int dim = event.world.provider.dimensionId;
		if (!MeteorsMod.proxy.metHandlers.containsKey(dim)) {
			HandlerMeteor metHandler = new HandlerMeteor(event);
			MeteorsMod.proxy.metHandlers.put(dim, metHandler);
		}
	}
	
	private void addRules(World world) {
		GameRules rules = world.getGameRules();
		addRule(rules, "meteorsFall", "true");
		addRule(rules, "summonMeteors", "true");
	}
	
	private void addRule(GameRules rules, String key, String val) {
		if (!rules.hasRule(key)) {
			rules.addGameRule(key, val);
		}
	}

}
