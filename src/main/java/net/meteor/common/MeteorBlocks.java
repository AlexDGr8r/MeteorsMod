package net.meteor.common;

import net.meteor.common.block.BlockDecoration;
import net.meteor.common.block.BlockFrezarite;
import net.meteor.common.block.BlockKreknorite;
import net.meteor.common.block.BlockMeteor;
import net.meteor.common.block.BlockMeteorOre;
import net.meteor.common.block.BlockMeteorShield;
import net.meteor.common.block.BlockMeteorShieldTorch;
import net.meteor.common.block.BlockMeteorTimer;
import net.meteor.common.block.BlockRareFallenMeteor;
import net.meteor.common.block.BlockRedMeteorGem;
import net.meteor.common.item.ItemBlockMeteorsMod;
import net.meteor.common.item.ItemBlockMeteorsModMetadata;
import net.minecraft.block.Block;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.registry.GameRegistry;

public class MeteorBlocks {
	
	public static final Block blockMeteor 				= new BlockMeteor().setBlockName("Meteor").setBlockTextureName("Meteor").setHardness(10F).setResistance(200F).setStepSound(Block.soundTypeStone).setLightLevel(0.5F);
	public static final Block blockMeteorOre			= new BlockMeteorOre().setBlockName("MeteorOre").setBlockTextureName("MeteorOre").setHardness(10F).setResistance(200F).setStepSound(Block.soundTypeStone);
	public static final Block blockRareMeteor			= new BlockRareFallenMeteor().setBlockName("Meteor").setBlockTextureName("MeteorRare").setHardness(10F).setResistance(200F).setStepSound(Block.soundTypeStone).setLightLevel(0.5F).setCreativeTab(null);
	public static final Block blockMeteorShield			= new BlockMeteorShield().setBlockName("MeteorShield").setBlockTextureName("Meteor").setHardness(2.5F).setStepSound(Block.soundTypeStone).setLightLevel(0.5F);
	public static final Block blockFrezarite			= new BlockFrezarite().setBlockName("Frezarite").setBlockTextureName("Frezarite").setHardness(8.5F).setResistance(150F).setStepSound(Block.soundTypeGlass).setLightLevel(0.25F);
	public static final Block blockKreknorite 			= new BlockKreknorite().setBlockName("Kreknorite").setBlockTextureName("Kreknorite").setHardness(11F).setResistance(350F).setStepSound(Block.soundTypeStone).setLightLevel(0.7F);
	public static final Block torchMeteorShieldIdle 	= new BlockMeteorShieldTorch(false).setHardness(0.0F).setStepSound(Block.soundTypeWood).setBlockName("ProtectedLandTester").setBlockTextureName("ProtectedLandTester").setCreativeTab(null);
	public static final Block torchMeteorShieldActive 	= new BlockMeteorShieldTorch(true).setHardness(0.0F).setLightLevel(0.5F).setStepSound(Block.soundTypeWood).setBlockName("ProtectedLandTesterActive").setBlockTextureName("ProtectedLandTesterActive").setCreativeTab(MeteorsMod.meteorTab);
	public static final Block blockMeteorTimer			= new BlockMeteorTimer().setHardness(0.0F).setStepSound(Block.soundTypeWood).setBlockName("MeteorTimer").setBlockTextureName("MeteorTimer");
	public static final Block blockRedMeteorGem			= new BlockRedMeteorGem().setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundTypeMetal).setBlockName("blockRedGem").setBlockTextureName("blockRedGem");
	public static final Block blockDecorator			= new BlockDecoration("meteorite_block", "frezarite_block", "kreknorite_block").setBlockName("meteorDecor").setStepSound(Block.soundTypeMetal).setHardness(5.0F).setResistance(10.0F);
	
	public static void registerBlocks() {
		GameRegistry.registerBlock(blockMeteorOre, ItemBlockMeteorsMod.class, "BlockMeteorOre");
		GameRegistry.registerBlock(blockMeteor, ItemBlockMeteorsMod.class, "BlockMeteor");
		GameRegistry.registerBlock(blockRareMeteor, ItemBlockMeteorsMod.class, "BlockMeteorRare");
		GameRegistry.registerBlock(blockMeteorShield, ItemBlockMeteorsMod.class, "BlockMeteorShield");
		GameRegistry.registerBlock(torchMeteorShieldActive, ItemBlockMeteorsMod.class, "BlockMeteorShieldA");
		GameRegistry.registerBlock(torchMeteorShieldIdle, ItemBlockMeteorsMod.class, "BlockMeteorShieldI");
		GameRegistry.registerBlock(blockFrezarite, ItemBlockMeteorsMod.class, "BlockFrezarite");
		GameRegistry.registerBlock(blockKreknorite, ItemBlockMeteorsMod.class, "BlockKreknorite");
		GameRegistry.registerBlock(blockMeteorTimer, ItemBlockMeteorsMod.class, "BlockMeteorTimer");
		GameRegistry.registerBlock(blockRedMeteorGem, ItemBlockMeteorsMod.class, "BlockRedMeteorGem");
		GameRegistry.registerBlock(blockDecorator, ItemBlockMeteorsModMetadata.class, "BlockMeteorDecoration");
		
		// Ore Dictionary
		OreDictionary.registerOre("oreMeteorite", blockMeteorOre);
	}

}
