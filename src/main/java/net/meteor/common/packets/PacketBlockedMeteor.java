package net.meteor.common.packets;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.meteor.common.EnumMeteor;
import net.meteor.common.IMeteorShield;
import net.meteor.common.MeteorsMod;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class PacketBlockedMeteor extends AbstractPacket {
	
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
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
		buffer.writeInt(type.getID());
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		this.x = buffer.readInt();
		this.y = buffer.readInt();
		this.z = buffer.readInt();
		this.type = EnumMeteor.getTypeFromID(buffer.readInt());
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		Block block = type.getRepresentingBlock();
		World world = player.worldObj;
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
            world.spawnParticle("blockcrack_" + Block.getIdFromBlock(block) + "_1", (double)((float)x + 0.5F), (double)((float)y + 1.0F), (double)((float)z + 0.5F), d7, d6, d8);
            MeteorsMod.log.info("Particle should've spawned");
        }
	}

	@Override
	public void handleServerSide(EntityPlayer player) {}

}
