package net.meteor.plugin.waila;

import java.util.List;

import net.meteor.common.tileentity.TileEntityMeteorShield;
import net.minecraft.item.ItemStack;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;

public class ShieldDataProvider implements IWailaDataProvider {

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
		
		if (accessor.getTileEntity() instanceof TileEntityMeteorShield && config.getConfig("meteors.shieldData", true)) {
			
			TileEntityMeteorShield shield = (TileEntityMeteorShield)accessor.getTileEntity();
			List<String> info = shield.getDisplayInfo();
			
			currenttip.add(info.get(0));
			currenttip.add(info.get(1));
			if (info.size() > 3) {
				currenttip.add(info.get(3));
				if (info.size() > 4) {
					currenttip.add(info.get(4) + " " + info.get(5));
				}
			}
			
		}
		
		return currenttip;
	}
	
	@Override
	public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		return currenttip;
	}
	
}
