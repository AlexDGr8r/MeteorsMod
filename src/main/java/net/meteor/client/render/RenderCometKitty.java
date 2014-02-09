package net.minecraft.src.meteor.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderOcelot;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderCometKitty extends RenderOcelot {
	
	private static final ResourceLocation skin = new ResourceLocation("meteors", "textures/entities/cometKitty.png");

	public RenderCometKitty(ModelBase par1ModelBase, float par2) {
		super(par1ModelBase, par2);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(Entity par1Entity)
    {
        return skin;
    }

}
