package net.meteor.common.block;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.meteor.common.ClientProxy;
import net.meteor.common.MeteorsMod;
import net.meteor.common.tileentity.TileEntityFreezingMachine;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockFreezingMachine extends BlockContainerMeteorsMod {
	
	private IIcon FrontIcon;
	private IIcon FrontIcon_Empty;

	public BlockFreezingMachine() {
		super(Material.rock);
	}
	
	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLiving, ItemStack itemstack) {
		super.onBlockPlacedBy(par1World, par2, par3, par4, par5EntityLiving, itemstack);
		
		int l = MathHelper.floor_double((double)(par5EntityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (l == 0)
        {
        	par1World.setBlockMetadataWithNotify(par2, par3, par4, 2, 2); // 2
        }

        if (l == 1)
        {
        	par1World.setBlockMetadataWithNotify(par2, par3, par4, 5, 2); // 5
        }

        if (l == 2)
        {
        	par1World.setBlockMetadataWithNotify(par2, par3, par4, 3, 2); // 3
        }

        if (l == 3)
        {
        	par1World.setBlockMetadataWithNotify(par2, par3, par4, 4, 2); // 4
        }
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
		if (side == 3 && meta == 0) {
			return FrontIcon_Empty;
		}
		int i = meta > 5 ? meta - 4 : meta;
		IIcon front = meta > 5 ? this.FrontIcon : this.FrontIcon_Empty;
		return side == 1 ? this.blockIcon : (side == 0 ? this.blockIcon : (side != i ? this.blockIcon : front));
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
		this.blockIcon = reg.registerIcon(MeteorsMod.MOD_ID + ":Freezer_Side");
		this.FrontIcon = reg.registerIcon(MeteorsMod.MOD_ID + ":Freezer_Front");
		this.FrontIcon_Empty = reg.registerIcon(MeteorsMod.MOD_ID + ":Freezer_Front_Empty");
    }

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityFreezingMachine();
	}
	
	@Override
	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int par6, float par7, float par8, float par9)
	{
		player.openGui(MeteorsMod.instance, 1, world, i, j, k);
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void randomDisplayTick(World p_149734_1_, int p_149734_2_, int p_149734_3_, int p_149734_4_, Random p_149734_5_)
    {
		int l = p_149734_1_.getBlockMetadata(p_149734_2_, p_149734_3_, p_149734_4_);
        if (l > 5)
        {
            float f = (float)p_149734_2_ + 0.5F;
            float f1 = (float)p_149734_3_ + 0.0625F + p_149734_5_.nextFloat() * 14.0F / 16.0F;
            float f2 = (float)p_149734_4_ + 0.5F;
            float f3 = 0.52F;
            float f4 = p_149734_5_.nextFloat() * 0.6F - 0.3F;

            if (l == 8)
            {
                ClientProxy.spawnParticle("frezadust", (double)(f - f3), (double)f1, (double)(f2 + f4), 0.0D, 0.0D, 0.0D, p_149734_1_, -1);
            }
            else if (l == 9)
            {
                ClientProxy.spawnParticle("frezadust", (double)(f + f3), (double)f1, (double)(f2 + f4), 0.0D, 0.0D, 0.0D, p_149734_1_, -1);
            }
            else if (l == 6)
            {
                ClientProxy.spawnParticle("frezadust", (double)(f + f4), (double)f1, (double)(f2 - f3), 0.0D, 0.0D, 0.0D, p_149734_1_, -1);
            }
            else if (l == 7)
            {
                ClientProxy.spawnParticle("frezadust", (double)(f + f4), (double)f1, (double)(f2 + f3), 0.0D, 0.0D, 0.0D, p_149734_1_, -1);
            }
        }
    }

}
