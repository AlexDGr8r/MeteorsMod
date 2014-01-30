package net.meteor.common.block;

import java.util.Random;

import net.meteor.common.MeteorItems;
import net.minecraft.block.material.Material;

public class BlockMeteorOre extends BlockMeteorsMod
{
	public BlockMeteorOre(int i)
	{
		super(i, Material.iron);
	}

	@Override
	public int quantityDropped(int meta, int fortune, Random random)
	{
		return 1 + random.nextInt(fortune + 1);
	}

	@Override
	public int idDropped(int i, Random random, int j)
	{
		return MeteorItems.itemMeteorChips.itemID;
	}
	
}