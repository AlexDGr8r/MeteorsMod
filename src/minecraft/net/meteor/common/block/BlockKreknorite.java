package net.meteor.common.block;

import java.util.Random;

import net.meteor.common.MeteorsMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockKreknorite extends BlockMeteor
{
	public BlockKreknorite(int i)
	{
		super(i);
	}

	@Override
	public int idDropped(int i, Random random, int j)
	{
		return MeteorsMod.itemKreknoChip.itemID;
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
				world.setBlock(i, j, k, Block.obsidian.blockID, 0, 2);
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
	public void onNeighborBlockChange(World world, int i, int j, int k, int l)
	{
		checkForHarden(world, i, j, k);
	}

	private void checkForHarden(World world, int i, int j, int k)
	{
		if (world.getBlockId(i, j, k) != blockID)
		{
			return;
		}
		boolean flag = false;
		if (flag || world.getBlockMaterial(i, j, k - 1) == Material.water)
		{
			flag = true;
		}
		if (flag || world.getBlockMaterial(i, j, k + 1) == Material.water)
		{
			flag = true;
		}
		if (flag || world.getBlockMaterial(i - 1, j, k) == Material.water)
		{
			flag = true;
		}
		if (flag || world.getBlockMaterial(i + 1, j, k) == Material.water)
		{
			flag = true;
		}
		if (flag || world.getBlockMaterial(i, j + 1, k) == Material.water)
		{
			flag = true;
		}
		if (flag)
		{
			world.setBlock(i, j, k, Block.obsidian.blockID, 0, 2);
			triggerLavaMixEffects(world, i, j, k);
		}
	}

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
	public Icon getIcon(int i, int j)
	{
		return this.blockIcon;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public void registerIcons(IconRegister par1IconRegister) {
		this.blockIcon = par1IconRegister.registerIcon("Kreknorite");
	}
	
}