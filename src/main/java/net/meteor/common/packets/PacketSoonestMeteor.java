package net.meteor.common.packets;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.meteor.common.ClientHandler;
import net.meteor.common.climate.GhostMeteor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;

public class PacketSoonestMeteor extends AbstractPacket {
	
	private ChunkCoordinates soonestMeteorLoc;
	
	public PacketSoonestMeteor() {}
	
	public PacketSoonestMeteor(GhostMeteor gMeteor) {
		if (gMeteor != null) {
			this.soonestMeteorLoc = new ChunkCoordinates(gMeteor.x, 0, gMeteor.z);
		} else {
			this.soonestMeteorLoc = new ChunkCoordinates(-1, -1, -1);
		}
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		buffer.writeInt(soonestMeteorLoc.posX);
		buffer.writeInt(soonestMeteorLoc.posY);
		buffer.writeInt(soonestMeteorLoc.posZ);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		int x, y, z;
		x = buffer.readInt();
		y = buffer.readInt();
		z = buffer.readInt();
		if (y == -1) {
			this.soonestMeteorLoc = null;
		} else {
			this.soonestMeteorLoc = new ChunkCoordinates(x, y, z);
		}
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		ClientHandler.nearestTimeLocation = this.soonestMeteorLoc;
	}

	@Override
	public void handleServerSide(EntityPlayer player) {}

}
