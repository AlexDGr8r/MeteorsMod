package net.meteor.common.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;

public class EnchantmentMagnetized extends Enchantment
{
	public EnchantmentMagnetized(int par1, int par2)
	{
		super(par1, par2, EnumEnchantmentType.all);
	}

	@Override
	public int getMinEnchantability(int par1)
	{
		return 15 + (par1 - 1) * 9;
	}

	@Override
	public int getMaxEnchantability(int par1)
	{
		return super.getMinEnchantability(par1) + 50;
	}

	@Override
	public int getMaxLevel()
	{
		return 1;
	}

}