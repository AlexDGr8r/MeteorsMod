package net.meteor.common.packets;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.meteor.common.ClientHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;

public class PacketLastCrash extends AbstractPacket {
	
	private ChunkCoordinates lastCrashLoc;
	
	public PacketLastCrash() {}
	
	public PacketLastCrash(ChunkCoordinates loc) {
		this.lastCrashLoc = loc;
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		buffer.writeInt(lastCrashLoc.posX);
		buffer.writeInt(lastCrashLoc.posY);
		buffer.writeInt(lastCrashLoc.posZ);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		int x, y, z;
		x = buffer.readInt();
		y = buffer.readInt();
		z = buffer.readInt();
		this.lastCrashLoc = new ChunkCoordinates(x, y, z);
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		ClientHandler.lastCrashLocation = this.lastCrashLoc;
	}

	@Override
	public void handleServerSide(EntityPlayer player) {}

}
