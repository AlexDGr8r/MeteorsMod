package net.meteor.common;

import java.io.Serializable;

public class GhostMeteor
implements Serializable
{
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

	public void update()
	{
		this.tick += 1;

		if (this.tick >= this.tGoal)
			this.ready = true;
	}

	public int getRemainingTicks()
	{
		return this.tGoal - this.tick;
	}
}