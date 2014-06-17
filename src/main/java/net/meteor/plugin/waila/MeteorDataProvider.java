package net.meteor.plugin.waila;

import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.SpecialChars;
import net.meteor.common.block.BlockFrezarite;
import net.meteor.common.block.BlockMeteor;
import net.minecraft.item.ItemStack;

public class MeteorDataProvider implements IWailaDataProvider {

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
		
		if (config.getConfig("meteors.meteorTemp", true)) {
			
			String tip = "Temperature: ";
			
			if (accessor.getBlock() instanceof BlockMeteor) {
				if (accessor.getMetadata() != 0) {
					tip += SpecialChars.RED + "Burning Hot";
				} else {
					tip += SpecialChars.WHITE + "Cooled";
				}
			} else if (accessor.getBlock() instanceof BlockFrezarite) {
				tip += SpecialChars.AQUA + "Freezing Cold";
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
