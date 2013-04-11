package net.minecraft.src.meteor.model;

import net.meteor.common.tileentity.TileEntityMeteorShield;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelMeteorShieldRay extends ModelBase
{
	ModelRenderer Beam;

	public ModelMeteorShieldRay()
	{
		this.textureWidth = 64;
		this.textureHeight = 32;

		setTextureOffset("beam.main1", 0, 6);
		setTextureOffset("beam.main2", 0, 0);
		setTextureOffset("beam.side1", 36, 0);
		setTextureOffset("beam.side2", 36, 0);
		setTextureOffset("beam.side3", 36, 0);
		setTextureOffset("beam.side4", 36, 0);
		this.Beam = new ModelRenderer(this, "beam");
		this.Beam.addBox("main1", -6.0F, -5.0F, -3.0F, 12, 10, 6);
		this.Beam.addBox("main2", -3.0F, -5.0F, -6.0F, 6, 10, 12);
		this.Beam.addBox("side1", 3.0F, -5.0F, 3.0F, 1, 10, 1);
		this.Beam.addBox("side2", -4.0F, -5.0F, 3.0F, 1, 10, 1);
		this.Beam.addBox("side3", -4.0F, -5.0F, -4.0F, 1, 10, 1);
		this.Beam.addBox("side4", 3.0F, -5.0F, -4.0F, 1, 10, 1);
		this.Beam.setRotationPoint(0.0F, 19.0F, 0.0F);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		this.Beam.render(f5);
	}

	public void setRotationAngles(TileEntityMeteorShield shield) {
		if (shield.direction > 0) {
			this.Beam.rotateAngleZ = (0.7853982F * (shield.rayZMod > 0.0F ? 1.0F : shield.rayZMod == 0.0F ? 0.0F : -1.0F));
			this.Beam.rotateAngleX = (0.7853982F * (shield.rayXMod > 0.0F ? -1.0F : shield.rayXMod == 0.0F ? 0.0F : 1.0F));
		}
		else {
			this.Beam.rotateAngleZ = 0.0F;
			this.Beam.rotateAngleX = 0.0F;
		}
	}

	public void renderModel(TileEntityMeteorShield shield, float f1) {
		setRotationAngles(shield);
		this.Beam.render(f1);
	}
}