package net.meteor.common;

import java.util.Random;
import java.util.logging.Logger;

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
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.ICraftingHandler;
import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.Mod;
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

@Mod(modid="Meteors", name="Falling Meteors", version="2.9.2.1")
@NetworkMod(clientSideRequired=true, serverSideRequired=false, channels={"MetChunk", "MetSettings", "MetNewCrash", "MetNewTime", "MetGhostAdd", "MetGhostRem", "MetShield"}, packetHandler=ClientHandler.class)
public class MeteorsMod
implements ICraftingHandler, IFuelHandler, IWorldGenerator
{
	public static boolean loggable = true;
	public static boolean forModpack = false;
	private static Configuration config;
	private static int[] IDs = new int[43];

	public static String textureFile = "/meteor/textures/meteorItems.png";
	public static Logger log = FMLLog.getLogger();

	public static EnumArmorMaterial MeteoriteArmor = EnumHelper.addArmorMaterial("METEORITE", 36, new int[] { 2, 7, 5, 2 }, 15);
	public static EnumArmorMaterial FrezariteArmor = EnumHelper.addArmorMaterial("FREZARITE", 7, new int[] { 2, 5, 3, 1 }, 25);
	public static EnumArmorMaterial KreknoriteArmor = EnumHelper.addArmorMaterial("KREKNORITE", 40, new int[] { 3, 8, 6, 3 }, 10);

	public static EnumToolMaterial MeteoriteTool = EnumHelper.addToolMaterial("METEORITE", 3, 900, 10.0F, 4, 10);
	public static EnumToolMaterial FrezariteTool = EnumHelper.addToolMaterial("FREZARITE", 2, 225, 7.0F, 2, 14);
	public static Enchantment Magnetization;
	public static Enchantment ColdTouch;
	public static Block blockMeteor;
	public static Block blockMeteorOre;
	public static Block blockRareMeteor;
	public static Block blockMeteorShield;
	public static Block blockFrezarite;
	public static Block blockKreknorite;
	public static Block torchMeteorShieldIdle;
	public static Block torchMeteorShieldActive;
	public static Item itemMeteorChips;
	public static Item itemRedMeteorGem;
	public static Item itemMeteorSummoner;
	public static Item itemFrezaCrystal;
	public static Item itemKreknoChip;
	public static Item itemVanillaIceCream;
	public static Item itemChocolateIceCream;
	public static Item itemMeteorProximityDetector;
	public static Item itemMeteorTimeDetector;
	public static Item itemMeteorCrashDetector;
	public static Item MeteoriteHelmet;
	public static Item MeteoriteBody;
	public static Item MeteoriteLegs;
	public static Item MeteoriteBoots;
	public static Item MeteoriteAxe;
	public static Item MeteoriteSpade;
	public static Item MeteoriteSword;
	public static Item MeteoritePickaxe;
	public static Item MeteoriteHoe;
	public static Item FrezariteHelmet;
	public static Item FrezariteBody;
	public static Item FrezariteLegs;
	public static Item FrezariteBoots;
	public static Item FrezaritePickaxe;
	public static Item FrezariteSpade;
	public static Item FrezariteSword;
	public static Item FrezariteAxe;
	public static Item FrezariteHoe;
	public static Item KreknoriteHelmet;
	public static Item KreknoriteBody;
	public static Item KreknoriteLegs;
	public static Item KreknoriteBoots;
	public static Item KreknoriteSword;

	@SidedProxy(clientSide="net.meteor.common.ClientProxy", serverSide="net.meteor.common.CommonProxy")
	public static CommonProxy proxy;

	@Mod.Instance("Meteors")
	public static MeteorsMod instance;
	public HandlerAchievement achHandler;
	public HandlerMeteorTick metTickHandler;
	public HandlerPlayerTick playerTickHandler;
	public int MinTicksUntilMeteorSpawn;
	public int RandTicksUntilMeteorSpawn;
	public int MinTicksUntilMeteorCrashes;
	public int RandTicksUntilMeteorCrashes;
	public boolean meteorsFallOnlyAtNight;
	public boolean allowSummonedMeteorGrief = true;
	public int meteorFallDistance;
	public int MaxMeteorSize;
	public int MinMeteorSize;
	public int ShieldRadiusMultiplier;
	public int kittyAttackChance;
	public boolean textNotifyCrash;
	public boolean meteoriteEnabled;
	public boolean frezariteEnabled;
	public boolean kreknoriteEnabled;
	public boolean unknownEnabled;
	private int chunkChecks;
	private int oreGenSize;

	private void setVars()
	{
		Magnetization 				= new EnchantmentMagnetized(IDs[38], 3).setName("Magnetization");
		ColdTouch 					= new EnchantmentColdTouch(IDs[39], 3).setName("Cold Touch");
		blockMeteor 				= new BlockMeteor(IDs[0], 81).setBlockName("Meteor").setHardness(10F).setResistance(200F).setStepSound(Block.soundStoneFootstep).setLightValue(0.5F).setCreativeTab(CreativeTabs.tabBlock);
		blockMeteorOre 				= new BlockMeteorOre(IDs[1], 81).setBlockName("MeteorOre").setHardness(10F).setResistance(200F).setStepSound(Block.soundStoneFootstep).setCreativeTab(CreativeTabs.tabBlock).setLightValue(0.2F);
		blockRareMeteor 			= new BlockRareFallenMeteor(IDs[2], 81).setBlockName("RareFallenMeteor").setHardness(10F).setResistance(200F).setStepSound(Block.soundStoneFootstep).setLightValue(0.5F);
		blockMeteorShield 			= new BlockMeteorShield(IDs[3], 82).setBlockName("MeteorShield").setHardness(2.5F).setStepSound(Block.soundStoneFootstep).setLightValue(0.5F).setCreativeTab(CreativeTabs.tabBlock);
		blockFrezarite 				= new BlockFrezarite(IDs[4], 96).setBlockName("Frezarite").setHardness(8.5F).setResistance(150F).setStepSound(Block.soundGlassFootstep).setLightValue(0.25F).setCreativeTab(CreativeTabs.tabBlock);
		blockKreknorite 			= new BlockKreknorite(IDs[5], 97).setBlockName("Kreknorite").setHardness(11F).setResistance(350F).setStepSound(Block.soundStoneFootstep).setLightValue(0.7F).setCreativeTab(CreativeTabs.tabBlock);
		torchMeteorShieldIdle 		= new BlockMeteorShieldTorch(IDs[6], 87, false).setHardness(0.0F).setLightValue(0.5F).setStepSound(Block.soundWoodFootstep).setBlockName("ProtectedLandTester");
		torchMeteorShieldActive 	= new BlockMeteorShieldTorch(IDs[7], 86, true).setHardness(0.0F).setLightValue(0.5F).setStepSound(Block.soundWoodFootstep).setBlockName("ProtectedLandTester");
		itemMeteorChips 			= new ItemMeteorsMod(IDs[8]).setIconCoord(0, 0).setMaxStackSize(64).setItemName("MeteorChips").setCreativeTab(CreativeTabs.tabMaterials);
		itemRedMeteorGem 			= new ItemMeteorsMod(IDs[9]).setIconCoord(6, 0).setMaxStackSize(64).setItemName("RedMeteorGem").setCreativeTab(CreativeTabs.tabMaterials);
		itemMeteorSummoner 			= new ItemSummoner(IDs[10]).setIconCoord(10, 1).setItemName("MeteorSummoner").setCreativeTab(CreativeTabs.tabMisc);
		itemFrezaCrystal 			= new ItemMeteorsMod(IDs[11]).setIconCoord(7, 0).setMaxStackSize(64).setItemName("FrezariteCrystal").setCreativeTab(CreativeTabs.tabMaterials);
		itemKreknoChip 				= new ItemMeteorsMod(IDs[12]).setIconCoord(8, 0).setMaxStackSize(64).setItemName("KreknoriteChip").setCreativeTab(CreativeTabs.tabMaterials);
		itemVanillaIceCream 		= new ItemFoodMeteorsMod(IDs[31], 2, false).setIconCoord(9, 0).setMaxStackSize(64).setItemName("VanillaIceCream");
		itemChocolateIceCream 		= new ItemFoodMeteorsMod(IDs[32], 3, false).setIconCoord(10, 0).setMaxStackSize(64).setItemName("ChocolateIceCream");
		itemMeteorProximityDetector = new ItemMeteorsMod(IDs[33]).setIconCoord(13, 0).setItemName("MeteorDetectorProximity").setCreativeTab(CreativeTabs.tabTools);
		itemMeteorTimeDetector 		= new ItemDetector(IDs[34]).setIconCoord(12, 0).setItemName("MeteorDetectorTime").setCreativeTab(CreativeTabs.tabTools);
		itemMeteorCrashDetector 	= new ItemMeteorsMod(IDs[35]).setIconCoord(14, 0).setItemName("MeteorDetectorCrash").setCreativeTab(CreativeTabs.tabTools);
		MeteoriteHelmet 			= new ItemEnchArmor(IDs[13], MeteoriteArmor, 3, 0).setEnch(Magnetization, 1).setIconCoord(0, 1).setItemName("MeteoriteHelmet");
		MeteoriteBody 				= new ItemEnchArmor(IDs[14], MeteoriteArmor, 3, 1).setEnch(Magnetization, 1).setIconCoord(0, 2).setItemName("MeteoriteChest");
		MeteoriteLegs 				= new ItemEnchArmor(IDs[15], MeteoriteArmor, 3, 2).setEnch(Magnetization, 1).setIconCoord(0, 3).setItemName("MeteoriteLegs");
		MeteoriteBoots 				= new ItemEnchArmor(IDs[16], MeteoriteArmor, 3, 3).setEnch(Magnetization, 1).setIconCoord(0, 4).setItemName("MeteoriteBoots");
		MeteoriteAxe 				= new ItemEnchAxe(IDs[25], MeteoriteTool).setEnch(Magnetization, 1).setIconCoord(4, 0).setItemName("MeteoriteAxe");
		MeteoriteSpade 				= new ItemEnchSpade(IDs[26], MeteoriteTool).setEnch(Magnetization, 1).setIconCoord(2, 0).setItemName("MeteoriteSpade");
		MeteoriteSword 				= new ItemEnchSword(IDs[27], MeteoriteTool).setEnch(Magnetization, 1).setIconCoord(3, 0).setItemName("MeteoriteSword");
		MeteoritePickaxe 			= new ItemEnchPickaxe(IDs[28], MeteoriteTool).setEnch(Magnetization, 1).setIconCoord(1, 0).setItemName("MeteoritePickaxe");
		MeteoriteHoe 				= new ItemEnchHoe(IDs[29], MeteoriteTool).setEnch(Magnetization, 1).setIconCoord(5, 0).setItemName("MeteoriteHoe");
		FrezariteHelmet 			= new ItemEnchArmor(IDs[17], FrezariteArmor, 3, 0).setEnch(Enchantment.respiration, 3).setIconCoord(1, 1).setItemName("FrezariteHelmet");
		FrezariteBody 				= new ItemEnchArmor(IDs[18], FrezariteArmor, 3, 1).setEnch(Enchantment.aquaAffinity, 1).setIconCoord(1, 2).setItemName("FrezariteChest");
		FrezariteLegs 				= new ItemEnchArmor(IDs[19], FrezariteArmor, 3, 2).setEnch(ColdTouch, 1).setIconCoord(1, 3).setItemName("FrezariteLegs");
		FrezariteBoots 				= new ItemEnchArmor(IDs[20], FrezariteArmor, 3, 3).setEnch(ColdTouch, 1).setIconCoord(1, 4).setItemName("FrezariteBoots");
		FrezaritePickaxe 			= new ItemFrezaritePickaxe(IDs[36], FrezariteTool).setIconCoord(6, 1).setItemName("FrezaritePickaxe");
		FrezariteSpade 				= new ItemFrezariteSpade(IDs[37], FrezariteTool).setIconCoord(7, 1).setItemName("FrezariteSpade");
		FrezariteSword 				= new ItemFrezariteSword(IDs[40], FrezariteTool).setIconCoord(9, 1).setItemName("FrezariteSword");
		FrezariteAxe 				= new ItemFrezariteAxe(IDs[41], FrezariteTool).setIconCoord(8, 1).setItemName("FrezariteAxe");
		FrezariteHoe 				= new ItemFrezariteHoe(IDs[42], FrezariteTool).setIconCoord(5, 1).setItemName("FrezariteHoe");
		KreknoriteHelmet 			= new ItemEnchArmor(IDs[21], KreknoriteArmor, 3, 0).setEnch(Enchantment.fireProtection, 4).setIconCoord(2, 1).setItemName("KreknoriteHelmet");
		KreknoriteBody 				= new ItemEnchArmor(IDs[22], KreknoriteArmor, 3, 1).setEnch(Enchantment.fireProtection, 4).setIconCoord(2, 2).setItemName("KreknoriteChest");
		KreknoriteLegs 				= new ItemEnchArmor(IDs[23], KreknoriteArmor, 3, 2).setEnch(Enchantment.fireProtection, 4).setIconCoord(2, 3).setItemName("KreknoriteLegs");
		KreknoriteBoots 			= new ItemEnchArmor(IDs[24], KreknoriteArmor, 3, 3).setEnch(Enchantment.fireProtection, 4).setIconCoord(2, 4).setItemName("KreknoriteBoots");
		KreknoriteSword 			= new ItemKreknoSword(IDs[30], MeteoriteTool).setIconCoord(11, 0).setItemName("KreknoriteSword");
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
	}

	@Mod.Init
	public void load(FMLInitializationEvent event)
	{
		regAndNameBlocks();
		registerEntities();
		readyBlocksAndItems();
		setRecipes();

		this.metTickHandler = new HandlerMeteorTick();
		this.playerTickHandler = new HandlerPlayerTick();
		this.achHandler = new HandlerAchievement();

		this.achHandler.readyAchievements();
		proxy.loadStuff();

		MinecraftForge.EVENT_BUS.register(proxy.meteorHandler);
		MinecraftForge.EVENT_BUS.register(this.achHandler);
		MinecraftForge.EVENT_BUS.register(new HandlerPlayerBreakSpeed());

		GameRegistry.registerCraftingHandler(this);
		GameRegistry.registerFuelHandler(this);
		GameRegistry.registerWorldGenerator(this);

		TickRegistry.registerTickHandler(this.metTickHandler, Side.SERVER);
		TickRegistry.registerTickHandler(this.playerTickHandler, Side.SERVER);
		TickRegistry.registerTickHandler(this.playerTickHandler, Side.CLIENT);

		NetworkRegistry.instance().registerConnectionHandler(new ClientHandler());
	}

	private void loadStaticConfigurationValues() {
		config.load();
		// Block IDs
		IDs[0] = Integer.parseInt(config.getBlock("Fallen Meteor ID", 667).value);
		IDs[1] = Integer.parseInt(config.getBlock("Underground Meteor ID", 671).value);
		IDs[2] = Integer.parseInt(config.getBlock("Rare Fallen Meteor ID", 672).value);
		IDs[3] = Integer.parseInt(config.getBlock("Meteor Shield ID", 668).value);
		IDs[6] = Integer.parseInt(config.getBlock("Test Torch Unlit ID", 669).value);
		IDs[7] = Integer.parseInt(config.getBlock("Test Torch Lit ID", 670).value);
		IDs[4] = Integer.parseInt(config.getBlock("Frezarite Block ID", 673).value);
		IDs[5] = Integer.parseInt(config.getBlock("Kreknorite ID", 674).value);
		// Item IDs
		IDs[8] = Integer.parseInt(config.getItem("Meteor Chips ID", 30228).value);
		IDs[9] = Integer.parseInt(config.getItem("Red Meteor Gem ID", 30238).value);
		IDs[13] = Integer.parseInt(config.getItem("Meteor Helmet ID", 30229).value);
		IDs[14] = Integer.parseInt(config.getItem("Meteor Body ID", 30230).value);
		IDs[15] = Integer.parseInt(config.getItem("Meteor Legs ID", 30231).value);
		IDs[16] = Integer.parseInt(config.getItem("Meteor Boots ID", 30232).value);
		IDs[25] = Integer.parseInt(config.getItem("Meteor Axe ID", 30233).value);
		IDs[26] = Integer.parseInt(config.getItem("Meteor Spade ID", 30234).value);
		IDs[27] = Integer.parseInt(config.getItem("Meteor Sword ID", 30235).value);
		IDs[28] = Integer.parseInt(config.getItem("Meteor Pickaxe ID", 30236).value);
		IDs[29] = Integer.parseInt(config.getItem("Meteor Hoe ID", 30237).value);
		IDs[10] = Integer.parseInt(config.getItem("Meteor Summoner ID", 30239).value);
		IDs[17] = Integer.parseInt(config.getItem("Frezarite Helmet ID", 30240).value);
		IDs[18] = Integer.parseInt(config.getItem("Frezarite Body ID", 30241).value);
		IDs[19] = Integer.parseInt(config.getItem("Frezarite Legs ID", 30242).value);
		IDs[20] = Integer.parseInt(config.getItem("Frezarite Boots ID", 30243).value);
		IDs[11] = Integer.parseInt(config.getItem("Frezarite Crystal ID", 30244).value);
		IDs[12] = Integer.parseInt(config.getItem("Kreknorite Chip ID", 30245).value);
		IDs[21] = Integer.parseInt(config.getItem("Kreknorite Helmet ID", 30246).value);
		IDs[22] = Integer.parseInt(config.getItem("Kreknorite Body ID", 30247).value);
		IDs[23] = Integer.parseInt(config.getItem("Kreknorite Legs ID", 30248).value);
		IDs[24] = Integer.parseInt(config.getItem("Kreknorite Boots ID", 30249).value);
		IDs[30] = Integer.parseInt(config.getItem("Kreknorite Sword ID", 30250).value);
		IDs[31] = Integer.parseInt(config.getItem("Vanilla Ice Cream ID", 30251).value);
		IDs[32] = Integer.parseInt(config.getItem("Chocolate Ice Cream ID", 30252).value);
		IDs[33] = Integer.parseInt(config.getItem("Meteor Proximity Detector ID", 30253).value);
		IDs[34] = Integer.parseInt(config.getItem("Meteor Time Detector ID", 30254).value);
		IDs[35] = Integer.parseInt(config.getItem("Meteor Crash Detector ID", 30255).value);
		IDs[36] = Integer.parseInt(config.getItem("Frezarite Pickaxe ID", 30256).value);
		IDs[37] = Integer.parseInt(config.getItem("Frezarite Spade ID", 30257).value);
		IDs[40] = Integer.parseInt(config.getItem("Frezarite Sword ID", 30258).value);
		IDs[41] = Integer.parseInt(config.getItem("Frezarite Axe ID", 30259).value);
		IDs[42] = Integer.parseInt(config.getItem("Frezarite Hoe ID", 30260).value);
		// General Configuration
		IDs[38] = Integer.parseInt(config.get("general", "Magnetization Enchantment ID", 157).value);
		IDs[39] = Integer.parseInt(config.get("general", "Cold Touch Enchantment ID", 158).value);
		this.chunkChecks = Integer.parseInt(config.get("general", "Chunk Generation Checks", 4).value);
		this.oreGenSize = Integer.parseInt(config.get("general", "Meteor Ore Gen Size", 6).value);
		int configTicks = Integer.parseInt(config.get("general", "Meteor Fall Deterrence", 25).value) * 2000;
		int mSpawn = (int)(configTicks * 0.25D);
		int mCrash = (int)(configTicks * 0.75D);
		this.MinTicksUntilMeteorSpawn = ((int)(mSpawn * 0.25D));
		this.RandTicksUntilMeteorSpawn = ((int)(mSpawn * 0.75D));
		this.MinTicksUntilMeteorCrashes = ((int)(mCrash * 0.4D));
		this.RandTicksUntilMeteorCrashes = ((int)(mCrash * 0.6D));
		this.meteorFallDistance = Integer.parseInt(config.get("general", "Meteor Fall Radius", 350).value);
		this.textNotifyCrash = Boolean.parseBoolean(config.get("general", "Text Crash Notification", false).value);
		this.kittyAttackChance = Integer.parseInt(config.get("general", "Kitty Attack Chance", 1).value);
		this.meteoriteEnabled = Boolean.parseBoolean(config.get("general", "Meteorite Meteor Enabled", true).value);
		this.frezariteEnabled = Boolean.parseBoolean(config.get("general", "Frezarite Meteor Enabled", true).value);
		this.kreknoriteEnabled = Boolean.parseBoolean(config.get("general", "Kreknorite Meteor Enabled", true).value);
		this.unknownEnabled = Boolean.parseBoolean(config.get("general", "Unknown Meteor Enabled", true).value);
		config.save();
		setClientStartConfig();
	}

	// Values loaded every new world Load and initially when mod is constructed
	public void setClientStartConfig() {
		config.load();
		this.meteorsFallOnlyAtNight = Boolean.parseBoolean(config.get("general", "Meteors Only Fall at Night", true).value);
		this.allowSummonedMeteorGrief = Boolean.parseBoolean(config.get("general", "Allow Summoned Meteor Grief", true).value);
		this.ShieldRadiusMultiplier = Integer.parseInt(config.get("general", "Shield Radius in Chunks", 4).value);
		this.MinMeteorSize = Integer.parseInt(config.get("general", "Minimum Meteor Size", 1).value);
		this.MaxMeteorSize = Integer.parseInt(config.get("general", "Maximum Meteor Size", 3).value);
		this.MinMeteorSize = MathHelper.clamp_int(this.MinMeteorSize, 1, 3);
		this.MaxMeteorSize = MathHelper.clamp_int(this.MaxMeteorSize, 1, 3);
		if (this.MinMeteorSize > this.MaxMeteorSize)
			this.MinMeteorSize = this.MaxMeteorSize;
		else if (this.MaxMeteorSize < this.MinMeteorSize) {
			this.MaxMeteorSize = this.MinMeteorSize;
		}
		config.save();
	}

	@Mod.PreInit
	public void loadConfigurationValues(FMLPreInitializationEvent event) {
		config = new Configuration(event.getSuggestedConfigurationFile());
		loadStaticConfigurationValues();
		LangLocalization.addLocalization("/meteor/lang/", "en_US");
		setVars();
		proxy.loadSounds();
	}

	@Mod.ServerStarting
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
		EntityRegistry.registerGlobalEntityID(EntityAlienCreeper.class, "AlienCreeper", EntityRegistry.findGlobalUniqueEntityId(), 7864485, 16732697);
		EntityRegistry.registerGlobalEntityID(EntityCometKitty.class, "CometKitty", EntityRegistry.findGlobalUniqueEntityId(), 2239283, 884535);
		EntityRegistry.registerGlobalEntityID(EntitySummoner.class, "MeteorSummoner", EntityRegistry.findGlobalUniqueEntityId());
		EntityRegistry.registerModEntity(EntityMeteor.class, "FallingMeteor", 1, this, 64, 8, true);
		EntityRegistry.registerModEntity(EntitySummoner.class, "MeteorSummoner", 2, this, 64, 10, true);
		EntityRegistry.registerModEntity(EntityAlienCreeper.class, "AlienCreeper", 3, this, 80, 3, true);
		EntityRegistry.registerModEntity(EntityCometKitty.class, "CometKitty", 4, this, 80, 3, true);
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

		GameRegistry.addRecipe(new ItemStack(torchMeteorShieldActive, 4), new Object[] { 
			"m", "s", Character.valueOf('m'), itemMeteorChips, Character.valueOf('s'), Item.stick 
		});

		GameRegistry.addRecipe(new ItemStack(blockMeteorShield, 1), new Object[] { 
			"mmm", "crc", "ccc", Character.valueOf('m'), itemMeteorChips, Character.valueOf('c'), Block.cobblestone, Character.valueOf('r'), Item.redstone 
		});

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

		GameRegistry.addRecipe(new ItemStack(KreknoriteSword, 1), new Object[] { 
			"c", "c", "s", Character.valueOf('c'), itemKreknoChip, Character.valueOf('s'), Item.stick 
		});

		GameRegistry.addShapelessRecipe(new ItemStack(itemVanillaIceCream, 4), new Object[] { 
			Item.bowlEmpty, Item.sugar, Item.bucketMilk, itemFrezaCrystal 
		});

		GameRegistry.addShapelessRecipe(new ItemStack(itemChocolateIceCream, 4), new Object[] { 
			Item.bowlEmpty, Item.sugar, Item.bucketMilk, itemFrezaCrystal, new ItemStack(Item.dyePowder, 1, 3) 
		});

		GameRegistry.addRecipe(new ItemStack(FrezaritePickaxe, 1), new Object[] { 
			"ccc", " s ", " s ", Character.valueOf('c'), itemFrezaCrystal, Character.valueOf('s'), Item.stick 
		});

		GameRegistry.addRecipe(new ItemStack(FrezariteSpade, 1), new Object[] { 
			"c", "s", "s", Character.valueOf('c'), itemFrezaCrystal, Character.valueOf('s'), Item.stick 
		});

		GameRegistry.addRecipe(new ItemStack(FrezariteAxe, 1), new Object[] { 
			" cc", " sc", " s ", Character.valueOf('c'), itemFrezaCrystal, Character.valueOf('s'), Item.stick 
		});

		GameRegistry.addRecipe(new ItemStack(FrezariteAxe, 1), new Object[] { 
			"cc ", "cs ", " s ", Character.valueOf('c'), itemFrezaCrystal, Character.valueOf('s'), Item.stick 
		});

		GameRegistry.addRecipe(new ItemStack(FrezariteHoe, 1), new Object[] { 
			"cc ", " s ", " s ", Character.valueOf('c'), itemFrezaCrystal, Character.valueOf('s'), Item.stick 
		});

		GameRegistry.addRecipe(new ItemStack(FrezariteHoe, 1), new Object[] { 
			" cc", " s ", " s ", Character.valueOf('c'), itemFrezaCrystal, Character.valueOf('s'), Item.stick 
		});

		GameRegistry.addRecipe(new ItemStack(FrezariteSword, 1), new Object[] { 
			"c", "c", "s", Character.valueOf('c'), itemFrezaCrystal, Character.valueOf('s'), Item.stick
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
				" m ", "mrm", " m ", Character.valueOf('m'), itemMeteorChips, Character.valueOf('r'), itemRedMeteorGem 
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
				"mmm", "ksk", "fff", Character.valueOf('m'), itemMeteorChips, Character.valueOf('s'), itemMeteorSummoner, 
				Character.valueOf('k'), itemKreknoChip, Character.valueOf('f'), itemFrezaCrystal 
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

	public void onCrafting(EntityPlayer player, ItemStack item, IInventory craftMatrix)
	{
		if (item.itemID == blockMeteorShield.blockID)
			player.addStat(HandlerAchievement.shieldCrafted, 1);
		else if (item.itemID == KreknoriteSword.itemID)
			player.addStat(HandlerAchievement.craftedKreknoSword, 1);
	}

	public void onSmelting(EntityPlayer player, ItemStack item) {}

	public int getBurnTime(ItemStack fuel)
	{
		if (fuel.itemID == itemKreknoChip.itemID) {
			return 3300;
		}
		return 0;
	}

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

	public static boolean isMeteoriteTool(int i) {
		return (i == MeteoriteAxe.itemID) || (i == MeteoriteSpade.itemID) || (i == MeteoriteSword.itemID) || (i == MeteoritePickaxe.itemID)
				|| (i == MeteoriteHoe.itemID);
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