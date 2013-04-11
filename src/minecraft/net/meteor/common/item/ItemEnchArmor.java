package net.meteor.common.item;

import java.util.List;

import net.meteor.common.LangLocalization;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemEnchArmor extends ItemMetArmor
{
	protected Enchantment enchantment;
	protected int level;

	public ItemEnchArmor(int par1, EnumArmorMaterial par2EnumArmorMaterial, int par3, int par4)
	{
		super(par1, par2EnumArmorMaterial, par3, par4);
	}

	public Item setEnch(Enchantment ench, int lvl) {
		this.enchantment = ench;
		this.level = lvl;
		return this;
	}

	@Override
	public void onCreated(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		par1ItemStack.addEnchantment(this.enchantment, this.level);
		super.onCreated(par1ItemStack, par2World, par3EntityPlayer);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		if (!par1ItemStack.isItemEnchanted()) {
			par1ItemStack.addEnchantment(this.enchantment, this.level);
		}
		return super.onItemRightClick(par1ItemStack, par2World, par3EntityPlayer);
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		if (!par1ItemStack.isItemEnchanted()) {
			par3List.add("\2474" + LangLocalization.get("enchantment.addEnch.one"));
			par3List.add("\2474" + LangLocalization.get("enchantment.addEnch.two"));
		}
	}

	@Override
	public String getItemDisplayName(ItemStack par1ItemStack)
	{
		return LangLocalization.get(this.getUnlocalizedName(par1ItemStack) + ".name").trim();
	}
}