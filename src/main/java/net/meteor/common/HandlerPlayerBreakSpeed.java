package net.meteor.common;

import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class HandlerPlayerBreakSpeed
{
	@SubscribeEvent
	public void onBreakSpeedModify(PlayerEvent.BreakSpeed event)
	{
		ItemStack stack = event.entityPlayer.getCurrentEquippedItem();
		if ((stack != null) && (event.entityPlayer.isInsideOfMaterial(Material.water))) {
			Item i = stack.getItem();
			if ((i == MeteorItems.FrezaritePickaxe) || (i == MeteorItems.FrezariteSpade) || (i == MeteorItems.FrezariteAxe))
				event.newSpeed = (event.originalSpeed * 5.0F);
		}
	}
}