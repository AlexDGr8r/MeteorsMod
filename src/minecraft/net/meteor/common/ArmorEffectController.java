package net.meteor.common;

import java.lang.reflect.Field;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class ArmorEffectController
{
	public static void setImmuneToFire(EntityPlayer player, boolean flag)
	{
		if (player.isImmuneToFire() != flag) {
			try {
				Side s = FMLCommonHandler.instance().getEffectiveSide();
				setFieldBool(Entity.class, player, 52, flag); // FIXME
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	private static void setFieldBool(Class class1, Object instance, int fieldIndex, boolean value) throws IllegalArgumentException, IllegalAccessException
	{
		Field field = class1.getDeclaredFields()[fieldIndex];
		field.setAccessible(true);
		field.setBoolean(instance, value);
	}
}