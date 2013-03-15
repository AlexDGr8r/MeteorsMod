package net.meteor.common;

import java.io.Serializable;

import net.minecraft.block.Block;

public enum EnumMeteor
  implements Serializable
{
  METEORITE(0, "fallingMeteor", MeteorsMod.blockMeteor.blockID, MeteorsMod.blockRareMeteor.blockID, true, 128, 0, 128, "met"), 
  FREZARITE(1, "frezaMeteor", MeteorsMod.blockFrezarite.blockID, Block.ice.blockID, false, 0, 255, 255, "freza"), 
  KREKNORITE(2, "kreknoMeteor", MeteorsMod.blockKreknorite.blockID, Block.lavaStill.blockID, true, 255, 0, 0, "krekno"), 
  UNKNOWN(3, "unknownMeteor", 0, 0, true, 128, 128, 128, "unk"), 
  KITTY(4, "kitty", 0, 0, false, 0, 128, 0, "kitty");

  private final int ID;
  private final String texture;
  private final int MaterialID;
  private final int RareMaterialID;
  private final boolean fieryExplosion;
  private final int red;
  private final int green;
  private final int blue;
  private final String beamTex;

  private EnumMeteor(int id, String tex, int bID, int rID, boolean fiery, int r, int g, int b, String bTex) { this.ID = id;
    this.texture = tex;
    this.MaterialID = bID;
    this.RareMaterialID = rID;
    this.fieryExplosion = fiery;
    this.red = r;
    this.green = g;
    this.blue = b;
    this.beamTex = bTex; }

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

  public String getTexture() {
    return this.texture;
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

  public int getRed() {
    return this.red;
  }

  public int getGreen() {
    return this.green;
  }

  public int getBlue() {
    return this.blue;
  }

  public String getBeamTexture() {
    return "/meteor/textures/" + this.beamTex + "beam.png";
  }
}