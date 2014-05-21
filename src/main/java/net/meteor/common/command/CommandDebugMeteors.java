package net.meteor.common.command;

import java.util.Iterator;

import net.meteor.common.MeteorsMod;
import net.meteor.common.climate.GhostMeteor;
import net.meteor.common.climate.HandlerMeteor;
import net.meteor.common.climate.MeteorForecast;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class CommandDebugMeteors extends CommandBase {

	@Override
	public String getCommandName() {
		return "listMeteors";
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public String getCommandUsage(ICommandSender var1) {
		return "/listMeteors";
	}

	@Override
	public void processCommand(ICommandSender var1, String[] var2) {
		World world = var1.getEntityWorld();
		HandlerMeteor metH = MeteorsMod.proxy.metHandlers.get(world.provider.dimensionId);
		if (metH != null) {
			
			MeteorForecast forecast = metH.getForecast();
			int secs = forecast.getSecondsUntilNewMeteor();
			var1.addChatMessage(new ChatComponentText(secs + " Seconds Until Possible New Meteor"));
			
			var1.addChatMessage(new ChatComponentText(metH.ghostMets.size() + " Meteor(s) in Orbit"));
			Iterator<GhostMeteor> iter = metH.ghostMets.iterator();
			while (iter.hasNext()) {
				GhostMeteor met = iter.next();
				var1.addChatMessage(new ChatComponentText("T:" + met.type.toString() + " S:" + met.size + " X:" + met.x + " Z:" + met.z + " RT:" + met.getRemainingTicks()));
			}
		}
	}

}
