package net.meteor.common.block;

import java.util.Random;

import net.meteor.common.ClientHandler;
import net.meteor.common.MeteorsMod;
import net.meteor.common.tileentity.TileEntityMeteorShield;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMeteorShield extends BlockContainerMeteorsMod
{
	
	public BlockMeteorShield()
	{
		super(Material.rock);
		this.setLightOpacity(0);
		this.setBlockBounds(0.0625F, 0.375F, 0.0625F, 0.9375F, 1.0F, 0.9375F);
	}

	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLiving, ItemStack itemstack)
	{
		if ((par5EntityLiving instanceof EntityPlayer)) {
			EntityPlayer player = (EntityPlayer)par5EntityLiving;
			if (!par1World.isRemote) {
				player.addChatMessage(ClientHandler.createMessage(StatCollector.translateToLocal("MeteorShield.charging"), EnumChatFormatting.YELLOW));
			}
			TileEntityMeteorShield shield = (TileEntityMeteorShield) par1World.getTileEntity(par2, par3, par4);
			shield.owner = player.getCommandSenderName();
			par1World.playSoundEffect(par2, par3, par4, "meteors:shield.humm", 1.0F, 1.0F);
		}
		
		int l = MathHelper.floor_double((double)(par5EntityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (l == 0)
        {
        	par1World.setBlockMetadataWithNotify(par2, par3, par4, 1, 2);
        }

        if (l == 1)
        {
        	par1World.setBlockMetadataWithNotify(par2, par3, par4, 0, 2);
        }

        if (l == 2)
        {
        	par1World.setBlockMetadataWithNotify(par2, par3, par4, 3, 2);
        }

        if (l == 3)
        {
        	par1World.setBlockMetadataWithNotify(par2, par3, par4, 2, 2);
        }
        
        super.onBlockPlacedBy(par1World, par2, par3, par4, par5EntityLiving, itemstack);
	}

	@Override
	public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6)
	{
		TileEntityMeteorShield shield = (TileEntityMeteorShield)par1World.getTileEntity(par2, par3, par4);
		if (!par1World.isRemote) {
			if (MeteorsMod.proxy.metHandlers.get(par1World.provider.dimensionId).getShieldManager().meteorShields.remove(shield)) {
				//MeteorsMod.log.info("METEOR SHIELD SHOULD BE REMOVED");
			}
			par1World.playSoundEffect(par2 + 0.5D, par3 + 0.5D, par4 + 0.5D, "meteors:shield.powerdown", 1.0F, 1.0F);
		}
		
		if (shield != null)
        {
            for (int i1 = 0; i1 < shield.getSizeInventory(); ++i1)
            {
                ItemStack itemstack = shield.getStackInSlot(i1);

                if (itemstack != null)
                {
                    float f = par1World.rand.nextFloat() * 0.8F + 0.1F;
                    float f1 = par1World.rand.nextFloat() * 0.8F + 0.1F;
                    EntityItem entityitem;

                    for (float f2 = par1World.rand.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; par1World.spawnEntityInWorld(entityitem))
                    {
                        int j1 = par1World.rand.nextInt(21) + 10;

                        if (j1 > itemstack.stackSize)
                        {
                            j1 = itemstack.stackSize;
                        }

                        itemstack.stackSize -= j1;
                        entityitem = new EntityItem(par1World, (double)((float)par2 + f), (double)((float)par3 + f1), (double)((float)par4 + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));
                        float f3 = 0.05F;
                        entityitem.motionX = (double)((float)par1World.rand.nextGaussian() * f3);
                        entityitem.motionY = (double)((float)par1World.rand.nextGaussian() * f3 + 0.2F);
                        entityitem.motionZ = (double)((float)par1World.rand.nextGaussian() * f3);

                        if (itemstack.hasTagCompound())
                        {
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                        }
                    }
                }
            }
            shield.invalidate();
        }
		
		super.breakBlock(par1World, par2, par3, par4, par5, par6);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void randomDisplayTick(World world, int i, int j, int k, Random random)
	{
		if (MeteorsMod.instance.meteorShieldSound && random.nextInt(256) == 100) {
			world.playSound(i + 0.5D, j + 0.5D, k + 0.5D, "meteors:shield.humm", 0.6F, 1.0F, false);
		}
	}

	@Override
	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int par6, float par7, float par8, float par9)
	{
		player.openGui(MeteorsMod.instance, 0, world, i, j, k);
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileEntityMeteorShield();
	}
	
	/**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
	@Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    /**
     * The type of render function that is called for this block
     */
	@Override
    public int getRenderType() {
        return -1;
    }
    
    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
	@Override
    public boolean renderAsNormalBlock() {
        return false;
    }
	
}