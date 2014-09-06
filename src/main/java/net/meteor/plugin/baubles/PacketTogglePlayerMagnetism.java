package net.meteor.plugin.baubles;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import baubles.api.BaublesApi;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PacketTogglePlayerMagnetism implements IMessage {
	
	private String playerName;
	
	public PacketTogglePlayerMagnetism() {}
	
	public PacketTogglePlayerMagnetism(String pName) {
		this.playerName = pName;
	}

	@Override
	public void fromBytes(ByteBuf buffer) {
		this.playerName = ByteBufUtils.readUTF8String(buffer);
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		ByteBufUtils.writeUTF8String(buffer, this.playerName);
	}
	
	// Client Side
	public static class Handler implements IMessageHandler<PacketTogglePlayerMagnetism, IMessage> {

		@Override
		public IMessage onMessage(PacketTogglePlayerMagnetism message, MessageContext ctx) {
			EntityPlayer player = getClientPlayer();
			EntityPlayer p = player.worldObj.getPlayerEntityByName(message.playerName);
			if (p != null) {
				IInventory inv = BaublesApi.getBaubles(p);
				ItemStack stack = inv.getStackInSlot(3);
				if (stack != null) {
					if (stack.getItem() == Baubles.MagnetismController) {
						boolean val = !ItemMagnetismController.getNBTData(stack);
						ItemMagnetismController.setNBTData(stack, val);
						if (player.getCommandSenderName().equals(message.playerName)) {
							Baubles.renderDisplayTicks = player.worldObj.getTotalWorldTime() + 100L;
							Baubles.renderDisplay = true;
							Baubles.enabledMagnetism = val;
						}
					}
				}
			}
			return null;
		}
		
		@SideOnly(Side.CLIENT)
		public EntityPlayer getClientPlayer() {
			return net.minecraft.client.Minecraft.getMinecraft().thePlayer;
		}
		
	}

}
