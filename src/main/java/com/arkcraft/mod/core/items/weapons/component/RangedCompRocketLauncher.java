package com.arkcraft.mod.core.items.weapons.component;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.arkcraft.mod.core.items.weapons.ItemRocketLauncher;
import com.arkcraft.mod.core.items.weapons.handlers.ReloadHelper;
import com.arkcraft.mod.core.items.weapons.projectiles.EntityRocketPropelledGrenade;

public class RangedCompRocketLauncher extends RangedComponent
{
	public RangedCompRocketLauncher()
	{
		super(RangedSpecs.ROCKETLAUNCHER);
	}

	@Override
	public void effectReloadDone(ItemStack itemstack, World world, EntityPlayer entityplayer)
	{
		entityplayer.swingItem();
		world.playSoundAtEntity(entityplayer, "random.door_close", 1.2F, 1.0F / (weapon.getItemRand().nextFloat() * 0.2F + 0.0F));
	}
	
	@Override
	public void onUpdate(ItemStack itemstack, World world, Entity entity, int i, boolean flag)
	{	
		
	}
	
	@Override
	public void fire(ItemStack itemstack, World world, EntityPlayer entityplayer, int i)
	{
		
		if (!world.isRemote)
		{
			EntityRocketPropelledGrenade entityprojectile = new EntityRocketPropelledGrenade(world, entityplayer, 1F);
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
		world.playSoundEffect(x, y, z, "random.explode", 3F, 1F / (weapon.getItemRand().nextFloat() * 0.4F + 0.7F));
		world.playSoundEffect(x, y, z, "ambient.weather.thunder", 3F, 1F / (weapon.getItemRand().nextFloat() * 0.4F + 0.4F));

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
	public float getMaxZoom()
	{
		return 0.50f;
	}
}