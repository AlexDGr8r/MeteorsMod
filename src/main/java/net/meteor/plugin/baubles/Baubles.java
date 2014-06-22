package net.meteor.plugin.baubles;

import net.meteor.common.MeteorItems;
import net.meteor.common.item.ItemMeteorsMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;

public class Baubles {
	
	public static Item MagnetismController;
	public static Item MagneticFieldDisruptor;
	
	public static long renderDisplayTicks;
	public static boolean renderDisplay = false;
	public static boolean enabledMagnetism;
	
	private static boolean baublesLoaded = false;
	private static HandlerKey keyHandler;
	
	public static void setupBaubleItems() {
		baublesLoaded = true;
		MagnetismController = new ItemMagnetismController().setUnlocalizedName("MagnetizationController").setTextureName("MagnetizationController");
		MagneticFieldDisruptor = new ItemMeteorsMod().setUnlocalizedName("MagneticFieldDisruptor").setTextureName("MagneticFieldDisruptor");
		GameRegistry.registerItem(MagnetismController, "MagnetizationController");
		GameRegistry.registerItem(MagneticFieldDisruptor, "MagneticFieldDisruptor");
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MagneticFieldDisruptor, 1), new Object[] {
			"oro", "mdm", "omo", Character.valueOf('o'), Blocks.obsidian, Character.valueOf('r'), MeteorItems.itemRedMeteorGem,
			Character.valueOf('d'), "gemDiamond", Character.valueOf('m'), MeteorItems.MeteoriteIngot
		}));
		
		GameRegistry.addRecipe(new ItemStack(MagnetismController, 1), new Object[] {
			" s ", "lml", Character.valueOf('s'), Items.string, Character.valueOf('l'), Items.leather,
			Character.valueOf('m'), MagneticFieldDisruptor
		});
		
		keyHandler = new HandlerKey();
		keyHandler.init();
		FMLCommonHandler.instance().bus().register(keyHandler);
	}
	
	public static boolean isBaublesLoaded() {
		return baublesLoaded;
	}
	
	public static boolean canAttractItems(EntityPlayer player, boolean gearMagnetized) {
		return ItemMagnetismController.isMagnetizationEnabled(player, gearMagnetized);
	}

}
