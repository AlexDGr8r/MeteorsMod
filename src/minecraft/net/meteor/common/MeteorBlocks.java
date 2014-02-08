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
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.registry.GameRegistry;

public class MeteorBlocks {
	
	public static final Block blockMeteor 				= new BlockMeteor(ModConfig.instance.getBlockID("Fallen Meteor ID", 667)).setUnlocalizedName("Meteor").setTextureName("Meteor").setHardness(10F).setResistance(200F).setStepSound(Block.soundStoneFootstep).setLightValue(0.5F);
	public static final Block blockMeteorOre			= new BlockMeteorOre(ModConfig.instance.getBlockID("Underground Meteor ID", 671)).setUnlocalizedName("MeteorOre").setTextureName("Meteor").setHardness(10F).setResistance(200F).setStepSound(Block.soundStoneFootstep);
	public static final Block blockRareMeteor			= new BlockRareFallenMeteor(ModConfig.instance.getBlockID("Rare Fallen Meteor ID", 672)).setUnlocalizedName("MeteorRare").setTextureName("MeteorRare").setHardness(10F).setResistance(200F).setStepSound(Block.soundStoneFootstep).setLightValue(0.5F);
	public static final Block blockMeteorShield			= new BlockMeteorShield(ModConfig.instance.getBlockID("Meteor Shield ID", 668)).setUnlocalizedName("MeteorShield").setTextureName("MeteorShield").setHardness(2.5F).setStepSound(Block.soundStoneFootstep).setLightValue(0.5F);
	public static final Block blockFrezarite			= new BlockFrezarite(ModConfig.instance.getBlockID("Frezarite Block ID", 673)).setUnlocalizedName("Frezarite").setTextureName("Frezarite").setHardness(8.5F).setResistance(150F).setStepSound(Block.soundGlassFootstep).setLightValue(0.25F);
	public static final Block blockKreknorite 			= new BlockKreknorite(ModConfig.instance.getBlockID("Kreknorite ID", 674)).setUnlocalizedName("Kreknorite").setTextureName("Kreknorite").setHardness(11F).setResistance(350F).setStepSound(Block.soundStoneFootstep).setLightValue(0.7F);
	public static final Block torchMeteorShieldIdle 	= new BlockMeteorShieldTorch(ModConfig.instance.getBlockID("Test Torch Unlit ID", 669), false).setHardness(0.0F).setStepSound(Block.soundWoodFootstep).setUnlocalizedName("ProtectedLandTester").setTextureName("ProtectedLandTester");
	public static final Block torchMeteorShieldActive 	= new BlockMeteorShieldTorch(ModConfig.instance.getBlockID("Test Torch Lit ID", 670), true).setHardness(0.0F).setLightValue(0.5F).setStepSound(Block.soundWoodFootstep).setUnlocalizedName("ProtectedLandTesterActive").setTextureName("ProtectedLandTesterActive");
	public static final Block blockMeteorTimer			= new BlockMeteorTimer(ModConfig.instance.getBlockID("Meteor Timer Block ID", 675)).setHardness(0.0F).setStepSound(Block.soundWoodFootstep).setUnlocalizedName("MeteorTimer").setTextureName("MeteorTimer");
	public static final Block blockRedMeteorGem			= new BlockRedMeteorGem(ModConfig.instance.getBlockID("Block Red Meteor Gem ID", 676)).setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundMetalFootstep).setUnlocalizedName("blockRedGem").setTextureName("blockRedGem");
	public static final Block blockDecorator			= new BlockDecoration(ModConfig.instance.getBlockID("Compressed Ore Decoration ID", 677), "blockMeteorMetal", "blockFrezMetal", "blockKrekMetal").setUnlocalizedName("meteorDecor").setStepSound(Block.soundStoneFootstep).setHardness(5.0F).setResistance(10.0F);
	
	public static void readyBlocks() {
		MinecraftForge.setBlockHarvestLevel(blockMeteor, "pickaxe", 2);
		MinecraftForge.setBlockHarvestLevel(blockMeteorOre, "pickaxe", 2);
		MinecraftForge.setBlockHarvestLevel(blockRareMeteor, "pickaxe", 2);
		MinecraftForge.setBlockHarvestLevel(blockMeteor, 0, "pickaxe", 1);
		MinecraftForge.setBlockHarvestLevel(blockRareMeteor, 0, "pickaxe", 1);
		MinecraftForge.setBlockHarvestLevel(blockFrezarite, "pickaxe", 1);
		MinecraftForge.setBlockHarvestLevel(blockKreknorite, "pickaxe", 3);
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
