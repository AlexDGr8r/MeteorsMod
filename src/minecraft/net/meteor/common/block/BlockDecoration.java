package net.meteor.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;

public class BlockDecoration extends BlockMeteorsMod {
	
	private String[] blockTextureNames;
	private Icon[] icons;

	public BlockDecoration(int id, String... textures) {
		super(id, Material.iron);
		this.blockTextureNames = textures;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta)
    {
		return meta >= this.icons.length ? null : this.icons[meta];
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister reg)
    {
		icons = new Icon[blockTextureNames.length];
		for (int i = 0; i < blockTextureNames.length; i++) {
			icons[i] = reg.registerIcon(blockTextureNames[i]);
		}
    }

}
