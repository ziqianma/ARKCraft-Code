package net.minecraft.command.common;

import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class CommandReplaceItem extends CommandBase
{
    private static final Map SHORTCUTS = Maps.newHashMap();
    private static final String __OBFID = "CL_00002340";

    /**
     * Get the name of the command
     */
    public String getName()
    {
        return "replaceitem";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.replaceitem.usage";
    }

    /**
     * Called when a CommandSender executes this command
     */
    public void execute(ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 1)
        {
            throw new WrongUsageException("commands.replaceitem.usage", new Object[0]);
        }
        else
        {
            boolean flag;

            if (args[0].equals("entity"))
            {
                flag = false;
            }
            else
            {
                if (!args[0].equals("block"))
                {
                    throw new WrongUsageException("commands.replaceitem.usage", new Object[0]);
                }

                flag = true;
            }

            byte b0;

            if (flag)
            {
                if (args.length < 6)
                {
                    throw new WrongUsageException("commands.replaceitem.block.usage", new Object[0]);
                }

                b0 = 4;
            }
            else
            {
                if (args.length < 4)
                {
                    throw new WrongUsageException("commands.replaceitem.entity.usage", new Object[0]);
                }

                b0 = 2;
            }

            int l = b0 + 1;
            int i = this.getSlotForShortcut(args[b0]);
            Item item;

            try
            {
                item = getItemByText(sender, args[l]);
            }
            catch (NumberInvalidException numberinvalidexception)
            {
                if (Block.getBlockFromName(args[l]) != Blocks.air)
                {
                    throw numberinvalidexception;
                }

                item = null;
            }

            ++l;
            int j = args.length > l ? parseInt(args[l++], 1, 64) : 1;
            int k = args.length > l ? parseInt(args[l++]) : 0;
            ItemStack itemstack = new ItemStack(item, j, k);

            if (args.length > l)
            {
                String s = getChatComponentFromNthArg(sender, args, l).getUnformattedText();

                try
                {
                    itemstack.setTagCompound(JsonToNBT.func_180713_a(s));
                }
                catch (NBTException nbtexception)
                {
                    throw new CommandException("commands.replaceitem.tagError", new Object[] {nbtexception.getMessage()});
                }
            }

            if (itemstack.getItem() == null)
            {
                itemstack = null;
            }

            if (flag)
            {
                sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 0);
                BlockPos blockpos = func_175757_a(sender, args, 1, false);
                World world = sender.getEntityWorld();
                TileEntity tileentity = world.getTileEntity(blockpos);

                if (tileentity == null || !(tileentity instanceof IInventory))
                {
                    throw new CommandException("commands.replaceitem.noContainer", new Object[] {Integer.valueOf(blockpos.getX()), Integer.valueOf(blockpos.getY()), Integer.valueOf(blockpos.getZ())});
                }

                IInventory iinventory = (IInventory)tileentity;

                if (i >= 0 && i < iinventory.getSizeInventory())
                {
                    iinventory.setInventorySlotContents(i, itemstack);
                }
            }
            else
            {
                Entity entity = func_175768_b(sender, args[1]);
                sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 0);

                if (entity instanceof EntityPlayer)
                {
                    ((EntityPlayer)entity).inventoryContainer.detectAndSendChanges();
                }

                if (!entity.replaceItemInInventory(i, itemstack))
                {
                    throw new CommandException("commands.replaceitem.failed", new Object[] {Integer.valueOf(i), Integer.valueOf(j), itemstack == null ? "Air" : itemstack.getChatComponent()});
                }

                if (entity instanceof EntityPlayer)
                {
                    ((EntityPlayer)entity).inventoryContainer.detectAndSendChanges();
                }
            }

            sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, j);
            notifyOperators(sender, this, "commands.replaceitem.success", new Object[] {Integer.valueOf(i), Integer.valueOf(j), itemstack == null ? "Air" : itemstack.getChatComponent()});
        }
    }

    private int getSlotForShortcut(String shortcut) throws CommandException
    {
        if (!SHORTCUTS.containsKey(shortcut))
        {
            throw new CommandException("commands.generic.parameter.invalid", new Object[] {shortcut});
        }
        else
        {
            return ((Integer)SHORTCUTS.get(shortcut)).intValue();
        }
    }

    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, new String[] {"entity", "block"}): (args.length == 2 && args[0].equals("entity") ? getListOfStringsMatchingLastWord(args, this.getUsernames()) : ((args.length != 3 || !args[0].equals("entity")) && (args.length != 5 || !args[0].equals("block")) ? ((args.length != 4 || !args[0].equals("entity")) && (args.length != 6 || !args[0].equals("block")) ? null : func_175762_a(args, Item.itemRegistry.getKeys())) : func_175762_a(args, SHORTCUTS.keySet())));
    }

    protected String[] getUsernames()
    {
        return MinecraftServer.getServer().getAllUsernames();
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] args, int index)
    {
        return args.length > 0 && args[0].equals("entity") && index == 1;
    }

    static
    {
        int var0;

        for (var0 = 0; var0 < 54; ++var0)
        {
            SHORTCUTS.put("slot.container." + var0, Integer.valueOf(var0));
        }

        for (var0 = 0; var0 < 9; ++var0)
        {
            SHORTCUTS.put("slot.hotbar." + var0, Integer.valueOf(var0));
        }

        for (var0 = 0; var0 < 27; ++var0)
        {
            SHORTCUTS.put("slot.inventory." + var0, Integer.valueOf(9 + var0));
        }

        for (var0 = 0; var0 < 27; ++var0)
        {
            SHORTCUTS.put("slot.enderchest." + var0, Integer.valueOf(200 + var0));
        }

        for (var0 = 0; var0 < 8; ++var0)
        {
            SHORTCUTS.put("slot.villager." + var0, Integer.valueOf(300 + var0));
        }

        for (var0 = 0; var0 < 15; ++var0)
        {
            SHORTCUTS.put("slot.horse." + var0, Integer.valueOf(500 + var0));
        }

        SHORTCUTS.put("slot.weapon", Integer.valueOf(99));
        SHORTCUTS.put("slot.armor.head", Integer.valueOf(103));
        SHORTCUTS.put("slot.armor.chest", Integer.valueOf(102));
        SHORTCUTS.put("slot.armor.legs", Integer.valueOf(101));
        SHORTCUTS.put("slot.armor.feet", Integer.valueOf(100));
        SHORTCUTS.put("slot.horse.saddle", Integer.valueOf(400));
        SHORTCUTS.put("slot.horse.armor", Integer.valueOf(401));
        SHORTCUTS.put("slot.horse.chest", Integer.valueOf(499));
    }
}