package net.meteor.common.block;

import net.meteor.common.LangLocalization;
import net.meteor.common.MeteorsMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockMeteorsMod extends Block
{
	public BlockMeteorsMod(Material par2Material)
	{
		super(par2Material);
		this.setCreativeTab(MeteorsMod.meteorTab);
	}
	
	@Override
	public Block setBlockTextureName(String s) {
		return super.setBlockTextureName(MeteorsMod.MOD_ID + ":" + s);
	}

	@Override
	public String getLocalizedName()
	{
		return LangLocalization.get(this.getUnlocalizedName() + ".name");
	}
}