package net.meteor.common.block.container;

import net.meteor.common.HandlerAchievement;
import net.meteor.common.MeteorItems;
import net.meteor.common.tileentity.TileEntityMeteorShield;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerMeteorShield extends Container {
	
	private TileEntityMeteorShield shield;
	
	public ContainerMeteorShield(InventoryPlayer inventoryPlayer, TileEntityMeteorShield mShield) {
		this.shield = mShield;
		
		// Shield Inventory
		this.addSlotToContainer(new SlotSingleItem(mShield, 0, 47, 60, MeteorItems.itemMeteorChips));
		
		for (int i = 0; i < 4; ++i) {
			this.addSlotToContainer(new SlotSingleItem(mShield, i + 1, 67 + i * 29, 60, MeteorItems.itemRedMeteorGem));
		}
		
		for (int i = 0; i < 4; ++i) {
			for (int j = 0; j < 2; ++j) {
				this.addSlotToContainer(new SlotTakeOnly(mShield, i * 2 + j + 5, 8 + j * 18, 6 + i * 18));
			}
		}
		
		if (inventoryPlayer == null) return;
		// Player inventory
		for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
        }
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return this.shield.isUseableByPlayer(entityplayer);
	}
	
	@Override
	/**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int slotID)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(slotID);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            
            if (slotID < shield.getSizeInventory()) {
            	// last boolean is which direction to search in (last to first, first to last)
            	// transfer into player inventory
            	if (!this.mergeItemStack(itemstack1, shield.getSizeInventory(), this.inventorySlots.size(), true)) {
            		return null;
            	}
            } else if (itemstack1.getItem() == MeteorItems.itemMeteorChips) {
            	ItemStack chip = new ItemStack(MeteorItems.itemMeteorChips, 1);
            	if (shield.isItemValidForSlot(0, chip)) {
            		this.putStackInSlot(0, chip);
            		itemstack1.stackSize--;
            	}
            	itemstack = null;
            } else if (itemstack1.getItem() == MeteorItems.itemRedMeteorGem) {
            	for (int i = 1; i < 5 && itemstack1.stackSize > 0; i++) {
            		Slot slot1 = (Slot)this.inventorySlots.get(i);
            		if (slot1 != null && shield.isItemValidForSlot(i, new ItemStack(MeteorItems.itemRedMeteorGem, 1))) {
            			slot1.putStack(new ItemStack(MeteorItems.itemRedMeteorGem, 1));
            			itemstack1.stackSize--;
            		}
            	}
            	itemstack = null;
            } else {
            	return null;
            }
            
            if (itemstack1.stackSize == 0) {
                slot.putStack((ItemStack)null);
            } else {
                slot.onSlotChanged();
            }
        }
        
        return itemstack;
    }

}
