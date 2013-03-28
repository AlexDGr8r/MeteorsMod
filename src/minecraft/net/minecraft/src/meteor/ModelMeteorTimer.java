package net.minecraft.src.meteor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

@SideOnly(Side.CLIENT)
public class ModelMeteorTimer extends ModelBase {
	
	public ModelRenderer base;
	public ModelRenderer element_meteorite;
	public ModelRenderer element_frezarite;
	public ModelRenderer element_kreknorite;
	
	public ModelMeteorTimer() {
		this.textureWidth = 64;
		this.textureHeight = 32;
		
		this.setTextureOffset("base.bottom", 0, 0);
		this.setTextureOffset("base.top", 4, 2);
		this.base = new ModelRenderer(this, "base");
		this.base.addBox("bottom", 0.0F, 0.0F, 0.0F, 16, 4, 16);
		this.base.addBox("top", 0.0F, 0.0F, 0.0F, 14, 1, 14);
		
		this.element_meteorite = new ModelRenderer(this, 0, 7);
		this.element_meteorite.addBox(-2F, 0F, -1F, 1, 3, 2);
		this.element_meteorite.setRotationPoint(0F, 15F, 0F);
		
		this.element_frezarite = new ModelRenderer(this, 9, 0);
		this.element_frezarite.addBox(-4F, 0F, -1F, 1, 3, 2);
		this.element_frezarite.setRotationPoint(0F, 15F, 0F);
		
		this.element_kreknorite = new ModelRenderer(this, 0, 0);
		this.element_kreknorite.addBox(6F, 0F, -1F, 1, 3, 2);
		this.element_kreknorite.setRotationPoint(0F, 15F, 0F);
	}
	
	public void renderAll() {
		float f = 0.0625F;
		this.base.render(f);
		this.element_meteorite.render(f);
		this.element_frezarite.render(f);
		this.element_kreknorite.render(f);
	}

}
