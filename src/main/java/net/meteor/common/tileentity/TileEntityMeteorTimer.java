package net.meteor.common.tileentity;

import net.meteor.common.MeteorsMod;
import net.meteor.common.climate.GhostMeteor;
import net.meteor.common.climate.HandlerMeteor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;

public class TileEntityMeteorTimer extends TileEntity {

	private int lastMeta;
	private HandlerMeteor metHandler;

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

		if (this.worldObj != null && !this.worldObj.isRemote && worldObj.getTotalWorldTime() % 20L == 0L) {

			if (metHandler == null) {
				this.metHandler = MeteorsMod.proxy.metHandlers.get(worldObj.provider.dimensionId);
			}
			GhostMeteor gMet = this.metHandler.getForecast().getNearestTimeMeteor();
			int ticksLeft;
			if (gMet != null) {
				ticksLeft = gMet.getRemainingTicks();
			} else {
				updateMeta(0);
				return;
			}
			int calc;
			if (ticksLeft < 450 && MeteorsMod.instance.meteorsFallOnlyAtNight) {
				long time = worldObj.getWorldTime() % 24000L;
				int timeTicks = ticksLeft;
				if (time < 12000L) {
					timeTicks += (int)((12000L - time) / 20);
				} else if ((24000L - time) / 20 < ticksLeft) {
					timeTicks += 600;
				}
				calc = (timeTicks / 30);
			} else {
				calc = (ticksLeft / 30);
			}
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

	}

	private void updateMeta(int meta) {
		if (lastMeta != meta) {
			this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, meta, 3);
			//this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, MeteorsMod.blockMeteorTimer.blockID);
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

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
	{
		readFromNBT(pkt.func_148857_g());
	}

	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound var1 = new NBTTagCompound();
		writeToNBT(var1);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, var1);
	}

}
