package net.meteor.plugin.baubles;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class MagnetizationOverlay {
	
	@SubscribeEvent
	public void renderOverlay(RenderGameOverlayEvent.Text event) {
		if (Baubles.renderDisplay) {
			long time = Minecraft.getMinecraft().theWorld.getTotalWorldTime();
			if (time < Baubles.renderDisplayTicks) {
				event.right.add(StatCollector.translateToLocal(Baubles.enabledMagnetism ? "info.magnetisationController.enabled" : "info.magnetisationController.disabled"));
			} else {
				Baubles.renderDisplay = false;
			}
		}
	}

}
