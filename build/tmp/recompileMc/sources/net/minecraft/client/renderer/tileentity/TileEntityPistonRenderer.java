package net.minecraft.client.renderer.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityPistonRenderer extends TileEntitySpecialRenderer
{
    private final BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
    private static final String __OBFID = "CL_00002469";

    public void func_178461_a(TileEntityPiston p_178461_1_, double p_178461_2_, double p_178461_4_, double p_178461_6_, float p_178461_8_, int p_178461_9_)
    {
        BlockPos blockpos = p_178461_1_.getPos();
        IBlockState iblockstate = p_178461_1_.getPistonState();
        Block block = iblockstate.getBlock();

        if (block.getMaterial() != Material.air && p_178461_1_.func_145860_a(p_178461_8_) < 1.0F)
        {
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            this.bindTexture(TextureMap.locationBlocksTexture);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.blendFunc(770, 771);
            GlStateManager.enableBlend();
            GlStateManager.disableCull();

            if (Minecraft.isAmbientOcclusionEnabled())
            {
                GlStateManager.shadeModel(7425);
            }
            else
            {
                GlStateManager.shadeModel(7424);
            }

            worldrenderer.startDrawingQuads();
            worldrenderer.setVertexFormat(DefaultVertexFormats.BLOCK);
            worldrenderer.setTranslation((double)((float)p_178461_2_ - (float)blockpos.getX() + p_178461_1_.func_174929_b(p_178461_8_)), (double)((float)p_178461_4_ - (float)blockpos.getY() + p_178461_1_.func_174928_c(p_178461_8_)), (double)((float)p_178461_6_ - (float)blockpos.getZ() + p_178461_1_.func_174926_d(p_178461_8_)));
            worldrenderer.setColorOpaque_F(1.0F, 1.0F, 1.0F);
            World world = this.getWorld();

            if (block == Blocks.piston_head && p_178461_1_.func_145860_a(p_178461_8_) < 0.5F)
            {
                iblockstate = iblockstate.withProperty(BlockPistonExtension.SHORT, Boolean.valueOf(true));
                this.blockRenderer.getBlockModelRenderer().renderModel(world, this.blockRenderer.getModelFromBlockState(iblockstate, world, blockpos), iblockstate, blockpos, worldrenderer, true);
            }
            else if (p_178461_1_.shouldPistonHeadBeRendered() && !p_178461_1_.isExtending())
            {
                BlockPistonExtension.EnumPistonType enumpistontype = block == Blocks.sticky_piston ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT;
                IBlockState iblockstate1 = Blocks.piston_head.getDefaultState().withProperty(BlockPistonExtension.TYPE, enumpistontype).withProperty(BlockPistonExtension.FACING, iblockstate.getValue(BlockPistonBase.FACING));
                iblockstate1 = iblockstate1.withProperty(BlockPistonExtension.SHORT, Boolean.valueOf(p_178461_1_.func_145860_a(p_178461_8_) >= 0.5F));
                this.blockRenderer.getBlockModelRenderer().renderModel(world, this.blockRenderer.getModelFromBlockState(iblockstate1, world, blockpos), iblockstate1, blockpos, worldrenderer, true);
                worldrenderer.setTranslation((double)((float)p_178461_2_ - (float)blockpos.getX()), (double)((float)p_178461_4_ - (float)blockpos.getY()), (double)((float)p_178461_6_ - (float)blockpos.getZ()));
                iblockstate.withProperty(BlockPistonBase.EXTENDED, Boolean.valueOf(true));
                this.blockRenderer.getBlockModelRenderer().renderModel(world, this.blockRenderer.getModelFromBlockState(iblockstate, world, blockpos), iblockstate, blockpos, worldrenderer, true);
            }
            else
            {
                this.blockRenderer.getBlockModelRenderer().renderModel(world, this.blockRenderer.getModelFromBlockState(iblockstate, world, blockpos), iblockstate, blockpos, worldrenderer, false);
            }

            worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
            tessellator.draw();
            RenderHelper.enableStandardItemLighting();
        }
    }

    public void renderTileEntityAt(TileEntity p_180535_1_, double posX, double posZ, double p_180535_6_, float p_180535_8_, int p_180535_9_)
    {
        this.func_178461_a((TileEntityPiston)p_180535_1_, posX, posZ, p_180535_6_, p_180535_8_, p_180535_9_);
    }
}