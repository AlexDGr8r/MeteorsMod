package net.meteor.common.block;

import java.util.Random;

import net.meteor.common.MeteorItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockKreknorite extends BlockMeteor
{
	public BlockKreknorite()
	{
		super();
	}

	@Override
	public Item getItemDropped(int i, Random random, int j)
	{
		return MeteorItems.itemKreknoChip;
	}

	@Override
	public void onBlockDestroyedByPlayer(World world, int i, int j, int k, int l)
	{
		if ((!world.isRemote) && (world.rand.nextInt(100) == 95)) {
			EntityBlaze blaze = new EntityBlaze(world);
			blaze.setLocationAndAngles(i, j, k, 0.0F, 0.0F);
			world.spawnEntityInWorld(blaze);
			blaze.spawnExplosionParticle();
		}
	}

	@Override
	public void updateTick(World world, int i, int j, int k, Random random)
	{
		int meta = world.getBlockMetadata(i, j, k);
		if (meta > 0) {
			world.setBlockMetadataWithNotify(i, j, k, --meta, 2);
			if (meta <= 0) {
				world.setBlock(i, j, k, Blocks.obsidian, 0, 2);
				triggerLavaMixEffects(world, i, j, k);
			} else {
				checkForHarden(world, i, j, k);
			}
		}
	}

	@Override
	public void onBlockAdded(World world, int i, int j, int k)
	{
		checkForHarden(world, i, j, k);
	}

	@Override
	public void onNeighborBlockChange(World world, int i, int j, int k, Block block)
	{
		checkForHarden(world, i, j, k);
	}

	private void checkForHarden(World world, int i, int j, int k)
	{
		if (world.getBlock(i, j, k) != this)
		{
			return;
		}
		boolean flag = false;
		if (flag || world.getBlock(i, j, k - 1).getMaterial() == Material.water)
        {
            flag = true;
        }

        if (flag || world.getBlock(i, j, k + 1).getMaterial() == Material.water)
        {
            flag = true;
        }

        if (flag || world.getBlock(i - 1, j, k).getMaterial() == Material.water)
        {
            flag = true;
        }

        if (flag || world.getBlock(i + 1, j, k).getMaterial() == Material.water)
        {
            flag = true;
        }

        if (flag || world.getBlock(i, j + 1, k).getMaterial() == Material.water)
        {
            flag = true;
        }
		if (flag)
		{
			world.setBlock(i, j, k, Blocks.obsidian, 0, 2);
			triggerLavaMixEffects(world, i, j, k);
		}
	}

	@Override
	protected void triggerLavaMixEffects(World world, int i, int j, int k)
	{
		world.playSoundEffect(i + 0.5F, j + 0.5F, k + 0.5F, "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
		for (int l = 0; l < 8; l++)
		{
			world.spawnParticle("largesmoke", i + Math.random(), j + 1.2D, k + Math.random(), 0.0D, 0.0D, 0.0D);
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int i, int j)
	{
		return this.blockIcon;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public void registerBlockIcons(IIconRegister par1IconRegister) {
		this.blockIcon = par1IconRegister.registerIcon("meteors:Kreknorite");
	}
	
	@Override
	public int getExpDrop(IBlockAccess world, int metadata, int fortune) {
		return MathHelper.getRandomIntegerInRange(rand, 3, 6);
	}
	
}