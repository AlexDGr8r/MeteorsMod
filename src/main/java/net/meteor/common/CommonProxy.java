package net.meteor.common;

import java.util.HashMap;

import net.meteor.common.climate.HandlerMeteor;
import net.meteor.common.tileentity.TileEntityMeteorShield;
import net.meteor.common.tileentity.TileEntityMeteorTimer;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy
{
	public HashMap<Integer, HandlerMeteor> metHandlers = new HashMap<Integer, HandlerMeteor>();

	public void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityMeteorShield.class, "TileEntityMeteorShield");
		GameRegistry.registerTileEntity(TileEntityMeteorTimer.class, "TileEntityMeteorTimer");
	}

	public void loadStuff() {}

}