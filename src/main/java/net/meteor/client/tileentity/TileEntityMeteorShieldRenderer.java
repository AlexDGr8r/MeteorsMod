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
		long time = shield.getWorldObj().getTotalWorldTime();

		GL11.glPushMatrix();
		GL11.glTranslatef((float)par2 + 0.5F, (float)par4 + 1.5F, (float)par6 + 0.5F);
		this.bindTexture(shieldTexture);
		GL11.glPushMatrix();
		GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
		this.modelShield.render(shield, 0.0F, 0.0F, 0.0F, 0.0F, par8, 0.0625F, time);
		GL11.glPopMatrix();
		GL11.glPopMatrix();

		if (level == 5 && shield.renderRay) {

			// Render shooting beam
			GL11.glPushMatrix();
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
			double heightDiff = shield.getWorldObj().getHeight() - par4;
			GL11.glTranslatef((float)par2 + 0.5F, (float)(par4 - (time % 3200 / 3200 * heightDiff)), (float)par6 + 0.5F);
			Tessellator tess = Tessellator.instance;
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_LIGHTING);
//			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDepthMask(false);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			
			double currBotY = time % 3200 / 3200 * heightDiff + par4;
			double topY = currBotY + 6;
			double x1 = par2;
			double x2 = par2 + 2;
			double z1 = par6;
			double z2 = par6 + 2;

			for (int i = 0; i < 4; i++) {
				GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
				tess.startDrawingQuads();
				tess.setColorRGBA(0, 255, 0, 120);
				tess.addVertex(-2.0D, -8.0D, 0.0D);
				tess.addVertex(2.0D, -8.0D, 0.0D);
				tess.addVertex(2.0D, 8.0D, 0.0D);
				tess.addVertex(-2.0D, 8.0D, 0.0D);
				tess.draw();
			}

			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDepthMask(true);
			GL11.glPopMatrix();

			// Render translucent outside

		}

		//		if ((level == 5) && (shield.renderRay)) {
		//			float var9 = shield.getBeaconModifier();
		//			GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
		//			EnumMeteor met = (EnumMeteor)MeteorsMod.proxy.lastMeteorPrevented.get(shield.owner);
		//			if (met == null) {
		//				met = EnumMeteor.METEORITE;
		//			}
		//			Tessellator var10 = Tessellator.instance;
		//			this.bindTexture(met.getBeamTexture());
		//			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0F);
		//			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0F);
		//			GL11.glDisable(GL11.GL_LIGHTING);
		//			GL11.glDisable(GL11.GL_CULL_FACE);
		//			GL11.glDisable(GL11.GL_BLEND);
		//			GL11.glDepthMask(true);
		//			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		//			float var11 = (float)shield.getWorldObj().getTotalWorldTime() + par8;
		//			float var12 = -var11 * 0.2F - (float)MathHelper.floor_float(-var11 * 0.1F);
		//			byte var13 = 1;
		//			double var14 = (double)var11 * 0.025D * (1.0D - (double)(var13 & 1) * 2.5D);
		//			var10.startDrawingQuads();
		//			var10.setColorRGBA(255, 255, 255, 32);
		//			double var16 = (double)var13 * 0.2D;
		//			double var18 = 0.5D + Math.cos(var14 + 2.356194490192345D) * var16;
		//			double var20 = 0.5D + Math.sin(var14 + 2.356194490192345D) * var16;
		//			double var22 = 0.5D + Math.cos(var14 + (Math.PI / 4D)) * var16;
		//			double var24 = 0.5D + Math.sin(var14 + (Math.PI / 4D)) * var16;
		//			double var26 = 0.5D + Math.cos(var14 + 3.9269908169872414D) * var16;
		//			double var28 = 0.5D + Math.sin(var14 + 3.9269908169872414D) * var16;
		//			double var30 = 0.5D + Math.cos(var14 + 5.497787143782138D) * var16;
		//			double var32 = 0.5D + Math.sin(var14 + 5.497787143782138D) * var16;
		//			double var34 = (double)(256.0F * var9);
		//			double var36 = 0.0D;
		//			double var38 = 1.0D;
		//			double var40 = (double)(-1.0F + var12);
		//			double var42 = (double)(256.0F * var9) * (0.5D / var16) + var40;
		//			var10.addVertexWithUV(par2 + var18, par4 + 1 + var34, par6 + var20, var38, var42);
		//			var10.addVertexWithUV(par2 + var18, par4 + 1, par6 + var20, var38, var40);
		//			var10.addVertexWithUV(par2 + var22, par4 + 1, par6 + var24, var36, var40);
		//			var10.addVertexWithUV(par2 + var22, par4 + 1 + var34, par6 + var24, var36, var42);
		//			var10.addVertexWithUV(par2 + var30, par4 + 1 + var34, par6 + var32, var38, var42);
		//			var10.addVertexWithUV(par2 + var30, par4 + 1, par6 + var32, var38, var40);
		//			var10.addVertexWithUV(par2 + var26, par4 + 1, par6 + var28, var36, var40);
		//			var10.addVertexWithUV(par2 + var26, par4 + 1 + var34, par6 + var28, var36, var42);
		//			var10.addVertexWithUV(par2 + var22, par4 + 1 + var34, par6 + var24, var38, var42);
		//			var10.addVertexWithUV(par2 + var22, par4 + 1, par6 + var24, var38, var40);
		//			var10.addVertexWithUV(par2 + var30, par4 + 1, par6 + var32, var36, var40);
		//			var10.addVertexWithUV(par2 + var30, par4 + 1 + var34, par6 + var32, var36, var42);
		//			var10.addVertexWithUV(par2 + var26, par4 + 1 + var34, par6 + var28, var38, var42);
		//			var10.addVertexWithUV(par2 + var26, par4 + 1, par6 + var28, var38, var40);
		//			var10.addVertexWithUV(par2 + var18, par4 + 1, par6 + var20, var36, var40);
		//			var10.addVertexWithUV(par2 + var18, par4 + 1 + var34, par6 + var20, var36, var42);
		//			var10.draw();
		//			GL11.glEnable(GL11.GL_BLEND);
		//			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		//			GL11.glDepthMask(false);
		//			var10.startDrawingQuads();
		//			var10.setColorRGBA(255, 255, 255, 32);
		//			double var44 = 0.2D;
		//			double var15 = 0.2D;
		//			double var17 = 0.8D;
		//			double var19 = 0.2D;
		//			double var21 = 0.2D;
		//			double var23 = 0.8D;
		//			double var25 = 0.8D;
		//			double var27 = 0.8D;
		//			double var29 = (double)(256.0F * var9);
		//			double var31 = 0.0D;
		//			double var33 = 1.0D;
		//			double var35 = (double)(-1.0F + var12);
		//			double var37 = (double)(256.0F * var9) + var35;
		//			var10.addVertexWithUV(par2 + var44, par4 + 1 + var29, par6 + var15, var33, var37);
		//			var10.addVertexWithUV(par2 + var44, par4 + 1, par6 + var15, var33, var35);
		//			var10.addVertexWithUV(par2 + var17, par4 + 1, par6 + var19, var31, var35);
		//			var10.addVertexWithUV(par2 + var17, par4 + 1 + var29, par6 + var19, var31, var37);
		//			var10.addVertexWithUV(par2 + var25, par4 + 1 + var29, par6 + var27, var33, var37);
		//			var10.addVertexWithUV(par2 + var25, par4 + 1, par6 + var27, var33, var35);
		//			var10.addVertexWithUV(par2 + var21, par4 + 1, par6 + var23, var31, var35);
		//			var10.addVertexWithUV(par2 + var21, par4 + 1 + var29, par6 + var23, var31, var37);
		//			var10.addVertexWithUV(par2 + var17, par4 + 1 + var29, par6 + var19, var33, var37);
		//			var10.addVertexWithUV(par2 + var17, par4 + 1, par6 + var19, var33, var35);
		//			var10.addVertexWithUV(par2 + var25, par4 + 1, par6 + var27, var31, var35);
		//			var10.addVertexWithUV(par2 + var25, par4 + 1 + var29, par6 + var27, var31, var37);
		//			var10.addVertexWithUV(par2 + var21, par4 + 1 + var29, par6 + var23, var33, var37);
		//			var10.addVertexWithUV(par2 + var21, par4 + 1, par6 + var23, var33, var35);
		//			var10.addVertexWithUV(par2 + var44, par4 + 1, par6 + var15, var31, var35);
		//			var10.addVertexWithUV(par2 + var44, par4 + 1 + var29, par6 + var15, var31, var37);
		//			var10.draw();
		//			GL11.glEnable(GL11.GL_LIGHTING);
		//			GL11.glEnable(GL11.GL_TEXTURE_2D);
		//			GL11.glDepthMask(true);
		//		}
	}
}