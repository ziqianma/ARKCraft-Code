package com.arkcraft.mod.common.items.weapons.component;

import java.util.Random;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.arkcraft.mod.common.items.weapons.handlers.IItemWeapon;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class ItemShooter extends ItemBow implements IItemWeapon
{
	protected static final int		MAX_DELAY	= 72000;
	public String setTextureName;
	
	public final RangedComponent	rangedComponent;
	
	public ItemShooter(String name, RangedComponent rangedcomponent) {
		super();
		
		rangedComponent = rangedcomponent;
		rangedcomponent.setItem(this);
		rangedcomponent.setThisItemProperties();
	}

	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers()
	{
		Multimap<String, AttributeModifier> multimap = HashMultimap.create();
		rangedComponent.addItemAttributeModifiers(multimap);
		return multimap;
	}
	
	@Override
	public boolean onLeftClickEntity(ItemStack itemstack, EntityPlayer player, Entity entity)
	{
		return rangedComponent.onLeftClickEntity(itemstack, player, entity);
	}
	
	///////
	
	@Override
	public EnumAction getItemUseAction(ItemStack itemstack)
	{
		return rangedComponent.getItemUseAction(itemstack);
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack itemstack)
	{
		return rangedComponent.getMaxItemUseDuration(itemstack);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
	{
		return rangedComponent.onItemRightClick(itemstack, world, entityplayer);
	}
	
	@Override
	public void onUsingTick(ItemStack itemstack, EntityPlayer entityplayer, int count)
	{
		rangedComponent.onUsingTick(itemstack, entityplayer, count);
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack itemstack, World world, EntityPlayer entityplayer, int i)
	{
		rangedComponent.onPlayerStoppedUsing(itemstack, world, entityplayer, i);
	}
	
	@Override
	public void onUpdate(ItemStack itemstack, World world, Entity entity, int i, boolean flag)
	{
		rangedComponent.onUpdate(itemstack, world, entity, i, flag);
	}
	
	@Override
	public boolean shouldRotateAroundWhenRendering()
	{
		return rangedComponent.shouldRotateAroundWhenRendering();
	}
	
	@Override
	public final UUID getUUID()
	{
		return itemModifierUUID;
	}
	
	@Override
	public final Random getItemRand()
	{
		return itemRand;
	}
	
	@Override
	public RangedComponent getRangedComponent()
	{
		return rangedComponent;
	}
	
//	@Override
//	@SideOnly(Side.CLIENT)
//	public void registerIcons(IIconRegister iconregister)
//	{
//		itemIcon = iconregister.registerIcon(getIconString());
//	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D()
	{
		return true;
	}
}