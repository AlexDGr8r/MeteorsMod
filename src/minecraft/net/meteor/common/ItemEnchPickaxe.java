package net.meteor.common;

import java.util.List;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemEnchPickaxe extends ItemPickaxe
{
	protected Enchantment enchantment;
	protected int level;

	public ItemEnchPickaxe(int par1, EnumToolMaterial par2EnumToolMaterial)
	{
		super(par1, par2EnumToolMaterial);
		this.setTextureFile(MeteorsMod.textureFile);
	}

	public Item setEnch(Enchantment ench, int lvl) {
		this.enchantment = ench;
		this.level = lvl;
		return this;
	}

	public void onCreated(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		par1ItemStack.addEnchantment(this.enchantment, this.level);
		super.onCreated(par1ItemStack, par2World, par3EntityPlayer);
	}

	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		if (!par1ItemStack.isItemEnchanted()) {
			par1ItemStack.addEnchantment(this.enchantment, this.level);
		}
		return super.onItemRightClick(par1ItemStack, par2World, par3EntityPlayer);
	}

	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		if (!par1ItemStack.isItemEnchanted()) {
			par3List.add("\2474" + LangLocalization.get("enchantment.addEnch.one"));
			par3List.add("\2474" + LangLocalization.get("enchantment.addEnch.two"));
		}
	}

	public String getItemDisplayName(ItemStack par1ItemStack)
	{
		return LangLocalization.get(getItemNameIS(par1ItemStack) + ".name").trim();
	}
}