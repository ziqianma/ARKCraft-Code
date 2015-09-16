package net.minecraft.command;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;

public class CommandKill extends CommandBase
{
    private static final String __OBFID = "CL_00000570";

    /**
     * Get the name of the command
     */
    public String getName()
    {
        return "kill";
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
        return "commands.kill.usage";
    }

    /**
     * Called when a CommandSender executes this command
     */
    public void execute(ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length == 0)
        {
            EntityPlayerMP entityplayermp = getCommandSenderAsPlayer(sender);
            entityplayermp.onKillCommand();
            notifyOperators(sender, this, "commands.kill.successful", new Object[] {entityplayermp.getDisplayName()});
        }
        else
        {
            Entity entity = func_175768_b(sender, args[0]);
            entity.onKillCommand();
            notifyOperators(sender, this, "commands.kill.successful", new Object[] {entity.getDisplayName()});
        }
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }
}