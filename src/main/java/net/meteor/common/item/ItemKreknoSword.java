package net.meteor.common.item;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;

public class ItemKreknoSword extends ItemEnchSword
{
	public ItemKreknoSword(Item.ToolMaterial toolMaterial)
	{
		super(toolMaterial);
		setEnch(Enchantment.fireAspect, 2);
	}

	@Override
	public float func_150931_i()
	{
		return 8;
	}
}