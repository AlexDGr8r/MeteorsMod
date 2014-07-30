package net.meteor.plugin.baubles;

import io.netty.buffer.ByteBuf;
import net.meteor.common.MeteorsMod;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import baubles.api.BaublesApi;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketToggleMagnetism implements IMessage {

	@Override
	public void fromBytes(ByteBuf buf) {}

	@Override
	public void toBytes(ByteBuf buf) {}
	
	// Server Side
	public static class Handler implements IMessageHandler<PacketToggleMagnetism, IMessage> {

		@Override
		public IMessage onMessage(PacketToggleMagnetism message, MessageContext ctx) {
			IInventory inv = BaublesApi.getBaubles(ctx.getServerHandler().playerEntity);
			ItemStack stack = inv.getStackInSlot(3);
			if (stack != null) {
				if (stack.getItem() == Baubles.MagnetismController) {
					if (MinecraftServer.getServer().isDedicatedServer()) {
						boolean val = !ItemMagnetismController.getNBTData(stack);
						ItemMagnetismController.setNBTData(stack, val);	
					}
					MeteorsMod.network.sendToAll(new PacketTogglePlayerMagnetism(ctx.getServerHandler().playerEntity.getCommandSenderName()));
				}
			}
			return null;
		}
		
	}

}
