package net.meteor.common.item;

import java.util.List;

import net.meteor.common.LangLocalization;
import net.meteor.common.MeteorItems;
import net.meteor.common.MeteorsMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemMeteorsMod extends Item
{
	public ItemMeteorsMod(int par1)
	{
		super(par1);
		this.setCreativeTab(MeteorsMod.meteorTab);
	}

	@Override
	public String getItemDisplayName(ItemStack par1ItemStack)
	{
		return LangLocalization.get(this.getUnlocalizedName(par1ItemStack) + ".name").trim();
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		if (this.itemID == MeteorItems.itemRedMeteorGem.itemID) {
			par3List.add(EnumChatFormatting.WHITE + LangLocalization.get("item.RedMeteorGem.desc.one"));
			par3List.add(EnumChatFormatting.WHITE + LangLocalization.get("item.RedMeteorGem.desc.two"));
		}
	}
	
	@Override
	public Item setTextureName(String s) {
		return super.setTextureName(MeteorsMod.MOD_ID + ":" + s);
	}
	
}