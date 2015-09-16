package net.minecraft.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.client.resources.model.WeightedBakedModel;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockRendererDispatcher implements IResourceManagerReloadListener
{
    private BlockModelShapes blockModelShapes;
    private final GameSettings gameSettings;
    private final BlockModelRenderer blockModelRenderer = new BlockModelRenderer();
    private final ChestRenderer chestRenderer = new ChestRenderer();
    private final BlockFluidRenderer fluidRenderer = new BlockFluidRenderer();
    private static final String __OBFID = "CL_00002520";

    public BlockRendererDispatcher(BlockModelShapes p_i46237_1_, GameSettings p_i46237_2_)
    {
        this.blockModelShapes = p_i46237_1_;
        this.gameSettings = p_i46237_2_;
    }

    public BlockModelShapes getBlockModelShapes()
    {
        return this.blockModelShapes;
    }

    public void renderBlockDamage(IBlockState p_175020_1_, BlockPos p_175020_2_, TextureAtlasSprite p_175020_3_, IBlockAccess p_175020_4_)
    {
        Block block = p_175020_1_.getBlock();
        int i = block.getRenderType();

        if (i == 3)
        {
            p_175020_1_ = block.getActualState(p_175020_1_, p_175020_4_, p_175020_2_);
            IBakedModel ibakedmodel = this.blockModelShapes.getModelForState(p_175020_1_);

            if(ibakedmodel instanceof net.minecraftforge.client.model.ISmartBlockModel)
            {
                IBlockState extendedState = block.getExtendedState(p_175020_1_, p_175020_4_, p_175020_2_);
                for ( net.minecraft.util.EnumWorldBlockLayer layer : net.minecraft.util.EnumWorldBlockLayer.values() )
                {
                    if ( block.canRenderInLayer( layer ) )
                    {
                        net.minecraftforge.client.ForgeHooksClient.setRenderLayer(layer);

                        IBakedModel targetLayer = ((net.minecraftforge.client.model.ISmartBlockModel)ibakedmodel).handleBlockState(extendedState);
                        IBakedModel damageModel = (new SimpleBakedModel.Builder(targetLayer, p_175020_3_)).makeBakedModel();
                        this.blockModelRenderer.renderModel(p_175020_4_, damageModel, p_175020_1_, p_175020_2_, Tessellator.getInstance().getWorldRenderer());
                    }
                }
                return;
            }

            IBakedModel ibakedmodel1 = (new SimpleBakedModel.Builder(ibakedmodel, p_175020_3_)).makeBakedModel();
            this.blockModelRenderer.renderModel(p_175020_4_, ibakedmodel1, p_175020_1_, p_175020_2_, Tessellator.getInstance().getWorldRenderer());
        }
    }

    public boolean renderBlock(IBlockState p_175018_1_, BlockPos p_175018_2_, IBlockAccess p_175018_3_, WorldRenderer p_175018_4_)
    {
        try
        {
            int i = p_175018_1_.getBlock().getRenderType();

            if (i == -1)
            {
                return false;
            }
            else
            {
                switch (i)
                {
                    case 1:
                        return this.fluidRenderer.renderFluid(p_175018_3_, p_175018_1_, p_175018_2_, p_175018_4_);
                    case 2:
                        return false;
                    case 3:
                        IBakedModel ibakedmodel = this.getModelFromBlockState(p_175018_1_, p_175018_3_, p_175018_2_);
                        return this.blockModelRenderer.renderModel(p_175018_3_, ibakedmodel, p_175018_1_, p_175018_2_, p_175018_4_);
                    default:
                        return false;
                }
            }
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Tesselating block in world");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being tesselated");
            CrashReportCategory.addBlockInfo(crashreportcategory, p_175018_2_, p_175018_1_.getBlock(), p_175018_1_.getBlock().getMetaFromState(p_175018_1_));
            throw new ReportedException(crashreport);
        }
    }

    public BlockModelRenderer getBlockModelRenderer()
    {
        return this.blockModelRenderer;
    }

    private IBakedModel getBakedModel(IBlockState p_175017_1_, BlockPos p_175017_2_)
    {
        IBakedModel ibakedmodel = this.blockModelShapes.getModelForState(p_175017_1_);

        if (p_175017_2_ != null && this.gameSettings.allowBlockAlternatives && ibakedmodel instanceof WeightedBakedModel)
        {
            ibakedmodel = ((WeightedBakedModel)ibakedmodel).getAlternativeModel(MathHelper.getPositionRandom(p_175017_2_));
        }

        return ibakedmodel;
    }

    public IBakedModel getModelFromBlockState(IBlockState p_175022_1_, IBlockAccess p_175022_2_, BlockPos p_175022_3_)
    {
        Block block = p_175022_1_.getBlock();

        if (p_175022_2_.getWorldType() != WorldType.DEBUG_WORLD)
        {
            try
            {
                p_175022_1_ = block.getActualState(p_175022_1_, p_175022_2_, p_175022_3_);
            }
            catch (Exception exception)
            {
                ;
            }
        }

        IBakedModel ibakedmodel = this.blockModelShapes.getModelForState(p_175022_1_);

        if (p_175022_3_ != null && this.gameSettings.allowBlockAlternatives && ibakedmodel instanceof WeightedBakedModel)
        {
            ibakedmodel = ((WeightedBakedModel)ibakedmodel).getAlternativeModel(MathHelper.getPositionRandom(p_175022_3_));
        }

        if(ibakedmodel instanceof net.minecraftforge.client.model.ISmartBlockModel)
        {
            IBlockState extendedState = block.getExtendedState(p_175022_1_, p_175022_2_, p_175022_3_);
            ibakedmodel = ((net.minecraftforge.client.model.ISmartBlockModel)ibakedmodel).handleBlockState(extendedState);
        }

        return ibakedmodel;
    }

    public void renderBlockBrightness(IBlockState p_175016_1_, float p_175016_2_)
    {
        int i = p_175016_1_.getBlock().getRenderType();

        if (i != -1)
        {
            switch (i)
            {
                case 1:
                default:
                    break;
                case 2:
                    this.chestRenderer.renderChestBrightness(p_175016_1_.getBlock(), p_175016_2_);
                    break;
                case 3:
                    IBakedModel ibakedmodel = this.getBakedModel(p_175016_1_, (BlockPos)null);
                    this.blockModelRenderer.renderModelBrightness(ibakedmodel, p_175016_1_, p_175016_2_, true);
            }
        }
    }

    public boolean isRenderTypeChest(Block p_175021_1_, int p_175021_2_)
    {
        if (p_175021_1_ == null)
        {
            return false;
        }
        else
        {
            int j = p_175021_1_.getRenderType();
            return j == 3 ? false : j == 2;
        }
    }

    public void onResourceManagerReload(IResourceManager resourceManager)
    {
        this.fluidRenderer.initAtlasSprites();
    }
}