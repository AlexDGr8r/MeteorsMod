package net.meteor.common.climate;

import net.meteor.common.MeteorsMod;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class HandlerWorld {
	
	public static final String METEORS_FALL_GAMERULE = "meteorsFall";
	public static final String SUMMON_METEORS_GAMERULE = "summonMeteors";
	
	private HandlerMeteorTick worldTickHandler = new HandlerMeteorTick();
	
	public HandlerWorld() {
		FMLCommonHandler.instance().bus().register(worldTickHandler);
	}
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event) {
		if (!event.world.isRemote) {
			addRules(event.world);
			int dim = event.world.provider.dimensionId;
			HandlerMeteor metHandler = new HandlerMeteor(event, worldTickHandler);
			MeteorsMod.proxy.metHandlers.put(dim, metHandler);
		}
	}
	
	private void addRules(World world) {
		GameRules rules = world.getGameRules();
		addRule(rules, METEORS_FALL_GAMERULE, (world.provider.isSurfaceWorld() ? "true" : "false"));
		addRule(rules, SUMMON_METEORS_GAMERULE, "true");
	}
	
	private void addRule(GameRules rules, String key, String val) {
		if (!rules.hasRule(key)) {
			rules.addGameRule(key, val);
		}
	}

}
