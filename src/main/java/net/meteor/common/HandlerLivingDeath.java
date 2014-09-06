package net.meteor.common;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class HandlerLivingDeath {
	
	@SubscribeEvent
	public void onLivingDeath(LivingDeathEvent event) {
		if (event.source.isExplosion()) {
			
		}
	}

}
