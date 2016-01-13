package com.arkcraft.module.item.common.entity.item.projectiles;

import com.arkcraft.module.item.common.items.ARKCraftItems;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityBallistaBolt extends EntityProjectile
{
    public EntityBallistaBolt(World world)
    {
        super(world);
    }

    public EntityBallistaBolt(World world, double d, double d1, double d2)
    {
        this(world);
        setPosition(d, d1, d2);
    }

    public EntityBallistaBolt(World world, EntityBallista entityballista, boolean superPowered)
    {
        this(world);
        shootingEntity = entityballista.riddenByEntity;

        setSize(0.5F, 0.5F);
        setLocationAndAngles(entityballista.posX, entityballista.posY + 1.0D, entityballista.posZ, entityballista.riddenByEntity.rotationYaw, entityballista.riddenByEntity.rotationPitch);
        posX -= MathHelper.cos((rotationYaw / 180F) * 3.141593F) * 0.16F;
        posY -= 0.1D;
        posZ -= MathHelper.sin((rotationYaw / 180F) * 3.141593F) * 0.16F;
        motionX = -MathHelper.sin((rotationYaw / 180F) * 3.141593F) * MathHelper.cos((rotationPitch / 180F) * 3.141593F);
        motionZ = MathHelper.cos((rotationYaw / 180F) * 3.141593F) * MathHelper.cos((rotationPitch / 180F) * 3.141593F);
        motionY = -MathHelper.sin((rotationPitch / 180F) * 3.141593F);
        posX += motionX * 1.2F;
        posY += motionY * 1.2F;
        posZ += motionZ * 1.2F;
        setPosition(posX, posY, posZ);
        setIsCritical(superPowered);
        setThrowableHeading(motionX, motionY, motionZ, superPowered ? 4.0F : 2.0F, superPowered ? 0.1F : 2.0F);
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        double speed = Math.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
        double amount = 8D;
        if (speed > 1.0D)
        {
            for (int i1 = 1; i1 < amount; i1++)
            {
                worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, posX + (motionX * i1) / amount, posY + (motionY * i1) / amount, posZ + (motionZ * i1) / amount, 0.0D, 0.0D, 0.0D);
            }
        }
    }
    /*
	@Override
	public void onEntityHit(Entity entity)
	{
		DamageSource damagesource = null;
		if (shootingEntity == null)
		{
			damagesource = WeaponDamageSource.causeThrownDamage(this, this);
		} else
		{
			damagesource = WeaponDamageSource.causeThrownDamage(this, shootingEntity);
		}
		if (entity.attackEntityFrom(damagesource, 30))
		{
			worldObj.playSoundAtEntity(this, "random.damage.hurtflesh", 1.0F, 1.2F / (rand.nextFloat() * 0.4F + 0.7F));
		}
	}	*/

    @Override
    public void onEntityHit(Entity entity)
    {
        this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 4F, true);
        this.setDead();
    }

    @Override
    public void onGroundHit(MovingObjectPosition movingobjectposition)
    {
        this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, posX, posY, posZ, 0.0D, 0.0D, 0.0D);
        this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 4F, true);
        this.setDead();
    }

    @Override
    public float getAirResistance()
    {
        return 0.98F;
    }

    @Override
    public float getGravity()
    {
        return 0.04F;
    }

    @Override
    public ItemStack getPickupItem()
    {
        return new ItemStack(ARKCraftItems.ballista_bolt, 1);
    }
}