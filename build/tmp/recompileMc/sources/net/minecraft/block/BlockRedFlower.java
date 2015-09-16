package net.minecraft.block;

public class BlockRedFlower extends BlockFlower
{
    private static final String __OBFID = "CL_00002073";

    /**
     * Get the Type of this flower (Yellow/Red)
     */
    public BlockFlower.EnumFlowerColor getBlockType()
    {
        return BlockFlower.EnumFlowerColor.RED;
    }
}