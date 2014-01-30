package net.meteor.common;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.ICraftingHandler;
import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.registry.GameRegistry;

public class HandlerRecipe implements ICraftingHandler, IFuelHandler {
	
	@Override
	public void onCrafting(EntityPlayer player, ItemStack item, IInventory craftMatrix)
	{
		if (item.itemID == MeteorBlocks.blockMeteorShield.blockID) {
			player.addStat(HandlerAchievement.shieldCrafted, 1);
		} else if (item.itemID == MeteorItems.KreknoriteSword.itemID) {
			player.addStat(HandlerAchievement.craftedKreknoSword, 1);
		} else if (item.itemID == MeteorItems.itemMeteorProximityDetector.itemID ||
				   item.itemID == MeteorItems.itemMeteorTimeDetector.itemID ||
				   item.itemID == MeteorItems.itemMeteorCrashDetector.itemID) {
			player.addStat(HandlerAchievement.craftedDetector, 1);
		} else if (item.itemID == MeteorBlocks.blockMeteorTimer.blockID) {
			player.addStat(HandlerAchievement.craftedMeteorTimer, 1);
		}
			
	}

	@Override
	public void onSmelting(EntityPlayer player, ItemStack item) {}

	@Override
	public int getBurnTime(ItemStack fuel)
	{
		if (fuel.itemID == MeteorItems.itemKreknoChip.itemID) {
			return 3300;
		}
		return 0;
	}

