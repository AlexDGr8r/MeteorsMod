package net.meteor.common;

import java.util.HashMap;

import net.meteor.common.climate.HandlerMeteor;
import net.meteor.common.tileentity.TileEntityFreezingMachine;
import net.meteor.common.tileentity.TileEntityMeteorShield;
import net.meteor.common.tileentity.TileEntityMeteorTimer;
import net.meteor.common.tileentity.TileEntitySlippery;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy
{
	public HashMap<Integer, HandlerMeteor> metHandlers = new HashMap<Integer, HandlerMeteor>();

	public void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityMeteorShield.class, "TileEntityMeteorShield");
		GameRegistry.registerTileEntity(TileEntityMeteorTimer.class, "TileEntityMeteorTimer");
		GameRegistry.registerTileEntity(TileEntityFreezingMachine.class, "TileEntityIceMaker");
		GameRegistry.registerTileEntity(TileEntitySlippery.class, "TileEntitySlippery");
	}

	public void loadStuff() {}
	
	public void preInit() {}

}