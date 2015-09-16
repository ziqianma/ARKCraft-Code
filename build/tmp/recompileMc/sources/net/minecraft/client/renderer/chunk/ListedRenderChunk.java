package net.minecraft.client.renderer.chunk;

import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ListedRenderChunk extends RenderChunk
{
    private final int baseDisplayList = GLAllocation.generateDisplayLists(EnumWorldBlockLayer.values().length);
    private static final String __OBFID = "CL_00002453";

    public ListedRenderChunk(World worldIn, RenderGlobal p_i46198_2_, BlockPos pos, int p_i46198_4_)
    {
        super(worldIn, p_i46198_2_, pos, p_i46198_4_);
    }

    public int getDisplayList(EnumWorldBlockLayer p_178600_1_, CompiledChunk p_178600_2_)
    {
        return !p_178600_2_.isLayerEmpty(p_178600_1_) ? this.baseDisplayList + p_178600_1_.ordinal() : -1;
    }

    public void deleteGlResources()
    {
        super.deleteGlResources();
        GLAllocation.deleteDisplayLists(this.baseDisplayList, EnumWorldBlockLayer.values().length);
    }
}