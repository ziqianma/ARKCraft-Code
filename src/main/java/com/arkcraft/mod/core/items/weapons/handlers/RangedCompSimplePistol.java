package com.arkcraft.mod.core.items.weapons.handlers;

import com.arkcraft.mod.core.items.weapons.ItemSimplePistol;
import com.arkcraft.mod.core.items.weapons.projectiles.EntitySimpleBullet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class RangedCompSimplePistol extends RangedComponent
{
	protected ItemSimplePistol	simple_pistol;

	public RangedCompSimplePistol()
	{
		super(RangedSpecs.SIMPLEPISTOL);
	}

	@Override
	protected void onSetItem()
	{
		super.onSetItem();
		if (item instanceof ItemSimplePistol)
		{
			simple_pistol = (ItemSimplePistol) item;
		}
	}

	@Override
	public void effectReloadDone(ItemStack itemstack, World world, EntityPlayer entityplayer)
	{
		entityplayer.swingItem();
		world.playSoundAtEntity(entityplayer, "random.click", 1.0F, 1.0F / (weapon.getItemRand().nextFloat() * 0.4F + 0.8F));
	}
	
	@Override
	public void fire(ItemStack itemstack, World world, EntityPlayer entityplayer, int i)
	{
		int j = getMaxItemUseDuration(itemstack) - i;
		float f = j / 20F;
		f = (f * f + f * 2F) / 3F;
		if (f > 1.0F)
		{
			f = 1.0F;
		}
		f += 0.02F;

		if (!world.isRemote)
		{
			EntitySimpleBullet entitymusketbullet = new EntitySimpleBullet(world, entityplayer, 1F / f);
			applyProjectileEnchantments(entitymusketbullet, itemstack);
			world.spawnEntityInWorld(entitymusketbullet);
		} else
		{
			setReloadState(itemstack, ReloadHelper.STATE_NONE);
		}
		postShootingEffects(itemstack, entityplayer, world);
	}

	@Override
	public void effectPlayer(ItemStack itemstack, EntityPlayer entityplayer, World world)
	{
		float f = entityplayer.isSneaking() ? -0.05F : -0.1F;
		double d = -MathHelper.sin((entityplayer.rotationYaw / 180F) * 3.141593F) * MathHelper.cos((0 / 180F) * 3.141593F) * f;
		double d1 = MathHelper.cos((entityplayer.rotationYaw / 180F) * 3.141593F) * MathHelper.cos((0 / 180F) * 3.141593F) * f;
		entityplayer.rotationPitch -= entityplayer.isSneaking() ? 7.5F : 15F;
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
		return 0.15f;
	}
}