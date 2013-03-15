package net.meteor.common;

import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IArmorTextureProvider;

public class ItemMetArmor extends ItemArmor
implements IArmorTextureProvider
{
	public ItemMetArmor(int i, EnumArmorMaterial enumarmormaterial, int j, int k)
	{
		super(i, enumarmormaterial, j, k);
		setTextureFile(MeteorsMod.textureFile);
	}

	public String getArmorTextureFile(ItemStack itemstack)
	{
		int i = itemstack.itemID;
		if ((i == MeteorsMod.MeteoriteHelmet.itemID) || (i == MeteorsMod.MeteoriteBody.itemID) || (i == MeteorsMod.MeteoriteBoots.itemID))
			return "/armor/MeteoriteArmor_1.png";
		if (i == MeteorsMod.MeteoriteLegs.itemID)
			return "/armor/MeteoriteArmor_2.png";
		if ((i == MeteorsMod.FrezariteHelmet.itemID) || (i == MeteorsMod.FrezariteBody.itemID) || (i == MeteorsMod.FrezariteBoots.itemID))
			return "/armor/FrezariteArmor_1.png";
		if (i == MeteorsMod.FrezariteLegs.itemID)
			return "/armor/FrezariteArmor_2.png";
		if ((i == MeteorsMod.KreknoriteHelmet.itemID) || (i == MeteorsMod.KreknoriteBody.itemID) || (i == MeteorsMod.KreknoriteBoots.itemID))
			return "/armor/KreknoriteArmor_1.png";
		if (i == MeteorsMod.KreknoriteLegs.itemID) {
			return "/armor/KreknoriteArmor_2.png";
		}

		return "/armor/MeteoriteArmor_1.png";
	}
}