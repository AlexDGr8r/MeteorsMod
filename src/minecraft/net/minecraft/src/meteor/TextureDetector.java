package net.minecraft.src.meteor;

import net.meteor.common.ClientHandler;
import net.meteor.common.MeteorsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.client.renderer.texture.TextureStitched;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class TextureDetector extends TextureStitched {
	
	public double field_94244_i;
    public double field_94242_j;
    public int detectorType;		// 0 = Proximity, 1 = Time, 2 = Crash

	public TextureDetector(int type) {
		super("detector");
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
        double d3 = 0.0D;

        if (par1World != null && !par8)
        {
            ChunkCoordinates chunkcoordinates;
            if (this.detectorType == 0) {
            	chunkcoordinates = ClientHandler.nearestTimeLocation;
			} else if (this.detectorType == 1) {
				chunkcoordinates = ClientHandler.getClosestIncomingMeteor(par2, par4);
			} else {
				chunkcoordinates = ClientHandler.lastCrashLocation;
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
            this.field_94244_i = d3;
        }
        else
        {
            double d6;

            for (d6 = d3 - this.field_94244_i; d6 < -Math.PI; d6 += (Math.PI * 2D))
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

            this.field_94242_j += d6 * 0.1D;
            this.field_94242_j *= 0.8D;
            this.field_94244_i += this.field_94242_j;
        }

        int i;

        for (i = (int)((this.field_94244_i / (Math.PI * 2D) + 1.0D) * (double)this.textureList.size()) % this.textureList.size(); i < 0; i = (i + this.textureList.size()) % this.textureList.size())
        {
            ;
        }

        if (i != this.frameCounter) // field_94222_f current index of icon?
        {
            this.frameCounter = i;
            // field_94226_b list of all textures from png file?
            // function below possibly sets the icon based off the index computed above
            // with modification, could create some nice effects
            this.textureSheet.copyFrom(this.originX, this.originY, (Texture)this.textureList.get(this.frameCounter), this.rotated);
        }
    }

}
