package net.meteor.client.effect;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityEnchantmentTableParticleFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EntityMeteorShieldParticleFX extends EntityEnchantmentTableParticleFX
{
	public EntityMeteorShieldParticleFX(World par1World, double par2, double par4, double par6, double par8, double par10, double par12)
	{
		super(par1World, par2, par4, par6, par8, par10, par12);
		if (this.rand.nextInt(8) != 0)
			setParticleTextureIndex((int)(Math.random() * 6.0D) + (int)(Math.random() * 5.0D) * 16);
		else
			setParticleTextureIndex((int)(Math.random() * 8.0D + 6.0D));
	}

	public EntityMeteorShieldParticleFX(World par1World, double par2, double par4, double par6, double par8, double par10, double par12, int metID)
	{
		super(par1World, par2, par4, par6, par8, par10, par12);
		if (this.rand.nextInt(8) != 0)
			setParticleTextureIndex((int)(Math.random() * 6.0D) + metID * 16);
		else
			setParticleTextureIndex((int)(Math.random() * 8.0D + 6.0D));
	}

	@Override
	public void renderParticle(Tessellator par1Tessellator, float par2, float par3, float par4, float par5, float par6, float par7)
	{
		Tessellator tessellator1 = new Tessellator();
		tessellator1.startDrawingQuads();
		tessellator1.setBrightness(getBrightnessForRender(par2));
		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("meteors", "textures/particles/particles.png"));
		//GL11.glBindTexture(GL11.GL_TEXTURE_2D, Minecraft.getMinecraft().renderEngine.func_110581_b(new ResourceLocation("meteors", "textures/particles/particles.png")).func_110552_b());
		super.renderParticle(tessellator1, par2, par3, par4, par5, par6, par7);
		tessellator1.draw();
		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("textures/particle/particles.png"));
		//GL11.glBindTexture(GL11.GL_TEXTURE_2D, Minecraft.getMinecraft().renderEngine.func_110581_b(new ResourceLocation("textures/particle/particles.png")).func_110552_b());
	}
}