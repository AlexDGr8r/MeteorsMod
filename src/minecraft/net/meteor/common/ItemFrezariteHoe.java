package net.meteor.common;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemFrezariteHoe extends ItemHoe
{
	public ItemFrezariteHoe(int par1, EnumToolMaterial par2EnumToolMaterial)
	{
		super(par1, par2EnumToolMaterial);
		setTextureFile(MeteorsMod.textureFile);
	}

	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
	{
		if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack))
		{
			return false;
		}

		UseHoeEvent event = new UseHoeEvent(par2EntityPlayer, par1ItemStack, par3World, par4, par5, par6);
		if (MinecraftForge.EVENT_BUS.post(event))
		{
			return false;
		}

		if (event.getResult() == Event.Result.ALLOW)
		{
			par1ItemStack.damageItem(1, par2EntityPlayer);
			return true;
		}

		int var11 = par3World.getBlockId(par4, par5, par6);
		int var12 = par3World.getBlockId(par4, par5 + 1, par6);

		if (((par7 == 0) || (var12 != 0) || (var11 != Block.grass.blockID)) && (var11 != Block.dirt.blockID))
		{
			return false;
		}

		Block var13 = Block.tilledField;
		par3World.playSoundEffect(par4 + 0.5F, par5 + 0.5F, par6 + 0.5F, var13.stepSound.getStepSound(), (var13.stepSound.getVolume() + 1.0F) / 2.0F, var13.stepSound.getPitch() * 0.8F);

		if (par3World.isRemote)
		{
			return true;
		}

		par3World.setBlockAndMetadataWithNotify(par4, par5, par6, var13.blockID, 14);
		par1ItemStack.damageItem(1, par2EntityPlayer);
		return true;
	}

	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		par3List.add("\2473" + LangLocalization.get("enchantment.frezHoe.one"));
		par3List.add("\2473" + LangLocalization.get("enchantment.frezHoe.two"));
	}

	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack par1ItemStack)
	{
		return true;
	}

	public int getItemEnchantability()
	{
		return 0;
	}

	public String getItemDisplayName(ItemStack par1ItemStack)
	{
		return LangLocalization.get(getItemNameIS(par1ItemStack) + ".name").trim();
	}
}