package com.arkcraft.module.weapon.common.item.ranged;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.arkcraft.module.blocks.common.config.ModuleItemBalance;
import com.arkcraft.module.weapon.common.item.attachment.supporting.Flashable;
import com.arkcraft.module.weapon.common.item.attachment.supporting.Laserable;
import com.arkcraft.module.weapon.common.item.attachment.supporting.Scopeable;
import com.arkcraft.module.weapon.common.item.attachment.supporting.Silenceable;

public class ItemSimplePistol extends ItemRangedWeapon implements Scopeable, Laserable, Flashable,
		Silenceable
{
	public ItemSimplePistol()
	{
		super("simple_pistol", 150, 6, "simple_bullet", 1, 1 / 2.1, 5F, 2.5F);
	}

	/*
	 * @Override public void soundCharge(ItemStack stack, World world,
	 * EntityPlayer player) { world.playSoundAtEntity(player, ARKCraft.MODID +
	 * ":" + "simple_pistol_reload", 0.7F, 0.9F / (getItemRand().nextFloat() *
	 * 0.2F + 0.0F)); }
	 */

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
}
