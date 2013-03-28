package net.meteor.common;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;

public class TileEntityMeteorTimer extends TileEntity {
	
	private int lastMeta;
	
	public TileEntityMeteorTimer() {
		this.lastMeta = 0;
	}
	
	@Override
	public void updateEntity() {
		
		// Do calculations for closeness of meteor falling to a metadata value of 0 to 15
		// Then check if that metadata value is different then when it was updated last
		// If so, mark block for update so redstone output gets changed
		
		// Values roughly:
		// 15 = 1200 ticks left
		// 14 = 2400 ticks left
		// 13 = 3600 ticks left
		// 12 = 4800 ticks left
		// 11 = 6000 ticks left
		// 10 = 7200 ticks left ...etc
		// y  = 15 - ((ticksLeft / 1200) > 15 ? 15 : (ticksLeft / 1200))
		
		int ticksLeft = MeteorsMod.proxy.nearestTimeLeft;
		int calc = (ticksLeft / 1200);
		if (calc > 15) calc = 15;
		int meta = 15 - calc;
		meta = MathHelper.clamp_int(meta, 0, 15);
		
		if (lastMeta != meta) {
			this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, meta, 0x02);
			// Perhaps not neccessary below with flag passed in setMeta above
			// this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
		}
		
	}

}
