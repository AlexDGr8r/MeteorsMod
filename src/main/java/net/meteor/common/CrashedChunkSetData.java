package net.meteor.common;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;

public class CrashedChunkSetData extends WorldSavedData {
	
	private final static String key = "metCrashedChunks";
	private static HandlerMeteor tempHandle;
	
	private HandlerMeteor metHandler;

	public CrashedChunkSetData(String s) {
		super(s);
	}
	
	public static CrashedChunkSetData forWorld(World world, HandlerMeteor metH) {
		tempHandle = metH;
		MapStorage storage = world.perWorldStorage;
		CrashedChunkSetData result = (CrashedChunkSetData)storage.loadData(CrashedChunkSetData.class, key);
		if (result == null) {
			result = new CrashedChunkSetData(key);
			storage.setData(key, result);
		}
		result.metHandler = metH;
		tempHandle = null;
		return result;
	}

	private ArrayList<CrashedChunkSet> loadCrashedChunks(NBTTagCompound tag) {
		ArrayList<CrashedChunkSet> cList = new ArrayList<CrashedChunkSet>();
		for (int i = 1; i <= 20; i++) {
			if (tag.hasKey("CCSet" + i)) {
				CrashedChunkSet ccSet = CrashedChunkSet.fromNBTString(tag.getString("CCSet" + i));
				if (ccSet != null) {
					cList.add(ccSet);
				}
			}
		}
		return cList;
	}

	private void saveCrashedChunks(NBTTagCompound tag) {
		Iterator<CrashedChunkSet> iter = metHandler.crashedChunks.iterator();
		int i = 1;
		while (iter.hasNext() && i <= 20) {
			CrashedChunkSet ccSet = iter.next();
			tag.setString("CCSet" + i, ccSet.toString());
			i++;
		}
		while (i < 21) {
			if (tag.hasKey("CCSet" + i)) {
				tag.removeTag("CCSet" + i);
			}
			i++;
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		if (metHandler != null) {
			metHandler.crashedChunks = loadCrashedChunks(tag);
		} else {
			tempHandle.crashedChunks = loadCrashedChunks(tag);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		saveCrashedChunks(tag);
	}
	
	@Override
	public boolean isDirty() {
		return true;
	}
	
}
