package net.meteor.common;

import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.world.World;

public class EntityAlienCreeper extends EntityCreeper
{
	public EntityAlienCreeper(World world)
	{
		super(world);
		this.tasks.addTask(3, new EntityAIAvoidEntity(this, EntityCometKitty.class, 6.0F, 0.25F, 0.3F));
        texture = "/meteor/textures/meteorcreeper.png";
        isImmuneToFire = true;
	}

	@Override
	public int getMaxHealth()
	{
		return 20;
	}

	@Override
	protected int getDropItemId()
	{
		if (this.worldObj.rand.nextBoolean()) {
			return MeteorsMod.itemRedMeteorGem.itemID;
		}
		return MeteorsMod.itemMeteorChips.itemID;
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataWatcher.updateObject(17, Byte.valueOf((byte)1));
	}

	@Override
	public String getEntityName()
	{
		return LangLocalization.get("entity.AlienCreeper.name");
	}
}