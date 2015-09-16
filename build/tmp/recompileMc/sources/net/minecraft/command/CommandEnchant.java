package net.minecraft.command;

import java.util.List;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

public class CommandEnchant extends CommandBase
{
    private static final String __OBFID = "CL_00000377";

    /**
     * Get the name of the command
     */
    public String getName()
    {
        return "enchant";
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
        return "commands.enchant.usage";
    }

    /**
     * Called when a CommandSender executes this command
     */
    public void execute(ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 2)
        {
            throw new WrongUsageException("commands.enchant.usage", new Object[0]);
        }
        else
        {
            EntityPlayerMP entityplayermp = getPlayer(sender, args[0]);
            sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 0);
            int i;

            try
            {
                i = parseInt(args[1], 0);
            }
            catch (NumberInvalidException numberinvalidexception)
            {
                Enchantment enchantment = Enchantment.getEnchantmentByLocation(args[1]);

                if (enchantment == null)
                {
                    throw numberinvalidexception;
                }

                i = enchantment.effectId;
            }

            int j = 1;
            ItemStack itemstack = entityplayermp.getCurrentEquippedItem();

            if (itemstack == null)
            {
                throw new CommandException("commands.enchant.noItem", new Object[0]);
            }
            else
            {
                Enchantment enchantment1 = Enchantment.getEnchantmentById(i);

                if (enchantment1 == null)
                {
                    throw new NumberInvalidException("commands.enchant.notFound", new Object[] {Integer.valueOf(i)});
                }
                else if (!enchantment1.canApply(itemstack))
                {
                    throw new CommandException("commands.enchant.cantEnchant", new Object[0]);
                }
                else
                {
                    if (args.length >= 3)
                    {
                        j = parseInt(args[2], enchantment1.getMinLevel(), enchantment1.getMaxLevel());
                    }

                    if (itemstack.hasTagCompound())
                    {
                        NBTTagList nbttaglist = itemstack.getEnchantmentTagList();

                        if (nbttaglist != null)
                        {
                            for (int k = 0; k < nbttaglist.tagCount(); ++k)
                            {
                                short short1 = nbttaglist.getCompoundTagAt(k).getShort("id");

                                if (Enchantment.getEnchantmentById(short1) != null)
                                {
                                    Enchantment enchantment2 = Enchantment.getEnchantmentById(short1);

                                    if (!enchantment2.canApplyTogether(enchantment1) || !enchantment1.canApplyTogether(enchantment2)) //Forge BugFix: Let Both enchantments veto being together
                                    {
                                        throw new CommandException("commands.enchant.cantCombine", new Object[] {enchantment1.getTranslatedName(j), enchantment2.getTranslatedName(nbttaglist.getCompoundTagAt(k).getShort("lvl"))});
                                    }
                                }
                            }
                        }
                    }

                    itemstack.addEnchantment(enchantment1, j);
                    notifyOperators(sender, this, "commands.enchant.success", new Object[0]);
                    sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 1);
                }
            }
        }
    }

    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, this.getListOfPlayers()) : (args.length == 2 ? getListOfStringsMatchingLastWord(args, Enchantment.getLocationNames()) : null);
    }

    protected String[] getListOfPlayers()
    {
        return MinecraftServer.getServer().getAllUsernames();
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }
}