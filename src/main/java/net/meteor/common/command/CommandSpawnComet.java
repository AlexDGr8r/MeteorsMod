package net.meteor.common.command;

import java.util.List;

import net.meteor.common.EnumMeteor;
import net.meteor.common.IMeteorShield;
import net.meteor.common.MeteorsMod;
import net.meteor.common.climate.HandlerMeteor;
import net.meteor.common.entity.EntityComet;
import net.meteor.common.tileentity.TileEntityMeteorShield;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class CommandSpawnComet extends CommandBase {

	@Override
	public String getCommandName() {
		return "comet";
	}

	@Override
	public String getCommandUsage(ICommandSender var1) {
		return "/comet <x> <y> <z> [type] [delay]";
	}

	@Override
	public void processCommand(ICommandSender var1, String[] var2) {
		try {
			if (var2.length < 3) {
				var1.addChatMessage(new ChatComponentText("Usage: " + getCommandUsage(var1)));
				return;
			}
			
			World world = var1.getEntityWorld();
			int type = world.rand.nextInt(EnumMeteor.values().length);
			if (var2.length >= 4) {
				type = parseIntBounded(var1, var2[3], 0, EnumMeteor.values().length - 1);
			}
			int delay = 0;
			if (var2.length >= 5) {
				delay = Integer.parseInt(var2[4]);
			}
			double x = var1.getPlayerCoordinates().posX;
			double y = var1.getPlayerCoordinates().posY;
			double z = var1.getPlayerCoordinates().posZ;
	        x = func_110666_a(var1, (double)x, var2[0]);
	        y = func_110666_a(var1, (double)y, var2[1]);
	        z = func_110666_a(var1, (double)z, var2[2]);
			EntityComet comet = new EntityComet(world, x, z, EnumMeteor.getTypeFromID(type));
			comet.setPosition(x, y, z);
			comet.prevPosY = comet.posY;
			comet.spawnPauseTicks = delay;
			
			HandlerMeteor meteorHandler = MeteorsMod.proxy.metHandlers.get(world.provider.dimensionId);
			List<IMeteorShield> shields = meteorHandler.getShieldManager().getShieldsInRange((int)x, (int)z);
			boolean blocked = false;
			
			for (int i = 0; i < shields.size(); i++) {
				IMeteorShield ims = shields.get(i);
				if (ims.getPreventComets()) {
					blocked = true;
					break;
				}
			}
			
			if (!blocked) {
				for (int i = 0; i < shields.size(); i++) {
					IMeteorShield ims = shields.get(i);
					TileEntityMeteorShield shield = (TileEntityMeteorShield)world.getTileEntity(ims.getX(), ims.getY(), ims.getZ());
					if (shield != null) {
						shield.detectComet(comet);
					}
				}
				
				world.spawnEntityInWorld(comet);
				var1.addChatMessage(new ChatComponentText("Comet spawned."));
			} else {
				var1.addChatMessage(new ChatComponentText("Comet was blocked by a Meteor Shield."));
			}
			
		} catch (Exception e) {
			var1.addChatMessage(new ChatComponentText(e.getMessage()));
			var1.addChatMessage(new ChatComponentText("Usage: " + getCommandUsage(var1)));
		}
	}

}
