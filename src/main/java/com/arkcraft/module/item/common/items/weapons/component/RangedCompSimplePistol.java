package com.arkcraft.module.item.common.items.weapons.component;

import com.arkcraft.module.core.ARKCraft;
import com.arkcraft.module.item.common.entity.item.projectiles.EntitySimpleBullet;
import com.arkcraft.module.item.common.items.weapons.handlers.ReloadHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class RangedCompSimplePistol extends RangedComponent
{
	public boolean setSound;
	
    public RangedCompSimplePistol()
    {
        super(RangedSpecs.SIMPLEPISTOL);
    }

    @Override
    public void effectReloadDone(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        entityplayer.swingItem();
  //      world.playSoundAtEntity(entityplayer, ARKCraft.MODID + ":" + "simple_pistol_reload", 0.7F, 0.9F / (weapon.getItemRand().nextFloat() * 0.2F + 0.0F));
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        if (itemstack.stackSize <= 0 || entityplayer.isUsingItem())
        {
            return itemstack;
        }

        //Check can reload
        if (hasAmmo(itemstack, world, entityplayer))
        {   	
            if (isReadyToFire(itemstack))
            {
                //Start aiming weapon to fire
                soundCharge(itemstack, world, entityplayer);
                entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
            	setSound = false;

            }
            else
            {
            	setSound = true;
                //Begin reloading
                entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
                if(setSound = true)
                {
                	world.playSoundAtEntity(entityplayer, ARKCraft.MODID + ":" + "simple_pistol_reload", 1.0F, 0.9F / (weapon.getItemRand().nextFloat() * 0.2F + 0.0F));
                	setSound = false;
                }
                if (world.isRemote && !entityplayer.capabilities.isCreativeMode)
                // i.e. "20 ammo"
                {
                    entityplayer.addChatMessage(new ChatComponentText(getAmmoQuantity(entityplayer) + StatCollector.translateToLocal("chat.ammo")));
                }
            }
        }
        else
        {
            //Can't reload; no ammo
        	setSound = false;
            soundEmpty(itemstack, world, entityplayer);
            setReloadState(itemstack, ReloadHelper.STATE_NONE);
        }
        return itemstack;
    }

    @Override
    public void fire(ItemStack itemstack, World world, EntityPlayer entityplayer, int i)
    {
        int j = getMaxItemUseDuration(itemstack) - i;
        float f = j / 5F;
        f = (f * f + f * 2F) / 3F;
        if (f > 1.0F)
        {
            f = 1.0F;
        }
        f += 0.02F;

        if (!world.isRemote)
        {
            EntitySimpleBullet entityprojectile = new EntitySimpleBullet(world, entityplayer);
            applyProjectileEnchantments(entityprojectile, itemstack);
            world.spawnEntityInWorld(entityprojectile);
        }

        int damage = 1;
        if (itemstack.getItemDamage() + damage <= itemstack.getMaxDamage())
        {
            setReloadState(itemstack, ReloadHelper.STATE_NONE);
        }

        itemstack.damageItem(damage, entityplayer);
        postShootingEffects(itemstack, entityplayer, world);
    }

    @Override
    public void effectPlayer(ItemStack itemstack, EntityPlayer entityplayer, World world)
    {
        float f = entityplayer.isSneaking() ? -0.01F : -0.02F;
        double d = -MathHelper.sin((entityplayer.rotationYaw / 180F) * 3.141593F) * MathHelper.cos((0 / 180F) * 3.141593F) * f;
        double d1 = MathHelper.cos((entityplayer.rotationYaw / 180F) * 3.141593F) * MathHelper.cos((0 / 180F) * 3.141593F) * f;
        entityplayer.rotationPitch -= entityplayer.isSneaking() ? 2.5F : 5F;
        entityplayer.addVelocity(d, 0, d1);
    }

    @Override
    public void effectShoot(World world, double x, double y, double z, float yaw, float pitch)
    {
    //    world.playSoundEffect(x, y, z, "random.explode", 1.5F, 1F / (weapon.getItemRand().nextFloat() * 0.4F + 0.7F));
    //    world.playSoundEffect(x, y, z, "ambient.weather.thunder", 1.5F, 1F / (weapon.getItemRand().nextFloat() * 0.4F + 0.4F));
        world.playSoundEffect(x, y, z,  ARKCraft.MODID + ":" + "simple_pistol_shoot", 1.5F, 1F / (weapon.getItemRand().nextFloat() * 0.4F + 0.7F));

        float particleX = -MathHelper.sin(((yaw + 23) / 180F) * 3.141593F) * MathHelper.cos((pitch / 180F) * 3.141593F);
        float particleY = -MathHelper.sin((pitch / 180F) * 3.141593F) - 0.1F;
        float particleZ = MathHelper.cos(((yaw + 23) / 180F) * 3.141593F) * MathHelper.cos((pitch / 180F) * 3.141593F);

        for (int i = 0; i < 3; i++)
        {
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + particleX, y + particleY, z + particleZ, 0.0D, 0.0D, 0.0D);
        }
        world.spawnParticle(EnumParticleTypes.FLAME, x + particleX, y + particleY, z + particleZ, 0.0D, 0.0D, 0.0D);
    }

    @Override
    public boolean ifCanScope()
    {
        return false;
    }
}