package net.meteor.common.tileentity;

import net.meteor.common.MeteorsMod;
import net.meteor.common.packets.PacketButtonPress;
import net.minecraft.tileentity.TileEntity;

public abstract class TileEntityNetworkBase extends TileEntity {

	public void pressButton(int id) {
		if (worldObj.isRemote) {
			MeteorsMod.network.sendToServer(new PacketButtonPress(this, id));
		}
	}
	
	public abstract void onButtonPress(int id);

	public void postButtonPress(int buttonID) {
		this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		this.markDirty();
	}
	
}
