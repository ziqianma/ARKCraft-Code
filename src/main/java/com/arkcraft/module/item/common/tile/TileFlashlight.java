package com.arkcraft.module.item.common.tile;

import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileFlashlight extends TileEntity implements ITickable
{
    public int ticks;

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        ticks = nbt.getInteger("Ticks");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);

        nbt.setInteger("Ticks", ticks);
    }

	@Override
	public void tick() 
	{
		ticks += 1;

        if (ticks > 2)
        {
            worldObj.setBlockState(pos, Blocks.air.getDefaultState());
        }
	}
}