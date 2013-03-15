package net.meteor.common;

import java.util.Random;

import net.minecraft.block.material.Material;

public class BlockMeteorOre extends BlockMeteorsMod
{
  public BlockMeteorOre(int i, int j)
  {
    super(i, j, Material.iron);
  }

  public int quantityDropped(int meta, int fortune, Random random)
  {
    return 1 + random.nextInt(fortune + 1);
  }

  public int a(int i, Random random, int j)
  {
    return MeteorsMod.itemMeteorChips.itemID;
  }
}