package net.meteor.common.block;

import java.util.Random;

import net.meteor.common.MeteorItems;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;

public class BlockRareFallenMeteor extends BlockMeteor
{
	public BlockRareFallenMeteor()
	{
		super();
	}

	@Override
	public Item getItemDropped(int i, Random random, int j)
	{
		return MeteorItems.itemRedMeteorGem;
	}

	@Override
	public int quantityDropped(int meta, int fortune, Random random)
	{
		return meta > 0 ? 1 + random.nextInt(fortune + 1) : 0;
	}
	
	@Override
	public int getExpDrop(IBlockAccess world, int metadata, int fortune) {
		return MathHelper.getRandomIntegerInRange(rand, 3, 6);
	}
	
	@Override
    public void registerBlockIcons(IIconRegister par1IconRegister) {
		super.registerBlockIcons(par1IconRegister);
		this.hotTex = par1IconRegister.registerIcon("meteors:Meteor_Hot_Rare");
	}
}