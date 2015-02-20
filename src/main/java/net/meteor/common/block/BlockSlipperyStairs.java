package net.meteor.common.block;

import java.util.Random;

import net.meteor.common.item.ItemBlockSlippery;
import net.meteor.common.tileentity.TileEntitySlippery;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class BlockSlipperyStairs extends BlockStairs implements ITileEntityProvider {

	public BlockSlipperyStairs(float slipperiness) {
		super(Blocks.packed_ice, 0);
		this.slipperiness = slipperiness;
		this.setCreativeTab(null);
	}

	public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_, int p_149749_4_, Block p_149749_5_, int p_149749_6_)
    {
        super.breakBlock(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_, p_149749_6_);
        p_149749_1_.removeTileEntity(p_149749_2_, p_149749_3_, p_149749_4_);
    }

    public boolean onBlockEventReceived(World p_149696_1_, int p_149696_2_, int p_149696_3_, int p_149696_4_, int p_149696_5_, int p_149696_6_)
    {
        super.onBlockEventReceived(p_149696_1_, p_149696_2_, p_149696_3_, p_149696_4_, p_149696_5_, p_149696_6_);
        TileEntity tileentity = p_149696_1_.getTileEntity(p_149696_2_, p_149696_3_, p_149696_4_);
        return tileentity != null ? tileentity.receiveClientEvent(p_149696_5_, p_149696_6_) : false;
    }

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntitySlippery();
	}
	
	/**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
	@Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    /**
     * The type of render function that is called for this block
     */
	@Override
    public int getRenderType() {
        return -1;
    }
    
    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
	@Override
    public boolean renderAsNormalBlock() {
        return false;
    }
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack item) {
		
		TileEntitySlippery tileEntity = (TileEntitySlippery) world.getTileEntity(x, y, z);
		tileEntity.setFacadeBlockName(item.getTagCompound().getString(ItemBlockSlippery.FACADE_BLOCK_KEY));
		tileEntity.markDirty();
		
		super.onBlockPlacedBy(world, x, y, z, entity, item);
	}
	
	@Override
	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest) {
		if (!world.isRemote && !player.capabilities.isCreativeMode) {
			TileEntitySlippery teSlippery = (TileEntitySlippery) world.getTileEntity(x, y, z);
			ItemStack slipItem = new ItemStack(this, 1, teSlippery.getFacadeBlock().damageDropped(world.getBlockMetadata(x, y, z)));
	    	NBTTagCompound nbt = slipItem.hasTagCompound() ? slipItem.getTagCompound() : new NBTTagCompound();
	    	nbt.setString(ItemBlockSlippery.FACADE_BLOCK_KEY, teSlippery.getFacadeBlockName());
	    	slipItem.setTagCompound(nbt);
	    	this.dropBlockAsItem(world, x, y, z, slipItem);
		}
    	return super.removedByPlayer(world, player, x, y, z, willHarvest);
	}
	
	@Override
	public int quantityDropped(int meta, int fortune, Random random) {
		return 0;
	}
	
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
		Item item = getItem(world, x, y, z);

        if (item == null)
        {
            return null;
        }
        
        BlockSlipperyStairs block = (BlockSlipperyStairs)world.getBlock(x, y, z);
        ItemStack stack = new ItemStack(item, 1, block.getDamageValue(world, x, y, z));
        NBTTagCompound nbt = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
        TileEntitySlippery tileEntity = (TileEntitySlippery)world.getTileEntity(x, y, z);
		nbt.setString(ItemBlockSlippery.FACADE_BLOCK_KEY, tileEntity.getFacadeBlockName());
		stack.setTagCompound(nbt);
		return stack;
	}
	
}
