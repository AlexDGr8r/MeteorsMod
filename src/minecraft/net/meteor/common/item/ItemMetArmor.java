package net.meteor.common.item;

import net.meteor.common.LangLocalization;
import net.meteor.common.MeteorsMod;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ItemMetArmor extends ItemArmor
{
	public ItemMetArmor(int i, EnumArmorMaterial enumarmormaterial, int j, int k)
	{
		super(i, enumarmormaterial, j, k);
		this.setCreativeTab(MeteorsMod.meteorTab);
	}

	@Override
	public String getArmorTexture(ItemStack itemstack, Entity entity, int slot, int layer)
	{
		int i = itemstack.itemID;
		if ((i == MeteorsMod.MeteoriteHelmet.itemID) || (i == MeteorsMod.MeteoriteBody.itemID) || (i == MeteorsMod.MeteoriteBoots.itemID))
			return "meteors:textures/armor/MeteoriteArmor_1.png";
		if (i == MeteorsMod.MeteoriteLegs.itemID)
			return "meteors:textures/armor/MeteoriteArmor_2.png";
		if ((i == MeteorsMod.FrezariteHelmet.itemID) || (i == MeteorsMod.FrezariteBody.itemID) || (i == MeteorsMod.FrezariteBoots.itemID))
			return "meteors:textures/armor/FrezariteArmor_1.png";
		if (i == MeteorsMod.FrezariteLegs.itemID)
			return "meteors:textures/armor/FrezariteArmor_2.png";
		if ((i == MeteorsMod.KreknoriteHelmet.itemID) || (i == MeteorsMod.KreknoriteBody.itemID) || (i == MeteorsMod.KreknoriteBoots.itemID))
			return "meteors:textures/armor/KreknoriteArmor_1.png";
		if (i == MeteorsMod.KreknoriteLegs.itemID) {
			return "meteors:textures/armor/KreknoriteArmor_2.png";
		}

		return "meteors:textures/armor/MeteoriteArmor_1.png";
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