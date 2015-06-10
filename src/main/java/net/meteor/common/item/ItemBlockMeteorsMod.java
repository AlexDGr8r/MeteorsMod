package net.meteor.common.item;

import java.util.List;

import net.meteor.common.MeteorBlocks;
import net.meteor.common.MeteorsMod;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockMeteorsMod extends ItemBlock
{
	public ItemBlockMeteorsMod(Block bl)
	{
		super(bl);
		this.setCreativeTab(MeteorsMod.meteorTab);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		if (this == Item.getItemFromBlock(MeteorBlocks.torchMeteorShieldActive)) {
			par3List.add(EnumChatFormatting.LIGHT_PURPLE + StatCollector.translateToLocal("ProtectionTorch.usage"));
		}
	}
	
	public Item setTexture(String s) {
		return this.setTextureName(MeteorsMod.MOD_ID + ":" + s);
	}
	
}