package net.meteor.common.packets;

import io.netty.buffer.ByteBuf;
import net.meteor.common.MeteorsMod;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketSettings implements IMessage {
	
	private boolean meteorGrief;
	private boolean fallAtNight;
	private int minSize;
	private int maxSize;
	private int shieldRadiusMultiplier;

	@Override
	public void fromBytes(ByteBuf buffer) {
		this.meteorGrief = buffer.readBoolean();
		this.fallAtNight = buffer.readBoolean();
		this.minSize = buffer.readInt();
		this.maxSize = buffer.readInt();
		this.shieldRadiusMultiplier = buffer.readInt();
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		MeteorsMod mod = MeteorsMod.instance;
		buffer.writeBoolean(mod.allowSummonedMeteorGrief);
		buffer.writeBoolean(mod.meteorsFallOnlyAtNight);
		buffer.writeInt(mod.MinMeteorSize);
		buffer.writeInt(mod.MaxMeteorSize);
		buffer.writeInt(mod.ShieldRadiusMultiplier);
	}
	
	// Client Side
	public static class Handler implements IMessageHandler<PacketSettings, IMessage> {

		@Override
		public IMessage onMessage(PacketSettings message, MessageContext ctx) {
			MeteorsMod mod = MeteorsMod.instance;
			mod.allowSummonedMeteorGrief = message.meteorGrief;
			mod.meteorsFallOnlyAtNight = message.fallAtNight;
			mod.MinMeteorSize = message.minSize;
			mod.MaxMeteorSize = message.maxSize;
			mod.ShieldRadiusMultiplier = message.shieldRadiusMultiplier;
			return null;
		}
		
	}

}
