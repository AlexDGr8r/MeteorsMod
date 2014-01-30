package net.meteor.common;

import net.meteor.common.item.ItemDetector;
import net.meteor.common.item.ItemEnchArmor;
import net.meteor.common.item.ItemEnchAxe;
import net.meteor.common.item.ItemEnchHoe;
import net.meteor.common.item.ItemEnchPickaxe;
import net.meteor.common.item.ItemEnchSpade;
import net.meteor.common.item.ItemEnchSword;
import net.meteor.common.item.ItemFoodMeteorsMod;
import net.meteor.common.item.ItemFrezariteAxe;
import net.meteor.common.item.ItemFrezariteHoe;
import net.meteor.common.item.ItemFrezaritePickaxe;
import net.meteor.common.item.ItemFrezariteSpade;
import net.meteor.common.item.ItemFrezariteSword;
import net.meteor.common.item.ItemKreknoSword;
import net.meteor.common.item.ItemMeteorsMod;
import net.meteor.common.item.ItemSummoner;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.registry.GameRegistry;

public class MeteorItems {
	
	public static final Item itemMeteorChips 			= new ItemMeteorsMod(ModConfig.instance.getItemID("Meteor Chips ID", 30228)).setMaxStackSize(64).setUnlocalizedName("MeteorChips").setTextureName("MeteorChips");
	public static final Item itemRedMeteorGem 			= new ItemMeteorsMod(ModConfig.instance.getItemID("Red Meteor Gem ID", 30238)).setMaxStackSize(64).setUnlocalizedName("RedMeteorGem").setTextureName("RedMeteorGem");
	public static final Item itemMeteorSummoner 		= new ItemSummoner(ModConfig.instance.getItemID("Meteor Summoner ID", 30239)).setUnlocalizedName("MeteorSummoner").setTextureName("MeteorSummoner");
	public static final Item itemFrezaCrystal 			= new ItemMeteorsMod(ModConfig.instance.getItemID("Frezarite Crystal ID", 30244)).setMaxStackSize(64).setUnlocalizedName("FrezariteCrystal").setTextureName("FrezariteCrystal");
	public static final Item itemKreknoChip 			= new ItemMeteorsMod(ModConfig.instance.getItemID("Kreknorite Chip ID", 30245)).setMaxStackSize(64).setUnlocalizedName("KreknoriteChip").setTextureName("KreknoriteChip");
	public static final Item itemVanillaIceCream 		= new ItemFoodMeteorsMod(ModConfig.instance.getItemID("Vanilla Ice Cream ID", 30251), 2, false).setMaxStackSize(64).setUnlocalizedName("VanillaIceCream").setTextureName("VanillaIceCream");
	public static final Item itemChocolateIceCream 		= new ItemFoodMeteorsMod(ModConfig.instance.getItemID("Chocolate Ice Cream ID", 30252), 3, false).setMaxStackSize(64).setUnlocalizedName("ChocolateIceCream").setTextureName("ChocolateIceCream");
	public static final Item itemMeteorProximityDetector= new ItemDetector(ModConfig.instance.getItemID("Meteor Proximity Detector ID", 30253), 0).setUnlocalizedName("MeteorDetectorProximity").setTextureName("MeteorDetectorProximity");
	public static final Item itemMeteorTimeDetector 	= new ItemDetector(ModConfig.instance.getItemID("Meteor Time Detector ID", 30254), 1).setUnlocalizedName("MeteorDetectorTime").setTextureName("MeteorDetectorTime");
	public static final Item itemMeteorCrashDetector 	= new ItemDetector(ModConfig.instance.getItemID("Meteor Crash Detector ID", 30255), 2).setUnlocalizedName("MeteorDetectorCrash").setTextureName("MeteorDetectorCrash");
	public static final Item MeteoriteHelmet 			= new ItemEnchArmor(ModConfig.instance.getItemID("Meteor Helmet ID", 30229), MeteorsMod.MeteoriteArmor, 3, 0).setEnch(MeteorsMod.Magnetization, 1).setUnlocalizedName("MeteoriteHelmet").setTextureName("MeteoriteHelmet");
	public static final Item MeteoriteBody 				= new ItemEnchArmor(ModConfig.instance.getItemID("Meteor Body ID", 30230), MeteorsMod.MeteoriteArmor, 3, 1).setEnch(MeteorsMod.Magnetization, 1).setUnlocalizedName("MeteoriteChest").setTextureName("MeteoriteChest");
	public static final Item MeteoriteLegs 				= new ItemEnchArmor(ModConfig.instance.getItemID("Meteor Legs ID", 30231), MeteorsMod.MeteoriteArmor, 3, 2).setEnch(MeteorsMod.Magnetization, 1).setUnlocalizedName("MeteoriteLegs").setTextureName("MeteoriteLegs");
	public static final Item MeteoriteBoots 			= new ItemEnchArmor(ModConfig.instance.getItemID("Meteor Boots ID", 30232), MeteorsMod.MeteoriteArmor, 3, 3).setEnch(MeteorsMod.Magnetization, 1).setUnlocalizedName("MeteoriteBoots").setTextureName("MeteoriteBoots");
	public static final Item MeteoriteAxe 				= new ItemEnchAxe(ModConfig.instance.getItemID("Meteor Axe ID", 30233), MeteorsMod.MeteoriteTool).setEnch(MeteorsMod.Magnetization, 1).setUnlocalizedName("MeteoriteAxe").setTextureName("MeteoriteAxe");
	public static final Item MeteoriteSpade 			= new ItemEnchSpade(ModConfig.instance.getItemID("Meteor Spade ID", 30234), MeteorsMod.MeteoriteTool).setEnch(MeteorsMod.Magnetization, 1).setUnlocalizedName("MeteoriteSpade").setTextureName("MeteoriteSpade");
	public static final Item MeteoriteSword 			= new ItemEnchSword(ModConfig.instance.getItemID("Meteor Sword ID", 30235), MeteorsMod.MeteoriteTool).setEnch(MeteorsMod.Magnetization, 1).setUnlocalizedName("MeteoriteSword").setTextureName("MeteoriteSword");
	public static final Item MeteoritePickaxe 			= new ItemEnchPickaxe(ModConfig.instance.getItemID("Meteor Pickaxe ID", 30236), MeteorsMod.MeteoriteTool).setEnch(MeteorsMod.Magnetization, 1).setUnlocalizedName("MeteoritePickaxe").setTextureName("MeteoritePickaxe");
	public static final Item MeteoriteHoe 				= new ItemEnchHoe(ModConfig.instance.getItemID("Meteor Hoe ID", 30237), MeteorsMod.MeteoriteTool).setEnch(MeteorsMod.Magnetization, 1).setUnlocalizedName("MeteoriteHoe").setTextureName("MeteoriteHoe");
	public static final Item FrezariteHelmet 			= new ItemEnchArmor(ModConfig.instance.getItemID("Frezarite Helmet ID", 30240), MeteorsMod.FrezariteArmor, 3, 0).setEnch(Enchantment.respiration, 3).setUnlocalizedName("FrezariteHelmet").setTextureName("FrezariteHelmet");
	public static final Item FrezariteBody 				= new ItemEnchArmor(ModConfig.instance.getItemID("Frezarite Body ID", 30241), MeteorsMod.FrezariteArmor, 3, 1).setEnch(Enchantment.aquaAffinity, 1).setUnlocalizedName("FrezariteChest").setTextureName("FrezariteChest");
	public static final Item FrezariteLegs 				= new ItemEnchArmor(ModConfig.instance.getItemID("Frezarite Legs ID", 30242), MeteorsMod.FrezariteArmor, 3, 2).setEnch(MeteorsMod.ColdTouch, 1).setUnlocalizedName("FrezariteLegs").setTextureName("FrezariteLegs");
	public static final Item FrezariteBoots 			= new ItemEnchArmor(ModConfig.instance.getItemID("Frezarite Boots ID", 30243), MeteorsMod.FrezariteArmor, 3, 3).setEnch(MeteorsMod.ColdTouch, 1).setUnlocalizedName("FrezariteBoots").setTextureName("FrezariteBoots");
	public static final Item FrezaritePickaxe 			= new ItemFrezaritePickaxe(ModConfig.instance.getItemID("Frezarite Pickaxe ID", 30256), MeteorsMod.FrezariteTool).setUnlocalizedName("FrezaritePickaxe").setTextureName("FrezaritePickaxe");
	public static final Item FrezariteSpade 			= new ItemFrezariteSpade(ModConfig.instance.getItemID("Frezarite Spade ID", 30257), MeteorsMod.FrezariteTool).setUnlocalizedName("FrezariteSpade").setTextureName("FrezariteSpade");
	public static final Item FrezariteSword 			= new ItemFrezariteSword(ModConfig.instance.getItemID("Frezarite Sword ID", 30258), MeteorsMod.FrezariteTool).setUnlocalizedName("FrezariteSword").setTextureName("FrezariteSword");
	public static final Item FrezariteAxe 				= new ItemFrezariteAxe(ModConfig.instance.getItemID("Frezarite Axe ID", 30259), MeteorsMod.FrezariteTool).setUnlocalizedName("FrezariteAxe").setTextureName("FrezariteAxe");
	public static final Item FrezariteHoe 				= new ItemFrezariteHoe(ModConfig.instance.getItemID("Frezarite Hoe ID", 30260), MeteorsMod.FrezariteTool).setUnlocalizedName("FrezariteHoe").setTextureName("FrezariteHoe");
	public static final Item KreknoriteHelmet 			= new ItemEnchArmor(ModConfig.instance.getItemID("Kreknorite Helmet ID", 30246), MeteorsMod.KreknoriteArmor, 3, 0).setEnch(Enchantment.fireProtection, 4).setUnlocalizedName("KreknoriteHelmet").setTextureName("KreknoriteHelmet");
	public static final Item KreknoriteBody 			= new ItemEnchArmor(ModConfig.instance.getItemID("Kreknorite Body ID", 30247), MeteorsMod.KreknoriteArmor, 3, 1).setEnch(Enchantment.fireProtection, 4).setUnlocalizedName("KreknoriteChest").setTextureName("KreknoriteChest");
	public static final Item KreknoriteLegs 			= new ItemEnchArmor(ModConfig.instance.getItemID("Kreknorite Legs ID", 30248), MeteorsMod.KreknoriteArmor, 3, 2).setEnch(Enchantment.fireProtection, 4).setUnlocalizedName("KreknoriteLegs").setTextureName("KreknoriteLegs");
	public static final Item KreknoriteBoots 			= new ItemEnchArmor(ModConfig.instance.getItemID("Kreknorite Boots ID", 30249), MeteorsMod.KreknoriteArmor, 3, 3).setEnch(Enchantment.fireProtection, 4).setUnlocalizedName("KreknoriteBoots").setTextureName("KreknoriteBoots");
	public static final Item KreknoriteSword 			= new ItemKreknoSword(ModConfig.instance.getItemID("Kreknorite Sword ID", 30250), MeteorsMod.MeteoriteTool).setUnlocalizedName("KreknoriteSword").setTextureName("KreknoriteSword");

