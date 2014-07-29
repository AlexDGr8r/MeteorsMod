package net.meteor.plugin.thaumcraft;

import net.meteor.common.MeteorBlocks;
import net.meteor.common.MeteorItems;
import net.meteor.common.MeteorsMod;
import net.minecraft.item.ItemStack;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public class Thaumcraft {
	
	public static void incorporateThaumcraft() {
		registerEntityScans();
		registerItemScans();
		MeteorsMod.log.info("Thaumcraft mod found. Thaumcraft integration enabled.");
	}
	
	private static void registerItemScans() {
		// Base Materials
		ThaumcraftApi.registerObjectTag(new ItemStack(MeteorItems.itemMeteorChips), new AspectList().add(Aspect.METAL, 1).add(Aspect.ENERGY, 2).add(Aspect.EARTH, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(MeteorItems.itemFrezaCrystal), new AspectList().add(Aspect.COLD, 3).add(Aspect.CRYSTAL, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(MeteorItems.itemKreknoChip), new AspectList().add(Aspect.METAL, 1).add(Aspect.FIRE, 3));
		ThaumcraftApi.registerObjectTag(new ItemStack(MeteorItems.itemRedMeteorGem), new AspectList().add(Aspect.ENERGY, 6).add(Aspect.CRYSTAL, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(MeteorItems.MeteoriteIngot), new AspectList().add(Aspect.METAL, 2).add(Aspect.ENERGY, 2).add(Aspect.EARTH, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(MeteorItems.FrozenIron), new AspectList().add(Aspect.METAL, 2).add(Aspect.COLD, 3));
		ThaumcraftApi.registerObjectTag(new ItemStack(MeteorItems.KreknoriteIngot), new AspectList().add(Aspect.METAL, 2).add(Aspect.FIRE, 3));
		ThaumcraftApi.registerObjectTag(new ItemStack(MeteorBlocks.blockMeteor), new AspectList().add(Aspect.METAL, 1).add(Aspect.ENERGY, 2).add(Aspect.EARTH, 1).add(Aspect.FIRE, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(MeteorBlocks.blockRareMeteor), new AspectList().add(Aspect.METAL, 1).add(Aspect.ENERGY, 2).add(Aspect.EARTH, 1).add(Aspect.FIRE, 1).add(Aspect.CRYSTAL, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(MeteorBlocks.blockMeteorOre), new AspectList().add(Aspect.METAL, 1).add(Aspect.ENERGY, 2).add(Aspect.EARTH, 1));
		ThaumcraftApi.registerObjectTag(new ItemStack(MeteorBlocks.blockFrezarite), new AspectList().add(Aspect.COLD, 3).add(Aspect.CRYSTAL, 1).add(Aspect.WATER, 2));
		ThaumcraftApi.registerObjectTag(new ItemStack(MeteorBlocks.blockKreknorite), new AspectList().add(Aspect.METAL, 1).add(Aspect.EARTH, 1).add(Aspect.FIRE, 4));
		
		// Craftables
		ThaumcraftApi.registerComplexObjectTag(new ItemStack(MeteorItems.MeteoriteAxe), new AspectList());
		ThaumcraftApi.registerComplexObjectTag(new ItemStack(MeteorItems.MeteoriteBody), new AspectList());
		ThaumcraftApi.registerComplexObjectTag(new ItemStack(MeteorItems.MeteoriteBoots), new AspectList());
		ThaumcraftApi.registerComplexObjectTag(new ItemStack(MeteorItems.MeteoriteHelmet), new AspectList());
		ThaumcraftApi.registerComplexObjectTag(new ItemStack(MeteorItems.MeteoriteHoe), new AspectList());
		ThaumcraftApi.registerComplexObjectTag(new ItemStack(MeteorItems.MeteoriteLegs), new AspectList());
		ThaumcraftApi.registerComplexObjectTag(new ItemStack(MeteorItems.MeteoritePickaxe), new AspectList());
		ThaumcraftApi.registerComplexObjectTag(new ItemStack(MeteorItems.MeteoriteSpade), new AspectList());
		ThaumcraftApi.registerComplexObjectTag(new ItemStack(MeteorItems.MeteoriteSword), new AspectList());
		ThaumcraftApi.registerComplexObjectTag(new ItemStack(MeteorItems.FrezariteAxe), new AspectList());
		ThaumcraftApi.registerComplexObjectTag(new ItemStack(MeteorItems.FrezariteBody), new AspectList());
		ThaumcraftApi.registerComplexObjectTag(new ItemStack(MeteorItems.FrezariteBoots), new AspectList());
		ThaumcraftApi.registerComplexObjectTag(new ItemStack(MeteorItems.FrezariteHelmet), new AspectList());
		ThaumcraftApi.registerComplexObjectTag(new ItemStack(MeteorItems.FrezariteHoe), new AspectList());
		ThaumcraftApi.registerComplexObjectTag(new ItemStack(MeteorItems.FrezariteLegs), new AspectList());
		ThaumcraftApi.registerComplexObjectTag(new ItemStack(MeteorItems.FrezaritePickaxe), new AspectList());
		ThaumcraftApi.registerComplexObjectTag(new ItemStack(MeteorItems.FrezariteSpade), new AspectList());
		ThaumcraftApi.registerComplexObjectTag(new ItemStack(MeteorItems.FrezariteSword), new AspectList());
		ThaumcraftApi.registerComplexObjectTag(new ItemStack(MeteorItems.KreknoriteBody), new AspectList());
		ThaumcraftApi.registerComplexObjectTag(new ItemStack(MeteorItems.KreknoriteBoots), new AspectList());
		ThaumcraftApi.registerComplexObjectTag(new ItemStack(MeteorItems.KreknoriteHelmet), new AspectList());
		ThaumcraftApi.registerComplexObjectTag(new ItemStack(MeteorItems.KreknoriteLegs), new AspectList());
		ThaumcraftApi.registerComplexObjectTag(new ItemStack(MeteorItems.KreknoriteSword), new AspectList());
		ThaumcraftApi.registerComplexObjectTag(new ItemStack(MeteorItems.itemMeteorCrashDetector), new AspectList());
		ThaumcraftApi.registerComplexObjectTag(new ItemStack(MeteorItems.itemMeteorProximityDetector), new AspectList());
		ThaumcraftApi.registerComplexObjectTag(new ItemStack(MeteorItems.itemMeteorTimeDetector), new AspectList());
		ThaumcraftApi.registerComplexObjectTag(new ItemStack(MeteorBlocks.blockMeteorShield), new AspectList());
		ThaumcraftApi.registerComplexObjectTag(new ItemStack(MeteorBlocks.torchMeteorShieldActive), new AspectList());
		ThaumcraftApi.registerComplexObjectTag(new ItemStack(MeteorBlocks.blockMeteorTimer), new AspectList());
	}
	
	private static void registerEntityScans() {
		ThaumcraftApi.registerEntityTag("AlienCreeper", new AspectList().add(Aspect.PLANT, 2).add(Aspect.FIRE, 2).add(Aspect.ENERGY, 2));
		ThaumcraftApi.registerEntityTag("CometKitty", new AspectList().add(Aspect.BEAST, 3).add(Aspect.ENTROPY, 3).add(Aspect.ENERGY, 2));
		ThaumcraftApi.registerEntityTag("Meteor", new AspectList().add(Aspect.ENTROPY, 10).add(Aspect.FIRE, 16).add(Aspect.EARTH, 10).add(Aspect.FLIGHT, 8).add(Aspect.AIR, 10).add(Aspect.WEATHER, 4));
	}

}
