package net.meteor.common.climate;

import java.io.Serializable;

import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.ChunkCoordIntPair;

public class CrashedChunkSet
implements Serializable
{
	private int[] theChunksX = new int[49];
	private int[] theChunksZ = new int[49];
	private int xChunkOrigin;
	private int zChunkOrigin;
	private int xOrigin;
	private int yOrigin;
	private int zOrigin;
	public int age;

	public CrashedChunkSet(int xChunkOrigin, int zChunkOrigin, int xPosOrigin, int yPosOrigin, int zPosOrigin) {
		int i = 0;
		for (int x = xChunkOrigin - 3; x <= xChunkOrigin + 3; x++) {
			for (int z = zChunkOrigin - 3; z <= zChunkOrigin + 3; z++) {
				this.theChunksX[i] = x;
				this.theChunksZ[i] = z;
				i++;
			}
		}
		this.xChunkOrigin = xChunkOrigin;
		this.zChunkOrigin = zChunkOrigin;
		this.xOrigin = xPosOrigin;
		this.yOrigin = yPosOrigin;
		this.zOrigin = zPosOrigin;
		this.age = 0;
	}

	public boolean containsChunk(ChunkCoordIntPair coords) {
		for (int i = 0; i < 49; i++) {
			if ((coords.chunkXPos == this.theChunksX[i]) && (coords.chunkZPos == this.theChunksZ[i])) {
				return true;
			}
		}

		return false;
	}

	public ChunkCoordinates getCrashCoords() {
		return new ChunkCoordinates(this.xOrigin, this.yOrigin, this.zOrigin);
	}
	
	public String toString() {
		return xChunkOrigin + ":" + zChunkOrigin + ":" + xOrigin + ":" + yOrigin + ":" + zOrigin + ":" + age;
	}
	
	public static CrashedChunkSet fromNBTString(String s) {
		String[] props = s.split(":");
		if (props == null || props.length < 6) {
			return null;
		}
		int xcOrigin, zcOrigin, xOrigin, yOrigin, zOrigin, age;
		xcOrigin = Integer.parseInt(props[0]);
		zcOrigin = Integer.parseInt(props[1]);
		xOrigin = Integer.parseInt(props[2]);
		yOrigin = Integer.parseInt(props[3]);
		zOrigin = Integer.parseInt(props[4]);
		age = Integer.parseInt(props[5]);
		CrashedChunkSet ccSet = new CrashedChunkSet(xcOrigin, zcOrigin, xOrigin, yOrigin, zOrigin);
		ccSet.age = age;
		return ccSet;
	}
	
}