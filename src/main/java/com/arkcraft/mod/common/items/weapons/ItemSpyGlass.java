package com.arkcraft.mod.common.items.weapons;

import java.util.List;

import org.lwjgl.input.Keyboard;

import com.arkcraft.mod.GlobalAdditions;
import com.arkcraft.mod.common.items.weapons.component.ItemShooter;
import com.arkcraft.mod.common.items.weapons.component.RangedComponent;
import com.arkcraft.mod.common.lib.KeyBindings;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSpyGlass extends ItemShooter{

	public ItemSpyGlass(String name, RangedComponent rangedcomponent)
	{
		super(name, rangedcomponent);
    	this.setUnlocalizedName(name);
		this.setCreativeTab(GlobalAdditions.tabARK);
		this.setMaxStackSize(1);
		GameRegistry.registerItem(this, this.getUnlocalizedName().substring(5));
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
		
	
	@SuppressWarnings("unchecked")
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean bool) {
        list.add(StatCollector.translateToLocal("item.zoom.binoculars.desc.1"));
        if (KeyBindings.playerScoping.getKeyCode() != 0) {
            list.add(StatCollector.translateToLocalFormatted("item.zoom.binoculars.desc.2", EnumChatFormatting.AQUA + Keyboard.getKeyName(KeyBindings.playerScoping.getKeyCode()) + EnumChatFormatting.GRAY));
        }
    }
}
