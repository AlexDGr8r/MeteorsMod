package net.minecraft.src.meteor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.meteor.common.ClientHandler;
import net.meteor.common.CommonProxy;
import net.meteor.common.MeteorsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TextureMeteorDetectorFX extends TextureFX
{
	private Minecraft mc;
	private int detectorType;
	private int[] compassIconImageData = new int[256];
	private int[] dialImageData = new int[256];
	private double field_4229_i;
	private double field_4228_j;
	private double field_76861_j;
	private double field_76862_k;

	public TextureMeteorDetectorFX(Minecraft par1Minecraft, int type)
	{
		super(type == 1 ? MeteorsMod.itemMeteorProximityDetector.getIconFromDamage(0) : type == 0 ? MeteorsMod.itemMeteorTimeDetector.getIconFromDamage(0)
				: MeteorsMod.itemMeteorCrashDetector.getIconFromDamage(0));

		this.detectorType = type;
		this.mc = par1Minecraft;
		this.tileImage = 1;
		try
		{
			BufferedImage var2 = ImageIO.read(Minecraft.class.getResource(MeteorsMod.textureFile));
			int var3 = this.iconIndex % 16 * 16;
			int var4 = this.iconIndex / 16 * 16;
			var2.getRGB(var3, var4, 16, 16, this.compassIconImageData, 0, 16);
		}
		catch (IOException var5)
		{
			var5.printStackTrace();
		}
		setup();
	}

	public void setup() {
		if (this.detectorType != 0) return;

		try
		{
			BufferedImage var2 = ImageIO.read(Minecraft.class.getResourceAsStream(MeteorsMod.textureFile));
			int var3 = this.iconIndex % 16 * 16;
			int var4 = this.iconIndex / 16 * 16;
			var2.getRGB(var3, var4, 16, 16, this.compassIconImageData, 0, 16);
			var2 = ImageIO.read(Minecraft.class.getResourceAsStream("/meteor/textures/timeDial.png"));
			if (var2.getWidth() != 16)
			{
				BufferedImage tmp = new BufferedImage(16, 16, 6);
				Graphics2D gfx = tmp.createGraphics();
				gfx.drawImage(var2, 0, 0, 16, 16, 0, 0, var2.getWidth(), var2.getHeight(), (ImageObserver)null);
				gfx.dispose();
				var2 = tmp;
			}

			var2.getRGB(0, 0, 16, 16, this.dialImageData, 0, 16);
		}
		catch (Exception var5)
		{
			var5.printStackTrace();
		}
	}

	public void onTick()
	{
		if (this.detectorType != 0)
			for (int var1 = 0; var1 < 256; var1++)
			{
				int var2 = this.compassIconImageData[var1] >> 24 & 0xFF;
			int var3 = this.compassIconImageData[var1] >> 16 & 0xFF;
			int var4 = this.compassIconImageData[var1] >> 8 & 0xFF;
			int var5 = this.compassIconImageData[var1] >> 0 & 0xFF;

			this.imageData[(var1 * 4 + 0)] = ((byte)var3);
			this.imageData[(var1 * 4 + 1)] = ((byte)var4);
			this.imageData[(var1 * 4 + 2)] = ((byte)var5);
			this.imageData[(var1 * 4 + 3)] = ((byte)var2);
			}
		else {
			drawClock();
		}

		double var20 = 0.0D;

		if ((this.mc.theWorld != null) && (this.mc.thePlayer != null))
		{
			ChunkCoordinates var21;
			if (this.detectorType == 0) {
				var21 = ClientHandler.nearestTimeLocation;
			} else if (this.detectorType == 1) {
				var21 = ClientHandler.getClosestIncomingMeteor(this.mc.thePlayer);
			} else {
				var21 = ClientHandler.lastCrashLocation;
			}
			if (var21 != null) {
				double var23 = var21.posX - this.mc.thePlayer.posX;
				double var25 = var21.posZ - this.mc.thePlayer.posZ;
				var20 = (this.mc.thePlayer.rotationYaw - 90.0F) * 3.141592653589793D / 180.0D - Math.atan2(var25, var23);
			} else {
				var20 = Math.random() * Math.PI * 2.0D;
			}

			if (!this.mc.theWorld.provider.isSurfaceWorld())
			{
				var20 = Math.random() * Math.PI * 2.0D;
			}

		}

		double var22;
		for (var22 = var20 - this.field_4229_i; var22 < -Math.PI; var22 += (Math.PI * 2D)) {
			;
		}
		while (var22 >= Math.PI)
		{
			var22 -= Math.PI * 2D;
		}

		if (var22 < -1.0D)
		{
			var22 = -1.0D;
		}

		if (var22 > 1.0D)
		{
			var22 = 1.0D;
		}

		this.field_4228_j += var22 * 0.1D;
		this.field_4228_j *= 0.8D;
		this.field_4229_i += this.field_4228_j;
		double var24 = Math.sin(this.field_4229_i);
		double var26 = Math.cos(this.field_4229_i);
		int var9;
		int var10;
		int var11;
		int var12;
		int var13;
		int var14;
		int var15;
		int var17;
		short var16;
		int var19;
		int var18;

		for (var9 = -4; var9 <= 4; var9++)
		{
			var10 = (int)(8.5D + var26 * var9 * 0.3D);
			var11 = (int)(7.5D - var24 * var9 * 0.3D * 0.5D);
			var12 = var11 * 16 + var10;
			var13 = 100;
			var14 = 100;
			var15 = 100;
			var16 = 255;

			this.imageData[(var12 * 4 + 0)] = ((byte)var13);
			this.imageData[(var12 * 4 + 1)] = ((byte)var14);
			this.imageData[(var12 * 4 + 2)] = ((byte)var15);
			this.imageData[(var12 * 4 + 3)] = ((byte)var16);
		}

		for (var9 = -8; var9 <= 16; var9++)
		{
			var10 = (int)(8.5D + var24 * var9 * 0.3D);
			var11 = (int)(7.5D + var26 * var9 * 0.3D * 0.5D);
			var12 = var11 * 16 + var10;
			if (this.detectorType == 0) {
				var13 = var9 >= 0 ? 255 : 100;
				var14 = var9 >= 0 ? 255 : 100;
				var15 = var9 >= 0 ? 255 : 100;
			} else if (this.detectorType == 1) {
				var13 = var9 >= 0 ? 20 : 100;
				var14 = var9 >= 0 ? 255 : 100;
				var15 = var9 >= 0 ? 20 : 100;
			} else {
				var13 = var9 >= 0 ? 255 : 100;
				var14 = var9 >= 0 ? 255 : 100;
				var15 = var9 >= 0 ? 0 : 100;
			}
			var16 = 255;

			this.imageData[(var12 * 4 + 0)] = ((byte)var13);
			this.imageData[(var12 * 4 + 1)] = ((byte)var14);
			this.imageData[(var12 * 4 + 2)] = ((byte)var15);
			this.imageData[(var12 * 4 + 3)] = ((byte)var16);
		}
	}

	private void drawClock()
	{
		double var1 = 0.0D;

		if ((this.mc.theWorld != null) && (this.mc.thePlayer != null))
		{
			if ((!this.mc.theWorld.provider.isSurfaceWorld()) || (ClientHandler.nearestTimeLocation == null))
			{
				var1 = Math.random() * Math.PI * 2.0D;
			}
			else {
				float var3 = calcAngle(1.0F);
				var1 = -var3 * 3.141593F * 2.0F;
			}

		}

		double var22;
		for (var22 = var1 - this.field_76861_j; var22 < -Math.PI; var22 += (Math.PI * 2D));
		while (var22 >= Math.PI)
		{
			var22 -= Math.PI * 2D;
		}

		if (var22 < -1.0D)
		{
			var22 = -1.0D;
		}

		if (var22 > 1.0D)
		{
			var22 = 1.0D;
		}

		this.field_76862_k += var22 * 0.1D;
		this.field_76862_k *= 0.8D;
		this.field_76861_j += this.field_76862_k;
		double var5 = Math.sin(this.field_76861_j);
		double var7 = Math.cos(this.field_76861_j);

		for (int var9 = 0; var9 < 256; var9++) {
			int var10 = this.compassIconImageData[var9] >> 24 & 0xFF;
		int var11 = this.compassIconImageData[var9] >> 16 & 0xFF;
		int var12 = this.compassIconImageData[var9] >> 8 & 0xFF;
		int var13 = this.compassIconImageData[var9] >> 0 & 0xFF;

		if ((var11 == var13) && (var12 == 0) && (var13 > 0))
		{
			double var14 = -(var9 % 16 / 15.0D - 0.5D);
			double var16 = var9 / 16 / 15.0D - 0.5D;
			int var18 = var11;
			int var19 = (int)((var14 * var7 + var16 * var5 + 0.5D) * 16.0D);
			int var20 = (int)((var16 * var7 - var14 * var5 + 0.5D) * 16.0D);
			int var21 = (var19 & 0xF) + (var20 & 0xF) * 16;
			var10 = this.dialImageData[var21] >> 24 & 0xFF;
			var11 = (this.dialImageData[var21] >> 16 & 0xFF) * var11 / 255;
			var12 = (this.dialImageData[var21] >> 8 & 0xFF) * var18 / 255;
			var13 = (this.dialImageData[var21] >> 0 & 0xFF) * var18 / 255;
		}

		this.imageData[(var9 * 4 + 0)] = ((byte)var11);
		this.imageData[(var9 * 4 + 1)] = ((byte)var12);
		this.imageData[(var9 * 4 + 2)] = ((byte)var13);
		this.imageData[(var9 * 4 + 3)] = ((byte)var10);
		}
	}

	public void bindImage(RenderEngine var1)
	{
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, var1.getTexture(MeteorsMod.textureFile));
	}

	private float calcAngle(float par3) {
		int timeLeft = MathHelper.clamp_int(MeteorsMod.proxy.nearestTimeLeft, 0, 23999);
		int var4 = (int)(timeLeft % 24000L);
		float var5 = (var4 + par3) / 24000.0F - 0.25F;

		if (var5 < 0.0F)
		{
			var5 += 1.0F;
		}

		if (var5 > 1.0F)
		{
			var5 -= 1.0F;
		}

		float var6 = var5;
		var5 = 1.0F - (float)((Math.cos(var5 * 3.141592653589793D) + 1.0D) / 2.0D);
		var5 = var6 + (var5 - var6) / 3.0F;
		return var5;
	}
}