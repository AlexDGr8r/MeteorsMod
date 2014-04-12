package net.meteor.client.block;

import org.lwjgl.opengl.GL11;

import net.meteor.client.model.ModelMeteorShield;
import net.meteor.common.MeteorsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.IItemRenderer;

public class ShieldItemRenderer implements IItemRenderer {
	
	private static final ResourceLocation shieldTexture = new ResourceLocation(MeteorsMod.MOD_ID, "textures/entities/meteorShield.png");
	
	private ModelMeteorShield modelShield;
	
	
	public ShieldItemRenderer() {
		this.modelShield = new ModelMeteorShield();
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
		
		GL11.glPushMatrix();
		
		Minecraft.getMinecraft().renderEngine.bindTexture(shieldTexture);
		int age = 0;
		
		switch (type) {
		case EQUIPPED:
			EntityLivingBase entity = (EntityLivingBase) data[1];
			age = entity.ticksExisted;
			GL11.glRotatef(180F, 1.0F, 0.0F, 0.0F);
			GL11.glTranslatef(0.5F, -1.0F, -0.5F);
			break;
		case EQUIPPED_FIRST_PERSON:
			EntityLivingBase entity2 = (EntityLivingBase) data[1];
			age = entity2.ticksExisted;
			GL11.glRotatef(180F, 1.0F, 0.0F, 0.0F);
			GL11.glTranslatef(0.0F, -1.5F, -0.5F);
			break;
		case INVENTORY:
			age = Minecraft.getMinecraft().thePlayer.ticksExisted;
			GL11.glRotatef(180F, 1.0F, 0.0F, 0.0F);
			GL11.glTranslatef(0.0F, -0.95F, 0.0F);
			GL11.glScalef(0.95F, 0.95F, 0.95F);
			break;
		case ENTITY:
			EntityItem entity3 = (EntityItem)data[1];
			age = entity3.age;
			GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
			GL11.glTranslatef(0.0F, -1.0F, 0.0F);
			break;
		default: break;
		}
		
		this.modelShield.render(5, 0, 0, 0, 0, 0, 0.0625F, age);
		
		GL11.glPopMatrix();
		
	}

}
