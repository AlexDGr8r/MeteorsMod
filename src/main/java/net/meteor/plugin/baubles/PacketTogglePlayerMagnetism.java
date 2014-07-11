package net.meteor.plugin.baubles;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

import net.meteor.common.MeteorsMod;
import net.meteor.common.packets.AbstractPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import baubles.api.BaublesApi;

import com.google.common.base.Charsets;

public class PacketTogglePlayerMagnetism extends AbstractPacket {
	
	private String playerName;
	
	public PacketTogglePlayerMagnetism() {}
	
	public PacketTogglePlayerMagnetism(String pName) {
		this.playerName = pName;
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		try {
			writeStringToBuffer(this.playerName, buffer);
		} catch (IOException e) {
			MeteorsMod.log.info("Could not encode player name to toggle magnetism");
		}
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		try {
			this.playerName = readStringFromBuffer(64, buffer);
		} catch (IOException e) {
			MeteorsMod.log.info("Could not decode player name to toggle magnetism");
		}
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		EntityPlayer p = player.worldObj.getPlayerEntityByName(playerName);
		if (p != null) {
			IInventory inv = BaublesApi.getBaubles(p);
			ItemStack stack = inv.getStackInSlot(3);
			if (stack != null) {
				if (stack.getItem() == Baubles.MagnetismController) {
					boolean val = !ItemMagnetismController.getNBTData(stack);
					ItemMagnetismController.setNBTData(stack, val);
					if (player.getCommandSenderName().equals(playerName)) {
						Baubles.renderDisplayTicks = Minecraft.getMinecraft().theWorld.getTotalWorldTime() + 100L;
						Baubles.renderDisplay = true;
						Baubles.enabledMagnetism = val;
					}
				}
			}
		}
	}

	@Override
	public void handleServerSide(EntityPlayer player) {}
	
	/**
     * Writes a (UTF-8 encoded) String to this buffer. Will throw IOException if String length exceeds 32767 bytes
     * Taken from PacketBuffer class
     */
    private void writeStringToBuffer(String str, ByteBuf buffer) throws IOException
    {
        byte[] abyte = str.getBytes(Charsets.UTF_8);

        if (abyte.length > 32767)
        {
            throw new IOException("String too big (was " + str.length() + " bytes encoded, max " + 32767 + ")");
        }
        else
        {
        	buffer.writeInt(abyte.length);
            buffer.writeBytes(abyte);
        }
    }
    
    /**
     * Reads a string from this buffer. Expected parameter is maximum allowed string length. Will throw IOException if
     * string length exceeds this value!
     * Taken from PacketBuffer class
     */
    private String readStringFromBuffer(int maxLength, ByteBuf buffer) throws IOException
    {
        int j = buffer.readInt();

        if (j > maxLength * 4)
        {
            throw new IOException("The received encoded string buffer length is longer than maximum allowed (" + j + " > " + maxLength * 4 + ")");
        }
        else if (j < 0)
        {
            throw new IOException("The received encoded string buffer length is less than zero! Weird string!");
        }
        else
        {
            String s = new String(buffer.readBytes(j).array(), Charsets.UTF_8);

            if (s.length() > maxLength)
            {
                throw new IOException("The received string length is longer than maximum allowed (" + j + " > " + maxLength + ")");
            }
            else
            {
                return s;
            }
        }
    }

}
