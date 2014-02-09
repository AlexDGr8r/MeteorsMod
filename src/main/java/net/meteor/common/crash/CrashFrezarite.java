package net.meteor.common.crash;

import java.util.ArrayList;
import java.util.Random;

import net.meteor.common.EnumMeteor;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class CrashFrezarite extends CrashMeteorite
{
	public CrashFrezarite(int Size, Explosion expl, EnumMeteor metType)
	{
		super(Size, expl, metType);
	}

	public void afterCraterFormed(World world, Random random, int i, int j, int k) {
		ArrayList arraylist = new ArrayList();
		arraylist.addAll(this.explosion.affectedBlockPositions);
		for (int j1 = arraylist.size() - 1; j1 >= 0; j1--) {
			ChunkPosition chunkposition1 = (ChunkPosition)arraylist.get(j1);
			int l = chunkposition1.chunkPosX;
			int j11 = chunkposition1.chunkPosY;
			int l1 = chunkposition1.chunkPosZ;
			boolean j2 = world.isAirBlock(l, j11, l1);
			Block k2 = world.getBlock(l, j11 - 1, l1);
			if (j2 && k2.isOpaqueCube() && (random.nextInt(2) == 0))
				world.setBlock(l, j11, l1, Blocks.snow, 0, 2);
		}
	}
}