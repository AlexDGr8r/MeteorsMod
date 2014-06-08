package net.meteor.common.crash;

import java.util.Random;

import net.meteor.common.EnumMeteor;
import net.meteor.common.entity.EntityCometKitty;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class CrashComet extends WorldGenerator {
	
	private EnumMeteor meteorType;
	
	public CrashComet(EnumMeteor type) {
		this.meteorType = type;
	}

	@Override
	public boolean generate(World world, Random random, int i, int j, int k) {
		
		if (meteorType == EnumMeteor.KITTY) {
			EntityCometKitty kitty = new EntityCometKitty(world);
			kitty.setPosition(i, j + 1, k);
			world.spawnEntityInWorld(kitty);
		}

		int chance = 15;
		int blocksPlaced = 0;
		int px = i;
		int py = j;
		int pz = k;
		
		for (int y = j - 2; y <= j + 2; y++) {
			for (int x = i - 2; x <= i + 2; x++) {
				for (int z = k - 2; z <= k + 2; z++) {
					Block block = world.getBlock(x, y, z);
					if (block.getMaterial().isReplaceable()) {
						px = x;
						py = y;
						pz = z;
						if (random.nextInt(100) + 1 > chance) {
							world.setBlock(x, y, z, meteorType.getMaterial(), random.nextInt(4) + 1, 3);
							blocksPlaced++;
							chance = 15 + (20 * blocksPlaced);
						}
					}
				}
			}
		}
		
		// Helps to ensure a block gets placed if extremely unlucky
		if (blocksPlaced == 0) {
			Block block = world.getBlock(px, py, pz);
			if (block.getMaterial().isReplaceable()) {
				world.setBlock(px, py, pz, meteorType.getMaterial(), random.nextInt(4) + 1, 3);
			}
		}
		
		return true;
	}

}
