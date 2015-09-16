package net.minecraft.command;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class CommandFill extends CommandBase
{
    private static final String __OBFID = "CL_00002342";

    /**
     * Get the name of the command
     */
    public String getName()
    {
        return "fill";
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
        return "commands.fill.usage";
    }

    /**
     * Called when a CommandSender executes this command
     */
    public void execute(ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 7)
        {
            throw new WrongUsageException("commands.fill.usage", new Object[0]);
        }
        else
        {
            sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
            BlockPos blockpos = func_175757_a(sender, args, 0, false);
            BlockPos blockpos1 = func_175757_a(sender, args, 3, false);
            Block block = CommandBase.getBlockByText(sender, args[6]);
            int i = 0;

            if (args.length >= 8)
            {
                i = parseInt(args[7], 0, 15);
            }
            IBlockState state = block.getStateFromMeta(i);

            BlockPos blockpos2 = new BlockPos(Math.min(blockpos.getX(), blockpos1.getX()), Math.min(blockpos.getY(), blockpos1.getY()), Math.min(blockpos.getZ(), blockpos1.getZ()));
            BlockPos blockpos3 = new BlockPos(Math.max(blockpos.getX(), blockpos1.getX()), Math.max(blockpos.getY(), blockpos1.getY()), Math.max(blockpos.getZ(), blockpos1.getZ()));
            int j = (blockpos3.getX() - blockpos2.getX() + 1) * (blockpos3.getY() - blockpos2.getY() + 1) * (blockpos3.getZ() - blockpos2.getZ() + 1);

            if (j > 32768)
            {
                throw new CommandException("commands.fill.tooManyBlocks", new Object[] {Integer.valueOf(j), Integer.valueOf(32768)});
            }
            else if (blockpos2.getY() >= 0 && blockpos3.getY() < 256)
            {
                World world = sender.getEntityWorld();

                for (int k = blockpos2.getZ(); k < blockpos3.getZ() + 16; k += 16)
                {
                    for (int l = blockpos2.getX(); l < blockpos3.getX() + 16; l += 16)
                    {
                        if (!world.isBlockLoaded(new BlockPos(l, blockpos3.getY() - blockpos2.getY(), k)))
                        {
                            throw new CommandException("commands.fill.outOfWorld", new Object[0]);
                        }
                    }
                }

                NBTTagCompound nbttagcompound = new NBTTagCompound();
                boolean flag = false;

                if (args.length >= 10 && block.hasTileEntity(state))
                {
                    String s = getChatComponentFromNthArg(sender, args, 9).getUnformattedText();

                    try
                    {
                        nbttagcompound = JsonToNBT.func_180713_a(s);
                        flag = true;
                    }
                    catch (NBTException nbtexception)
                    {
                        throw new CommandException("commands.fill.tagError", new Object[] {nbtexception.getMessage()});
                    }
                }

                ArrayList arraylist = Lists.newArrayList();
                j = 0;

                for (int i1 = blockpos2.getZ(); i1 <= blockpos3.getZ(); ++i1)
                {
                    for (int j1 = blockpos2.getY(); j1 <= blockpos3.getY(); ++j1)
                    {
                        for (int k1 = blockpos2.getX(); k1 <= blockpos3.getX(); ++k1)
                        {
                            BlockPos blockpos4 = new BlockPos(k1, j1, i1);
                            IBlockState iblockstate;

                            if (args.length >= 9)
                            {
                                if (!args[8].equals("outline") && !args[8].equals("hollow"))
                                {
                                    if (args[8].equals("destroy"))
                                    {
                                        world.destroyBlock(blockpos4, true);
                                    }
                                    else if (args[8].equals("keep"))
                                    {
                                        if (!world.isAirBlock(blockpos4))
                                        {
                                            continue;
                                        }
                                    }
                                    else if (args[8].equals("replace") && !block.hasTileEntity(state))
                                    {
                                        if (args.length > 9)
                                        {
                                            Block block1 = CommandBase.getBlockByText(sender, args[9]);

                                            if (world.getBlockState(blockpos4).getBlock() != block1)
                                            {
                                                continue;
                                            }
                                        }

                                        if (args.length > 10)
                                        {
                                            int l1 = CommandBase.parseInt(args[10]);
                                            iblockstate = world.getBlockState(blockpos4);

                                            if (iblockstate.getBlock().getMetaFromState(iblockstate) != l1)
                                            {
                                                continue;
                                            }
                                        }
                                    }
                                }
                                else if (k1 != blockpos2.getX() && k1 != blockpos3.getX() && j1 != blockpos2.getY() && j1 != blockpos3.getY() && i1 != blockpos2.getZ() && i1 != blockpos3.getZ())
                                {
                                    if (args[8].equals("hollow"))
                                    {
                                        world.setBlockState(blockpos4, Blocks.air.getDefaultState(), 2);
                                        arraylist.add(blockpos4);
                                    }

                                    continue;
                                }
                            }

                            TileEntity tileentity1 = world.getTileEntity(blockpos4);

                            if (tileentity1 != null)
                            {
                                if (tileentity1 instanceof IInventory)
                                {
                                    ((IInventory)tileentity1).clear();
                                }

                                world.setBlockState(blockpos4, Blocks.barrier.getDefaultState(), block == Blocks.barrier ? 2 : 4);
                            }

                            iblockstate = block.getStateFromMeta(i);

                            if (world.setBlockState(blockpos4, iblockstate, 2))
                            {
                                arraylist.add(blockpos4);
                                ++j;

                                if (flag)
                                {
                                    TileEntity tileentity = world.getTileEntity(blockpos4);

                                    if (tileentity != null)
                                    {
                                        nbttagcompound.setInteger("x", blockpos4.getX());
                                        nbttagcompound.setInteger("y", blockpos4.getY());
                                        nbttagcompound.setInteger("z", blockpos4.getZ());
                                        tileentity.readFromNBT(nbttagcompound);
                                    }
                                }
                            }
                        }
                    }
                }

                Iterator iterator = arraylist.iterator();

                while (iterator.hasNext())
                {
                    BlockPos blockpos5 = (BlockPos)iterator.next();
                    Block block2 = world.getBlockState(blockpos5).getBlock();
                    world.notifyNeighborsRespectDebug(blockpos5, block2);
                }

                if (j <= 0)
                {
                    throw new CommandException("commands.fill.failed", new Object[0]);
                }
                else
                {
                    sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, j);
                    notifyOperators(sender, this, "commands.fill.success", new Object[] {Integer.valueOf(j)});
                }
            }
            else
            {
                throw new CommandException("commands.fill.outOfWorld", new Object[0]);
            }
        }
    }

    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        return args.length > 0 && args.length <= 3 ? func_175771_a(args, 0, pos) : (args.length > 3 && args.length <= 6 ? func_175771_a(args, 3, pos) : (args.length == 7 ? func_175762_a(args, Block.blockRegistry.getKeys()) : (args.length == 9 ? getListOfStringsMatchingLastWord(args, new String[] {"replace", "destroy", "keep", "hollow", "outline"}): null)));
    }
}