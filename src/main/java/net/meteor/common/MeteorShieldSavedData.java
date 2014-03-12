package net.meteor.common;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;

public class MeteorShieldSavedData extends WorldSavedData {
	
	private final static String key = "meteorShields";
	private static HandlerMeteor tempHandle;
	
	private HandlerMeteor metHandler;
	private boolean loaded;

	public MeteorShieldSavedData(String s) {
		super(s);
		this.loaded = false;
	}
	
	public static MeteorShieldSavedData forWorld(World world, HandlerMeteor metH) {
		tempHandle = metH;
		MapStorage storage = world.perWorldStorage;
		MeteorShieldSavedData result = (MeteorShieldSavedData)storage.loadData(MeteorShieldSavedData.class, key);
		if (result == null) {
			result = new MeteorShieldSavedData(key);
			storage.setData(key, result);
		}
		result.metHandler = metH;
		tempHandle = null;
		return result;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		if (loaded) return;
		if (metHandler == null) metHandler = tempHandle;
		int numShields = nbt.getInteger("numShields");
		for (int i = 0; i < numShields; i++) {
			NBTTagCompound sNBT = nbt.getCompoundTag("s" + i);
			int x = sNBT.getInteger("x");
			int y = sNBT.getInteger("y");
			int z = sNBT.getInteger("z");
			int range = sNBT.getInteger("range");
			String owner = sNBT.getString("owner");
			
			metHandler.addShield(new MeteorShieldData(x, y, z, range, owner));
		}
		loaded = true;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		int numShields = metHandler.meteorShields.size();
		nbt.setInteger("numShields", numShields);
		for (int i = 0; i < numShields; i++) {
			NBTTagCompound sNBT = new NBTTagCompound();
			IMeteorShield shield = metHandler.meteorShields.get(i);
			sNBT.setInteger("x", shield.getX());
			sNBT.setInteger("y", shield.getY());
			sNBT.setInteger("z", shield.getZ());
			sNBT.setInteger("range", shield.getRange());
			sNBT.setString("owner", shield.getOwner());
			nbt.setTag("s" + i, sNBT);
		}
	}
	
	@Override
	public boolean isDirty() {
		return true;
	}

}
