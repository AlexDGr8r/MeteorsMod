package net.meteor.common.entity;

import java.util.List;

import net.meteor.common.ClientHandler;
import net.meteor.common.EnumMeteor;
import net.meteor.common.HandlerAchievement;
import net.meteor.common.HandlerMeteor;
import net.meteor.common.LangLocalization;
import net.meteor.common.MeteorsMod;
import net.meteor.common.SafeChunkCoordsIntPair;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;

public class EntitySummoner extends EntityThrowable implements IEntityAdditionalSpawnData
{
	private static final float[][] spellRGB = { 
		{0.4352941176470588F, 0.0784313725490196F, 0.6588235294117647F}, 
		{0.0588235294117647F, 0.6784313725490196F, 0.6784313725490196F}, 
		{0.6941176470588235F, 0.0470588235294118F, 0.0470588235294118F}, 
		{0.392156862745098F, 0.3725490196078431F, 0.3450980392156863F}, 
		{0.0941176470588235F, 0.6470588235294118F, 0.0941176470588235F} };
	
	public int mID;
	public boolean isRandom;
	
	private EntityLiving thrower;
    private String throwerName = null;

	public EntitySummoner(World world)
	{
		super(world);
	}

	public EntitySummoner(World world, EntityLivingBase entityliving)
	{
		super(world, entityliving);
	}

	public EntitySummoner(World world, EntityLivingBase entityliving, int meteorID, boolean r)
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
	public void onUpdate()
    {
		super.onUpdate();
		if (this.worldObj.isRemote) {
			int rgbIndex = mID;
			if (isRandom) {
				rgbIndex = this.worldObj.rand.nextInt(5);
			}
			worldObj.spawnParticle("mobSpell", this.posX, this.posY, this.posZ, spellRGB[rgbIndex][0], spellRGB[rgbIndex][1], spellRGB[rgbIndex][2]);
		}
    }

	@Override
	protected void onImpact(MovingObjectPosition movingobjectposition)
	{
		for (int i = 0; i < 8; i++)
		{
			this.worldObj.spawnParticle("snowballpoof", posX, posY, posZ, 0.0D, 0.0D, 0.0D);
		}

		EntityPlayer player = (EntityPlayer)this.getThrower();

		if (this.worldObj.provider.dimensionId != 0) {
			if ((player != null) && (!this.worldObj.isRemote)) {
				player.addChatMessage(LangLocalization.get("MeteorSummoner.wrongDimension"));
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
			
			if (!worldObj.getGameRules().getGameRuleBooleanValue("summonMeteors")) {
				canHit = false;
				player.sendChatToPlayer(ClientHandler.createMessage(LangLocalization.get("MeteorSummoner.cannotSummon"), EnumChatFormatting.RED));
				if (!player.capabilities.isCreativeMode) {
					if (this.isRandom) {
						player.inventory.addItemStackToInventory(new ItemStack(MeteorsMod.itemMeteorSummoner, 1));
					} else {
						player.inventory.addItemStackToInventory(new ItemStack(MeteorsMod.itemMeteorSummoner, 1, this.mID + 1));
					}
				}
			} else if ((!MeteorsMod.instance.allowSummonedMeteorGrief) && (player != null)) {
				ChunkCoordIntPair cPair = this.worldObj.getChunkFromBlockCoords((int)this.posX, (int)this.posZ).getChunkCoordIntPair();
				List oPairList = MeteorsMod.proxy.metHandlers.get(worldObj.provider.dimensionId).safeChunksWithOwners;
				for (int i = 0; i < oPairList.size(); i++) {
					SafeChunkCoordsIntPair oPair = (SafeChunkCoordsIntPair)oPairList.get(i);
					if ((oPair.hasCoords(cPair.chunkXPos, cPair.chunkZPos)) && (!player.username.equalsIgnoreCase(oPair.getOwner()))) {
						canHit = false;
						player.addChatMessage(LangLocalization.get("MeteorSummoner.landProtected"));
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
				if (player != null) {
					player.sendChatToPlayer(ClientHandler.createMessage(LangLocalization.get("MeteorSummoner.incomingMeteor"), EnumChatFormatting.LIGHT_PURPLE));
					player.triggerAchievement(HandlerAchievement.summonMeteor);
				}
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