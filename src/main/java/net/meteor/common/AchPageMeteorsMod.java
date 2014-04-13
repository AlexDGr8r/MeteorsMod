package net.meteor.common;

import net.minecraft.stats.Achievement;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.AchievementPage;

public class AchPageMeteorsMod extends AchievementPage
{
	public AchPageMeteorsMod(String name, Achievement[] achievements)
	{
		super(name, achievements);
	}

	public String getName()
	{
		return StatCollector.translateToLocal(super.getName());
	}

}