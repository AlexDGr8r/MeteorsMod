package net.meteor.common.item;

import java.util.List;

import net.meteor.common.MeteorItems;
import net.meteor.common.MeteorsMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemMeteorsMod extends Item
{
	public ItemMeteorsMod()
	{
		super();
		this.setCreativeTab(MeteorsMod.meteorTab);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		if (this == MeteorItems.itemRedMeteorGem) {
			par3List.add(EnumChatFormatting.WHITE + StatCollector.translateToLocal("item.RedMeteorGem.desc.one"));
			par3List.add(EnumChatFormatting.WHITE + StatCollector.translateToLocal("item.RedMeteorGem.desc.two"));
		}
	}
	
	public Item setTexture(String s) {
		return this.setTextureName(MeteorsMod.MOD_ID + ":" + s);
	}
	
}