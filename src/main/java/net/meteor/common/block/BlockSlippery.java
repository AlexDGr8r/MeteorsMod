package net.meteor.common.block;

import java.util.ArrayList;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.meteor.common.MeteorBlocks;
import net.meteor.common.item.ItemBlockSlippery;
import net.meteor.common.tileentity.TileEntitySlippery;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockSlippery extends BlockContainerMeteorsMod {

	public BlockSlippery(float slipperiness) {
		super(Material.ice);
		this.slipperiness = slipperiness;
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
	@SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
		this.blockIcon = reg.registerIcon("ice");
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
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
		return true;
	}
	
	public static boolean canBeSlippery(Block block) {
		if ((block instanceof BlockSlippery || block instanceof BlockSlipperyStairs) && block.slipperiness < 1.1F) {
			return true;
		}
		if (block.getRenderType() == 0 || block.getRenderType() == 31 || block.getRenderType() == 10 || block.getRenderType() == 39) {
			return !(block instanceof ITileEntityProvider) && !(block instanceof BlockSlab);
		}
		return false;
	}

}
