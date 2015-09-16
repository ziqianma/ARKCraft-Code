package net.minecraft.client.resources.model;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandom;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class WeightedBakedModel implements IBakedModel
{
    private final int totalWeight;
    public final List models;
    private final IBakedModel baseModel;
    private static final String __OBFID = "CL_00002384";

    public WeightedBakedModel(List p_i46073_1_)
    {
        this.models = p_i46073_1_;
        this.totalWeight = WeightedRandom.getTotalWeight(p_i46073_1_);
        this.baseModel = ((WeightedBakedModel.MyWeighedRandomItem)p_i46073_1_.get(0)).model;
    }

    public List getFaceQuads(EnumFacing p_177551_1_)
    {
        return this.baseModel.getFaceQuads(p_177551_1_);
    }

    public List getGeneralQuads()
    {
        return this.baseModel.getGeneralQuads();
    }

    public boolean isAmbientOcclusion()
    {
        return this.baseModel.isAmbientOcclusion();
    }

    public boolean isGui3d()
    {
        return this.baseModel.isGui3d();
    }

    public boolean isBuiltInRenderer()
    {
        return this.baseModel.isBuiltInRenderer();
    }

    public TextureAtlasSprite getTexture()
    {
        return this.baseModel.getTexture();
    }

    public ItemCameraTransforms getItemCameraTransforms()
    {
        return this.baseModel.getItemCameraTransforms();
    }

    public IBakedModel getAlternativeModel(long p_177564_1_)
    {
        return ((WeightedBakedModel.MyWeighedRandomItem)WeightedRandom.getRandomItem(this.models, Math.abs((int)p_177564_1_ >> 16) % this.totalWeight)).model;
    }

    @SideOnly(Side.CLIENT)
    public static class Builder
        {
            private List listItems = Lists.newArrayList();
            private static final String __OBFID = "CL_00002383";

            public WeightedBakedModel.Builder add(IBakedModel p_177677_1_, int p_177677_2_)
            {
                this.listItems.add(new WeightedBakedModel.MyWeighedRandomItem(p_177677_1_, p_177677_2_));
                return this;
            }

            public WeightedBakedModel build()
            {
                Collections.sort(this.listItems);
                return new WeightedBakedModel(this.listItems);
            }

            public IBakedModel first()
            {
                return ((WeightedBakedModel.MyWeighedRandomItem)this.listItems.get(0)).model;
            }
        }

    @SideOnly(Side.CLIENT)
    static class MyWeighedRandomItem extends WeightedRandom.Item implements Comparable
        {
            protected final IBakedModel model;
            private static final String __OBFID = "CL_00002382";

            public MyWeighedRandomItem(IBakedModel p_i46072_1_, int p_i46072_2_)
            {
                super(p_i46072_2_);
                this.model = p_i46072_1_;
            }

            public int compareToItem(WeightedBakedModel.MyWeighedRandomItem p_177634_1_)
            {
                return ComparisonChain.start().compare(p_177634_1_.itemWeight, this.itemWeight).compare(this.getCountQuads(), p_177634_1_.getCountQuads()).result();
            }

            protected int getCountQuads()
            {
                int i = this.model.getGeneralQuads().size();
                EnumFacing[] aenumfacing = EnumFacing.values();
                int j = aenumfacing.length;

                for (int k = 0; k < j; ++k)
                {
                    EnumFacing enumfacing = aenumfacing[k];
                    i += this.model.getFaceQuads(enumfacing).size();
                }

                return i;
            }

            public String toString()
            {
                return "MyWeighedRandomItem{weight=" + this.itemWeight + ", model=" + this.model + '}';
            }

            public int compareTo(Object p_compareTo_1_)
            {
                return this.compareToItem((WeightedBakedModel.MyWeighedRandomItem)p_compareTo_1_);
            }
        }
}