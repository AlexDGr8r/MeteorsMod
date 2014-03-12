package net.meteor.common;

public class MeteorShieldData implements IMeteorShield
{
	public int x;
	public int y;
	public int z;
	public int range;
	public String owner;

	public MeteorShieldData(int x, int y, int z, int r, String o)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.range = r;
		this.owner = o;
	}

	public boolean equals(Object o)
	{
		if (o == null) return false;
		IMeteorShield data = (IMeteorShield)o;
		return (this.x == data.getX()) && (this.y == data.getY()) && (this.z == data.getZ());
	}

	@Override
	public int getRange() {
		return range;
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public int getZ() {
		return z;
	}

	@Override
	public boolean isTileEntity() {
		return false;
	}

	@Override
	public String getOwner() {
		return owner;
	}

}