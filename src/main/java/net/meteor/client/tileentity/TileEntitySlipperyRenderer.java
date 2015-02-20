package net.meteor.client.tileentity;

import net.meteor.common.tileentity.TileEntitySlippery;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

public class TileEntitySlipperyRenderer extends TileEntitySpecialRenderer {

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTick) {
		doRenderTileEntityAt((TileEntitySlippery)tileEntity, x, y, z, partialTick);
	}

	private void doRenderTileEntityAt(TileEntitySlippery slip, double x, double y, double z, float partialTick) {
		Block facadeBlock = slip.getFacadeBlock();
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		RenderHelper.disableStandardItemLighting();
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_BLEND);
		if (Minecraft.isAmbientOcclusionEnabled()) {
			GL11.glShadeModel(GL11.GL_SMOOTH);
		} else {
			GL11.glShadeModel(GL11.GL_FLAT);
		}
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		Tessellator tess = Tessellator.instance;
		tess.startDrawingQuads();
		RenderBlocks renderBlocks = new RenderBlocks(slip.getWorldObj());
		tess.setTranslation(-slip.xCoord, -slip.yCoord, -slip.zCoord);
		renderBlocks.renderBlockByRenderType(facadeBlock, slip.xCoord, slip.yCoord, slip.zCoord);
		tess.draw();
		tess.setTranslation(0, 0, 0);
		GL11.glPopMatrix();
		RenderHelper.enableStandardItemLighting();
		GL11.glDisable(GL11.GL_BLEND);
		
	}

}
