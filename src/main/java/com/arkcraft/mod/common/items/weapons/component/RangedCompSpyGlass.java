package com.arkcraft.mod.common.items.weapons.component;

import java.util.List;

import org.lwjgl.input.Keyboard;

import com.arkcraft.mod.common.lib.KeyBindings;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RangedCompSpyGlass extends RangedComponent
{
	public RangedCompSpyGlass()
	{
		super(RangedSpecs.SHOTGUN);

	}
	
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
	
	@Override
	public float getMaxZoom()
	{
		return 0.50f;
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
	public void effectReloadDone(ItemStack itemstack, World world,
			EntityPlayer entityplayer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fire(ItemStack itemstack, World world,
			EntityPlayer entityplayer, int i) {
	
		return;
	}

	@Override
	public void effectPlayer(ItemStack itemstack, EntityPlayer entityplayer,
			World world) {
		// TODO Auto-generated method stub
		return;
	}

	@Override
	public void effectShoot(World world, double x, double y, double z,
			float yaw, float pitch) {
		// TODO Auto-generated method stub
		return;
	}

}
