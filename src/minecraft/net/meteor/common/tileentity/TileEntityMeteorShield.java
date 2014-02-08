package net.meteor.common.tileentity;

import java.util.Random;

import net.meteor.common.ClientProxy;
import net.meteor.common.EnumMeteor;
import net.meteor.common.MeteorItems;
import net.meteor.common.MeteorsMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityMeteorShield extends TileEntity implements IInventory
{
	private static final double[][] directionMotion = { { 0.0D, 0.0D }, { 0.0D, 0.3D }, { -0.3D, 0.3D }, { -0.3D, 0.0D }, { -0.3D, -0.3D }, { 0.0D, -0.3D }, { 0.3D, -0.3D }, { 0.3D, 0.0D }, { 0.3D, 0.3D } };

	@SideOnly(Side.CLIENT)
	private float field_82138_c;

	@SideOnly(Side.CLIENT)
	private long lastTickTime;
	private boolean shieldedChunks;
	private EnumMeteor lastStoppedMeteor = EnumMeteor.METEORITE;
	public String owner;
	public int direction;
	public float rayHeight;
	public float rayXMod;
	public float rayZMod;
	public boolean renderRay = false;
	
	// Inventory stuff
	private ItemStack[] inv;

	public TileEntityMeteorShield() {
		this.shieldedChunks = false;
		this.rayHeight = 0.0F;
		this.direction = 0;
		this.inv = new ItemStack[5];
	}

	public TileEntityMeteorShield(String theOwner) {
		this();
		this.owner = theOwner;
	}

	@Override
	public void updateEntity()
	{
		if (!this.shieldedChunks) {
			int meta = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
			if (meta > 0) {
				if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
					Chunk chunk = this.worldObj.getChunkFromBlockCoords(this.xCoord, this.zCoord);
					MeteorsMod.proxy.metHandlers.get(worldObj.provider.dimensionId).addSafeChunks(chunk.xPosition, chunk.zPosition, MeteorsMod.instance.ShieldRadiusMultiplier * meta, this.owner);
				}
				this.shieldedChunks = this.renderRay = true;
				this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
			}
		}
		if (this.shieldedChunks) {
			int meta = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
			if (meta == 5) {
				if (this.worldObj.getTotalWorldTime() % 60L == 0L) {
					this.renderRay = this.worldObj.canBlockSeeTheSky(this.xCoord, this.yCoord + 1, this.zCoord);
				}
				if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
					GenerateParticles(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.worldObj.rand);
			}
			else {
				int mult = MathHelper.clamp_int(meta, 1, 4);
				if (this.rayHeight < 100.0F) {
					this.rayHeight += 0.3F * mult;
					this.rayXMod = ((float)(this.rayXMod + this.directionMotion[this.direction][0] * mult));
					this.rayZMod = ((float)(this.rayZMod + this.directionMotion[this.direction][1] * mult));
				} else {
					this.rayHeight = 0.0F;
					this.rayXMod = 0.0F;
					this.rayZMod = 0.0F;
					this.renderRay = true;
					this.direction = this.worldObj.rand.nextInt(9);
				}
				if ((this.rayHeight > 1.0F) && (this.renderRay)) {
					int x = (int)(this.xCoord + this.rayXMod);
					int y = (int)(this.yCoord + this.rayHeight);
					int z = (int)(this.zCoord + this.rayZMod);
					this.renderRay = !worldObj.isBlockNormalCubeDefault(xCoord, y, zCoord, true);
				}
				if ((this.rayHeight > 100.0F - (35.0F + 5 * mult)) && (this.rayHeight < 100.0F - (10.0F + 5 * mult)) && 
						(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)) {
					GenerateParticles(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.worldObj.rand);
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	private void GenerateParticles(World world, int i, int j, int k, Random random)
	{
		if (world.isBlockOpaqueCube(i, j + 1, k)) return;
		for (int l = i - 2; l <= i + 2; l++)
		{
			for (int i1 = k - 2; i1 <= k + 2; i1++)
			{
				if ((l > i - 2) && (l < i + 2) && (i1 == k - 1))
				{
					i1 = k + 2;
				}
				if (random.nextInt(32) == 0)
				{
					for (int j1 = j; j1 <= j + 1; j1++)
					{
						if (!world.isAirBlock((l - i) / 2 + i, j1, (i1 - k) / 2 + k))
						{
							break;
						}
						if (world.getBlockMetadata(i, j, k) == 5) {
							int id = getLastStoppedMeteor().getID();
							ClientProxy.spawnParticle("meteorshield", i + 0.5D, j + 2.0D, k + 0.5D, l - i + random.nextFloat() - 0.5D, j1 - j - random.nextFloat() - 1.0F, i1 - k + random.nextFloat() - 0.5D, world, id);
						} else {
							ClientProxy.spawnParticle("meteorshield", i + 0.5D, j + 2.0D, k + 0.5D, l - i + random.nextFloat() - 0.5D, j1 - j - random.nextFloat() - 1.0F, i1 - k + random.nextFloat() - 0.5D, world, -1);
						}
					}
				}
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.owner = nbt.getString("owner");
		this.lastStoppedMeteor = EnumMeteor.getTypeFromID(nbt.getInteger("metprevent"));
		MeteorsMod.proxy.lastMeteorPrevented.put(this.owner, this.lastStoppedMeteor);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setString("owner", this.owner);
		this.lastStoppedMeteor = ((EnumMeteor)MeteorsMod.proxy.lastMeteorPrevented.get(this.owner));
		if (this.lastStoppedMeteor != null)
			nbt.setInteger("metprevent", this.lastStoppedMeteor.getID());
		else
			nbt.setInteger("metprevent", EnumMeteor.METEORITE.getID());
	}

	public EnumMeteor getLastStoppedMeteor()
	{
		this.lastStoppedMeteor = ((EnumMeteor)MeteorsMod.proxy.lastMeteorPrevented.get(this.owner));
		if (this.lastStoppedMeteor != null) {
			return this.lastStoppedMeteor;
		}
		return EnumMeteor.METEORITE;
	}

	public float getBeaconModifier()
	{
		int var1 = (int)(this.worldObj.getTotalWorldTime() - this.lastTickTime);
		this.lastTickTime = this.worldObj.getTotalWorldTime();

		if (var1 > 1)
		{
			this.field_82138_c -= var1 / 40.0F;

			if (this.field_82138_c < 0.0F)
			{
				this.field_82138_c = 0.0F;
			}
		}

		this.field_82138_c += 0.025F;

		if (this.field_82138_c > 1.0F)
		{
			this.field_82138_c = 1.0F;
		}

		return this.field_82138_c;
	}

	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
	{
		readFromNBT(pkt.data);
	}

	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound var1 = new NBTTagCompound();
		writeToNBT(var1);
		return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, var1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox() {
		return TileEntity.INFINITE_EXTENT_AABB;
	}

	@Override
	public int getSizeInventory() {
		return inv.length;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return inv[i];
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		ItemStack stack = getStackInSlot(i);
		if (stack != null) {
			if (stack.stackSize <= j) {
				setInventorySlotContents(i, null);
			} else {
				ItemStack stack2 = stack.splitStack(j);
				if (stack.stackSize <= 0) {
					setInventorySlotContents(i, null);
				}
				stack = stack2;
			}
		}
		return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		return null; // not needed, no items should be dropped
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		if (itemstack == null) {
			inv[i] = itemstack;
		} else if (isItemValidForSlot(i, itemstack)) {
			if (i < 5 && itemstack.stackSize > 1) {
				itemstack.stackSize = 1;
			}
			if (i > 0 && i < 5) {
				int meta = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
				inv[meta] = itemstack;
			}
		}
	}
	
	

	@Override
	public String getInvName() {
		return "Meteor Shield";
	}

	@Override
	public boolean isInvNameLocalized() {
		return false; // TODO localize later
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return true; // TODO put in some proper checks for this
	}

	@Override
	public void openChest() {}

	@Override
	public void closeChest() {}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		int meta = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
		if (itemstack.itemID == MeteorItems.itemRedMeteorGem.itemID) {
			return meta > 0 && i > 0 && i < 5;
		} else if (itemstack.itemID == MeteorItems.itemMeteorChips.itemID) {
			return i == 0 && meta == 0;
		}
		
		return false;
	}
	
	

}