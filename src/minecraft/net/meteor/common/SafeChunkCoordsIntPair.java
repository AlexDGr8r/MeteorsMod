package net.meteor.common;

import net.minecraft.world.ChunkCoordIntPair;

public class SafeChunkCoordsIntPair extends ChunkCoordIntPair
{
	private String shieldOwner;

	public SafeChunkCoordsIntPair(int par1, int par2, String ShieldOwner)
	{
		super(par1, par2);
		this.shieldOwner = ShieldOwner;
	}

	public String getOwner() {
		return this.shieldOwner;
	}

	public boolean hasCoords(int x, int z) {
		return (x == this.chunkXPos) && (z == this.chunkZPos);
	}

	public boolean equals(Object par1Obj) {
		if (par1Obj == null) return false;
		SafeChunkCoordsIntPair var2 = (SafeChunkCoordsIntPair)par1Obj;
		if ((var2.getOwner() == null) || (getOwner() == null)) return false;
		return (var2.chunkXPos == this.chunkXPos) && (var2.chunkZPos == this.chunkZPos) && (var2.getOwner().equalsIgnoreCase(getOwner()));
	}
}