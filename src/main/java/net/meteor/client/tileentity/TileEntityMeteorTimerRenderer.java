package net.meteor.client.tileentity;

import java.util.Random;

import net.meteor.client.model.ModelMeteorTimer;
import net.meteor.common.MeteorsMod;
import net.meteor.common.tileentity.TileEntityMeteorTimer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityMeteorTimerRenderer extends TileEntitySpecialRenderer {
	
	private static final ResourceLocation timerTex = new ResourceLocation(MeteorsMod.MOD_ID, "textures/entities/metTimer.png");
	
	public ModelMeteorTimer modelMetTimer;
	
	private Random rand = new Random();
	
	public TileEntityMeteorTimerRenderer() {
		this.modelMetTimer = new ModelMeteorTimer();
		modelMetTimer.element_meteorite.rotateAngleY = rand.nextFloat() * 360F;
		modelMetTimer.element_frezarite.rotateAngleY = rand.nextFloat() * 360F;
		modelMetTimer.element_kreknorite.rotateAngleY = rand.nextFloat() * 360F;
	}

	@Override
	// float f appears to be partialTickTime so that's what I should use for cos function for the rotation of the elements
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f) {
		renderMeteorTimer((TileEntityMeteorTimer)tileentity, d0, d1, d2, f, true);
	}
	
	public void renderMeteorTimer(TileEntityMeteorTimer timer, double d0, double d1, double d2, float f, boolean bindTex) {
		GL11.glPushMatrix();
		if (bindTex) {
			this.bindTexture(timerTex);
		}
		modelMetTimer.element_meteorite.rotateAngleY += 0.02F;
		modelMetTimer.element_meteorite.rotateAngleY %= 360F;
		
		modelMetTimer.element_frezarite.rotateAngleY += 0.01F;
		modelMetTimer.element_frezarite.rotateAngleY %= 360F;
		
		modelMetTimer.element_kreknorite.rotateAngleY += 0.003F;
		modelMetTimer.element_kreknorite.rotateAngleY %= 360F;
		
		GL11.glTranslatef((float)d0, (float)d1, (float)d2);
		modelMetTimer.renderAll();
		GL11.glPopMatrix();
	}

}
