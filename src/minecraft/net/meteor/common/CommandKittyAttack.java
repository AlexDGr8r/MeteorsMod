package net.meteor.common;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class CommandKittyAttack extends CommandBase
{
  public String getCommandName()
  {
    return "kittyattack";
  }

  public int getRequiredPermissionLevel()
  {
    return 2;
  }

  public void processCommand(ICommandSender var1, String[] var2)
  {
    if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
      MeteorsMod.proxy.meteorHandler.kittyAttack();
  }

}