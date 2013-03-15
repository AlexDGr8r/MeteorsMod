package net.meteor.common;

public class ShieldData
{
	public int x;
	public int z;
	public int radius;
	public String owner;
	public boolean tbr;

	public ShieldData(int i, int k, int r, String o, boolean remove)
	{
		this.x = i;
		this.z = k;
		this.radius = r;
		this.owner = o;
		this.tbr = remove;
	}

	public boolean equals(Object o)
	{
		if (o == null) return false;
		ShieldData data = (ShieldData)o;
		if ((data.owner == null) || (this.owner == null)) return false;
		return (this.x == data.x) && (this.z == data.z) && (this.radius == data.radius) && (this.owner.equalsIgnoreCase(data.owner));
	}
}