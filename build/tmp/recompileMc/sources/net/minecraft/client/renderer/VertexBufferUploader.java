package net.minecraft.client.renderer;

import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class VertexBufferUploader extends WorldVertexBufferUploader
{
    private VertexBuffer vertexBuffer = null;
    private static final String __OBFID = "CL_00002532";

    public int draw(WorldRenderer p_178177_1_, int p_178177_2_)
    {
        p_178177_1_.reset();
        this.vertexBuffer.bufferData(p_178177_1_.getByteBuffer(), p_178177_1_.getByteIndex());
        return p_178177_2_;
    }

    public void setVertexBuffer(VertexBuffer p_178178_1_)
    {
        this.vertexBuffer = p_178178_1_;
    }
}