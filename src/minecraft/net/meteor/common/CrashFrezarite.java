package net.meteor.common;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
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
			int l = chunkposition1.x;
			int j11 = chunkposition1.y;
			int l1 = chunkposition1.z;
			int j2 = world.getBlockId(l, j11, l1);
			int k2 = world.getBlockId(l, j11 - 1, l1);
			if ((j2 == 0) && Block.opaqueCubeLookup[k2] && (random.nextInt(2) == 0))
				world.setBlock(l, j11, l1, Block.snow.blockID, 0, 2);
		}
	}
}