package net.meteor.client.model;

import net.meteor.common.tileentity.TileEntityMeteorShield;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelMeteorShield extends ModelBase
{
	//fields
	private ModelRenderer BaseShape;
	private ModelRenderer TopLayer;
	private ModelRenderer MiddleLayer;
	private ModelRenderer BottomLayer;
	private ModelRenderer Side1;
	private ModelRenderer Side2;
	private ModelRenderer Side3;
	private ModelRenderer Side4;
	private ModelRenderer Leg1;
	private ModelRenderer Leg2;
	private ModelRenderer Leg3;
	private ModelRenderer Leg4;
	private ModelRenderer Foot1;
	private ModelRenderer Foot2;
	private ModelRenderer Foot3;
	private ModelRenderer Foot4;
	private ModelRenderer InnerSlope1;
	private ModelRenderer InnerSlope2;
	private ModelRenderer InnerSlope3;
	private ModelRenderer InnerSlope4;
	private ModelRenderer SidePanel1;
	private ModelRenderer SidePanel2;
	
	private float topY;
	private float midY;
	private float botY;

	public ModelMeteorShield()
	{
		textureWidth = 128;
		textureHeight = 64;

		BaseShape = new ModelRenderer(this, 0, 0);
		BaseShape.addBox(-6F, 0F, -6F, 12, 0, 12);
		BaseShape.setRotationPoint(0F, 15F, 0F);
		BaseShape.setTextureSize(128, 64);
		BaseShape.mirror = true;
		setRotation(BaseShape, 0F, 0F, 0F);
		TopLayer = new ModelRenderer(this, 0, 14);
		TopLayer.addBox(-5F, -8F, -5F, 10, 1, 10);
		TopLayer.setRotationPoint(0F, 15F, 0F);
		TopLayer.setTextureSize(128, 64);
		TopLayer.mirror = true;
		setRotation(TopLayer, 0F, 0F, 0F);
		topY = TopLayer.rotationPointY;
		MiddleLayer = new ModelRenderer(this, 0, 26);
		MiddleLayer.addBox(-4F, -10F, -4F, 8, 1, 8);
		MiddleLayer.setRotationPoint(0F, 15F, 0F);
		MiddleLayer.setTextureSize(128, 64);
		MiddleLayer.mirror = true;
		setRotation(MiddleLayer, 0F, 0F, 0F);
		midY = MiddleLayer.rotationPointY;
		BottomLayer = new ModelRenderer(this, 0, 35);
		BottomLayer.addBox(-3F, -12F, -3F, 6, 1, 6);
		BottomLayer.setRotationPoint(0F, 15F, 0F);
		BottomLayer.setTextureSize(128, 64);
		BottomLayer.mirror = true;
		setRotation(BottomLayer, 0F, 0F, 0F);
		botY = BottomLayer.rotationPointY;
		Side1 = new ModelRenderer(this, 66, 0);
		Side1.addBox(-7F, -7F, -7F, 14, 10, 1);
		Side1.setRotationPoint(0F, 15F, 0F);
		Side1.setTextureSize(128, 64);
		Side1.mirror = true;
		setRotation(Side1, 0F, 0F, 0F);
		Side2 = new ModelRenderer(this, 66, 0);
		Side2.addBox(-7F, -7F, 6F, 14, 10, 1);
		Side2.setRotationPoint(0F, 15F, 0F);
		Side2.setTextureSize(128, 64);
		Side2.mirror = true;
		setRotation(Side2, 0F, 0F, 0F);
		Side3 = new ModelRenderer(this, 83, 0);
		Side3.addBox(-7F, -7F, -7F, 1, 10, 14);
		Side3.setRotationPoint(0F, 15F, 0F);
		Side3.setTextureSize(128, 64);
		Side3.mirror = true;
		setRotation(Side3, 0F, 0F, 0F);
		Side4 = new ModelRenderer(this, 83, 0);
		Side4.addBox(6F, -7F, -7F, 1, 10, 14);
		Side4.setRotationPoint(0F, 15F, 0F);
		Side4.setTextureSize(128, 64);
		Side4.mirror = true;
		setRotation(Side4, 0F, 0F, 0F);
		Leg1 = new ModelRenderer(this, 48, 0);
		Leg1.addBox(-0.5F, 0F, -0.5F, 1, 6, 1);
		Leg1.setRotationPoint(-6F, 18F, 6F);
		Leg1.setTextureSize(128, 64);
		Leg1.mirror = true;
		setRotation(Leg1, 0.2617994F, -0.7853982F, 0F);
		Leg2 = new ModelRenderer(this, 48, 0);
		Leg2.addBox(-0.5F, 0F, -0.5F, 1, 6, 1);
		Leg2.setRotationPoint(6F, 18F, 6F);
		Leg2.setTextureSize(128, 64);
		Leg2.mirror = true;
		setRotation(Leg2, 0.2617994F, 0.7853982F, 0F);
		Leg3 = new ModelRenderer(this, 48, 0);
		Leg3.addBox(-0.5F, 0F, -0.5F, 1, 6, 1);
		Leg3.setRotationPoint(6F, 18F, -6F);
		Leg3.setTextureSize(128, 64);
		Leg3.mirror = true;
		setRotation(Leg3, -0.2617994F, -0.7853982F, 0F);
		Leg4 = new ModelRenderer(this, 48, 0);
		Leg4.addBox(-0.5F, 0F, -0.5F, 1, 6, 1);
		Leg4.setRotationPoint(-6F, 18F, -6F);
		Leg4.setTextureSize(128, 64);
		Leg4.mirror = true;
		setRotation(Leg4, -0.2617994F, 0.7853982F, 0F);
		Foot1 = new ModelRenderer(this, 52, 0);
		Foot1.addBox(-0.5F, 0F, -0.5F, 1, 1, 2);
		Foot1.setRotationPoint(-7F, 23F, 7F);
		Foot1.setTextureSize(128, 64);
		Foot1.mirror = true;
		setRotation(Foot1, 0F, -0.7853982F, 0F);
		Foot2 = new ModelRenderer(this, 52, 0);
		Foot2.addBox(-0.5F, 0F, -0.5F, 1, 1, 2);
		Foot2.setRotationPoint(7F, 23F, 7F);
		Foot2.setTextureSize(128, 64);
		Foot2.mirror = true;
		setRotation(Foot2, 0F, 0.7853982F, 0F);
		Foot3 = new ModelRenderer(this, 52, 0);
		Foot3.addBox(-0.5F, 0F, -0.5F, 1, 1, 2);
		Foot3.setRotationPoint(-7F, 23F, -7F);
		Foot3.setTextureSize(128, 64);
		Foot3.mirror = true;
		setRotation(Foot3, 0F, -2.356194F, 0F);
		Foot4 = new ModelRenderer(this, 52, 0);
		Foot4.addBox(-0.5F, 0F, -0.5F, 1, 1, 2);
		Foot4.setRotationPoint(7F, 23F, -7F);
		Foot4.setTextureSize(128, 64);
		Foot4.mirror = true;
		setRotation(Foot4, 0F, 2.356194F, 0F);
		InnerSlope1 = new ModelRenderer(this, 32, 26);
		InnerSlope1.addBox(-6F, 1F, 1F, 12, 0, 7);
		InnerSlope1.setRotationPoint(0F, 15F, 0F);
		InnerSlope1.setTextureSize(128, 64);
		InnerSlope1.mirror = true;
		setRotation(InnerSlope1, 0.8203047F, 0F, 0F);
		InnerSlope2 = new ModelRenderer(this, 32, 26);
		InnerSlope2.addBox(-6F, 1F, -8F, 12, 0, 7);
		InnerSlope2.setRotationPoint(0F, 15F, 0F);
		InnerSlope2.setTextureSize(128, 64);
		InnerSlope2.mirror = true;
		setRotation(InnerSlope2, -0.8203047F, 0F, 0F);
		InnerSlope3 = new ModelRenderer(this, 32, 14);
		InnerSlope3.addBox(-8F, 1F, -6F, 7, 0, 12);
		InnerSlope3.setRotationPoint(0F, 15F, 0F);
		InnerSlope3.setTextureSize(128, 64);
		InnerSlope3.mirror = true;
		setRotation(InnerSlope3, 0F, 0F, 0.8203047F);
		InnerSlope4 = new ModelRenderer(this, 32, 14);
		InnerSlope4.addBox(1F, 1F, -6F, 7, 0, 12);
		InnerSlope4.setRotationPoint(0F, 15F, 0F);
		InnerSlope4.setTextureSize(128, 64);
		InnerSlope4.mirror = true;
		setRotation(InnerSlope4, 0F, 0F, -0.8203047F);
		SidePanel1 = new ModelRenderer(this, 32, 34);
		SidePanel1.addBox(6F, 1F, -2F, 1, 4, 4);
		SidePanel1.setRotationPoint(0F, 12F, 0F);
		SidePanel1.setTextureSize(128, 64);
		SidePanel1.mirror = true;
		setRotation(SidePanel1, 0F, 0F, -0.2617994F);
		SidePanel2 = new ModelRenderer(this, 32, 34);
		SidePanel2.addBox(-7F, 1F, -2F, 1, 4, 4);
		SidePanel2.setRotationPoint(0F, 12F, 0F);
		SidePanel2.setTextureSize(128, 64);
		SidePanel2.mirror = true;
		setRotation(SidePanel2, 0F, 0F, 0.2617994F);
	}

	public void render(int pLevel, float partialTick, float f1, float f2, float f3, float f4, float f5, int age)
	{
		setRotationAngles(partialTick, f1, f2, f3, f4, f5, pLevel, age);
		float bobModifier = MathHelper.sin(((float)age + partialTick) / 1600F * 360F) * 0.5F - 0.3F;
		BottomLayer.rotationPointY = botY + bobModifier;
		MiddleLayer.rotationPointY = midY + bobModifier;
		TopLayer.rotationPointY = topY + bobModifier;
		BaseShape.render(f5);
		switch (pLevel) {
			case 5:
			case 4:
				BottomLayer.render(f5);
			case 3:
				MiddleLayer.render(f5);
			case 2:
				TopLayer.render(f5);
		}
		Side1.render(f5);
		Side2.render(f5);
		Side3.render(f5);
		Side4.render(f5);
		Leg1.render(f5);
		Leg2.render(f5);
		Leg3.render(f5);
		Leg4.render(f5);
		Foot1.render(f5);
		Foot2.render(f5);
		Foot3.render(f5);
		Foot4.render(f5);
		InnerSlope1.render(f5);
		InnerSlope2.render(f5);
		InnerSlope3.render(f5);
		InnerSlope4.render(f5);
		SidePanel1.render(f5);
		SidePanel2.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, int pLevel, int age)
	{
		if (pLevel == 0) {
			return;
		}
		TopLayer.rotateAngleY = ((float)age + f) / (140F / pLevel);
		MiddleLayer.rotateAngleY = ((float)age + f) / (70F / pLevel);
		BottomLayer.rotateAngleY = ((float)age + f) / (40F / pLevel);
	}

}
