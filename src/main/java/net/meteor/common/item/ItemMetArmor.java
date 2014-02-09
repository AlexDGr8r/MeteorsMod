package net.meteor.common.item;

import net.meteor.common.LangLocalization;
import net.meteor.common.MeteorItems;
import net.meteor.common.MeteorsMod;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ItemMetArmor extends ItemArmor
{
	
	private String armorTex;
	
	public ItemMetArmor(ItemArmor.ArmorMaterial armorMaterial, int j, int k)
	{
		super(armorMaterial, j, k);
		this.setCreativeTab(MeteorsMod.meteorTab);
	}
	
	public ItemMetArmor setArmorTexture(String tex) {
		this.armorTex = tex;
		return this;
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
	{
		if (slot != 1) { // TODO double check for derps
			return "meteors:" + this.armorTex + "_1.png";
		} else {
			return "meteors:" + this.armorTex + "_2.png";
		}
//		int i = itemstack.itemID;
//		if ((i == MeteorItems.MeteoriteHelmet.itemID) || (i == MeteorItems.MeteoriteBody.itemID) || (i == MeteorItems.MeteoriteBoots.itemID))
//			return "meteors:textures/armor/MeteoriteArmor_1.png";
//		if (i == MeteorItems.MeteoriteLegs.itemID)
//			return "meteors:textures/armor/MeteoriteArmor_2.png";
//		if ((i == MeteorItems.FrezariteHelmet.itemID) || (i == MeteorItems.FrezariteBody.itemID) || (i == MeteorItems.FrezariteBoots.itemID))
//			return "meteors:textures/armor/FrezariteArmor_1.png";
//		if (i == MeteorItems.FrezariteLegs.itemID)
//			return "meteors:textures/armor/FrezariteArmor_2.png";
//		if ((i == MeteorItems.KreknoriteHelmet.itemID) || (i == MeteorItems.KreknoriteBody.itemID) || (i == MeteorItems.KreknoriteBoots.itemID))
//			return "meteors:textures/armor/KreknoriteArmor_1.png";
//		if (i == MeteorItems.KreknoriteLegs.itemID) {
//			return "meteors:textures/armor/KreknoriteArmor_2.png";
//		}
//
//		return "meteors:textures/armor/MeteoriteArmor_1.png";
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack par1ItemStack)
	{
		return LangLocalization.get(this.getUnlocalizedName(par1ItemStack) + ".name").trim();
	}
	
	@Override
	public Item setTextureName(String s) {
		return super.setTextureName(MeteorsMod.MOD_ID + ":" + s);
	}
	
}