package net.meteor.common.block;

import java.util.Random;

import net.meteor.common.ClientProxy;
import net.meteor.common.MeteorsMod;
import net.meteor.common.entity.EntityAlienCreeper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMeteor extends BlockMeteorsMod
{
	
	private Icon hotTex;
	
	public BlockMeteor(int i)
	{
		super(i, Material.rock);
		this.setTickRandomly(true);
	}

	@Override
	public int idDropped(int i, Random random, int j)
	{
		return MeteorsMod.itemMeteorChips.itemID;
	}

	@Override
	public int quantityDropped(int meta, int fortune, Random random)
	{
		if ((meta == 0) && (random.nextInt(4) == 0)) {
			return 1 + random.nextInt(fortune + 1);
		}
		return random.nextInt(1 + meta + random.nextInt(fortune + 1));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void randomDisplayTick(World world, int i, int j, int k, Random random)
	{
		if (world.getBlockMetadata(i, j, k) > 0)
			renderGlowParticles(world, i, j, k, random);
	}

	@SideOnly(Side.CLIENT)
	public void renderGlowParticles(World world, int i, int j, int k, Random random)
	{
		double d = 0.0625D;
		for (int l = 0; l < 6; l++)
		{
			double d1 = i + random.nextFloat();
			double d2 = j + random.nextFloat();
			double d3 = k + random.nextFloat();
			if ((l == 0) && (!world.isBlockOpaqueCube(i, j + 1, k)))
			{
				d2 = j + 1 + d;
			}
			if ((l == 1) && (!world.isBlockOpaqueCube(i, j - 1, k)))
			{
				d2 = j + 0 - d;
			}
			if ((l == 2) && (!world.isBlockOpaqueCube(i, j, k + 1)))
			{
				d3 = k + 1 + d;
			}
			if ((l == 3) && (!world.isBlockOpaqueCube(i, j, k - 1)))
			{
				d3 = k + 0 - d;
			}
			if ((l == 4) && (!world.isBlockOpaqueCube(i + 1, j, k)))
			{
				d1 = i + 1 + d;
			}
			if ((l == 5) && (!world.isBlockOpaqueCube(i - 1, j, k)))
			{
				d1 = i + 0 - d;
			}
			if ((d1 < i) || (d1 > i + 1) || (d2 < 0.0D) || (d2 > j + 1) || (d3 < k) || (d3 > k + 1))
			{
				ClientProxy.spawnParticle("meteordust", d1, d2, d3, 0.0D, 0.0D, 0.0D, world, -1);
			}
		}
	}

	@Override
	public int tickRate(World world)
	{
		return 30;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
	{
		AxisAlignedBB AABB = super.getCollisionBoundingBoxFromPool(world, i, j, k);
		return AABB.contract(0.006D, 0.006D, 0.006D);
	}

	@Override
	public boolean isBlockBurning(World world, int i, int j, int k)
	{
		if (world.getBlockMetadata(i, j, k) == 0) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isFireSource(World world, int x, int y, int z, int metadata, ForgeDirection direction)
	{
		if ((direction == ForgeDirection.UP) && (metadata > 0)) {
			return true;
		}
		return super.isFireSource(world, x, y, z, metadata, direction);
	}

	@Override
	public void updateTick(World world, int i, int j, int k, Random random)
	{
		int meta = world.getBlockMetadata(i, j, k);
		if (meta > 0) {
			world.setBlockMetadataWithNotify(i, j, k, --meta, 2);
		}
		if ((meta > 0) && (isWaterAround(world, i, j, k))) {
			world.setBlockMetadataWithNotify(i, j, k, 0, 2);
		}
	}

	private boolean isWaterAround(World world, int i, int j, int k) {
		for (int sY = j + 1; sY >= j - 1; sY--) {
			for (int sX = i + 1; sX >= i - 1; sX--) {
				for (int sZ = k + 1; sZ >= k - 1; sZ--) {
					int id = world.getBlockId(sX, sY, sZ);
					if (id == Block.waterStill.blockID || id == Block.waterMoving.blockID) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public Icon getIcon(int i, int j)
	{
		if (j == 0) {
			super.getIcon(i, j);
			return this.blockIcon;
		}
		return this.hotTex;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public void registerIcons(IconRegister par1IconRegister) {
		this.blockIcon = par1IconRegister.registerIcon("Meteor");
		this.hotTex = par1IconRegister.registerIcon("Meteor_Hot");
	}

	@Override
	public void onBlockDestroyedByPlayer(World world, int i, int j, int k, int l)
	{
		super.onBlockDestroyedByPlayer(world, i, j, k, l);
		if ((!world.isRemote) && (world.rand.nextInt(100) == 95)) {
			EntityAlienCreeper creeper = new EntityAlienCreeper(world);
			creeper.setLocationAndAngles(i, j, k, 0.0F, 0.0F);
			world.spawnEntityInWorld(creeper);
			creeper.spawnExplosionParticle();
			return;
		}
	}

	@Override
	public int getLightValue(IBlockAccess iba, int i, int j, int k)
	{
		if (iba.getBlockMetadata(i, j, k) > 0) {
			return lightValue[this.blockID];
		}
		return 0;
	}
}