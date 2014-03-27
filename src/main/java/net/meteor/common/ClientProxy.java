package net.meteor.common;

import net.meteor.client.effect.EntityFrezaDustFX;
import net.meteor.client.effect.EntityMeteorShieldParticleFX;
import net.meteor.client.effect.EntityMeteordustFX;
import net.meteor.client.model.ModelCometKitty;
import net.meteor.client.render.RenderAlienCreeper;
import net.meteor.client.render.RenderCometKitty;
import net.meteor.client.render.RenderMeteor;
import net.meteor.client.render.RenderSummoner;
import net.meteor.client.tileentity.TileEntityMeteorShieldRenderer;
import net.meteor.client.tileentity.TileEntityMeteorTimerRenderer;
import net.meteor.common.entity.EntityAlienCreeper;
import net.meteor.common.entity.EntityCometKitty;
import net.meteor.common.entity.EntityMeteor;
import net.meteor.common.entity.EntitySummoner;
import net.meteor.common.tileentity.TileEntityMeteorShield;
import net.meteor.common.tileentity.TileEntityMeteorTimer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.world.World;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
	
	@Override
	public void registerTileEntities()
	{
		TileEntityMeteorShieldRenderer tileRend = new TileEntityMeteorShieldRenderer();
		ClientRegistry.registerTileEntity(TileEntityMeteorShield.class, "TileEntityMeteorShield", tileRend);
		TileEntityMeteorTimerRenderer timerRend = new TileEntityMeteorTimerRenderer();
		ClientRegistry.registerTileEntity(TileEntityMeteorTimer.class, "TileEntityMeteorTimer", timerRend);
	}

	@Override
	public void loadStuff()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityMeteor.class, new RenderMeteor());
		RenderingRegistry.registerEntityRenderingHandler(EntityAlienCreeper.class, new RenderAlienCreeper());
		RenderingRegistry.registerEntityRenderingHandler(EntityCometKitty.class, new RenderCometKitty(new ModelCometKitty(), 0.4F));
		RenderingRegistry.registerEntityRenderingHandler(EntitySummoner.class, new RenderSummoner());
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