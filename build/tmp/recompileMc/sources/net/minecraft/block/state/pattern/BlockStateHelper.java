package net.minecraft.block.state.pattern;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;

public class BlockStateHelper implements Predicate
{
    private final BlockState blockstate;
    private final Map propertyPredicates = Maps.newHashMap();
    private static final String __OBFID = "CL_00002019";

    private BlockStateHelper(BlockState blockStateIn)
    {
        this.blockstate = blockStateIn;
    }

    public static BlockStateHelper forBlock(Block blockIn)
    {
        return new BlockStateHelper(blockIn.getBlockState());
    }

    public boolean matchesState(IBlockState testState)
    {
        if (testState != null && testState.getBlock().equals(this.blockstate.getBlock()))
        {
            Iterator iterator = this.propertyPredicates.entrySet().iterator();
            Entry entry;
            Comparable comparable;

            do
            {
                if (!iterator.hasNext())
                {
                    return true;
                }

                entry = (Entry)iterator.next();
                comparable = testState.getValue((IProperty)entry.getKey());
            }
            while (((Predicate)entry.getValue()).apply(comparable));

            return false;
        }
        else
        {
            return false;
        }
    }

    public BlockStateHelper where(IProperty property, Predicate is)
    {
        if (!this.blockstate.getProperties().contains(property))
        {
            throw new IllegalArgumentException(this.blockstate + " cannot support property " + property);
        }
        else
        {
            this.propertyPredicates.put(property, is);
            return this;
        }
    }

    public boolean apply(Object p_apply_1_)
    {
        return this.matchesState((IBlockState)p_apply_1_);
    }
}