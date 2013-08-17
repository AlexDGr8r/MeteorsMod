package net.minecraft.src.meteor.render;

import java.util.HashMap;

import net.meteor.common.entity.EntityMeteor;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.src.meteor.model.ModelMeteor;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderMeteor extends Render {

	private ModelMeteor modelMeteor;
	private int metID = 0;
	
	private static HashMap<Integer, ResourceLocation> skins = new HashMap<Integer, ResourceLocation>();
	
	static {
		skins.put(0, new ResourceLocation("meteors", "textures/entities/fallingMeteor.png"));
		skins.put(1, new ResourceLocation("meteors", "textures/entities/frezaMeteor.png"));
		skins.put(2, new ResourceLocation("meteors", "textures/entities/kreknoMeteor.png"));
		skins.put(3, new ResourceLocation("meteors", "textures/entities/unknownMeteor.png"));
		skins.put(4, new ResourceLocation("meteors", "textures/entities/kitty.png"));
	}

	public RenderMeteor() {
		modelMeteor = new ModelMeteor();
	}

	@Override
	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
		renderMeteor((EntityMeteor)entity, d, d1, d2, f, f1);
	}

	public void renderMeteor(EntityMeteor entityMeteor, double d, double d1, double d2, float f, float f1) {
		this.metID = entityMeteor.meteorType.getID();
		GL11.glPushMatrix();
		GL11.glTranslatef((float)d, (float)d1, (float)d2);
		func_110776_a(skins.get(metID));
		float f2 = 1.0F * (float)entityMeteor.size;
		GL11.glScalef(f2, f2, f2);
		modelMeteor.renderWithSize(entityMeteor, 0.0625F);
		GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation func_110775_a(Entity entity) {
		return skins.get(metID);
	}

}