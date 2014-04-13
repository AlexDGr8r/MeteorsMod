package net.meteor.common.packets;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.meteor.common.ClientHandler;
import net.meteor.common.CrashLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;

public class PacketLastCrash extends AbstractPacket {
	
	private CrashLocation lastCrashLoc;
	
	public PacketLastCrash() {}
	
	public PacketLastCrash(CrashLocation loc) {
		this.lastCrashLoc = loc;
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		buffer.writeInt(lastCrashLoc.x);
		buffer.writeInt(lastCrashLoc.y);
		buffer.writeInt(lastCrashLoc.z);
		buffer.writeBoolean(lastCrashLoc.inOrbit);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		int x, y, z;
		boolean inOrbit;
		x = buffer.readInt();
		y = buffer.readInt();
		z = buffer.readInt();
		inOrbit = buffer.readBoolean();
		
		if (x == -1 && y == -1 && z == -1) {
			if (ClientHandler.lastCrashLocation != null) {
				this.lastCrashLoc = ClientHandler.lastCrashLocation.prevCrash;
			} else {
				this.lastCrashLoc = null;
			}
		} else {
			this.lastCrashLoc = new CrashLocation(x, y, z, inOrbit, ClientHandler.lastCrashLocation);
		}
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		ClientHandler.lastCrashLocation = this.lastCrashLoc;
	}

	@Override
	public void handleServerSide(EntityPlayer player) {}

}
