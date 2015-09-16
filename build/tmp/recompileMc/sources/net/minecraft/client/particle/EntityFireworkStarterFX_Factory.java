package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EntityFireworkStarterFX_Factory implements IParticleFactory
{
    private static final String __OBFID = "CL_00002603";

    public EntityFX getEntityFX(int p_178902_1_, World worldIn, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int ... p_178902_15_)
    {
        EntityFireworkSparkFX entityfireworksparkfx = new EntityFireworkSparkFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, p_178902_9_, p_178902_11_, p_178902_13_, Minecraft.getMinecraft().effectRenderer);
        entityfireworksparkfx.setAlphaF(0.99F);
        return entityfireworksparkfx;
    }
}