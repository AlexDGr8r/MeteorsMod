package net.meteor.common;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemMeteorsMod extends Item
{
	public ItemMeteorsMod(int par1)
	{
		super(par1);
	}

	@Override
	public String getItemDisplayName(ItemStack par1ItemStack)
	{
		return LangLocalization.get(this.getUnlocalizedName(par1ItemStack) + ".name").trim();
	}
}