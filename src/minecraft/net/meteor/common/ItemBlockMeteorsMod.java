package net.meteor.common;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockMeteorsMod extends ItemBlock
{
	public ItemBlockMeteorsMod(int par1)
	{
		super(par1);
		setTextureFile(MeteorsMod.textureFile);
	}

	@Override
	public String getItemDisplayName(ItemStack par1ItemStack)
	{
		return LangLocalization.get(this.getItemNameIS(par1ItemStack) + ".name").trim();
	}
}