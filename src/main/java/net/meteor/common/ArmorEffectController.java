package net.meteor.common;

import java.lang.reflect.Field;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class ArmorEffectController
{
	
	private static Field fireImmunityField;
	
	// TODO Always check for correct field name in each update
	public static void setImmuneToFire(EntityPlayer player, boolean flag)
	{
		if (player.isImmuneToFire() != flag) {
			try {
				if (fireImmunityField == null) {
					fireImmunityField = Entity.class.getDeclaredField("field_70178_ae"); // note that in order to test this in deobfuscated environment, change to "isImmuneToFire"
					fireImmunityField.setAccessible(true);
				}
				fireImmunityField.setBoolean(player, flag);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

}