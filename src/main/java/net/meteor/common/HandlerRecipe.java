package net.meteor.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class HandlerRecipe implements IFuelHandler {
	
	@SubscribeEvent
	public void onCrafting(ItemCraftedEvent event)
	{
		Item item = event.crafting.getItem();
		EntityPlayer player = event.player;
		if (item == Item.getItemFromBlock(MeteorBlocks.blockMeteorShield)) {
			player.addStat(HandlerAchievement.shieldCrafted, 1);
		} else if (item == MeteorItems.KreknoriteSword) {
			player.addStat(HandlerAchievement.craftedKreknoSword, 1);
		} else if (item == MeteorItems.itemMeteorProximityDetector ||
				   item == MeteorItems.itemMeteorTimeDetector ||
				   item == MeteorItems.itemMeteorCrashDetector) {
			player.addStat(HandlerAchievement.craftedDetector, 1);
		} else if (item == Item.getItemFromBlock(MeteorBlocks.blockMeteorTimer)) {
			player.addStat(HandlerAchievement.craftedMeteorTimer, 1);
		}
			
	}

	@Override
	public int getBurnTime(ItemStack fuel)
	{
		if (fuel.getItem() == MeteorItems.itemKreknoChip) {
			return 3300;
		}
		return 0;
	}

	public void addRecipes() {
		// ============= Crafting Recipes =============
		GameRegistry.addRecipe(new ItemStack(MeteorItems.MeteoriteHelmet, 1), new Object[] { 
			"mmm", "m m", Character.valueOf('m'), MeteorItems.MeteoriteIngot 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.MeteoriteBody, 1), new Object[] { 
			"m m", "mmm", "mmm", Character.valueOf('m'), MeteorItems.MeteoriteIngot 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.MeteoriteLegs, 1), new Object[] { 
			"mmm", "m m", "m m", Character.valueOf('m'), MeteorItems.MeteoriteIngot 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.MeteoriteBoots, 1), new Object[] { 
			"m m", "m m", Character.valueOf('m'), MeteorItems.MeteoriteIngot 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.MeteoriteAxe, 1), new Object[] { 
			"mm", "ms", " s", Character.valueOf('m'), MeteorItems.MeteoriteIngot, Character.valueOf('s'), Items.iron_ingot 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.MeteoriteAxe, 1), new Object[] { 
			"mm", "sm", "s ", Character.valueOf('m'), MeteorItems.MeteoriteIngot, Character.valueOf('s'), Items.iron_ingot 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.MeteoriteSpade, 1), new Object[] { 
			"m", "s", "s", Character.valueOf('m'), MeteorItems.MeteoriteIngot, Character.valueOf('s'), Items.iron_ingot 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.MeteoriteSword), new Object[] { 
			"m", "m", "s", Character.valueOf('m'), MeteorItems.MeteoriteIngot, Character.valueOf('s'), Items.iron_ingot 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.MeteoritePickaxe, 1), new Object[] { 
			"mmm", " s ", " s ", Character.valueOf('m'), MeteorItems.MeteoriteIngot, Character.valueOf('s'), Items.iron_ingot 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.MeteoriteHoe, 1), new Object[] { 
			"mm", " s", " s", Character.valueOf('m'), MeteorItems.MeteoriteIngot, Character.valueOf('s'), Items.iron_ingot 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.MeteoriteHoe, 1), new Object[] { 
			"mm", "s ", "s ", Character.valueOf('m'), MeteorItems.MeteoriteIngot, Character.valueOf('s'), Items.iron_ingot
		});

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MeteorBlocks.torchMeteorShieldActive, 4), new Object[] { 
			"m", "s", Character.valueOf('m'), MeteorItems.itemMeteorChips, Character.valueOf('s'), "stickWood" 
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MeteorBlocks.blockMeteorShield, 1), new Object[] { 
			"mmm", "crc", "ccc", Character.valueOf('m'), MeteorItems.MeteoriteIngot, Character.valueOf('c'), "cobblestone", Character.valueOf('r'), Items.redstone 
		}));

		GameRegistry.addShapelessRecipe(new ItemStack(Blocks.ice, 4), new Object[] { 
			Items.water_bucket, MeteorItems.itemFrezaCrystal 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.FrezariteHelmet, 1), new Object[] { 
			"ccc", "c c", Character.valueOf('c'), MeteorItems.FrozenIron 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.FrezariteBody, 1), new Object[] { 
			"c c", "ccc", "ccc", Character.valueOf('c'), MeteorItems.FrozenIron 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.FrezariteLegs, 1), new Object[] { 
			"ccc", "c c", "c c", Character.valueOf('c'), MeteorItems.FrozenIron 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.FrezariteBoots, 1), new Object[] { 
			"c c", "c c", Character.valueOf('c'), MeteorItems.FrozenIron 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.KreknoriteHelmet, 1), new Object[] { 
			"ccc", "c c", Character.valueOf('c'), MeteorItems.KreknoriteIngot 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.KreknoriteBody, 1), new Object[] { 
			"c c", "ccc", "ccc", Character.valueOf('c'), MeteorItems.KreknoriteIngot 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.KreknoriteLegs, 1), new Object[] { 
			"ccc", "c c", "c c", Character.valueOf('c'), MeteorItems.KreknoriteIngot 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.KreknoriteBoots, 1), new Object[] { 
			"c c", "c c", Character.valueOf('c'), MeteorItems.KreknoriteIngot 
		});

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MeteorItems.KreknoriteSword, 1), new Object[] { 
			"c", "c", "s", Character.valueOf('c'), MeteorItems.KreknoriteIngot, Character.valueOf('s'), "stickWood"
		}));

		GameRegistry.addShapelessRecipe(new ItemStack(MeteorItems.itemVanillaIceCream, 4), new Object[] { 
			Items.bowl, Items.sugar, Items.milk_bucket, MeteorItems.itemFrezaCrystal 
		});

		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(MeteorItems.itemChocolateIceCream, 4), new Object[] { 
			Items.bowl, Items.sugar, Items.milk_bucket, MeteorItems.itemFrezaCrystal, "dyeBrown"
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MeteorItems.FrezaritePickaxe, 1), new Object[] { 
			"ccc", " s ", " s ", Character.valueOf('c'), MeteorItems.FrozenIron, Character.valueOf('s'), "stickWood"
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MeteorItems.FrezariteSpade, 1), new Object[] { 
			"c", "s", "s", Character.valueOf('c'), MeteorItems.FrozenIron, Character.valueOf('s'), "stickWood" 
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MeteorItems.FrezariteAxe, 1), new Object[] { 
			" cc", " sc", " s ", Character.valueOf('c'), MeteorItems.FrozenIron, Character.valueOf('s'), "stickWood"
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MeteorItems.FrezariteAxe, 1), new Object[] { 
			"cc ", "cs ", " s ", Character.valueOf('c'), MeteorItems.FrozenIron, Character.valueOf('s'), "stickWood" 
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MeteorItems.FrezariteHoe, 1), new Object[] { 
			"cc ", " s ", " s ", Character.valueOf('c'), MeteorItems.FrozenIron, Character.valueOf('s'), "stickWood" 
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MeteorItems.FrezariteHoe, 1), new Object[] { 
			" cc", " s ", " s ", Character.valueOf('c'), MeteorItems.FrozenIron, Character.valueOf('s'), "stickWood"
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MeteorItems.FrezariteSword, 1), new Object[] { 
			"c", "c", "s", Character.valueOf('c'), MeteorItems.FrozenIron, Character.valueOf('s'), "stickWood"
		}));

		GameRegistry.addRecipe(new ItemStack(MeteorBlocks.blockMeteorTimer, 1), new Object[] {
			"mfk", "brb", Character.valueOf('m'), MeteorItems.MeteoriteIngot, Character.valueOf('f'), MeteorItems.FrozenIron, 
			Character.valueOf('k'), MeteorItems.KreknoriteIngot, Character.valueOf('b'), new ItemStack(MeteorBlocks.blockDecorator, 1, 0), 
			Character.valueOf('r'), Items.redstone
		});

		GameRegistry.addRecipe(new ItemStack(MeteorBlocks.blockRedMeteorGem, 1), new Object[] {
			"rrr", "rrr", "rrr", Character.valueOf('r'), MeteorItems.itemRedMeteorGem
		});

		GameRegistry.addShapelessRecipe(new ItemStack(MeteorItems.itemRedMeteorGem, 9), new Object[] {
			MeteorBlocks.blockRedMeteorGem
		});
		
		GameRegistry.addRecipe(new ItemStack(MeteorBlocks.blockDecorator, 1, 0), new Object[] {
			"iii", "iii", "iii", Character.valueOf('i'), MeteorItems.MeteoriteIngot
		});

		GameRegistry.addShapelessRecipe(new ItemStack(MeteorItems.MeteoriteIngot, 9), new Object[] {
			new ItemStack(MeteorBlocks.blockDecorator, 1, 0)
		});
		
		GameRegistry.addRecipe(new ItemStack(MeteorBlocks.blockDecorator, 1, 1), new Object[] {
			"iii", "iii", "iii", Character.valueOf('i'), MeteorItems.FrozenIron
		});

		GameRegistry.addShapelessRecipe(new ItemStack(MeteorItems.FrozenIron, 9), new Object[] {
			new ItemStack(MeteorBlocks.blockDecorator, 1, 1)
		});
		
		GameRegistry.addRecipe(new ItemStack(MeteorBlocks.blockDecorator, 1, 2), new Object[] {
			"iii", "iii", "iii", Character.valueOf('i'), MeteorItems.KreknoriteIngot
		});

		GameRegistry.addShapelessRecipe(new ItemStack(MeteorItems.KreknoriteIngot, 9), new Object[] {
			new ItemStack(MeteorBlocks.blockDecorator, 1, 2)
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.itemMeteorProximityDetector, 1), new Object[] { 
			" m ", "mrm", " m ", Character.valueOf('m'), MeteorItems.MeteoriteIngot, Character.valueOf('r'), Items.redstone
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.itemMeteorTimeDetector, 1), new Object[] { 
			" f ", "frf", " f ", Character.valueOf('f'), MeteorItems.FrozenIron, Character.valueOf('r'), MeteorItems.itemRedMeteorGem 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.itemMeteorCrashDetector, 1), new Object[] { 
			" k ", "krk", " k ", Character.valueOf('k'), MeteorItems.KreknoriteIngot, Character.valueOf('r'), MeteorItems.itemRedMeteorGem 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.itemMeteorSummoner, 1), new Object[] { 
			"rmr", "mrm", "rmr", Character.valueOf('m'), MeteorItems.MeteoriteIngot, Character.valueOf('r'), MeteorItems.itemRedMeteorGem 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.itemMeteorSummoner, 1, 1), new Object[] { 
			"mmm", "msm", "mmm", Character.valueOf('m'), MeteorItems.MeteoriteIngot, Character.valueOf('s'), MeteorItems.itemMeteorSummoner 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.itemMeteorSummoner, 1, 2), new Object[] { 
			"mmm", "msm", "mmm", Character.valueOf('m'), MeteorItems.FrozenIron, Character.valueOf('s'), MeteorItems.itemMeteorSummoner 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.itemMeteorSummoner, 1, 3), new Object[] { 
			"mmm", "msm", "mmm", Character.valueOf('m'), MeteorItems.KreknoriteIngot, Character.valueOf('s'), MeteorItems.itemMeteorSummoner 
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.itemMeteorSummoner, 1, 4), new Object[] { 
			"rmr", "ksk", "rfr", Character.valueOf('m'), MeteorItems.MeteoriteIngot, Character.valueOf('s'), MeteorItems.itemMeteorSummoner, 
			Character.valueOf('k'), MeteorItems.KreknoriteIngot, Character.valueOf('f'), MeteorItems.FrozenIron, Character.valueOf('r'),
			MeteorItems.itemRedMeteorGem
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.itemMeteorSummoner, 1, 5), new Object[] { 
			"mfm", "fsf", "mfm", Character.valueOf('m'), MeteorItems.MeteoriteIngot, Character.valueOf('s'), MeteorItems.itemMeteorSummoner, 
			Character.valueOf('f'), Items.fish
		});

		GameRegistry.addRecipe(new ItemStack(MeteorItems.itemMeteorSummoner, 1, 5), new Object[] { 
			"mfm", "fsf", "mfm", Character.valueOf('m'), MeteorItems.MeteoriteIngot, Character.valueOf('s'), MeteorItems.itemMeteorSummoner, 
			Character.valueOf('f'), Items.cooked_fished
		});
		
		GameRegistry.addShapelessRecipe(new ItemStack(MeteorItems.FrozenIron), new Object[] {
			Items.iron_ingot, MeteorItems.itemFrezaCrystal
		});
		
		// ============= Smelting Recipes =============
		GameRegistry.addSmelting(MeteorItems.itemMeteorChips, new ItemStack(MeteorItems.MeteoriteIngot), 0.7F);
		GameRegistry.addSmelting(MeteorItems.itemKreknoChip, new ItemStack(MeteorItems.KreknoriteIngot), 0.75F);
	}

}
