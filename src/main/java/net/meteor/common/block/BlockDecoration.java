package net.meteor.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
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
			icons[i] = reg.registerIcon(blockTextureNames[i]);
		}
    }

}
