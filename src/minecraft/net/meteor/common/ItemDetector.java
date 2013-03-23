package net.meteor.common;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemDetector extends ItemMeteorsMod
{
	
	private int detectorType;
	
	public ItemDetector(int par1, int type)
	{
		super(par1);
		this.detectorType = type;
	}

//	@Override
//	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer player)
//	{
//		player.sendChatToPlayer(new StringBuilder().append("Time left: ").append(MeteorsMod.proxy.nearestTimeLeft).toString());
//		player.sendChatToPlayer(new StringBuilder().append("Side: ").append(FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER ? "Server" : "Client").toString());
//		return par1ItemStack;
//	}
	
}