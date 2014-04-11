package net.meteor.common.crash;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.meteor.common.EnumMeteor;
import net.meteor.common.MeteorBlocks;
import net.meteor.common.MeteorItems;
import net.meteor.common.SBAPI;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class CrashUnknown extends CrashMeteorite
{
	public CrashUnknown(int Size, Explosion expl, EnumMeteor metType)
	{
		super(Size, expl, metType);
	}

	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		int j1 = getFirstUncoveredBlock(world, i, k, j - this.crashSize * 4, j + this.crashSize * 4) + 1;

		SBAPI.generateCuboid(world, i + 1, j1 + 1, k + 1, i - 1, j1 - 1, k - 1, MeteorBlocks.blockMeteor, random.nextInt(4) + 1);
		int end = random.nextInt(5) + 4;
		for (int r = 0; r < end; r++) {
			Block id = random.nextBoolean() ? MeteorBlocks.blockFrezarite : MeteorBlocks.blockKreknorite;
			SBAPI.placeBlock(world, i + random.nextInt(3) - 1, j1 + random.nextInt(3) - 1, k + random.nextInt(3) - 1, id, random.nextInt(4) + 1);
		}
		SBAPI.placeBlock(world, i, j1 + 2, k, Blocks.glowstone);
		SBAPI.placeBlock(world, i, j1 - 2, k, Blocks.glowstone);
		SBAPI.placeBlock(world, i + 2, j1, k, Blocks.glowstone);
		SBAPI.placeBlock(world, i - 2, j1, k, Blocks.glowstone);
		SBAPI.placeBlock(world, i, j1, k + 2, Blocks.glowstone);
		SBAPI.placeBlock(world, i, j1, k - 2, Blocks.glowstone);
		world.setBlock(i, j1, k, Blocks.chest, 0, 3);
		TileEntityChest chest = (TileEntityChest)world.getTileEntity(i, j1, k);
		if (chest != null) {
			for (int i1 = 0; i1 < 8; i1++) {
				ItemStack item = getRandomLoot(random);
				if (item != null) {
					chest.setInventorySlotContents(random.nextInt(chest.getSizeInventory()), item);
				}
			}
		}

		return true;
	}

	private int getFirstUncoveredBlock(World world, int x, int z, int yStart, int yEnd)
	{
		int var3;

		for (var3 = yStart; !world.isAirBlock(x, var3 + 1, z) && var3 <= yEnd; ++var3) {}

		return var3;
	}

	public static ItemStack getRandomLoot(Random random) {
		int r = random.nextInt(60);
		switch (r) {
		case 0:
		case 1:
		case 2:
			return new ItemStack(MeteorItems.itemMeteorChips, random.nextInt(8) + 1);
		case 3:
		case 4:
		case 5:
			return new ItemStack(MeteorItems.itemFrezaCrystal, random.nextInt(8) + 1);
		case 6:
		case 7:
		case 8:
			return new ItemStack(MeteorItems.itemKreknoChip, random.nextInt(8) + 1);
		case 9:
		case 10:
		case 11:
			ItemStack item2 = new ItemStack(MeteorItems.MeteoriteBody, 1);
			return item2;
		case 12:
		case 13:
		case 14:
			return null;
		case 15:
		case 16:
			ItemStack item = new ItemStack(MeteorItems.KreknoriteSword, 1);
			return item;
		case 17:
		case 18:
			return null;
		case 19:
		case 20:
		case 21:
		case 22:
			return new ItemStack(MeteorItems.itemRedMeteorGem, random.nextInt(16) + 1);
		case 23:
		case 24:
		case 25:
			ItemStack item1 = new ItemStack(MeteorItems.KreknoriteBody, 1);
			return item1;
		case 26:
		case 27:
		case 28:
		case 29:
			return new ItemStack(Items.diamond, random.nextInt(2) + 1);
		case 30:
		case 31:
			return new ItemStack(Items.gold_ingot, random.nextInt(4) + 1);
		case 32:
		case 33:
		case 34:
			return new ItemStack(MeteorItems.FrezaritePickaxe, 1);
		case 35:
		case 36:
		case 37:
			return new ItemStack(MeteorItems.itemChocolateIceCream, random.nextInt(6) + 1);
		case 38:
			return new ItemStack(MeteorItems.FrezariteSpade, 1);
		case 39:
		case 40:
			ItemStack item3 = new ItemStack(MeteorItems.KreknoriteHelmet, 1);
			return item3;
		case 41:
		case 42:
			ItemStack item4 = new ItemStack(MeteorItems.FrezariteHelmet, 1);
			return item4;
		case 43:
		case 44:
			return new ItemStack(Items.redstone, random.nextInt(16) + 1);
		case 45:
		case 46:
		case 47:
			return null;
		case 48:
		case 49:
			return new ItemStack(Blocks.iron_block, random.nextInt(5) + 1);
		case 50:
		case 51:
		case 52:
			List l = new ArrayList();
			l.add(MeteorItems.itemMeteorProximityDetector);
			l.add(MeteorItems.itemMeteorTimeDetector);
			l.add(MeteorItems.itemMeteorCrashDetector);
			return new ItemStack((Item)l.get(random.nextInt(3)), 1);
		}
		return null;
	}
}