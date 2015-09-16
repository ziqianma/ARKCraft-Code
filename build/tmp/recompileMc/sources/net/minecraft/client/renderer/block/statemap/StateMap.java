package net.minecraft.client.renderer.block.statemap;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class StateMap extends StateMapperBase
{
    private final IProperty property;
    private final String suffix;
    private final List listProperties;
    private static final String __OBFID = "CL_00002476";

    private StateMap(IProperty p_i46210_1_, String p_i46210_2_, List p_i46210_3_)
    {
        this.property = p_i46210_1_;
        this.suffix = p_i46210_2_;
        this.listProperties = p_i46210_3_;
    }

    protected ModelResourceLocation getModelResourceLocation(IBlockState p_178132_1_)
    {
        LinkedHashMap linkedhashmap = Maps.newLinkedHashMap(p_178132_1_.getProperties());
        String s;

        if (this.property == null)
        {
            s = ((ResourceLocation)Block.blockRegistry.getNameForObject(p_178132_1_.getBlock())).toString();
        }
        else
        {
            s = String.format("%s:%s", ((ResourceLocation)Block.blockRegistry.getNameForObject(p_178132_1_.getBlock())).getResourceDomain(), this.property.getName((Comparable)linkedhashmap.remove(this.property)));
        }

        if (this.suffix != null)
        {
            s = s + this.suffix;
        }

        Iterator iterator = this.listProperties.iterator();

        while (iterator.hasNext())
        {
            IProperty iproperty = (IProperty)iterator.next();
            linkedhashmap.remove(iproperty);
        }

        return new ModelResourceLocation(s, this.getPropertyString(linkedhashmap));
    }

    StateMap(IProperty p_i46211_1_, String p_i46211_2_, List p_i46211_3_, Object p_i46211_4_)
    {
        this(p_i46211_1_, p_i46211_2_, p_i46211_3_);
    }

    @SideOnly(Side.CLIENT)
    public static class Builder
        {
            private IProperty builderProperty;
            private String builderSuffix;
            private final List builderListProperties = Lists.newArrayList();
            private static final String __OBFID = "CL_00002474";

            public StateMap.Builder setProperty(IProperty p_178440_1_)
            {
                this.builderProperty = p_178440_1_;
                return this;
            }

            public StateMap.Builder setBuilderSuffix(String p_178439_1_)
            {
                this.builderSuffix = p_178439_1_;
                return this;
            }

            /**
             * Add properties that will not be used to compute all possible states of a block, used for block rendering
             * to ignore some property that does not alter block's appearance
             */
            public StateMap.Builder addPropertiesToIgnore(IProperty ... p_178442_1_)
            {
                Collections.addAll(this.builderListProperties, p_178442_1_);
                return this;
            }

            public StateMap build()
            {
                return new StateMap(this.builderProperty, this.builderSuffix, this.builderListProperties, null);
            }
        }
}