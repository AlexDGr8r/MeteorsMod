package net.meteor.common.block;

import java.util.List;

import net.meteor.common.MeteorsMod;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockDecoration extends BlockMeteorsMod {
	
	private String[] blockTextureNames;
	private IIcon[] icons;

	public BlockDecoration(String... textures) {
		super(Material.iron);
		this.blockTextureNames = textures;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
		return meta >= this.icons.length ? null : this.icons[meta];
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg)
    {
		icons = new IIcon[blockTextureNames.length];
		for (int i = 0; i < blockTextureNames.length; i++) {
			icons[i] = reg.registerIcon(MeteorsMod.MOD_ID + ":" + blockTextureNames[i]);
		}
    }
	
	/**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int p_149692_1_)
    {
        return p_149692_1_;
    }
	
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List p_149666_3_)
    {
        for (int i = 0; i < 3; ++i)
        {
            p_149666_3_.add(new ItemStack(p_149666_1_, 1, i));
        }
    }
	
	public boolean isBeaconBase(IBlockAccess worldObj, int x, int y, int z, int beaconX, int beaconY, int beaconZ)
    {
        return true;
    }

}
