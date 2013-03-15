package net.minecraft.src.meteor;

import net.meteor.common.EntitySummoner;
import net.meteor.common.MeteorsMod;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSummoner extends Render
{
	private int index;

	public RenderSummoner(int par1)
	{
		this.index = par1;
	}

	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
	{
		EntitySummoner summoner = (EntitySummoner)par1Entity;
		if (!summoner.isRandom)
			this.index = MeteorsMod.itemMeteorSummoner.getIconFromDamage(summoner.mID + 1);
		else {
			this.index = MeteorsMod.itemMeteorSummoner.getIconFromDamage(0);
		}
		GL11.glPushMatrix();
		GL11.glTranslatef((float)par2, (float)par4, (float)par6);
		GL11.glEnable(32826);
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		this.loadTexture(MeteorsMod.textureFile);
		Tessellator var10 = Tessellator.instance;
		func_77026_a(var10, this.index);
		GL11.glDisable(32826);
		GL11.glPopMatrix();
	}

	private void func_77026_a(Tessellator par1Tessellator, int par2)
	{
		float var3 = (float)(par2 % 16 * 16 + 0) / 256.0F;
		float var4 = (float)(par2 % 16 * 16 + 16) / 256.0F;
		float var5 = (float)(par2 / 16 * 16 + 0) / 256.0F;
		float var6 = (float)(par2 / 16 * 16 + 16) / 256.0F;
		float var7 = 1.0F;
		float var8 = 0.5F;
		float var9 = 0.25F;
		GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		par1Tessellator.startDrawingQuads();
		par1Tessellator.setNormal(0.0F, 1.0F, 0.0F);
		par1Tessellator.addVertexWithUV((double)(0.0F - var8), (double)(0.0F - var9), 0.0D, (double)var3, (double)var6);
		par1Tessellator.addVertexWithUV((double)(var7 - var8), (double)(0.0F - var9), 0.0D, (double)var4, (double)var6);
		par1Tessellator.addVertexWithUV((double)(var7 - var8), (double)(var7 - var9), 0.0D, (double)var4, (double)var5);
		par1Tessellator.addVertexWithUV((double)(0.0F - var8), (double)(var7 - var9), 0.0D, (double)var3, (double)var5);
		par1Tessellator.draw();
	}
}