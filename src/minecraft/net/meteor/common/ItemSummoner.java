package net.meteor.common;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemSummoner extends ItemMeteorsMod
{
	private String[] names = { "random", "meteorite", "frezarite", "kreknorite", "unknown", "kitty" };

	public ItemSummoner(int i) {
		super(i);
		this.maxStackSize = 16;
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
	{
		if (!entityplayer.capabilities.isCreativeMode)
		{
			itemstack.stackSize--;
		}
		world.playSoundAtEntity(entityplayer, "random.bow", 0.5F, 0.4F / (this.itemRand.nextFloat() * 0.4F + 0.8F));
		if (!world.isRemote)
		{
			int i = itemstack.getItemDamage();
			world.spawnEntityInWorld(new EntitySummoner(world, entityplayer, i == 0 ? HandlerMeteor.getMeteorType().getID() : i - 1, i == 0));
		}
		return itemstack;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public Icon getIconFromDamage(int i)
	{
		return this.iconIndex + i;
	}

	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack)
	{
		int var2 = par1ItemStack.getItemDamage();
		return super.getUnlocalizedName() + "." + this.names[var2];
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (int var4 = 0; var4 < 6; var4++)
			par3List.add(new ItemStack(par1, 1, var4));
	}
}