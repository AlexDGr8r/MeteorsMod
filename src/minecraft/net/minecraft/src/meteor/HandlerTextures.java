package net.minecraft.src.meteor;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.ForgeSubscribe;

public class HandlerTextures {
	
	@ForgeSubscribe
	public void registerTextures(TextureStitchEvent.Pre event) {
		TextureMap map = event.map;
		if (map.textureType == 1) {		// Items
			map.setTextureEntry("MeteorDetectorProximity", new TextureDetector(0));
			map.setTextureEntry("MeteorDetectorTime", new TextureDetector(1));
			map.setTextureEntry("MeteorDetectorCrash", new TextureDetector(2));
		}
	}

}
