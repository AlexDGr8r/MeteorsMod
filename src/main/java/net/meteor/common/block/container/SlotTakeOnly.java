package net.meteor.common.block.container;

import net.meteor.common.HandlerAchievement;
import net.meteor.common.block.BlockSlippery;
import net.meteor.common.block.BlockSlipperyStairs;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotTakeOnly extends Slot {
	
	private EntityPlayer thePlayer;

	public SlotTakeOnly(IInventory par1iInventory, int par2, int par3, int par4) {
		super(par1iInventory, par2, par3, par4);
	}
	
	public SlotTakeOnly(IInventory par1iInventory, int par2, int par3, int par4, EntityPlayer player) {
		this(par1iInventory, par2, par3, par4);
		this.thePlayer = player;
	}
	
	/**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
	@Override
	public boolean isItemValid(ItemStack par1ItemStack) {
		return false;
	}
	
	@Override
	protected void onCrafting(ItemStack p_75210_1_, int p_75210_2_) {
		this.onCrafting(p_75210_1_);
		super.onCrafting(p_75210_1_, p_75210_2_);
    }
	
	@Override
	protected void onCrafting(ItemStack item) {
		
		if (item.getItem() == Item.getItemFromBlock(Blocks.ice)) {
			this.thePlayer.addStat(HandlerAchievement.freezeWater, 1);
		} else if (Block.getBlockFromItem(item.getItem()) instanceof BlockSlippery || Block.getBlockFromItem(item.getItem()) instanceof BlockSlipperyStairs) {
			this.thePlayer.addStat(HandlerAchievement.freezeBlocks, 1);
		}
		
		super.onCrafting(item);
	}
	
	@Override
	public void onPickupFromSlot(EntityPlayer p_82870_1_, ItemStack p_82870_2_) {
		this.onCrafting(p_82870_2_);
		super.onPickupFromSlot(p_82870_1_, p_82870_2_);
    }

}
