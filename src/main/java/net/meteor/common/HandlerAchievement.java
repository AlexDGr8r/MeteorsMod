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
	public static final Achievement materialGather = new AchievementMeteorsMod(21841, "materialGather", 0, 0, MeteorItems.itemMeteorChips, null).registerAchievement();
	public static final Achievement meteorManipulator = new AchievementMeteorsMod(21842, "meteorManipulator", 2, 0, MeteorItems.itemRedMeteorGem, null).registerAchievement();
	public static final Achievement shieldCrafted = new AchievementMeteorsMod(21830, "shieldCrafted", 0, 2, MeteorBlocks.blockMeteorShield, materialGather).registerAchievement();
	public static final Achievement shieldFullyUpgraded = new AchievementMeteorsMod(21832, "shieldFullyUpgraded", 4, 1, MeteorItems.itemRedMeteorGem, meteorManipulator).registerAchievement();
	public static final Achievement meteorBlocked = new AchievementMeteorsMod(21831, "meteorBlocked", -2, 4, MeteorBlocks.torchMeteorShieldActive, shieldCrafted).registerAchievement();
	public static final Achievement attractedDrop = new AchievementMeteorsMod(21833, "attractedDrop", -2, 0, MeteorItems.MeteoritePickaxe, materialGather).registerAchievement();
	public static final Achievement craftedKreknoSword = new AchievementMeteorsMod(21834, "craftedKreknoSword", -4, 0, MeteorItems.KreknoriteSword, attractedDrop).registerAchievement();
	public static final Achievement craftedDetector = new AchievementMeteorsMod(21835, "craftedDetector", 0, -2, MeteorItems.itemMeteorProximityDetector, materialGather).registerAchievement();
	public static final Achievement foundMeteor = new AchievementMeteorsMod(21836, "foundMeteor", 2, -2, new ItemStack(MeteorBlocks.blockMeteor, 1, 1), craftedDetector).registerAchievement();
	public static final Achievement craftedMeteorTimer = new AchievementMeteorsMod(21837, "craftedMeteorTimer", 2, -4, MeteorBlocks.blockMeteorTimer, craftedDetector).registerAchievement();
	public static final Achievement summonMeteor = new AchievementMeteorsMod(21838, "summonMeteor", 4, -1, MeteorItems.itemMeteorSummoner, meteorManipulator).registerAchievement();
	public static final Achievement kittyEvent = new AchievementMeteorsMod(21839, "cometKittyEvent", 4, -4, new ItemStack(MeteorItems.itemMeteorSummoner, 1, 5), craftedMeteorTimer).registerAchievement();
	public static final Achievement kittyTame = new AchievementMeteorsMod(21840, "kittyTame", 6, -4, Item.fishRaw, kittyEvent).registerAchievement();
	
	public void readyAchievements()
	{
		AchievementPage page = new AchPageMeteorsMod("achievement.pageMeteors", new Achievement[] { 
				materialGather, shieldCrafted, shieldFullyUpgraded, meteorBlocked, attractedDrop, craftedKreknoSword,
				craftedDetector, foundMeteor, craftedMeteorTimer, summonMeteor, kittyEvent, kittyTame, meteorManipulator
		});
		AchievementPage.registerAchievementPage(page);
	}

	@Override
	public void notifyPickup(EntityItem eitem, EntityPlayer player) {
		
		if (HandlerPlayerTick.isPlayerMagnetized(player)) {
			player.triggerAchievement(attractedDrop);
		}
		
		ItemStack item = eitem.getEntityItem();
		if ((item == null) || (player == null)) return;
		if (item.itemID == MeteorItems.itemMeteorChips.itemID || item.itemID == MeteorItems.itemFrezaCrystal.itemID || item.itemID == MeteorItems.itemKreknoChip.itemID) {
			player.triggerAchievement(materialGather);
		} else if (item.itemID == MeteorItems.itemRedMeteorGem.itemID) {
			player.triggerAchievement(meteorManipulator);
		}
			
	}
}