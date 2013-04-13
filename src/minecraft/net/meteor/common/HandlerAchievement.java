package net.meteor.common;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import cpw.mods.fml.common.IPickupNotifier;

public class HandlerAchievement implements IPickupNotifier
{
	public static Achievement materialGather = new AchievementMeteorsMod(21841, "materialGather", 0, 0, MeteorsMod.itemMeteorChips, null).registerAchievement();
	public static Achievement shieldCrafted = new AchievementMeteorsMod(21830, "shieldCrafted", 0, 2, MeteorsMod.blockMeteorShield, materialGather).registerAchievement();
	public static Achievement shieldFullyUpgraded = new AchievementMeteorsMod(21832, "shieldFullyUpgraded", 0, 4, MeteorsMod.itemRedMeteorGem, shieldCrafted).registerAchievement();
	public static Achievement meteorBlocked = new AchievementMeteorsMod(21831, "meteorBlocked", -2, 4, MeteorsMod.torchMeteorShieldActive, shieldCrafted).registerAchievement();
	public static Achievement attractedDrop = new AchievementMeteorsMod(21833, "attractedDrop", -2, 0, MeteorsMod.MeteoritePickaxe, materialGather).registerAchievement();
	public static Achievement craftedKreknoSword = new AchievementMeteorsMod(21834, "craftedKreknoSword", -4, 0, MeteorsMod.KreknoriteSword, attractedDrop).registerAchievement();
	public static Achievement craftedDetector = new AchievementMeteorsMod(21835, "craftedDetector", 0, -2, MeteorsMod.itemMeteorProximityDetector, materialGather).registerAchievement();
	public static Achievement foundMeteor = new AchievementMeteorsMod(21836, "foundMeteor", 2, -2, new ItemStack(MeteorsMod.blockMeteor, 1, 1), craftedDetector).registerAchievement();
	public static Achievement craftedMeteorTimer = new AchievementMeteorsMod(21837, "craftedMeteorTimer", 2, -4, MeteorsMod.blockMeteorTimer, craftedDetector).registerAchievement();
	public static Achievement summonMeteor = new AchievementMeteorsMod(21838, "summonMeteor", 2, 0, MeteorsMod.itemMeteorSummoner, materialGather).registerAchievement();
	public static Achievement kittyEvent = new AchievementMeteorsMod(21839, "cometKittyEvent", 4, -4, new ItemStack(MeteorsMod.itemMeteorSummoner, 1, 5), craftedMeteorTimer).registerAchievement();
	public static Achievement kittyTame = new AchievementMeteorsMod(21840, "kittyTame", 6, -4, Item.fishRaw, kittyEvent).registerAchievement();
	
	public void readyAchievements()
	{
		AchievementPage page = new AchPageMeteorsMod("achievement.pageMeteors", new Achievement[] { 
				materialGather, shieldCrafted, shieldFullyUpgraded, meteorBlocked, attractedDrop, craftedKreknoSword,
				craftedDetector, foundMeteor, craftedMeteorTimer, summonMeteor, kittyEvent, kittyTame
		});
		AchievementPage.registerAchievementPage(page);
	}

	@Override
	public void notifyPickup(EntityItem eitem, EntityPlayer player) {
		ItemStack item = eitem.getEntityItem();
		if ((item == null) || (player == null)) return;
		if (item.itemID == MeteorsMod.itemMeteorChips.itemID || item.itemID == MeteorsMod.itemFrezaCrystal.itemID || item.itemID == MeteorsMod.itemKreknoChip.itemID) {
			player.triggerAchievement(materialGather);
		}
		
		ItemStack pItem = player.getCurrentEquippedItem();
		if (pItem == null) return;
		if ((MeteorsMod.isMeteoriteTool(pItem.itemID)) || (ArmorEffectController.isWearingMagneticArmor(player))) {
			player.triggerAchievement(attractedDrop);
		}
	}
}