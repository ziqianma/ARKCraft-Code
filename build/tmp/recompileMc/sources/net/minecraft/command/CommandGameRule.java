package net.minecraft.command;

import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.GameRules;

public class CommandGameRule extends CommandBase
{
    private static final String __OBFID = "CL_00000475";

    /**
     * Get the name of the command
     */
    public String getName()
    {
        return "gamerule";
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
        return "commands.gamerule.usage";
    }

    /**
     * Called when a CommandSender executes this command
     */
    public void execute(ICommandSender sender, String[] args) throws CommandException
    {
        GameRules gamerules = this.getGameRules();
        String s = args.length > 0 ? args[0] : "";
        String s1 = args.length > 1 ? func_180529_a(args, 1) : "";

        switch (args.length)
        {
            case 0:
                sender.addChatMessage(new ChatComponentText(joinNiceString(gamerules.getRules())));
                break;
            case 1:
                if (!gamerules.hasRule(s))
                {
                    throw new CommandException("commands.gamerule.norule", new Object[] {s});
                }

                String s2 = gamerules.getGameRuleStringValue(s);
                sender.addChatMessage((new ChatComponentText(s)).appendText(" = ").appendText(s2));
                sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, gamerules.getInt(s));
                break;
            default:
                if (gamerules.areSameType(s, GameRules.ValueType.BOOLEAN_VALUE) && !"true".equals(s1) && !"false".equals(s1))
                {
                    throw new CommandException("commands.generic.boolean.invalid", new Object[] {s1});
                }

                gamerules.setOrCreateGameRule(s, s1);
                func_175773_a(gamerules, s);
                notifyOperators(sender, this, "commands.gamerule.success", new Object[0]);
        }
    }

    public static void func_175773_a(GameRules p_175773_0_, String p_175773_1_)
    {
        if ("reducedDebugInfo".equals(p_175773_1_))
        {
            int i = p_175773_0_.getGameRuleBooleanValue(p_175773_1_) ? 22 : 23;
            Iterator iterator = MinecraftServer.getServer().getConfigurationManager().playerEntityList.iterator();

            while (iterator.hasNext())
            {
                EntityPlayerMP entityplayermp = (EntityPlayerMP)iterator.next();
                entityplayermp.playerNetServerHandler.sendPacket(new S19PacketEntityStatus(entityplayermp, (byte)i));
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
            return getListOfStringsMatchingLastWord(args, this.getGameRules().getRules());
        }
        else
        {
            if (args.length == 2)
            {
                GameRules gamerules = this.getGameRules();

                if (gamerules.areSameType(args[0], GameRules.ValueType.BOOLEAN_VALUE))
                {
                    /**
                     * Returns a List of strings (chosen from the given strings) which the last word in the given string
                     * array is a beginning-match for. (Tab completion).
                     */
                    return getListOfStringsMatchingLastWord(args, new String[] {"true", "false"});
                }
            }

            return null;
        }
    }

    /**
     * Return the game rule set this command should be able to manipulate.
     */
    private GameRules getGameRules()
    {
        return MinecraftServer.getServer().worldServerForDimension(0).getGameRules();
    }
}