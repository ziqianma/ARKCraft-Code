package net.minecraft.util;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MovementInput
{
    /** The speed at which the player is strafing. Postive numbers to the left and negative to the right. */
    public float moveStrafe;
    /** The speed at which the player is moving forward. Negative numbers will move backwards. */
    public float moveForward;
    public boolean jump;
    public boolean sneak;
    private static final String __OBFID = "CL_00000936";

    public void updatePlayerMoveState() {}
}