package net.minecraft.src.meteor.model;

import net.minecraft.client.model.ModelOcelot;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityOcelot;

import org.lwjgl.opengl.GL11;

public class ModelCometKitty extends ModelOcelot
{
	ModelRenderer helmet;

	public ModelCometKitty() {
		super();
		this.setTextureOffset("helmet.main", 34, 17);
		this.setTextureOffset("helmet.top", 48, 12);
		this.setTextureOffset("helmet.left", 14, 25);
		this.setTextureOffset("helmet.right", 24, 25);
		this.setTextureOffset("helmet.right2", 6, 29);
		this.setTextureOffset("helmet.left2", 0, 29);
		this.helmet = new ModelRenderer(this, "helmet");
		this.helmet.addBox("main", -3.5F, -4.0F, -5.0F, 7, 7, 8);
		this.helmet.addBox("top", -1F, -6F, -3F, 2, 1, 4);
		this.helmet.addBox("left", 4F, -2F, -3F, 1, 3, 4);
		this.helmet.addBox("right", -5F, -2F, -3F, 1, 3, 4);
		this.helmet.addBox("right2", -6F, -1F, -2F, 1, 1, 2);
		this.helmet.addBox("left2", 5F, -1F, -2F, 1, 1, 2);
		this.helmet.setRotationPoint(0.0F, 15.0F, -9.0F);
	}

	@Override
	public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity) {
		super.setRotationAngles(par1, par2, par3, par4, par5, par6, par7Entity);
		this.helmet.rotateAngleX = par5 / (180F / (float)Math.PI);
		this.helmet.rotateAngleY = par4 / (180F / (float)Math.PI);
	}

	@Override
	public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
		super.render(par1Entity, par2, par3, par4, par5, par6, par7);
		if (this.isChild) {
			float var8 = 2.0F;
			GL11.glPushMatrix();
			GL11.glScalef(1.5F / var8, 1.5F / var8, 1.5F / var8);
			GL11.glTranslatef(0.0F, 10.0F * par7, 4.0F * par7);
			this.helmet.render(par7);
			GL11.glPopMatrix();
		} else {
			this.helmet.render(par7);
		}
	}

	@Override
	public void setLivingAnimations(EntityLivingBase par1EntityLiving, float par2, float par3, float par4) {
		super.setLivingAnimations(par1EntityLiving, par2, par3, par4);
		EntityOcelot var5 = (EntityOcelot)par1EntityLiving;
		this.helmet.rotationPointY = 15.0F;
		this.helmet.rotationPointZ = -9.0F;
		if (var5.isSneaking()) {
			this.helmet.rotationPointY += 2.0F;
		} else if (var5.isSitting()) {
			this.helmet.rotationPointY += -3.3F;
			++this.helmet.rotationPointZ;
		}
	}
}