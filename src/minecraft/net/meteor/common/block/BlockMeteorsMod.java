package net.meteor.common.block;

import net.meteor.common.LangLocalization;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockMeteorsMod extends Block
{
	public BlockMeteorsMod(int id, Material par2Material)
	{
		super(id, par2Material);
	}

	@Override
	public String getLocalizedName()
	{
		return LangLocalization.get(this.getUnlocalizedName() + ".name");
	}
}