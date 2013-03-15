package net.meteor.common;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class HandlerPlayerBreakSpeed
{
	@ForgeSubscribe
	public void onBreakSpeedModify(PlayerEvent.BreakSpeed event)
	{
		ItemStack stack = event.entityPlayer.getCurrentEquippedItem();
		if ((stack != null) && (event.entityPlayer.isInsideOfMaterial(Material.water))) {
			int i = stack.itemID;
			if ((i == MeteorsMod.FrezaritePickaxe.itemID) || (i == MeteorsMod.FrezariteSpade.itemID) || (i == MeteorsMod.FrezariteAxe.itemID))
				event.newSpeed = (event.originalSpeed * 5.0F);
		}
	}
}