package net.meteor.common;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockMeteorsMod extends Block
{
  public BlockMeteorsMod(int id, Material par2Material)
  {
    super(id, par2Material);
    setTextureFile(MeteorsMod.textureFile);
  }

  public BlockMeteorsMod(int id, int par2, Material mat) {
    super(id, par2, mat);
    setTextureFile(MeteorsMod.textureFile);
  }

  @Override
  public String translateBlockName()
  {
    return LangLocalization.get(this.getBlockName() + ".name");
  }
}