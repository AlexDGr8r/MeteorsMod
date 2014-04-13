package net.meteor.common;

import net.minecraft.block.Block;
import net.minecraft.event.HoverEvent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.IStatStringFormat;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class AchievementMeteorsMod extends Achievement
{

	public AchievementMeteorsMod(String par1, String par2Str, int par3, int par4, Item par5Item, Achievement par6Achievement)
	{
		this(par1, par2Str, par3, par4, new ItemStack(par5Item), par6Achievement);
	}

	public AchievementMeteorsMod(String par1, String par2Str, int par3, int par4, Block par5Block, Achievement par6Achievement) {
		this(par1, par2Str, par3, par4, new ItemStack(par5Block), par6Achievement);
	}

	public AchievementMeteorsMod(String par1, String par2Str, int par3, int par4, ItemStack par5ItemStack, Achievement par6Achievement) {
		super(par1, par2Str, par3, par4, par5ItemStack, par6Achievement);
		NBTTagCompound tag = par5ItemStack.getTagCompound();
		if (tag == null) {
			tag = new NBTTagCompound();
		}
		tag.setBoolean("enchant-set", true);
		par5ItemStack.setTagCompound(tag);
	}
	
}