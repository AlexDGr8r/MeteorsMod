package net.meteor.common.command;

import java.util.Iterator;

import net.meteor.common.IMeteorShield;
import net.meteor.common.MeteorsMod;
import net.meteor.common.climate.HandlerMeteor;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class CommandDebugShields extends CommandBase {
	
	@Override
	public String getCommandName()
	{
		return "listShields";
	}

	@Override
	public int getRequiredPermissionLevel()
	{
		return 2;
	}

	@Override
	public void processCommand(ICommandSender var1, String[] var2)
	{
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			World world = var1.getEntityWorld();
			HandlerMeteor metH = MeteorsMod.proxy.metHandlers.get(world.provider.dimensionId);
			if (metH != null) {
				Iterator<IMeteorShield> iter = metH.getShieldManager().meteorShields.iterator();
				while (iter.hasNext()) {
					IMeteorShield shield = iter.next();
					var1.addChatMessage(new ChatComponentText("x:" + shield.getX() + " y:" + shield.getY() + " z:" + shield.getZ() + " r:" + shield.getRange() + " o:" + shield.getOwner()));
				}
			}
			
		}
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "/listShields";
	}

}
