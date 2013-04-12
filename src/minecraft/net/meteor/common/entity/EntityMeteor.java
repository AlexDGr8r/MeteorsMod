package net.meteor.common.entity;

import java.util.List;

import net.meteor.common.ClientHandler;
import net.meteor.common.EnumMeteor;
import net.meteor.common.HandlerAchievement;
import net.meteor.common.MeteorsMod;
import net.meteor.common.SafeChunkCoordsIntPair;
import net.meteor.common.crash.CrashFrezarite;
import net.meteor.common.crash.CrashKitty;
import net.meteor.common.crash.CrashKreknorite;
import net.meteor.common.crash.CrashMeteorite;
import net.meteor.common.crash.CrashUnknown;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityMeteor extends Entity
implements IEntityAdditionalSpawnData
{
	public int size = 1;
	public EnumMeteor meteorType;
	public int spawnPauseTicks = 0;

	protected boolean summoned = false;

	public EntityMeteor(World world) {
		super(world);
		this.preventEntitySpawning = true;
		this.setSize(0.98F, 0.98F);
		this.yOffset = (this.height / 2.0F);
		this.meteorType = EnumMeteor.METEORITE;
	}

	public EntityMeteor(World world, int mSize, double x, double z, EnumMeteor metType, boolean summon) {
		this(world);
		this.size = mSize;
		this.meteorType = metType;
		this.summoned = summon;
		this.setPosition(x, 250.0D, z);
		this.prevPosX = x;
		this.prevPosY = 250.0D;
		this.prevPosZ = z;
	}

	@Override
	protected void entityInit() {}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	public boolean canBeCollidedWith()
	{
		return !this.isDead;
	}

	@Override
	public void onUpdate()
	{
		if (!this.worldObj.provider.isSurfaceWorld()) {
			this.setDead();
			return;
		}
		if ((!this.summoned) && (MeteorInProtectedZone())) {
			List safeCoords = MeteorsMod.proxy.meteorHandler.getSafeChunkCoords((int)this.posX, (int)this.posZ);
			for (int j = 0; j < safeCoords.size(); j++) {
				SafeChunkCoordsIntPair sc = (SafeChunkCoordsIntPair)safeCoords.get(j);
				if (this.worldObj.isRemote) {
					MeteorsMod.proxy.meteorProtectCheck(sc.getOwner());
				}
				MeteorsMod.proxy.lastMeteorPrevented.put(sc.getOwner(), this.meteorType);
				ClientHandler.sendShieldProtectUpdate(sc.getOwner());
			}
			this.worldObj.playSoundEffect(posX, posY, posZ, "random.explode", 5F, (1.0F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
			this.worldObj.spawnParticle("hugeexplosion", posX, posY, posZ, 0.0D, 0.0D, 0.0D);
			this.setDead();
			return;
		}
		if (this.spawnPauseTicks > 0) {
			this.spawnPauseTicks -= 1;
			return;
		}
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		motionY -= 0.039999999105930328D;
		moveEntity(motionX, motionY, motionZ);
		motionX *= 0.98000001907348633D;
		motionY *= 0.98000001907348633D;
		motionZ *= 0.98000001907348633D;
		if (onGround) {
			setDead();
			if(!worldObj.isRemote) {
				if (!summoned) {
					AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(posX - 40D, posY - 20D, posZ - 40D, posX + 40D, posY + 20D, posZ + 40D);
					List<EntityPlayer> players = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, aabb);
					for (int i = 0; i < players.size(); i++) {
						EntityPlayer player = players.get(i);
						player.addStat(HandlerAchievement.foundMeteor, 1);
					}
				}
				CrashMeteorite worldGen = getWorldGen();
				if (worldGen.generate(worldObj, rand, (int)posX, (int)posY, (int)posZ)) {
					worldGen.afterCrashCompleted(worldObj, (int)posX, (int)posY, (int)posZ);
				}
			}
		} else {
			worldObj.spawnParticle("largesmoke", posX, posY + 2.0D, posZ, 0.0D, 0.0D, 0.0D);
			worldObj.spawnParticle("largesmoke", posX + 1.0D, posY + 2.0D, posZ, 0.0D, 0.0D, 0.0D);
			worldObj.spawnParticle("largesmoke", posX, posY + 2.0D, posZ + 1.0D, 0.0D, 0.0D, 0.0D);
			worldObj.spawnParticle("largesmoke", posX + 1.0D, posY + 2.0D, posZ + 1.0D, 0.0D, 0.0D, 0.0D);
		}
	}

	protected Explosion explode() {
		float f = 5.0F * this.size;
		return worldObj.newExplosion(null, posX, posY, posZ, f, meteorType.getFieryExplosion(), true);
	}

	protected CrashMeteorite getWorldGen() {
		switch (this.meteorType.getID()) {
		case 0:
			return new CrashMeteorite(this.size, explode(), this.meteorType);
		case 1:
			return new CrashFrezarite(this.size, explode(), this.meteorType);
		case 2:
			return new CrashKreknorite(this.size, explode(), this.meteorType);
		case 3:
			return new CrashUnknown(this.size, explode(), this.meteorType);
		case 4:
			return new CrashKitty(this.size, explode(), this.meteorType);
		}
		return new CrashMeteorite(this.size, explode(), this.meteorType);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound)
	{
		nbttagcompound.setInteger("size", this.size);
		nbttagcompound.setBoolean("summoned", this.summoned);
		nbttagcompound.setInteger("metTypeID", this.meteorType.getID());
		nbttagcompound.setInteger("pauseTicks", this.spawnPauseTicks);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound)
	{
		this.size = nbttagcompound.getInteger("size");
		this.summoned = nbttagcompound.getBoolean("summoned");
		this.meteorType = EnumMeteor.getTypeFromID(nbttagcompound.getInteger("metTypeID"));
		this.spawnPauseTicks = nbttagcompound.getInteger("pauseTicks");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public float getShadowSize()
	{
		return 0.0F;
	}

	private boolean MeteorInProtectedZone() {
		return MeteorInProtectedZone((int)this.posX, (int)this.posY);
	}

	private boolean MeteorInProtectedZone(int x, int z) {
		Chunk chunk = this.worldObj.getChunkFromBlockCoords(x, z);
		return MeteorsMod.proxy.meteorHandler.safeChunks.contains(new ChunkCoordIntPair(chunk.xPosition, chunk.zPosition));
	}

	@Override
	public void writeSpawnData(ByteArrayDataOutput data)
	{
		data.writeInt(this.meteorType.getID());
		data.writeInt(this.size);
		data.writeInt(this.spawnPauseTicks);
		data.writeBoolean(this.summoned);
	}

	@Override
	public void readSpawnData(ByteArrayDataInput data)
	{
		this.meteorType = EnumMeteor.getTypeFromID(data.readInt());
		this.size = data.readInt();
		this.spawnPauseTicks = data.readInt();
		this.summoned = data.readBoolean();
	}
}