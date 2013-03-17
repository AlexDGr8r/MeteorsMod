package net.meteor.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
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

		SBAPI.generateCuboid(world, i + 1, j1 + 1, k + 1, i - 1, j1 - 1, k - 1, MeteorsMod.blockMeteor.blockID, random.nextInt(4) + 1);
		int end = random.nextInt(5) + 4;
		for (int r = 0; r < end; r++) {
			int id = random.nextBoolean() ? MeteorsMod.blockFrezarite.blockID : MeteorsMod.blockKreknorite.blockID;
			SBAPI.placeBlock(world, i + random.nextInt(3) - 1, j1 + random.nextInt(3) - 1, k + random.nextInt(3) - 1, id, random.nextInt(4) + 1);
		}
		SBAPI.placeBlock(world, i, j1 + 2, k, Block.glowStone.blockID);
		SBAPI.placeBlock(world, i, j1 - 2, k, Block.glowStone.blockID);
		SBAPI.placeBlock(world, i + 2, j1, k, Block.glowStone.blockID);
		SBAPI.placeBlock(world, i - 2, j1, k, Block.glowStone.blockID);
		SBAPI.placeBlock(world, i, j1, k + 2, Block.glowStone.blockID);
		SBAPI.placeBlock(world, i, j1, k - 2, Block.glowStone.blockID);
		world.setBlockAndMetadataWithNotify(i, j1, k, Block.chest.blockID, 0, 3);
		TileEntityChest chest = (TileEntityChest)world.getBlockTileEntity(i, j1, k);
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

	private ItemStack getRandomLoot(Random random) {
		int r = random.nextInt(60);
		switch (r) {
		case 0:
		case 1:
		case 2:
			return new ItemStack(MeteorsMod.itemMeteorChips, random.nextInt(8) + 1);
		case 3:
		case 4:
		case 5:
			return new ItemStack(MeteorsMod.itemFrezaCrystal, random.nextInt(8) + 1);
		case 6:
		case 7:
		case 8:
			return new ItemStack(MeteorsMod.itemKreknoChip, random.nextInt(8) + 1);
		case 9:
		case 10:
		case 11:
			ItemStack item2 = new ItemStack(MeteorsMod.MeteoriteBody, 1);
			item2.addEnchantment(MeteorsMod.Magnetization, 1);
			return item2;
		case 12:
		case 13:
		case 14:
			return null;
		case 15:
		case 16:
			ItemStack item = new ItemStack(MeteorsMod.KreknoriteSword, 1);
			item.addEnchantment(Enchantment.fireAspect, 2);
			return item;
		case 17:
		case 18:
			return null;
		case 19:
		case 20:
		case 21:
		case 22:
			return new ItemStack(MeteorsMod.itemRedMeteorGem, random.nextInt(16) + 1);
		case 23:
		case 24:
		case 25:
			ItemStack item1 = new ItemStack(MeteorsMod.KreknoriteBody, 1);
			item1.addEnchantment(Enchantment.fireProtection, 4);
			return item1;
		case 26:
		case 27:
		case 28:
		case 29:
			return new ItemStack(Item.diamond, random.nextInt(2) + 1);
		case 30:
		case 31:
			return new ItemStack(Item.ingotGold, random.nextInt(4) + 1);
		case 32:
		case 33:
		case 34:
			return new ItemStack(MeteorsMod.FrezaritePickaxe, 1);
		case 35:
		case 36:
		case 37:
			return new ItemStack(MeteorsMod.itemChocolateIceCream, random.nextInt(6) + 1);
		case 38:
			return new ItemStack(MeteorsMod.FrezariteSpade, 1);
		case 39:
		case 40:
			ItemStack item3 = new ItemStack(MeteorsMod.KreknoriteHelmet, 1);
			item3.addEnchantment(Enchantment.fireProtection, 4);
			return item3;
		case 41:
		case 42:
			ItemStack item4 = new ItemStack(MeteorsMod.FrezariteHelmet, 1);
			item4.addEnchantment(Enchantment.respiration, 3);
			return item4;
		case 43:
		case 44:
			return new ItemStack(Item.redstone, random.nextInt(16) + 1);
		case 45:
		case 46:
		case 47:
			return null;
		case 48:
		case 49:
			return new ItemStack(Block.blockSteel, random.nextInt(5) + 1);
		case 50:
		case 51:
		case 52:
			if (MeteorsMod.forModpack) {
				List l = new ArrayList();
				l.add(MeteorsMod.itemMeteorProximityDetector);
				l.add(MeteorsMod.itemMeteorTimeDetector);
				l.add(MeteorsMod.itemMeteorCrashDetector);
				return new ItemStack((Item)l.get(random.nextInt(3)), 1);
			}
			return new ItemStack(MeteorsMod.itemMeteorSummoner, 1, random.nextInt(6));
		}
		return null;
	}
}