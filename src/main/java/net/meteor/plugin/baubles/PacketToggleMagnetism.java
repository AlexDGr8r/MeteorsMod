package net.meteor.plugin.baubles;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.meteor.common.MeteorsMod;
import net.meteor.common.packets.AbstractPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import baubles.api.BaublesApi;

public class PacketToggleMagnetism extends AbstractPacket {

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {}

	@Override
	public void handleClientSide(EntityPlayer player) {}

	@Override
	public void handleServerSide(EntityPlayer player) {
		IInventory inv = BaublesApi.getBaubles(player);
		ItemStack stack = inv.getStackInSlot(3);
		if (stack != null) {
			if (stack.getItem() == Baubles.MagnetismController) {
				if (MinecraftServer.getServer().isDedicatedServer()) {
					boolean val = !ItemMagnetismController.getNBTData(stack);
					ItemMagnetismController.setNBTData(stack, val);	
				}
				MeteorsMod.packetPipeline.sendToAll(new PacketTogglePlayerMagnetism(player.getCommandSenderName()));
			}
		}
	}

}
