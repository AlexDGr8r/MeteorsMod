package net.meteor.common;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.meteor.common.tileentity.TileEntityFreezingMachine;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

public class TooltipProvider {
	
	@SubscribeEvent
	public void onTooltip(ItemTooltipEvent event) {
		int i = TileEntityFreezingMachine.getItemFreezeTime(event.itemStack);
		if (i > 0) {
			event.toolTip.add(StatCollector.translateToLocalFormatted("info.freezeTime", i));
		}
	}

}
