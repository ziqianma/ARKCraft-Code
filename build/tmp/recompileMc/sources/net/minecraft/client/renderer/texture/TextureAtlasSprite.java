package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import net.minecraft.client.resources.data.AnimationFrame;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TextureAtlasSprite
{
    private final String iconName;
    protected List framesTextureData = Lists.newArrayList();
    protected int[][] interpolatedFrameData;
    private AnimationMetadataSection animationMetadata;
    protected boolean rotated;
    protected int originX;
    protected int originY;
    protected int width;
    protected int height;
    private float minU;
    private float maxU;
    private float minV;
    private float maxV;
    protected int frameCounter;
    protected int tickCounter;
    private static String locationNameClock = "builtin/clock";
    private static String locationNameCompass = "builtin/compass";
    private static final String __OBFID = "CL_00001062";

    protected TextureAtlasSprite(String spriteName)
    {
        this.iconName = spriteName;
    }

    protected static TextureAtlasSprite makeAtlasSprite(ResourceLocation spriteResourceLocation)
    {
        String s = spriteResourceLocation.toString();
        return (TextureAtlasSprite)(locationNameClock.equals(s) ? new TextureClock(s) : (locationNameCompass.equals(s) ? new TextureCompass(s) : new TextureAtlasSprite(s)));
    }

    public static void setLocationNameClock(String p_176602_0_)
    {
        locationNameClock = p_176602_0_;
    }

    public static void setLocationNameCompass(String p_176603_0_)
    {
        locationNameCompass = p_176603_0_;
    }

    public void initSprite(int p_110971_1_, int p_110971_2_, int p_110971_3_, int p_110971_4_, boolean p_110971_5_)
    {
        this.originX = p_110971_3_;
        this.originY = p_110971_4_;
        this.rotated = p_110971_5_;
        float f = (float)(0.009999999776482582D / (double)p_110971_1_);
        float f1 = (float)(0.009999999776482582D / (double)p_110971_2_);
        this.minU = (float)p_110971_3_ / (float)((double)p_110971_1_) + f;
        this.maxU = (float)(p_110971_3_ + this.width) / (float)((double)p_110971_1_) - f;
        this.minV = (float)p_110971_4_ / (float)p_110971_2_ + f1;
        this.maxV = (float)(p_110971_4_ + this.height) / (float)p_110971_2_ - f1;
    }

    public void copyFrom(TextureAtlasSprite p_94217_1_)
    {
        this.originX = p_94217_1_.originX;
        this.originY = p_94217_1_.originY;
        this.width = p_94217_1_.width;
        this.height = p_94217_1_.height;
        this.rotated = p_94217_1_.rotated;
        this.minU = p_94217_1_.minU;
        this.maxU = p_94217_1_.maxU;
        this.minV = p_94217_1_.minV;
        this.maxV = p_94217_1_.maxV;
    }

    /**
     * Returns the X position of this icon on its texture sheet, in pixels.
     */
    public int getOriginX()
    {
        return this.originX;
    }

    /**
     * Returns the Y position of this icon on its texture sheet, in pixels.
     */
    public int getOriginY()
    {
        return this.originY;
    }

    /**
     * Returns the width of the icon, in pixels.
     */
    public int getIconWidth()
    {
        return this.width;
    }

    /**
     * Returns the height of the icon, in pixels.
     */
    public int getIconHeight()
    {
        return this.height;
    }

    /**
     * Returns the minimum U coordinate to use when rendering with this icon.
     */
    public float getMinU()
    {
        return this.minU;
    }

    /**
     * Returns the maximum U coordinate to use when rendering with this icon.
     */
    public float getMaxU()
    {
        return this.maxU;
    }

    /**
     * Gets a U coordinate on the icon. 0 returns uMin and 16 returns uMax. Other arguments return in-between values.
     */
    public float getInterpolatedU(double p_94214_1_)
    {
        float f = this.maxU - this.minU;
        return this.minU + f * (float)p_94214_1_ / 16.0F;
    }

    /**
     * Returns the minimum V coordinate to use when rendering with this icon.
     */
    public float getMinV()
    {
        return this.minV;
    }

    /**
     * Returns the maximum V coordinate to use when rendering with this icon.
     */
    public float getMaxV()
    {
        return this.maxV;
    }

    /**
     * Gets a V coordinate on the icon. 0 returns vMin and 16 returns vMax. Other arguments return in-between values.
     */
    public float getInterpolatedV(double p_94207_1_)
    {
        float f = this.maxV - this.minV;
        return this.minV + f * ((float)p_94207_1_ / 16.0F);
    }

    public String getIconName()
    {
        return this.iconName;
    }

    public void updateAnimation()
    {
        ++this.tickCounter;

        if (this.tickCounter >= this.animationMetadata.getFrameTimeSingle(this.frameCounter))
        {
            int i = this.animationMetadata.getFrameIndex(this.frameCounter);
            int j = this.animationMetadata.getFrameCount() == 0 ? this.framesTextureData.size() : this.animationMetadata.getFrameCount();
            this.frameCounter = (this.frameCounter + 1) % j;
            this.tickCounter = 0;
            int k = this.animationMetadata.getFrameIndex(this.frameCounter);

            if (i != k && k >= 0 && k < this.framesTextureData.size())
            {
                TextureUtil.uploadTextureMipmap((int[][])this.framesTextureData.get(k), this.width, this.height, this.originX, this.originY, false, false);
            }
        }
        else if (this.animationMetadata.isInterpolate())
        {
            this.updateAnimationInterpolated();
        }
    }

    private void updateAnimationInterpolated()
    {
        double d0 = 1.0D - (double)this.tickCounter / (double)this.animationMetadata.getFrameTimeSingle(this.frameCounter);
        int i = this.animationMetadata.getFrameIndex(this.frameCounter);
        int j = this.animationMetadata.getFrameCount() == 0 ? this.framesTextureData.size() : this.animationMetadata.getFrameCount();
        int k = this.animationMetadata.getFrameIndex((this.frameCounter + 1) % j);

        if (i != k && k >= 0 && k < this.framesTextureData.size())
        {
            int[][] aint = (int[][])this.framesTextureData.get(i);
            int[][] aint1 = (int[][])this.framesTextureData.get(k);

            if (this.interpolatedFrameData == null || this.interpolatedFrameData.length != aint.length)
            {
                this.interpolatedFrameData = new int[aint.length][];
            }

            for (int l = 0; l < aint.length; ++l)
            {
                if (this.interpolatedFrameData[l] == null)
                {
                    this.interpolatedFrameData[l] = new int[aint[l].length];
                }

                if (l < aint1.length && aint1[l].length == aint[l].length)
                {
                    for (int i1 = 0; i1 < aint[l].length; ++i1)
                    {
                        int j1 = aint[l][i1];
                        int k1 = aint1[l][i1];
                        int l1 = (int)((double)((j1 & 16711680) >> 16) * d0 + (double)((k1 & 16711680) >> 16) * (1.0D - d0));
                        int i2 = (int)((double)((j1 & 65280) >> 8) * d0 + (double)((k1 & 65280) >> 8) * (1.0D - d0));
                        int j2 = (int)((double)(j1 & 255) * d0 + (double)(k1 & 255) * (1.0D - d0));
                        this.interpolatedFrameData[l][i1] = j1 & -16777216 | l1 << 16 | i2 << 8 | j2;
                    }
                }
            }

            TextureUtil.uploadTextureMipmap(this.interpolatedFrameData, this.width, this.height, this.originX, this.originY, false, false);
        }
    }

    public int[][] getFrameTextureData(int p_147965_1_)
    {
        return (int[][])this.framesTextureData.get(p_147965_1_);
    }

    public int getFrameCount()
    {
        return this.framesTextureData.size();
    }

    public void setIconWidth(int newWidth)
    {
        this.width = newWidth;
    }

    public void setIconHeight(int newHeight)
    {
        this.height = newHeight;
    }

    public void loadSprite(BufferedImage[] p_180598_1_, AnimationMetadataSection p_180598_2_)
    {
        this.resetSprite();
        int i = p_180598_1_[0].getWidth();
        int j = p_180598_1_[0].getHeight();
        this.width = i;
        this.height = j;
        int[][] aint = new int[p_180598_1_.length][];
        int k;

        for (k = 0; k < p_180598_1_.length; ++k)
        {
            BufferedImage bufferedimage = p_180598_1_[k];

            if (bufferedimage != null)
            {
                if (k > 0 && (bufferedimage.getWidth() != i >> k || bufferedimage.getHeight() != j >> k))
                {
                    throw new RuntimeException(String.format("Unable to load miplevel: %d, image is size: %dx%d, expected %dx%d", new Object[] {Integer.valueOf(k), Integer.valueOf(bufferedimage.getWidth()), Integer.valueOf(bufferedimage.getHeight()), Integer.valueOf(i >> k), Integer.valueOf(j >> k)}));
                }

                aint[k] = new int[bufferedimage.getWidth() * bufferedimage.getHeight()];
                bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), aint[k], 0, bufferedimage.getWidth());
            }
        }

        if (p_180598_2_ == null)
        {
            if (j != i)
            {
                throw new RuntimeException("broken aspect ratio and not an animation");
            }

            this.framesTextureData.add(aint);
        }
        else
        {
            k = j / i;
            int j1 = i;
            int l = i;
            this.height = this.width;
            int i1;

            if (p_180598_2_.getFrameCount() > 0)
            {
                Iterator iterator = p_180598_2_.getFrameIndexSet().iterator();

                while (iterator.hasNext())
                {
                    i1 = ((Integer)iterator.next()).intValue();

                    if (i1 >= k)
                    {
                        throw new RuntimeException("invalid frameindex " + i1);
                    }

                    this.allocateFrameTextureData(i1);
                    this.framesTextureData.set(i1, getFrameTextureData(aint, j1, l, i1));
                }

                this.animationMetadata = p_180598_2_;
            }
            else
            {
                ArrayList arraylist = Lists.newArrayList();

                for (i1 = 0; i1 < k; ++i1)
                {
                    this.framesTextureData.add(getFrameTextureData(aint, j1, l, i1));
                    arraylist.add(new AnimationFrame(i1, -1));
                }

                this.animationMetadata = new AnimationMetadataSection(arraylist, this.width, this.height, p_180598_2_.getFrameTime(), p_180598_2_.isInterpolate());
            }
        }
    }

    public void generateMipmaps(int p_147963_1_)
    {
        ArrayList arraylist = Lists.newArrayList();

        for (int j = 0; j < this.framesTextureData.size(); ++j)
        {
            final int[][] aint = (int[][])this.framesTextureData.get(j);

            if (aint != null)
            {
                try
                {
                    arraylist.add(TextureUtil.generateMipmapData(p_147963_1_, this.width, aint));
                }
                catch (Throwable throwable)
                {
                    CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Generating mipmaps for frame");
                    CrashReportCategory crashreportcategory = crashreport.makeCategory("Frame being iterated");
                    crashreportcategory.addCrashSection("Frame index", Integer.valueOf(j));
                    crashreportcategory.addCrashSectionCallable("Frame sizes", new Callable()
                    {
                        private static final String __OBFID = "CL_00001063";
                        public String call()
                        {
                            StringBuilder stringbuilder = new StringBuilder();
                            int[][] aint1 = aint;
                            int k = aint1.length;

                            for (int l = 0; l < k; ++l)
                            {
                                int[] aint2 = aint1[l];

                                if (stringbuilder.length() > 0)
                                {
                                    stringbuilder.append(", ");
                                }

                                stringbuilder.append(aint2 == null ? "null" : Integer.valueOf(aint2.length));
                            }

                            return stringbuilder.toString();
                        }
                    });
                    throw new ReportedException(crashreport);
                }
            }
        }

        this.setFramesTextureData(arraylist);
    }

    private void allocateFrameTextureData(int p_130099_1_)
    {
        if (this.framesTextureData.size() <= p_130099_1_)
        {
            for (int j = this.framesTextureData.size(); j <= p_130099_1_; ++j)
            {
                this.framesTextureData.add((Object)null);
            }
        }
    }

    private static int[][] getFrameTextureData(int[][] p_147962_0_, int p_147962_1_, int p_147962_2_, int p_147962_3_)
    {
        int[][] aint1 = new int[p_147962_0_.length][];

        for (int l = 0; l < p_147962_0_.length; ++l)
        {
            int[] aint2 = p_147962_0_[l];

            if (aint2 != null)
            {
                aint1[l] = new int[(p_147962_1_ >> l) * (p_147962_2_ >> l)];
                System.arraycopy(aint2, p_147962_3_ * aint1[l].length, aint1[l], 0, aint1[l].length);
            }
        }

        return aint1;
    }

    public void clearFramesTextureData()
    {
        this.framesTextureData.clear();
    }

    public boolean hasAnimationMetadata()
    {
        return this.animationMetadata != null;
    }

    public void setFramesTextureData(List newFramesTextureData)
    {
        this.framesTextureData = newFramesTextureData;
    }

    private void resetSprite()
    {
        this.animationMetadata = null;
        this.setFramesTextureData(Lists.newArrayList());
        this.frameCounter = 0;
        this.tickCounter = 0;
    }

    public String toString()
    {
        return "TextureAtlasSprite{name=\'" + this.iconName + '\'' + ", frameCount=" + this.framesTextureData.size() + ", rotated=" + this.rotated + ", x=" + this.originX + ", y=" + this.originY + ", height=" + this.height + ", width=" + this.width + ", u0=" + this.minU + ", u1=" + this.maxU + ", v0=" + this.minV + ", v1=" + this.maxV + '}';
    }

    /*===================================== FORGE START =====================================*/
    /**
     * The result of this function determines is the below 'load' function is called, and the
     * default vanilla loading code is bypassed completely.
     * @param manager
     * @param location
     * @return True to use your own custom load code and bypass vanilla loading.
     */
    public boolean hasCustomLoader(net.minecraft.client.resources.IResourceManager manager, net.minecraft.util.ResourceLocation location)
    {
        return false;
    }

    /**
     * Load the specified resource as this sprite's data.
     * Returning false from this function will prevent this icon from being stitched onto the master texture.
     * @param manager Main resource manager
     * @param location File resource location
     * @return False to prevent this Icon from being stitched
     */
    public boolean load(net.minecraft.client.resources.IResourceManager manager, net.minecraft.util.ResourceLocation location)
    {
        return true;
    }
    /*===================================== FORGE END ======================================*/
}