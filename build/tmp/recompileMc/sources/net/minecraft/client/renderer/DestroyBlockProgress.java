package net.minecraft.client.renderer;

import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class DestroyBlockProgress
{
    /**
     * entity ID of the player associated with this partially destroyed Block. Used to identify the Blocks in the client
     * Renderer, max 1 per player on a server
     */
    private final int miningPlayerEntId;
    private final BlockPos position;
    /** damage ranges from 1 to 10. -1 causes the client to delete the partial block renderer. */
    private int partialBlockProgress;
    /** keeps track of how many ticks this PartiallyDestroyedBlock already exists */
    private int createdAtCloudUpdateTick;
    private static final String __OBFID = "CL_00001427";

    public DestroyBlockProgress(int p_i45925_1_, BlockPos p_i45925_2_)
    {
        this.miningPlayerEntId = p_i45925_1_;
        this.position = p_i45925_2_;
    }

    public BlockPos getPosition()
    {
        return this.position;
    }

    /**
     * inserts damage value into this partially destroyed Block. -1 causes client renderer to delete it, otherwise
     * ranges from 1 to 10
     */
    public void setPartialBlockDamage(int damage)
    {
        if (damage > 10)
        {
            damage = 10;
        }

        this.partialBlockProgress = damage;
    }

    public int getPartialBlockDamage()
    {
        return this.partialBlockProgress;
    }

    /**
     * saves the current Cloud update tick into the PartiallyDestroyedBlock
     */
    public void setCloudUpdateTick(int p_82744_1_)
    {
        this.createdAtCloudUpdateTick = p_82744_1_;
    }

    /**
     * retrieves the 'date' at which the PartiallyDestroyedBlock was created
     */
    public int getCreationCloudUpdateTick()
    {
        return this.createdAtCloudUpdateTick;
    }
}