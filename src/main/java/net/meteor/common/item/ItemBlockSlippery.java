package net.meteor.common.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.meteor.common.block.BlockSlipperyStairs;
import net.meteor.common.tileentity.TileEntitySlippery;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemBlockSlippery extends ItemBlock {
	
	public static final String FACADE_BLOCK_KEY = "slipFacadeBlock";

	public ItemBlockSlippery(Block block) {
		super(block);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, EntityPlayer player, List info, boolean advancedTip) {
		checkNBT(itemStack);
		int slipperiness = getSlipperiness(this.field_150939_a);
		info.add(StatCollector.translateToLocalFormatted("info.slipperyBlock.one", slipperiness));
	}

	@Override
	public String getItemStackDisplayName(ItemStack itemStack) {
		checkNBT(itemStack);
		Block storedBlock = getStoredBlock(itemStack);
		String facadeName = new ItemStack(Item.getItemFromBlock(storedBlock), 1, itemStack.getItemDamage()).getDisplayName();
		return StatCollector.translateToLocalFormatted(this.getUnlocalizedName() + ".name", facadeName);
    }
	
	@Override
	public void onUpdate(ItemStack itemStack, World p_77663_2_, Entity p_77663_3_, int p_77663_4_, boolean p_77663_5_) {
		checkNBT(itemStack);
	}
	
	@Override
	public int getMetadata(int p_77647_1_) {
        return p_77647_1_;
    }
	
	public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_)
    {
        Block block = p_77648_3_.getBlock(p_77648_4_, p_77648_5_, p_77648_6_);
        Block storedBlock = getStoredBlock(p_77648_1_);

        if (block == Blocks.snow_layer && (p_77648_3_.getBlockMetadata(p_77648_4_, p_77648_5_, p_77648_6_) & 7) < 1)
        {
            p_77648_7_ = 1;
        }
        else if (block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush && !block.isReplaceable(p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_))
        {
            if (p_77648_7_ == 0)
            {
                --p_77648_5_;
            }

            if (p_77648_7_ == 1)
            {
                ++p_77648_5_;
            }

            if (p_77648_7_ == 2)
            {
                --p_77648_6_;
            }

            if (p_77648_7_ == 3)
            {
                ++p_77648_6_;
            }

            if (p_77648_7_ == 4)
            {
                --p_77648_4_;
            }

            if (p_77648_7_ == 5)
            {
                ++p_77648_4_;
            }
        }

        if (p_77648_1_.stackSize == 0)
        {
            return false;
        }
        else if (!p_77648_2_.canPlayerEdit(p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, p_77648_1_))
        {
            return false;
        }
        else if (p_77648_5_ == 255 && storedBlock.getMaterial().isSolid())
        {
            return false;
        }
        else if (p_77648_3_.canPlaceEntityOnSide(storedBlock, p_77648_4_, p_77648_5_, p_77648_6_, false, p_77648_7_, p_77648_2_, p_77648_1_))
        {
            int i1 = this.getMetadata(p_77648_1_.getItemDamage());
            int j1 = storedBlock.onBlockPlaced(p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, p_77648_8_, p_77648_9_, p_77648_10_, i1);

            if (placeBlockAt(p_77648_1_, p_77648_2_, p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, p_77648_8_, p_77648_9_, p_77648_10_, j1))
            {
                p_77648_3_.playSoundEffect((double)((float)p_77648_4_ + 0.5F), (double)((float)p_77648_5_ + 0.5F), (double)((float)p_77648_6_ + 0.5F), storedBlock.stepSound.func_150496_b(), (storedBlock.stepSound.getVolume() + 1.0F) / 2.0F, storedBlock.stepSound.getPitch() * 0.8F);
                --p_77648_1_.stackSize;
            }

            return true;
        }
        else
        {
            return false;
        }
    }
	
	public static void checkNBT(ItemStack itemStack) {
		if (itemStack == null) return;
		NBTTagCompound nbt = itemStack.hasTagCompound() ? itemStack.getTagCompound() : new NBTTagCompound();
		if (!nbt.hasKey(FACADE_BLOCK_KEY)) {
			Block def = Blocks.stone;
			if (itemStack.getItem() instanceof ItemBlockSlippery) {
				ItemBlockSlippery itemBlock = (ItemBlockSlippery) itemStack.getItem();
				if (itemBlock.field_150939_a instanceof BlockSlipperyStairs) {
					def = Blocks.oak_stairs;
				}
			}
			nbt.setString(FACADE_BLOCK_KEY, TileEntitySlippery.getNameFromBlock(def));
			itemStack.setTagCompound(nbt);
		}
	}
	
	public static Block getStoredBlock(ItemStack itemStack) {
		if (itemStack.hasTagCompound()) {
			Block def = Blocks.stone;
			if (itemStack.getItem() instanceof ItemBlockSlippery) {
				ItemBlockSlippery itemBlock = (ItemBlockSlippery) itemStack.getItem();
				if (itemBlock.field_150939_a instanceof BlockSlipperyStairs) {
					def = Blocks.oak_stairs;
				}
			}
			return TileEntitySlippery.getBlockFromName(itemStack.getTagCompound().getString(FACADE_BLOCK_KEY), def);
		}
		return Blocks.stone;
	}
	
	public static int getSlipperiness(Block block) {
		return (int) ((block.slipperiness - 0.94F) / 0.04F);
	}

}
