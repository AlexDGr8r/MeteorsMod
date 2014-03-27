package net.meteor.common;

import net.meteor.client.gui.GuiMeteorShield;
import net.meteor.common.block.container.ContainerMeteorShield;
import net.meteor.common.tileentity.TileEntityMeteorShield;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class HandlerGui implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity instanceof TileEntityMeteorShield) {
			return new ContainerMeteorShield(player.inventory, (TileEntityMeteorShield)tileEntity);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity instanceof TileEntityMeteorShield) {
			return new GuiMeteorShield(player.inventory, (TileEntityMeteorShield)tileEntity);
		}
		return null;
	}

}
