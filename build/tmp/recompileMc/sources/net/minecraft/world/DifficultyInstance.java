package net.minecraft.world;

import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DifficultyInstance
{
    private final EnumDifficulty worldDifficulty;
    private final float additionalDifficulty;
    private static final String __OBFID = "CL_00002261";

    public DifficultyInstance(EnumDifficulty worldDifficulty, long worldTime, long chunkInhabitedTime, float moonPhaseFactor)
    {
        this.worldDifficulty = worldDifficulty;
        this.additionalDifficulty = this.calculateAdditionalDifficulty(worldDifficulty, worldTime, chunkInhabitedTime, moonPhaseFactor);
    }

    @SideOnly(Side.CLIENT)
    public float func_180168_b()
    {
        return this.additionalDifficulty;
    }

    public float getClampedAdditionalDifficulty()
    {
        return this.additionalDifficulty < 2.0F ? 0.0F : (this.additionalDifficulty > 4.0F ? 1.0F : (this.additionalDifficulty - 2.0F) / 2.0F);
    }

    private float calculateAdditionalDifficulty(EnumDifficulty difficulty, long worldTime, long chunkInhabitedTime, float moonPhaseFactor)
    {
        if (difficulty == EnumDifficulty.PEACEFUL)
        {
            return 0.0F;
        }
        else
        {
            boolean flag = difficulty == EnumDifficulty.HARD;
            float f1 = 0.75F;
            float f2 = MathHelper.clamp_float(((float)worldTime + -72000.0F) / 1440000.0F, 0.0F, 1.0F) * 0.25F;
            f1 += f2;
            float f3 = 0.0F;
            f3 += MathHelper.clamp_float((float)chunkInhabitedTime / 3600000.0F, 0.0F, 1.0F) * (flag ? 1.0F : 0.75F);
            f3 += MathHelper.clamp_float(moonPhaseFactor * 0.25F, 0.0F, f2);

            if (difficulty == EnumDifficulty.EASY)
            {
                f3 *= 0.5F;
            }

            f1 += f3;
            return (float)difficulty.getDifficultyId() * f1;
        }
    }
}