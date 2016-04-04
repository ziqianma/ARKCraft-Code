package com.arkcraft.module.core.client.gui.book;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.arkcraft.module.core.ARKCraft;
import com.arkcraft.module.core.GlobalAdditions;
import com.arkcraft.module.core.client.gui.book.proxy.DClient;

/**
 * @author Vastatio
 */
public class Dossier extends Item
{
    public Dossier(String name)
    {
        super();
        setUnlocalizedName(name);
        setCreativeTab(GlobalAdditions.tabARKMisc);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if (world.isRemote)
        {
            openBook(stack, world, player);
        }
        return stack;
    }

    @SideOnly(Side.CLIENT)
    public void openBook(ItemStack stack, World world, EntityPlayer player)
    {
        player.openGui(ARKCraft.instance, GlobalAdditions.GUI.BOOK_GUI.getID(), world, 0, 0, 0);
        FMLClientHandler.instance().displayGuiScreen(player, new GuiDossier(stack, DClient.info.data));
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
    {
        list.add(DClient.info.data.tooltip);
    }


}
