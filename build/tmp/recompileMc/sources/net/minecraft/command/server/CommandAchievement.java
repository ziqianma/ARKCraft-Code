package net.minecraft.command.server;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;

public class CommandAchievement extends CommandBase
{
    private static final String __OBFID = "CL_00000113";

    /**
     * Get the name of the command
     */
    public String getName()
    {
        return "achievement";
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
        return "commands.achievement.usage";
    }

    /**
     * Called when a CommandSender executes this command
     */
    public void execute(ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 2)
        {
            throw new WrongUsageException("commands.achievement.usage", new Object[0]);
        }
        else
        {
            final StatBase statbase = StatList.getOneShotStat(args[1]);

            if (statbase == null && !args[1].equals("*"))
            {
                throw new CommandException("commands.achievement.unknownAchievement", new Object[] {args[1]});
            }
            else
            {
                final EntityPlayerMP entityplayermp = args.length >= 3 ? getPlayer(sender, args[2]) : getCommandSenderAsPlayer(sender);
                boolean flag = args[0].equalsIgnoreCase("give");
                boolean flag1 = args[0].equalsIgnoreCase("take");

                if (flag || flag1)
                {
                    if (statbase == null)
                    {
                        Iterator iterator1;
                        Achievement achievement2;

                        if (flag)
                        {
                            iterator1 = AchievementList.achievementList.iterator();

                            while (iterator1.hasNext())
                            {
                                achievement2 = (Achievement)iterator1.next();
                                entityplayermp.triggerAchievement(achievement2);
                            }

                            notifyOperators(sender, this, "commands.achievement.give.success.all", new Object[] {entityplayermp.getName()});
                        }
                        else if (flag1)
                        {
                            iterator1 = Lists.reverse(AchievementList.achievementList).iterator();

                            while (iterator1.hasNext())
                            {
                                achievement2 = (Achievement)iterator1.next();
                                entityplayermp.func_175145_a(achievement2);
                            }

                            notifyOperators(sender, this, "commands.achievement.take.success.all", new Object[] {entityplayermp.getName()});
                        }
                    }
                    else
                    {
                        if (statbase instanceof Achievement)
                        {
                            Achievement achievement = (Achievement)statbase;
                            ArrayList arraylist;
                            Iterator iterator;
                            Achievement achievement1;

                            if (flag)
                            {
                                if (entityplayermp.getStatFile().hasAchievementUnlocked(achievement))
                                {
                                    throw new CommandException("commands.achievement.alreadyHave", new Object[] {entityplayermp.getName(), statbase.func_150955_j()});
                                }

                                for (arraylist = Lists.newArrayList(); achievement.parentAchievement != null && !entityplayermp.getStatFile().hasAchievementUnlocked(achievement.parentAchievement); achievement = achievement.parentAchievement)
                                {
                                    arraylist.add(achievement.parentAchievement);
                                }

                                iterator = Lists.reverse(arraylist).iterator();

                                while (iterator.hasNext())
                                {
                                    achievement1 = (Achievement)iterator.next();
                                    entityplayermp.triggerAchievement(achievement1);
                                }
                            }
                            else if (flag1)
                            {
                                if (!entityplayermp.getStatFile().hasAchievementUnlocked(achievement))
                                {
                                    throw new CommandException("commands.achievement.dontHave", new Object[] {entityplayermp.getName(), statbase.func_150955_j()});
                                }

                                for (arraylist = Lists.newArrayList(Iterators.filter(AchievementList.achievementList.iterator(), new Predicate()
                            {
                                private static final String __OBFID = "CL_00002350";
                                    public boolean func_179605_a(Achievement p_179605_1_)
                                    {
                                        return entityplayermp.getStatFile().hasAchievementUnlocked(p_179605_1_) && p_179605_1_ != statbase;
                                    }
                                    // $FF: synthetic method
                                    public boolean apply(Object p_apply_1_)
                                    {
                                        return this.func_179605_a((Achievement)p_apply_1_);
                                    }
                                })); achievement.parentAchievement != null && entityplayermp.getStatFile().hasAchievementUnlocked(achievement.parentAchievement); achievement = achievement.parentAchievement)
                                {
                                    arraylist.remove(achievement.parentAchievement);
                                }
                                iterator = arraylist.iterator();

                                while (iterator.hasNext())
                                {
                                    achievement1 = (Achievement)iterator.next();
                                    entityplayermp.func_175145_a(achievement1);
                                }
                            }
                        }

                        if (flag)
                        {
                            entityplayermp.triggerAchievement(statbase);
                            notifyOperators(sender, this, "commands.achievement.give.success.one", new Object[] {entityplayermp.getName(), statbase.func_150955_j()});
                        }
                        else if (flag1)
                        {
                            entityplayermp.func_175145_a(statbase);
                            notifyOperators(sender, this, "commands.achievement.take.success.one", new Object[] {statbase.func_150955_j(), entityplayermp.getName()});
                        }
                    }
                }
            }
        }
    }

    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        if (args.length == 1)
        {
            /**
             * Returns a List of strings (chosen from the given strings) which the last word in the given string array
             * is a beginning-match for. (Tab completion).
             */
            return getListOfStringsMatchingLastWord(args, new String[] {"give", "take"});
        }
        else if (args.length != 2)
        {
            return args.length == 3 ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
        }
        else
        {
            ArrayList arraylist = Lists.newArrayList();
            Iterator iterator = StatList.allStats.iterator();

            while (iterator.hasNext())
            {
                StatBase statbase = (StatBase)iterator.next();
                arraylist.add(statbase.statId);
            }

            return func_175762_a(args, arraylist);
        }
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 2;
    }
}