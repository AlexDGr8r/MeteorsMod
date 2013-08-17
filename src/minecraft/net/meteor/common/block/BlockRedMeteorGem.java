package net.meteor.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.world.World;

public class BlockRedMeteorGem extends BlockMeteorsMod {

	public BlockRedMeteorGem(int id) {
		super(id, Material.rock);
	}
	
	@Override
	public boolean isBeaconBase(World worldObj, int x, int y, int z, int beaconX, int beaconY, int beaconZ) {
		return true;
	}

}
