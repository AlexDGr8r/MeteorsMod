package net.meteor.common.climate;

import net.minecraft.nbt.NBTTagCompound;

public class CrashLocation {
	
	public final int x;
	public final int y;
	public final int z;
	public final boolean inOrbit;
	
	public final CrashLocation prevCrash;
	
	public CrashLocation(int x, int y, int z, boolean inOrbit, CrashLocation prevCrash) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.inOrbit = inOrbit;
		this.prevCrash = prevCrash;
	}
	
	public void toNBT(NBTTagCompound nbt) {
		nbt.setInteger("LCX", x);
		nbt.setInteger("LCY", y);
		nbt.setInteger("LCZ", z);
		nbt.setBoolean("LCO", inOrbit);
	}
	
	public static CrashLocation fromNBT(NBTTagCompound nbt) {
		int x = nbt.getInteger("LCX");
		int y = nbt.getInteger("LCY");
		int z = nbt.getInteger("LCZ");
		boolean orbit = nbt.getBoolean("LCO");
		if (x == 0 && y == 0 && z == 0) {
			return null;
		}
		return new CrashLocation(x, y, z, orbit, null);
	}

}
