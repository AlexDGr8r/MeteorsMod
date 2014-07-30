package net.meteor.common.packets;

import io.netty.buffer.ByteBuf;
import net.meteor.common.tileentity.TileEntityNetworkBase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketButtonPress implements IMessage {
	
	private TileEntityNetworkBase tileEntity;
	private int buttonID;
	
	public PacketButtonPress() {}
	
	public PacketButtonPress(TileEntityNetworkBase tile, int buttonID) {
		this.tileEntity = tile;
		this.buttonID = buttonID;
	}

	@Override
	public void fromBytes(ByteBuf buffer) {
		int dim = buffer.readInt();
		int x = buffer.readInt();
		int y = buffer.readInt();
		int z = buffer.readInt();
		buttonID = buffer.readInt();
		
		World world = MinecraftServer.getServer().worldServerForDimension(dim);
		if (world != null) {
			TileEntity tile = world.getTileEntity(x, y, z);
			if (tile instanceof TileEntityNetworkBase) {
				this.tileEntity = (TileEntityNetworkBase)tile;
			}
		}
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		buffer.writeInt(tileEntity.getWorldObj().provider.dimensionId);
		buffer.writeInt(tileEntity.xCoord);
		buffer.writeInt(tileEntity.yCoord);
		buffer.writeInt(tileEntity.zCoord);
		buffer.writeInt(buttonID);
	}
	
	// Server side
	public static class Handler implements IMessageHandler<PacketButtonPress, IMessage> {

		@Override
		public IMessage onMessage(PacketButtonPress message, MessageContext ctx) {
			if (message.tileEntity != null) {
				message.tileEntity.onButtonPress(message.buttonID);
				message.tileEntity.postButtonPress(message.buttonID);
			}
			return null;
		}
		
	}

}
