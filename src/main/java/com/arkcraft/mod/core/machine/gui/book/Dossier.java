package com.arkcraft.mod.core.machine.gui.book;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.arkcraft.mod.core.GlobalAdditions;
import com.arkcraft.mod.core.GlobalAdditions.GUI;
import com.arkcraft.mod.core.Main;
import com.arkcraft.mod.core.lib.LogHelper;

/**
 * 
 * @author Vastatio
 * Pointless to create a manager class like "ARKBook" or something, since we only have one type of dossier.
 * Tempted to make it fully to just that one dossier, but that would be messy, so I'm going to ask for the GUI-ID in the constructor.
 */
public class Dossier extends Item {

	private int guiID;
	private ArrayList<String> entriesToAdd = new ArrayList<String>();
	
	public Dossier(String name, int guiID) {
		super();
		this.guiID = guiID;
		this.setCreativeTab(GlobalAdditions.tabARK);
		this.setUnlocalizedName(name);
		this.setMaxStackSize(1);
		GameRegistry.registerItem(this, name);
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
	public ItemStack onItemRightClick(ItemStack stack, World worldIn, EntityPlayer playerIn) {
		playerIn.openGui(Main.instance, GUI.BOOK_GUI.getID(), worldIn, 0, 0, 0);
        FMLClientHandler.instance().displayGuiScreen(playerIn, new GuiDossier(stack, getData()));
		return super.onItemRightClick(stack, worldIn, playerIn);
	}
	
	private BookData getData() {
		LogHelper.info(DClient.dossierInfo == null ? "Dossier - dossierInfo is null!" : "Dossier - dossierInfo is not null.");
		LogHelper.info(DClient.dossier == null ?  "Dossier - the BookData from DClient is null!" : "Dossier - the BookData from DClient is not null.");
		return DClient.dossierInfo.dossier;
	}
	
}
