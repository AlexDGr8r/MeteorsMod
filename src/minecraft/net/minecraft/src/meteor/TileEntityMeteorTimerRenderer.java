package net.minecraft.src.meteor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.meteor.common.MeteorsMod;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;

@SideOnly(Side.CLIENT)
public class TileEntityMeteorTimerRenderer extends TileEntitySpecialRenderer {
	
	public ModelMeteorTimer modelMetTimer;
	
	public TileEntityMeteorTimerRenderer() {
		this.modelMetTimer = new ModelMeteorTimer();
	}

	@Override
	// float f appears to be partialTickTime so that's what I should use for cos function for the rotation of the elements
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f) {
		this.bindTextureByName(MeteorsMod.texturesFolder + "metTimer.png");
		modelMetTimer.element_meteorite.rotateAngleY = MathHelper.cos(f * 0.65746F);
		modelMetTimer.element_frezarite.rotateAngleY = MathHelper.cos(f * 0.43245F);
		modelMetTimer.element_kreknorite.rotateAngleY = MathHelper.cos(f);
		
		modelMetTimer.renderAll();
	}

}
