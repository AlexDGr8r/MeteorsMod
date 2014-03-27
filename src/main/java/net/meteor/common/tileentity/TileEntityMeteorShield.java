package net.meteor.common.tileentity;

import java.util.Random;

import net.meteor.common.ClientProxy;
import net.meteor.common.EnumMeteor;
import net.meteor.common.IMeteorShield;
import net.meteor.common.LangLocalization;
import net.meteor.common.MeteorItems;
import net.meteor.common.MeteorsMod;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityMeteorShield extends TileEntity implements IInventory, IMeteorShield
{
	@SideOnly(Side.CLIENT)
	private float field_82138_c;

	@SideOnly(Side.CLIENT)
	private long lastTickTime;
	private boolean shieldedChunks;
	private EnumMeteor lastStoppedMeteor = EnumMeteor.METEORITE;
	public String owner;
	public boolean renderRay = false;

	// start redoing the meteor shield
	private int range;
	private int powerLevel;

	// Inventory stuff
	private ItemStack[] inv;

	public TileEntityMeteorShield() {
		this.range = MeteorsMod.instance.ShieldRadiusMultiplier;
		this.powerLevel = 0;
		this.shieldedChunks = false;
		this.inv = new ItemStack[13];
	}

	public TileEntityMeteorShield(String theOwner) {
		this();
		this.owner = theOwner;
	}

	@Override
	public void updateEntity()
	{
		// Possible solution for moveover: powerLevel will equal 0, so remove meta dependency entirely and cause the block update
		// function to check powerLevel instead of metadata. Shields will have to charge, but should get reconfigured correctly.
		// TODO solution for red meteor gems
		if (!this.shieldedChunks) {
			if (powerLevel > 0) {
				if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
					MeteorsMod.proxy.metHandlers.get(worldObj.provider.dimensionId).addShield(this);
				}
				this.shieldedChunks = this.renderRay = true;
				this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
			} else if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
				GenerateParticles(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.worldObj.rand);
			}
		}
		if (this.shieldedChunks) {
			range = powerLevel * MeteorsMod.instance.ShieldRadiusMultiplier;	// update range (may load as 0 for old updates, thus check with this)

			if (powerLevel == 5) {
				if (this.worldObj.getTotalWorldTime() % 60L == 0L) {
					this.renderRay = this.worldObj.canBlockSeeTheSky(this.xCoord, this.yCoord + 1, this.zCoord);
				}

			}
			if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
				GenerateParticles(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.worldObj.rand);
			}
		}
	}
	
	public void setCharged() {
		if (powerLevel == 0) {
			powerLevel = 1;
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
						if (powerLevel == 5) {
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
		this.powerLevel = nbt.getInteger("powerLevel");
		this.range = MeteorsMod.instance.ShieldRadiusMultiplier * powerLevel;
		
		NBTTagList nbttaglist = nbt.getTagList("Items", 10);
		this.inv = new ItemStack[this.getSizeInventory()];
		
		for (int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 255;

            if (j >= 0 && j < this.inv.length) {
                this.inv[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setString("owner", this.owner);
		this.lastStoppedMeteor = ((EnumMeteor)MeteorsMod.proxy.lastMeteorPrevented.get(this.owner));
		if (this.lastStoppedMeteor != null) {
			nbt.setInteger("metprevent", this.lastStoppedMeteor.getID());
		} else {
			nbt.setInteger("metprevent", EnumMeteor.METEORITE.getID());
		}
		nbt.setInteger("powerLevel", powerLevel);

		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < inv.length; i++) {
			if (inv[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.inv[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
			}
		}
		
		nbt.setTag("Items", nbttaglist);
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
			inv[i] = null;
			if (i > 0 && i < 5 && powerLevel > 1) {
				this.downgradeRange(powerLevel - 1);
			}
		} else if (isItemValidForSlot(i, itemstack)) {
			if (i < 5 && itemstack.stackSize > 1) {
				itemstack.stackSize = 1;
			}
			if (i == 0) {
				this.worldObj.getBlock(xCoord, yCoord, zCoord).updateTick(worldObj, xCoord, yCoord, zCoord, worldObj.rand);
				this.worldObj.playSoundEffect(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D, "meteors:shield.powerup", 1.0F, 0.6F);
				this.markDirty();
			} else if (i > 0 && i < 5) {
				inv[i] = itemstack;
				upgradeRange(powerLevel + 1);
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
		if (itemstack.getItem() == MeteorItems.itemRedMeteorGem) {
			return powerLevel > 0 && i > 0 && i < 5 && powerLevel < 5 && inv[i] == null;
		} else if (itemstack.getItem() == MeteorItems.itemMeteorChips) {
			return i == 0 && powerLevel == 0;
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

	private void upgradeRange(int power) {
		this.powerLevel = power;
		this.range = MeteorsMod.instance.ShieldRadiusMultiplier * power;
		this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		this.worldObj.playSoundEffect(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D, "meteors:shield.powerup", 1.0F, power / 10.0F + 0.5F);

		if (MeteorsMod.instance.ShieldRadiusMultiplier <= 0 && !worldObj.isRemote) {
			EntityPlayer player = ((WorldServer)worldObj).func_73046_m().getConfigurationManager().getPlayerForUsername(owner);
			if (player != null) {
				player.addChatMessage(new ChatComponentText(LangLocalization.get("MeteorShield.noUpgrade")));
			}	
		}
		this.markDirty();
	}

	private void downgradeRange(int power) {
		this.powerLevel = power;
		this.range = MeteorsMod.instance.ShieldRadiusMultiplier * power;
		this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		// TODO sound for power down
		this.markDirty();
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

	public int getPowerLevel() {
		return this.powerLevel;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof IMeteorShield) {
			IMeteorShield shield = (IMeteorShield)o;
			return (this.getX() == shield.getX()) && (this.getY() == shield.getY()) && (this.getZ() == shield.getZ());
		}
		return super.equals(o);
	}
	
	@Override
	public void onChunkUnload() {
		
	}

}