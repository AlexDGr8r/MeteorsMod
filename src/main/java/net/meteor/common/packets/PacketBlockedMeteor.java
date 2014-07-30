package net.meteor.common.packets;

import io.netty.buffer.ByteBuf;
import net.meteor.common.EnumMeteor;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketBlockedMeteor implements IMessage {
	
	private int x, y, z;
	private EnumMeteor type;
	
	public PacketBlockedMeteor() {}
	
	public PacketBlockedMeteor(int x, int y, int z, EnumMeteor type) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.type = type;
	}

	@Override
	public void fromBytes(ByteBuf buffer) {
		this.x = buffer.readInt();
		this.y = buffer.readInt();
		this.z = buffer.readInt();
		this.type = EnumMeteor.getTypeFromID(buffer.readInt());
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
		buffer.writeInt(type.getID());
	}
	
	// Client sided
	public static class Handler implements IMessageHandler<PacketBlockedMeteor, IMessage> {

		@Override
		public IMessage onMessage(PacketBlockedMeteor message, MessageContext ctx) {
			Block block = message.type.getRepresentingBlock();
			World world = Minecraft.getMinecraft().theWorld;
			double d3 = (double)Math.min(0.2F + (float)10 / 15.0F, 10.0F);

	        if (d3 > 2.5D)
	        {
	            d3 = 2.5D;
	        }

	        int l1 = (int)(150.0D * d3);

	        for (int i2 = 0; i2 < l1; ++i2)
	        {
	            float f3 = MathHelper.randomFloatClamp(world.rand, 0.0F, ((float)Math.PI * 2F));
	            double d5 = (double)MathHelper.randomFloatClamp(world.rand, 0.75F, 1.0F);
	            double d6 = 0.20000000298023224D + d3 / 100.0D;
	            double d7 = (double)(MathHelper.cos(f3) * 0.2F) * d5 * d5 * (d3 + 0.2D);
	            double d8 = (double)(MathHelper.sin(f3) * 0.2F) * d5 * d5 * (d3 + 0.2D);
	            world.spawnParticle("blockcrack_" + Block.getIdFromBlock(block) + "_1", (double)((float)message.x + 0.5F), (double)((float)message.y + 1.0F), (double)((float)message.z + 0.5F), d7, d6, d8);
	        }
	        return null;
		}
		
	}

}
