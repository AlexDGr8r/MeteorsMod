package net.meteor.common;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumToolMaterial;

public class ItemKreknoSword extends ItemEnchSword
{
	public ItemKreknoSword(int i, EnumToolMaterial enumtoolmaterial)
	{
		super(i, enumtoolmaterial);
		setTextureFile(MeteorsMod.textureFile);
		setEnch(Enchantment.fireAspect, 2);
	}

	@Override
	public int getDamageVsEntity(Entity par1Entity)
	{
		return 8;
	}
}