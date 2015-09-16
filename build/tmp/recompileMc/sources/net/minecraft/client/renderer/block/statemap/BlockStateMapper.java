package net.minecraft.client.renderer.block.statemap;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockStateMapper
{
    private Map blockStateMap = Maps.newIdentityHashMap();
    private Set setBuiltInBlocks = Sets.newIdentityHashSet();
    private static final String __OBFID = "CL_00002478";

    public void registerBlockStateMapper(Block p_178447_1_, IStateMapper p_178447_2_)
    {
        this.blockStateMap.put(p_178447_1_, p_178447_2_);
    }

    public void registerBuiltInBlocks(Block ... p_178448_1_)
    {
        Collections.addAll(this.setBuiltInBlocks, p_178448_1_);
    }

    public Map putAllStateModelLocations()
    {
        IdentityHashMap identityhashmap = Maps.newIdentityHashMap();
        Iterator iterator = Block.blockRegistry.iterator();

        while (iterator.hasNext())
        {
            Block block = (Block)iterator.next();

            if (!this.setBuiltInBlocks.contains(block))
            {
                identityhashmap.putAll(((IStateMapper)Objects.firstNonNull(this.blockStateMap.get(block), new DefaultStateMapper())).putStateModelLocations(block));
            }
        }

        return identityhashmap;
    }
}