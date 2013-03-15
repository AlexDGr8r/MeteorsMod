package net.meteor.common;

import java.util.List;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemFrezariteSword extends ItemSword
{
	public ItemFrezariteSword(int par1, EnumToolMaterial par2EnumToolMaterial)
	{
		super(par1, par2EnumToolMaterial);
		setTextureFile(MeteorsMod.textureFile);
	}

	public boolean hitEntity(ItemStack par1ItemStack, EntityLiving par2EntityLiving, EntityLiving par3EntityLiving)
	{
		par2EntityLiving.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 40, 5));
		return super.hitEntity(par1ItemStack, par2EntityLiving, par3EntityLiving);
	}

	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		par3List.add("\2473" + LangLocalization.get("enchantment.frezSword.one"));
		par3List.add("\2473" + LangLocalization.get("enchantment.frezSword.two"));
	}

	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack par1ItemStack)
	{
		return true;
	}

	public int getItemEnchantability()
	{
		return 0;
	}

	public String getItemDisplayName(ItemStack par1ItemStack)
	{
		return LangLocalization.get(getItemNameIS(par1ItemStack) + ".name").trim();
	}
}