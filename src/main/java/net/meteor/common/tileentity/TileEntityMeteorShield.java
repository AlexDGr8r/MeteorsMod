package net.meteor.common.tileentity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.meteor.common.ClientHandler;
import net.meteor.common.ClientProxy;
import net.meteor.common.EnumMeteor;
import net.meteor.common.IMeteorShield;
import net.meteor.common.MeteorItems;
import net.meteor.common.MeteorsMod;
import net.meteor.common.climate.HandlerMeteor;
import net.meteor.common.climate.MeteorShieldData;
import net.meteor.common.entity.EntityComet;
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
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityMeteorShield extends TileEntity implements IInventory, IMeteorShield
{

	public static final int CHARGE_TIME = 1600;

	private boolean shieldedChunks;
	public String owner;

	private int range;
	private int powerLevel;
	private int cometX;
	private int cometZ;
	private int cometType = -1;

	public int age;

	// Inventory stuff
	private ItemStack[] inv;

	public TileEntityMeteorShield() {
		this.range = 0;
		this.powerLevel = 0;
		this.age = 0;
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
		++age;
		
		if (!this.shieldedChunks) {
			if (powerLevel > 0) {
				if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
					MeteorsMod.proxy.metHandlers.get(worldObj.provider.dimensionId).getShieldManager().addShield(this);
				}
				this.shieldedChunks = true;
				this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
			} else if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
				GenerateParticles(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.worldObj.rand);
			} else if (age >= CHARGE_TIME) {
				setCharged();
				if (!worldObj.isRemote) {
					if (owner != null && owner.length() > 0) {
						EntityPlayer player = worldObj.getPlayerEntityByName(owner);
						if (player != null) {
							player.addChatMessage(ClientHandler.createMessage(StatCollector.translateToLocal("MeteorShield.howToUpgrade"), EnumChatFormatting.GOLD));
						}
					}
				}
			}
		}
		if (this.shieldedChunks) {
			range = powerLevel * MeteorsMod.instance.ShieldRadiusMultiplier;	// update range (may load as 0 for old updates, thus check with this)
		}
	}

	public void setCharged() {
		if (powerLevel == 0) {
			powerLevel = 1;
		}
	}
	
	public EnumMeteor getCometType() {
		if (cometType == -1) return EnumMeteor.METEORITE;
		return EnumMeteor.getTypeFromID(cometType);
	}

	public List<String> getDisplayInfo() {
		List<String> info = new ArrayList<String>();
		if (powerLevel == 0) {
			info.add("Charging...");
			info.add("Charged: " + (int)((float)age / (float)CHARGE_TIME * 100) + "%");
		} else {
			info.add("Power Level: " + powerLevel + " / 5");
			info.add("Range: " + range + " blocks");
		}
		info.add("Owner: " + owner);
		if (cometType != -1) {
			EnumMeteor type = EnumMeteor.getTypeFromID(cometType);
			info.add("Comet Entered Orbit at:");
			info.add("X: " + cometX);
			info.add("Z: " + cometZ);
		} else {
			info.add("No Comets Detected");
		}
		return info;
	}

	public void addMeteorMaterials(List<ItemStack> items) {
		
		for (int i = 0; i < items.size(); i++) {
			ItemStack par1ItemStack = items.get(i);
			System.out.println(par1ItemStack);
			ItemStack itemstack1;
			int k = 5;
			if (par1ItemStack.isStackable())
			{
				while (par1ItemStack.stackSize > 0 && k >= 5 && k < this.getSizeInventory())
				{
					itemstack1 = inv[k];

					if (itemstack1 != null && itemstack1.getItem() == par1ItemStack.getItem() && (!par1ItemStack.getHasSubtypes() || par1ItemStack.getItemDamage() == itemstack1.getItemDamage()) && ItemStack.areItemStackTagsEqual(par1ItemStack, itemstack1))
					{
						int l = itemstack1.stackSize + par1ItemStack.stackSize;

						if (l <= par1ItemStack.getMaxStackSize())
						{
							par1ItemStack.stackSize = 0;
							itemstack1.stackSize = l;
						}
						else if (itemstack1.stackSize < par1ItemStack.getMaxStackSize())
						{
							par1ItemStack.stackSize -= par1ItemStack.getMaxStackSize() - itemstack1.stackSize;
							itemstack1.stackSize = par1ItemStack.getMaxStackSize();
						}
					}

					++k;
				}
			}

			if (par1ItemStack.stackSize > 0)
			{
				k = 5;

				while (k >= 5 && k < this.getSizeInventory())
				{
					itemstack1 = inv[k];

					if (itemstack1 == null)
					{
						inv[k] = par1ItemStack.copy();
						par1ItemStack.stackSize = 0;
						break;
					}

					++k;
				}
			}
		}
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		this.markDirty();
	}
	
	public void detectComet(EntityComet comet) {
		this.cometX = (int)comet.posX;
		this.cometZ = (int)comet.posZ;
		this.cometType = comet.meteorType.getID();
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		this.markDirty();
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
				if (random.nextInt(100) == 25)
				{
					for (int currY = y; currY <= y + 1; currY++)
					{
						if (!world.isAirBlock((currX - x) / 2 + x, currY, (currZ - z) / 2 + z))
						{
							break;
						}
						ClientProxy.spawnParticle("meteorshield", x + 0.5D, y + 2.0D, z + 0.5D, currX - x + random.nextFloat() - 0.5D, currY - y - random.nextFloat() - 1.0F, currZ - z + random.nextFloat() - 0.5D, world, -1);
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
		if (owner == null || owner.trim().isEmpty()) {
			owner = "None";
		}
		
		this.powerLevel = nbt.getInteger("powerLevel");
		this.range = MeteorsMod.instance.ShieldRadiusMultiplier * powerLevel;
		
		if (nbt.hasKey("cometType")) {
			this.cometType = nbt.getInteger("cometType");
			this.cometX = nbt.getInteger("cometX");
			this.cometZ = nbt.getInteger("cometZ");
		}

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
		nbt.setInteger("powerLevel", powerLevel);
		if (cometType != -1) {
			nbt.setInteger("cometType", cometType);
			nbt.setInteger("cometX", cometX);
			nbt.setInteger("cometZ", cometZ);
		}

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
		return AxisAlignedBB.getAABBPool().getAABB(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1.5, zCoord + 1);
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
				this.setCharged();
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
				player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("MeteorShield.noUpgrade")));
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
		HandlerMeteor metHandler = MeteorsMod.proxy.metHandlers.get(worldObj.provider.dimensionId);
		metHandler.getShieldManager().addShield(new MeteorShieldData(xCoord, yCoord, zCoord, powerLevel, owner));
	}

}