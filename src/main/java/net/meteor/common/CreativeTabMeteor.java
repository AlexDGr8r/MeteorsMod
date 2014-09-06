package net.meteor.common;

import java.util.List;

import net.meteor.common.item.ItemBlockSlippery;
import net.meteor.common.tileentity.TileEntitySlippery;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CreativeTabMeteor extends CreativeTabs {

	public CreativeTabMeteor(String label) {
		super(label);
	}
	
	@Override
	public ItemStack getIconItemStack()
    {
        return new ItemStack(MeteorBlocks.blockMeteor, 1, 1);
    }
	
	@SideOnly(Side.CLIENT)
	@Override
    public void displayAllReleventItems(List par1List) {
		super.displayAllReleventItems(par1List);
		par1List.add(16, getSlipperyStairItemStack(MeteorBlocks.blockSlipperyStairs));
		par1List.add(17, getSlipperyStairItemStack(MeteorBlocks.blockSlipperyStairsTwo));
		par1List.add(18, getSlipperyStairItemStack(MeteorBlocks.blockSlipperyStairsThree));
		par1List.add(19, getSlipperyStairItemStack(MeteorBlocks.blockSlipperyStairsFour));
		par1List.add(Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(MeteorsMod.ColdTouch, MeteorsMod.ColdTouch.getMaxLevel())));
		par1List.add(Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(MeteorsMod.Magnetization, MeteorsMod.Magnetization.getMaxLevel())));
	}
	
	private ItemStack getSlipperyStairItemStack(Block block) {
		ItemStack stack = new ItemStack(block, 1);
		NBTTagCompound nbt = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
		nbt.setString(ItemBlockSlippery.FACADE_BLOCK_KEY, TileEntitySlippery.getNameFromBlock(Blocks.oak_stairs));
		stack.setTagCompound(nbt);
		return stack;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem() {
		return MeteorItems.itemMeteorChips;
	}

}
