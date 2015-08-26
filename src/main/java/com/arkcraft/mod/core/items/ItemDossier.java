package com.arkcraft.mod.core.items;

import java.util.ArrayList;
import java.util.List;

import com.arkcraft.mod.core.Main;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

/**
 * 
 * @author Vastatio
 * Pointless to create a manager class like "ARKBook" or something, since we only have one type of dossier.
 * Tempted to make it fully to just that one dossier, but that would be messy, so I'm going to ask for the GUI-ID in the constructor.
 */
public class ItemDossier extends Item {

	private int guiID;
	private ArrayList<String> entriesToAdd = new ArrayList<String>();
	
	public ItemDossier(String name, int guiID) {
		super();
		this.guiID = guiID;
	}
	
	public void addMoreInformation(String... entries) {
		for(String e : entries) if(e!=null) entriesToAdd.add(e);
	}
	
	public int getGuiID() { return guiID; }

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		super.addInformation(stack, playerIn, tooltip, advanced);
		for(String e : entriesToAdd) tooltip.add(e);
	}

	/* Use this on the item stack's right click */
	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
		BlockPos pos = playerIn.getPosition();
		playerIn.openGui(Main.instance(), guiID, worldIn, pos.getX(), pos.getY(), pos.getZ());
		return super.onItemRightClick(itemStackIn, worldIn, playerIn);
	}
	
	
}
