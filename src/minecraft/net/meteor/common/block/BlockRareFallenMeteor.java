package net.meteor.common.block;

import java.util.Random;

import net.meteor.common.MeteorsMod;

public class BlockRareFallenMeteor extends BlockMeteor
{
	public BlockRareFallenMeteor(int i)
	{
		super(i);
	}

	@Override
	public int idDropped(int i, Random random, int j)
	{
		return MeteorsMod.itemRedMeteorGem.itemID;
	}

	@Override
	public int quantityDropped(int meta, int fortune, Random random)
	{
		return meta > 0 ? 1 + random.nextInt(fortune + 1) : 0;
	}
}