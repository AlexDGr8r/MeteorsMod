package net.meteor.common;

import java.io.Serializable;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

public enum EnumMeteor
implements Serializable
{
	METEORITE(0, MeteorBlocks.blockMeteor, MeteorBlocks.blockRareMeteor, true, "met", EnumChatFormatting.LIGHT_PURPLE, new ItemStack(MeteorBlocks.blockMeteor, 1, 1)), 
	FREZARITE(1, MeteorBlocks.blockFrezarite, Blocks.ice, false, "freza", EnumChatFormatting.AQUA, new ItemStack(MeteorBlocks.blockFrezarite, 1, 1)), 
	KREKNORITE(2, MeteorBlocks.blockKreknorite, Blocks.lava, true, "krekno", EnumChatFormatting.RED, new ItemStack(MeteorBlocks.blockKreknorite, 1, 1)), 
	UNKNOWN(3, Blocks.air, Blocks.air, true, "unk", EnumChatFormatting.GRAY, new ItemStack(Blocks.chest, 1)), 
	KITTY(4, Blocks.air, Blocks.air, false, "kitty", EnumChatFormatting.GREEN, new ItemStack(Items.fish, 1));

	private final int ID;
	private final Block material;
	private final Block rareMaterial;
	private final boolean fieryExplosion;
	private final String beamTex;
	private final EnumChatFormatting chatColor;
	private final ItemStack representingItem;

	private EnumMeteor(int id, Block mat, Block rMat, boolean fiery, String bTex, EnumChatFormatting color, ItemStack item) { 
		this.ID = id;
		this.material = mat;
		this.rareMaterial = rMat;
		this.fieryExplosion = fiery;
		this.beamTex = bTex; 
		this.chatColor = color;
		this.representingItem = item;
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
	
	public EnumChatFormatting getChatColor() {
		return this.chatColor;
	}
	
	public ItemStack getRepresentingItem() {
		return this.representingItem;
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