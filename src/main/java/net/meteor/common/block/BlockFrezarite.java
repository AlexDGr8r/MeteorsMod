package net.meteor.common.block;

import java.util.Random;

import net.meteor.common.ClientProxy;
import net.meteor.common.MeteorItems;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockFrezarite extends BlockMeteorsMod
{
	
	private Random rand = new Random();
	
	public BlockFrezarite()
	{
		super(Material.rock);
		this.slipperiness = 0.98F;
		setTickRandomly(true);
	}

	@Override
	public Item getItemDropped(int i, Random random, int j)
	{
		return MeteorItems.itemFrezaCrystal;
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
			if ((l == 0) && !world.getBlock(i, j + 1, k).isOpaqueCube())
			{
				d2 = j + 1 + d;
			}
			if ((l == 1) && !world.getBlock(i, j - 1, k).isOpaqueCube())
			{
				d2 = j + 0 - d;
			}
			if ((l == 2) && !world.getBlock(i, j, k + 1).isOpaqueCube())
			{
				d3 = k + 1 + d;
			}
			if ((l == 3) && !world.getBlock(i, j, k - 1).isOpaqueCube())
			{
				d3 = k + 0 - d;
			}
			if ((l == 4) && !world.getBlock(i + 1, j, k).isOpaqueCube())
			{
				d1 = i + 1 + d;
			}
			if ((l == 5) && !world.getBlock(i - 1, j, k).isOpaqueCube())
			{
				d1 = i + 0 - d;
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
				world.setBlock(i, j, k, Blocks.water, 0, 3);
				world.playSound((double)((float)i + 0.5F), (double)((float)j + 0.5F), (double)((float)k + 0.5F), "random.glass", 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
			}
		}
	}
	
	@Override
	public int getExpDrop(IBlockAccess world, int metadata, int fortune) {
		if (metadata > 0) {
			return MathHelper.getRandomIntegerInRange(rand, 2, 5);
		}
		return MathHelper.getRandomIntegerInRange(rand, 0, 2);
	}
	
}