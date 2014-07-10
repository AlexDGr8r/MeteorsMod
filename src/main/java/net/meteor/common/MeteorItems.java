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
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;
import cpw.mods.fml.common.registry.GameRegistry;

public class MeteorItems {
	
	public static final ArmorMaterial MeteoriteArmor = EnumHelper.addArmorMaterial("METEORITE", 36, new int[] { 2, 7, 5, 2 }, 15);
	public static final ArmorMaterial FrezariteArmor = EnumHelper.addArmorMaterial("FREZARITE", 7, new int[] { 2, 5, 3, 1 }, 20);
	public static final ArmorMaterial KreknoriteArmor = EnumHelper.addArmorMaterial("KREKNORITE", 40, new int[] { 3, 8, 6, 3 }, 10);

	public static final ToolMaterial MeteoriteTool = EnumHelper.addToolMaterial("METEORITE", 3, 900, 10.0F, 4, 15);
	public static final ToolMaterial FrezariteTool = EnumHelper.addToolMaterial("FREZARITE", 2, 225, 7.0F, 2, 20);
	
	public static final Item itemMeteorChips 			= new ItemMeteorsMod().setMaxStackSize(64).setUnlocalizedName("MeteorChips").setTextureName("MeteorChips");
	public static final Item itemRedMeteorGem 			= new ItemMeteorsMod().setMaxStackSize(64).setUnlocalizedName("RedMeteorGem").setTextureName("RedMeteorGem");
	public static final Item itemMeteorSummoner 		= new ItemSummoner().setUnlocalizedName("MeteorSummoner").setTextureName("MeteorSummoner");
	public static final Item itemFrezaCrystal 			= new ItemMeteorsMod().setMaxStackSize(64).setUnlocalizedName("FrezariteCrystal").setTextureName("FrezariteCrystal");
	public static final Item itemKreknoChip 			= new ItemMeteorsMod().setMaxStackSize(64).setUnlocalizedName("KreknoriteChip").setTextureName("KreknoriteChip");
	public static final Item itemVanillaIceCream 		= new ItemFoodMeteorsMod(4, false).setMaxStackSize(64).setUnlocalizedName("VanillaIceCream").setTextureName("VanillaIceCream");
	public static final Item itemChocolateIceCream 		= new ItemFoodMeteorsMod(6, false).setMaxStackSize(64).setUnlocalizedName("ChocolateIceCream").setTextureName("ChocolateIceCream");
	public static final Item itemMeteorProximityDetector= new ItemDetector(0).setUnlocalizedName("MeteorDetectorProximity").setTextureName("MeteorDetectorProximity");
	public static final Item itemMeteorTimeDetector 	= new ItemDetector(1).setUnlocalizedName("MeteorDetectorTime").setTextureName("MeteorDetectorTime");
	public static final Item itemMeteorCrashDetector 	= new ItemDetector(2).setUnlocalizedName("MeteorDetectorCrash").setTextureName("MeteorDetectorCrash");
	public static final Item MeteoriteHelmet 			= new ItemEnchArmor(MeteoriteArmor, 3, 0).setEnch(MeteorsMod.Magnetization, 1).setArmorTexture("Meteorite").setUnlocalizedName("MeteoriteHelmet").setTextureName("MeteoriteHelmet");
	public static final Item MeteoriteBody 				= new ItemEnchArmor(MeteoriteArmor, 3, 1).setEnch(MeteorsMod.Magnetization, 1).setArmorTexture("Meteorite").setUnlocalizedName("MeteoriteChest").setTextureName("MeteoriteChest");
	public static final Item MeteoriteLegs 				= new ItemEnchArmor(MeteoriteArmor, 3, 2).setEnch(MeteorsMod.Magnetization, 1).setArmorTexture("Meteorite").setUnlocalizedName("MeteoriteLegs").setTextureName("MeteoriteLegs");
	public static final Item MeteoriteBoots 			= new ItemEnchArmor(MeteoriteArmor, 3, 3).setEnch(MeteorsMod.Magnetization, 1).setArmorTexture("Meteorite").setUnlocalizedName("MeteoriteBoots").setTextureName("MeteoriteBoots");
	public static final Item MeteoriteAxe 				= new ItemEnchAxe(MeteoriteTool).setEnch(MeteorsMod.Magnetization, 1).setUnlocalizedName("MeteoriteAxe").setTextureName("MeteoriteAxe");
	public static final Item MeteoriteSpade 			= new ItemEnchSpade(MeteoriteTool).setEnch(MeteorsMod.Magnetization, 1).setUnlocalizedName("MeteoriteSpade").setTextureName("MeteoriteSpade");
	public static final Item MeteoriteSword 			= new ItemEnchSword(MeteoriteTool).setEnch(MeteorsMod.Magnetization, 1).setUnlocalizedName("MeteoriteSword").setTextureName("MeteoriteSword");
	public static final Item MeteoritePickaxe 			= new ItemEnchPickaxe(MeteoriteTool).setEnch(MeteorsMod.Magnetization, 1).setUnlocalizedName("MeteoritePickaxe").setTextureName("MeteoritePickaxe");
	public static final Item MeteoriteHoe 				= new ItemEnchHoe(MeteoriteTool).setEnch(MeteorsMod.Magnetization, 1).setUnlocalizedName("MeteoriteHoe").setTextureName("MeteoriteHoe");
	public static final Item FrezariteHelmet 			= new ItemEnchArmor(FrezariteArmor, 3, 0).setEnch(Enchantment.respiration, 3).setArmorTexture("Frezarite").setUnlocalizedName("FrezariteHelmet").setTextureName("FrezariteHelmet");
	public static final Item FrezariteBody 				= new ItemEnchArmor(FrezariteArmor, 3, 1).setEnch(Enchantment.aquaAffinity, 1).setArmorTexture("Frezarite").setUnlocalizedName("FrezariteChest").setTextureName("FrezariteChest");
	public static final Item FrezariteLegs 				= new ItemEnchArmor(FrezariteArmor, 3, 2).setEnch(MeteorsMod.ColdTouch, 1).setArmorTexture("Frezarite").setUnlocalizedName("FrezariteLegs").setTextureName("FrezariteLegs");
	public static final Item FrezariteBoots 			= new ItemEnchArmor(FrezariteArmor, 3, 3).setEnch(MeteorsMod.ColdTouch, 1).setArmorTexture("Frezarite").setUnlocalizedName("FrezariteBoots").setTextureName("FrezariteBoots");
	public static final Item FrezaritePickaxe 			= new ItemFrezaritePickaxe(FrezariteTool).setUnlocalizedName("FrezaritePickaxe").setTextureName("FrezaritePickaxe");
	public static final Item FrezariteSpade 			= new ItemFrezariteSpade(FrezariteTool).setUnlocalizedName("FrezariteSpade").setTextureName("FrezariteSpade");
	public static final Item FrezariteSword 			= new ItemFrezariteSword(FrezariteTool).setUnlocalizedName("FrezariteSword").setTextureName("FrezariteSword");
	public static final Item FrezariteAxe 				= new ItemFrezariteAxe(FrezariteTool).setUnlocalizedName("FrezariteAxe").setTextureName("FrezariteAxe");
	public static final Item FrezariteHoe 				= new ItemFrezariteHoe(FrezariteTool).setUnlocalizedName("FrezariteHoe").setTextureName("FrezariteHoe");
	public static final Item KreknoriteHelmet 			= new ItemEnchArmor(KreknoriteArmor, 3, 0).setEnch(Enchantment.fireProtection, 4).setArmorTexture("Kreknorite").setUnlocalizedName("KreknoriteHelmet").setTextureName("KreknoriteHelmet");
	public static final Item KreknoriteBody 			= new ItemEnchArmor(KreknoriteArmor, 3, 1).setEnch(Enchantment.fireProtection, 4).setArmorTexture("Kreknorite").setUnlocalizedName("KreknoriteChest").setTextureName("KreknoriteChest");
	public static final Item KreknoriteLegs 			= new ItemEnchArmor(KreknoriteArmor, 3, 2).setEnch(Enchantment.fireProtection, 4).setArmorTexture("Kreknorite").setUnlocalizedName("KreknoriteLegs").setTextureName("KreknoriteLegs");
	public static final Item KreknoriteBoots 			= new ItemEnchArmor(KreknoriteArmor, 3, 3).setEnch(Enchantment.fireProtection, 4).setArmorTexture("Kreknorite").setUnlocalizedName("KreknoriteBoots").setTextureName("KreknoriteBoots");
	public static final Item KreknoriteSword 			= new ItemKreknoSword(MeteoriteTool).setUnlocalizedName("KreknoriteSword").setTextureName("KreknoriteSword");

