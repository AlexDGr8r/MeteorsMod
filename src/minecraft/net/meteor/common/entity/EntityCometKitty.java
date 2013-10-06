package net.meteor.common.entity;

import net.meteor.common.HandlerAchievement;
import net.meteor.common.LangLocalization;
import net.meteor.common.MeteorsMod;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityCometKitty extends EntityOcelot
{
	public EntityCometKitty(World par1World)
	{
		super(par1World);
		this.isImmuneToFire = true;
	}

	@Override
	protected int getDropItemId()
	{
		return MeteorsMod.itemRedMeteorGem.itemID;
	}

	@Override
	public EntityOcelot spawnBabyAnimal(EntityAgeable par1EntityAgeable)
	{
		EntityOcelot var2;
		if (this.worldObj.rand.nextBoolean()) {
			var2 = new EntityOcelot(this.worldObj);
		} else {
			var2 = new EntityCometKitty(this.worldObj);
		}

		if (this.isTamed())
		{
			var2.setOwner(this.getOwnerName());
			var2.setTamed(true);
			var2.setTameSkin(this.getTameSkin());
		}

		return var2;
	}

	@Override
	protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(16.0D);
    }

	@Override
	public String getEntityName()
	{
		return LangLocalization.get("entity.CometKitty.name");
	}
	
	@Override
	public void setOwner(String par1Str) {
		super.setOwner(par1Str);
		EntityPlayer player = this.worldObj.getPlayerEntityByName(par1Str);
		if (player != null) {
			player.addStat(HandlerAchievement.kittyTame, 1);
		}
	}
	
}