package com.arkcraft.mod.common.items.weapons.component;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;

import com.arkcraft.mod.common.items.weapons.handlers.ReloadHelper;
import com.arkcraft.mod.common.items.weapons.projectiles.EntitySimpleRifleAmmo;
import com.arkcraft.mod.common.lib.KeyBindings;
import com.arkcraft.mod.common.lib.LogHelper;

public class RangedCompLongneckRifle extends RangedComponent
{
	public RangedCompLongneckRifle(EntityPlayer player, int ID)
	{
		super(RangedSpecs.LONGNECKRIFLE);

	}

	@Override
	public void effectReloadDone(ItemStack itemstack, World world, EntityPlayer entityplayer)
	{
		entityplayer.swingItem();
		world.playSoundAtEntity(entityplayer, "random.door_close", 1.2F, 1.0F / (weapon.getItemRand().nextFloat() * 0.2F + 0.0F));
	}

/*	
	@Override
	public void onUpdate(ItemStack itemstack, World world, Entity entity, int i, boolean flag)
	{
	EntityPlayer player = (EntityPlayer)entity;

	if (playerScoping() && player.getHeldItem() != null
		&& player.getHeldItem().getItem() instanceof ItemLongneckRifle && !world.isRemote)
		{
			player.openGui(ARKCraft.instance(), ID, world, (int)player.posX, (int)player.posY, (int)player.posZ);
		}
	else 
		{
			setCanScope(false);
		}
	}	*/
	
	/*
	@Override
	public void onUpdate(ItemStack itemstack, World world, Entity entity, int i, boolean flag)
	{
	EntityPlayer player = (EntityPlayer)entity;

	 if (KeyBindings.playerScoping.getKeyCode() && player.getHeldItem() != null
		&& player.getHeldItem().getItem() instanceof ItemLongneckRifle && !world.isRemote)
		{
			player.openGui(ARKCraft.instance(), ID, world, (int)player.posX, (int)player.posY, (int)player.posZ);
		}
	else 
		{
			setCanScope(false);
		}
	}	*/
	
	
    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        player.setItemInUse(itemStack, this.getMaxItemUseDuration(itemStack));
        return itemStack;
    }
    
	@Override
	public int getMaxItemUseDuration(ItemStack itemStack) 
	{
		return Integer.MAX_VALUE;
	}
		
	
	@SuppressWarnings("unchecked")
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean bool) {
        list.add(StatCollector.translateToLocal("item.zoom.binoculars.desc.1"));
        if (KeyBindings.playerScoping.getKeyCode() != 0) {
            list.add(StatCollector.translateToLocalFormatted("item.zoom.binoculars.desc.2", EnumChatFormatting.AQUA + Keyboard.getKeyName(KeyBindings.playerScoping.getKeyCode()) + EnumChatFormatting.GRAY));
        }
    }

	@Override
	public void fire(ItemStack itemstack, World world, EntityPlayer entityplayer, int i)
	{
		
		if (!world.isRemote)
		{
			EntitySimpleRifleAmmo entityprojectile = new EntitySimpleRifleAmmo(world, entityplayer, 1F);
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