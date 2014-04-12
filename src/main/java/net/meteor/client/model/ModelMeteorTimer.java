package net.meteor.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelMeteorTimer extends ModelBase {

	public ModelRenderer Shape1;
	public ModelRenderer base;
	public ModelRenderer element_meteorite;
	public ModelRenderer element_frezarite;
	public ModelRenderer element_kreknorite;

	public ModelMeteorTimer() {
		this.textureWidth = 64;
		this.textureHeight = 64;

		this.Shape1 = new ModelRenderer(this, 0, 36);
		this.Shape1.addBox(0F, 0F, 0F, 16, 8, 16);
		this.Shape1.setRotationPoint(0F, 0F, 0F);
		this.Shape1.setTextureSize(64, 64);
		this.Shape1.mirror = false;

		this.base = new ModelRenderer(this, 4, 0);
		this.base.addBox(0F, 0F, 0F, 14, 1, 14);
		this.base.setRotationPoint(1F, 8F, 1F);
		this.base.setTextureSize(64, 64);

		this.element_meteorite = new ModelRenderer(this, 0, 7);
		this.element_meteorite.addBox(-2F, 0F, -1F, 1, 3, 2);
		this.element_meteorite.setRotationPoint(8F, 10F, 8F);
		this.element_meteorite.setTextureSize(64, 64);

		this.element_frezarite = new ModelRenderer(this, 9, 0);
		this.element_frezarite.addBox(-4F, 0F, -1F, 1, 3, 2);
		this.element_frezarite.setRotationPoint(8F, 10F, 8F);
		this.element_frezarite.setTextureSize(64, 64);

		this.element_kreknorite = new ModelRenderer(this, 0, 0);
		this.element_kreknorite.addBox(6F, 0F, -1F, 1, 3, 2);
		this.element_kreknorite.setRotationPoint(8F, 10F, 8F);
		this.element_kreknorite.setTextureSize(64, 64);
	}

	public void renderAll() {
		float f = 0.0625F;
		this.Shape1.render(f);
		this.base.render(f);
		this.element_meteorite.render(f);
		this.element_frezarite.render(f);
		this.element_kreknorite.render(f);
	}

}
