package net.meteor.common.item;

import java.util.List;

import net.meteor.common.MeteorsMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemFrezariteAxe extends ItemAxe
{
	public ItemFrezariteAxe(Item.ToolMaterial toolMaterial)
	{
		super(toolMaterial);
		this.setCreativeTab(MeteorsMod.meteorTab);
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		par3List.add("\2473" + StatCollector.translateToLocal("enchantment.frezAxe.one"));
		par3List.add("\2473" + StatCollector.translateToLocal("enchantment.frezAxe.two"));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean hasEffect(ItemStack par1ItemStack)
	{
		return true;
	}

	@Override
	public int getItemEnchantability()
	{
		return 0;
	}
	
	public Item setTexture(String s) {
		return this.setTextureName(MeteorsMod.MOD_ID + ":" + s);
	}
	
}