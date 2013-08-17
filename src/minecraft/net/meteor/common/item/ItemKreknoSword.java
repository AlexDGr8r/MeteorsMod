package net.meteor.common.item;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.EnumToolMaterial;

public class ItemKreknoSword extends ItemEnchSword
{
	public ItemKreknoSword(int i, EnumToolMaterial enumtoolmaterial)
	{
		super(i, enumtoolmaterial);
		setEnch(Enchantment.fireAspect, 2);
	}

	@Override
	public float func_82803_g()
	{
		return 8;
	}
}