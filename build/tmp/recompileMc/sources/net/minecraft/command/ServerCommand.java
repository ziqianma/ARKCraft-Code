package net.minecraft.command;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.SERVER)
public class ServerCommand
{
    /** The command string. */
    public final String input;
    public final ICommandSender sender;
    private static final String __OBFID = "CL_00001779";

    public ServerCommand(String input, ICommandSender sender)
    {
        this.input = input;
        this.sender = sender;
    }
}