package net.minecraft.command.server;

import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class CommandTestForBlock extends CommandBase
{
    private static final String __OBFID = "CL_00001181";

    /**
     * Get the name of the command
     */
    public String getName()
    {
        return "testforblock";
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
        return "commands.testforblock.usage";
    }

    /**
     * Called when a CommandSender executes this command
     */
    public void execute(ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 4)
        {
            throw new WrongUsageException("commands.testforblock.usage", new Object[0]);
        }
        else
        {
            sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
            BlockPos blockpos = func_175757_a(sender, args, 0, false);
            Block block = Block.getBlockFromName(args[3]);

            if (block == null)
            {
                throw new NumberInvalidException("commands.setblock.notFound", new Object[] {args[3]});
            }
            else
            {
                int i = -1;

                if (args.length >= 5)
                {
                    i = parseInt(args[4], -1, 15);
                }

                World world = sender.getEntityWorld();

                if (!world.isBlockLoaded(blockpos))
                {
                    throw new CommandException("commands.testforblock.outOfWorld", new Object[0]);
                }
                else
                {
                    NBTTagCompound nbttagcompound = new NBTTagCompound();
                    boolean flag = false;

                    if (args.length >= 6 && block.hasTileEntity(block.getStateFromMeta(i)))
                    {
                        String s = getChatComponentFromNthArg(sender, args, 5).getUnformattedText();

                        try
                        {
                            nbttagcompound = JsonToNBT.func_180713_a(s);
                            flag = true;
                        }
                        catch (NBTException nbtexception)
                        {
                            throw new CommandException("commands.setblock.tagError", new Object[] {nbtexception.getMessage()});
                        }
                    }

                    IBlockState iblockstate = world.getBlockState(blockpos);
                    Block block1 = iblockstate.getBlock();

                    if (block1 != block)
                    {
                        throw new CommandException("commands.testforblock.failed.tile", new Object[] {Integer.valueOf(blockpos.getX()), Integer.valueOf(blockpos.getY()), Integer.valueOf(blockpos.getZ()), block1.getLocalizedName(), block.getLocalizedName()});
                    }
                    else
                    {
                        if (i > -1)
                        {
                            int j = iblockstate.getBlock().getMetaFromState(iblockstate);

                            if (j != i)
                            {
                                throw new CommandException("commands.testforblock.failed.data", new Object[] {Integer.valueOf(blockpos.getX()), Integer.valueOf(blockpos.getY()), Integer.valueOf(blockpos.getZ()), Integer.valueOf(j), Integer.valueOf(i)});
                            }
                        }

                        if (flag)
                        {
                            TileEntity tileentity = world.getTileEntity(blockpos);

                            if (tileentity == null)
                            {
                                throw new CommandException("commands.testforblock.failed.tileEntity", new Object[] {Integer.valueOf(blockpos.getX()), Integer.valueOf(blockpos.getY()), Integer.valueOf(blockpos.getZ())});
                            }

                            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                            tileentity.writeToNBT(nbttagcompound1);

                            if (!func_175775_a(nbttagcompound, nbttagcompound1, true))
                            {
                                throw new CommandException("commands.testforblock.failed.nbt", new Object[] {Integer.valueOf(blockpos.getX()), Integer.valueOf(blockpos.getY()), Integer.valueOf(blockpos.getZ())});
                            }
                        }

                        sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 1);
                        notifyOperators(sender, this, "commands.testforblock.success", new Object[] {Integer.valueOf(blockpos.getX()), Integer.valueOf(blockpos.getY()), Integer.valueOf(blockpos.getZ())});
                    }
                }
            }
        }
    }

    public static boolean func_175775_a(NBTBase p_175775_0_, NBTBase p_175775_1_, boolean p_175775_2_)
    {
        if (p_175775_0_ == p_175775_1_)
        {
            return true;
        }
        else if (p_175775_0_ == null)
        {
            return true;
        }
        else if (p_175775_1_ == null)
        {
            return false;
        }
        else if (!p_175775_0_.getClass().equals(p_175775_1_.getClass()))
        {
            return false;
        }
        else if (p_175775_0_ instanceof NBTTagCompound)
        {
            NBTTagCompound nbttagcompound = (NBTTagCompound)p_175775_0_;
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)p_175775_1_;
            Iterator iterator = nbttagcompound.getKeySet().iterator();
            String s;
            NBTBase nbtbase3;

            do
            {
                if (!iterator.hasNext())
                {
                    return true;
                }

                s = (String)iterator.next();
                nbtbase3 = nbttagcompound.getTag(s);
            }
            while (func_175775_a(nbtbase3, nbttagcompound1.getTag(s), p_175775_2_));

            return false;
        }
        else if (p_175775_0_ instanceof NBTTagList && p_175775_2_)
        {
            NBTTagList nbttaglist = (NBTTagList)p_175775_0_;
            NBTTagList nbttaglist1 = (NBTTagList)p_175775_1_;

            if (nbttaglist.tagCount() == 0)
            {
                return nbttaglist1.tagCount() == 0;
            }
            else
            {
                int i = 0;

                while (i < nbttaglist.tagCount())
                {
                    NBTBase nbtbase2 = nbttaglist.get(i);
                    boolean flag1 = false;
                    int j = 0;

                    while (true)
                    {
                        if (j < nbttaglist1.tagCount())
                        {
                            if (!func_175775_a(nbtbase2, nbttaglist1.get(j), p_175775_2_))
                            {
                                ++j;
                                continue;
                            }

                            flag1 = true;
                        }

                        if (!flag1)
                        {
                            return false;
                        }

                        ++i;
                        break;
                    }
                }

                return true;
            }
        }
        else
        {
            return p_175775_0_.equals(p_175775_1_);
        }
    }

    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        return args.length > 0 && args.length <= 3 ? func_175771_a(args, 0, pos) : (args.length == 4 ? func_175762_a(args, Block.blockRegistry.getKeys()) : null);
    }
}