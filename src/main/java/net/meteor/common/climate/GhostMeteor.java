package net.meteor.common.climate;

import java.io.Serializable;

import net.meteor.common.EnumMeteor;

public class GhostMeteor
implements Serializable
{
	private int tagID = 0;
	private int tick;
	private int tGoal;
	public int size;
	public EnumMeteor type;
	public int x;
	public int z;
	public boolean ready;

	public GhostMeteor(int theX, int theZ, int theSize, int theGoal, EnumMeteor theType)
	{
		this.x = theX;
		this.z = theZ;
		this.size = theSize;
		this.tGoal = theGoal;
		this.type = theType;
		this.tick = 0;
		this.ready = false;
	}
	
	public GhostMeteor setTick(int t) {
		this.tick = t;
		return this;
	}

	public void update()
	{
		this.tick += 1;
		this.ready = tick >= tGoal;
	}

	public int getRemainingTicks()
	{
		return this.tGoal - this.tick;
	}
	
	public void setTagID(int i) {
		this.tagID = i;
	}
	
	@Override
	public String toString() {
		return size + ":" + x + ":" + z + ":" + tGoal + ":" + tick + ":" + type.getID();
	}
	
	public static GhostMeteor fromNBTString(String s) {
		String[] props = s.split(":");
		if (props == null || props[0] == null || props[0].equalsIgnoreCase("0") || props.length < 6) {
			return null;
		}
		int x, z, size, goal, tick, typeID;
		size = Integer.parseInt(props[0]);
		x = Integer.parseInt(props[1]);
		z = Integer.parseInt(props[2]);
		goal = Integer.parseInt(props[3]);
		tick = Integer.parseInt(props[4]);
		typeID = Integer.parseInt(props[5]);
		return new GhostMeteor(x, z, size, goal, EnumMeteor.getTypeFromID(typeID)).setTick(tick);
	}
}