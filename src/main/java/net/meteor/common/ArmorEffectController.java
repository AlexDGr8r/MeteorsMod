package net.meteor.common;

import java.lang.reflect.Field;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class ArmorEffectController
{
	public static void setImmuneToFire(EntityPlayer player, boolean flag)
	{
		if (player.isImmuneToFire() != flag) {
			try {
				Side s = FMLCommonHandler.instance().getEffectiveSide();
				setFieldBool(Entity.class, player, flag);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	// TODO Always check for correct field name in each update
	private static void setFieldBool(Class class1, Object instance, boolean value) throws IllegalArgumentException, IllegalAccessException
	{
		try {
			Field field = class1.getDeclaredField("field_70178_ae"); // note that in order to test this in deobfuscated environment, change to "isImmuneToFire"
			field.setAccessible(true);
			field.setBoolean(instance, value);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}
}