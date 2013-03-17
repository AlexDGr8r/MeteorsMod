package net.meteor.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class ItemDetector extends ItemMeteorsMod
{
	public ItemDetector(int par1)
	{
		super(par1);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer player)
	{
		player.sendChatToPlayer(new StringBuilder().append("Time left: ").append(MeteorsMod.proxy.nearestTimeLeft).toString());
		player.sendChatToPlayer(new StringBuilder().append("Side: ").append(FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER ? "Server" : "Client").toString());
		return par1ItemStack;
	}
}