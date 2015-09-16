package net.minecraft.client.renderer.texture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class TextureUtil
{
    private static final Logger logger = LogManager.getLogger();
    private static final IntBuffer dataBuffer = GLAllocation.createDirectIntBuffer(4194304);
    public static final DynamicTexture missingTexture = new DynamicTexture(16, 16);
    public static final int[] missingTextureData = missingTexture.getTextureData();
    private static final int[] mipmapBuffer;
    private static final String __OBFID = "CL_00001067";

    public static int glGenTextures()
    {
        return GlStateManager.generateTexture();
    }

    public static void deleteTexture(int textureId)
    {
        GlStateManager.deleteTexture(textureId);
    }

    public static int uploadTextureImage(int p_110987_0_, BufferedImage p_110987_1_)
    {
        return uploadTextureImageAllocate(p_110987_0_, p_110987_1_, false, false);
    }

    public static void uploadTexture(int textureId, int[] p_110988_1_, int p_110988_2_, int p_110988_3_)
    {
        bindTexture(textureId);
        uploadTextureSub(0, p_110988_1_, p_110988_2_, p_110988_3_, 0, 0, false, false, false);
    }

    public static int[][] generateMipmapData(int p_147949_0_, int p_147949_1_, int[][] p_147949_2_)
    {
        int[][] aint1 = new int[p_147949_0_ + 1][];
        aint1[0] = p_147949_2_[0];

        if (p_147949_0_ > 0)
        {
            boolean flag = false;
            int k;

            for (k = 0; k < p_147949_2_.length; ++k)
            {
                if (p_147949_2_[0][k] >> 24 == 0)
                {
                    flag = true;
                    break;
                }
            }

            for (k = 1; k <= p_147949_0_; ++k)
            {
                if (p_147949_2_[k] != null)
                {
                    aint1[k] = p_147949_2_[k];
                }
                else
                {
                    int[] aint2 = aint1[k - 1];
                    int[] aint3 = new int[aint2.length >> 2];
                    int l = p_147949_1_ >> k;
                    int i1 = aint3.length / l;
                    int j1 = l << 1;

                    for (int k1 = 0; k1 < l; ++k1)
                    {
                        for (int l1 = 0; l1 < i1; ++l1)
                        {
                            int i2 = 2 * (k1 + l1 * j1);
                            aint3[k1 + l1 * l] = blendColors(aint2[i2 + 0], aint2[i2 + 1], aint2[i2 + 0 + j1], aint2[i2 + 1 + j1], flag);
                        }
                    }

                    aint1[k] = aint3;
                }
            }
        }

        return aint1;
    }

    private static int blendColors(int p_147943_0_, int p_147943_1_, int p_147943_2_, int p_147943_3_, boolean p_147943_4_)
    {
        if (!p_147943_4_)
        {
            int i2 = blendColorComponent(p_147943_0_, p_147943_1_, p_147943_2_, p_147943_3_, 24);
            int j2 = blendColorComponent(p_147943_0_, p_147943_1_, p_147943_2_, p_147943_3_, 16);
            int k2 = blendColorComponent(p_147943_0_, p_147943_1_, p_147943_2_, p_147943_3_, 8);
            int l2 = blendColorComponent(p_147943_0_, p_147943_1_, p_147943_2_, p_147943_3_, 0);
            return i2 << 24 | j2 << 16 | k2 << 8 | l2;
        }
        else
        {
            mipmapBuffer[0] = p_147943_0_;
            mipmapBuffer[1] = p_147943_1_;
            mipmapBuffer[2] = p_147943_2_;
            mipmapBuffer[3] = p_147943_3_;
            float f = 0.0F;
            float f1 = 0.0F;
            float f2 = 0.0F;
            float f3 = 0.0F;
            int i1;

            for (i1 = 0; i1 < 4; ++i1)
            {
                if (mipmapBuffer[i1] >> 24 != 0)
                {
                    f += (float)Math.pow((double)((float)(mipmapBuffer[i1] >> 24 & 255) / 255.0F), 2.2D);
                    f1 += (float)Math.pow((double)((float)(mipmapBuffer[i1] >> 16 & 255) / 255.0F), 2.2D);
                    f2 += (float)Math.pow((double)((float)(mipmapBuffer[i1] >> 8 & 255) / 255.0F), 2.2D);
                    f3 += (float)Math.pow((double)((float)(mipmapBuffer[i1] >> 0 & 255) / 255.0F), 2.2D);
                }
            }

            f /= 4.0F;
            f1 /= 4.0F;
            f2 /= 4.0F;
            f3 /= 4.0F;
            i1 = (int)(Math.pow((double)f, 0.45454545454545453D) * 255.0D);
            int j1 = (int)(Math.pow((double)f1, 0.45454545454545453D) * 255.0D);
            int k1 = (int)(Math.pow((double)f2, 0.45454545454545453D) * 255.0D);
            int l1 = (int)(Math.pow((double)f3, 0.45454545454545453D) * 255.0D);

            if (i1 < 96)
            {
                i1 = 0;
            }

            return i1 << 24 | j1 << 16 | k1 << 8 | l1;
        }
    }

    private static int blendColorComponent(int p_147944_0_, int p_147944_1_, int p_147944_2_, int p_147944_3_, int p_147944_4_)
    {
        float f = (float)Math.pow((double)((float)(p_147944_0_ >> p_147944_4_ & 255) / 255.0F), 2.2D);
        float f1 = (float)Math.pow((double)((float)(p_147944_1_ >> p_147944_4_ & 255) / 255.0F), 2.2D);
        float f2 = (float)Math.pow((double)((float)(p_147944_2_ >> p_147944_4_ & 255) / 255.0F), 2.2D);
        float f3 = (float)Math.pow((double)((float)(p_147944_3_ >> p_147944_4_ & 255) / 255.0F), 2.2D);
        float f4 = (float)Math.pow((double)(f + f1 + f2 + f3) * 0.25D, 0.45454545454545453D);
        return (int)((double)f4 * 255.0D);
    }

    public static void uploadTextureMipmap(int[][] p_147955_0_, int p_147955_1_, int p_147955_2_, int p_147955_3_, int p_147955_4_, boolean p_147955_5_, boolean p_147955_6_)
    {
        for (int i1 = 0; i1 < p_147955_0_.length; ++i1)
        {
            int[] aint1 = p_147955_0_[i1];
            uploadTextureSub(i1, aint1, p_147955_1_ >> i1, p_147955_2_ >> i1, p_147955_3_ >> i1, p_147955_4_ >> i1, p_147955_5_, p_147955_6_, p_147955_0_.length > 1);
        }
    }

    private static void uploadTextureSub(int p_147947_0_, int[] p_147947_1_, int p_147947_2_, int p_147947_3_, int p_147947_4_, int p_147947_5_, boolean p_147947_6_, boolean p_147947_7_, boolean p_147947_8_)
    {
        int j1 = 4194304 / p_147947_2_;
        setTextureBlurMipmap(p_147947_6_, p_147947_8_);
        setTextureClamped(p_147947_7_);
        int i2;

        for (int k1 = 0; k1 < p_147947_2_ * p_147947_3_; k1 += p_147947_2_ * i2)
        {
            int l1 = k1 / p_147947_2_;
            i2 = Math.min(j1, p_147947_3_ - l1);
            int j2 = p_147947_2_ * i2;
            copyToBufferPos(p_147947_1_, k1, j2);
            GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, p_147947_0_, p_147947_4_, p_147947_5_ + l1, p_147947_2_, i2, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, dataBuffer);
        }
    }

    public static int uploadTextureImageAllocate(int p_110989_0_, BufferedImage p_110989_1_, boolean p_110989_2_, boolean p_110989_3_)
    {
        allocateTexture(p_110989_0_, p_110989_1_.getWidth(), p_110989_1_.getHeight());
        return uploadTextureImageSub(p_110989_0_, p_110989_1_, 0, 0, p_110989_2_, p_110989_3_);
    }

    public static void allocateTexture(int p_110991_0_, int p_110991_1_, int p_110991_2_)
    {
        allocateTextureImpl(p_110991_0_, 0, p_110991_1_, p_110991_2_);
    }

    public static void allocateTextureImpl(int p_180600_0_, int p_180600_1_, int p_180600_2_, int p_180600_3_)
    {
        synchronized (net.minecraftforge.fml.client.SplashProgress.class)
        {
        deleteTexture(p_180600_0_);
        bindTexture(p_180600_0_);
        }
        if (p_180600_1_ >= 0)
        {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL, p_180600_1_);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MIN_LOD, 0.0F);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LOD, (float)p_180600_1_);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, 0.0F);
        }

        for (int i1 = 0; i1 <= p_180600_1_; ++i1)
        {
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, i1, GL11.GL_RGBA, p_180600_2_ >> i1, p_180600_3_ >> i1, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, (IntBuffer)null);
        }
    }

    public static int uploadTextureImageSub(int textureId, BufferedImage p_110995_1_, int p_110995_2_, int p_110995_3_, boolean p_110995_4_, boolean p_110995_5_)
    {
        bindTexture(textureId);
        uploadTextureImageSubImpl(p_110995_1_, p_110995_2_, p_110995_3_, p_110995_4_, p_110995_5_);
        return textureId;
    }

    private static void uploadTextureImageSubImpl(BufferedImage p_110993_0_, int p_110993_1_, int p_110993_2_, boolean p_110993_3_, boolean p_110993_4_)
    {
        int k = p_110993_0_.getWidth();
        int l = p_110993_0_.getHeight();
        int i1 = 4194304 / k;
        int[] aint = new int[i1 * k];
        setTextureBlurred(p_110993_3_);
        setTextureClamped(p_110993_4_);

        for (int j1 = 0; j1 < k * l; j1 += k * i1)
        {
            int k1 = j1 / k;
            int l1 = Math.min(i1, l - k1);
            int i2 = k * l1;
            p_110993_0_.getRGB(0, k1, k, l1, aint, 0, k);
            copyToBuffer(aint, i2);
            GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, p_110993_1_, p_110993_2_ + k1, k, l1, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, dataBuffer);
        }
    }

    private static void setTextureClamped(boolean p_110997_0_)
    {
        if (p_110997_0_)
        {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
        }
        else
        {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        }
    }

    private static void setTextureBlurred(boolean p_147951_0_)
    {
        setTextureBlurMipmap(p_147951_0_, false);
    }

    private static void setTextureBlurMipmap(boolean p_147954_0_, boolean p_147954_1_)
    {
        if (p_147954_0_)
        {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, p_147954_1_ ? GL11.GL_LINEAR_MIPMAP_LINEAR : GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        }
        else
        {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, p_147954_1_ ? GL11.GL_NEAREST_MIPMAP_LINEAR : GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        }
    }

    private static void copyToBuffer(int[] p_110990_0_, int p_110990_1_)
    {
        copyToBufferPos(p_110990_0_, 0, p_110990_1_);
    }

    private static void copyToBufferPos(int[] p_110994_0_, int p_110994_1_, int p_110994_2_)
    {
        int[] aint1 = p_110994_0_;

        if (Minecraft.getMinecraft().gameSettings.anaglyph)
        {
            aint1 = updateAnaglyph(p_110994_0_);
        }

        dataBuffer.clear();
        dataBuffer.put(aint1, p_110994_1_, p_110994_2_);
        dataBuffer.position(0).limit(p_110994_2_);
    }

    static void bindTexture(int p_94277_0_)
    {
        GlStateManager.bindTexture(p_94277_0_);
    }

    public static int[] readImageData(IResourceManager resourceManager, ResourceLocation imageLocation) throws IOException
    {
        BufferedImage bufferedimage = readBufferedImage(resourceManager.getResource(imageLocation).getInputStream());
        int i = bufferedimage.getWidth();
        int j = bufferedimage.getHeight();
        int[] aint = new int[i * j];
        bufferedimage.getRGB(0, 0, i, j, aint, 0, i);
        return aint;
    }

    public static BufferedImage readBufferedImage(InputStream imageStream) throws IOException
    {
        BufferedImage bufferedimage;

        try
        {
            bufferedimage = ImageIO.read(imageStream);
        }
        finally
        {
            IOUtils.closeQuietly(imageStream);
        }

        return bufferedimage;
    }

    public static int[] updateAnaglyph(int[] p_110985_0_)
    {
        int[] aint1 = new int[p_110985_0_.length];

        for (int i = 0; i < p_110985_0_.length; ++i)
        {
            aint1[i] = anaglyphColor(p_110985_0_[i]);
        }

        return aint1;
    }

    public static int anaglyphColor(int p_177054_0_)
    {
        int j = p_177054_0_ >> 24 & 255;
        int k = p_177054_0_ >> 16 & 255;
        int l = p_177054_0_ >> 8 & 255;
        int i1 = p_177054_0_ & 255;
        int j1 = (k * 30 + l * 59 + i1 * 11) / 100;
        int k1 = (k * 30 + l * 70) / 100;
        int l1 = (k * 30 + i1 * 70) / 100;
        return j << 24 | j1 << 16 | k1 << 8 | l1;
    }

    public static void saveGlTexture(String name, int textureId, int mipmapLevels, int width, int height)
    {
        bindTexture(textureId);
        GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

        for (int i1 = 0; i1 <= mipmapLevels; ++i1)
        {
            File file1 = new File(name + "_" + i1 + ".png");
            int j1 = width >> i1;
            int k1 = height >> i1;
            int l1 = j1 * k1;
            IntBuffer intbuffer = BufferUtils.createIntBuffer(l1);
            int[] aint = new int[l1];
            GL11.glGetTexImage(GL11.GL_TEXTURE_2D, i1, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, intbuffer);
            intbuffer.get(aint);
            BufferedImage bufferedimage = new BufferedImage(j1, k1, 2);
            bufferedimage.setRGB(0, 0, j1, k1, aint, 0, j1);

            try
            {
                ImageIO.write(bufferedimage, "png", file1);
                logger.debug("Exported png to: {}", new Object[] {file1.getAbsolutePath()});
            }
            catch (IOException ioexception)
            {
                logger.debug("Unable to write: ", ioexception);
            }
        }
    }

    public static void processPixelValues(int[] p_147953_0_, int p_147953_1_, int p_147953_2_)
    {
        int[] aint1 = new int[p_147953_1_];
        int k = p_147953_2_ / 2;

        for (int l = 0; l < k; ++l)
        {
            System.arraycopy(p_147953_0_, l * p_147953_1_, aint1, 0, p_147953_1_);
            System.arraycopy(p_147953_0_, (p_147953_2_ - 1 - l) * p_147953_1_, p_147953_0_, l * p_147953_1_, p_147953_1_);
            System.arraycopy(aint1, 0, p_147953_0_, (p_147953_2_ - 1 - l) * p_147953_1_, p_147953_1_);
        }
    }

    static
    {
        int var0 = -16777216;
        int var1 = -524040;
        int[] var2 = new int[] { -524040, -524040, -524040, -524040, -524040, -524040, -524040, -524040};
        int[] var3 = new int[] { -16777216, -16777216, -16777216, -16777216, -16777216, -16777216, -16777216, -16777216};
        int var4 = var2.length;

        for (int var5 = 0; var5 < 16; ++var5)
        {
            System.arraycopy(var5 < var4 ? var2 : var3, 0, missingTextureData, 16 * var5, var4);
            System.arraycopy(var5 < var4 ? var3 : var2, 0, missingTextureData, 16 * var5 + var4, var4);
        }

        missingTexture.updateDynamicTexture();
        mipmapBuffer = new int[4];
    }
}