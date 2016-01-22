package com.arkcraft.module.item.common.tile;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;

public class TileFlashlight extends TileEntity implements IUpdatePlayerListBox
{
	public int ticks;

	public TileFlashlight()
	{
		super();
		ticks = 0;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);

		ticks = nbt.getInteger("ticks");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);

		nbt.setInteger("ticks", ticks);
	}

	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return new S35PacketUpdateTileEntity(this.pos, getBlockMetadata(), nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
	{
		NBTTagCompound nbt = pkt.getNbtCompound();
		if (nbt != null)
		{
			this.readFromNBT(nbt);
		}
	}

	@Override
	public void update()
	{
		if (++ticks > 2)
		{
			this.invalidate();
			this.worldObj.setBlockState(pos, Blocks.air.getDefaultState());
			return;
		}
	}
}