	public static void readyItems() {
		MinecraftForge.setToolClass(MeteoriteAxe, "axe", 3);
		MinecraftForge.setToolClass(MeteoritePickaxe, "pickaxe", 3);
		MinecraftForge.setToolClass(MeteoriteSpade, "shovel", 3);
		MinecraftForge.setToolClass(FrezaritePickaxe, "pickaxe", 2);
		MinecraftForge.setToolClass(FrezariteSpade, "shovel", 2);
		MinecraftForge.setToolClass(FrezariteAxe, "axe", 2);
	}
	
	public static void registerItems() {
		GameRegistry.registerItem(MeteorItems.itemMeteorChips, "MeteorChips");
		GameRegistry.registerItem(MeteorItems.itemRedMeteorGem, "RedMeteorGem");
		GameRegistry.registerItem(MeteorItems.itemChocolateIceCream, "ChocolateICream");
		GameRegistry.registerItem(MeteorItems.itemFrezaCrystal, "FrezaCrystal");
		GameRegistry.registerItem(MeteorItems.itemKreknoChip, "KreknoChip");
		GameRegistry.registerItem(MeteorItems.itemMeteorCrashDetector, "MeteorCrashDet");
		GameRegistry.registerItem(MeteorItems.itemMeteorProximityDetector, "MeteorProxDet");
		GameRegistry.registerItem(MeteorItems.itemMeteorSummoner, "MeteorSummoner");
		GameRegistry.registerItem(MeteorItems.itemMeteorTimeDetector, "MeteorTimeDet");
		GameRegistry.registerItem(MeteorItems.itemVanillaIceCream, "VanillaICream");
		GameRegistry.registerItem(MeteorItems.FrezariteBody, "FrezBody");
		GameRegistry.registerItem(MeteorItems.FrezariteBoots, "FrezBoots");
		GameRegistry.registerItem(MeteorItems.FrezariteHelmet, "FrezHelm");
		GameRegistry.registerItem(MeteorItems.FrezariteLegs, "FrezLegs");
		GameRegistry.registerItem(MeteorItems.FrezaritePickaxe, "FrezPick");
		GameRegistry.registerItem(MeteorItems.FrezariteSpade, "FrezSpade");
		GameRegistry.registerItem(MeteorItems.KreknoriteBody, "KrekBody");
		GameRegistry.registerItem(MeteorItems.KreknoriteBoots, "KrekBoots");
		GameRegistry.registerItem(MeteorItems.KreknoriteHelmet, "KrekHelm");
		GameRegistry.registerItem(MeteorItems.KreknoriteLegs, "KrekLegs");
		GameRegistry.registerItem(MeteorItems.KreknoriteSword, "KrekSword");
		GameRegistry.registerItem(MeteorItems.MeteoriteAxe, "MetAxe");
		GameRegistry.registerItem(MeteorItems.MeteoriteBody, "MetBody");
		GameRegistry.registerItem(MeteorItems.MeteoriteBoots, "MetBoots");
		GameRegistry.registerItem(MeteorItems.MeteoriteHelmet, "MetHelm");
		GameRegistry.registerItem(MeteorItems.MeteoriteHoe, "MetHoe");
		GameRegistry.registerItem(MeteorItems.MeteoriteLegs, "MetLegs");
		GameRegistry.registerItem(MeteorItems.MeteoritePickaxe, "MetPick");
		GameRegistry.registerItem(MeteorItems.MeteoriteSpade, "MetSpade");
		GameRegistry.registerItem(MeteorItems.MeteoriteSword, "MetSword");
		GameRegistry.registerItem(MeteorItems.FrezariteSword, "FrezSword");
		GameRegistry.registerItem(MeteorItems.FrezariteAxe, "FrezAxe");
		GameRegistry.registerItem(MeteorItems.FrezariteHoe, "FrezHoe");
	}
	
}
