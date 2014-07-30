package net.meteor.client.tileentity;

import java.util.Random;

import net.meteor.client.model.ModelMeteorTimer;
import net.meteor.common.MeteorsMod;
import net.meteor.common.tileentity.TileEntityMeteorTimer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
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
		renderMeteorTimer((TileEntityMeteorTimer)tileentity, d0, d1, d2, f, true, true);
	}

	public void renderMeteorTimer(TileEntityMeteorTimer timer, double d0, double d1, double d2, float f, boolean bindTex, boolean displayInfo) {
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

		if (displayInfo) {
			drawStringAbove(timer, d0 + 0.5D, d1 + 0.65D, d2 + 0.5D);
		}
	}

	protected void drawStringAbove(TileEntityMeteorTimer timer, double x, double y, double z)
	{
		RenderManager renderManager = RenderManager.instance;
		MovingObjectPosition mop = Minecraft.getMinecraft().objectMouseOver;

		if (Minecraft.isGuiEnabled() && mop != null && timer.xCoord == mop.blockX && timer.yCoord == mop.blockY && timer.zCoord == mop.blockZ)
		{
			float f = 1.6F;
			float f1 = 0.016666668F * f;
			double d3 = getDistanceSqToEntity(renderManager.livingPlayer, timer);
			float f2 = 32F;

			if (d3 < (double)(f2 * f2))
			{
				String s = timer.quickMode ? "Quick Mode" : "Power Mode";
				FontRenderer fontrenderer = this.func_147498_b();
				GL11.glPushMatrix();
				GL11.glTranslatef((float)x + 0.0F, (float)y + 0.5F, (float)z);
				GL11.glNormal3f(0.0F, 1.0F, 0.0F);
				GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
				GL11.glScalef(-f1, -f1, f1);
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDepthMask(false);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				GL11.glEnable(GL11.GL_BLEND);
				OpenGlHelper.glBlendFunc(770, 771, 1, 0);
				Tessellator tessellator = Tessellator.instance;
				byte b0 = 0;
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				tessellator.startDrawingQuads();
				int j = fontrenderer.getStringWidth(s) / 2;
				tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
				tessellator.addVertex((double)(-j - 1), (double)(-1 + b0), 0.0D);
				tessellator.addVertex((double)(-j - 1), (double)(8 + b0), 0.0D);
				tessellator.addVertex((double)(j + 1), (double)(8 + b0), 0.0D);
				tessellator.addVertex((double)(j + 1), (double)(-1 + b0), 0.0D);
				tessellator.draw();
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, b0, 553648127);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
				GL11.glDepthMask(true);
				fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, b0, -1);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				GL11.glPopMatrix();
			}
		}
	}

	/**
	 * Returns the squared distance to the entity. Args: entity
	 */
	private double getDistanceSqToEntity(Entity p_70068_1_, TileEntityMeteorTimer timer)
	{
		double d0 = timer.xCoord + 0.5D - p_70068_1_.posX;
		double d1 = timer.yCoord + 0.5D - p_70068_1_.posY;
		double d2 = timer.zCoord + 0.5D - p_70068_1_.posZ;
		return d0 * d0 + d1 * d1 + d2 * d2;
	}

}
