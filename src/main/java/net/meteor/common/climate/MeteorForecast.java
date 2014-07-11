package net.meteor.common.climate;

import java.util.ArrayList;

import net.meteor.common.EnumMeteor;
import net.meteor.common.MeteorsMod;
import net.meteor.common.packets.PacketSoonestMeteor;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class MeteorForecast {
	
	private World theWorld;
	private ClimateUpdater worldTick;
	private ArrayList<GhostMeteor> ghostMets;
	private CrashLocation lastCrash = null;
	
	public MeteorForecast(ClimateUpdater wTick, ArrayList<GhostMeteor> gMets, CrashLocation lCrash, World world) {
		this.worldTick = wTick;
		this.ghostMets = gMets;
		this.lastCrash = lCrash;
		this.theWorld = world;
	}
	
	public void setLastCrashLocation(CrashLocation loc) {
		this.lastCrash = loc;
	}
	
	public CrashLocation getLastCrashLocation() {
		return this.lastCrash;
	}
	
	public int getSecondsUntilNewMeteor() {
		return worldTick.getSecondsUntilNewMeteor();
	}
	
	public GhostMeteor getNearestTimeMeteor() {
		if (this.theWorld == null) return null;
		GhostMeteor closestMeteor = null;
		for (int i = 0; i < this.ghostMets.size(); i++) {
			if (closestMeteor != null) {
				if (this.ghostMets.get(i).type != EnumMeteor.KITTY) {
					int var1 = closestMeteor.getRemainingTicks();
					int var2 = this.ghostMets.get(i).getRemainingTicks();
					if (var2 < var1)
						closestMeteor = this.ghostMets.get(i);
				}
			} else if (this.ghostMets.get(i).type != EnumMeteor.KITTY) {
				closestMeteor = this.ghostMets.get(i);
			}

		}

		return closestMeteor;
	}
	
	public void updateNearestTimeForClients() {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			GhostMeteor gMeteor = getNearestTimeMeteor();
			MeteorsMod.packetPipeline.sendToDimension(new PacketSoonestMeteor(gMeteor), theWorld.provider.dimensionId);
		}
	}
	
	public void clearMeteors() {
		ghostMets.clear();
	}

}
