package net.meteor.common;

import java.util.HashMap;

import net.meteor.common.entity.EntityMeteor;
import net.meteor.common.tileentity.TileEntityMeteorShield;
import net.meteor.common.tileentity.TileEntityMeteorTimer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy
{
	public HashMap<Integer, HandlerMeteor> metHandlers = new HashMap<Integer, HandlerMeteor>();
	public HashMap<String, EnumMeteor> lastMeteorPrevented = new HashMap<String, EnumMeteor>();

	public void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityMeteorShield.class, "TileEntityMeteorShield");
		GameRegistry.registerTileEntity(TileEntityMeteorTimer.class, "TileEntityMeteorTimer");
	}

	public void loadStuff() {}

	public void loadSounds() {}

}