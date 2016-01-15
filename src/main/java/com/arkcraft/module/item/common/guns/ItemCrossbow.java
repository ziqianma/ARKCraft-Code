package com.arkcraft.module.item.common.guns;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.arkcraft.module.item.common.config.ModuleItemBalance;

public class ItemCrossbow extends ItemRangedWeapon
{
	public ItemCrossbow()
	{
		super("crossbow", 250, 1);
	}

	@Override
	public int getReloadDuration()
	{
		return (int) (ModuleItemBalance.WEAPONS.CROSSBOW_RELOAD * 20.0);
	}

	@Override
	public void effectPlayer(ItemStack itemstack, EntityPlayer entityplayer, World world)
	{
		entityplayer.rotationPitch -= entityplayer.isSneaking() ? 4F : 8F;
	}

	@Override
	public void effectShoot(World world, double x, double y, double z, float yaw, float pitch)
	{
		world.playSoundEffect(x, y, z, "random.bow", 1.0F,
				1.0F / (this.getItemRand().nextFloat() * 0.4F + 0.8F));
	}

	@Override
	public void effectReloadDone(ItemStack stack, World world, EntityPlayer player)
	{
		world.playSoundAtEntity(player, "random.click", 0.8F, 1.0F / (this.getItemRand()
				.nextFloat() * 0.4F + 0.4F));
	}
}
