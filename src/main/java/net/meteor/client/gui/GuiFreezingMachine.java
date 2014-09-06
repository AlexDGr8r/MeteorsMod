package net.meteor.client.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.meteor.common.FreezerRecipes.RecipeType;
import net.meteor.common.MeteorsMod;
import net.meteor.common.block.container.ContainerFreezingMachine;
import net.meteor.common.tileentity.TileEntityFreezingMachine;
import net.meteor.common.util.Util;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

public class GuiFreezingMachine extends GuiContainer {
	
	public static final ResourceLocation freezingMachineTextures = new ResourceLocation(MeteorsMod.MOD_ID, "textures/gui/container/freezing_machine.png");
	
	private TileEntityFreezingMachine freezer;
	private int fluidAmount = 0;
	private String fluidName;

	public GuiFreezingMachine(InventoryPlayer playerInv, TileEntityFreezingMachine freezer) {
		super(new ContainerFreezingMachine(playerInv, freezer));
		this.freezer = freezer;
	}

	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.add(new GuiButtonFreezerRecipeMode(0, guiLeft + 149, guiTop + 60, 20, 20, freezer, this));
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(freezingMachineTextures);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        
        // Draw Fluids in Tank
        FluidTankInfo tankInfo = freezer.getTankInfo();
        FluidStack fluidStack = tankInfo.fluid;
        
        if (fluidStack != null && fluidStack.amount > 0) {
        	
        	fluidAmount = fluidStack.amount;
        	Fluid fluid = FluidRegistry.getFluid(fluidStack.fluidID);
        	
        	if (fluid != null) {
        		
        		fluidName = fluid.getLocalizedName(fluidStack);
        		IIcon icon = fluid.getIcon();
        		int height = (int) (((double)fluidAmount / (double)tankInfo.capacity) * 69);
        		int drawnHeight = 0;
        		this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        		
        		for (int i = height / 16; i >= 0; i--) {
        			if (i > 0) {
        				this.drawTexturedModelRectFromIcon(k + 13, l + 76 - 16 - drawnHeight, icon, 20, 16);
        				drawnHeight += 16;
        			} else {
        				this.drawTexturedModelRectFromIcon(k + 13, l + 76 - drawnHeight - height, icon, 20, height);
        			}
        			height -= 16;
        		}
        		
        	}
        } else if (fluidAmount > 0) {
        	fluidAmount = 0;
        	fluidName = "";
        }
        
        this.mc.getTextureManager().bindTexture(freezingMachineTextures);
        this.drawTexturedModalRect(k + 13, l + 7, 176, 31, 20, 69);
        
        if (this.freezer.isFreezing()) {
        	int i1 = this.freezer.getBurnTimeRemainingScaled(13);
            this.drawTexturedModalRect(k + 73, l + 36 + 12 - i1, 176, 12 - i1, 14, i1 + 2);
            i1 = this.freezer.getCookProgressScaled(24);
            this.drawTexturedModalRect(k + 96, l + 34, 176, 14, i1 + 1, 16);
        }
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
		if (func_146978_c(13, 7, 20, 69, p_146979_1_, p_146979_2_)) {
			List info = new ArrayList();
			if (fluidName != null && !fluidName.isEmpty()) {
				info.add(fluidName);
			}
			info.add(fluidAmount + " / 10000 mB");
			this.func_146283_a(info, p_146979_1_ - guiLeft, p_146979_2_ - guiTop);
		}
		
		if (func_146978_c(149, 60, 20, 20, p_146979_1_, p_146979_2_)) {
			List info = new ArrayList();
			RecipeType type = freezer.getRecipeMode();
			if (type == RecipeType.item) {
				info.add(EnumChatFormatting.AQUA + StatCollector.translateToLocal("info.freezer.itemMode"));
				info.addAll(Util.getFormattedLines("info.freezer.itemMode.desc", EnumChatFormatting.WHITE));
			} else if (type == RecipeType.fluid) {
				info.add(EnumChatFormatting.AQUA + StatCollector.translateToLocal("info.freezer.fluidMode"));
				info.addAll(Util.getFormattedLines("info.freezer.fluidMode.desc", EnumChatFormatting.WHITE));
			} else if (type == RecipeType.both) {
				info.add(EnumChatFormatting.AQUA + StatCollector.translateToLocal("info.freezer.bothMode"));
				info.addAll(Util.getFormattedLines("info.freezer.bothMode.desc", EnumChatFormatting.WHITE));
			} else if (type == RecipeType.either) {
				info.add(EnumChatFormatting.AQUA + StatCollector.translateToLocal("info.freezer.eitherMode"));
				info.addAll(Util.getFormattedLines("info.freezer.eitherMode.desc", EnumChatFormatting.WHITE));
			}
			this.func_146283_a(info, p_146979_1_ - guiLeft, p_146979_2_ - guiTop);
		}
		
		String s = StatCollector.translateToLocal("tile.freezingMachine.name");
		int i = this.fontRendererObj.getStringWidth(s);
		this.fontRendererObj.drawString(s, 168 - i, 6, 4210752, false);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		freezer.pressButton(button.id);
	}
	
//	// Workaround to not rewrite minecraft code
//	// Used in GuiButtonFreezerRecipeMode
//	public void drawTooltip(List p_146283_1_, int p_146283_2_, int p_146283_3_) {
//		this.func_146283_a(p_146283_1_, p_146283_2_, p_146283_3_);
//	}

}
