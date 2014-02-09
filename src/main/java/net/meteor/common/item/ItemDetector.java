package net.meteor.common.item;

import java.util.List;

import net.meteor.client.TextureDetector;
import net.meteor.common.ClientHandler;
import net.meteor.common.LangLocalization;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemDetector extends ItemMeteorsMod {
	
	private int type;
	private String dots = ".";
	private long lastDot = 0L;

	public ItemDetector(int t) {
		super();
		this.type = t;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		
		long worldTime = par2EntityPlayer.worldObj.getTotalWorldTime();
		if (worldTime - lastDot > 10L) {
			lastDot = worldTime;
			dots = dots.length() > 5 ? "." : dots + ".";
		}
		
		if (type == 0) {
			par3List.add(EnumChatFormatting.AQUA + LangLocalization.get("Detector.time"));
			if (ClientHandler.nearestTimeLocation == null) {
				par3List.add(LangLocalization.get("Detector.scanning") + dots);
			} else {
				par3List.add(EnumChatFormatting.GREEN + LangLocalization.get("Detector.detected"));
			}
		} else if (type == 1) {
			par3List.add(EnumChatFormatting.LIGHT_PURPLE + LangLocalization.get("Detector.proximity"));
			if (ClientHandler.getClosestIncomingMeteor(par2EntityPlayer.posX, par2EntityPlayer.posZ) == null) {
				par3List.add(LangLocalization.get("Detector.scanning") + dots);
			} else {
				par3List.add(EnumChatFormatting.GREEN + LangLocalization.get("Detector.detected"));
			}
		} else {
			par3List.add(EnumChatFormatting.RED + LangLocalization.get("Detector.crash"));
			if (ClientHandler.lastCrashLocation == null) {
				par3List.add(LangLocalization.get("CrashDetector.noActivity"));
				par3List.add(LangLocalization.get("Detector.scanning") + dots);
			} else {
				par3List.add(EnumChatFormatting.GREEN + LangLocalization.get("CrashDetector.zoneLocated"));
			}
		}
		
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public void registerIcons(IIconRegister par1IconRegister) {
		TextureMap map = (TextureMap)par1IconRegister;
		map.setTextureEntry(this.iconString, new TextureDetector(this.iconString, type));
		this.itemIcon = map.getTextureExtry(this.iconString);
	}

}
