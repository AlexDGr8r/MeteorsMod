package net.meteor.common.item;

import java.util.List;

import net.meteor.client.TextureDetector;
import net.meteor.common.ClientHandler;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
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
		
		if (type == 1) {
			par3List.add(EnumChatFormatting.AQUA + StatCollector.translateToLocal("Detector.time"));
			if (ClientHandler.nearestTimeLocation == null) {
				par3List.add(StatCollector.translateToLocal("Detector.scanning") + dots);
			} else {
				par3List.add(EnumChatFormatting.GREEN + StatCollector.translateToLocal("Detector.detected"));
			}
		} else if (type == 0) {
			par3List.add(EnumChatFormatting.LIGHT_PURPLE + StatCollector.translateToLocal("Detector.proximity"));
			if (ClientHandler.getClosestIncomingMeteor(par2EntityPlayer.posX, par2EntityPlayer.posZ) == null) {
				par3List.add(StatCollector.translateToLocal("Detector.scanning") + dots);
			} else {
				par3List.add(EnumChatFormatting.GREEN + StatCollector.translateToLocal("Detector.detected"));
			}
		} else {
			par3List.add(EnumChatFormatting.RED + StatCollector.translateToLocal("Detector.crash"));
			if (ClientHandler.lastCrashLocation == null) {
				par3List.add(StatCollector.translateToLocal("CrashDetector.noActivity"));
				par3List.add(StatCollector.translateToLocal("Detector.scanning") + dots);
			} else {
				if (ClientHandler.lastCrashLocation.inOrbit) {
					par3List.add(EnumChatFormatting.GREEN + "Orbital Entrance Detected!");
				} else {
					par3List.add(EnumChatFormatting.GREEN + StatCollector.translateToLocal("CrashDetector.zoneLocated"));
				}
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
