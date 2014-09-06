package net.meteor.common;

import net.meteor.client.block.ShieldItemRenderer;
import net.meteor.client.block.SlipperyItemRenderer;
import net.meteor.client.block.TimerItemRenderer;
import net.meteor.client.effect.EntityFrezaDustFX;
import net.meteor.client.effect.EntityMeteorShieldParticleFX;
import net.meteor.client.effect.EntityMeteordustFX;
import net.meteor.client.model.ModelCometKitty;
import net.meteor.client.render.RenderAlienCreeper;
import net.meteor.client.render.RenderComet;
import net.meteor.client.render.RenderCometKitty;
import net.meteor.client.render.RenderMeteor;
import net.meteor.client.render.RenderSummoner;
import net.meteor.client.tileentity.TileEntityMeteorShieldRenderer;
import net.meteor.client.tileentity.TileEntityMeteorTimerRenderer;
import net.meteor.client.tileentity.TileEntitySlipperyRenderer;
import net.meteor.common.entity.EntityAlienCreeper;
import net.meteor.common.entity.EntityComet;
import net.meteor.common.entity.EntityCometKitty;
import net.meteor.common.entity.EntityMeteor;
import net.meteor.common.entity.EntitySummoner;
import net.meteor.common.tileentity.TileEntityFreezingMachine;
import net.meteor.common.tileentity.TileEntityMeteorShield;
import net.meteor.common.tileentity.TileEntityMeteorTimer;
import net.meteor.common.tileentity.TileEntitySlippery;
import net.meteor.plugin.baubles.Baubles;
import net.meteor.plugin.baubles.MagnetizationOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class ClientProxy extends CommonProxy
{
	
	@Override
	public void registerTileEntities()
	{
		TileEntityMeteorShieldRenderer tileRend = new TileEntityMeteorShieldRenderer();
		ClientRegistry.registerTileEntity(TileEntityMeteorShield.class, "TileEntityMeteorShield", tileRend);
		TileEntityMeteorTimerRenderer timerRend = new TileEntityMeteorTimerRenderer();
		ClientRegistry.registerTileEntity(TileEntityMeteorTimer.class, "TileEntityMeteorTimer", timerRend);
		GameRegistry.registerTileEntity(TileEntityFreezingMachine.class, "TileEntityIceMaker");
		ClientRegistry.registerTileEntity(TileEntitySlippery.class, "TileEntitySlippery", new TileEntitySlipperyRenderer());
	}

	@Override
	public void loadStuff()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityMeteor.class, new RenderMeteor());
		RenderingRegistry.registerEntityRenderingHandler(EntityAlienCreeper.class, new RenderAlienCreeper());
		RenderingRegistry.registerEntityRenderingHandler(EntityCometKitty.class, new RenderCometKitty(new ModelCometKitty(), 0.4F));
		RenderingRegistry.registerEntityRenderingHandler(EntitySummoner.class, new RenderSummoner());
		RenderingRegistry.registerEntityRenderingHandler(EntityComet.class, new RenderComet());
		
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(MeteorBlocks.blockMeteorShield), new ShieldItemRenderer());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(MeteorBlocks.blockMeteorTimer), new TimerItemRenderer());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(MeteorBlocks.blockSlippery), new SlipperyItemRenderer());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(MeteorBlocks.blockSlipperyTwo), new SlipperyItemRenderer());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(MeteorBlocks.blockSlipperyThree), new SlipperyItemRenderer());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(MeteorBlocks.blockSlipperyFour), new SlipperyItemRenderer());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(MeteorBlocks.blockSlipperyStairs), new SlipperyItemRenderer());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(MeteorBlocks.blockSlipperyStairsTwo), new SlipperyItemRenderer());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(MeteorBlocks.blockSlipperyStairsThree), new SlipperyItemRenderer());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(MeteorBlocks.blockSlipperyStairsFour), new SlipperyItemRenderer());
		
		if (Baubles.isBaublesLoaded()) {
			MinecraftForge.EVENT_BUS.register(new MagnetizationOverlay());
		}
	}
	
	@Override
	public void preInit() {
		if (Baubles.isBaublesLoaded()) {
			Baubles.setupBaubleClient();
		}
	}

	public static void spawnParticle(String s, double d, double d1, double d2, double d3, double d4, double d5, World worldObj, int opt)
	{
		Minecraft mc = Minecraft.getMinecraft();
		if (mc == null || mc.renderViewEntity == null || mc.effectRenderer == null) {
			return;
		}
		int i = mc.gameSettings.particleSetting;
		if (i == 1 && worldObj.rand.nextInt(3) == 0) {
			i = 2;
		}
		double d6 = mc.renderViewEntity.posX - d;
		double d7 = mc.renderViewEntity.posY - d1;
		double d8 = mc.renderViewEntity.posZ - d2;
		EntityFX obj = null;
		double d9 = 16D;
		if (d6 * d6 + d7 * d7 + d8 * d8 > d9 * d9) {
			return;
		}
		if (i > 1) {
			return;
		}
		if (s.equals("meteordust"))
			obj = new EntityMeteordustFX(worldObj, d, d1, d2, (float)d3, (float)d4, (float)d5);
		else if (s.equals("frezadust"))
			obj = new EntityFrezaDustFX(worldObj, d, d1, d2, (float)d3, (float)d4, (float)d5);
		else if (s.equals("meteorshield")) {
			if (opt != -1)
				obj = new EntityMeteorShieldParticleFX(worldObj, d, d1, d2, d3, d4, d5, opt);
			else {
				obj = new EntityMeteorShieldParticleFX(worldObj, d, d1, d2, d3, d4, d5);
			}
		}
		if (obj != null) {
			mc.effectRenderer.addEffect((EntityFX)obj);
		}
	}
}