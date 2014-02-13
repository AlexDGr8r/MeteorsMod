package net.meteor.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelMeteorTimer extends ModelBase {
	
	public ModelRenderer base;
	public ModelRenderer element_meteorite;
	public ModelRenderer element_frezarite;
	public ModelRenderer element_kreknorite;
	
	public ModelMeteorTimer() {
		this.textureWidth = 64;
		this.textureHeight = 32;
		
		this.base = new ModelRenderer(this, 4, 0);
		this.base.addBox(0F, 0F, 0F, 14, 1, 14);
	    this.base.setRotationPoint(1F, 8F, 1F);
		
		this.element_meteorite = new ModelRenderer(this, 0, 7);
		this.element_meteorite.addBox(-2F, 0F, -1F, 1, 3, 2);
		this.element_meteorite.setRotationPoint(8F, 10F, 8F);
		
		this.element_frezarite = new ModelRenderer(this, 9, 0);
		this.element_frezarite.addBox(-4F, 0F, -1F, 1, 3, 2);
		this.element_frezarite.setRotationPoint(8F, 10F, 8F);
		
		this.element_kreknorite = new ModelRenderer(this, 0, 0);
		this.element_kreknorite.addBox(6F, 0F, -1F, 1, 3, 2);
		this.element_kreknorite.setRotationPoint(8F, 10F, 8F);
	}
	
	public void renderAll() {
		float f = 0.0625F;
		this.base.render(f);
		this.element_meteorite.render(f);
		this.element_frezarite.render(f);
		this.element_kreknorite.render(f);
	}

}
