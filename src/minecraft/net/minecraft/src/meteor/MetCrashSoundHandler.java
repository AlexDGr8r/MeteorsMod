package net.minecraft.src.meteor;

import net.minecraft.client.audio.SoundManager;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;

public class MetCrashSoundHandler
{
	@ForgeSubscribe
	public void onLoadSoundSettings(SoundLoadEvent event)
	{
		try {
			SoundManager soundManager = event.manager;
			soundManager.soundPoolSounds.addSound("meteors:meteor/crash.wav");
			soundManager.soundPoolSounds.addSound("meteors:shield/powerup.wav");
			soundManager.soundPoolSounds.addSound("meteors:shield/powerdown.wav");
			soundManager.soundPoolSounds.addSound("meteors:shield/humm.wav");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}