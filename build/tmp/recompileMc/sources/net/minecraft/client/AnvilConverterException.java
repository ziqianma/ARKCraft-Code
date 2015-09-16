package net.minecraft.client;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class AnvilConverterException extends Exception
{
    private static final String __OBFID = "CL_00000599";

    public AnvilConverterException(String exceptionMessage)
    {
        super(exceptionMessage);
    }
}