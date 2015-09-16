package net.minecraft.client.renderer.entity;

import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public abstract class Render
{
    private static final ResourceLocation shadowTextures = new ResourceLocation("textures/misc/shadow.png");
    protected final RenderManager renderManager;
    protected float shadowSize;
    /** Determines the darkness of the object's shadow. Higher value makes a darker shadow. */
    protected float shadowOpaque = 1.0F;
    private static final String __OBFID = "CL_00000992";

    protected Render(RenderManager renderManager)
    {
        this.renderManager = renderManager;
    }

    public boolean shouldRender(Entity entity, ICamera camera, double camX, double camY, double camZ)
    {
        return entity.isInRangeToRender3d(camX, camY, camZ) && (entity.ignoreFrustumCheck || camera.isBoundingBoxInFrustum(entity.getEntityBoundingBox()));
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity>) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doe
     */
    public void doRender(Entity entity, double x, double y, double z, float p_76986_8_, float partialTicks)
    {
        this.renderName(entity, x, y, z);
    }

    protected void renderName(Entity entity, double x, double y, double z)
    {
        if (this.canRenderName(entity))
        {
            this.renderLivingLabel(entity, entity.getDisplayName().getFormattedText(), x, y, z, 64);
        }
    }

    protected boolean canRenderName(Entity entity)
    {
        return entity.getAlwaysRenderNameTagForRender() && entity.hasCustomName();
    }

    protected void func_177069_a(Entity p_177069_1_, double p_177069_2_, double p_177069_4_, double p_177069_6_, String p_177069_8_, float p_177069_9_, double p_177069_10_)
    {
        this.renderLivingLabel(p_177069_1_, p_177069_8_, p_177069_2_, p_177069_4_, p_177069_6_, 64);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected abstract ResourceLocation getEntityTexture(Entity entity);

    protected boolean bindEntityTexture(Entity entity)
    {
        ResourceLocation resourcelocation = this.getEntityTexture(entity);

        if (resourcelocation == null)
        {
            return false;
        }
        else
        {
            this.bindTexture(resourcelocation);
            return true;
        }
    }

    public void bindTexture(ResourceLocation location)
    {
        this.renderManager.renderEngine.bindTexture(location);
    }

    /**
     * Renders fire on top of the entity. Args: entity, x, y, z, partialTickTime
     */
    private void renderEntityOnFire(Entity entity, double x, double y, double z, float partialTicks)
    {
        GlStateManager.disableLighting();
        TextureMap texturemap = Minecraft.getMinecraft().getTextureMapBlocks();
        TextureAtlasSprite textureatlassprite = texturemap.getAtlasSprite("minecraft:blocks/fire_layer_0");
        TextureAtlasSprite textureatlassprite1 = texturemap.getAtlasSprite("minecraft:blocks/fire_layer_1");
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);
        float f1 = entity.width * 1.4F;
        GlStateManager.scale(f1, f1, f1);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        float f2 = 0.5F;
        float f3 = 0.0F;
        float f4 = entity.height / f1;
        float f5 = (float)(entity.posY - entity.getEntityBoundingBox().minY);
        GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(0.0F, 0.0F, -0.3F + (float)((int)f4) * 0.02F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        float f6 = 0.0F;
        int i = 0;
        worldrenderer.startDrawingQuads();

        while (f4 > 0.0F)
        {
            TextureAtlasSprite textureatlassprite2 = i % 2 == 0 ? textureatlassprite : textureatlassprite1;
            this.bindTexture(TextureMap.locationBlocksTexture);
            float f7 = textureatlassprite2.getMinU();
            float f8 = textureatlassprite2.getMinV();
            float f9 = textureatlassprite2.getMaxU();
            float f10 = textureatlassprite2.getMaxV();

            if (i / 2 % 2 == 0)
            {
                float f11 = f9;
                f9 = f7;
                f7 = f11;
            }

            worldrenderer.addVertexWithUV((double)(f2 - f3), (double)(0.0F - f5), (double)f6, (double)f9, (double)f10);
            worldrenderer.addVertexWithUV((double)(-f2 - f3), (double)(0.0F - f5), (double)f6, (double)f7, (double)f10);
            worldrenderer.addVertexWithUV((double)(-f2 - f3), (double)(1.4F - f5), (double)f6, (double)f7, (double)f8);
            worldrenderer.addVertexWithUV((double)(f2 - f3), (double)(1.4F - f5), (double)f6, (double)f9, (double)f8);
            f4 -= 0.45F;
            f5 -= 0.45F;
            f2 *= 0.9F;
            f6 += 0.03F;
            ++i;
        }

        tessellator.draw();
        GlStateManager.popMatrix();
        GlStateManager.enableLighting();
    }

    /**
     * Renders the entity shadows at the position, shadow alpha and partialTickTime. Args: entity, x, y, z, shadowAlpha,
     * partialTickTime
     */
    private void renderShadow(Entity p_76975_1_, double p_76975_2_, double p_76975_4_, double p_76975_6_, float p_76975_8_, float p_76975_9_)
    {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        this.renderManager.renderEngine.bindTexture(shadowTextures);
        World world = this.getWorldFromRenderManager();
        GlStateManager.depthMask(false);
        float f2 = this.shadowSize;

        if (p_76975_1_ instanceof EntityLiving)
        {
            EntityLiving entityliving = (EntityLiving)p_76975_1_;
            f2 *= entityliving.getRenderSizeModifier();

            if (entityliving.isChild())
            {
                f2 *= 0.5F;
            }
        }

        double d8 = p_76975_1_.lastTickPosX + (p_76975_1_.posX - p_76975_1_.lastTickPosX) * (double)p_76975_9_;
        double d3 = p_76975_1_.lastTickPosY + (p_76975_1_.posY - p_76975_1_.lastTickPosY) * (double)p_76975_9_;
        double d4 = p_76975_1_.lastTickPosZ + (p_76975_1_.posZ - p_76975_1_.lastTickPosZ) * (double)p_76975_9_;
        int i = MathHelper.floor_double(d8 - (double)f2);
        int j = MathHelper.floor_double(d8 + (double)f2);
        int k = MathHelper.floor_double(d3 - (double)f2);
        int l = MathHelper.floor_double(d3);
        int i1 = MathHelper.floor_double(d4 - (double)f2);
        int j1 = MathHelper.floor_double(d4 + (double)f2);
        double d5 = p_76975_2_ - d8;
        double d6 = p_76975_4_ - d3;
        double d7 = p_76975_6_ - d4;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.startDrawingQuads();
        Iterator iterator = BlockPos.getAllInBox(new BlockPos(i, k, i1), new BlockPos(j, l, j1)).iterator();

        while (iterator.hasNext())
        {
            BlockPos blockpos = (BlockPos)iterator.next();
            Block block = world.getBlockState(blockpos.down()).getBlock();

            if (block.getRenderType() != -1 && world.getLightFromNeighbors(blockpos) > 3)
            {
                this.func_180549_a(block, p_76975_2_, p_76975_4_, p_76975_6_, blockpos, p_76975_8_, f2, d5, d6, d7);
            }
        }

        tessellator.draw();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
    }

    /**
     * Returns the render manager's world object
     */
    private World getWorldFromRenderManager()
    {
        return this.renderManager.worldObj;
    }

    private void func_180549_a(Block p_180549_1_, double p_180549_2_, double p_180549_4_, double p_180549_6_, BlockPos p_180549_8_, float p_180549_9_, float p_180549_10_, double p_180549_11_, double p_180549_13_, double p_180549_15_)
    {
        if (p_180549_1_.isFullCube())
        {
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            double d6 = ((double)p_180549_9_ - (p_180549_4_ - ((double)p_180549_8_.getY() + p_180549_13_)) / 2.0D) * 0.5D * (double)this.getWorldFromRenderManager().getLightBrightness(p_180549_8_);

            if (d6 >= 0.0D)
            {
                if (d6 > 1.0D)
                {
                    d6 = 1.0D;
                }

                worldrenderer.setColorRGBA_F(1.0F, 1.0F, 1.0F, (float)d6);
                double d7 = (double)p_180549_8_.getX() + p_180549_1_.getBlockBoundsMinX() + p_180549_11_;
                double d8 = (double)p_180549_8_.getX() + p_180549_1_.getBlockBoundsMaxX() + p_180549_11_;
                double d9 = (double)p_180549_8_.getY() + p_180549_1_.getBlockBoundsMinY() + p_180549_13_ + 0.015625D;
                double d10 = (double)p_180549_8_.getZ() + p_180549_1_.getBlockBoundsMinZ() + p_180549_15_;
                double d11 = (double)p_180549_8_.getZ() + p_180549_1_.getBlockBoundsMaxZ() + p_180549_15_;
                float f2 = (float)((p_180549_2_ - d7) / 2.0D / (double)p_180549_10_ + 0.5D);
                float f3 = (float)((p_180549_2_ - d8) / 2.0D / (double)p_180549_10_ + 0.5D);
                float f4 = (float)((p_180549_6_ - d10) / 2.0D / (double)p_180549_10_ + 0.5D);
                float f5 = (float)((p_180549_6_ - d11) / 2.0D / (double)p_180549_10_ + 0.5D);
                worldrenderer.addVertexWithUV(d7, d9, d10, (double)f2, (double)f4);
                worldrenderer.addVertexWithUV(d7, d9, d11, (double)f2, (double)f5);
                worldrenderer.addVertexWithUV(d8, d9, d11, (double)f3, (double)f5);
                worldrenderer.addVertexWithUV(d8, d9, d10, (double)f3, (double)f4);
            }
        }
    }

    /**
     * Renders a white box with the bounds of the AABB translated by the offset. Args: aabb, x, y, z
     */
    public static void renderOffsetAABB(AxisAlignedBB p_76978_0_, double p_76978_1_, double p_76978_3_, double p_76978_5_)
    {
        GlStateManager.disableTexture2D();
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        worldrenderer.startDrawingQuads();
        worldrenderer.setTranslation(p_76978_1_, p_76978_3_, p_76978_5_);
        worldrenderer.setNormal(0.0F, 0.0F, -1.0F);
        worldrenderer.addVertex(p_76978_0_.minX, p_76978_0_.maxY, p_76978_0_.minZ);
        worldrenderer.addVertex(p_76978_0_.maxX, p_76978_0_.maxY, p_76978_0_.minZ);
        worldrenderer.addVertex(p_76978_0_.maxX, p_76978_0_.minY, p_76978_0_.minZ);
        worldrenderer.addVertex(p_76978_0_.minX, p_76978_0_.minY, p_76978_0_.minZ);
        worldrenderer.setNormal(0.0F, 0.0F, 1.0F);
        worldrenderer.addVertex(p_76978_0_.minX, p_76978_0_.minY, p_76978_0_.maxZ);
        worldrenderer.addVertex(p_76978_0_.maxX, p_76978_0_.minY, p_76978_0_.maxZ);
        worldrenderer.addVertex(p_76978_0_.maxX, p_76978_0_.maxY, p_76978_0_.maxZ);
        worldrenderer.addVertex(p_76978_0_.minX, p_76978_0_.maxY, p_76978_0_.maxZ);
        worldrenderer.setNormal(0.0F, -1.0F, 0.0F);
        worldrenderer.addVertex(p_76978_0_.minX, p_76978_0_.minY, p_76978_0_.minZ);
        worldrenderer.addVertex(p_76978_0_.maxX, p_76978_0_.minY, p_76978_0_.minZ);
        worldrenderer.addVertex(p_76978_0_.maxX, p_76978_0_.minY, p_76978_0_.maxZ);
        worldrenderer.addVertex(p_76978_0_.minX, p_76978_0_.minY, p_76978_0_.maxZ);
        worldrenderer.setNormal(0.0F, 1.0F, 0.0F);
        worldrenderer.addVertex(p_76978_0_.minX, p_76978_0_.maxY, p_76978_0_.maxZ);
        worldrenderer.addVertex(p_76978_0_.maxX, p_76978_0_.maxY, p_76978_0_.maxZ);
        worldrenderer.addVertex(p_76978_0_.maxX, p_76978_0_.maxY, p_76978_0_.minZ);
        worldrenderer.addVertex(p_76978_0_.minX, p_76978_0_.maxY, p_76978_0_.minZ);
        worldrenderer.setNormal(-1.0F, 0.0F, 0.0F);
        worldrenderer.addVertex(p_76978_0_.minX, p_76978_0_.minY, p_76978_0_.maxZ);
        worldrenderer.addVertex(p_76978_0_.minX, p_76978_0_.maxY, p_76978_0_.maxZ);
        worldrenderer.addVertex(p_76978_0_.minX, p_76978_0_.maxY, p_76978_0_.minZ);
        worldrenderer.addVertex(p_76978_0_.minX, p_76978_0_.minY, p_76978_0_.minZ);
        worldrenderer.setNormal(1.0F, 0.0F, 0.0F);
        worldrenderer.addVertex(p_76978_0_.maxX, p_76978_0_.minY, p_76978_0_.minZ);
        worldrenderer.addVertex(p_76978_0_.maxX, p_76978_0_.maxY, p_76978_0_.minZ);
        worldrenderer.addVertex(p_76978_0_.maxX, p_76978_0_.maxY, p_76978_0_.maxZ);
        worldrenderer.addVertex(p_76978_0_.maxX, p_76978_0_.minY, p_76978_0_.maxZ);
        worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
        tessellator.draw();
        GlStateManager.enableTexture2D();
    }

    /**
     * Renders the entity's shadow and fire (if its on fire). Args: entity, x, y, z, yaw, partialTickTime
     */
    public void doRenderShadowAndFire(Entity p_76979_1_, double p_76979_2_, double p_76979_4_, double p_76979_6_, float p_76979_8_, float p_76979_9_)
    {
        if (this.renderManager.options != null)
        {
            if (this.renderManager.options.fancyGraphics && this.shadowSize > 0.0F && !p_76979_1_.isInvisible() && this.renderManager.isRenderShadow())
            {
                double d3 = this.renderManager.getDistanceToCamera(p_76979_1_.posX, p_76979_1_.posY, p_76979_1_.posZ);
                float f2 = (float)((1.0D - d3 / 256.0D) * (double)this.shadowOpaque);

                if (f2 > 0.0F)
                {
                    this.renderShadow(p_76979_1_, p_76979_2_, p_76979_4_, p_76979_6_, f2, p_76979_9_);
                }
            }

            if (p_76979_1_.canRenderOnFire() && (!(p_76979_1_ instanceof EntityPlayer) || !((EntityPlayer)p_76979_1_).isSpectator()))
            {
                this.renderEntityOnFire(p_76979_1_, p_76979_2_, p_76979_4_, p_76979_6_, p_76979_9_);
            }
        }
    }

    /**
     * Returns the font renderer from the set render manager
     */
    public FontRenderer getFontRendererFromRenderManager()
    {
        return this.renderManager.getFontRenderer();
    }

    /**
     * Renders an entity's name above its head
     */
    protected void renderLivingLabel(Entity p_147906_1_, String p_147906_2_, double p_147906_3_, double p_147906_5_, double p_147906_7_, int p_147906_9_)
    {
        double d3 = p_147906_1_.getDistanceSqToEntity(this.renderManager.livingPlayer);

        if (d3 <= (double)(p_147906_9_ * p_147906_9_))
        {
            FontRenderer fontrenderer = this.getFontRendererFromRenderManager();
            float f = 1.6F;
            float f1 = 0.016666668F * f;
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)p_147906_3_ + 0.0F, (float)p_147906_5_ + p_147906_1_.height + 0.5F, (float)p_147906_7_);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
            GlStateManager.scale(-f1, -f1, f1);
            GlStateManager.disableLighting();
            GlStateManager.depthMask(false);
            GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            byte b0 = 0;

            if (p_147906_2_.equals("deadmau5"))
            {
                b0 = -10;
            }

            GlStateManager.disableTexture2D();
            worldrenderer.startDrawingQuads();
            int j = fontrenderer.getStringWidth(p_147906_2_) / 2;
            worldrenderer.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
            worldrenderer.addVertex((double)(-j - 1), (double)(-1 + b0), 0.0D);
            worldrenderer.addVertex((double)(-j - 1), (double)(8 + b0), 0.0D);
            worldrenderer.addVertex((double)(j + 1), (double)(8 + b0), 0.0D);
            worldrenderer.addVertex((double)(j + 1), (double)(-1 + b0), 0.0D);
            tessellator.draw();
            GlStateManager.enableTexture2D();
            fontrenderer.drawString(p_147906_2_, -fontrenderer.getStringWidth(p_147906_2_) / 2, b0, 553648127);
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            fontrenderer.drawString(p_147906_2_, -fontrenderer.getStringWidth(p_147906_2_) / 2, b0, -1);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
        }
    }

    public RenderManager func_177068_d()
    {
        return this.renderManager;
    }
}