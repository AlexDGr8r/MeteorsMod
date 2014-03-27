package net.meteor.common.block.container;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotTakeOnly extends Slot {

	public SlotTakeOnly(IInventory par1iInventory, int par2, int par3, int par4) {
		super(par1iInventory, par2, par3, par4);
	}
	
	/**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
	@Override
	public boolean isItemValid(ItemStack par1ItemStack) {
		return false;
	}

}
