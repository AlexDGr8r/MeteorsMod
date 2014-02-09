package net.meteor.common.crash;

import java.util.ArrayList;
import java.util.Random;

import net.meteor.common.EnumMeteor;
import net.meteor.common.MeteorsMod;
import net.meteor.common.entity.EntityAlienCreeper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class CrashMeteorite extends WorldGenerator
{
	protected int crashSize;
	protected Explosion explosion;
	protected EnumMeteor meteorType;

	public CrashMeteorite(int Size, Explosion expl, EnumMeteor metType)
	{
		this.crashSize = MathHelper.clamp_int(Size, 1, 3);
		this.explosion = expl;
		this.meteorType = metType;
	}

	public boolean generate(World world, Random random, int i, int j, int k)
	{
		Block meteor = this.meteorType.getMaterial();
		Block rareMeteor = this.meteorType.getRareMaterial();

		//Initial spreading
		for (int y = j + MeteorsMod.instance.ImpactSpread * this.crashSize; y >= j - MeteorsMod.instance.ImpactSpread * this.crashSize; y--) {
			for (int startX = i + MeteorsMod.instance.ImpactSpread * this.crashSize; startX >= i - MeteorsMod.instance.ImpactSpread * this.crashSize; startX--) {
				for (int startZ = k + MeteorsMod.instance.ImpactSpread * this.crashSize; startZ >= k - MeteorsMod.instance.ImpactSpread * this.crashSize; startZ--) {
					if ((!world.isAirBlock(startX, y, startZ)) && (meteorsAboveAndBelow(world, startX, y, startZ) == 0) && (random.nextInt(10) + 1 > 7) && (checkBlockIDs(world, startX, y, startZ))) {
						Block theBlock = random.nextInt(45) == 25 ? rareMeteor : meteor;
						if (Block.isEqualTo(theBlock, Blocks.ice) || Block.isEqualTo(theBlock, Blocks.lava)) {
							world.setBlock(startX, y, startZ, theBlock, 0, 2);
						} else {
							world.setBlock(startX, y, startZ, theBlock, random.nextInt(4) + 1, 3);
						}
					}
				}
			}

		}

		//Bottom layer spreading for a higher concentration in center
		for (int y = j - MeteorsMod.instance.ImpactSpread * this.crashSize; y >= j - (MeteorsMod.instance.ImpactSpread * this.crashSize + 1); y--) {
			for (int startX = i + MeteorsMod.instance.ImpactSpread * this.crashSize; startX >= i - MeteorsMod.instance.ImpactSpread * this.crashSize; startX--) {
				for (int startZ = k + MeteorsMod.instance.ImpactSpread * this.crashSize; startZ >= k - MeteorsMod.instance.ImpactSpread * this.crashSize; startZ--) {
					if ((!world.isAirBlock(startX, y, startZ)) && (meteorsAboveAndBelow(world, startX, y, startZ) == 0) && (checkBlockIDs(world, startX, y, startZ))) {
						Block theBlock = random.nextInt(45) == 25 ? rareMeteor : meteor;
						if (Block.isEqualTo(theBlock, Blocks.ice) || Block.isEqualTo(theBlock, Blocks.lava)) {
							world.setBlock(startX, y, startZ, theBlock, 0, 2);
						} else {
							world.setBlock(startX, y, startZ, theBlock, random.nextInt(4) + 1, 3);
						}
					}
				}

			}

		}

		afterCraterFormed(world, random, i, j, k);

		return true;
	}

	public void afterCraterFormed(World world, Random random, int i, int j, int k)
	{
		int aliencreepers = random.nextInt(5);
		ArrayList arraylist = new ArrayList();
		arraylist.addAll(this.explosion.affectedBlockPositions);
		for (int j1 = arraylist.size() - 1; (j1 >= 0) && (aliencreepers > 0); j1--)
		{
			ChunkPosition chunkposition1 = (ChunkPosition)arraylist.get(j1);
			int l = chunkposition1.chunkPosX;
			int j11 = chunkposition1.chunkPosY;
			int l1 = chunkposition1.chunkPosZ;
			boolean j2 = world.isAirBlock(l, j11, l1);
			Block k2 = world.getBlock(l, j11 - 1, l1);
			if (j2 && k2.isOpaqueCube() && (random.nextInt(10) > 4)) {
				EntityAlienCreeper creeper = new EntityAlienCreeper(world);
				creeper.setLocationAndAngles(l, j11, l1, 0.0F, 0.0F);
				world.spawnEntityInWorld(creeper);
				aliencreepers--;
			}
		}
	}

	public void afterCrashCompleted(World world, int i, int j, int k) {}

	public int meteorsAbove(World world, int x, int y, int z) { int ceiling = y + 6 * this.crashSize;
	int mAbove = 0;
	for (int y1 = y; y1 <= ceiling; y1++) {
		Block block = world.getBlock(x, y1, z);
		if (Block.isEqualTo(block, this.meteorType.getMaterial()) || Block.isEqualTo(block, this.meteorType.getRareMaterial())) {
			mAbove++;
		}
	}
	return mAbove; }

	public int meteorsBelow(World world, int x, int y, int z)
	{
		int floor = y - 6 * this.crashSize;
		int mBelow = 0;
		for (int y1 = y; y1 >= floor; y1--) {
			Block block = world.getBlock(x, y1, z);
			if (Block.isEqualTo(block, this.meteorType.getMaterial()) || Block.isEqualTo(block, this.meteorType.getRareMaterial())) {
				mBelow++;
			}
		}
		return mBelow;
	}

	public int meteorsAboveAndBelow(World world, int x, int y, int z) {
		Block bl = world.getBlock(x, y, z);
		return 0 - (Block.isEqualTo(bl, meteorType.getRareMaterial()) ? 1 : meteorsAbove(world, x, y, z) + meteorsBelow(world, x, y, z) - (Block.isEqualTo(bl, meteorType.getMaterial()) ? 1 : 0));
	}

	protected boolean checkBlockIDs(World world, int i, int j, int k) {
		Block bl = world.getBlock(i, j, k);
		return (!Block.isEqualTo(bl, Blocks.bedrock) && !Block.isEqualTo(bl, meteorType.getMaterial()) && 
				!Block.isEqualTo(bl, meteorType.getRareMaterial()) && !Block.isEqualTo(bl, Blocks.flowing_water) && 
				!Block.isEqualTo(bl, Blocks.water));
	}
}