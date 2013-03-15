package net.meteor.common;

import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

public class AchPageMeteorsMod extends AchievementPage
{
	public AchPageMeteorsMod(String name, Achievement[] achievements)
	{
		super(name, achievements);
	}

	public String getName()
	{
		return LangLocalization.get(super.getName());
	}
}