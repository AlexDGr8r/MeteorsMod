package net.meteor.common.packets;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.meteor.common.EnumMeteor;
import net.meteor.common.MeteorsMod;
import net.minecraft.entity.player.EntityPlayer;

public class PacketShieldUpdate extends AbstractPacket {
	
	private String owner;
	private EnumMeteor metType;
	
	public PacketShieldUpdate() {}
	
	public PacketShieldUpdate(String owner) {
		EnumMeteor type = MeteorsMod.proxy.lastMeteorPrevented.get(owner);
		if (type == null) {
			type = EnumMeteor.METEORITE;
		}
		
		this.metType = type;
		this.owner = owner;
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		ByteBufUtils.writeUTF8String(buffer, owner);
		buffer.writeInt(metType.getID());
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		this.owner = ByteBufUtils.readUTF8String(buffer);
		this.metType = EnumMeteor.getTypeFromID(buffer.readInt());
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		MeteorsMod.proxy.lastMeteorPrevented.put(owner, metType);
	}

	@Override
	public void handleServerSide(EntityPlayer player) {}

}
