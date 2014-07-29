package net.meteor.common.packets;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.meteor.common.tileentity.TileEntityNetworkBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PacketButtonPress extends AbstractPacket {
	
	private TileEntityNetworkBase tileEntity;
	private int buttonID;
	
	public PacketButtonPress() {}
	
	public PacketButtonPress(TileEntityNetworkBase tile, int buttonID) {
		this.tileEntity = tile;
		this.buttonID = buttonID;
	}

	// Always Client Side
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		buffer.writeInt(tileEntity.getWorldObj().provider.dimensionId);
		buffer.writeInt(tileEntity.xCoord);
		buffer.writeInt(tileEntity.yCoord);
		buffer.writeInt(tileEntity.zCoord);
		buffer.writeInt(buttonID);
	}

	// Always Server Side
	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
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
	public void handleClientSide(EntityPlayer player) {}

	@Override
	public void handleServerSide(EntityPlayer player) {
		if (tileEntity != null) {
			tileEntity.onButtonPress(buttonID);
			tileEntity.postButtonPress(buttonID);
		}
	}

}
