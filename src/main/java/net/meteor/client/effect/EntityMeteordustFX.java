package net.meteor.client.effect;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EntityMeteordustFX extends EntityFX
{
	float reddustParticleScale;

	public EntityMeteordustFX(World world, double d, double d1, double d2, float f, float f1, float f2)
	{
		this(world, d, d1, d2, 1.0F, f, f1, f2);
	}

	public EntityMeteordustFX(World world, double d, double d1, double d2, float f, float f1, float f2, float f3)
	{
		super(world, d, d1, d2, 0.0D, 0.0D, 0.0D);
		motionX *= 0.10000000149011612D;
		motionY *= 0.10000000149011612D;
		motionZ *= 0.10000000149011612D;
		if(f1 == 0.0F)
		{
			f1 = 1.0F;
		}
		float f4 = (float)Math.random() * 0.4F + 0.6F;
		particleRed = 0.09F;
		particleGreen = 0.09F;
		particleBlue = 0.09F;
		particleScale *= 0.75F;
		particleScale *= f;
		reddustParticleScale = particleScale;
		particleMaxAge = (int)(8D / (Math.random() * 0.80000000000000004D + 0.20000000000000001D));
		particleMaxAge *= f;
		noClip = false;
	}

	@SideOnly(Side.CLIENT)
	public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5)
	{
		float f6 = (((float)particleAge + f) / (float)particleMaxAge) * 32F;
		if(f6 < 0.0F)
		{
			f6 = 0.0F;
		}
		if(f6 > 1.0F)
		{
			f6 = 1.0F;
		}
		particleScale = reddustParticleScale * f6;
		super.renderParticle(tessellator, f, f1, f2, f3, f4, f5);
	}

	public void onUpdate()
	{
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		if(particleAge++ >= particleMaxAge)
		{
			setDead();
		}
		setParticleTextureIndex(7 - (particleAge * 8) / particleMaxAge);
		moveEntity(motionX, motionY, motionZ);
		if(posY == prevPosY)
		{
			motionX *= 1.1000000000000001D;
			motionZ *= 1.1000000000000001D;
		}
		motionX *= 0.95999997854232788D;
		motionY *= 0.95999997854232788D;
		motionZ *= 0.95999997854232788D;
		if(onGround)
		{
			motionX *= 0.69999998807907104D;
			motionZ *= 0.69999998807907104D;
		}
	}
}