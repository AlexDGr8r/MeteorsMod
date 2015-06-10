package net.meteor.common.item;

import net.meteor.common.MeteorsMod;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemEnchSpade extends ItemSpade
{
	protected Enchantment enchantment;
	protected int level;

	public ItemEnchSpade(Item.ToolMaterial toolMaterial)
	{
		super(toolMaterial);
		this.setCreativeTab(MeteorsMod.meteorTab);
	}

	public Item setEnch(Enchantment ench, int lvl) {
		this.enchantment = ench;
		this.level = lvl;
		return this;
	}
	
	@Override
	public int getDamage(ItemStack stack) {
		if (!stack.isItemEnchanted() && !isRestricted(stack)) {
			stack.addEnchantment(this.enchantment, this.level);
			NBTTagCompound tag = stack.getTagCompound();
			tag.setBoolean("enchant-set", true);
			stack.setTagCompound(tag);
		}
		return super.getDamage(stack);
	}
	
	public ItemEnchSpade setTexture(String s) {
		return (ItemEnchSpade) this.setTextureName(MeteorsMod.MOD_ID + ":" + s);
	}
	
	private boolean isRestricted(ItemStack item) {
		if (item.hasTagCompound()) {
			NBTTagCompound tag = item.getTagCompound();
			if (tag.hasKey("enchant-set")) {
				return tag.getBoolean("enchant-set");
			} else {
				tag.setBoolean("enchant-set", false);
				item.setTagCompound(tag);
			}
		}
		return false;
	}
}