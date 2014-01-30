package net.meteor.common;

import java.util.Random;
import java.util.logging.Logger;

import net.meteor.common.block.BlockFrezarite;
import net.meteor.common.block.BlockKreknorite;
import net.meteor.common.block.BlockMeteor;
import net.meteor.common.block.BlockMeteorOre;
import net.meteor.common.block.BlockMeteorShield;
import net.meteor.common.block.BlockMeteorShieldTorch;
import net.meteor.common.block.BlockMeteorTimer;
import net.meteor.common.block.BlockRareFallenMeteor;
import net.meteor.common.block.BlockRedMeteorGem;
import net.meteor.common.command.CommandKittyAttack;
import net.meteor.common.enchantment.EnchantmentColdTouch;
import net.meteor.common.enchantment.EnchantmentMagnetized;
import net.meteor.common.entity.EntityAlienCreeper;
import net.meteor.common.entity.EntityCometKitty;
import net.meteor.common.entity.EntityMeteor;
import net.meteor.common.entity.EntitySummoner;
import net.meteor.common.item.ItemBlockMeteorsMod;
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
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderGenerate;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.EnumHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.ICraftingHandler;
import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid=MeteorsMod.MOD_ID, name=MeteorsMod.MOD_NAME, version=MeteorsMod.VERSION)
@NetworkMod(clientSideRequired=true, serverSideRequired=false, channels={"MetSettings", "MetNewCrash", "MetNewTime", "MetGhostAdd", "MetGhostRem", "MetShield"}, packetHandler=ClientHandler.class)
public class MeteorsMod
implements ICraftingHandler, IFuelHandler, IWorldGenerator
{
	
	public static final String MOD_ID = "meteors";
	public static final String MOD_NAME = "Falling Meteors";
	public static final String VERSION = "2.12"; 		// Switch to automatic versioning later on
	
	public static final boolean loggable = false;		// For Debugging Purposes Only
	public static final boolean forModpack = false;		// TODO Change this for publishing modpack

	public static final Logger log = FMLLog.getLogger();

	public static final EnumArmorMaterial MeteoriteArmor = EnumHelper.addArmorMaterial("METEORITE", 36, new int[] { 2, 7, 5, 2 }, 15);
	public static final EnumArmorMaterial FrezariteArmor = EnumHelper.addArmorMaterial("FREZARITE", 7, new int[] { 2, 5, 3, 1 }, 25);
	public static final EnumArmorMaterial KreknoriteArmor = EnumHelper.addArmorMaterial("KREKNORITE", 40, new int[] { 3, 8, 6, 3 }, 10);

	public static final EnumToolMaterial MeteoriteTool = EnumHelper.addToolMaterial("METEORITE", 3, 900, 10.0F, 4, 10);
	public static final EnumToolMaterial FrezariteTool = EnumHelper.addToolMaterial("FREZARITE", 2, 225, 7.0F, 2, 14);
	
	public static final Enchantment Magnetization = new EnchantmentMagnetized(ModConfig.instance.get("Magnetization Enchantment ID", 157), 3).setName("Magnetization");
	public static final Enchantment ColdTouch 	  = new EnchantmentColdTouch(ModConfig.instance.get("Cold Touch Enchantment ID", 158), 3).setName("Cold Touch");
	
	public static final CreativeTabs meteorTab = new CreativeTabMeteor("Falling Meteors Mod");
	
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
	public static final Item MeteoriteHelmet 			= new ItemEnchArmor(ModConfig.instance.getItemID("Meteor Helmet ID", 30229), MeteoriteArmor, 3, 0).setEnch(Magnetization, 1).setUnlocalizedName("MeteoriteHelmet").setTextureName("MeteoriteHelmet");
	public static final Item MeteoriteBody 				= new ItemEnchArmor(ModConfig.instance.getItemID("Meteor Body ID", 30230), MeteoriteArmor, 3, 1).setEnch(Magnetization, 1).setUnlocalizedName("MeteoriteChest").setTextureName("MeteoriteChest");
	public static final Item MeteoriteLegs 				= new ItemEnchArmor(ModConfig.instance.getItemID("Meteor Legs ID", 30231), MeteoriteArmor, 3, 2).setEnch(Magnetization, 1).setUnlocalizedName("MeteoriteLegs").setTextureName("MeteoriteLegs");
	public static final Item MeteoriteBoots 			= new ItemEnchArmor(ModConfig.instance.getItemID("Meteor Boots ID", 30232), MeteoriteArmor, 3, 3).setEnch(Magnetization, 1).setUnlocalizedName("MeteoriteBoots").setTextureName("MeteoriteBoots");
	public static final Item MeteoriteAxe 				= new ItemEnchAxe(ModConfig.instance.getItemID("Meteor Axe ID", 30233), MeteoriteTool).setEnch(Magnetization, 1).setUnlocalizedName("MeteoriteAxe").setTextureName("MeteoriteAxe");
	public static final Item MeteoriteSpade 			= new ItemEnchSpade(ModConfig.instance.getItemID("Meteor Spade ID", 30234), MeteoriteTool).setEnch(Magnetization, 1).setUnlocalizedName("MeteoriteSpade").setTextureName("MeteoriteSpade");
	public static final Item MeteoriteSword 			= new ItemEnchSword(ModConfig.instance.getItemID("Meteor Sword ID", 30235), MeteoriteTool).setEnch(Magnetization, 1).setUnlocalizedName("MeteoriteSword").setTextureName("MeteoriteSword");
	public static final Item MeteoritePickaxe 			= new ItemEnchPickaxe(ModConfig.instance.getItemID("Meteor Pickaxe ID", 30236), MeteoriteTool).setEnch(Magnetization, 1).setUnlocalizedName("MeteoritePickaxe").setTextureName("MeteoritePickaxe");
	public static final Item MeteoriteHoe 				= new ItemEnchHoe(ModConfig.instance.getItemID("Meteor Hoe ID", 30237), MeteoriteTool).setEnch(Magnetization, 1).setUnlocalizedName("MeteoriteHoe").setTextureName("MeteoriteHoe");
	public static final Item FrezariteHelmet 			= new ItemEnchArmor(ModConfig.instance.getItemID("Frezarite Helmet ID", 30240), FrezariteArmor, 3, 0).setEnch(Enchantment.respiration, 3).setUnlocalizedName("FrezariteHelmet").setTextureName("FrezariteHelmet");
	public static final Item FrezariteBody 				= new ItemEnchArmor(ModConfig.instance.getItemID("Frezarite Body ID", 30241), FrezariteArmor, 3, 1).setEnch(Enchantment.aquaAffinity, 1).setUnlocalizedName("FrezariteChest").setTextureName("FrezariteChest");
	public static final Item FrezariteLegs 				= new ItemEnchArmor(ModConfig.instance.getItemID("Frezarite Legs ID", 30242), FrezariteArmor, 3, 2).setEnch(ColdTouch, 1).setUnlocalizedName("FrezariteLegs").setTextureName("FrezariteLegs");
	public static final Item FrezariteBoots 			= new ItemEnchArmor(ModConfig.instance.getItemID("Frezarite Boots ID", 30243), FrezariteArmor, 3, 3).setEnch(ColdTouch, 1).setUnlocalizedName("FrezariteBoots").setTextureName("FrezariteBoots");
	public static final Item FrezaritePickaxe 			= new ItemFrezaritePickaxe(ModConfig.instance.getItemID("Frezarite Pickaxe ID", 30256), FrezariteTool).setUnlocalizedName("FrezaritePickaxe").setTextureName("FrezaritePickaxe");
	public static final Item FrezariteSpade 			= new ItemFrezariteSpade(ModConfig.instance.getItemID("Frezarite Spade ID", 30257), FrezariteTool).setUnlocalizedName("FrezariteSpade").setTextureName("FrezariteSpade");
	public static final Item FrezariteSword 			= new ItemFrezariteSword(ModConfig.instance.getItemID("Frezarite Sword ID", 30258), FrezariteTool).setUnlocalizedName("FrezariteSword").setTextureName("FrezariteSword");
	public static final Item FrezariteAxe 				= new ItemFrezariteAxe(ModConfig.instance.getItemID("Frezarite Axe ID", 30259), FrezariteTool).setUnlocalizedName("FrezariteAxe").setTextureName("FrezariteAxe");
	public static final Item FrezariteHoe 				= new ItemFrezariteHoe(ModConfig.instance.getItemID("Frezarite Hoe ID", 30260), FrezariteTool).setUnlocalizedName("FrezariteHoe").setTextureName("FrezariteHoe");
	public static final Item KreknoriteHelmet 			= new ItemEnchArmor(ModConfig.instance.getItemID("Kreknorite Helmet ID", 30246), KreknoriteArmor, 3, 0).setEnch(Enchantment.fireProtection, 4).setUnlocalizedName("KreknoriteHelmet").setTextureName("KreknoriteHelmet");
	public static final Item KreknoriteBody 			= new ItemEnchArmor(ModConfig.instance.getItemID("Kreknorite Body ID", 30247), KreknoriteArmor, 3, 1).setEnch(Enchantment.fireProtection, 4).setUnlocalizedName("KreknoriteChest").setTextureName("KreknoriteChest");
	public static final Item KreknoriteLegs 			= new ItemEnchArmor(ModConfig.instance.getItemID("Kreknorite Legs ID", 30248), KreknoriteArmor, 3, 2).setEnch(Enchantment.fireProtection, 4).setUnlocalizedName("KreknoriteLegs").setTextureName("KreknoriteLegs");
	public static final Item KreknoriteBoots 			= new ItemEnchArmor(ModConfig.instance.getItemID("Kreknorite Boots ID", 30249), KreknoriteArmor, 3, 3).setEnch(Enchantment.fireProtection, 4).setUnlocalizedName("KreknoriteBoots").setTextureName("KreknoriteBoots");
	public static final Item KreknoriteSword 			= new ItemKreknoSword(ModConfig.instance.getItemID("Kreknorite Sword ID", 30250), MeteoriteTool).setUnlocalizedName("KreknoriteSword").setTextureName("KreknoriteSword");

	@SidedProxy(clientSide="net.meteor.common.ClientProxy", serverSide="net.meteor.common.CommonProxy")
	public static CommonProxy proxy;

	@Mod.Instance(MOD_ID)
	public static MeteorsMod instance;
	public HandlerAchievement achHandler;
	public HandlerPlayerTick playerTickHandler;
	public int MinTicksUntilMeteorSpawn;
	public int RandTicksUntilMeteorSpawn;
	public int MinTicksUntilMeteorCrashes;
	public int RandTicksUntilMeteorCrashes;
	public boolean meteorsFallOnlyAtNight;
	public boolean allowSummonedMeteorGrief = false;
	public int meteorFallDistance				= ModConfig.instance.get("Meteor Fall Radius", 350);
	public int MaxMeteorSize;
	public int MinMeteorSize;
	public int ShieldRadiusMultiplier;
	public int kittyAttackChance				= ModConfig.instance.get("Kitty Attack Chance", 1);
	public boolean textNotifyCrash				= ModConfig.instance.get("Text Crash Notification", false);
	public boolean meteoriteEnabled				= ModConfig.instance.get("Meteorite Meteor Enabled", true);
	public boolean frezariteEnabled				= ModConfig.instance.get("Frezarite Meteor Enabled", true);
	public boolean kreknoriteEnabled			= ModConfig.instance.get("Kreknorite Meteor Enabled", true);
	public boolean unknownEnabled				= ModConfig.instance.get("Unknown Meteor Enabled", true);
	private int chunkChecks 					= ModConfig.instance.get("Chunk Generation Checks", 4);
	private int oreGenSize  					= ModConfig.instance.get("Meteor Ore Gen Size", 6);
	public int MinMeteorSizeForPortal;
	public double ImpactExplosionMultiplier;
	public int ImpactSpread;

	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		regAndNameBlocks();
		registerEntities();
		readyBlocksAndItems();
		setRecipes();

		this.playerTickHandler = new HandlerPlayerTick();

		this.achHandler.readyAchievements();
		proxy.loadStuff();

		MinecraftForge.EVENT_BUS.register(new HandlerPlayerBreakSpeed());
		MinecraftForge.EVENT_BUS.register(new HandlerWorld());

		GameRegistry.registerCraftingHandler(this);
		GameRegistry.registerFuelHandler(this);
		GameRegistry.registerWorldGenerator(this);
		GameRegistry.registerPickupHandler(achHandler);

		TickRegistry.registerTickHandler(this.playerTickHandler, Side.SERVER);
		TickRegistry.registerTickHandler(this.playerTickHandler, Side.CLIENT);

		ClientHandler cHandler = new ClientHandler();
		NetworkRegistry.instance().registerConnectionHandler(cHandler);
		MinecraftForge.EVENT_BUS.register(cHandler);
	}

	private void loadStaticConfigurationValues() {
		// General Configuration
		int configTicks = ModConfig.instance.get("Meteor Fall Deterrence", 25) * 100;
		int mSpawn = (int)(configTicks * 0.25D);
		int mCrash = (int)(configTicks * 0.75D);
		this.MinTicksUntilMeteorSpawn = ((int)(mSpawn * 0.25D));
		this.RandTicksUntilMeteorSpawn = ((int)(mSpawn * 0.75D));
		this.MinTicksUntilMeteorCrashes = ((int)(mCrash * 0.5D));
		this.RandTicksUntilMeteorCrashes = ((int)(mCrash * 0.5D));
		setClientStartConfig();
	}

	// Values loaded every new world Load and initially when mod is constructed
	public void setClientStartConfig() {
		this.meteorsFallOnlyAtNight = ModConfig.instance.get("Meteors Only Fall at Night", true);
		this.allowSummonedMeteorGrief = ModConfig.instance.get("Allow Summoned Meteor Grief", false);
		this.ShieldRadiusMultiplier = ModConfig.instance.get("Shield Radius in Chunks", 4);
		this.MinMeteorSize = ModConfig.instance.get("Minimum Meteor Size", 1);
		this.MaxMeteorSize = ModConfig.instance.get("Maximum Meteor Size", 3);
		this.MinMeteorSize = MathHelper.clamp_int(this.MinMeteorSize, 1, 3);
		this.MaxMeteorSize = MathHelper.clamp_int(this.MaxMeteorSize, 1, 3);
		if (this.MinMeteorSize > this.MaxMeteorSize)
			this.MinMeteorSize = this.MaxMeteorSize;
		else if (this.MaxMeteorSize < this.MinMeteorSize) {
			this.MaxMeteorSize = this.MinMeteorSize;
		}
		this.MinMeteorSizeForPortal = ModConfig.instance.get("Minimum Meteor Size To Spawn Nether Portal", 2);
		if (this.MinMeteorSizeForPortal < this.MinMeteorSize)
			this.MinMeteorSizeForPortal = this.MinMeteorSize;
		this.ImpactExplosionMultiplier = ModConfig.instance.get("Meteor Impact Explosion Multiplier", 5.0D);
		if (this.ImpactExplosionMultiplier > 20.0) {
			this.ImpactExplosionMultiplier = 20.0;
		} else if (this.ImpactExplosionMultiplier < 0.0) {
			this.ImpactExplosionMultiplier = 0.0;
		}
		this.ImpactSpread = MathHelper.clamp_int(ModConfig.instance.get("Meteor Impact Spread", 4), 0, 8);
	}

	@EventHandler
	public void loadConfigurationValues(FMLPreInitializationEvent event) {
		ModConfig.instance.load(event.getSuggestedConfigurationFile());
		loadStaticConfigurationValues();
		LangLocalization.addLocalization("/assets/meteors/lang/", "en_US");
		
		HandlerMeteor.defaultType = EnumMeteor.METEORITE;
		if (!this.meteoriteEnabled) {
			HandlerMeteor.defaultType = EnumMeteor.FREZARITE;
			if (!this.frezariteEnabled) {
				HandlerMeteor.defaultType = EnumMeteor.KREKNORITE;
				if (!this.kreknoriteEnabled) {
					HandlerMeteor.defaultType = EnumMeteor.UNKNOWN;
					if (!this.unknownEnabled)
						HandlerMeteor.defaultType = EnumMeteor.METEORITE;
				}
			}
		}
		
		proxy.loadSounds();
		this.achHandler = new HandlerAchievement();
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent evt) {
		evt.registerServerCommand(new CommandKittyAttack());
	}

	private void regAndNameBlocks() {
		GameRegistry.registerBlock(blockMeteorOre, ItemBlockMeteorsMod.class, "BlockMeteorOre");
		GameRegistry.registerBlock(blockMeteor, ItemBlockMeteorsMod.class, "BlockMeteor");
		GameRegistry.registerBlock(blockMeteorShield, ItemBlockMeteorsMod.class, "BlockMeteorShield");
		GameRegistry.registerBlock(torchMeteorShieldActive, ItemBlockMeteorsMod.class, "BlockMeteorShieldA");
		GameRegistry.registerBlock(blockFrezarite, ItemBlockMeteorsMod.class, "BlockFrezarite");
		GameRegistry.registerBlock(blockKreknorite, ItemBlockMeteorsMod.class, "BlockKreknorite");
		GameRegistry.registerBlock(blockMeteorTimer, ItemBlockMeteorsMod.class, "BlockMeteorTimer");
		GameRegistry.registerBlock(blockRedMeteorGem, ItemBlockMeteorsMod.class, "BlockRedMeteorGem");
		GameRegistry.registerItem(itemMeteorChips, "MeteorChips");
		GameRegistry.registerItem(itemRedMeteorGem, "RedMeteorGem");
		GameRegistry.registerItem(itemChocolateIceCream, "ChocolateICream");
		GameRegistry.registerItem(itemFrezaCrystal, "FrezaCrystal");
		GameRegistry.registerItem(itemKreknoChip, "KreknoChip");
		GameRegistry.registerItem(itemMeteorCrashDetector, "MeteorCrashDet");
		GameRegistry.registerItem(itemMeteorProximityDetector, "MeteorProxDet");
		GameRegistry.registerItem(itemMeteorSummoner, "MeteorSummoner");
		GameRegistry.registerItem(itemMeteorTimeDetector, "MeteorTimeDet");
		GameRegistry.registerItem(itemVanillaIceCream, "VanillaICream");
		GameRegistry.registerItem(FrezariteBody, "FrezBody");
		GameRegistry.registerItem(FrezariteBoots, "FrezBoots");
		GameRegistry.registerItem(FrezariteHelmet, "FrezHelm");
		GameRegistry.registerItem(FrezariteLegs, "FrezLegs");
		GameRegistry.registerItem(FrezaritePickaxe, "FrezPick");
		GameRegistry.registerItem(FrezariteSpade, "FrezSpade");
		GameRegistry.registerItem(KreknoriteBody, "KrekBody");
		GameRegistry.registerItem(KreknoriteBoots, "KrekBoots");
		GameRegistry.registerItem(KreknoriteHelmet, "KrekHelm");
		GameRegistry.registerItem(KreknoriteLegs, "KrekLegs");
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
	}

	private void registerEntities() {
		proxy.registerTileEntities();
		EntityRegistry.registerGlobalEntityID(EntityMeteor.class, "Meteor", EntityRegistry.findGlobalUniqueEntityId());
		EntityRegistry.registerModEntity(EntityMeteor.class, "FallingMeteor", 1, this, 64, 8, true);
		EntityRegistry.registerGlobalEntityID(EntityAlienCreeper.class, "AlienCreeper", EntityRegistry.findGlobalUniqueEntityId(), 7864485, 16732697);
		EntityRegistry.registerModEntity(EntityAlienCreeper.class, "AlienCreeper", 3, this, 80, 3, true);
		EntityRegistry.registerGlobalEntityID(EntityCometKitty.class, "CometKitty", EntityRegistry.findGlobalUniqueEntityId(), 2239283, 884535);
		EntityRegistry.registerModEntity(EntityCometKitty.class, "CometKitty", 4, this, 80, 3, true);
		EntityRegistry.registerGlobalEntityID(EntitySummoner.class, "MeteorSummoner", EntityRegistry.findGlobalUniqueEntityId());
		EntityRegistry.registerModEntity(EntitySummoner.class, "MeteorSummoner", 2, this, 64, 8, true);
	}

	private void readyBlocksAndItems() {
		MinecraftForge.setBlockHarvestLevel(blockMeteor, "pickaxe", 2);
		MinecraftForge.setBlockHarvestLevel(blockMeteorOre, "pickaxe", 2);
		MinecraftForge.setBlockHarvestLevel(blockRareMeteor, "pickaxe", 2);
		MinecraftForge.setBlockHarvestLevel(blockMeteor, 0, "pickaxe", 1);
		MinecraftForge.setBlockHarvestLevel(blockRareMeteor, 0, "pickaxe", 1);
		MinecraftForge.setBlockHarvestLevel(blockFrezarite, "pickaxe", 1);
		MinecraftForge.setBlockHarvestLevel(blockKreknorite, "pickaxe", 3);
		MinecraftForge.setToolClass(MeteoriteAxe, "axe", 3);
		MinecraftForge.setToolClass(MeteoritePickaxe, "pickaxe", 3);
		MinecraftForge.setToolClass(MeteoriteSpade, "shovel", 3);
		MinecraftForge.setToolClass(FrezaritePickaxe, "pickaxe", 2);
		MinecraftForge.setToolClass(FrezariteSpade, "shovel", 2);
		MinecraftForge.setToolClass(FrezariteAxe, "axe", 2);
	}

	private void setRecipes() {
		GameRegistry.addRecipe(new ItemStack(MeteoriteHelmet, 1), new Object[] { 
			"mmm", "m m", Character.valueOf('m'), itemMeteorChips 
		});

		GameRegistry.addRecipe(new ItemStack(MeteoriteBody, 1), new Object[] { 
			"m m", "mmm", "mmm", Character.valueOf('m'), itemMeteorChips 
		});

		GameRegistry.addRecipe(new ItemStack(MeteoriteLegs, 1), new Object[] { 
			"mmm", "m m", "m m", Character.valueOf('m'), itemMeteorChips 
		});

		GameRegistry.addRecipe(new ItemStack(MeteoriteBoots, 1), new Object[] { 
			"m m", "m m", Character.valueOf('m'), itemMeteorChips 
		});

		GameRegistry.addRecipe(new ItemStack(MeteoriteAxe, 1), new Object[] { 
			"mm", "ms", " s", Character.valueOf('m'), itemMeteorChips, Character.valueOf('s'), Item.ingotIron 
		});

		GameRegistry.addRecipe(new ItemStack(MeteoriteAxe, 1), new Object[] { 
			"mm", "sm", "s ", Character.valueOf('m'), itemMeteorChips, Character.valueOf('s'), Item.ingotIron 
		});

		GameRegistry.addRecipe(new ItemStack(MeteoriteSpade, 1), new Object[] { 
			"m", "s", "s", Character.valueOf('m'), itemMeteorChips, Character.valueOf('s'), Item.ingotIron 
		});

		GameRegistry.addRecipe(new ItemStack(MeteoriteSword), new Object[] { 
			"m", "m", "s", Character.valueOf('m'), itemMeteorChips, Character.valueOf('s'), Item.ingotIron 
		});

		GameRegistry.addRecipe(new ItemStack(MeteoritePickaxe, 1), new Object[] { 
			"mmm", " s ", " s ", Character.valueOf('m'), itemMeteorChips, Character.valueOf('s'), Item.ingotIron 
		});

		GameRegistry.addRecipe(new ItemStack(MeteoriteHoe, 1), new Object[] { 
			"mm", " s", " s", Character.valueOf('m'), itemMeteorChips, Character.valueOf('s'), Item.ingotIron 
		});

		GameRegistry.addRecipe(new ItemStack(MeteoriteHoe, 1), new Object[] { 
			"mm", "s ", "s ", Character.valueOf('m'), itemMeteorChips, Character.valueOf('s'), Item.ingotIron
		});

		GameRegistry.addRecipe(new ItemStack(blockMeteorOre, 1), new Object[] { 
			"mm", "mm", Character.valueOf('m'), itemMeteorChips 
		});

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(torchMeteorShieldActive, 4), new Object[] { 
			"m", "s", Character.valueOf('m'), itemMeteorChips, Character.valueOf('s'), "stickWood" 
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockMeteorShield, 1), new Object[] { 
			"mmm", "crc", "ccc", Character.valueOf('m'), itemMeteorChips, Character.valueOf('c'), "cobblestone", Character.valueOf('r'), Item.redstone 
		}));

		GameRegistry.addShapelessRecipe(new ItemStack(Block.ice, 4), new Object[] { 
			Item.bucketWater, itemFrezaCrystal 
		});

		GameRegistry.addRecipe(new ItemStack(FrezariteHelmet, 1), new Object[] { 
			"ccc", "c c", Character.valueOf('c'), itemFrezaCrystal 
		});

		GameRegistry.addRecipe(new ItemStack(FrezariteBody, 1), new Object[] { 
			"c c", "ccc", "ccc", Character.valueOf('c'), itemFrezaCrystal 
		});

		GameRegistry.addRecipe(new ItemStack(FrezariteLegs, 1), new Object[] { 
			"ccc", "c c", "c c", Character.valueOf('c'), itemFrezaCrystal 
		});

		GameRegistry.addRecipe(new ItemStack(FrezariteBoots, 1), new Object[] { 
			"c c", "c c", Character.valueOf('c'), itemFrezaCrystal 
		});

		GameRegistry.addRecipe(new ItemStack(KreknoriteHelmet, 1), new Object[] { 
			"ccc", "c c", Character.valueOf('c'), itemKreknoChip 
		});

		GameRegistry.addRecipe(new ItemStack(KreknoriteBody, 1), new Object[] { 
			"c c", "ccc", "ccc", Character.valueOf('c'), itemKreknoChip 
		});

		GameRegistry.addRecipe(new ItemStack(KreknoriteLegs, 1), new Object[] { 
			"ccc", "c c", "c c", Character.valueOf('c'), itemKreknoChip 
		});

		GameRegistry.addRecipe(new ItemStack(KreknoriteBoots, 1), new Object[] { 
			"c c", "c c", Character.valueOf('c'), itemKreknoChip 
		});

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(KreknoriteSword, 1), new Object[] { 
			"c", "c", "s", Character.valueOf('c'), itemKreknoChip, Character.valueOf('s'), "stickWood"
		}));

		GameRegistry.addShapelessRecipe(new ItemStack(itemVanillaIceCream, 4), new Object[] { 
			Item.bowlEmpty, Item.sugar, Item.bucketMilk, itemFrezaCrystal 
		});

		GameRegistry.addShapelessRecipe(new ItemStack(itemChocolateIceCream, 4), new Object[] { 
			Item.bowlEmpty, Item.sugar, Item.bucketMilk, itemFrezaCrystal, new ItemStack(Item.dyePowder, 1, 3) 
		});

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(FrezaritePickaxe, 1), new Object[] { 
			"ccc", " s ", " s ", Character.valueOf('c'), itemFrezaCrystal, Character.valueOf('s'), "stickWood"
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(FrezariteSpade, 1), new Object[] { 
			"c", "s", "s", Character.valueOf('c'), itemFrezaCrystal, Character.valueOf('s'), "stickWood" 
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(FrezariteAxe, 1), new Object[] { 
			" cc", " sc", " s ", Character.valueOf('c'), itemFrezaCrystal, Character.valueOf('s'), "stickWood"
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(FrezariteAxe, 1), new Object[] { 
			"cc ", "cs ", " s ", Character.valueOf('c'), itemFrezaCrystal, Character.valueOf('s'), "stickWood" 
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(FrezariteHoe, 1), new Object[] { 
			"cc ", " s ", " s ", Character.valueOf('c'), itemFrezaCrystal, Character.valueOf('s'), "stickWood" 
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(FrezariteHoe, 1), new Object[] { 
			" cc", " s ", " s ", Character.valueOf('c'), itemFrezaCrystal, Character.valueOf('s'), "stickWood"
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(FrezariteSword, 1), new Object[] { 
			"c", "c", "s", Character.valueOf('c'), itemFrezaCrystal, Character.valueOf('s'), "stickWood"
		}));
		
		GameRegistry.addRecipe(new ItemStack(blockMeteorTimer, 1), new Object[] {
			"mfk", "brb", Character.valueOf('m'), itemMeteorChips, Character.valueOf('f'), itemFrezaCrystal, 
			Character.valueOf('k'), itemKreknoChip, Character.valueOf('b'), blockMeteorOre, 
			Character.valueOf('r'), Item.redstone
		});
		
		GameRegistry.addRecipe(new ItemStack(blockMeteorTimer, 1), new Object[] {
			"mfk", "brb", Character.valueOf('m'), itemMeteorChips, Character.valueOf('f'), itemFrezaCrystal, 
			Character.valueOf('k'), itemKreknoChip, Character.valueOf('b'), blockMeteor, 
			Character.valueOf('r'), Item.redstone
		});
		
		GameRegistry.addRecipe(new ItemStack(blockRedMeteorGem, 1), new Object[] {
			"rrr", "rrr", "rrr", Character.valueOf('r'), itemRedMeteorGem
		});
		
		GameRegistry.addShapelessRecipe(new ItemStack(itemRedMeteorGem, 9), new Object[] {
			blockRedMeteorGem
		});

		if (!forModpack) {
			GameRegistry.addRecipe(new ItemStack(itemMeteorProximityDetector, 1), new Object[] { 
				" m ", "mrm", " m ", Character.valueOf('m'), itemMeteorChips, Character.valueOf('r'), Item.redstone
			});

			GameRegistry.addRecipe(new ItemStack(itemMeteorTimeDetector, 1), new Object[] { 
				" f ", "frf", " f ", Character.valueOf('f'), itemFrezaCrystal, Character.valueOf('r'), itemRedMeteorGem 
			});

			GameRegistry.addRecipe(new ItemStack(itemMeteorCrashDetector, 1), new Object[] { 
				" k ", "krk", " k ", Character.valueOf('k'), itemKreknoChip, Character.valueOf('r'), itemRedMeteorGem 
			});

			GameRegistry.addRecipe(new ItemStack(itemMeteorSummoner, 1), new Object[] { 
				"rmr", "mrm", "rmr", Character.valueOf('m'), itemMeteorChips, Character.valueOf('r'), itemRedMeteorGem 
			});

			GameRegistry.addRecipe(new ItemStack(itemMeteorSummoner, 1, 1), new Object[] { 
				"mmm", "msm", "mmm", Character.valueOf('m'), itemMeteorChips, Character.valueOf('s'), itemMeteorSummoner 
			});

			GameRegistry.addRecipe(new ItemStack(itemMeteorSummoner, 1, 2), new Object[] { 
				"mmm", "msm", "mmm", Character.valueOf('m'), itemFrezaCrystal, Character.valueOf('s'), itemMeteorSummoner 
			});

			GameRegistry.addRecipe(new ItemStack(itemMeteorSummoner, 1, 3), new Object[] { 
				"mmm", "msm", "mmm", Character.valueOf('m'), itemKreknoChip, Character.valueOf('s'), itemMeteorSummoner 
			});

			GameRegistry.addRecipe(new ItemStack(itemMeteorSummoner, 1, 4), new Object[] { 
				"rmr", "ksk", "rfr", Character.valueOf('m'), itemMeteorChips, Character.valueOf('s'), itemMeteorSummoner, 
				Character.valueOf('k'), itemKreknoChip, Character.valueOf('f'), itemFrezaCrystal, Character.valueOf('r'),
				itemRedMeteorGem
			});

			GameRegistry.addRecipe(new ItemStack(itemMeteorSummoner, 1, 5), new Object[] { 
				"mfm", "fsf", "mfm", Character.valueOf('m'), itemMeteorChips, Character.valueOf('s'), itemMeteorSummoner, 
				Character.valueOf('f'), Item.fishRaw
			});

			GameRegistry.addRecipe(new ItemStack(itemMeteorSummoner, 1, 5), new Object[] { 
				"mfm", "fsf", "mfm", Character.valueOf('m'), itemMeteorChips, Character.valueOf('s'), itemMeteorSummoner, 
				Character.valueOf('f'), Item.fishCooked 
			});
		}
	}

	@Override
	public void onCrafting(EntityPlayer player, ItemStack item, IInventory craftMatrix)
	{
		if (item.itemID == blockMeteorShield.blockID) {
			player.addStat(HandlerAchievement.shieldCrafted, 1);
		} else if (item.itemID == KreknoriteSword.itemID) {
			player.addStat(HandlerAchievement.craftedKreknoSword, 1);
		} else if (item.itemID == itemMeteorProximityDetector.itemID ||
				   item.itemID == itemMeteorTimeDetector.itemID ||
				   item.itemID == itemMeteorCrashDetector.itemID) {
			player.addStat(HandlerAchievement.craftedDetector, 1);
		} else if (item.itemID == blockMeteorTimer.blockID) {
			player.addStat(HandlerAchievement.craftedMeteorTimer, 1);
		}
			
	}

	@Override
	public void onSmelting(EntityPlayer player, ItemStack item) {}

	@Override
	public int getBurnTime(ItemStack fuel)
	{
		if (fuel.itemID == itemKreknoChip.itemID) {
			return 3300;
		}
		return 0;
	}

	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		if ((chunkGenerator instanceof ChunkProviderGenerate)) {
			int x = chunkX << 4;
			int z = chunkZ << 4;
			for (int i = 0; i < this.chunkChecks; i++) {
				int randX = x + rand.nextInt(16);
				int randY = rand.nextInt(16) + 6;
				int randZ = z + rand.nextInt(16);
				(new WorldGenMinable(blockMeteorOre.blockID, this.oreGenSize)).generate(world, rand, randX, randY, randZ);
			}
		}
	}

	public static void whatSide(Side side, String s)
	{
		if (!loggable) return;
		if (side == Side.SERVER)
			log.info(s + " called on server side");
		else if (side == Side.CLIENT)
			log.info(s + " called on client side");
	}
}