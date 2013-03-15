package net.meteor.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

public class HandlerAchievement
{
	public static Achievement shieldCrafted = new AchievementMeteorsMod(2830, "shieldCrafted", 0, 0, MeteorsMod.blockMeteorShield, null).setIndependent().registerAchievement();
	public static Achievement shieldFullyUpgraded = new AchievementMeteorsMod(2832, "shieldFullyUpgraded", 2, 0, MeteorsMod.itemRedMeteorGem, shieldCrafted).registerAchievement();
	public static Achievement meteorBlocked = new AchievementMeteorsMod(2831, "meteorBlocked", -2, 0, MeteorsMod.torchMeteorShieldActive, shieldCrafted).registerAchievement();
	public static Achievement attractedDrop = new AchievementMeteorsMod(2833, "attractedDrop", 0, 2, MeteorsMod.MeteoritePickaxe, null).setIndependent().registerAchievement();
	public static Achievement craftedKreknoSword = new AchievementMeteorsMod(2834, "craftedKreknoSword", 0, 4, MeteorsMod.KreknoriteSword, attractedDrop).registerAchievement();

	public void readyAchievements()
	{
		AchievementPage page = new AchPageMeteorsMod("achievement.pageMeteors", new Achievement[] { shieldCrafted, shieldFullyUpgraded, meteorBlocked, attractedDrop, craftedKreknoSword });
		AchievementPage.registerAchievementPage(page);
	}

	@ForgeSubscribe
	public void onItemPickup(EntityItemPickupEvent event)
	{
		ItemStack item = event.item.func_92014_d();
		EntityPlayer player = event.entityPlayer;
		if ((item == null) || (player == null)) return;
		ItemStack pItem = player.getCurrentEquippedItem();
		if (pItem == null) return;
		if ((MeteorsMod.isMeteoriteTool(pItem.itemID)) || (ArmorEffectController.isWearingMagneticArmor(player)))
			player.triggerAchievement(attractedDrop);
	}
}