package net.meteor.client.gui;

import org.lwjgl.opengl.GL11;

import net.meteor.common.MeteorsMod;
import net.meteor.common.block.container.ContainerMeteorShield;
import net.meteor.common.tileentity.TileEntityMeteorShield;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiMeteorShield extends GuiContainer {
	
	private static final ResourceLocation shieldGuiTexture = new ResourceLocation(MeteorsMod.MOD_ID + ":textures/gui/container/meteor_shield.png");

	public GuiMeteorShield(InventoryPlayer inventoryPlayer, TileEntityMeteorShield mShield) {
		super(new ContainerMeteorShield(inventoryPlayer, mShield));
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(shieldGuiTexture);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
		
	}

}
