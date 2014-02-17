package net.meteor.common.packets;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.meteor.common.ClientHandler;
import net.meteor.common.GhostMeteor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;

public class PacketGhostMeteor extends AbstractPacket {
	
	private boolean addGhostMeteor;
	private int x;
	private int y;
	private int z;
	
	public PacketGhostMeteor() {
		this.addGhostMeteor = false;
		this.x = this.y = this.z = -1;
	}
	
	public PacketGhostMeteor(boolean adding, GhostMeteor gMet) {
		this(adding, gMet.x, 0, gMet.z);
	}
	
	public PacketGhostMeteor(boolean adding, int x, int y, int z) {
		this.addGhostMeteor = adding;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		buffer.writeBoolean(addGhostMeteor);
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		this.addGhostMeteor = buffer.readBoolean();
		this.x = buffer.readInt();
		this.y = buffer.readInt();
		this.z = buffer.readInt();
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		ChunkCoordinates cc = new ChunkCoordinates(x, y, z);
		if (this.addGhostMeteor) {
			ClientHandler.ghostMetLocs.add(cc);
		} else if (cc.posX == -1 && cc.posY == -1 && cc.posZ == -1) {
			ClientHandler.ghostMetLocs.clear();
		} else {
			for (int i = 0; i < ClientHandler.ghostMetLocs.size(); i++) {
				ChunkCoordinates cc2 = ClientHandler.ghostMetLocs.get(i);
				if (cc2.posX == cc.posX && cc2.posZ == cc.posZ) {
					ClientHandler.ghostMetLocs.remove(i);
					break;
				}
			}
		}
	}

	@Override
	public void handleServerSide(EntityPlayer player) {}

}
