package net.minecraft.src.meteor.model;

import net.meteor.common.entity.EntityMeteor;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelMeteor extends ModelBase
{
	//fields
	ModelRenderer SmallMet;
	ModelRenderer Block1;
	ModelRenderer Block2;
	ModelRenderer Block3;
	ModelRenderer Block4;
	ModelRenderer Block5;
	ModelRenderer Block6;
	ModelRenderer Block7;
	ModelRenderer Block8;
	ModelRenderer Block9;
	ModelRenderer Block10;
	ModelRenderer Block11;
	ModelRenderer Block12;
	ModelRenderer Block13;
	ModelRenderer Block14;

	public ModelMeteor()
	{
		textureWidth = 64;
		textureHeight = 32;

		SmallMet = new ModelRenderer(this, 0, 0);
		SmallMet.addBox(-8F, -8F, -8F, 16, 16, 16);
		SmallMet.setRotationPoint(0F, 16F, 0F);
		SmallMet.setTextureSize(64, 32);
		SmallMet.mirror = true;
		Block1 = new ModelRenderer(this, 0, 0);
		Block1.addBox(-8F, -8F, -8F, 16, 16, 16);
		Block1.setRotationPoint(8F, 16F, 8F);
		Block1.setTextureSize(64, 32);
		Block1.mirror = true;
		Block2 = new ModelRenderer(this, 0, 0);
		Block2.addBox(-8F, -8F, -8F, 16, 16, 16);
		Block2.setRotationPoint(8F, 0F, 8F);
		Block2.setTextureSize(64, 32);
		Block2.mirror = true;
		Block3 = new ModelRenderer(this, 0, 0);
		Block3.addBox(-8F, -8F, -8F, 16, 16, 16);
		Block3.setRotationPoint(-8F, 0F, -8F);
		Block3.setTextureSize(64, 32);
		Block3.mirror = true;
		Block4 = new ModelRenderer(this, 0, 0);
		Block4.addBox(-8F, -8F, -8F, 16, 16, 16);
		Block4.setRotationPoint(8F, 0F, -8F);
		Block4.setTextureSize(64, 32);
		Block4.mirror = true;
		Block5 = new ModelRenderer(this, 0, 0);
		Block5.addBox(-8F, -8F, -8F, 16, 16, 16);
		Block5.setRotationPoint(-8F, 16F, -8F);
		Block5.setTextureSize(64, 32);
		Block5.mirror = true;
		Block6 = new ModelRenderer(this, 0, 0);
		Block6.addBox(-8F, -8F, -8F, 16, 16, 16);
		Block6.setRotationPoint(8F, 16F, -8F);
		Block6.setTextureSize(64, 32);
		Block6.mirror = true;
		Block7 = new ModelRenderer(this, 0, 0);
		Block7.addBox(-8F, -8F, -8F, 16, 16, 16);
		Block7.setRotationPoint(-8F, 0F, 8F);
		Block7.setTextureSize(64, 32);
		Block7.mirror = true;
		Block8 = new ModelRenderer(this, 0, 0);
		Block8.addBox(-8F, -8F, -8F, 16, 16, 16);
		Block8.setRotationPoint(-8F, 16F, 8F);
		Block8.setTextureSize(64, 32);
		Block8.mirror = true;
		Block9 = new ModelRenderer(this, 0, 0);
		Block9.addBox(-8F, -8F, -8F, 16, 16, 16);
		Block9.setRotationPoint(0F, 8F, 16F);
		Block9.setTextureSize(64, 32);
		Block9.mirror = true;
		Block10 = new ModelRenderer(this, 0, 0);
		Block10.addBox(-8F, -8F, -8F, 16, 16, 16);
		Block10.setRotationPoint(0F, 8F, -16F);
		Block10.setTextureSize(64, 32);
		Block10.mirror = true;
		Block11 = new ModelRenderer(this, 0, 0);
		Block11.addBox(-8F, -8F, -8F, 16, 16, 16);
		Block11.setRotationPoint(0F, -8F, 0F);
		Block11.setTextureSize(64, 32);
		Block11.mirror = true;
		Block12 = new ModelRenderer(this, 0, 0);
		Block12.addBox(-8F, -8F, -8F, 16, 16, 16);
		Block12.setRotationPoint(0F, 24F, 0F);
		Block12.setTextureSize(64, 32);
		Block12.mirror = true;
		Block13 = new ModelRenderer(this, 0, 0);
		Block13.addBox(-8F, -8F, -8F, 16, 16, 16);
		Block13.setRotationPoint(16F, 8F, 0F);
		Block13.setTextureSize(64, 32);
		Block13.mirror = true;
		Block14 = new ModelRenderer(this, 0, 0);
		Block14.addBox(-8F, -8F, -8F, 16, 16, 16);
		Block14.setRotationPoint(-16F, 8F, 0F);
		Block14.setTextureSize(64, 32);
		Block14.mirror = true;
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		Block1.render(f5);
		Block2.render(f5);
		Block3.render(f5);
		Block4.render(f5);
		Block5.render(f5);
		Block6.render(f5);
		Block7.render(f5);
		Block8.render(f5);
		Block9.render(f5);
		Block10.render(f5);
		Block11.render(f5);
		Block12.render(f5);
		Block13.render(f5);
		Block14.render(f5);
	}

	public void renderWithSize(EntityMeteor entityMeteor, float f5) {
		int mSize = entityMeteor.size;
		if (mSize == 1) {
			SmallMet.render(f5);
			return;
		}
		if (mSize >= 2) {
			Block1.render(f5);
			Block2.render(f5);
			Block3.render(f5);
			Block4.render(f5);
			Block5.render(f5);
			Block6.render(f5);
			Block7.render(f5);
			Block8.render(f5);
		}
		if (mSize == 3) {
			Block9.render(f5);
			Block10.render(f5);
			Block11.render(f5);
			Block12.render(f5);
			Block13.render(f5);
			Block14.render(f5);
		}
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
	{

	}

}