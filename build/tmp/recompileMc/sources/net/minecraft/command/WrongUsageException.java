package net.minecraft.command;

public class WrongUsageException extends SyntaxErrorException
{
    private static final String __OBFID = "CL_00001192";

    public WrongUsageException(String message, Object ... replacements)
    {
        super(message, replacements);
    }
}