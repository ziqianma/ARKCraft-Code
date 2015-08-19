package com.arkcraft.mod.core.machine.temp;

import net.minecraft.nbt.NBTTagCompound;

public abstract class AbstractSave {

	protected String UID;
	protected NBTTagCompound nbt;
	protected boolean manualSaving = false;
	
	protected AbstractSave(NBTTagCompound data) {
		nbt = data;
		if(nbt == null) nbt = new NBTTagCompound();
	}
	
	public AbstractSave(String UID) { load(UID); }
	public void setManualSaving() { manualSaving = true; }
	
	public abstract byte getType();
	public abstract void setType(byte val);
	public abstract void save();
	protected abstract void load(String UID);
	
}
