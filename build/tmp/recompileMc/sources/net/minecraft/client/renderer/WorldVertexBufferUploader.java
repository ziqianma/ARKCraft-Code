package net.minecraft.client.renderer;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class WorldVertexBufferUploader
{
    private static final String __OBFID = "CL_00002567";

    public int draw(WorldRenderer p_178177_1_, int p_178177_2_)
    {
        if (p_178177_2_ > 0)
        {
            VertexFormat vertexformat = p_178177_1_.getVertexFormat();
            int j = vertexformat.getNextOffset();
            ByteBuffer bytebuffer = p_178177_1_.getByteBuffer();
            List list = vertexformat.getElements();
            Iterator iterator = list.iterator();
            VertexFormatElement vertexformatelement;
            VertexFormatElement.EnumUsage enumusage;
            int k;

            while (iterator.hasNext())
            {
                // moved to VertexFormatElement.preDraw
                vertexformatelement = (VertexFormatElement)iterator.next();
                vertexformatelement.getUsage().preDraw(vertexformatelement, j, bytebuffer);
            }

            GL11.glDrawArrays(p_178177_1_.getDrawMode(), 0, p_178177_1_.getVertexCount());
            iterator = list.iterator();

            while (iterator.hasNext())
            {
             // moved to VertexFormatElement.postDraw
                vertexformatelement = (VertexFormatElement)iterator.next();
                vertexformatelement.getUsage().postDraw(vertexformatelement, j, bytebuffer);
            }
        }

        p_178177_1_.reset();
        return p_178177_2_;
    }

    @SideOnly(Side.CLIENT)

    static final class SwitchEnumUsage
        {
            static final int[] VALUES = new int[VertexFormatElement.EnumUsage.values().length];
            private static final String __OBFID = "CL_00002566";

            static
            {
                try
                {
                    VALUES[VertexFormatElement.EnumUsage.POSITION.ordinal()] = 1;
                }
                catch (NoSuchFieldError var4)
                {
                    ;
                }

                try
                {
                    VALUES[VertexFormatElement.EnumUsage.UV.ordinal()] = 2;
                }
                catch (NoSuchFieldError var3)
                {
                    ;
                }

                try
                {
                    VALUES[VertexFormatElement.EnumUsage.COLOR.ordinal()] = 3;
                }
                catch (NoSuchFieldError var2)
                {
                    ;
                }

                try
                {
                    VALUES[VertexFormatElement.EnumUsage.NORMAL.ordinal()] = 4;
                }
                catch (NoSuchFieldError var1)
                {
                    ;
                }
            }
        }
}