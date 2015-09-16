package net.minecraft.client.particle;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EffectRenderer
{
    private static final ResourceLocation particleTextures = new ResourceLocation("textures/particle/particles.png");
    /** Reference to the World object. */
    protected World worldObj;
    private List[][] fxLayers = new List[4][];
    private List field_178933_d = Lists.newArrayList();
    private TextureManager renderer;
    /** RNG. */
    private Random rand = new Random();
    private Map field_178932_g = Maps.newHashMap();
    private static final String __OBFID = "CL_00000915";

    public EffectRenderer(World worldIn, TextureManager p_i1220_2_)
    {
        this.worldObj = worldIn;
        this.renderer = p_i1220_2_;

        for (int i = 0; i < 4; ++i)
        {
            this.fxLayers[i] = new List[2];

            for (int j = 0; j < 2; ++j)
            {
                this.fxLayers[i][j] = Lists.newArrayList();
            }
        }

        this.func_178930_c();
    }

    private void func_178930_c()
    {
        this.func_178929_a(EnumParticleTypes.EXPLOSION_NORMAL.getParticleID(), new EntityExplodeFX.Factory());
        this.func_178929_a(EnumParticleTypes.WATER_BUBBLE.getParticleID(), new EntityBubbleFX.Factory());
        this.func_178929_a(EnumParticleTypes.WATER_SPLASH.getParticleID(), new EntitySplashFX.Factory());
        this.func_178929_a(EnumParticleTypes.WATER_WAKE.getParticleID(), new EntityFishWakeFX.Factory());
        this.func_178929_a(EnumParticleTypes.WATER_DROP.getParticleID(), new EntityRainFX.Factory());
        this.func_178929_a(EnumParticleTypes.SUSPENDED.getParticleID(), new EntitySuspendFX.Factory());
        this.func_178929_a(EnumParticleTypes.SUSPENDED_DEPTH.getParticleID(), new EntityAuraFX.Factory());
        this.func_178929_a(EnumParticleTypes.CRIT.getParticleID(), new EntityCrit2FX.Factory());
        this.func_178929_a(EnumParticleTypes.CRIT_MAGIC.getParticleID(), new EntityCrit2FX.MagicFactory());
        this.func_178929_a(EnumParticleTypes.SMOKE_NORMAL.getParticleID(), new EntitySmokeFX.Factory());
        this.func_178929_a(EnumParticleTypes.SMOKE_LARGE.getParticleID(), new EntityCritFX.Factory());
        this.func_178929_a(EnumParticleTypes.SPELL.getParticleID(), new EntitySpellParticleFX.Factory());
        this.func_178929_a(EnumParticleTypes.SPELL_INSTANT.getParticleID(), new EntitySpellParticleFX.InstantFactory());
        this.func_178929_a(EnumParticleTypes.SPELL_MOB.getParticleID(), new EntitySpellParticleFX.MobFactory());
        this.func_178929_a(EnumParticleTypes.SPELL_MOB_AMBIENT.getParticleID(), new EntitySpellParticleFX.AmbientMobFactory());
        this.func_178929_a(EnumParticleTypes.SPELL_WITCH.getParticleID(), new EntitySpellParticleFX.WitchFactory());
        this.func_178929_a(EnumParticleTypes.DRIP_WATER.getParticleID(), new EntityDropParticleFX.WaterFactory());
        this.func_178929_a(EnumParticleTypes.DRIP_LAVA.getParticleID(), new EntityDropParticleFX.LavaFactory());
        this.func_178929_a(EnumParticleTypes.VILLAGER_ANGRY.getParticleID(), new EntityHeartFX.AngryVillagerFactory());
        this.func_178929_a(EnumParticleTypes.VILLAGER_HAPPY.getParticleID(), new EntityAuraFX.HappyVillagerFactory());
        this.func_178929_a(EnumParticleTypes.TOWN_AURA.getParticleID(), new EntityAuraFX.Factory());
        this.func_178929_a(EnumParticleTypes.NOTE.getParticleID(), new EntityNoteFX.Factory());
        this.func_178929_a(EnumParticleTypes.PORTAL.getParticleID(), new EntityPortalFX.Factory());
        this.func_178929_a(EnumParticleTypes.ENCHANTMENT_TABLE.getParticleID(), new EntityEnchantmentTableParticleFX.EnchantmentTable());
        this.func_178929_a(EnumParticleTypes.FLAME.getParticleID(), new EntityFlameFX.Factory());
        this.func_178929_a(EnumParticleTypes.LAVA.getParticleID(), new EntityLavaFX.Factory());
        this.func_178929_a(EnumParticleTypes.FOOTSTEP.getParticleID(), new EntityFootStepFX.Factory());
        this.func_178929_a(EnumParticleTypes.CLOUD.getParticleID(), new EntityCloudFX.Factory());
        this.func_178929_a(EnumParticleTypes.REDSTONE.getParticleID(), new EntityReddustFX.Factory());
        this.func_178929_a(EnumParticleTypes.SNOWBALL.getParticleID(), new EntityBreakingFX.SnowballFactory());
        this.func_178929_a(EnumParticleTypes.SNOW_SHOVEL.getParticleID(), new EntitySnowShovelFX.Factory());
        this.func_178929_a(EnumParticleTypes.SLIME.getParticleID(), new EntityBreakingFX.SlimeFactory());
        this.func_178929_a(EnumParticleTypes.HEART.getParticleID(), new EntityHeartFX.Factory());
        this.func_178929_a(EnumParticleTypes.BARRIER.getParticleID(), new Barrier.Factory());
        this.func_178929_a(EnumParticleTypes.ITEM_CRACK.getParticleID(), new EntityBreakingFX.Factory());
        this.func_178929_a(EnumParticleTypes.BLOCK_CRACK.getParticleID(), new EntityDiggingFX.Factory());
        this.func_178929_a(EnumParticleTypes.BLOCK_DUST.getParticleID(), new EntityBlockDustFX.Factory());
        this.func_178929_a(EnumParticleTypes.EXPLOSION_HUGE.getParticleID(), new EntityHugeExplodeFX.Factory());
        this.func_178929_a(EnumParticleTypes.EXPLOSION_LARGE.getParticleID(), new EntityLargeExplodeFX.Factory());
        this.func_178929_a(EnumParticleTypes.FIREWORKS_SPARK.getParticleID(), new EntityFireworkStarterFX_Factory());
        this.func_178929_a(EnumParticleTypes.MOB_APPEARANCE.getParticleID(), new MobAppearance.Factory());
    }

    public void func_178929_a(int p_178929_1_, IParticleFactory p_178929_2_)
    {
        this.field_178932_g.put(Integer.valueOf(p_178929_1_), p_178929_2_);
    }

    public void func_178926_a(Entity p_178926_1_, EnumParticleTypes p_178926_2_)
    {
        this.field_178933_d.add(new EntityParticleEmitter(this.worldObj, p_178926_1_, p_178926_2_));
    }

    /**
     * Spawns the relevant particle according to the particle id.
     *  
     * @param particleId The id of the particle
     */
    public EntityFX spawnEffectParticle(int particleId, double p_178927_2_, double p_178927_4_, double p_178927_6_, double p_178927_8_, double p_178927_10_, double p_178927_12_, int ... p_178927_14_)
    {
        IParticleFactory iparticlefactory = (IParticleFactory)this.field_178932_g.get(Integer.valueOf(particleId));

        if (iparticlefactory != null)
        {
            EntityFX entityfx = iparticlefactory.getEntityFX(particleId, this.worldObj, p_178927_2_, p_178927_4_, p_178927_6_, p_178927_8_, p_178927_10_, p_178927_12_, p_178927_14_);

            if (entityfx != null)
            {
                this.addEffect(entityfx);
                return entityfx;
            }
        }

        return null;
    }

    public void addEffect(EntityFX p_78873_1_)
    {
        if (p_78873_1_ == null) return; //Forge: Prevent modders from being bad and adding nulls causing untraceable NPEs.
        int i = p_78873_1_.getFXLayer();
        int j = p_78873_1_.func_174838_j() != 1.0F ? 0 : 1;

        if (this.fxLayers[i][j].size() >= 4000)
        {
            this.fxLayers[i][j].remove(0);
        }

        this.fxLayers[i][j].add(p_78873_1_);
    }

    public void updateEffects()
    {
        for (int i = 0; i < 4; ++i)
        {
            this.func_178922_a(i);
        }

        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = this.field_178933_d.iterator();

        while (iterator.hasNext())
        {
            EntityParticleEmitter entityparticleemitter = (EntityParticleEmitter)iterator.next();
            entityparticleemitter.onUpdate();

            if (entityparticleemitter.isDead)
            {
                arraylist.add(entityparticleemitter);
            }
        }

        this.field_178933_d.removeAll(arraylist);
    }

    private void func_178922_a(int p_178922_1_)
    {
        for (int j = 0; j < 2; ++j)
        {
            this.func_178925_a(this.fxLayers[p_178922_1_][j]);
        }
    }

    private void func_178925_a(List p_178925_1_)
    {
        ArrayList arraylist = Lists.newArrayList();

        for (int i = 0; i < p_178925_1_.size(); ++i)
        {
            EntityFX entityfx = (EntityFX)p_178925_1_.get(i);
            this.func_178923_d(entityfx);

            if (entityfx.isDead)
            {
                arraylist.add(entityfx);
            }
        }

        p_178925_1_.removeAll(arraylist);
    }

    private void func_178923_d(final EntityFX p_178923_1_)
    {
        try
        {
            p_178923_1_.onUpdate();
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Ticking Particle");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being ticked");
            final int i = p_178923_1_.getFXLayer();
            crashreportcategory.addCrashSectionCallable("Particle", new Callable()
            {
                private static final String __OBFID = "CL_00000916";
                public String call()
                {
                    return p_178923_1_.toString();
                }
            });
            crashreportcategory.addCrashSectionCallable("Particle Type", new Callable()
            {
                private static final String __OBFID = "CL_00000917";
                public String call()
                {
                    return i == 0 ? "MISC_TEXTURE" : (i == 1 ? "TERRAIN_TEXTURE" : (i == 3 ? "ENTITY_PARTICLE_TEXTURE" : "Unknown - " + i));
                }
            });
            throw new ReportedException(crashreport);
        }
    }

    /**
     * Renders all current particles. Args player, partialTickTime
     */
    public void renderParticles(Entity p_78874_1_, float partialTicks)
    {
        float f1 = ActiveRenderInfo.getRotationX();
        float f2 = ActiveRenderInfo.getRotationZ();
        float f3 = ActiveRenderInfo.getRotationYZ();
        float f4 = ActiveRenderInfo.getRotationXY();
        float f5 = ActiveRenderInfo.getRotationXZ();
        EntityFX.interpPosX = p_78874_1_.lastTickPosX + (p_78874_1_.posX - p_78874_1_.lastTickPosX) * (double)partialTicks;
        EntityFX.interpPosY = p_78874_1_.lastTickPosY + (p_78874_1_.posY - p_78874_1_.lastTickPosY) * (double)partialTicks;
        EntityFX.interpPosZ = p_78874_1_.lastTickPosZ + (p_78874_1_.posZ - p_78874_1_.lastTickPosZ) * (double)partialTicks;
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.alphaFunc(516, 0.003921569F);

        for (int i_nf = 0; i_nf < 3; ++i_nf)
        {
            final int i = i_nf;

            for (int j = 0; j < 2; ++j)
            {
                if (!this.fxLayers[i][j].isEmpty())
                {
                    switch (j)
                    {
                        case 0:
                            GlStateManager.depthMask(false);
                            break;
                        case 1:
                            GlStateManager.depthMask(true);
                    }

                    switch (i)
                    {
                        case 0:
                        default:
                            this.renderer.bindTexture(particleTextures);
                            break;
                        case 1:
                            this.renderer.bindTexture(TextureMap.locationBlocksTexture);
                    }

                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    Tessellator tessellator = Tessellator.getInstance();
                    WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                    worldrenderer.startDrawingQuads();

                    for (int k = 0; k < this.fxLayers[i][j].size(); ++k)
                    {
                        final EntityFX entityfx = (EntityFX)this.fxLayers[i][j].get(k);
                        worldrenderer.setBrightness(entityfx.getBrightnessForRender(partialTicks));

                        try
                        {
                            entityfx.func_180434_a(worldrenderer, p_78874_1_, partialTicks, f1, f5, f2, f3, f4);
                        }
                        catch (Throwable throwable)
                        {
                            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering Particle");
                            CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being rendered");
                            crashreportcategory.addCrashSectionCallable("Particle", new Callable()
                            {
                                private static final String __OBFID = "CL_00000918";
                                public String call()
                                {
                                    return entityfx.toString();
                                }
                            });
                            crashreportcategory.addCrashSectionCallable("Particle Type", new Callable()
                            {
                                private static final String __OBFID = "CL_00000919";
                                public String call()
                                {
                                    return i == 0 ? "MISC_TEXTURE" : (i == 1 ? "TERRAIN_TEXTURE" : (i == 3 ? "ENTITY_PARTICLE_TEXTURE" : "Unknown - " + i));
                                }
                            });
                            throw new ReportedException(crashreport);
                        }
                    }

                    tessellator.draw();
                }
            }
        }

        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.alphaFunc(516, 0.1F);
    }

    public void renderLitParticles(Entity p_78872_1_, float p_78872_2_)
    {
        float f1 = 0.017453292F;
        float f2 = MathHelper.cos(p_78872_1_.rotationYaw * 0.017453292F);
        float f3 = MathHelper.sin(p_78872_1_.rotationYaw * 0.017453292F);
        float f4 = -f3 * MathHelper.sin(p_78872_1_.rotationPitch * 0.017453292F);
        float f5 = f2 * MathHelper.sin(p_78872_1_.rotationPitch * 0.017453292F);
        float f6 = MathHelper.cos(p_78872_1_.rotationPitch * 0.017453292F);

        for (int i = 0; i < 2; ++i)
        {
            List list = this.fxLayers[3][i];

            if (!list.isEmpty())
            {
                Tessellator tessellator = Tessellator.getInstance();
                WorldRenderer worldrenderer = tessellator.getWorldRenderer();

                for (int j = 0; j < list.size(); ++j)
                {
                    EntityFX entityfx = (EntityFX)list.get(j);
                    worldrenderer.setBrightness(entityfx.getBrightnessForRender(p_78872_2_));
                    entityfx.func_180434_a(worldrenderer, p_78872_1_, p_78872_2_, f2, f6, f3, f4, f5);
                }
            }
        }
    }

    public void clearEffects(World worldIn)
    {
        this.worldObj = worldIn;

        for (int i = 0; i < 4; ++i)
        {
            for (int j = 0; j < 2; ++j)
            {
                this.fxLayers[i][j].clear();
            }
        }

        this.field_178933_d.clear();
    }

    public void func_180533_a(BlockPos p_180533_1_, IBlockState p_180533_2_)
    {
        if (!p_180533_2_.getBlock().isAir(worldObj, p_180533_1_) && !p_180533_2_.getBlock().addDestroyEffects(worldObj, p_180533_1_, this))
        {
            p_180533_2_ = p_180533_2_.getBlock().getActualState(p_180533_2_, this.worldObj, p_180533_1_);
            byte b0 = 4;

            for (int i = 0; i < b0; ++i)
            {
                for (int j = 0; j < b0; ++j)
                {
                    for (int k = 0; k < b0; ++k)
                    {
                        double d0 = (double)p_180533_1_.getX() + ((double)i + 0.5D) / (double)b0;
                        double d1 = (double)p_180533_1_.getY() + ((double)j + 0.5D) / (double)b0;
                        double d2 = (double)p_180533_1_.getZ() + ((double)k + 0.5D) / (double)b0;
                        this.addEffect((new EntityDiggingFX(this.worldObj, d0, d1, d2, d0 - (double)p_180533_1_.getX() - 0.5D, d1 - (double)p_180533_1_.getY() - 0.5D, d2 - (double)p_180533_1_.getZ() - 0.5D, p_180533_2_)).func_174846_a(p_180533_1_));
                    }
                }
            }
        }
    }

    /**
     * Adds block hit particles for the specified block
     *  
     * @param pos The block's coordinates
     * @param side The side the block was hit from
     */
    public void addBlockHitEffects(BlockPos pos, EnumFacing side)
    {
        IBlockState iblockstate = this.worldObj.getBlockState(pos);
        Block block = iblockstate.getBlock();

        if (block.getRenderType() != -1)
        {
            int i = pos.getX();
            int j = pos.getY();
            int k = pos.getZ();
            float f = 0.1F;
            double d0 = (double)i + this.rand.nextDouble() * (block.getBlockBoundsMaxX() - block.getBlockBoundsMinX() - (double)(f * 2.0F)) + (double)f + block.getBlockBoundsMinX();
            double d1 = (double)j + this.rand.nextDouble() * (block.getBlockBoundsMaxY() - block.getBlockBoundsMinY() - (double)(f * 2.0F)) + (double)f + block.getBlockBoundsMinY();
            double d2 = (double)k + this.rand.nextDouble() * (block.getBlockBoundsMaxZ() - block.getBlockBoundsMinZ() - (double)(f * 2.0F)) + (double)f + block.getBlockBoundsMinZ();

            if (side == EnumFacing.DOWN)
            {
                d1 = (double)j + block.getBlockBoundsMinY() - (double)f;
            }

            if (side == EnumFacing.UP)
            {
                d1 = (double)j + block.getBlockBoundsMaxY() + (double)f;
            }

            if (side == EnumFacing.NORTH)
            {
                d2 = (double)k + block.getBlockBoundsMinZ() - (double)f;
            }

            if (side == EnumFacing.SOUTH)
            {
                d2 = (double)k + block.getBlockBoundsMaxZ() + (double)f;
            }

            if (side == EnumFacing.WEST)
            {
                d0 = (double)i + block.getBlockBoundsMinX() - (double)f;
            }

            if (side == EnumFacing.EAST)
            {
                d0 = (double)i + block.getBlockBoundsMaxX() + (double)f;
            }

            this.addEffect((new EntityDiggingFX(this.worldObj, d0, d1, d2, 0.0D, 0.0D, 0.0D, iblockstate)).func_174846_a(pos).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
        }
    }

    public void func_178928_b(EntityFX p_178928_1_)
    {
        this.func_178924_a(p_178928_1_, 1, 0);
    }

    public void func_178931_c(EntityFX p_178931_1_)
    {
        this.func_178924_a(p_178931_1_, 0, 1);
    }

    private void func_178924_a(EntityFX p_178924_1_, int p_178924_2_, int p_178924_3_)
    {
        for (int k = 0; k < 4; ++k)
        {
            if (this.fxLayers[k][p_178924_2_].contains(p_178924_1_))
            {
                this.fxLayers[k][p_178924_2_].remove(p_178924_1_);
                this.fxLayers[k][p_178924_3_].add(p_178924_1_);
            }
        }
    }

    public String getStatistics()
    {
        int i = 0;

        for (int j = 0; j < 4; ++j)
        {
            for (int k = 0; k < 2; ++k)
            {
                i += this.fxLayers[j][k].size();
            }
        }

        return "" + i;
    }

    public void addBlockHitEffects(BlockPos pos, net.minecraft.util.MovingObjectPosition target)
    {
        Block block = worldObj.getBlockState(pos).getBlock();
        if (block != null && !block.addHitEffects(worldObj, target, this))
        {
            addBlockHitEffects(pos, target.sideHit);
        }
     }
}