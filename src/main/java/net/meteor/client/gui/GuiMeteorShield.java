package net.meteor.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.meteor.common.EnumMeteor;
import net.meteor.common.MeteorItems;
import net.meteor.common.MeteorsMod;
import net.meteor.common.block.container.ContainerMeteorShield;
import net.meteor.common.tileentity.TileEntityMeteorShield;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.common.registry.LanguageRegistry;

public class GuiMeteorShield extends GuiContainer {
	
	private static final ResourceLocation shieldGuiTexture = new ResourceLocation(MeteorsMod.MOD_ID + ":textures/gui/container/meteor_shield.png");
	
	private TileEntityMeteorShield shield;

	public GuiMeteorShield(InventoryPlayer inventoryPlayer, TileEntityMeteorShield mShield) {
		super(new ContainerMeteorShield(inventoryPlayer, mShield));
		this.shield = mShield;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		// id, xpos, ypos, width, height, string
		String status = shield.getPreventComets() ? EnumChatFormatting.GREEN + StatCollector.translateToLocal("options.on") : EnumChatFormatting.RED + StatCollector.translateToLocal("options.off");
		this.buttonList.add(new GuiButton(0, guiLeft + 2, guiTop - 22, xSize - 4, 20, StatCollector.translateToLocalFormatted("gui.toggleCometButton", status)));
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(shieldGuiTexture);
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, this.xSize, this.ySize);
        if (shield.getPowerLevel() > 0) {
        	this.drawTexturedModalRect(guiLeft + 47, guiTop + 60, 0, 166, 16, 16);
        }
        
        RenderItem renderitem = new RenderItem();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.6F);
        renderitem.renderWithColor = false;
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_CULL_FACE);
        
        if (shield.getPowerLevel() == 0) {
        	renderitem.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.getTextureManager(), new ItemStack(MeteorItems.itemMeteorChips), guiLeft + 47, guiTop + 60);
        }
        
        for (int i = 0; i < 4; ++i) {
        	renderitem.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.getTextureManager(), new ItemStack(MeteorItems.itemRedMeteorGem), guiLeft + 67 + i * 29, guiTop + 60);
        }
        
        GL11.glDisable(GL11.GL_LIGHTING);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
		List<String> displayInfo = shield.getDisplayInfo();
		float f = 0.65F;
		GL11.glScalef(f, f, 1.0F);
		for (int i = 0; i < 3; i++) {
			this.fontRendererObj.drawString(displayInfo.get(i), 74, 4 + (i * fontRendererObj.FONT_HEIGHT + 8), -1);
		}
		if (displayInfo.size() > 3) {
			this.fontRendererObj.drawString(displayInfo.get(3), 74, 50, -1);
		}
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		if (displayInfo.size() > 4) {
			
			this.fontRendererObj.drawString(displayInfo.get(4), 80, 67, -1);
			
			// Comet prevention may be enabled so should double-check list size
			if (displayInfo.size() > 5) {
				this.fontRendererObj.drawString(displayInfo.get(5), 150, 67, -1);
		        float f2 = 1F / f;
		        GL11.glScalef(f2, f2, 1.0F);
		        
		        RenderItem renderItem = new RenderItem();
		        renderItem.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.getTextureManager(), shield.getCometType().getRepresentingItem(), 148, 37);
		        
		        if (func_146978_c(148, 37, 16, 16, p_146979_1_, p_146979_2_)) {
		        	int k1 = this.guiLeft;
		            int l1 = this.guiTop;
		            p_146979_1_ -= k1;
		            p_146979_2_ -= l1;
		            EnumMeteor type = shield.getCometType();
		            ArrayList info = new ArrayList();
		            info.add("Comet Type:");
		            String name = type.toString().toLowerCase();
		            name = name.substring(0, 1).toUpperCase() + name.substring(1);
		            info.add(type.getChatColor() + name);
		        	this.func_146283_a(info, p_146979_1_, p_146979_2_);
		        }
			}
	        
		} else {
			float f2 = 1F / f;
	        GL11.glScalef(f2, f2, 1.0F);
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		shield.pressButton(button.id);
		
		if (button.id == 0) {
			String status = !shield.getPreventComets() ? EnumChatFormatting.GREEN + StatCollector.translateToLocal("options.on") : EnumChatFormatting.RED + StatCollector.translateToLocal("options.off");
			button.displayString = StatCollector.translateToLocalFormatted("gui.toggleCometButton", status);
		}
	}

}
