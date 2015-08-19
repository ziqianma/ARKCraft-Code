package com.arkcraft.mod.core.machine.temp;

public interface ISaveableInventory<S extends AbstractSave> {

	public void readFromNBT(S save);
	public void writeToNBT(S save);
	
}
