package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityEnderChestRenderer extends TileEntitySpecialRenderer
{
    private static final ResourceLocation field_147520_b = new ResourceLocation("textures/entity/chest/ender.png");
    private ModelChest field_147521_c = new ModelChest();
    private static final String __OBFID = "CL_00000967";

    public void func_180540_a(TileEntityEnderChest p_180540_1_, double p_180540_2_, double p_180540_4_, double p_180540_6_, float p_180540_8_, int p_180540_9_)
    {
        int j = 0;

        if (p_180540_1_.hasWorldObj())
        {
            j = p_180540_1_.getBlockMetadata();
        }

        if (p_180540_9_ >= 0)
        {
            this.bindTexture(DESTROY_STAGES[p_180540_9_]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0F, 4.0F, 1.0F);
            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        }
        else
        {
            this.bindTexture(field_147520_b);
        }

        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.translate((float)p_180540_2_, (float)p_180540_4_ + 1.0F, (float)p_180540_6_ + 1.0F);
        GlStateManager.scale(1.0F, -1.0F, -1.0F);
        GlStateManager.translate(0.5F, 0.5F, 0.5F);
        short short1 = 0;

        if (j == 2)
        {
            short1 = 180;
        }

        if (j == 3)
        {
            short1 = 0;
        }

        if (j == 4)
        {
            short1 = 90;
        }

        if (j == 5)
        {
            short1 = -90;
        }

        GlStateManager.rotate((float)short1, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(-0.5F, -0.5F, -0.5F);
        float f1 = p_180540_1_.prevLidAngle + (p_180540_1_.field_145972_a - p_180540_1_.prevLidAngle) * p_180540_8_;
        f1 = 1.0F - f1;
        f1 = 1.0F - f1 * f1 * f1;
        this.field_147521_c.chestLid.rotateAngleX = -(f1 * (float)Math.PI / 2.0F);
        this.field_147521_c.renderAll();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        if (p_180540_9_ >= 0)
        {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }

    public void renderTileEntityAt(TileEntity p_180535_1_, double posX, double posZ, double p_180535_6_, float p_180535_8_, int p_180535_9_)
    {
        this.func_180540_a((TileEntityEnderChest)p_180535_1_, posX, posZ, p_180535_6_, p_180535_8_, p_180535_9_);
    }
}