package com.arkcraft.module.weapon.common.item.ranged;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.arkcraft.module.crafting.common.config.ModuleItemBalance;
import com.arkcraft.module.weapon.common.entity.EntityProjectile;
import com.arkcraft.module.weapon.common.item.attachment.supporting.NonSupporting;

public class ItemShotgun extends ItemRangedWeapon implements NonSupporting
{
	public ItemShotgun()
	{
		super("shotgun", 200, 2, "simple_shotgun_ammo", 1, 0, 6F, 15F);
	}

	@Override
	public int getReloadDuration()
	{
		return (int) (ModuleItemBalance.WEAPONS.SHOTGUN_RELOAD * 20.0);
	}

	/*
	 * @Override public void effectReloadDone(ItemStack stack, World world,
	 * EntityPlayer player) { world.playSoundAtEntity(player,
	 * "random.door_close", 0.8F, 1.0F / (this.getItemRand() .nextFloat() * 0.2F
	 * + 0.0F)); }
	 */

	@Override
	public void effectPlayer(ItemStack itemstack, EntityPlayer entityplayer, World world)
	{
		float f = entityplayer.isSneaking() ? -0.1F : -0.2F;
		double d = -MathHelper.sin((entityplayer.rotationYaw / 180F) * 3.141593F) * MathHelper
				.cos((0 / 180F) * 3.141593F) * f;
		double d1 = MathHelper.cos((entityplayer.rotationYaw / 180F) * 3.141593F) * MathHelper
				.cos((0 / 180F) * 3.141593F) * f;
		entityplayer.rotationPitch -= entityplayer.isSneaking() ? 17.5F : 25F;
		entityplayer.addVelocity(d, 0, d1);
	}

	/*
	 * @Override public void soundCharge(ItemStack stack, World world,
	 * EntityPlayer player) { world.playSoundAtEntity(player, ARKCraft.MODID +
	 * ":" + "shotgun_reload", 0.7F, 0.9F / (getItemRand().nextFloat() * 0.2F +
	 * 0.0F)); }
	 */

	@Override
	public void fire(ItemStack stack, World world, EntityPlayer player, int timeLeft)
	{
		if (!world.isRemote)
		{
			for (int i = 0; i < this.getAmmoConsumption() * 10; i++)
			{
				EntityProjectile projectile = createProjectile(stack, world, player);
				if (projectile != null)
				{
					applyProjectileEnchantments(projectile, stack);
					world.spawnEntityInWorld(projectile);
				}
			}
		}
		afterFire(stack, world, player);
	}
}
