package net.minecraft.src.meteor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.meteor.common.EntityMeteor;
import net.meteor.common.EnumMeteor;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderMeteor extends Render {
	
	ModelMeteor modelMeteor;
	
	public RenderMeteor() {
		modelMeteor = new ModelMeteor();
	}
	
	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
		renderMeteor((EntityMeteor)entity, d, d1, d2, f, f1);
	}
	
	public void renderMeteor(EntityMeteor entityMeteor, double d, double d1, double d2, float f, float f1) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float)d, (float)d1, (float)d2);
		loadTexture("/meteor/textures/" +entityMeteor.meteorType.getTexture()+ ".png");
		float f2 = 1.0F * (float)entityMeteor.size;
		GL11.glScalef(f2, f2, f2);
		modelMeteor.renderWithSize(entityMeteor, 0.0625F);
		GL11.glPopMatrix();
	}
	
}