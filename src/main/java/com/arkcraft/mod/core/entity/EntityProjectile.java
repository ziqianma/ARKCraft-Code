package com.arkcraft.mod.core.entity;

import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IThrowableEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class EntityProjectile extends EntityArrow implements IThrowableEntity
{
	protected int			xTile;
	protected int			yTile;
	protected int			zTile;
	protected Block			inTile;
	protected int			inData;
	protected boolean		inGround;
	public int				pickupMode;
	protected int			ticksInGround;
	protected int			ticksInAir;
	public boolean			beenInGround;
	public float			extraDamage;
	public int				knockBack;
	
	public EntityProjectile(World world)
	{
		super(world);
		xTile = -1;
		yTile = -1;
		zTile = -1;
		inTile = null;
		inData = 0;
		inGround = false;
		arrowShake = 0;
		ticksInAir = 0;
		extraDamage = 0;
		knockBack = 0;
		
		setSize(0.5F, 0.5F);
	}
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
	}
	
	@Override
	public Entity getThrower()
	{
		return shootingEntity;
	}
	
	@Override
	public void setThrower(Entity entity)
	{
		shootingEntity = entity;
	}
	

	@Override
	public void setThrowableHeading(double x, double y, double z, float speed, float deviation)
	{
		float f2 = MathHelper.sqrt_double(x * x + y * y + z * z);
		x /= f2;
		y /= f2;
		z /= f2;
		x += rand.nextGaussian() * 0.0075F * deviation;
		y += rand.nextGaussian() * 0.0075F * deviation;
		z += rand.nextGaussian() * 0.0075F * deviation;
		x *= speed;
		y *= speed;
		z *= speed;
		motionX = x;
		motionY = y;
		motionZ = z;
		float f3 = MathHelper.sqrt_double(x * x + z * z);
		prevRotationYaw = rotationYaw = (float) ((Math.atan2(x, z) * 180D) / Math.PI);
		prevRotationPitch = rotationPitch = (float) ((Math.atan2(y, f3) * 180D) / Math.PI);
		ticksInGround = 0;
	}
	
	@Override
	public void setVelocity(double d, double d1, double d2)
	{
		motionX = d;
		motionY = d1;
		motionZ = d2;
		if (aimRotation() && prevRotationPitch == 0.0F && prevRotationYaw == 0.0F)
		{
			float f = MathHelper.sqrt_double(d * d + d2 * d2);
			prevRotationYaw = rotationYaw = (float) ((Math.atan2(d, d2) * 180D) / Math.PI);
			prevRotationPitch = rotationPitch = (float) ((Math.atan2(d1, f) * 180D) / Math.PI);
			setLocationAndAngles(posX, posY, posZ, rotationYaw, rotationPitch);
			ticksInGround = 0;
		}
	}
	
	@Override
	public void onUpdate()
	{
		onEntityUpdate();
	}
	
	
	
	public void onEntityHit(Entity entity)
	{
		bounceBack();
		applyEntityHitEffects(entity);
	}
	
	public void applyEntityHitEffects(Entity entity)
	{
		if (isBurning() && !(entity instanceof EntityEnderman))
		{
			entity.setFire(5);
		}
		if (entity instanceof EntityLivingBase)
		{
			EntityLivingBase entityliving = (EntityLivingBase) entity;
			if (knockBack > 0)
			{
				float f = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
				if (f > 0.0F)
				{
					entity.addVelocity(motionX * knockBack * 0.6D / f, 0.1D, motionZ * knockBack * 0.6D / f);
				}
			}
			if (shootingEntity instanceof EntityLivingBase)
			{
				EnchantmentHelper.func_151384_a(entityliving, this.shootingEntity);
				EnchantmentHelper.func_151385_b((EntityLivingBase) this.shootingEntity, entityliving);
			}
			if (shootingEntity instanceof EntityPlayerMP && shootingEntity != entity && entity instanceof EntityPlayer)
			{
				((EntityPlayerMP) shootingEntity).playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(6, 0));
			}
		}
	}

	protected void bounceBack()
	{
		motionX *= -0.1D;
		motionY *= -0.1D;
		motionZ *= -0.1D;
		rotationYaw += 180F;
		prevRotationYaw += 180F;
		ticksInAir = 0;
	}
	
	public final double getTotalVelocity()
	{
		return Math.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
	}
	
	public boolean aimRotation()
	{
		return true;
	}
	
	public int getMaxLifetime()
	{
		return 1200;
	}
	
	public ItemStack getPickupItem()
	{
		return null;
	}
	
	public float getAirResistance()
	{
		return 0.99F;
	}
	
	public float getGravity()
	{
		return 0.05F;
	}
	
	public int getMaxArrowShake()
	{
		return 7;
	}
	
	public void playHitSound()
	{
	}
	
	public boolean canBeCritical()
	{
		return false;
	}
	
	@Override
	public void setIsCritical(boolean flag)
	{
		if (canBeCritical())
		{
			dataWatcher.updateObject(16, Byte.valueOf((byte) (flag ? 1 : 0)));
		}
	}
	
	@Override
	public boolean getIsCritical()
	{
		return canBeCritical() && dataWatcher.getWatchableObjectByte(16) != 0;
	}
	
	public void setExtraDamage(float f)
	{
		extraDamage = f;
	}
	
	@Override
	public void setKnockbackStrength(int i)
	{
		knockBack = i;
	}
	
	public void setPickupMode(int i)
	{
		pickupMode = i;
	}
	
	public int getPickupMode()
	{
		return pickupMode;
	}
	
	protected void onItemPickup(EntityPlayer entityplayer)
	{
		entityplayer.onItemPickup(this, 1);
	}
	
	@SideOnly(Side.CLIENT)
	public float getShadowSize()
	{
		return 0.0F;
	}
	
	@Override
	protected boolean canTriggerWalking()
	{
		return false;
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound)
	{
		nbttagcompound.setShort("xTile", (short) xTile);
		nbttagcompound.setShort("yTile", (short) yTile);
		nbttagcompound.setShort("zTile", (short) zTile);
		nbttagcompound.setByte("inTile", (byte) Block.getIdFromBlock(inTile));
		nbttagcompound.setByte("inData", (byte) inData);
		nbttagcompound.setByte("shake", (byte) arrowShake);
		nbttagcompound.setBoolean("inGround", inGround);
		nbttagcompound.setBoolean("beenInGround", beenInGround);
		nbttagcompound.setByte("pickup", (byte) pickupMode);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound)
	{
		xTile = nbttagcompound.getShort("xTile");
		yTile = nbttagcompound.getShort("yTile");
		zTile = nbttagcompound.getShort("zTile");
		inTile = Block.getBlockById(nbttagcompound.getByte("inTile") & 0xFF);
		inData = nbttagcompound.getByte("inData") & 0xFF;
		arrowShake = nbttagcompound.getByte("shake") & 0xFF;
		inGround = nbttagcompound.getBoolean("inGround");
		beenInGround = nbttagcompound.getBoolean("beenInGrond");
		pickupMode = nbttagcompound.getByte("pickup");
	}
}