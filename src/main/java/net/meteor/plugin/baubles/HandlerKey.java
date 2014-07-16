package net.meteor.plugin.baubles;

import net.meteor.common.MeteorsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;

import baubles.api.BaublesApi;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;

public class HandlerKey {
	
	private static KeyBinding toggleMagnetism;
	
	public void init() {
		toggleMagnetism = new KeyBinding("key.toggleMagnetism", Keyboard.KEY_Y, "key.categories.meteors");
		ClientRegistry.registerKeyBinding(toggleMagnetism);
	}
	
	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event) {
		if (toggleMagnetism.isPressed()) {
			IInventory inv = BaublesApi.getBaubles(Minecraft.getMinecraft().thePlayer);
			ItemStack stack = inv.getStackInSlot(3);
			if (stack != null) {
				if (stack.getItem() == Baubles.MagnetismController) {
					MeteorsMod.packetPipeline.sendToServer(new PacketToggleMagnetism());
				}
			}
		}
	}
	
	public static String getKey() {
		return Keyboard.getKeyName(toggleMagnetism.getKeyCode());
	}

}
