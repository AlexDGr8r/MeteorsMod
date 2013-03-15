package net.meteor.common;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemFrezaritePickaxe extends ItemPickaxe
{
	public ItemFrezaritePickaxe(int par1, EnumToolMaterial par2EnumToolMaterial)
	{
		super(par1, par2EnumToolMaterial);
		setTextureFile(MeteorsMod.textureFile);
	}

	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		par3List.add("\2473" + LangLocalization.get("enchantment.frezPickaxe.one"));
		par3List.add("\2473" + LangLocalization.get("enchantment.frezPickaxe.two"));
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