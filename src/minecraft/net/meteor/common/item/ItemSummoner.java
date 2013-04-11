package net.meteor.common.item;

import java.util.List;

import net.meteor.common.HandlerMeteor;
import net.meteor.common.entity.EntitySummoner;
import net.minecraft.client.renderer.texture.IconRegister;
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
	
	private Icon metIcon;
	private Icon frezIcon;
	private Icon krekIcon;
	private Icon unkIcon;
	private Icon kittyIcon;

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
		switch (i) {
		case 1:
			return this.metIcon;
		case 2:
			return this.frezIcon;
		case 3:
			return this.krekIcon;
		case 4:
			return this.unkIcon;
		case 5:
			return this.kittyIcon;
		default:
			return this.iconIndex;
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public void updateIcons(IconRegister par1IconRegister) {
		this.iconIndex = par1IconRegister.registerIcon("summoner");
		this.metIcon = par1IconRegister.registerIcon("sum_met");
		this.frezIcon = par1IconRegister.registerIcon("sum_frez");
		this.krekIcon = par1IconRegister.registerIcon("sum_krek");
		this.unkIcon = par1IconRegister.registerIcon("sum_unk");
		this.kittyIcon = par1IconRegister.registerIcon("sum_kitty");
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