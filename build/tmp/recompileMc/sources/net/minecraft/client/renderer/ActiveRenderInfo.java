package net.minecraft.client.renderer;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

@SideOnly(Side.CLIENT)
public class ActiveRenderInfo
{
    private static final IntBuffer field_178814_a = GLAllocation.createDirectIntBuffer(16);
    private static final FloatBuffer field_178812_b = GLAllocation.createDirectFloatBuffer(16);
    private static final FloatBuffer field_178813_c = GLAllocation.createDirectFloatBuffer(16);
    private static final FloatBuffer field_178810_d = GLAllocation.createDirectFloatBuffer(3);
    private static Vec3 position = new Vec3(0.0D, 0.0D, 0.0D);
    /** The X component of the entity's yaw rotation */
    private static float rotationX;
    /** The combined X and Z components of the entity's pitch rotation */
    private static float rotationXZ;
    /** The Z component of the entity's yaw rotation */
    private static float rotationZ;
    /** The Y component (scaled along the Z axis) of the entity's pitch rotation */
    private static float rotationYZ;
    /** The Y component (scaled along the X axis) of the entity's pitch rotation */
    private static float rotationXY;
    private static final String __OBFID = "CL_00000626";

    /**
     * Updates the current render info and camera location based on entity look angles and 1st/3rd person view mode
     */
    public static void updateRenderInfo(EntityPlayer p_74583_0_, boolean p_74583_1_)
    {
        GlStateManager.getFloat(2982, field_178812_b);
        GlStateManager.getFloat(2983, field_178813_c);
        GL11.glGetInteger(GL11.GL_VIEWPORT, field_178814_a);
        float f = (float)((field_178814_a.get(0) + field_178814_a.get(2)) / 2);
        float f1 = (float)((field_178814_a.get(1) + field_178814_a.get(3)) / 2);
        GLU.gluUnProject(f, f1, 0.0F, field_178812_b, field_178813_c, field_178814_a, field_178810_d);
        position = new Vec3((double)field_178810_d.get(0), (double)field_178810_d.get(1), (double)field_178810_d.get(2));
        int i = p_74583_1_ ? 1 : 0;
        float f2 = p_74583_0_.rotationPitch;
        float f3 = p_74583_0_.rotationYaw;
        rotationX = MathHelper.cos(f3 * (float)Math.PI / 180.0F) * (float)(1 - i * 2);
        rotationZ = MathHelper.sin(f3 * (float)Math.PI / 180.0F) * (float)(1 - i * 2);
        rotationYZ = -rotationZ * MathHelper.sin(f2 * (float)Math.PI / 180.0F) * (float)(1 - i * 2);
        rotationXY = rotationX * MathHelper.sin(f2 * (float)Math.PI / 180.0F) * (float)(1 - i * 2);
        rotationXZ = MathHelper.cos(f2 * (float)Math.PI / 180.0F);
    }

    public static Vec3 projectViewFromEntity(Entity p_178806_0_, double p_178806_1_)
    {
        double d1 = p_178806_0_.prevPosX + (p_178806_0_.posX - p_178806_0_.prevPosX) * p_178806_1_;
        double d2 = p_178806_0_.prevPosY + (p_178806_0_.posY - p_178806_0_.prevPosY) * p_178806_1_;
        double d3 = p_178806_0_.prevPosZ + (p_178806_0_.posZ - p_178806_0_.prevPosZ) * p_178806_1_;
        double d4 = d1 + position.xCoord;
        double d5 = d2 + position.yCoord;
        double d6 = d3 + position.zCoord;
        return new Vec3(d4, d5, d6);
    }

    public static Block getBlockAtEntityViewpoint(World worldIn, Entity p_180786_1_, float p_180786_2_)
    {
        Vec3 vec3 = projectViewFromEntity(p_180786_1_, (double)p_180786_2_);
        BlockPos blockpos = new BlockPos(vec3);
        IBlockState iblockstate = worldIn.getBlockState(blockpos);
        Block block = iblockstate.getBlock();

        if (block.getMaterial().isLiquid())
        {
            float f1 = 0.0F;

            if (iblockstate.getBlock() instanceof BlockLiquid)
            {
                f1 = BlockLiquid.getLiquidHeightPercent(((Integer)iblockstate.getValue(BlockLiquid.LEVEL)).intValue()) - 0.11111111F;
            }

            float f2 = (float)(blockpos.getY() + 1) - f1;

            if (vec3.yCoord >= (double)f2)
            {
                block = worldIn.getBlockState(blockpos.up()).getBlock();
            }
        }

        return block;
    }

    public static Vec3 getPosition()
    {
        return position;
    }

    public static float getRotationX()
    {
        /** The X component of the entity's yaw rotation */
        return rotationX;
    }

    public static float getRotationXZ()
    {
        /** The combined X and Z components of the entity's pitch rotation */
        return rotationXZ;
    }

    public static float getRotationZ()
    {
        /** The Z component of the entity's yaw rotation */
        return rotationZ;
    }

    public static float getRotationYZ()
    {
        /** The Y component (scaled along the Z axis) of the entity's pitch rotation */
        return rotationYZ;
    }

    public static float getRotationXY()
    {
        /** The Y component (scaled along the X axis) of the entity's pitch rotation */
        return rotationXY;
    }
}