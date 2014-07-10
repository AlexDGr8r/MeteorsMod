package net.meteor.common.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class EnchantmentColdTouch extends Enchantment
{
	public EnchantmentColdTouch(int par1, int par2)
	{
		super(par1, par2, EnumEnchantmentType.armor);
	}

	@Override
	public int getMinEnchantability(int par1)
	{
		return 15 + (par1 - 1) * 6;
	}

	@Override
	public int getMaxEnchantability(int par1)
	{
		return super.getMinEnchantability(par1) + 45;
	}

	@Override
	public int getMaxLevel()
	{
		return 2;
	}
	
	@Override
	public boolean canApply(ItemStack par1ItemStack) {
		Item item = par1ItemStack.getItem();
		if (item instanceof ItemArmor) {
			ItemArmor itemarmor = (ItemArmor)item;
			return itemarmor.armorType == 2 || itemarmor.armorType == 3;
		}
		return false;
    }
}