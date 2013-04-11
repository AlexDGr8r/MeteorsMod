package net.meteor.common;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CreativeTabMeteor extends CreativeTabs {

	public CreativeTabMeteor(String label) {
		super(label);
	}
	
	@Override
	public ItemStack getIconItemStack()
    {
        return new ItemStack(MeteorsMod.blockMeteor, 1, 1);
    }
	
	@SideOnly(Side.CLIENT)
	@Override
    public String getTranslatedTabLabel() {
		return LangLocalization.get("itemGroup.Meteor");
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public void displayAllReleventItems(List par1List) {
		super.displayAllReleventItems(par1List);
		par1List.add(Item.enchantedBook.func_92111_a(new EnchantmentData(MeteorsMod.ColdTouch, MeteorsMod.ColdTouch.getMaxLevel())));
		par1List.add(Item.enchantedBook.func_92111_a(new EnchantmentData(MeteorsMod.Magnetization, MeteorsMod.Magnetization.getMaxLevel())));
	}

}