	public void addRecipes() {
		GameRegistry.addRecipe(new ItemStack(MeteorItems.MeteoriteHelmet, 1), new Object[] { 
			"mmm", "m m", Character.valueOf('m'), MeteorItems.itemMeteorChips 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.MeteoriteBody, 1), new Object[] { 
			"m m", "mmm", "mmm", Character.valueOf('m'), MeteorItems.itemMeteorChips 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.MeteoriteLegs, 1), new Object[] { 
			"mmm", "m m", "m m", Character.valueOf('m'), MeteorItems.itemMeteorChips 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.MeteoriteBoots, 1), new Object[] { 
			"m m", "m m", Character.valueOf('m'), MeteorItems.itemMeteorChips 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.MeteoriteAxe, 1), new Object[] { 
			"mm", "ms", " s", Character.valueOf('m'), MeteorItems.itemMeteorChips, Character.valueOf('s'), Item.ingotIron 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.MeteoriteAxe, 1), new Object[] { 
			"mm", "sm", "s ", Character.valueOf('m'), MeteorItems.itemMeteorChips, Character.valueOf('s'), Item.ingotIron 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.MeteoriteSpade, 1), new Object[] { 
			"m", "s", "s", Character.valueOf('m'), MeteorItems.itemMeteorChips, Character.valueOf('s'), Item.ingotIron 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.MeteoriteSword), new Object[] { 
			"m", "m", "s", Character.valueOf('m'), MeteorItems.itemMeteorChips, Character.valueOf('s'), Item.ingotIron 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.MeteoritePickaxe, 1), new Object[] { 
			"mmm", " s ", " s ", Character.valueOf('m'), MeteorItems.itemMeteorChips, Character.valueOf('s'), Item.ingotIron 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.MeteoriteHoe, 1), new Object[] { 
			"mm", " s", " s", Character.valueOf('m'), MeteorItems.itemMeteorChips, Character.valueOf('s'), Item.ingotIron 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.MeteoriteHoe, 1), new Object[] { 
			"mm", "s ", "s ", Character.valueOf('m'), MeteorItems.itemMeteorChips, Character.valueOf('s'), Item.ingotIron
		});

		GameRegistry.addRecipe(new ItemStack(MeteorBlocks.blockMeteorOre, 1), new Object[] { 
			"mm", "mm", Character.valueOf('m'), MeteorItems.itemMeteorChips 
		});

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MeteorBlocks.torchMeteorShieldActive, 4), new Object[] { 
			"m", "s", Character.valueOf('m'), MeteorItems.itemMeteorChips, Character.valueOf('s'), "stickWood" 
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MeteorBlocks.blockMeteorShield, 1), new Object[] { 
			"mmm", "crc", "ccc", Character.valueOf('m'), MeteorItems.itemMeteorChips, Character.valueOf('c'), "cobblestone", Character.valueOf('r'), Item.redstone 
		}));

		GameRegistry.addShapelessRecipe(new ItemStack(Block.ice, 4), new Object[] { 
			Item.bucketWater, MeteorItems.itemFrezaCrystal 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.FrezariteHelmet, 1), new Object[] { 
			"ccc", "c c", Character.valueOf('c'), MeteorItems.itemFrezaCrystal 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.FrezariteBody, 1), new Object[] { 
			"c c", "ccc", "ccc", Character.valueOf('c'), MeteorItems.itemFrezaCrystal 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.FrezariteLegs, 1), new Object[] { 
			"ccc", "c c", "c c", Character.valueOf('c'), MeteorItems.itemFrezaCrystal 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.FrezariteBoots, 1), new Object[] { 
			"c c", "c c", Character.valueOf('c'), MeteorItems.itemFrezaCrystal 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.KreknoriteHelmet, 1), new Object[] { 
			"ccc", "c c", Character.valueOf('c'), MeteorItems.itemKreknoChip 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.KreknoriteBody, 1), new Object[] { 
			"c c", "ccc", "ccc", Character.valueOf('c'), MeteorItems.itemKreknoChip 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.KreknoriteLegs, 1), new Object[] { 
			"ccc", "c c", "c c", Character.valueOf('c'), MeteorItems.itemKreknoChip 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.KreknoriteBoots, 1), new Object[] { 
			"c c", "c c", Character.valueOf('c'), MeteorItems.itemKreknoChip 
		});

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MeteorItems.KreknoriteSword, 1), new Object[] { 
			"c", "c", "s", Character.valueOf('c'), MeteorItems.itemKreknoChip, Character.valueOf('s'), "stickWood"
		}));

		GameRegistry.addShapelessRecipe(new ItemStack(MeteorItems.itemVanillaIceCream, 4), new Object[] { 
			Item.bowlEmpty, Item.sugar, Item.bucketMilk, MeteorItems.itemFrezaCrystal 
		});

		GameRegistry.addShapelessRecipe(new ItemStack(MeteorItems.itemChocolateIceCream, 4), new Object[] { 
			Item.bowlEmpty, Item.sugar, Item.bucketMilk, MeteorItems.itemFrezaCrystal, new ItemStack(Item.dyePowder, 1, 3) 
		});

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MeteorItems.FrezaritePickaxe, 1), new Object[] { 
			"ccc", " s ", " s ", Character.valueOf('c'), MeteorItems.itemFrezaCrystal, Character.valueOf('s'), "stickWood"
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MeteorItems.FrezariteSpade, 1), new Object[] { 
			"c", "s", "s", Character.valueOf('c'), MeteorItems.itemFrezaCrystal, Character.valueOf('s'), "stickWood" 
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MeteorItems.FrezariteAxe, 1), new Object[] { 
			" cc", " sc", " s ", Character.valueOf('c'), MeteorItems.itemFrezaCrystal, Character.valueOf('s'), "stickWood"
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MeteorItems.FrezariteAxe, 1), new Object[] { 
			"cc ", "cs ", " s ", Character.valueOf('c'), MeteorItems.itemFrezaCrystal, Character.valueOf('s'), "stickWood" 
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MeteorItems.FrezariteHoe, 1), new Object[] { 
			"cc ", " s ", " s ", Character.valueOf('c'), MeteorItems.itemFrezaCrystal, Character.valueOf('s'), "stickWood" 
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MeteorItems.FrezariteHoe, 1), new Object[] { 
			" cc", " s ", " s ", Character.valueOf('c'), MeteorItems.itemFrezaCrystal, Character.valueOf('s'), "stickWood"
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MeteorItems.FrezariteSword, 1), new Object[] { 
			"c", "c", "s", Character.valueOf('c'), MeteorItems.itemFrezaCrystal, Character.valueOf('s'), "stickWood"
		}));

		GameRegistry.addRecipe(new ItemStack(MeteorBlocks.blockMeteorTimer, 1), new Object[] {
			"mfk", "brb", Character.valueOf('m'), MeteorItems.itemMeteorChips, Character.valueOf('f'), MeteorItems.itemFrezaCrystal, 
			Character.valueOf('k'), MeteorItems.itemKreknoChip, Character.valueOf('b'), MeteorBlocks.blockMeteorOre, 
			Character.valueOf('r'), Item.redstone
		});

		GameRegistry.addRecipe(new ItemStack(MeteorBlocks.blockMeteorTimer, 1), new Object[] {
			"mfk", "brb", Character.valueOf('m'), MeteorItems.itemMeteorChips, Character.valueOf('f'), MeteorItems.itemFrezaCrystal, 
			Character.valueOf('k'), MeteorItems.itemKreknoChip, Character.valueOf('b'), MeteorBlocks.blockMeteor, 
			Character.valueOf('r'), Item.redstone
		});

		GameRegistry.addRecipe(new ItemStack(MeteorBlocks.blockRedMeteorGem, 1), new Object[] {
			"rrr", "rrr", "rrr", Character.valueOf('r'), MeteorItems.itemRedMeteorGem
		});

		GameRegistry.addShapelessRecipe(new ItemStack(MeteorItems.itemRedMeteorGem, 9), new Object[] {
			MeteorBlocks.blockRedMeteorGem
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.itemMeteorProximityDetector, 1), new Object[] { 
			" m ", "mrm", " m ", Character.valueOf('m'), MeteorItems.itemMeteorChips, Character.valueOf('r'), Item.redstone
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.itemMeteorTimeDetector, 1), new Object[] { 
			" f ", "frf", " f ", Character.valueOf('f'), MeteorItems.itemFrezaCrystal, Character.valueOf('r'), MeteorItems.itemRedMeteorGem 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.itemMeteorCrashDetector, 1), new Object[] { 
			" k ", "krk", " k ", Character.valueOf('k'), MeteorItems.itemKreknoChip, Character.valueOf('r'), MeteorItems.itemRedMeteorGem 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.itemMeteorSummoner, 1), new Object[] { 
			"rmr", "mrm", "rmr", Character.valueOf('m'), MeteorItems.itemMeteorChips, Character.valueOf('r'), MeteorItems.itemRedMeteorGem 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.itemMeteorSummoner, 1, 1), new Object[] { 
			"mmm", "msm", "mmm", Character.valueOf('m'), MeteorItems.itemMeteorChips, Character.valueOf('s'), MeteorItems.itemMeteorSummoner 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.itemMeteorSummoner, 1, 2), new Object[] { 
			"mmm", "msm", "mmm", Character.valueOf('m'), MeteorItems.itemFrezaCrystal, Character.valueOf('s'), MeteorItems.itemMeteorSummoner 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.itemMeteorSummoner, 1, 3), new Object[] { 
			"mmm", "msm", "mmm", Character.valueOf('m'), MeteorItems.itemKreknoChip, Character.valueOf('s'), MeteorItems.itemMeteorSummoner 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.itemMeteorSummoner, 1, 4), new Object[] { 
			"rmr", "ksk", "rfr", Character.valueOf('m'), MeteorItems.itemMeteorChips, Character.valueOf('s'), MeteorItems.itemMeteorSummoner, 
			Character.valueOf('k'), MeteorItems.itemKreknoChip, Character.valueOf('f'), MeteorItems.itemFrezaCrystal, Character.valueOf('r'),
			MeteorItems.itemRedMeteorGem
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.itemMeteorSummoner, 1, 5), new Object[] { 
			"mfm", "fsf", "mfm", Character.valueOf('m'), MeteorItems.itemMeteorChips, Character.valueOf('s'), MeteorItems.itemMeteorSummoner, 
			Character.valueOf('f'), Item.fishRaw
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.itemMeteorSummoner, 1, 5), new Object[] { 
			"mfm", "fsf", "mfm", Character.valueOf('m'), MeteorItems.itemMeteorChips, Character.valueOf('s'), MeteorItems.itemMeteorSummoner, 
			Character.valueOf('f'), Item.fishCooked 
		});
	}

}
