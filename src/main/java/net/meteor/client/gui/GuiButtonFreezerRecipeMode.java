package net.meteor.client.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.meteor.common.MeteorItems;
import net.meteor.common.tileentity.TileEntityFreezingMachine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class GuiButtonFreezerRecipeMode extends GuiButton {

	private TileEntityFreezingMachine freezer;
	private GuiFreezingMachine gui;

	public GuiButtonFreezerRecipeMode(int id, int xPos, int yPos, int width, int height, TileEntityFreezingMachine freezer, GuiFreezingMachine gui) {
		super(id, xPos, yPos, width, height, "");
		this.freezer = freezer;
		this.gui = gui;
	}

	@Override
	public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_)
	{
		if (this.visible)
		{
			p_146112_1_.getTextureManager().bindTexture(GuiFreezingMachine.freezingMachineTextures);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.field_146123_n = p_146112_2_ >= this.xPosition && p_146112_3_ >= this.yPosition && p_146112_2_ < this.xPosition + this.width && p_146112_3_ < this.yPosition + this.height;
			int k = this.getHoverState(this.field_146123_n) - 1;
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			this.drawTexturedModalRect(this.xPosition, this.yPosition, k * 20, 166, this.width, this.height);
			this.mouseDragged(p_146112_1_, p_146112_2_, p_146112_3_);

			RenderItem renderitem = new RenderItem();
			ArrayList<String> info = new ArrayList<String>();
			p_146112_1_.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
			int state = freezer.getRecipeMode().getID();

			if (state == 0) {	// Item Only - Frozen Iron
				
				renderitem.renderItemAndEffectIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().getTextureManager(), new ItemStack(MeteorItems.FrozenIron), xPosition + 2, yPosition + 2);
			
			} else if (state == 1) { // Fluid only - Water
				
				Fluid water = FluidRegistry.WATER;
				this.drawTexturedModelRectFromIcon(xPosition + 2, yPosition + 2, water.getIcon(), 16, 16);
				
			} else if (state == 2) { // Both - water and stone (slippery stone)
				
				Fluid water = FluidRegistry.WATER;
				this.drawTexturedModelRectFromIcon(xPosition + 2, yPosition + 2, water.getIcon(), 16, 16);
				renderitem.renderItemAndEffectIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().getTextureManager(), new ItemStack(Blocks.stone), xPosition + 2, yPosition + 2);
				
			} else if (state == 3) { // Either - water and frozen iron
				
				Fluid water = FluidRegistry.WATER;
				this.drawTexturedModelRectFromIcon(xPosition + 2, yPosition + 2, water.getIcon(), 16, 16);
				p_146112_1_.getTextureManager().bindTexture(TextureMap.locationItemsTexture);
				this.drawTexturedModelRectFromIcon(xPosition + 3, yPosition + 3, MeteorItems.FrozenIron.getIconFromDamage(0), 14, 14);
				
			}

		}
	}

}
