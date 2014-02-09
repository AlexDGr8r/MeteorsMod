package net.meteor.common;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;

public class GhostMeteorData extends WorldSavedData {
	
	private final static String key = "metGhostMets";
	private static HandlerMeteor tempHandle;
	
	private HandlerMeteor metHandler;

	public GhostMeteorData(String s) {
		super(s);
	}
	
	public static GhostMeteorData forWorld(World world, HandlerMeteor metH) {
		tempHandle = metH;
		MapStorage storage = world.perWorldStorage;
		GhostMeteorData result = (GhostMeteorData)storage.loadData(GhostMeteorData.class, key);
		if (result == null) {
			result = new GhostMeteorData(key);
			storage.setData(key, result);
		}
		result.metHandler = metH;
		tempHandle = null;
		return result;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		if (metHandler != null) {
			metHandler.ghostMets = loadGhostMeteors(tag);
		} else {
			tempHandle.ghostMets = loadGhostMeteors(tag);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		saveGhostMeteors(tag);
	}
	
	private ArrayList<GhostMeteor> loadGhostMeteors(NBTTagCompound tag) {
		ArrayList<GhostMeteor> gMets = new ArrayList<GhostMeteor>();
		for (int i = 1; i <= 3; i++) {
			if (tag.hasKey("GMet" + i)) {
			   	GhostMeteor gmet = GhostMeteor.fromNBTString(tag.getString("GMet" + i));
			   	if (gmet != null) {
			   		gMets.add(gmet);
			   	}
			}
		}
		return gMets;
	}

	private void saveGhostMeteors(NBTTagCompound tag) {
		Iterator<GhostMeteor> iter = metHandler.ghostMets.iterator();
		int i = 1;
		while (iter.hasNext() && i <= 3) {
			GhostMeteor gmet = iter.next();
			tag.setString("GMet" + i, gmet.toString());
			i++;
		}
		while (i < 4) {
			if (tag.hasKey("GMet" + i)) {
				tag.removeTag("GMet" + i);
			}
			i++;
		}
	}
	
	@Override
	public boolean isDirty() {
		return true;
	}

}
