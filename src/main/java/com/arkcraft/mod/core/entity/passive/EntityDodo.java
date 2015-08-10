package com.arkcraft.mod.core.entity.passive;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInvBasic;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

import com.arkcraft.mod.core.GlobalAdditions;
import com.arkcraft.mod.core.Main;
import com.arkcraft.mod.core.entity.ai.EntityDodoAILookIdle;
import com.arkcraft.mod.core.items.ARKFood;
import com.arkcraft.mod.lib.LogHelper;

/***
 * 
 * @author wildbill22
 *
 */
public class EntityDodo extends EntityChicken implements IInvBasic {
//	private ItemStack[] dodoItemStacks = new ItemStack[9];
	public IInventory invDodo;

	private int DODO_EYE_WATCHER = 21;
	public boolean isEyesOpen() {
		return (this.dataWatcher.getWatchableObjectByte(DODO_EYE_WATCHER) & 1) != 0;
	}
	public void setEyesOpen(boolean eyesOpen) {
		byte b0 = (byte) (eyesOpen ? 1 : 0);
		this.dataWatcher.updateObject(DODO_EYE_WATCHER, Byte.valueOf(b0));
	}

	private int DODO_CHEST_WATCHER = 22;
	public boolean isChested() {
		return (this.dataWatcher.getWatchableObjectByte(DODO_CHEST_WATCHER) & 1) != 0;
	}
	public void setChested(boolean chested) {
		if (!this.isChild()) {
			byte b0 = (byte) (chested ? 1 : 0);
			this.dataWatcher.updateObject(DODO_CHEST_WATCHER, Byte.valueOf(b0));
		}
	}

	public EntityDodo(World worldIn) {
		super(worldIn);

		this.getDataWatcher().addObject(DODO_EYE_WATCHER,
				Byte.valueOf((byte) 1));
		// TODO: Set to 0 after the code to add the pack is added.
		this.getDataWatcher().addObject(DODO_CHEST_WATCHER,
				Byte.valueOf((byte) 1));
		
		this.invDodo = new InventoryBasic("Items", false, 27);

		// Replace Idle task with one that blinks eyes
		this.tasks.taskEntries.clear();
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIPanic(this, 1.4D));
		this.tasks.addTask(2, new EntityAIMate(this, 1.0D));
		this.tasks.addTask(3, new EntityAITempt(this, 1.0D,
				GlobalAdditions.narcoBerry, false));
		this.tasks.addTask(4, new EntityAIFollowParent(this, 1.1D));
		this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(6, new EntityAIWatchClosest(this,
				EntityPlayer.class, 6.0F));
		this.tasks.addTask(7, new EntityDodoAILookIdle(this));
	}

	// TODO: Update when we have a dodo feather
	@Override
	protected Item getDropItem() {
		return Items.feather;
	}

	/**
	 * Drop 0-2 items of this living's type
	 */
	// TODO: Update to drop backpack when we have that item
	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
		int j = this.rand.nextInt(3) + this.rand.nextInt(1 + p_70628_2_);

		for (int k = 0; k < j; ++k) {
			this.dropItem(Items.feather, 1); // TODO: Dodo feather instead
		}

		if (this.isBurning()) {
			this.dropItem(GlobalAdditions.porkchop_cooked, 1);
		} else {
			this.dropItem(GlobalAdditions.porkchop_raw, 1);
		}
	}

	@Override
	public boolean interact(EntityPlayer p) {
		LogHelper.info("The player right clicked a Dodo.");
		if (isChested()) {
			p.openGui(Main.instance, GlobalAdditions.guiIDInvDodo, worldObj, p
					.getPosition().getX(), p.getPosition().getY(), p
					.getPosition().getZ());
			return true;
		}
		return false;
	}

	@Override
	public EntityDodo createChild(EntityAgeable ageable) {
		return new EntityDodo(this.worldObj);
	}

	/**
	 * Checks if the parameter is an item which this animal can be fed to breed
	 * it (wheat, carrots or seeds depending on the animal type)
	 */
	@Override
	public boolean isBreedingItem(ItemStack stack) {
		return stack != null ? stack.getItem() instanceof ARKFood : false;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		NBTTagList dataForAllSlots = new NBTTagList();
//		for (int i = 0; i < this.dodoItemStacks.length; ++i) {
//			if (this.dodoItemStacks[i] != null)	{
//				NBTTagCompound dataForThisSlot = new NBTTagCompound();
//				dataForThisSlot.setByte("Slot", (byte) i);
//				this.dodoItemStacks[i].writeToNBT(dataForThisSlot);
//				dataForAllSlots.appendTag(dataForThisSlot);
//			}
		for (int i = 0; i < this.invDodo.getSizeInventory(); ++i) {
			if (this.invDodo.getStackInSlot(i) != null)	{
				NBTTagCompound dataForThisSlot = new NBTTagCompound();
				dataForThisSlot.setByte("Slot", (byte) i);
				this.invDodo.getStackInSlot(i).writeToNBT(dataForThisSlot);
				dataForAllSlots.appendTag(dataForThisSlot);
			}
		}
		// the array of hashmaps is then inserted into the parent hashmap for the container
		nbt.setTag("Items", dataForAllSlots);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		final byte NBT_TYPE_COMPOUND = 10;  
		NBTTagList dataForAllSlots = nbt.getTagList("Items", NBT_TYPE_COMPOUND);

//		Arrays.fill(dodoItemStacks, null);   
		for (int i = 0; i < dataForAllSlots.tagCount(); ++i) {
			NBTTagCompound dataForOneSlot = dataForAllSlots.getCompoundTagAt(i);
			int slotIndex = dataForOneSlot.getByte("Slot") & 255;

//			if (slotIndex >= 0 && slotIndex < this.dodoItemStacks.length) {
//				this.dodoItemStacks[slotIndex] = ItemStack.loadItemStackFromNBT(dataForOneSlot);
			if (slotIndex >= 0 && slotIndex < this.invDodo.getSizeInventory()) {
				this.invDodo.setInventorySlotContents(slotIndex, ItemStack.loadItemStackFromNBT(dataForOneSlot));
			}
		}
	}

	// From IInvBasic 
	@Override
	public void onInventoryChanged(InventoryBasic invBasic) {

	}
}
