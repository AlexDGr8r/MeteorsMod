package net.meteor.common.block;

import net.meteor.common.tileentity.TileEntityMeteorTimer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMeteorTimer extends BlockContainerMeteorsMod {
	
	@SideOnly(Side.CLIENT)
	private IIcon timerSide;

	public BlockMeteorTimer() {
		super(Material.redstoneLight);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	public int getRenderType() {
        return -1;
    }
	
	@Override
	public boolean canProvidePower() {
		return true;
	}
	
	/**
     * Returns true if the block is emitting indirect/weak redstone power on the specified side. If isBlockNormalCube
     * returns true, standard redstone propagation rules will apply instead and this will not be called. Args: World, X,
     * Y, Z, side. Note that the side is reversed - eg it is 1 (up) when checking the bottom of the block.
     */
	@Override
    public int isProvidingWeakPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
    	return par1IBlockAccess.getBlockMetadata(par2, par3, par4);
    }
	
	@Override
	public int getMobilityFlag() {
		return 1;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return AxisAlignedBB.getAABBPool().getAABB((double)par2 + this.minX, (double)par3 + this.minY, (double)par4 + this.minZ, (double)par2 + this.maxX, (double)par3 + this.maxY + 0.125D, (double)par4 + this.maxZ);
    }
	
	@Override
	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int par6, float par7, float par8, float par9)
	{
		if (!world.isRemote) {
			TileEntityMeteorTimer tEntity = (TileEntityMeteorTimer)world.getTileEntity(i, j, k);
			tEntity.quickMode = !tEntity.quickMode;
			if (tEntity.quickMode) {
				player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("MeteorTimer.modeChange.two")));
			} else {
				player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("MeteorTimer.modeChange.one")));
			}
		}
		
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileEntityMeteorTimer();
	}

}
