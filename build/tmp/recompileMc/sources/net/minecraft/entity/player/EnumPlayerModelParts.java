package net.minecraft.entity.player;

import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public enum EnumPlayerModelParts
{
    CAPE(0, "cape"),
    JACKET(1, "jacket"),
    LEFT_SLEEVE(2, "left_sleeve"),
    RIGHT_SLEEVE(3, "right_sleeve"),
    LEFT_PANTS_LEG(4, "left_pants_leg"),
    RIGHT_PANTS_LEG(5, "right_pants_leg"),
    HAT(6, "hat");
    private final int partId;
    private final int partMask;
    private final String partName;
    private final IChatComponent field_179339_k;

    private static final String __OBFID = "CL_00002187";

    private EnumPlayerModelParts(int p_i45809_3_, String p_i45809_4_)
    {
        this.partId = p_i45809_3_;
        this.partMask = 1 << p_i45809_3_;
        this.partName = p_i45809_4_;
        this.field_179339_k = new ChatComponentTranslation("options.modelPart." + p_i45809_4_, new Object[0]);
    }

    public int getPartMask()
    {
        return this.partMask;
    }

    public int getPartId()
    {
        return this.partId;
    }

    public String getPartName()
    {
        return this.partName;
    }

    public IChatComponent func_179326_d()
    {
        return this.field_179339_k;
    }
}