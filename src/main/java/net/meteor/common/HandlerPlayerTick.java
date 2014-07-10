package net.meteor.common;

import java.util.List;

import net.meteor.plugin.baubles.Baubles;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class HandlerPlayerTick
{
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event) {
		EntityPlayer player = event.player;
		World world = player.worldObj;
		InventoryPlayer inv = player.inventory;

		if ((isWearing(MeteorItems.KreknoriteHelmet, inv.armorItemInSlot(3))) && 
				(isWearing(MeteorItems.KreknoriteBody, inv.armorItemInSlot(2))) && 
				(isWearing(MeteorItems.KreknoriteLegs, inv.armorItemInSlot(1))) && 
				(isWearing(MeteorItems.KreknoriteBoots, inv.armorItemInSlot(0)))) {
			ArmorEffectController.setImmuneToFire(player, true);
		} else {
			ArmorEffectController.setImmuneToFire(player, false);
		}

		int totalLevel = EnchantmentHelper.getEnchantmentLevel(MeteorsMod.ColdTouch.effectId, inv.armorItemInSlot(1)) + EnchantmentHelper.getEnchantmentLevel(MeteorsMod.ColdTouch.effectId, inv.armorItemInSlot(0));
		if (totalLevel > 0 && !player.isInWater())
		{
			if (totalLevel > 1 && player.isSprinting())
			{
				int l = MathHelper.floor_double(player.posX);
				int j1 = MathHelper.floor_double(player.posY - 2.0D);
				int k1 = MathHelper.floor_double(player.posZ);
				for (int x = l - 2; x < l + 2; x++) {
					for (int z = k1 - 2; z < k1 + 2; z++) {
						if ((world.getBlock(x, j1, z) == Blocks.water) || (world.getBlock(x, j1, z) == Blocks.flowing_water)) {
							world.setBlock(x, j1, z, Blocks.ice, 0, 2);
						}
					}		
				}
			}
			else
			{
				for (int j = 0; j < 4; j++) {
					int l = MathHelper.floor_double(player.posX + (j % 2 * 2 - 1) * 0.25F);
					int j1 = MathHelper.floor_double(player.posY - 2.0D);
					int k1 = MathHelper.floor_double(player.posZ + (j / 2 % 2 * 2 - 1) * 0.25F);
					if ((world.getBlock(l, j1, k1) == Blocks.water) || (world.getBlock(l, j1, k1) == Blocks.flowing_water)) {
						world.setBlock(l, j1, k1, Blocks.ice, 0, 2);
					}
				}
			}
		}

		int enchLevel = getMagnetizationLevel(player);
		if (enchLevel > 0) {
			double distance = 8 * enchLevel;
			List entities = world.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(player.posX - distance, player.posY - distance, player.posZ - distance, player.posX + distance, player.posY + distance, player.posZ + distance));
			for (int i1 = 0; i1 < entities.size(); i1++)
				if ((entities.get(i1) instanceof EntityItem))
					updateEntityItem((EntityItem)entities.get(i1), player, distance);
		}
	}

	private static boolean isWearing(Item item, ItemStack itemStack) {
		if ((itemStack != null) && 
				(item == itemStack.getItem())) return true;

		return false;
	}

	public static int getMagnetizationLevel(EntityPlayer player) {
		int enchLevel = Math.max(EnchantmentHelper.getMaxEnchantmentLevel(MeteorsMod.Magnetization.effectId, player.getLastActiveItems()), EnchantmentHelper.getEnchantmentLevel(MeteorsMod.Magnetization.effectId, player.getHeldItem()));
		if (Baubles.isBaublesLoaded()) {
			return Baubles.canAttractItems(player, enchLevel);
		} else {
			return enchLevel;
		}
	}

	private void updateEntityItem(EntityItem en, EntityPlayer player, double closeness) {
		if (player != null) {
			double var3 = (player.posX - en.posX) / closeness;
			double var5 = (player.posY + player.getEyeHeight() - en.posY) / closeness;
			double var7 = (player.posZ - en.posZ) / closeness;
			double var9 = Math.sqrt(var3 * var3 + var5 * var5 + var7 * var7);
			double var11 = 1.0D - var9;

			if (var11 > 0.0D)
			{
				var11 *= var11;
				en.motionX += var3 / var9 * var11 * 0.1D;
				en.motionY += var5 / var9 * var11 * 0.1D;
				en.motionZ += var7 / var9 * var11 * 0.1D;
			}
		}
	}
}