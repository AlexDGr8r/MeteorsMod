package net.meteor.common.item;

import java.util.List;

import net.meteor.common.MeteorItems;
import net.meteor.common.MeteorsMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
		if (slot == 2) {
			return MeteorsMod.MOD_ID + ":textures/armor/" + this.armorTex + "Armor_2.png";
		} else {
			return MeteorsMod.MOD_ID + ":textures/armor/" + this.armorTex + "Armor_1.png";
		}
	}
	
	public ItemMetArmor setTexture(String s) {
		return (ItemMetArmor)this.setTextureName(MeteorsMod.MOD_ID + ":" + s);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		if (this == MeteorItems.KreknoriteHelmet || this == MeteorItems.KreknoriteBody ||
				this == MeteorItems.KreknoriteLegs || this == MeteorItems.KreknoriteBoots) {
			par3List.add(StatCollector.translateToLocal("info.kreknoriteArmorBonus"));
		}
	}
	
}