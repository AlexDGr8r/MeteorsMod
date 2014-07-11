package net.meteor.common;

import java.util.ArrayList;

import net.meteor.common.climate.CrashLocation;
import net.meteor.common.climate.HandlerMeteor;
import net.meteor.common.packets.PacketBlockedMeteor;
import net.meteor.common.packets.PacketGhostMeteor;
import net.meteor.common.packets.PacketLastCrash;
import net.meteor.common.packets.PacketPipeline;
import net.meteor.common.packets.PacketSettings;
import net.meteor.common.packets.PacketSoonestMeteor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.relauncher.Side;

public class ClientHandler
{
	public static CrashLocation lastCrashLocation = null;
	public static ChunkCoordinates nearestTimeLocation = null;
	public static ArrayList<ChunkCoordinates> ghostMetLocs = new ArrayList<ChunkCoordinates>(); // TODO Privatize
	
	private PacketPipeline packetPipeline;
	
	public ClientHandler(PacketPipeline pipeline) {
		this.packetPipeline = pipeline;
	}
	
	public void registerPackets() {
		this.packetPipeline.registerPacket(PacketGhostMeteor.class);
		this.packetPipeline.registerPacket(PacketLastCrash.class);
		this.packetPipeline.registerPacket(PacketSettings.class);
		this.packetPipeline.registerPacket(PacketSoonestMeteor.class);
		this.packetPipeline.registerPacket(PacketBlockedMeteor.class);
	}

	public static ChunkCoordinates getClosestIncomingMeteor(double pX, double pZ) {
		ChunkCoordinates coords = null;
		double y = 50.0D;
		for (int i = 0; i < ghostMetLocs.size(); i++) {
			if (coords != null) {
				ChunkCoordinates loc = ghostMetLocs.get(i);
				double var1 = getDistance(pX, y, pZ, loc.posX, y, loc.posZ);
				double var2 = getDistance(pX, y, pZ, coords.posX, y, coords.posZ);
				if (var1 < var2)
					coords = loc;
			}
			else {
				coords = ghostMetLocs.get(i);
			}
		}
		return coords;
	}

	private static double getDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
		double var7 = x1 - x2;
		double var9 = y1 - y2;
		double var11 = z1 - z2;
		return MathHelper.sqrt_double(var7 * var7 + var9 * var9 + var11 * var11);
	}
	
	public static IChatComponent createMessage(String s, EnumChatFormatting ecf) {
		return new ChatComponentText(s).setChatStyle(new ChatStyle().setColor(ecf));
	}

	@SubscribeEvent
	public void playerLoggedIn(PlayerLoggedInEvent event)
	{
		EntityPlayerMP player = (EntityPlayerMP) event.player;
		packetPipeline.sendTo(new PacketSettings(), player);
	}
	
	@SubscribeEvent
	public void entityJoinWorld(EntityJoinWorldEvent event) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			if (event.entity instanceof EntityPlayer) {
				EntityPlayerMP player = (EntityPlayerMP) event.entity;
				HandlerMeteor metHandler = MeteorsMod.proxy.metHandlers.get(event.world.provider.dimensionId);
				packetPipeline.sendTo(new PacketGhostMeteor(), player);		// Clear Ghost Meteors
				metHandler.sendGhostMeteorPackets(player);
				if (metHandler.getForecast() == null) {
					MeteorsMod.log.info("FORECAST WAS NULL");
				}
				packetPipeline.sendTo(new PacketLastCrash(metHandler.getForecast().getLastCrashLocation()), player);
				packetPipeline.sendTo(new PacketSoonestMeteor(metHandler.getForecast().getNearestTimeMeteor()), player);
			}
		}
	}

}