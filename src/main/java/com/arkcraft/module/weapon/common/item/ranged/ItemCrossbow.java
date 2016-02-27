package com.arkcraft.module.weapon.common.item.ranged;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.arkcraft.module.crafting.common.config.ModuleItemBalance;

public class ItemCrossbow extends ItemRangedWeapon
{
	public ItemCrossbow()
	{
		super("crossbow", 250, 1, "stone_arrow", 1, 2, 1.5F, 2F);
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
	public void effectShoot(ItemStack stack, World world, double x, double y, double z, float yaw, float pitch)
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
