package net.minecraft.command;

import java.util.List;
import net.minecraft.util.BlockPos;

public interface ICommand extends Comparable
{
    /**
     * Get the name of the command
     */
    String getName();

    String getCommandUsage(ICommandSender sender);

    /**
     * Gets a list of aliases for this command
     */
    List getAliases();

    /**
     * Called when a CommandSender executes this command
     */
    void execute(ICommandSender sender, String[] args) throws CommandException;

    /**
     * Returns true if the given command sender is allowed to execute this command
     */
    boolean canCommandSenderUse(ICommandSender sender);

    List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos);

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    boolean isUsernameIndex(String[] args, int index);
}