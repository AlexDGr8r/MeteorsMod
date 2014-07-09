package net.meteor.common.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.meteor.common.ClientProxy;
import net.meteor.common.IMeteorShield;
import net.meteor.common.MeteorBlocks;
import net.meteor.common.MeteorsMod;
import net.meteor.common.climate.HandlerMeteor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMeteorShieldTorch extends BlockTorch
{
	private boolean torchActive;

	public BlockMeteorShieldTorch(boolean active)
	{
		super();
		this.torchActive = active;
		setTickRandomly(true);
	}

	@Override
	public int tickRate(World world)
	{
		return 2;
	}

	@Override
	public void updateTick(World world, int i, int j, int k, Random random)
	{
		checkArea(world, i, j, k);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void randomDisplayTick(World world, int i, int j, int k, Random random)
	{
		if (!this.torchActive) {
			return;
		}
		int l = world.getBlockMetadata(i, j, k);
		double d = i + 0.5F + (random.nextFloat() - 0.5F) * 0.2D;
		double d1 = j + 0.7F + (random.nextFloat() - 0.5F) * 0.2D;
		double d2 = k + 0.5F + (random.nextFloat() - 0.5F) * 0.2D;
		double d3 = 0.219999998807907D;
		double d4 = 0.2700000107288361D;
		if (l == 1)
		{
			ClientProxy.spawnParticle("meteordust", d - d4, d1 + d3, d2, 0.0D, 0.0D, 0.0D, world, -1);
		}
		else if (l == 2)
		{
			ClientProxy.spawnParticle("meteordust", d + d4, d1 + d3, d2, 0.0D, 0.0D, 0.0D, world, -1);
		}
		else if (l == 3)
		{
			ClientProxy.spawnParticle("meteordust", d, d1 + d3, d2 - d4, 0.0D, 0.0D, 0.0D, world, -1);
		}
		else if (l == 4)
		{
			ClientProxy.spawnParticle("meteordust", d, d1 + d3, d2 + d4, 0.0D, 0.0D, 0.0D, world, -1);
		}
		else
			ClientProxy.spawnParticle("meteordust", d, d1, d2, 0.0D, 0.0D, 0.0D, world, -1);
	}

	@Override
	public Item getItemDropped(int i, Random random, int j)
	{
		return Item.getItemFromBlock(MeteorBlocks.torchMeteorShieldActive);
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 0;
	}

	@Override
	public void onBlockAdded(World world, int i, int j, int k)
	{
		if (world.getBlockMetadata(i, j, k) == 0)
		{
			super.onBlockAdded(world, i, j, k);
		}
		checkArea(world, i, j, k);
	}

	private void checkArea(World world, int i, int j, int k) {
		boolean isSafeChunk = MeteorsMod.proxy.metHandlers.get(world.provider.dimensionId).getShieldManager().getClosestShieldInRange(i, k) != null;
		if (this.torchActive) {
			if (!isSafeChunk)
				world.setBlock(i, j, k, MeteorBlocks.torchMeteorShieldIdle, world.getBlockMetadata(i, j, k), 3);
		}
		else if (isSafeChunk)
			world.setBlock(i, j, k, MeteorBlocks.torchMeteorShieldActive, world.getBlockMetadata(i, j, k), 3);
	}

	@Override
	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int par6, float par7, float par8, float par9)
	{
		if (!world.isRemote) {
			checkArea(world, i, j, k);
			HandlerMeteor meteorHandler = MeteorsMod.proxy.metHandlers.get(world.provider.dimensionId);
			List<IMeteorShield> shields = meteorHandler.getShieldManager().getShieldsInRange(i, k);
			if (!shields.isEmpty()) {
				player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("ProtectionTorch.landOwnership")));
				List owners = new ArrayList();
				for (int l = 0; l < shields.size(); l++) {
					IMeteorShield oPair = shields.get(l);
					if (!owners.contains(oPair.getOwner())) {
						owners.add(oPair.getOwner());
					}
				}

				for (int l = 0; l < owners.size(); l++) {
					player.addChatMessage(new ChatComponentText("    - " + (String)owners.get(l)));
				}
			}
		}

		return true;
	}
	
	@Override
	public Block setBlockTextureName(String s) {
		return super.setBlockTextureName(MeteorsMod.MOD_ID + ":" + s);
	}

}