package net.meteor.client.gui;

import java.util.List;

import net.meteor.common.MeteorItems;
import net.meteor.common.MeteorsMod;
import net.meteor.common.block.container.ContainerMeteorShield;
import net.meteor.common.tileentity.TileEntityMeteorShield;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiMeteorShield extends GuiContainer {
	
	private static final ResourceLocation shieldGuiTexture = new ResourceLocation(MeteorsMod.MOD_ID + ":textures/gui/container/meteor_shield.png");
	
	private TileEntityMeteorShield shield;

	public GuiMeteorShield(InventoryPlayer inventoryPlayer, TileEntityMeteorShield mShield) {
		super(new ContainerMeteorShield(inventoryPlayer, mShield));
		this.shield = mShield;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(shieldGuiTexture);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        if (shield.getPowerLevel() > 0) {
        	this.drawTexturedModalRect(k + 47, l + 60, 0, 166, 16, 16);
        }
        
        RenderItem renderitem = new RenderItem();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.6F);
        renderitem.renderWithColor = false;
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_CULL_FACE);
        
        if (shield.getPowerLevel() == 0) {
        	renderitem.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.getTextureManager(), new ItemStack(MeteorItems.itemMeteorChips), k + 47, l + 60);
        }
        
        for (int i = 0; i < 4; ++i) {
        	renderitem.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.getTextureManager(), new ItemStack(MeteorItems.itemRedMeteorGem), k + 67 + i * 29, l + 60);
        }
        
        GL11.glDisable(GL11.GL_LIGHTING);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
		List<String> displayInfo = shield.getDisplayInfo();
		for (int i = 0; i < displayInfo.size(); i++) {
			this.fontRendererObj.drawString(displayInfo.get(i), 48, 6 + (i * fontRendererObj.FONT_HEIGHT + 2), -1);
		}
	}

}
