package net.meteor.common.crash;

import java.util.Random;

import net.meteor.common.EnumMeteor;
import net.meteor.common.entity.EntityCometKitty;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class CrashKitty extends CrashMeteorite
{
	public CrashKitty(int Size, Explosion expl, EnumMeteor metType)
	{
		super(Size, expl, metType);
	}

	@Override
	public boolean generate(World world, Random random, int i, int j, int k) {
		return true;
	}

	@Override
	public void afterCrashCompleted(World world, int i, int j, int k) {
		int kitties = (int)Math.pow(this.crashSize, 2.0D);
		for (int c = 0; c < kitties; c++) {
			EntityCometKitty kitty = new EntityCometKitty(world);
			kitty.setPosition(i, j, k);
			world.spawnEntityInWorld(kitty);
		}
	}
}