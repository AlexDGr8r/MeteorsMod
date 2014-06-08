package net.meteor.common;

import net.meteor.common.entity.EntityComet;
import net.minecraft.nbt.NBTTagCompound;

public interface IMeteorShield {
	
	public int getRange();
	
	public int getPowerLevel();
	
	public int getX();
	
	public int getY();
	
	public int getZ();
	
	public boolean isTileEntity();
	
	public String getOwner();

}
