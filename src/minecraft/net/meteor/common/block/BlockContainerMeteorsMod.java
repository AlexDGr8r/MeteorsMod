package net.meteor.common.block;

import net.meteor.common.LangLocalization;
import net.meteor.common.MeteorsMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class BlockContainerMeteorsMod extends BlockContainer {

	protected BlockContainerMeteorsMod(int par1, Material par2Material) {
		super(par1, par2Material);
		this.setCreativeTab(MeteorsMod.meteorTab);
	}
	
	@Override
	public Block setTextureName(String s) {
		return super.setTextureName(MeteorsMod.MOD_ID + ":" + s);
	}
	
	@Override
	public String getLocalizedName()
	{
		return LangLocalization.get(this.getUnlocalizedName() + ".name");
	}

}
