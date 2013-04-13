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
		if (player.isImmuneToFire() != flag)
			try {
				Side s = FMLCommonHandler.instance().getEffectiveSide();
				setFieldBool(Entity.class, player, 54 - (s == Side.SERVER ? 2 : 0), flag); // FIXME
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
	}

	public static boolean isWearingMagneticArmor(EntityPlayer player)
	{
		if (HandlerPlayerTick.hasEnchantment(MeteorsMod.Magnetization, player.inventory)) {
			return true;
		}

		if ((isWearing(MeteorsMod.MeteoriteHelmet.itemID, player.inventory.armorItemInSlot(3))) ||
				(isWearing(MeteorsMod.MeteoriteBody.itemID, player.inventory.armorItemInSlot(2))) || 
				(isWearing(MeteorsMod.MeteoriteLegs.itemID, player.inventory.armorItemInSlot(1))) || 
				(isWearing(MeteorsMod.MeteoriteBoots.itemID, player.inventory.armorItemInSlot(0))))
		{
			return true;
		}

		return false;
	}

	private static boolean isWearing(int ID, ItemStack item) {
		if ((item != null) && 
				(item.itemID == ID)) return true;

		return false;
	}

	private static void setFieldBool(Class class1, Object instance, int fieldIndex, boolean value) throws IllegalArgumentException, IllegalAccessException
	{
		Field field = class1.getDeclaredFields()[fieldIndex];
		field.setAccessible(true);
		field.setBoolean(instance, value);
	}
}