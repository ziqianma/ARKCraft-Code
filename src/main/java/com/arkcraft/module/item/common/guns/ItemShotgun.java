package com.arkcraft.module.item.common.guns;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.arkcraft.module.core.ARKCraft;
import com.arkcraft.module.item.common.config.ModuleItemBalance;

public class ItemShotgun extends ItemRangedWeapon
{

	public ItemShotgun()
	{
		super("shotgun", 200, 5);
	}

	@Override
	public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int useRemaining)
	{
		ModelResourceLocation m = super.getModel(stack, player, useRemaining);
		if (player.isUsingItem() && this.canReload(stack)) { return new ModelResourceLocation(
				m.getResourcePath() + "_reload", m.getResourceDomain()); }
		return m;
	}

	@Override
	public int getReloadDuration()
	{
		return (int) (ModuleItemBalance.WEAPONS.SHOTGUN_RELOAD * 20.0);
	}

	@Override
	public void effectReloadDone(ItemStack stack, World world, EntityPlayer player)
	{
		super.effectReloadDone(stack, world, player);
		world.playSoundAtEntity(player, "random.door_close", 0.8F, 1.0F / (this.getItemRand()
				.nextFloat() * 0.2F + 0.0F));
	}

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

	@Override
	public void effectShoot(World world, double x, double y, double z, float yaw, float pitch)
	{

		// world.playSoundEffect(x, y, z, ARKCraft.MODID + ":" +
		// "shotgun_doubleShoot", 5.0F, 0.7F / (weapon.getItemRand().nextFloat()
		// * 0.4F + 0.6F));
		world.playSoundEffect(x, y, z, ARKCraft.MODID + ":" + "shotgun_doubleShoot", 1.5F,
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
