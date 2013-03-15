package net.minecraft.src.meteor;

import net.minecraft.client.audio.SoundManager;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;

public class MetCrashSoundHandler
{
	@ForgeSubscribe
	public void onLoadSoundSettings(SoundLoadEvent event)
	{
		String folder = "/meteor/sound/";
		try {
			SoundManager soundManager = event.manager;
			soundManager.soundPoolSounds.addSound("meteor/crash.wav", getClass().getResource(folder + "meteor/crash.wav"));
			soundManager.soundPoolSounds.addSound("shield/powerup.wav", getClass().getResource(folder + "shield/powerup.wav"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}