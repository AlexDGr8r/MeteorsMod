package net.meteor.common.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

public class BlockMeteorOre extends BlockMeteorsMod
{
	private final Item droppedItem;
	
	public BlockMeteorOre(Item droppedItem)
	{
		super(Material.iron);
		this.setHarvestLevel("pickaxe", 2);
		this.droppedItem = droppedItem;
	}

	@Override
	public int quantityDropped(int meta, int fortune, Random random)
	{
		return 1 + random.nextInt(fortune + 1);
	}

	@Override
	public Item getItemDropped(int i, Random random, int j)
	{
		return this.droppedItem;
	}
	
}