package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.realms.RealmsSimpleScrolledSelectionList;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiSimpleScrolledSelectionListProxy extends GuiSlot
{
    private final RealmsSimpleScrolledSelectionList field_178050_u;
    private static final String __OBFID = "CL_00001938";

    public GuiSimpleScrolledSelectionListProxy(RealmsSimpleScrolledSelectionList p_i45525_1_, int p_i45525_2_, int p_i45525_3_, int p_i45525_4_, int p_i45525_5_, int p_i45525_6_)
    {
        super(Minecraft.getMinecraft(), p_i45525_2_, p_i45525_3_, p_i45525_4_, p_i45525_5_, p_i45525_6_);
        this.field_178050_u = p_i45525_1_;
    }

    protected int getSize()
    {
        return this.field_178050_u.getItemCount();
    }

    /**
     * The element in the slot that was clicked, boolean for whether it was double clicked or not
     */
    protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY)
    {
        this.field_178050_u.selectItem(slotIndex, isDoubleClick, mouseX, mouseY);
    }

    /**
     * Returns true if the element passed in is currently selected
     */
    protected boolean isSelected(int slotIndex)
    {
        return this.field_178050_u.isSelectedItem(slotIndex);
    }

    protected void drawBackground()
    {
        this.field_178050_u.renderBackground();
    }

    protected void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int p_180791_5_, int p_180791_6_)
    {
        this.field_178050_u.renderItem(entryID, p_180791_2_, p_180791_3_, p_180791_4_, p_180791_5_, p_180791_6_);
    }

    public int func_178048_e()
    {
        return super.width;
    }

    public int func_178047_f()
    {
        return super.mouseY;
    }

    public int func_178049_g()
    {
        return super.mouseX;
    }

    /**
     * Return the height of the content being scrolled
     */
    protected int getContentHeight()
    {
        return this.field_178050_u.getMaxPosition();
    }

    protected int getScrollBarX()
    {
        return this.field_178050_u.getScrollbarPosition();
    }

    public void handleMouseInput()
    {
        super.handleMouseInput();
    }

    public void drawScreen(int mouseXIn, int mouseYIn, float p_148128_3_)
    {
        if (this.field_178041_q)
        {
            this.mouseX = mouseXIn;
            this.mouseY = mouseYIn;
            this.drawBackground();
            int k = this.getScrollBarX();
            int l = k + 6;
            this.bindAmountScrolled();
            GlStateManager.disableLighting();
            GlStateManager.disableFog();
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            int i1 = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
            int j1 = this.top + 4 - (int)this.amountScrolled;

            if (this.hasListHeader)
            {
                this.drawListHeader(i1, j1, tessellator);
            }

            this.drawSelectionBox(i1, j1, mouseXIn, mouseYIn);
            GlStateManager.disableDepth();
            boolean flag = true;
            this.overlayBackground(0, this.top, 255, 255);
            this.overlayBackground(this.bottom, this.height, 255, 255);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            GlStateManager.disableAlpha();
            GlStateManager.shadeModel(7425);
            GlStateManager.disableTexture2D();
            int k1 = this.func_148135_f();

            if (k1 > 0)
            {
                int l1 = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();
                l1 = MathHelper.clamp_int(l1, 32, this.bottom - this.top - 8);
                int i2 = (int)this.amountScrolled * (this.bottom - this.top - l1) / k1 + this.top;

                if (i2 < this.top)
                {
                    i2 = this.top;
                }

                worldrenderer.startDrawingQuads();
                worldrenderer.setColorRGBA_I(0, 255);
                worldrenderer.addVertexWithUV((double)k, (double)this.bottom, 0.0D, 0.0D, 1.0D);
                worldrenderer.addVertexWithUV((double)l, (double)this.bottom, 0.0D, 1.0D, 1.0D);
                worldrenderer.addVertexWithUV((double)l, (double)this.top, 0.0D, 1.0D, 0.0D);
                worldrenderer.addVertexWithUV((double)k, (double)this.top, 0.0D, 0.0D, 0.0D);
                tessellator.draw();
                worldrenderer.startDrawingQuads();
                worldrenderer.setColorRGBA_I(8421504, 255);
                worldrenderer.addVertexWithUV((double)k, (double)(i2 + l1), 0.0D, 0.0D, 1.0D);
                worldrenderer.addVertexWithUV((double)l, (double)(i2 + l1), 0.0D, 1.0D, 1.0D);
                worldrenderer.addVertexWithUV((double)l, (double)i2, 0.0D, 1.0D, 0.0D);
                worldrenderer.addVertexWithUV((double)k, (double)i2, 0.0D, 0.0D, 0.0D);
                tessellator.draw();
                worldrenderer.startDrawingQuads();
                worldrenderer.setColorRGBA_I(12632256, 255);
                worldrenderer.addVertexWithUV((double)k, (double)(i2 + l1 - 1), 0.0D, 0.0D, 1.0D);
                worldrenderer.addVertexWithUV((double)(l - 1), (double)(i2 + l1 - 1), 0.0D, 1.0D, 1.0D);
                worldrenderer.addVertexWithUV((double)(l - 1), (double)i2, 0.0D, 1.0D, 0.0D);
                worldrenderer.addVertexWithUV((double)k, (double)i2, 0.0D, 0.0D, 0.0D);
                tessellator.draw();
            }

            this.func_148142_b(mouseXIn, mouseYIn);
            GlStateManager.enableTexture2D();
            GlStateManager.shadeModel(7424);
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
        }
    }
}