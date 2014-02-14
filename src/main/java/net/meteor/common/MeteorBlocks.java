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
import net.minecraft.block.Block;
import cpw.mods.fml.common.registry.GameRegistry;

public class MeteorBlocks {
	
	public static final Block blockMeteor 				= new BlockMeteor().setBlockName("Meteor").setBlockTextureName("Meteor").setHardness(10F).setResistance(200F).setStepSound(Block.soundTypeStone).setLightLevel(0.5F);
	public static final Block blockMeteorOre			= new BlockMeteorOre().setBlockName("MeteorOre").setBlockTextureName("Meteor").setHardness(10F).setResistance(200F).setStepSound(Block.soundTypeStone);
	public static final Block blockRareMeteor			= new BlockRareFallenMeteor().setBlockName("MeteorRare").setBlockTextureName("MeteorRare").setHardness(10F).setResistance(200F).setStepSound(Block.soundTypeStone).setLightLevel(0.5F);
	public static final Block blockMeteorShield			= new BlockMeteorShield().setBlockName("MeteorShield").setBlockTextureName("MeteorShield").setHardness(2.5F).setStepSound(Block.soundTypeStone).setLightLevel(0.5F);
	public static final Block blockFrezarite			= new BlockFrezarite().setBlockName("Frezarite").setBlockTextureName("Frezarite").setHardness(8.5F).setResistance(150F).setStepSound(Block.soundTypeGlass).setLightLevel(0.25F);
	public static final Block blockKreknorite 			= new BlockKreknorite().setBlockName("Kreknorite").setBlockTextureName("Kreknorite").setHardness(11F).setResistance(350F).setStepSound(Block.soundTypeStone).setLightLevel(0.7F);
	public static final Block torchMeteorShieldIdle 	= new BlockMeteorShieldTorch(false).setHardness(0.0F).setStepSound(Block.soundTypeWood).setBlockName("ProtectedLandTester").setBlockTextureName("ProtectedLandTester");
	public static final Block torchMeteorShieldActive 	= new BlockMeteorShieldTorch(true).setHardness(0.0F).setLightLevel(0.5F).setStepSound(Block.soundTypeWood).setBlockName("ProtectedLandTesterActive").setBlockTextureName("ProtectedLandTesterActive");
	public static final Block blockMeteorTimer			= new BlockMeteorTimer().setHardness(0.0F).setStepSound(Block.soundTypeWood).setBlockName("MeteorTimer").setBlockTextureName("MeteorTimer");
	public static final Block blockRedMeteorGem			= new BlockRedMeteorGem().setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundTypeMetal).setBlockName("blockRedGem").setBlockTextureName("blockRedGem");
	public static final Block blockDecorator			= new BlockDecoration("blockMeteorMetal", "blockFrezMetal", "blockKrekMetal").setBlockName("meteorDecor").setStepSound(Block.soundTypeStone).setHardness(5.0F).setResistance(10.0F);
	
	public static void readyBlocks() {
		// TODO new functions in Block class for this
//		MinecraftForge.setBlockHarvestLevel(blockMeteor, "pickaxe", 2);
//		MinecraftForge.setBlockHarvestLevel(blockMeteorOre, "pickaxe", 2);
//		MinecraftForge.setBlockHarvestLevel(blockRareMeteor, "pickaxe", 2);
//		MinecraftForge.setBlockHarvestLevel(blockMeteor, 0, "pickaxe", 1);
//		MinecraftForge.setBlockHarvestLevel(blockRareMeteor, 0, "pickaxe", 1);
//		MinecraftForge.setBlockHarvestLevel(blockFrezarite, "pickaxe", 1);
//		MinecraftForge.setBlockHarvestLevel(blockKreknorite, "pickaxe", 3);
	}
	
	public static void registerBlocks() {
		GameRegistry.registerBlock(MeteorBlocks.blockMeteorOre, ItemBlockMeteorsMod.class, "BlockMeteorOre");
		GameRegistry.registerBlock(MeteorBlocks.blockMeteor, ItemBlockMeteorsMod.class, "BlockMeteor");
		GameRegistry.registerBlock(MeteorBlocks.blockMeteorShield, ItemBlockMeteorsMod.class, "BlockMeteorShield");
		GameRegistry.registerBlock(MeteorBlocks.torchMeteorShieldActive, ItemBlockMeteorsMod.class, "BlockMeteorShieldA");
		GameRegistry.registerBlock(MeteorBlocks.blockFrezarite, ItemBlockMeteorsMod.class, "BlockFrezarite");
		GameRegistry.registerBlock(MeteorBlocks.blockKreknorite, ItemBlockMeteorsMod.class, "BlockKreknorite");
		GameRegistry.registerBlock(MeteorBlocks.blockMeteorTimer, ItemBlockMeteorsMod.class, "BlockMeteorTimer");
		GameRegistry.registerBlock(MeteorBlocks.blockRedMeteorGem, ItemBlockMeteorsMod.class, "BlockRedMeteorGem");
	}

}
