package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Barrier extends EntityFX
{
    private static final String __OBFID = "CL_00002615";

    protected Barrier(World worldIn, double p_i46286_2_, double p_i46286_4_, double p_i46286_6_, Item p_i46286_8_)
    {
        super(worldIn, p_i46286_2_, p_i46286_4_, p_i46286_6_, 0.0D, 0.0D, 0.0D);
        this.func_180435_a(Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getParticleIcon(p_i46286_8_));
        this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
        this.motionX = this.motionY = this.motionZ = 0.0D;
        this.particleGravity = 0.0F;
        this.particleMaxAge = 80;
    }

    public int getFXLayer()
    {
        return 1;
    }

    public void func_180434_a(WorldRenderer p_180434_1_, Entity p_180434_2_, float p_180434_3_, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_)
    {
        float f6 = this.particleIcon.getMinU();
        float f7 = this.particleIcon.getMaxU();
        float f8 = this.particleIcon.getMinV();
        float f9 = this.particleIcon.getMaxV();
        float f10 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)p_180434_3_ - interpPosX);
        float f11 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)p_180434_3_ - interpPosY);
        float f12 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)p_180434_3_ - interpPosZ);
        p_180434_1_.setColorOpaque_F(this.particleRed, this.particleGreen, this.particleBlue);
        float f13 = 0.5F;
        p_180434_1_.addVertexWithUV((double)(f10 - p_180434_4_ * f13 - p_180434_7_ * f13), (double)(f11 - p_180434_5_ * f13), (double)(f12 - p_180434_6_ * f13 - p_180434_8_ * f13), (double)f7, (double)f9);
        p_180434_1_.addVertexWithUV((double)(f10 - p_180434_4_ * f13 + p_180434_7_ * f13), (double)(f11 + p_180434_5_ * f13), (double)(f12 - p_180434_6_ * f13 + p_180434_8_ * f13), (double)f7, (double)f8);
        p_180434_1_.addVertexWithUV((double)(f10 + p_180434_4_ * f13 + p_180434_7_ * f13), (double)(f11 + p_180434_5_ * f13), (double)(f12 + p_180434_6_ * f13 + p_180434_8_ * f13), (double)f6, (double)f8);
        p_180434_1_.addVertexWithUV((double)(f10 + p_180434_4_ * f13 - p_180434_7_ * f13), (double)(f11 - p_180434_5_ * f13), (double)(f12 + p_180434_6_ * f13 - p_180434_8_ * f13), (double)f6, (double)f9);
    }

    @SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory
        {
            private static final String __OBFID = "CL_00002614";

            public EntityFX getEntityFX(int p_178902_1_, World worldIn, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int ... p_178902_15_)
            {
                return new Barrier(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, Item.getItemFromBlock(Blocks.barrier));
            }
        }
}