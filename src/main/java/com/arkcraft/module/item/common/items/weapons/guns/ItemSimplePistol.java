package com.arkcraft.module.item.common.items.weapons.guns;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.arkcraft.module.core.ARKCraft;
import com.arkcraft.module.item.common.config.ModuleItemBalance;

public class ItemSimplePistol extends ItemRangedWeapon
{
	public ItemSimplePistol()
	{
		super("simple_pistol", 150, 6, "simple_bullet", 1, 1 / 2.1, 5F, 2.5F);
	}

	@Override
	public void soundCharge(ItemStack stack, World world, EntityPlayer player)
	{
		world.playSoundAtEntity(player, ARKCraft.MODID + ":" + "simple_pistol_reload", 0.7F,
				0.9F / (getItemRand().nextFloat() * 0.2F + 0.0F));
	}

	@Override
	public int getReloadDuration()
	{
		return (int) (ModuleItemBalance.WEAPONS.SIMPLE_PISTOL_RELOAD * 20.0);
	}

	@Override
	public void effectPlayer(ItemStack itemstack, EntityPlayer entityplayer, World world)
	{
		float f = entityplayer.isSneaking() ? -0.01F : -0.02F;
		double d = -MathHelper.sin((entityplayer.rotationYaw / 180F) * 3.141593F) * MathHelper
				.cos((0 / 180F) * 3.141593F) * f;
		double d1 = MathHelper.cos((entityplayer.rotationYaw / 180F) * 3.141593F) * MathHelper
				.cos((0 / 180F) * 3.141593F) * f;
		entityplayer.rotationPitch -= entityplayer.isSneaking() ? 2.5F : 5F;
		entityplayer.addVelocity(d, 0, d1);
	}

	@Override
	public void effectShoot(World world, double x, double y, double z, float yaw, float pitch)
	{
		// world.playSoundEffect(x, y, z, "random.explode", 1.5F, 1F /
		// (weapon.getItemRand().nextFloat() * 0.4F + 0.7F));
		// world.playSoundEffect(x, y, z, "ambient.weather.thunder", 1.5F, 1F /
		// (weapon.getItemRand().nextFloat() * 0.4F + 0.4F));
		world.playSoundEffect(x, y, z, ARKCraft.MODID + ":" + "simple_pistol_shoot", 1.5F,
				1F / (this.getItemRand().nextFloat() * 0.4F + 0.7F));

		float particleX = -MathHelper.sin(((yaw + 23) / 180F) * 3.141593F) * MathHelper
				.cos((pitch / 180F) * 3.141593F);
		float particleY = -MathHelper.sin((pitch / 180F) * 3.141593F) - 0.1F;
		float particleZ = MathHelper.cos(((yaw + 23) / 180F) * 3.141593F) * MathHelper
				.cos((pitch / 180F) * 3.141593F);

		for (int i = 0; i < 3; i++)
		{
			world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + particleX, y + particleY,
					z + particleZ, 0.0D, 0.0D, 0.0D);
		}
		world.spawnParticle(EnumParticleTypes.FLAME, x + particleX, y + particleY, z + particleZ,
				0.0D, 0.0D, 0.0D);
	}
}
