package net.minecraft.client.gui;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec4b;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MapItemRenderer
{
    private static final ResourceLocation mapIcons = new ResourceLocation("textures/map/map_icons.png");
    private final TextureManager textureManager;
    private final Map loadedMaps = Maps.newHashMap();
    private static final String __OBFID = "CL_00000663";

    public MapItemRenderer(TextureManager p_i45009_1_)
    {
        this.textureManager = p_i45009_1_;
    }

    public void func_148246_a(MapData p_148246_1_)
    {
        this.func_148248_b(p_148246_1_).func_148236_a();
    }

    public void func_148250_a(MapData p_148250_1_, boolean p_148250_2_)
    {
        this.func_148248_b(p_148250_1_).func_148237_a(p_148250_2_);
    }

    private MapItemRenderer.Instance func_148248_b(MapData p_148248_1_)
    {
        MapItemRenderer.Instance instance = (MapItemRenderer.Instance)this.loadedMaps.get(p_148248_1_.mapName);

        if (instance == null)
        {
            instance = new MapItemRenderer.Instance(p_148248_1_, null);
            this.loadedMaps.put(p_148248_1_.mapName, instance);
        }

        return instance;
    }

    /**
     * Clears the currently loaded maps and removes their corresponding textures
     */
    public void clearLoadedMaps()
    {
        Iterator iterator = this.loadedMaps.values().iterator();

        while (iterator.hasNext())
        {
            MapItemRenderer.Instance instance = (MapItemRenderer.Instance)iterator.next();
            this.textureManager.deleteTexture(instance.location);
        }

        this.loadedMaps.clear();
    }

    @SideOnly(Side.CLIENT)
    class Instance
    {
        private final MapData mapData;
        private final DynamicTexture field_148243_c;
        private final ResourceLocation location;
        private final int[] field_148241_e;
        private static final String __OBFID = "CL_00000665";

        private Instance(MapData p_i45007_2_)
        {
            this.mapData = p_i45007_2_;
            this.field_148243_c = new DynamicTexture(128, 128);
            this.field_148241_e = this.field_148243_c.getTextureData();
            this.location = MapItemRenderer.this.textureManager.getDynamicTextureLocation("map/" + p_i45007_2_.mapName, this.field_148243_c);

            for (int i = 0; i < this.field_148241_e.length; ++i)
            {
                this.field_148241_e[i] = 0;
            }
        }

        private void func_148236_a()
        {
            for (int i = 0; i < 16384; ++i)
            {
                int j = this.mapData.colors[i] & 255;

                if (j / 4 == 0)
                {
                    this.field_148241_e[i] = (i + i / 128 & 1) * 8 + 16 << 24;
                }
                else
                {
                    this.field_148241_e[i] = MapColor.mapColorArray[j / 4].func_151643_b(j & 3);
                }
            }

            this.field_148243_c.updateDynamicTexture();
        }

        private void func_148237_a(boolean p_148237_1_)
        {
            byte b0 = 0;
            byte b1 = 0;
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            float f = 0.0F;
            MapItemRenderer.this.textureManager.bindTexture(this.location);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(1, 771, 0, 1);
            GlStateManager.disableAlpha();
            worldrenderer.startDrawingQuads();
            worldrenderer.addVertexWithUV((double)((float)(b0 + 0) + f), (double)((float)(b1 + 128) - f), -0.009999999776482582D, 0.0D, 1.0D);
            worldrenderer.addVertexWithUV((double)((float)(b0 + 128) - f), (double)((float)(b1 + 128) - f), -0.009999999776482582D, 1.0D, 1.0D);
            worldrenderer.addVertexWithUV((double)((float)(b0 + 128) - f), (double)((float)(b1 + 0) + f), -0.009999999776482582D, 1.0D, 0.0D);
            worldrenderer.addVertexWithUV((double)((float)(b0 + 0) + f), (double)((float)(b1 + 0) + f), -0.009999999776482582D, 0.0D, 0.0D);
            tessellator.draw();
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
            MapItemRenderer.this.textureManager.bindTexture(MapItemRenderer.mapIcons);
            int i = 0;
            Iterator iterator = this.mapData.playersVisibleOnMap.values().iterator();

            while (iterator.hasNext())
            {
                Vec4b vec4b = (Vec4b)iterator.next();

                if (!p_148237_1_ || vec4b.func_176110_a() == 1)
                {
                    GlStateManager.pushMatrix();
                    GlStateManager.translate((float)b0 + (float)vec4b.func_176112_b() / 2.0F + 64.0F, (float)b1 + (float)vec4b.func_176113_c() / 2.0F + 64.0F, -0.02F);
                    GlStateManager.rotate((float)(vec4b.func_176111_d() * 360) / 16.0F, 0.0F, 0.0F, 1.0F);
                    GlStateManager.scale(4.0F, 4.0F, 3.0F);
                    GlStateManager.translate(-0.125F, 0.125F, 0.0F);
                    byte b2 = vec4b.func_176110_a();
                    float f1 = (float)(b2 % 4 + 0) / 4.0F;
                    float f2 = (float)(b2 / 4 + 0) / 4.0F;
                    float f3 = (float)(b2 % 4 + 1) / 4.0F;
                    float f4 = (float)(b2 / 4 + 1) / 4.0F;
                    worldrenderer.startDrawingQuads();
                    worldrenderer.addVertexWithUV(-1.0D, 1.0D, (double)((float)i * 0.001F), (double)f1, (double)f2);
                    worldrenderer.addVertexWithUV(1.0D, 1.0D, (double)((float)i * 0.001F), (double)f3, (double)f2);
                    worldrenderer.addVertexWithUV(1.0D, -1.0D, (double)((float)i * 0.001F), (double)f3, (double)f4);
                    worldrenderer.addVertexWithUV(-1.0D, -1.0D, (double)((float)i * 0.001F), (double)f1, (double)f4);
                    tessellator.draw();
                    GlStateManager.popMatrix();
                    ++i;
                }
            }

            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, 0.0F, -0.04F);
            GlStateManager.scale(1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
        }

        Instance(MapData p_i45008_2_, Object p_i45008_3_)
        {
            this(p_i45008_2_);
        }
    }
}