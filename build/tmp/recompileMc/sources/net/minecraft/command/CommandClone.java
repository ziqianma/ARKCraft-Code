package net.minecraft.command;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.NextTickListEntry;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class CommandClone extends CommandBase
{
    private static final String __OBFID = "CL_00002348";

    /**
     * Get the name of the command
     */
    public String getName()
    {
        return "clone";
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
        return "commands.clone.usage";
    }

    /**
     * Called when a CommandSender executes this command
     */
    public void execute(ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 9)
        {
            throw new WrongUsageException("commands.clone.usage", new Object[0]);
        }
        else
        {
            sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
            BlockPos blockpos = func_175757_a(sender, args, 0, false);
            BlockPos blockpos1 = func_175757_a(sender, args, 3, false);
            BlockPos blockpos2 = func_175757_a(sender, args, 6, false);
            StructureBoundingBox structureboundingbox = new StructureBoundingBox(blockpos, blockpos1);
            StructureBoundingBox structureboundingbox1 = new StructureBoundingBox(blockpos2, blockpos2.add(structureboundingbox.func_175896_b()));
            int i = structureboundingbox.getXSize() * structureboundingbox.getYSize() * structureboundingbox.getZSize();

            if (i > 32768)
            {
                throw new CommandException("commands.clone.tooManyBlocks", new Object[] {Integer.valueOf(i), Integer.valueOf(32768)});
            }
            else
            {
                boolean flag = false;
                Block block = null;
                int j = -1;

                if ((args.length < 11 || !args[10].equals("force") && !args[10].equals("move")) && structureboundingbox.intersectsWith(structureboundingbox1))
                {
                    throw new CommandException("commands.clone.noOverlap", new Object[0]);
                }
                else
                {
                    if (args.length >= 11 && args[10].equals("move"))
                    {
                        flag = true;
                    }

                    if (structureboundingbox.minY >= 0 && structureboundingbox.maxY < 256 && structureboundingbox1.minY >= 0 && structureboundingbox1.maxY < 256)
                    {
                        World world = sender.getEntityWorld();

                        if (world.isAreaLoaded(structureboundingbox) && world.isAreaLoaded(structureboundingbox1))
                        {
                            boolean flag1 = false;

                            if (args.length >= 10)
                            {
                                if (args[9].equals("masked"))
                                {
                                    flag1 = true;
                                }
                                else if (args[9].equals("filtered"))
                                {
                                    if (args.length < 12)
                                    {
                                        throw new WrongUsageException("commands.clone.usage", new Object[0]);
                                    }

                                    block = getBlockByText(sender, args[11]);

                                    if (args.length >= 13)
                                    {
                                        j = parseInt(args[12], 0, 15);
                                    }
                                }
                            }

                            ArrayList arraylist = Lists.newArrayList();
                            ArrayList arraylist1 = Lists.newArrayList();
                            ArrayList arraylist2 = Lists.newArrayList();
                            LinkedList linkedlist = Lists.newLinkedList();
                            BlockPos blockpos3 = new BlockPos(structureboundingbox1.minX - structureboundingbox.minX, structureboundingbox1.minY - structureboundingbox.minY, structureboundingbox1.minZ - structureboundingbox.minZ);

                            for (int k = structureboundingbox.minZ; k <= structureboundingbox.maxZ; ++k)
                            {
                                for (int l = structureboundingbox.minY; l <= structureboundingbox.maxY; ++l)
                                {
                                    for (int i1 = structureboundingbox.minX; i1 <= structureboundingbox.maxX; ++i1)
                                    {
                                        BlockPos blockpos4 = new BlockPos(i1, l, k);
                                        BlockPos blockpos5 = blockpos4.add(blockpos3);
                                        IBlockState iblockstate = world.getBlockState(blockpos4);

                                        if ((!flag1 || iblockstate.getBlock() != Blocks.air) && (block == null || iblockstate.getBlock() == block && (j < 0 || iblockstate.getBlock().getMetaFromState(iblockstate) == j)))
                                        {
                                            TileEntity tileentity = world.getTileEntity(blockpos4);

                                            if (tileentity != null)
                                            {
                                                NBTTagCompound nbttagcompound = new NBTTagCompound();
                                                tileentity.writeToNBT(nbttagcompound);
                                                arraylist1.add(new CommandClone.StaticCloneData(blockpos5, iblockstate, nbttagcompound));
                                                linkedlist.addLast(blockpos4);
                                            }
                                            else if (!iblockstate.getBlock().isFullBlock() && !iblockstate.getBlock().isFullCube())
                                            {
                                                arraylist2.add(new CommandClone.StaticCloneData(blockpos5, iblockstate, (NBTTagCompound)null));
                                                linkedlist.addFirst(blockpos4);
                                            }
                                            else
                                            {
                                                arraylist.add(new CommandClone.StaticCloneData(blockpos5, iblockstate, (NBTTagCompound)null));
                                                linkedlist.addLast(blockpos4);
                                            }
                                        }
                                    }
                                }
                            }

                            if (flag)
                            {
                                Iterator iterator;
                                BlockPos blockpos6;

                                for (iterator = linkedlist.iterator(); iterator.hasNext(); world.setBlockState(blockpos6, Blocks.barrier.getDefaultState(), 2))
                                {
                                    blockpos6 = (BlockPos)iterator.next();
                                    TileEntity tileentity1 = world.getTileEntity(blockpos6);

                                    if (tileentity1 instanceof IInventory)
                                    {
                                        ((IInventory)tileentity1).clear();
                                    }
                                }

                                iterator = linkedlist.iterator();

                                while (iterator.hasNext())
                                {
                                    blockpos6 = (BlockPos)iterator.next();
                                    world.setBlockState(blockpos6, Blocks.air.getDefaultState(), 3);
                                }
                            }

                            ArrayList arraylist3 = Lists.newArrayList();
                            arraylist3.addAll(arraylist);
                            arraylist3.addAll(arraylist1);
                            arraylist3.addAll(arraylist2);
                            List list = Lists.reverse(arraylist3);
                            Iterator iterator1;
                            CommandClone.StaticCloneData staticclonedata;
                            TileEntity tileentity2;

                            for (iterator1 = list.iterator(); iterator1.hasNext(); world.setBlockState(staticclonedata.field_179537_a, Blocks.barrier.getDefaultState(), 2))
                            {
                                staticclonedata = (CommandClone.StaticCloneData)iterator1.next();
                                tileentity2 = world.getTileEntity(staticclonedata.field_179537_a);

                                if (tileentity2 instanceof IInventory)
                                {
                                    ((IInventory)tileentity2).clear();
                                }
                            }

                            i = 0;
                            iterator1 = arraylist3.iterator();

                            while (iterator1.hasNext())
                            {
                                staticclonedata = (CommandClone.StaticCloneData)iterator1.next();

                                if (world.setBlockState(staticclonedata.field_179537_a, staticclonedata.blockState, 2))
                                {
                                    ++i;
                                }
                            }

                            for (iterator1 = arraylist1.iterator(); iterator1.hasNext(); world.setBlockState(staticclonedata.field_179537_a, staticclonedata.blockState, 2))
                            {
                                staticclonedata = (CommandClone.StaticCloneData)iterator1.next();
                                tileentity2 = world.getTileEntity(staticclonedata.field_179537_a);

                                if (staticclonedata.field_179536_c != null && tileentity2 != null)
                                {
                                    staticclonedata.field_179536_c.setInteger("x", staticclonedata.field_179537_a.getX());
                                    staticclonedata.field_179536_c.setInteger("y", staticclonedata.field_179537_a.getY());
                                    staticclonedata.field_179536_c.setInteger("z", staticclonedata.field_179537_a.getZ());
                                    tileentity2.readFromNBT(staticclonedata.field_179536_c);
                                    tileentity2.markDirty();
                                }
                            }

                            iterator1 = list.iterator();

                            while (iterator1.hasNext())
                            {
                                staticclonedata = (CommandClone.StaticCloneData)iterator1.next();
                                world.notifyNeighborsRespectDebug(staticclonedata.field_179537_a, staticclonedata.blockState.getBlock());
                            }

                            List list1 = world.func_175712_a(structureboundingbox, false);

                            if (list1 != null)
                            {
                                Iterator iterator2 = list1.iterator();

                                while (iterator2.hasNext())
                                {
                                    NextTickListEntry nextticklistentry = (NextTickListEntry)iterator2.next();

                                    if (structureboundingbox.func_175898_b(nextticklistentry.position))
                                    {
                                        BlockPos blockpos7 = nextticklistentry.position.add(blockpos3);
                                        world.func_180497_b(blockpos7, nextticklistentry.getBlock(), (int)(nextticklistentry.scheduledTime - world.getWorldInfo().getWorldTotalTime()), nextticklistentry.priority);
                                    }
                                }
                            }

                            if (i <= 0)
                            {
                                throw new CommandException("commands.clone.failed", new Object[0]);
                            }
                            else
                            {
                                sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, i);
                                notifyOperators(sender, this, "commands.clone.success", new Object[] {Integer.valueOf(i)});
                            }
                        }
                        else
                        {
                            throw new CommandException("commands.clone.outOfWorld", new Object[0]);
                        }
                    }
                    else
                    {
                        throw new CommandException("commands.clone.outOfWorld", new Object[0]);
                    }
                }
            }
        }
    }

    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        return args.length > 0 && args.length <= 3 ? func_175771_a(args, 0, pos) : (args.length > 3 && args.length <= 6 ? func_175771_a(args, 3, pos) : (args.length > 6 && args.length <= 9 ? func_175771_a(args, 6, pos) : (args.length == 10 ? getListOfStringsMatchingLastWord(args, new String[] {"replace", "masked", "filtered"}): (args.length == 11 ? getListOfStringsMatchingLastWord(args, new String[] {"normal", "force", "move"}): (args.length == 12 && "filtered".equals(args[9]) ? func_175762_a(args, Block.blockRegistry.getKeys()) : null)))));
    }

    static class StaticCloneData
        {
            public final BlockPos field_179537_a;
            public final IBlockState blockState;
            public final NBTTagCompound field_179536_c;
            private static final String __OBFID = "CL_00002347";

            public StaticCloneData(BlockPos p_i46037_1_, IBlockState p_i46037_2_, NBTTagCompound p_i46037_3_)
            {
                this.field_179537_a = p_i46037_1_;
                this.blockState = p_i46037_2_;
                this.field_179536_c = p_i46037_3_;
            }
        }
}