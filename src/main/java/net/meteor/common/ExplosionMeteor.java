package net.meteor.common;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

// It's amazing that I'll do all this for a little death message
public class ExplosionMeteor extends Explosion {
	
	public static final DamageSource meteorExplosionDamage = new DamageSource("meteor").setDifficultyScaled();
	
	private int field_77289_h = 16;
    private Random explosionRNG = new Random();
    private World worldObj;
    private Map field_77288_k = new HashMap();

	public ExplosionMeteor(World p_i1948_1_, Entity p_i1948_2_, double p_i1948_3_, double p_i1948_5_, double p_i1948_7_, float p_i1948_9_) {
		super(p_i1948_1_, p_i1948_2_, p_i1948_3_, p_i1948_5_, p_i1948_7_, p_i1948_9_);
		this.worldObj = p_i1948_1_;
	}

	// Unfortunately have to do it this way in order to get that stupid death-by information correct
	// TODO Keep an eye on the Explosion class to ensure nothing changes in each update
	@Override
	public void doExplosionA()
    {
        float f = this.explosionSize;
        HashSet hashset = new HashSet();
        int i;
        int j;
        int k;
        double d5;
        double d6;
        double d7;

        for (i = 0; i < this.field_77289_h; ++i)
        {
            for (j = 0; j < this.field_77289_h; ++j)
            {
                for (k = 0; k < this.field_77289_h; ++k)
                {
                    if (i == 0 || i == this.field_77289_h - 1 || j == 0 || j == this.field_77289_h - 1 || k == 0 || k == this.field_77289_h - 1)
                    {
                        double d0 = (double)((float)i / ((float)this.field_77289_h - 1.0F) * 2.0F - 1.0F);
                        double d1 = (double)((float)j / ((float)this.field_77289_h - 1.0F) * 2.0F - 1.0F);
                        double d2 = (double)((float)k / ((float)this.field_77289_h - 1.0F) * 2.0F - 1.0F);
                        double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                        d0 /= d3;
                        d1 /= d3;
                        d2 /= d3;
                        float f1 = this.explosionSize * (0.7F + this.worldObj.rand.nextFloat() * 0.6F);
                        d5 = this.explosionX;
                        d6 = this.explosionY;
                        d7 = this.explosionZ;

                        for (float f2 = 0.3F; f1 > 0.0F; f1 -= f2 * 0.75F)
                        {
                            int j1 = MathHelper.floor_double(d5);
                            int k1 = MathHelper.floor_double(d6);
                            int l1 = MathHelper.floor_double(d7);
                            Block block = this.worldObj.getBlock(j1, k1, l1);

                            if (block.getMaterial() != Material.air)
                            {
                                float f3 = this.exploder != null ? this.exploder.func_145772_a(this, this.worldObj, j1, k1, l1, block) : block.getExplosionResistance(this.exploder, worldObj, j1, k1, l1, explosionX, explosionY, explosionZ);
                                f1 -= (f3 + 0.3F) * f2;
                            }

                            if (f1 > 0.0F && (this.exploder == null || this.exploder.func_145774_a(this, this.worldObj, j1, k1, l1, block, f1)))
                            {
                                hashset.add(new ChunkPosition(j1, k1, l1));
                            }

                            d5 += d0 * (double)f2;
                            d6 += d1 * (double)f2;
                            d7 += d2 * (double)f2;
                        }
                    }
                }
            }
        }

        this.affectedBlockPositions.addAll(hashset);
        this.explosionSize *= 2.0F;
        i = MathHelper.floor_double(this.explosionX - (double)this.explosionSize - 1.0D);
        j = MathHelper.floor_double(this.explosionX + (double)this.explosionSize + 1.0D);
        k = MathHelper.floor_double(this.explosionY - (double)this.explosionSize - 1.0D);
        int i2 = MathHelper.floor_double(this.explosionY + (double)this.explosionSize + 1.0D);
        int l = MathHelper.floor_double(this.explosionZ - (double)this.explosionSize - 1.0D);
        int j2 = MathHelper.floor_double(this.explosionZ + (double)this.explosionSize + 1.0D);
        List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this.exploder, AxisAlignedBB.getBoundingBox((double)i, (double)k, (double)l, (double)j, (double)i2, (double)j2));
        Vec3 vec3 = Vec3.createVectorHelper(this.explosionX, this.explosionY, this.explosionZ);

        for (int i1 = 0; i1 < list.size(); ++i1)
        {
            Entity entity = (Entity)list.get(i1);
            double d4 = entity.getDistance(this.explosionX, this.explosionY, this.explosionZ) / (double)this.explosionSize;

            if (d4 <= 1.0D)
            {
                d5 = entity.posX - this.explosionX;
                d6 = entity.posY + (double)entity.getEyeHeight() - this.explosionY;
                d7 = entity.posZ - this.explosionZ;
                double d9 = (double)MathHelper.sqrt_double(d5 * d5 + d6 * d6 + d7 * d7);

                if (d9 != 0.0D)
                {
                    d5 /= d9;
                    d6 /= d9;
                    d7 /= d9;
                    double d10 = (double)this.worldObj.getBlockDensity(vec3, entity.boundingBox);
                    double d11 = (1.0D - d4) * d10;
                    entity.attackEntityFrom(meteorExplosionDamage, (float)((int)((d11 * d11 + d11) / 2.0D * 8.0D * (double)this.explosionSize + 1.0D)));
                    double d8 = EnchantmentProtection.func_92092_a(entity, d11);
                    entity.motionX += d5 * d8;
                    entity.motionY += d6 * d8;
                    entity.motionZ += d7 * d8;

                    if (entity instanceof EntityPlayer)
                    {
                        this.field_77288_k.put((EntityPlayer)entity, Vec3.createVectorHelper(d5 * d11, d6 * d11, d7 * d11));
                    }
                }
            }
        }

        this.explosionSize = f;
    }
	
}
