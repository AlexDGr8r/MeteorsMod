package net.meteor.common;

import java.util.HashMap;

import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy
{
	public HandlerMeteor meteorHandler = new HandlerMeteor();
	public HashMap<String, EnumMeteor> lastMeteorPrevented = new HashMap<String, EnumMeteor>();
	public int nearestTimeLeft = 0;

	public void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityMeteorShield.class, "TileEntityMeteorShield");
		GameRegistry.registerTileEntity(TileEntityMeteorTimer.class, "TileEntityMeteorTimer");
	}

	public void loadStuff()
	{
	}

	public void loadSounds()
	{
	}

	public void playCrashSound(World world, EntityMeteor meteor)
	{
	}

	public void meteorProtectCheck(String owner)
	{
	}

	public void updateMeteorBlockAch(World world)
	{
	}

	public void sendPortalCreationMessage()
	{
	}
}