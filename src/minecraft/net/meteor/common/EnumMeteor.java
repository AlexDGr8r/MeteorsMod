package net.meteor.common;

import java.io.Serializable;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

public enum EnumMeteor
implements Serializable
{
	METEORITE(0, MeteorBlocks.blockMeteor.blockID, MeteorBlocks.blockRareMeteor.blockID, true, "met"), 
	FREZARITE(1, MeteorBlocks.blockFrezarite.blockID, Block.ice.blockID, false, "freza"), 
	KREKNORITE(2, MeteorBlocks.blockKreknorite.blockID, Block.lavaStill.blockID, true, "krekno"), 
	UNKNOWN(3, 0, 0, true, "unk"), 
	KITTY(4, 0, 0, false, "kitty");

	private final int ID;
	private final int MaterialID;
	private final int RareMaterialID;
	private final boolean fieryExplosion;
	private final String beamTex;

	private EnumMeteor(int id, int bID, int rID, boolean fiery, String bTex) { 
		this.ID = id;
		this.MaterialID = bID;
		this.RareMaterialID = rID;
		this.fieryExplosion = fiery;
		this.beamTex = bTex; 
	}

	public int getMaterialID()
	{
		return this.MaterialID;
	}

	public int getRareMaterialID() {
		return this.RareMaterialID;
	}

	public int getID() {
		return this.ID;
	}

	public boolean getFieryExplosion() {
		return this.fieryExplosion;
	}

	public static EnumMeteor getTypeFromID(int i) {
		switch (i) {
		case 0:
			return METEORITE;
		case 1:
			return FREZARITE;
		case 2:
			return KREKNORITE;
		case 3:
			return UNKNOWN;
		case 4:
			return KITTY;
		}
		return METEORITE;
	}

	public ResourceLocation getBeamTexture() {
		return new ResourceLocation("meteors", "textures/entities/" + beamTex + "beam.png");
	}
}