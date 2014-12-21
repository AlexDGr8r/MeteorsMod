package net.meteor.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemPickupEvent;

public class HandlerAchievement
{
	public static final Achievement materialGather = new AchievementMeteorsMod("metMaterialGather", "materialGather", 0, 0, MeteorItems.itemMeteorChips, null).registerStat();
	public static final Achievement meteorManipulator = new AchievementMeteorsMod("metMeteorManipulator", "meteorManipulator", 2, 0, MeteorItems.itemRedMeteorGem, null).registerStat();
	public static final Achievement shieldCrafted = new AchievementMeteorsMod("metShieldCrafted", "shieldCrafted", -2, 2, MeteorBlocks.blockMeteorShield, materialGather).registerStat();
	public static final Achievement shieldFullyUpgraded = new AchievementMeteorsMod("metShieldFullyUpgraded", "shieldFullyUpgraded", 4, 1, MeteorItems.itemRedMeteorGem, meteorManipulator).registerStat();
	public static final Achievement meteorBlocked = new AchievementMeteorsMod("metMeteorBlocked", "meteorBlocked", -3, 4, MeteorBlocks.torchMeteorShieldActive, shieldCrafted).registerStat();
	public static final Achievement attractedDrop = new AchievementMeteorsMod("metAttractedDrop", "attractedDrop", -2, 0, MeteorItems.MeteoritePickaxe, materialGather).registerStat();
	public static final Achievement craftedKreknoSword = new AchievementMeteorsMod("metCraftedKreknoSword", "craftedKreknoSword", -4, 0, MeteorItems.KreknoriteSword, attractedDrop).registerStat();
	public static final Achievement craftedDetector = new AchievementMeteorsMod("metCraftedDetector", "craftedDetector", 0, -2, MeteorItems.itemMeteorProximityDetector, materialGather).registerStat();
	public static final Achievement foundMeteor = new AchievementMeteorsMod("metFoundMeteor", "foundMeteor", 2, -2, new ItemStack(MeteorBlocks.blockMeteor, 1, 1), craftedDetector).registerStat();
	public static final Achievement craftedMeteorTimer = new AchievementMeteorsMod("metCraftedMeteorTimer", "craftedMeteorTimer", 2, -4, MeteorBlocks.blockMeteorTimer, craftedDetector).registerStat();
	public static final Achievement summonMeteor = new AchievementMeteorsMod("metSummonMeteor", "summonMeteor", 4, -1, MeteorItems.itemMeteorSummoner, meteorManipulator).registerStat();
	public static final Achievement kittyEvent = new AchievementMeteorsMod("metCometKittyEvent", "cometKittyEvent", 4, -4, new ItemStack(MeteorItems.itemMeteorSummoner, 1, 5), craftedMeteorTimer).registerStat();
	public static final Achievement kittyTame = new AchievementMeteorsMod("metKittyTame", "kittyTame", 6, -4, Items.fish, kittyEvent).registerStat();
	public static final Achievement craftedFreezer = new AchievementMeteorsMod("metFreezerCrafted", "freezerCrafted", 3, 3, MeteorBlocks.blockFreezer, materialGather).registerStat();
	public static final Achievement freezeWater = new AchievementMeteorsMod("metFreezeWater", "freezeWater", 5, 3, Blocks.ice, craftedFreezer).registerStat();
	public static final Achievement freezeBlocks = new AchievementMeteorsMod("metFreezeBlocks", "freezeBlocks", 3, 5, MeteorBlocks.blockSlippery, craftedFreezer).registerStat();
	public static final Achievement freezeIce = new AchievementMeteorsMod("metFreezeIce", "freezeIce", 7, 3, Blocks.packed_ice, freezeWater).registerStat();
	
	public void readyAchievements()
	{
		AchievementPage page = new AchPageMeteorsMod("achievement.pageMeteors", new Achievement[] { 
				materialGather, shieldCrafted, shieldFullyUpgraded, meteorBlocked, attractedDrop, craftedKreknoSword,
				craftedDetector, foundMeteor, craftedMeteorTimer, summonMeteor, kittyEvent, kittyTame, meteorManipulator,
				craftedFreezer, freezeWater, freezeBlocks, freezeIce
		});
		AchievementPage.registerAchievementPage(page);
	}

	@SubscribeEvent
	public void notifyPickup(ItemPickupEvent event) {
		
		EntityPlayer player = event.player;
		if (HandlerPlayerTick.getMagnetizationLevel(player) > 0) {
			player.triggerAchievement(attractedDrop);
		}
		
		ItemStack iStack = event.pickedUp.getEntityItem();
		if (iStack != null) {
			Item item = iStack.getItem();
			if ((item == null) || (player == null)) return;
			if (item == MeteorItems.itemMeteorChips || item == MeteorItems.itemFrezaCrystal || item == MeteorItems.itemKreknoChip) {
				player.triggerAchievement(materialGather);
			} else if (item == MeteorItems.itemRedMeteorGem) {
				player.triggerAchievement(meteorManipulator);
			}
		}
			
	}
}