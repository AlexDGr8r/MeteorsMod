package net.meteor.common;

import java.util.Random;
import java.util.logging.Logger;

import net.meteor.common.climate.HandlerMeteor;
import net.meteor.common.climate.HandlerWorld;
import net.meteor.common.command.CommandDebugMeteors;
import net.meteor.common.command.CommandDebugShields;
import net.meteor.common.command.CommandKittyAttack;
import net.meteor.common.enchantment.EnchantmentColdTouch;
import net.meteor.common.enchantment.EnchantmentMagnetized;
import net.meteor.common.entity.EntityAlienCreeper;
import net.meteor.common.entity.EntityCometKitty;
import net.meteor.common.entity.EntityMeteor;
import net.meteor.common.entity.EntitySummoner;
import net.meteor.common.packets.PacketPipeline;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderGenerate;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid=MeteorsMod.MOD_ID, name=MeteorsMod.MOD_NAME, version=MeteorsMod.VERSION)
public class MeteorsMod
implements IWorldGenerator
{
	
	public static final String MOD_ID 	= "meteors";
	public static final String MOD_NAME = "Falling Meteors";
	public static final String VERSION 	= "2.12"; 		// Switch to automatic versioning later on
	
	public static final boolean loggable = true;		// For Debugging Purposes Only

	public static final Logger log = Logger.getLogger("Falling Meteors Mod");

	public static final PacketPipeline packetPipeline = new PacketPipeline();
	
	public static Enchantment Magnetization;
	public static Enchantment ColdTouch;
	
	public static final CreativeTabs meteorTab = new CreativeTabMeteor("Falling Meteors Mod");

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
	public boolean meteorShieldSound;
	private int chunkChecks;
	private int oreGenSize;
	public int MinMeteorSizeForPortal;
	public double ImpactExplosionMultiplier;
	public int ImpactSpread;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		ModConfig.instance.load(event.getSuggestedConfigurationFile());
		loadStaticConfigurationValues();
		
		MeteorBlocks.registerBlocks();
		MeteorItems.registerItems();
		MeteorItems.readyItems();
		
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
		
		this.achHandler = new HandlerAchievement();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		registerEntities();
		HandlerRecipe recipeHandler = new HandlerRecipe();
		recipeHandler.addRecipes();

		this.playerTickHandler = new HandlerPlayerTick();

		this.achHandler.readyAchievements();
		proxy.loadStuff();

		MinecraftForge.EVENT_BUS.register(new HandlerPlayerBreakSpeed());
		MinecraftForge.EVENT_BUS.register(new HandlerWorld());

		FMLCommonHandler.instance().bus().register(recipeHandler);
		FMLCommonHandler.instance().bus().register(achHandler);
		GameRegistry.registerFuelHandler(recipeHandler);
		GameRegistry.registerWorldGenerator(this, 1);
		FMLCommonHandler.instance().bus().register(new HandlerPlayerTick());
		ClientHandler cHandler = new ClientHandler(packetPipeline);
		packetPipeline.initalise();
		cHandler.registerPackets();
		MinecraftForge.EVENT_BUS.register(cHandler);
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new HandlerGui());
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		packetPipeline.postInitialise();
	}

	private void loadStaticConfigurationValues() {
		ModConfig config = ModConfig.instance;
		// Enchantments 
		Magnetization = new EnchantmentMagnetized(config.get("Magnetization Enchantment ID", 157), 3).setName("Magnetization");
		ColdTouch 	  = new EnchantmentColdTouch(config.get("Cold Touch Enchantment ID", 158), 3).setName("Cold Touch");
		// General Configuration
		meteorFallDistance	= config.get("Meteor Fall Radius", 350);
		kittyAttackChance	= config.get("Kitty Attack Chance", 1);
		textNotifyCrash		= config.get("Text Crash Notification", false);
		meteoriteEnabled	= config.get("Meteorite Meteor Enabled", true);
		frezariteEnabled	= config.get("Frezarite Meteor Enabled", true);
		kreknoriteEnabled	= config.get("Kreknorite Meteor Enabled", true);
		unknownEnabled		= config.get("Unknown Meteor Enabled", true);
		chunkChecks 		= config.get("Chunk Generation Checks", 4);
		oreGenSize  		= config.get("Meteor Ore Gen Size", 6);
		meteorShieldSound 	= config.get("Meteor Shield Humming Noise Enabled", true);
		int configTicks 	= config.get("Meteor Fall Deterrence", 25) * 100;
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
		this.ShieldRadiusMultiplier = ModConfig.instance.get("Shield Radius in Blocks", 64);
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
	public void serverStarting(FMLServerStartingEvent evt) {
		evt.registerServerCommand(new CommandKittyAttack());
		evt.registerServerCommand(new CommandDebugShields());
		evt.registerServerCommand(new CommandDebugMeteors());
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
				(new WorldGenMinable(MeteorBlocks.blockMeteorOre, this.oreGenSize)).generate(world, rand, randX, randY, randZ);
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