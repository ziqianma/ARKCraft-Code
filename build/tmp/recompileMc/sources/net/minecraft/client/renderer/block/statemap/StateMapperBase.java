package net.minecraft.client.renderer.block.statemap;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class StateMapperBase implements IStateMapper
{
    protected Map mapStateModelLocations = Maps.newLinkedHashMap();
    private static final String __OBFID = "CL_00002479";

    public String getPropertyString(Map p_178131_1_)
    {
        StringBuilder stringbuilder = new StringBuilder();
        Iterator iterator = p_178131_1_.entrySet().iterator();

        while (iterator.hasNext())
        {
            Entry entry = (Entry)iterator.next();

            if (stringbuilder.length() != 0)
            {
                stringbuilder.append(",");
            }

            IProperty iproperty = (IProperty)entry.getKey();
            Comparable comparable = (Comparable)entry.getValue();
            stringbuilder.append(iproperty.getName());
            stringbuilder.append("=");
            stringbuilder.append(iproperty.getName(comparable));
        }

        if (stringbuilder.length() == 0)
        {
            stringbuilder.append("normal");
        }

        return stringbuilder.toString();
    }

    public Map putStateModelLocations(Block p_178130_1_)
    {
        Iterator iterator = p_178130_1_.getBlockState().getValidStates().iterator();

        while (iterator.hasNext())
        {
            IBlockState iblockstate = (IBlockState)iterator.next();
            this.mapStateModelLocations.put(iblockstate, this.getModelResourceLocation(iblockstate));
        }

        return this.mapStateModelLocations;
    }

    protected abstract ModelResourceLocation getModelResourceLocation(IBlockState p_178132_1_);
}