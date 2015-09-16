package net.minecraft.client.resources.data;

import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PackMetadataSection implements IMetadataSection
{
    private final IChatComponent packDescription;
    private final int packFormat;
    private static final String __OBFID = "CL_00001112";

    public PackMetadataSection(IChatComponent p_i1034_1_, int p_i1034_2_)
    {
        this.packDescription = p_i1034_1_;
        this.packFormat = p_i1034_2_;
    }

    public IChatComponent getPackDescription()
    {
        return this.packDescription;
    }

    public int getPackFormat()
    {
        return this.packFormat;
    }
}