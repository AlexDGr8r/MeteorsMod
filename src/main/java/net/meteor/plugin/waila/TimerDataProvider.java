package net.meteor.plugin.waila;

import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.meteor.common.tileentity.TileEntityMeteorTimer;
import net.minecraft.item.ItemStack;

public class TimerDataProvider implements IWailaDataProvider {

	@Override
	public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
		return null;
	}
	
	@Override
	public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		return currenttip;
	}
	
	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		
		if (accessor.getTileEntity() instanceof TileEntityMeteorTimer && config.getConfig("meteors.timerMode", true)) {
			
			String tip = "Mode: ";
			TileEntityMeteorTimer timer = (TileEntityMeteorTimer)accessor.getTileEntity();
			if (timer.quickMode) {
				tip += "Quick";
			} else {
				tip += "Power";
			}
			
			currenttip.add(tip);
		}
		
		return currenttip;
	}
	
	@Override
	public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		return currenttip;
	}

}
