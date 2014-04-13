package net.meteor.client.tileentity;

import net.meteor.client.model.ModelMeteorShield;
import net.meteor.common.EnumMeteor;
import net.meteor.common.MeteorsMod;
import net.meteor.common.tileentity.TileEntityMeteorShield;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityMeteorShieldRenderer extends TileEntitySpecialRenderer
{
	private static final ResourceLocation shieldTexture = new ResourceLocation(MeteorsMod.MOD_ID, "textures/entities/meteorShield.png");

	private ModelMeteorShield modelShield;

	public TileEntityMeteorShieldRenderer() {
		this.modelShield = new ModelMeteorShield();
	}

	public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f) {
		renderAModelAt((TileEntityMeteorShield)tileentity, d, d1, d2, f);
	}

	public void renderAModelAt(TileEntityMeteorShield shield, double par2, double par4, double par6, float par8) {

		int level = shield.getPowerLevel();
		int meta = shield.getBlockMetadata();

		GL11.glPushMatrix();
		GL11.glTranslatef((float)par2 + 0.5F, (float)par4 + 1.5F, (float)par6 + 0.5F);
		this.bindTexture(shieldTexture);
		GL11.glPushMatrix();
		GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
		GL11.glRotatef(meta * -90F, 0.0F, 1.0F, 0.0F);
		this.modelShield.render(level, par8, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, shield.age);
		GL11.glPopMatrix();
		GL11.glPopMatrix();

	}
}