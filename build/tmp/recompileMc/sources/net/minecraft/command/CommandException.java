package net.minecraft.command;

public class CommandException extends Exception
{
    private final Object[] errorObjects;
    private static final String __OBFID = "CL_00001187";

    public CommandException(String message, Object ... objects)
    {
        super(message);
        this.errorObjects = objects;
    }

    public Object[] getErrorObjects()
    {
        return this.errorObjects;
    }
}