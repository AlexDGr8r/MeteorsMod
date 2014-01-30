package net.meteor.common.item;

import net.meteor.common.LangLocalization;
import net.meteor.common.MeteorsMod;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class ItemFoodMeteorsMod extends ItemFood
{
	public ItemFoodMeteorsMod(int par1, int par2, boolean par3)
	{
		super(par1, par2, par3);
		this.setCreativeTab(MeteorsMod.meteorTab);
	}

	@Override
	public String getItemDisplayName(ItemStack par1ItemStack)
	{
		return LangLocalization.get(this.getUnlocalizedName(par1ItemStack) + ".name").trim();
	}
	
	@Override
	public Item setTextureName(String s) {
		return super.setTextureName(MeteorsMod.MOD_ID + ":" + s);
	}
	
}