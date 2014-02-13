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
	private IChatComponent statName;
	private String desc;

	@SideOnly(Side.CLIENT)
	private IStatStringFormat formatter;

	public AchievementMeteorsMod(String par1, String par2Str, int par3, int par4, Item par5Item, Achievement par6Achievement)
	{
		this(par1, par2Str, par3, par4, new ItemStack(par5Item), par6Achievement);
	}

	public AchievementMeteorsMod(String par1, String par2Str, int par3, int par4, Block par5Block, Achievement par6Achievement) {
		this(par1, par2Str, par3, par4, new ItemStack(par5Block), par6Achievement);
	}

	public AchievementMeteorsMod(String par1, String par2Str, int par3, int par4, ItemStack par5ItemStack, Achievement par6Achievement) {
		super(par1, par2Str, par3, par4, par5ItemStack, par6Achievement);
		this.statName = new ChatComponentTranslation("achievement." + par2Str, new Object[0]);
		this.desc = ("achievement." + par2Str + ".desc");
		NBTTagCompound tag = par5ItemStack.getTagCompound();
		if (tag == null) {
			tag = new NBTTagCompound();
		}
		tag.setBoolean("enchant-set", true);
		par5ItemStack.setTagCompound(tag);
	}
	
	@Override
	// TODO translate properly
	public IChatComponent func_150951_e()
    {
        IChatComponent ichatcomponent = this.statName.createCopy();
        ichatcomponent.getChatStyle().setColor(EnumChatFormatting.GRAY);
        ichatcomponent.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ACHIEVEMENT, new ChatComponentText(this.statId)));
        return ichatcomponent;
    }

	@Override
	public String toString()
	{
		return LangLocalization.get(this.statName.getUnformattedText());
	}

	@SideOnly(Side.CLIENT)
	@Override
	public String getDescription()
	{
		return this.formatter != null ? this.formatter.formatString(LangLocalization.get(this.desc)) : LangLocalization.get(this.desc);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public Achievement setStatStringFormatter(IStatStringFormat par1IStatStringFormat)
	{
		this.formatter = par1IStatStringFormat;
		return super.setStatStringFormatter(par1IStatStringFormat);
	}
}