package net.meteor.client.render;

import net.meteor.client.model.ModelAlienCreeper;
import net.meteor.common.entity.EntityAlienCreeper;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderAlienCreeper extends RenderLiving {

	private ModelBase creeperModel;
	
	private static final ResourceLocation skin = new ResourceLocation("meteors", "textures/entities/meteorcreeper.png");
	private static final ResourceLocation electricity = new ResourceLocation("textures/entity/creeper/creeper_armor.png");

	public RenderAlienCreeper()
	{
		super(new ModelAlienCreeper(), 0.5F);
		creeperModel = new ModelAlienCreeper(2.0F);
	}

	protected void updateCreeperScale(EntityAlienCreeper entitycreeper, float f)
	{
		EntityAlienCreeper entitycreeper1 = entitycreeper;
		float f1 = entitycreeper1.getCreeperFlashIntensity(f);
		float f2 = 1.0F + MathHelper.sin(f1 * 100F) * f1 * 0.01F;
		if (f1 < 0.0F)
		{
			f1 = 0.0F;
		}
		if (f1 > 1.0F)
		{
			f1 = 1.0F;
		}
		f1 *= f1;
		f1 *= f1;
		float f3 = (1.0F + f1 * 0.4F) * f2;
		float f4 = (1.0F + f1 * 0.1F) / f2;
		GL11.glScalef(f3, f4, f3);
	}

	protected int updateCreeperColorMultiplier(EntityAlienCreeper entityCreeper, float par2, float par3)
	{
		float var5 = entityCreeper.getCreeperFlashIntensity(par3);

		if ((int)(var5 * 10.0F) % 2 == 0)
		{
			return 0;
		}
		else
		{
			int var6 = (int)(var5 * 0.2F * 255.0F);

			if (var6 < 0)
			{
				var6 = 0;
			}

			if (var6 > 255)
			{
				var6 = 255;
			}

			short var7 = 255;
			short var8 = 255;
			short var9 = 255;
			return var6 << 24 | var7 << 16 | var8 << 8 | var9;
		}
	}

	protected int func_27006_a(EntityAlienCreeper entitycreeper, int par2, float par3)
	{
		if (entitycreeper.getPowered())
		{
			if (entitycreeper.isInvisible())
			{
				GL11.glDepthMask(false);
			}
			else
			{
				GL11.glDepthMask(true);
			}

			if (par2 == 1)
			{
				float var4 = (float)entitycreeper.ticksExisted + par3;
				this.bindTexture(electricity);
				GL11.glMatrixMode(GL11.GL_TEXTURE);
				GL11.glLoadIdentity();
				float var5 = var4 * 0.01F;
				float var6 = var4 * 0.01F;
				GL11.glTranslatef(var5, var6, 0.0F);
				this.setRenderPassModel(this.creeperModel);
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glEnable(GL11.GL_BLEND);
				float var7 = 0.5F;
				GL11.glColor4f(var7, var7, var7, 1.0F);
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
				return 1;
			}

			if (par2 == 2)
			{
				GL11.glMatrixMode(GL11.GL_TEXTURE);
				GL11.glLoadIdentity();
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_BLEND);
			}
		}

		return -1;
	}

	protected int func_27007_b(EntityAlienCreeper entitycreeper, int i, float f)
	{
		return -1;
	}

	@Override
	protected void preRenderCallback(EntityLivingBase entityliving, float f)
	{
		updateCreeperScale((EntityAlienCreeper)entityliving, f);
	}

	@Override
	protected int getColorMultiplier(EntityLivingBase entityliving, float f, float f1)
	{
		return updateCreeperColorMultiplier((EntityAlienCreeper)entityliving, f, f1);
	}

	@Override
	protected int shouldRenderPass(EntityLivingBase entityliving, int i, float f)
	{
		return func_27006_a((EntityAlienCreeper)entityliving, i, f);
	}

	@Override
	protected int inheritRenderPass(EntityLivingBase entityliving, int i, float f)
	{
		return func_27007_b((EntityAlienCreeper)entityliving, i, f);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return skin;
	}

}