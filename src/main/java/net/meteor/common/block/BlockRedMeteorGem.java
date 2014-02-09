package net.meteor.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRedMeteorGem extends BlockMeteorsMod {

	public BlockRedMeteorGem() {
		super(Material.rock);
	}
	
	@Override
	public boolean isBeaconBase(IBlockAccess worldObj, int x, int y, int z, int beaconX, int beaconY, int beaconZ) {
		return true;
	}

}
