package net.minecraft.client.renderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

@SideOnly(Side.CLIENT)
public class GLAllocation
{
    private static final String __OBFID = "CL_00000630";

    /**
     * Generates the specified number of display lists and returns the first index.
     */
    public static synchronized int generateDisplayLists(int p_74526_0_)
    {
        int j = GL11.glGenLists(p_74526_0_);

        if (j == 0)
        {
            int k = GL11.glGetError();
            String s = "No error code reported";

            if (k != 0)
            {
                s = GLU.gluErrorString(k);
            }

            throw new IllegalStateException("glGenLists returned an ID of 0 for a count of " + p_74526_0_ + ", GL error (" + k + "): " + s);
        }
        else
        {
            return j;
        }
    }

    public static synchronized void deleteDisplayLists(int p_178874_0_, int p_178874_1_)
    {
        GL11.glDeleteLists(p_178874_0_, p_178874_1_);
    }

    public static synchronized void deleteDisplayLists(int p_74523_0_)
    {
        GL11.glDeleteLists(p_74523_0_, 1);
    }

    /**
     * Creates and returns a direct byte buffer with the specified capacity. Applies native ordering to speed up access.
     */
    public static synchronized ByteBuffer createDirectByteBuffer(int p_74524_0_)
    {
        return ByteBuffer.allocateDirect(p_74524_0_).order(ByteOrder.nativeOrder());
    }

    /**
     * Creates and returns a direct int buffer with the specified capacity. Applies native ordering to speed up access.
     */
    public static IntBuffer createDirectIntBuffer(int p_74527_0_)
    {
        /**
         * Creates and returns a direct byte buffer with the specified capacity. Applies native ordering to speed up
         * access.
         */
        return createDirectByteBuffer(p_74527_0_ << 2).asIntBuffer();
    }

    /**
     * Creates and returns a direct float buffer with the specified capacity. Applies native ordering to speed up
     * access.
     */
    public static FloatBuffer createDirectFloatBuffer(int p_74529_0_)
    {
        /**
         * Creates and returns a direct byte buffer with the specified capacity. Applies native ordering to speed up
         * access.
         */
        return createDirectByteBuffer(p_74529_0_ << 2).asFloatBuffer();
    }
}