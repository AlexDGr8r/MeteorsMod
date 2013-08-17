package net.meteor.common.block;

import java.util.Random;

import net.meteor.common.ClientProxy;
import net.meteor.common.MeteorsMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockFrezarite extends BlockMeteorsMod
{
	public BlockFrezarite(int i)
	{
		super(i, Material.rock);
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
		if (world.getBlockMetadata(i, j, k) > 0) {
			renderGlowParticles(world, i, j, k, random);
		}
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
	public int tickRate(World world)
	{
		return 30;
	}

	@Override
	public void updateTick(World world, int i, int j, int k, Random random)
	{
		int meta = world.getBlockMetadata(i, j, k);
		float temp = world.getBiomeGenForCoords(i, k).temperature;
		if ((meta > 0) && (temp > 0.15F)) {
			world.setBlockMetadataWithNotify(i, j, k, --meta, 2);
			if (meta <= 0) {
				world.setBlock(i, j, k, Block.waterStill.blockID, 0, 3);
				world.playSound((double)((float)i + 0.5F), (double)((float)j + 0.5F), (double)((float)k + 0.5F), "random.glass", 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
			}
		}
	}
	
	@Override
	public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6, int par7) {
        super.dropBlockAsItemWithChance(par1World, par2, par3, par4, par5, par6, par7);

        if (this.idDropped(par5, par1World.rand, par7) != this.blockID) {
        	int xp;
        	if (par5 > 0) {
        		xp = MathHelper.getRandomIntegerInRange(par1World.rand, 2, 5);
        	} else {
        		xp = MathHelper.getRandomIntegerInRange(par1World.rand, 0, 2);
        	}
        	this.dropXpOnBlockBreak(par1World, par2, par3, par4, xp);
        }
    }
	
}