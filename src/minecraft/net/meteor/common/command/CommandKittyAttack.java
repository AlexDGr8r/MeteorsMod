package net.meteor.common.command;

import net.meteor.common.MeteorsMod;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class CommandKittyAttack extends CommandBase
{
	@Override
	public String getCommandName()
	{
		return "kittyattack";
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
			MeteorsMod.proxy.metHandlers.get(world.provider.dimensionId).kittyAttack();
		}
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "/kittyattack";
	}

}