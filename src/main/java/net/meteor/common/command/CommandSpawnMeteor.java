package net.meteor.common.command;

import net.meteor.common.EnumMeteor;
import net.meteor.common.MeteorsMod;
import net.meteor.common.entity.EntityMeteor;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class CommandSpawnMeteor extends CommandBase {

	@Override
	public String getCommandName() {
		return "meteor";
	}

	@Override
	public String getCommandUsage(ICommandSender var1) {
		return "/meteor <x> <y> <z> <size> [delay] [type] [summoned]";
	}
	
	public int getRequiredPermissionLevel() {
        return 2;
    }

	@Override
	public void processCommand(ICommandSender var1, String[] var2) {
		try {
			if (var2.length < 4) {
				var1.addChatMessage(new ChatComponentText("Usage: " + getCommandUsage(var1)));
				return;
			}
			
			World world = var1.getEntityWorld();
			int type = world.rand.nextInt(EnumMeteor.values().length);
			if (var2.length >= 6) {
				type = parseIntBounded(var1, var2[5], 0, EnumMeteor.values().length - 1);
			}
			int delay = 0;
			if (var2.length >= 5) {
				delay = Integer.parseInt(var2[4]);
			}
			boolean summoned = false;
			if (var2.length >= 7) {
				summoned = Boolean.parseBoolean(var2[6]);
			}
			double x = var1.getPlayerCoordinates().posX;
			double y = var1.getPlayerCoordinates().posY;
			double z = var1.getPlayerCoordinates().posZ;
	        x = func_110666_a(var1, (double)x, var2[0]);
	        y = func_110666_a(var1, (double)y, var2[1]);
	        z = func_110666_a(var1, (double)z, var2[2]);
			EntityMeteor meteor = new EntityMeteor(world, parseIntBounded(var1, var2[3], 1, 3), x, z, EnumMeteor.getTypeFromID(type), summoned);
			meteor.posY = y;
			meteor.prevPosY = meteor.posY;
			meteor.spawnPauseTicks = delay;
			world.spawnEntityInWorld(meteor);
			if (!MeteorsMod.instance.isDimensionWhitelisted(world.provider.dimensionId)) {
				var1.addChatMessage(new ChatComponentText("The Meteor isn't techincally allowed in this dimension, but I'll spawn it for you anyway."));
			}
			var1.addChatMessage(new ChatComponentText("Meteor spawned."));
		} catch (Exception e) {
			var1.addChatMessage(new ChatComponentText(e.getMessage()));
			var1.addChatMessage(new ChatComponentText("Usage: " + getCommandUsage(var1)));
		}
	}

}
