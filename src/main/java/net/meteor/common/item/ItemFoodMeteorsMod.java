package net.meteor.common.item;

import net.meteor.common.MeteorsMod;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class ItemFoodMeteorsMod extends ItemFood
{
	public ItemFoodMeteorsMod(int par1, float par2, boolean par3)
	{
		super(par1, par2, par3);
		this.setCreativeTab(MeteorsMod.meteorTab);
	}
	
	public ItemFoodMeteorsMod(int p_i45340_1_, boolean p_i45340_2_)
    {
        this(p_i45340_1_, 0.6F, p_i45340_2_);
    }
	
	@Override
	public Item setTextureName(String s) {
		return super.setTextureName(MeteorsMod.MOD_ID + ":" + s);
	}
	
}