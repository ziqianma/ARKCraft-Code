package com.arkcraft.module.item.common.items.weapons.ranged;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.arkcraft.module.item.common.config.ModuleItemBalance;
import com.arkcraft.module.item.common.items.weapons.ranged.supporting.Flashable;
import com.arkcraft.module.item.common.items.weapons.ranged.supporting.HoloScopeable;
import com.arkcraft.module.item.common.items.weapons.ranged.supporting.Laserable;
import com.arkcraft.module.item.common.items.weapons.ranged.supporting.Scopeable;
import com.arkcraft.module.item.common.items.weapons.ranged.supporting.Silenceable;

public class ItemFabricatedPistol extends ItemRangedWeapon implements Silenceable, Laserable,
		Scopeable, HoloScopeable, Flashable
{

	public ItemFabricatedPistol()
	{
		super("fabricated_pistol", 350, 13, "advanced_bullet", 1, 0.2, 6F, 1.4F);
	}

	@Override
	public int getReloadDuration()
	{
		return (int) (ModuleItemBalance.WEAPONS.FABRICATED_PISTOL_RELOAD * 20.0);
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
