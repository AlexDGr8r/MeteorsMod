package net.meteor.common.block;

import java.util.ArrayList;
import java.util.Random;

import net.meteor.common.ClientHandler;
import net.meteor.common.ClientProxy;
import net.meteor.common.HandlerAchievement;
import net.meteor.common.HandlerMeteor;
import net.meteor.common.LangLocalization;
import net.meteor.common.MeteorItems;
import net.meteor.common.MeteorsMod;
import net.meteor.common.tileentity.TileEntityMeteorShield;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMeteorShield extends BlockContainerMeteorsMod
{
	
	private IIcon topUnlit;
	private IIcon bottom;
	private IIcon gemSide;
	private IIcon noGemSide;
	private IIcon crackedSide;
	
	public BlockMeteorShield()
	{
		super(Material.rock);
		this.setLightOpacity(0);
		setTickRandomly(true);
	}

	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLiving, ItemStack itemstack)
	{
		if ((par5EntityLiving instanceof EntityPlayer)) {
			EntityPlayer player = (EntityPlayer)par5EntityLiving;
			if (!par1World.isRemote) {
				player.addChatMessage(ClientHandler.createMessage(LangLocalization.get("MeteorShield.charging"), EnumChatFormatting.YELLOW));
			}
			TileEntityMeteorShield shield = (TileEntityMeteorShield) par1World.getTileEntity(par2, par3, par4);
			shield.owner = player.getCommandSenderName();
			par1World.playSoundEffect(par2, par3, par4, "meteors:shield.humm", 1.0F, 1.0F);
		}
	}

	@Override
	public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6)
	{
		int meta = par6;
		if (!par1World.isRemote) {
			TileEntityMeteorShield shield = (TileEntityMeteorShield)par1World.getTileEntity(par2, par3, par4);
			if (MeteorsMod.proxy.metHandlers.get(par1World.provider.dimensionId).meteorShields.remove(shield)) {
				MeteorsMod.log.info("METEOR SHIELD SHOULD BE REMOVED");
			}
			par1World.playSoundEffect(par2 + 0.5D, par3 + 0.5D, par4 + 0.5D, "meteors:shield.powerdown", 1.0F, 1.0F);
		}
		super.breakBlock(par1World, par2, par3, par4, par5, par6);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public void registerBlockIcons(IIconRegister par1IconRegister) {
		this.blockIcon = par1IconRegister.registerIcon("meteors:shieldTop_lit");
	}

	@Override
	public void updateTick(World world, int i, int j, int k, Random random)
	{
		TileEntityMeteorShield shield = (TileEntityMeteorShield) world.getTileEntity(i, j, k);
		int powerLevel = shield.getPowerLevel();
		if (powerLevel <= 0) {
			shield.setCharged();
			if (!world.isRemote) {
				if (shield.owner != null && shield.owner.length() > 0) {
					EntityPlayer player = world.getPlayerEntityByName(shield.owner);
					if (player != null) {
						//player.addChatMessage(ClientHandler.createMessage(LangLocalization.get("MeteorShield.PowerUpgradePercentage") + " 20%", EnumChatFormatting.GREEN));
						player.addChatMessage(ClientHandler.createMessage(LangLocalization.get("MeteorShield.howToUpgrade"), EnumChatFormatting.GOLD));
					}
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void randomDisplayTick(World world, int i, int j, int k, Random random)
	{
		if (random.nextInt(64) == 0) { // TODO config option to turn off
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
	public int getLightValue(IBlockAccess bAccess, int x, int y, int z)
	{
		if (bAccess.getBlockMetadata(x, y, z) == 5) {
			return 15;
		}
		return super.getLightValue(bAccess, x, y, z);
	}

	@Override
	public String getLocalizedName()
	{
		return LangLocalization.get(this.getUnlocalizedName() + ".name");
	}
	
	@Override	// TODO
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
		ArrayList<ItemStack> ret = super.getDrops(world, x, y, z, metadata, fortune);
		int gems = metadata > 1 ? metadata - 1 : 0;
		for (int i = 0; i < gems; i++) {
			ret.add(new ItemStack(MeteorItems.itemRedMeteorGem, 1));
		}
		return ret;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		MeteorsMod.whatSide(FMLCommonHandler.instance().getEffectiveSide(), "new Meteor Shield");
		return new TileEntityMeteorShield();
	}
	
	/**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube() {
        return false;
    }
    
    /**
     * The type of render function that is called for this block
     */
    public int getRenderType() {
        return -1;
    }
    
    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock() {
        return false;
    }
	
}