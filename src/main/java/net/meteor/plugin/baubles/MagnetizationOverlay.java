package net.meteor.plugin.baubles;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class MagnetizationOverlay {
	
	@SubscribeEvent
	public void renderOverlay(RenderGameOverlayEvent.Text event) {
		if (Baubles.renderDisplay) {
			long time = Minecraft.getMinecraft().theWorld.getTotalWorldTime();
			if (time < Baubles.renderDisplayTicks) {
				event.right.add("Magnetization " + (Baubles.enabledMagnetism ? EnumChatFormatting.GREEN + "Enabled" : EnumChatFormatting.RED + "Disabled"));
			} else {
				Baubles.renderDisplay = false;
			}
		}
	}

}