	// Begin industrialization! :D
	public static final Item MeteoriteIngot				= new ItemMeteorsMod().setUnlocalizedName("MeteoriteIngot").setTextureName("MeteoriteIngot");
	public static final Item FrozenIron					= new ItemMeteorsMod().setUnlocalizedName("FrozenIron").setTextureName("FrozenIron");
	public static final Item KreknoriteIngot			= new ItemMeteorsMod().setUnlocalizedName("KreknoriteIngot").setTextureName("KreknoriteIngot");
	
	public static void readyItems() {
		MeteoriteAxe.setHarvestLevel("axe", 3);
		MeteoritePickaxe.setHarvestLevel("pickaxe", 3);
		MeteoriteSpade.setHarvestLevel("shovel", 3);
		FrezaritePickaxe.setHarvestLevel("pickaxe", 2);
		FrezariteSpade.setHarvestLevel("shovel", 2);
		FrezariteAxe.setHarvestLevel("axe", 2);
	}
	
	public static void registerItems() {
		GameRegistry.registerItem(itemMeteorChips, "MeteorChips");
		GameRegistry.registerItem(itemRedMeteorGem, "RedMeteorGem");
		GameRegistry.registerItem(itemFrezaCrystal, "FrezaCrystal");
		GameRegistry.registerItem(itemKreknoChip, "KreknoChip");
		GameRegistry.registerItem(itemVanillaIceCream, "VanillaICream");
		GameRegistry.registerItem(itemChocolateIceCream, "ChocolateICream");
		GameRegistry.registerItem(itemMeteorCrashDetector, "MeteorCrashDet");
		GameRegistry.registerItem(itemMeteorProximityDetector, "MeteorProxDet");
		GameRegistry.registerItem(itemMeteorTimeDetector, "MeteorTimeDet");
		GameRegistry.registerItem(itemMeteorSummoner, "MeteorSummoner");
		GameRegistry.registerItem(FrezariteHelmet, "FrezHelm");
		GameRegistry.registerItem(FrezariteBody, "FrezBody");
		GameRegistry.registerItem(FrezariteLegs, "FrezLegs");
		GameRegistry.registerItem(FrezariteBoots, "FrezBoots");
		GameRegistry.registerItem(FrezaritePickaxe, "FrezPick");
		GameRegistry.registerItem(FrezariteSpade, "FrezSpade");
		GameRegistry.registerItem(KreknoriteHelmet, "KrekHelm");
		GameRegistry.registerItem(KreknoriteBody, "KrekBody");
		GameRegistry.registerItem(KreknoriteLegs, "KrekLegs");
		GameRegistry.registerItem(KreknoriteBoots, "KrekBoots");
		GameRegistry.registerItem(KreknoriteSword, "KrekSword");
		GameRegistry.registerItem(MeteoriteAxe, "MetAxe");
		GameRegistry.registerItem(MeteoriteBody, "MetBody");
		GameRegistry.registerItem(MeteoriteBoots, "MetBoots");
		GameRegistry.registerItem(MeteoriteHelmet, "MetHelm");
		GameRegistry.registerItem(MeteoriteHoe, "MetHoe");
		GameRegistry.registerItem(MeteoriteLegs, "MetLegs");
		GameRegistry.registerItem(MeteoritePickaxe, "MetPick");
		GameRegistry.registerItem(MeteoriteSpade, "MetSpade");
		GameRegistry.registerItem(MeteoriteSword, "MetSword");
		GameRegistry.registerItem(FrezariteSword, "FrezSword");
		GameRegistry.registerItem(FrezariteAxe, "FrezAxe");
		GameRegistry.registerItem(FrezariteHoe, "FrezHoe");
		GameRegistry.registerItem(MeteoriteIngot, "MeteoriteIngot");
		GameRegistry.registerItem(FrozenIron, "FrozenIron");
		GameRegistry.registerItem(KreknoriteIngot, "KreknoriteIngot");
	}
	
}
