package net.meteor.common.item;

import java.util.List;

import net.meteor.common.LangLocalization;
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
	}

	@Override
	public String getItemDisplayName(ItemStack par1ItemStack)
	{
		return LangLocalization.get(this.getUnlocalizedName(par1ItemStack) + ".name").trim();
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		if (this.itemID == MeteorsMod.itemRedMeteorGem.itemID) {
			par3List.add(EnumChatFormatting.WHITE + LangLocalization.get("item.RedMeteorGem.desc.one"));
			par3List.add(EnumChatFormatting.WHITE + LangLocalization.get("item.RedMeteorGem.desc.two"));
		}
	}
	
}