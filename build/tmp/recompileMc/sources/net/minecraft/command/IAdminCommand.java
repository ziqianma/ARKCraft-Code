package net.minecraft.command;

public interface IAdminCommand
{
    void notifyOperators(ICommandSender sender, ICommand command, int p_152372_3_, String msgFormat, Object ... msgParams);
}