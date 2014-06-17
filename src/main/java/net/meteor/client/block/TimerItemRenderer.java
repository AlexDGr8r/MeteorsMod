package net.meteor.client.block;

import net.meteor.client.tileentity.TileEntityMeteorTimerRenderer;
import net.meteor.common.MeteorsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

public class TimerItemRenderer implements IItemRenderer {
	
	private static final ResourceLocation timerTex = new ResourceLocation(MeteorsMod.MOD_ID, "textures/entities/metTimer.png");
	
	TileEntityMeteorTimerRenderer timerRend;
	
	public TimerItemRenderer() {
		timerRend = new TileEntityMeteorTimerRenderer();
	}

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		switch (type) {
		case EQUIPPED:
		case EQUIPPED_FIRST_PERSON:
		case INVENTORY:
		case ENTITY:
			return true;
		default:
			return false;
		}
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		
		Minecraft.getMinecraft().renderEngine.bindTexture(timerTex);
		
		switch (type) {
		case ENTITY:
			timerRend.renderMeteorTimer(null, -0.5F, 0, -0.5F, 0, false);
			break;
		default: 
			timerRend.renderMeteorTimer(null, 0, 0, 0, 0, false);
			break;
		}
		
	}

}
