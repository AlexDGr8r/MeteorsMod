package net.meteor.common;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockFrezarite extends BlockMeteorsMod
{
	public BlockFrezarite(int i, int j)
	{
		super(i, j, Material.rock);
		this.slipperiness = 0.98F;
		setTickRandomly(true);
	}

	@Override
	public int idDropped(int i, Random random, int j)
	{
		return MeteorsMod.itemFrezaCrystal.itemID;
	}

	@Override
	public int quantityDropped(int meta, int fortune, Random random)
	{
		return random.nextInt(1 + meta + random.nextInt(fortune + 1));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void randomDisplayTick(World world, int i, int j, int k, Random random)
	{
		if (world.getBlockMetadata(i, j, k) > 0)
			renderGlowParticles(world, i, j, k, random);
	}

	@SideOnly(Side.CLIENT)
	public void renderGlowParticles(World world, int i, int j, int k, Random random)
	{
		double d = 0.0625D;
		for (int l = 0; l < 6; l++)
		{
			double d1 = (float)i + random.nextFloat();
			double d2 = (float)j + random.nextFloat();
			double d3 = (float)k + random.nextFloat();
			if(l == 0 && !world.isBlockOpaqueCube(i, j + 1, k))
			{
				d2 = (double)(j + 1) + d;
			}
			if(l == 1 && !world.isBlockOpaqueCube(i, j - 1, k))
			{
				d2 = (double)(j + 0) - d;
			}
			if(l == 2 && !world.isBlockOpaqueCube(i, j, k + 1))
			{
				d3 = (double)(k + 1) + d;
			}
			if(l == 3 && !world.isBlockOpaqueCube(i, j, k - 1))
			{
				d3 = (double)(k + 0) - d;
			}
			if(l == 4 && !world.isBlockOpaqueCube(i + 1, j, k))
			{
				d1 = (double)(i + 1) + d;
			}
			if(l == 5 && !world.isBlockOpaqueCube(i - 1, j, k))
			{
				d1 = (double)(i + 0) - d;
			}
			if ((d1 < i) || (d1 > i + 1) || (d2 < 0.0D) || (d2 > j + 1) || (d3 < k) || (d3 > k + 1))
			{
				ClientProxy.spawnParticle("frezadust", d1, d2, d3, 0.0D, 0.0D, 0.0D, world, -1);
			}
		}
	}

	@Override
	public int tickRate()
	{
		return 30;
	}

	@Override
	public void updateTick(World world, int i, int j, int k, Random random)
	{
		int meta = world.getBlockMetadata(i, j, k);
		float temp = world.getBiomeGenForCoords(i, k).temperature;
		if ((meta > 0) && (temp > 0.15F)) {
			world.setBlockMetadata(i, j, k, --meta);
			if (meta <= 0)
				world.setBlockWithNotify(i, j, k, Block.waterStill.blockID);
		}
	}
}