package net.meteor.plugin.baubles;

import java.util.List;

import net.meteor.common.MeteorsMod;
import net.meteor.common.item.ItemMeteorsMod;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemMagnetismController extends ItemMeteorsMod implements IBauble {
	
	private final static String MAGNETIZED_TAG = "magnetEnabled";
	
	protected Enchantment enchantment;
	protected int level;
	
	public ItemMagnetismController() {
		super();
		setMaxStackSize(1);
		setEnch(MeteorsMod.Magnetization, 3);
	}

	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.BELT;
	}

	@Override
	public void onWornTick(ItemStack itemstack, EntityLivingBase player) {}

	@Override
	public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
		NBTTagCompound nbt = itemstack.getTagCompound();
		if (nbt == null || !nbt.hasKey(MAGNETIZED_TAG)) {
			setNBTData(itemstack, true);
		}
	}

	@Override
	public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {}

	@Override
	public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
		return true;
	}

	@Override
	public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
		return true;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List info, boolean advTooltip) {
		info.add("Magnetization: " + (getNBTData(stack) ? EnumChatFormatting.GREEN + "On" : EnumChatFormatting.RED + "Off")); // TODO localize
		info.add(EnumChatFormatting.DARK_GRAY + "Press '" + HandlerKey.getKey() + "' while equipped to");
		info.add(EnumChatFormatting.DARK_GRAY + "toggle ON or OFF.");
		info.add("");
	}
	
	public static void setNBTData(ItemStack itemstack, boolean val) {
		NBTTagCompound nbt = itemstack.getTagCompound();
		if (nbt == null) {
			nbt = new NBTTagCompound();
		}
		nbt.setBoolean(MAGNETIZED_TAG, val);
		itemstack.setTagCompound(nbt);
	}
	
	public static boolean getNBTData(ItemStack itemstack) {
		NBTTagCompound nbt = itemstack.getTagCompound();
		if (nbt != null && nbt.hasKey(MAGNETIZED_TAG)) {
			return nbt.getBoolean(MAGNETIZED_TAG);
		}
		setNBTData(itemstack, true);
		return true;
	}
	
	public static int isMagnetizationEnabled(EntityPlayer player, int def) {
		IInventory inv = BaublesApi.getBaubles(player);
		ItemStack stack = inv.getStackInSlot(3);
		if (stack != null) {
			if (stack.getItem() == Baubles.MagnetismController) {
				return (getNBTData(stack) ? Math.max(EnchantmentHelper.getEnchantmentLevel(MeteorsMod.Magnetization.effectId, stack), def) : 0);
			}
		}
		return def;
	}
	
	public ItemMeteorsMod setEnch(Enchantment ench, int lvl) {
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
