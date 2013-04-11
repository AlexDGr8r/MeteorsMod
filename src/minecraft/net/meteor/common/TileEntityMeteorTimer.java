package net.meteor.common;

import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;

public class TileEntityMeteorTimer extends TileEntity {
	
	private int lastMeta;
	
	public boolean quickMode;
	
	public TileEntityMeteorTimer() {
		this.lastMeta = 0;
		this.quickMode = false;
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
		int calc = (ticksLeft / 800);
		if (calc > 15) calc = 15;
		int meta = 15 - calc;
		meta = MathHelper.clamp_int(meta, 0, 15);
		
		if (quickMode) {
			if (meta == 15) {
				updateMeta(meta);
			} else {
				updateMeta(0);
			}
		} else {
			updateMeta(meta);
		}
	}
	
	private void updateMeta(int meta) {
		if (lastMeta != meta) {
			this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, meta, 0x02);
			this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, MeteorsMod.blockMeteorTimer.blockID);
			lastMeta = meta;
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setBoolean("mode", quickMode);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.quickMode = nbt.getBoolean("mode");
	}

}
