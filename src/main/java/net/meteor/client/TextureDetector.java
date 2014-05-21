package net.meteor.client;

import net.meteor.common.ClientHandler;
import net.meteor.common.climate.CrashLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class TextureDetector extends TextureAtlasSprite {

	public double currentAngle;
	public double angleDelta;
	public int detectorType;		// 0 = Proximity, 1 = Time, 2 = Crash

	public TextureDetector(String s, int type) {
		super(s);
		this.detectorType = type;
	}

	@Override
	public void updateAnimation()
	{
		Minecraft minecraft = Minecraft.getMinecraft();

		if (minecraft.theWorld != null && minecraft.thePlayer != null)
		{
			this.updateCompass(minecraft.theWorld, minecraft.thePlayer.posX, minecraft.thePlayer.posZ, (double)minecraft.thePlayer.rotationYaw, false, false);
		}
		else
		{
			this.updateCompass((World)null, 0.0D, 0.0D, 0.0D, true, false);
		}
	}

	public void updateCompass(World par1World, double par2, double par4, double par6, boolean par8, boolean par9)
	{
		if (this.framesTextureData.isEmpty()) {
			return;
		}
		double d3 = 0.0D;

		if (par1World != null && !par8)
		{
			ChunkCoordinates chunkcoordinates;
			if (this.detectorType == 0) {
				chunkcoordinates = ClientHandler.getClosestIncomingMeteor(par2, par4);
			} else if (this.detectorType == 1) {
				chunkcoordinates = ClientHandler.nearestTimeLocation;
			} else {
				CrashLocation cl = ClientHandler.lastCrashLocation;
				if (cl != null) {
					chunkcoordinates = new ChunkCoordinates(cl.x, cl.y, cl.z);
				} else {
					chunkcoordinates = null;
				}
			}
			if (chunkcoordinates != null) {
				double d4 = (double)chunkcoordinates.posX - par2;
				double d5 = (double)chunkcoordinates.posZ - par4;
				par6 %= 360.0D;
				d3 = -((par6 - 90.0D) * Math.PI / 180.0D - Math.atan2(d5, d4));
			} else {
				d3 = Math.random() * Math.PI * 2.0D;
			}

			if (!par1World.provider.isSurfaceWorld())
			{
				d3 = Math.random() * Math.PI * 2.0D;
			}
		}

		if (par9)
		{
			this.currentAngle = d3;
		}
		else
		{
			double d6;

			for (d6 = d3 - this.currentAngle; d6 < -Math.PI; d6 += (Math.PI * 2D))
			{
				;
			}

			while (d6 >= Math.PI)
			{
				d6 -= (Math.PI * 2D);
			}

			if (d6 < -1.0D)
			{
				d6 = -1.0D;
			}

			if (d6 > 1.0D)
			{
				d6 = 1.0D;
			}

			this.angleDelta += d6 * 0.1D;
			this.angleDelta *= 0.8D;
			this.currentAngle += this.angleDelta;
		}

		int i;

		for (i = (int)((this.currentAngle / (Math.PI * 2D) + 1.0D) * (double)this.framesTextureData.size()) % this.framesTextureData.size(); i < 0; i = (i + this.framesTextureData.size()) % this.framesTextureData.size())
		{
			;
		}

		if (i != this.frameCounter)
		{
			this.frameCounter = i;
			//this.textureSheet.copyFrom(this.originX, this.originY, (Texture)this.field_110976_a.get(this.frameCounter), this.rotated);
			TextureUtil.uploadTextureMipmap((int[][])this.framesTextureData.get(this.frameCounter), this.width, this.height, this.originX, this.originY, false, false);
		}
	}

}
