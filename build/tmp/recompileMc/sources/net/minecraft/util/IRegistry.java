package net.minecraft.util;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IRegistry extends Iterable
{
    @SideOnly(Side.CLIENT)
    Object getObject(Object p_82594_1_);

    /**
     * Register an object on this registry.
     */
    @SideOnly(Side.CLIENT)
    void putObject(Object p_82595_1_, Object p_82595_2_);
}