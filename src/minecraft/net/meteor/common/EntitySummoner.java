package net.meteor.common;

import java.util.List;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;

public class EntitySummoner extends EntityThrowable
implements IEntityAdditionalSpawnData
{
	public int mID;
	public boolean isRandom;

	public EntitySummoner(World world)
	{
		super(world);
	}

	public EntitySummoner(World world, EntityLiving entityliving)
	{
		super(world, entityliving);
	}

	public EntitySummoner(World world, EntityLiving entityliving, int meteorID, boolean r)
	{
		this(world, entityliving);
		this.mID = meteorID;
		this.isRandom = r;
	}

	public EntitySummoner(World world, double d, double d1, double d2)
	{
		super(world, d, d1, d2);
	}

	@Override
	protected void onImpact(MovingObjectPosition movingobjectposition)
	{
		for (int i = 0; i < 8; i++)
		{
			this.worldObj.spawnParticle("snowballpoof", posX, posY, posZ, 0.0D, 0.0D, 0.0D);
		}

		EntityPlayer player = (EntityPlayer)this.getThrower();

		if (!this.worldObj.provider.isSurfaceWorld()) {
			if ((player != null) && (!this.worldObj.isRemote)) {
				player.sendChatToPlayer(LangLocalization.get("MeteorSummoner.wrongDimension"));
				if (!player.capabilities.isCreativeMode) {
					if (this.isRandom) {
						player.inventory.addItemStackToInventory(new ItemStack(MeteorsMod.itemMeteorSummoner, 1));
					} else {
						player.inventory.addItemStackToInventory(new ItemStack(MeteorsMod.itemMeteorSummoner, 1, this.mID + 1));
					}
				}
			}

			this.setDead();
			return;
		}

		if (!this.worldObj.isRemote) {
			boolean canHit = true;
			if ((!MeteorsMod.instance.allowSummonedMeteorGrief) && (player != null)) {
				ChunkCoordIntPair cPair = this.worldObj.getChunkFromBlockCoords((int)this.posX, (int)this.posZ).getChunkCoordIntPair();
				List oPairList = MeteorsMod.proxy.meteorHandler.safeChunksWithOwners;
				for (int i = 0; i < oPairList.size(); i++) {
					SafeChunkCoordsIntPair oPair = (SafeChunkCoordsIntPair)oPairList.get(i);
					if ((oPair.hasCoords(cPair.chunkXPos, cPair.chunkZPos)) && (!player.username.equalsIgnoreCase(oPair.getOwner()))) {
						canHit = false;
						player.sendChatToPlayer(LangLocalization.get("MeteorSummoner.landProtected"));
						if (player.capabilities.isCreativeMode) break;
						if (this.isRandom) {
							player.inventory.addItemStackToInventory(new ItemStack(MeteorsMod.itemMeteorSummoner, 1)); 
							break;
						}
						player.inventory.addItemStackToInventory(new ItemStack(MeteorsMod.itemMeteorSummoner, 1, this.mID + 1)); 
						break;
					}

				}

			}

			if (canHit) {
				if (player != null) player.sendChatToPlayer("§5" + LangLocalization.get("MeteorSummoner.incomingMeteor"));
				EntityMeteor meteorToSpawn = new EntityMeteor(this.worldObj, HandlerMeteor.getMeteorSize(), this.posX, this.posZ, EnumMeteor.getTypeFromID(this.mID), true);
				this.worldObj.spawnEntityInWorld(meteorToSpawn);
			}
		}
		this.setDead();
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeEntityToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setInteger("metType", this.mID);
		par1NBTTagCompound.setBoolean("isRandom", this.isRandom);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readEntityFromNBT(par1NBTTagCompound);
		this.mID = par1NBTTagCompound.getInteger("metType");
		this.isRandom = par1NBTTagCompound.getBoolean("isRandom");
	}

	@Override
	public void writeSpawnData(ByteArrayDataOutput data)
	{
		data.writeInt(this.mID);
		data.writeBoolean(this.isRandom);
	}

	@Override
	public void readSpawnData(ByteArrayDataInput data)
	{
		this.mID = data.readInt();
		this.isRandom = data.readBoolean();
	}
}