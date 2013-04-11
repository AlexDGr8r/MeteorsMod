package net.meteor.common;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.IStatStringFormat;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class AchievementMeteorsMod extends Achievement
{
	private String desc;

	@SideOnly(Side.CLIENT)
	private IStatStringFormat formatter;

	public AchievementMeteorsMod(int par1, String par2Str, int par3, int par4, Item par5Item, Achievement par6Achievement)
	{
		this(par1, par2Str, par3, par4, new ItemStack(par5Item), par6Achievement);
	}

	public AchievementMeteorsMod(int par1, String par2Str, int par3, int par4, Block par5Block, Achievement par6Achievement) {
		this(par1, par2Str, par3, par4, new ItemStack(par5Block), par6Achievement);
	}

	public AchievementMeteorsMod(int par1, String par2Str, int par3, int par4, ItemStack par5ItemStack, Achievement par6Achievement) {
		super(par1, par2Str, par3, par4, par5ItemStack, par6Achievement);
		this.desc = ("achievement." + par2Str + ".desc");
	}
	
	@SideOnly(Side.CLIENT)
    public String getName()
    {
        return LangLocalization.get(super.getName());
    }

	@Override
	public String toString()
	{
		return LangLocalization.get(this.statName);
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