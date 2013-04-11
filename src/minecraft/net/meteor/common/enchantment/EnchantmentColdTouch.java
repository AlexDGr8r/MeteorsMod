package net.meteor.common.enchantment;

import net.meteor.common.LangLocalization;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.util.StatCollector;

public class EnchantmentColdTouch extends Enchantment
{
	public EnchantmentColdTouch(int par1, int par2)
	{
		super(par1, par2, EnumEnchantmentType.armor);
	}

	@Override
	public int getMinEnchantability(int par1)
	{
		return 40;
	}

	@Override
	public int getMaxEnchantability(int par1)
	{
		return super.getMinEnchantability(par1) + 20;
	}

	@Override
	public int getMaxLevel()
	{
		return 1;
	}

	@Override
	public String getName()
	{
		return LangLocalization.get("enchantment.ColdTouch");
	}

	@Override
	public String getTranslatedName(int par1)
	{
		String var2 = getName();
		return var2 + " " + StatCollector.translateToLocal("enchantment.level." + par1);
	}
}