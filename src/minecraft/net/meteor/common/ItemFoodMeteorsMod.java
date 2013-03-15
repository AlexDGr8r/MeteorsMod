package net.meteor.common;

import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class ItemFoodMeteorsMod extends ItemFood
{
	public ItemFoodMeteorsMod(int par1, int par2, boolean par3)
	{
		super(par1, par2, par3);
		this.setTextureFile(MeteorsMod.textureFile);
	}

	public String getItemDisplayName(ItemStack par1ItemStack)
	{
		return LangLocalization.get(getItemNameIS(par1ItemStack) + ".name").trim();
	}
}