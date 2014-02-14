package net.meteor.common.packets;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.meteor.common.MeteorsMod;
import net.minecraft.entity.player.EntityPlayer;

public class PacketSettings extends AbstractPacket {
	
	private boolean meteorGrief;
	private boolean fallAtNight;
	private int minSize;
	private int maxSize;
	private int shieldRadiusMultiplier;

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		MeteorsMod mod = MeteorsMod.instance;
		buffer.writeBoolean(mod.allowSummonedMeteorGrief);
		buffer.writeBoolean(mod.meteorsFallOnlyAtNight);
		buffer.writeInt(mod.MinMeteorSize);
		buffer.writeInt(mod.MaxMeteorSize);
		buffer.writeInt(mod.ShieldRadiusMultiplier);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		this.meteorGrief = buffer.readBoolean();
		this.fallAtNight = buffer.readBoolean();
		this.minSize = buffer.readInt();
		this.maxSize = buffer.readInt();
		this.shieldRadiusMultiplier = buffer.readInt();
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		MeteorsMod mod = MeteorsMod.instance;
		mod.allowSummonedMeteorGrief = this.meteorGrief;
		mod.meteorsFallOnlyAtNight = this.fallAtNight;
		mod.MinMeteorSize = this.minSize;
		mod.MaxMeteorSize = this.maxSize;
		mod.ShieldRadiusMultiplier = this.shieldRadiusMultiplier;
	}

	@Override
	public void handleServerSide(EntityPlayer player) {}

}
