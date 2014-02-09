package net.meteor.common;

import java.io.Serializable;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;

public enum EnumMeteor
implements Serializable
{
	METEORITE(0, MeteorBlocks.blockMeteor, MeteorBlocks.blockRareMeteor, true, "met"), 
	FREZARITE(1, MeteorBlocks.blockFrezarite, Blocks.ice, false, "freza"), 
	KREKNORITE(2, MeteorBlocks.blockKreknorite, Blocks.lava, true, "krekno"), 
	UNKNOWN(3, Blocks.air, Blocks.air, true, "unk"), 
	KITTY(4, Blocks.air, Blocks.air, false, "kitty");

	private final int ID;
	private final Block material;
	private final Block rareMaterial;
	private final boolean fieryExplosion;
	private final String beamTex;

	private EnumMeteor(int id, Block mat, Block rMat, boolean fiery, String bTex) { 
		this.ID = id;
		this.material = mat;
		this.rareMaterial = rMat;
		this.fieryExplosion = fiery;
		this.beamTex = bTex; 
	}

	public Block getMaterial()
	{
		return this.material;
	}

	public Block getRareMaterial() {
		return this.rareMaterial;
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