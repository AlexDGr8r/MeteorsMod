package net.meteor.common.packets;

import io.netty.buffer.ByteBuf;
import net.meteor.common.ClientHandler;
import net.meteor.common.climate.CrashLocation;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketLastCrash implements IMessage {
	
	private CrashLocation lastCrashLoc;
	
	public PacketLastCrash() {}
	
	public PacketLastCrash(CrashLocation loc) {
		this.lastCrashLoc = loc;
	}

	@Override
	public void fromBytes(ByteBuf buffer) {
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
	public void toBytes(ByteBuf buffer) {
		buffer.writeInt(lastCrashLoc.x);
		buffer.writeInt(lastCrashLoc.y);
		buffer.writeInt(lastCrashLoc.z);
		buffer.writeBoolean(lastCrashLoc.inOrbit);
	}
	
	// Client Side
	public static class Handler implements IMessageHandler<PacketLastCrash, IMessage> {

		@Override
		public IMessage onMessage(PacketLastCrash message, MessageContext ctx) {
			ClientHandler.lastCrashLocation = message.lastCrashLoc;
			return null;
		}
		
	}

}
