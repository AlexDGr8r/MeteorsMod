package net.meteor.common;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityCometKitty extends EntityOcelot
{
	public EntityCometKitty(World par1World)
	{
		super(par1World);
		this.texture = "/meteor/textures/cometKitty.png";
		this.isImmuneToFire = true;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public String getTexture()
	{
		return "/meteor/textures/cometKitty.png";
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
	public int getMaxHealth()
	{
		return 16;
	}

	@Override
	public String getEntityName()
	{
		return LangLocalization.get("entity.CometKitty.name");
	}
}