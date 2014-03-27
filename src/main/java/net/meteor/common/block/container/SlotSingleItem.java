package net.meteor.common.block.container;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotSingleItem extends Slot {
	
	private Item item;

	public SlotSingleItem(IInventory par1iInventory, int par2, int par3, int par4, Item item) {
		super(par1iInventory, par2, par3, par4);
		this.item = item;
	}
	
	/**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
	@Override
    public boolean isItemValid(ItemStack par1ItemStack) {
		return par1ItemStack != null && par1ItemStack.getItem() == item && this.inventory.isItemValidForSlot(this.getSlotIndex(), par1ItemStack);
    }
	
	/**
     * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in the case
     * of armor slots)
     */
	@Override
	public int getSlotStackLimit() {
		return 1;
	}

}
