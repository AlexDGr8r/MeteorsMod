package net.meteor.common.block;

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
		
		this.addSlotToContainer(new Slot(mShield, 0, 47, 60));
		
		for (int i = 0; i < 4; ++i) {
			this.addSlotToContainer(new Slot(mShield, i + 1, 67 + i * 14, 60));
		}
		
		for (int i = 0; i < 4; ++i) {
			for (int j = 0; j < 2; ++j) {
				this.addSlotToContainer(new Slot(mShield, i * 2 + j + 5, 8 + j * 18, 6 + i * 18));
			}
		}
		
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
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(par2);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            
            if (par2 < shield.getSizeInventory()) {
            	if (!this.mergeItemStack(itemstack1, shield.getSizeInventory(), this.inventorySlots.size(), true)) {
            		return null;
            	}
            } else if (itemstack1.getItem() == MeteorItems.itemMeteorChips) {
            	
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
