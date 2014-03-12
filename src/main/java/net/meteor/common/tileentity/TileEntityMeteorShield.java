package net.meteor.common.tileentity;

import java.util.Random;

import net.meteor.common.ClientProxy;
import net.meteor.common.EnumMeteor;
import net.meteor.common.IMeteorShield;
import net.meteor.common.MeteorItems;
import net.meteor.common.MeteorsMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityMeteorShield extends TileEntity implements IInventory, IMeteorShield
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
	
	// start redoing the meteor shield
	private int range;
	
	// Inventory stuff
	private ItemStack[] inv;

	public TileEntityMeteorShield() {
		this.range = MeteorsMod.instance.ShieldRadiusMultiplier;
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
					MeteorsMod.proxy.metHandlers.get(worldObj.provider.dimensionId).addShield(this);
				}
				this.shieldedChunks = this.renderRay = true;
				this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
			}
		}
		if (this.shieldedChunks) {
			int meta = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
			
			range = meta * MeteorsMod.instance.ShieldRadiusMultiplier;	// update range (may load as 0 for old updates, thus check with this)
			
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
	private void GenerateParticles(World world, int x, int y, int z, Random random)
	{
		if (world.getBlock(x, y + 1, z).isOpaqueCube()) return;
		for (int currX = x - 2; currX <= x + 2; currX++)
		{
			for (int currZ = z - 2; currZ <= z + 2; currZ++)
			{
				if ((currX > x - 2) && (currX < x + 2) && (currZ == z - 1))
				{
					currZ = z + 2;
				}
				if (random.nextInt(64) == 0)
				{
					for (int currY = y; currY <= y + 1; currY++)
					{
						if (!world.isAirBlock((currX - x) / 2 + x, currY, (currZ - z) / 2 + z))
						{
							break;
						}
						if (world.getBlockMetadata(x, y, z) == 5) {
							int id = getLastStoppedMeteor().getID();
							ClientProxy.spawnParticle("meteorshield", x + 0.5D, y + 2.0D, z + 0.5D, currX - x + random.nextFloat() - 0.5D, currY - y - random.nextFloat() - 1.0F, currZ - z + random.nextFloat() - 0.5D, world, id);
						} else {
							ClientProxy.spawnParticle("meteorshield", x + 0.5D, y + 2.0D, z + 0.5D, currX - x + random.nextFloat() - 0.5D, currY - y - random.nextFloat() - 1.0F, currZ - z + random.nextFloat() - 0.5D, world, -1);
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
		this.range = nbt.getInteger("range");
		if (range == 0) {
			range = MeteorsMod.instance.ShieldRadiusMultiplier;
		}
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
		nbt.setInteger("range", range);
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
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
	{
		readFromNBT(pkt.func_148857_g());
	}

	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound var1 = new NBTTagCompound();
		writeToNBT(var1);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, var1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox() {
		return TileEntity.INFINITE_EXTENT_AABB;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared()
    {
        return 65536.0D;
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
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return true; // TODO put in some proper checks for this
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		int meta = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
		if (itemstack.getItem() == MeteorItems.itemRedMeteorGem) {
			return meta > 0 && i > 0 && i < 5;
		} else if (itemstack.getItem() == MeteorItems.itemMeteorChips) {
			return i == 0 && meta == 0;
		}
		
		return false;
	}

	@Override
	public String getInventoryName() {
		return "Meteor Shield";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false; // TODO localize later
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}
	
	public void upgradeRange() {
		this.range += MeteorsMod.instance.ShieldRadiusMultiplier;
	}

	@Override
	public int getRange() {
		return this.range;
	}

	@Override
	public int getX() {
		return this.xCoord;
	}
	
	@Override
	public int getY() {
		return this.yCoord;
	}

	@Override
	public int getZ() {
		return this.zCoord;
	}
	
	@Override
	public boolean isTileEntity() {
		return true;
	}
	
	@Override
	public String getOwner() {
		return this.owner;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof IMeteorShield) {
			IMeteorShield shield = (IMeteorShield)o;
			return (this.getX() == shield.getX()) && (this.getY() == shield.getY()) && (this.getZ() == shield.getZ());
		}
		return false;
	}

}