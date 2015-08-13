package com.arkcraft.mod.core.entity.passive;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInvBasic;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
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
//public class EntityDodo extends EntityChicken implements IInvBasic {
public class EntityDodo extends EntityTameable implements IInvBasic {
	public IInventory invDodo;
	private boolean isChested = false;

	// Stuff from chicken:
    public float field_70886_e;
    public float destPos;
    public float field_70884_g;
    public float field_70888_h;
    public float field_70889_i = 1.0F;
    /** The time until the next egg is spawned. */
    public int timeUntilNextEgg;

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
		isChested = (this.dataWatcher.getWatchableObjectByte(DODO_CHEST_WATCHER) & 1) != 0;
		return isChested;
	}
	public void setChested(boolean chested) {
		if (!this.isChild() && this.isTamed()) {
			isChested = chested;
			byte b0 = (byte) (chested ? 1 : 0);
			this.dataWatcher.updateObject(DODO_CHEST_WATCHER, Byte.valueOf(b0));
		}
	}

	public EntityDodo(World worldIn) {
		super(worldIn);
        this.setSize(0.4F, 0.7F);
        this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;

		this.getDataWatcher().addObject(DODO_EYE_WATCHER, Byte.valueOf((byte) 1));
		this.getDataWatcher().addObject(DODO_CHEST_WATCHER,	Byte.valueOf((byte) 0));
		
		this.invDodo = new InventoryBasic("Items", false, 27);

		this.tasks.taskEntries.clear();
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIPanic(this, 1.4D));
		this.tasks.addTask(2, new EntityAIMate(this, 1.0D));
		this.tasks.addTask(3, new EntityAITempt(this, 1.0D,	GlobalAdditions.narcoBerry, false));
		this.tasks.addTask(4, new EntityAIFollowParent(this, 1.1D));
		this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(5, new EntityAIFollowOwner(this, 1.0D, 8.0F, 5.0F));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		// Replace Idle task with one that blinks eyes
		this.tasks.addTask(7, new EntityDodoAILookIdle(this));
		
		this.riddenByEntity = null;
	}

	@Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(4.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
    }
    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
	@Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.field_70888_h = this.field_70886_e;
        this.field_70884_g = this.destPos;
        this.destPos = (float)((double)this.destPos + (double)(this.onGround ? -1 : 4) * 0.3D);
        this.destPos = MathHelper.clamp_float(this.destPos, 0.0F, 1.0F);
        if (!this.onGround && this.field_70889_i < 1.0F) {
            this.field_70889_i = 1.0F;
        }
        this.field_70889_i = (float)((double)this.field_70889_i * 0.9D);
        if (!this.onGround && this.motionY < 0.0D) {
            this.motionY *= 0.6D;
        }
        this.field_70886_e += this.field_70889_i * 2.0F;
        if (!this.worldObj.isRemote && !this.isChild() && --this.timeUntilNextEgg <= 0) {
            this.playSound("mob.chicken.plop", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            this.dropItem(Items.egg, 1);
            this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
        }
    }

    // No fall damage
	@Override
    public void fall(float distance, float damageMultiplier) {}

	// TODO: Update when we have a dodo feather
	@Override
	protected Item getDropItem() {
		return Items.feather;
	}

	/**
	 * Drop 0-2 items of this living's type
	 */
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
		if (this.isChested()) {
			this.dropItem(GlobalAdditions.dodo_bag, 1);
			this.dropItemsInChest(this, this.invDodo);
		}
	}
	
    private void dropItemsInChest(Entity entity, IInventory inventory) {
        if (inventory != null && !this.worldObj.isRemote) {
            for (int i = 0; i < inventory.getSizeInventory(); ++i) {
                ItemStack itemstack = inventory.getStackInSlot(i);
                if (itemstack != null) {
                    this.entityDropItem(itemstack, 0.0F);
                }
            }
        }
    }

	@Override
    public void setTamed(boolean tamed) {
        super.setTamed(tamed);
        if (tamed) {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
            this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5D);
        }
    }

	@Override
	public boolean interact(EntityPlayer player) {
		LogHelper.info("The player right clicked a Dodo.");
        ItemStack itemstack = player.inventory.getCurrentItem();
		if (isChested()) {
			player.openGui(Main.instance, GlobalAdditions.guiIDInvDodo, worldObj, player
					.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ());
			return true;
		}
		else if (isTamed()) {
			LogHelper.info("The Dodo is tamed.");
			if (player.isSneaking()) {
				// Put Dodo Bag on Dodo
				if (itemstack.getItem() == GlobalAdditions.dodo_bag) {
					if (!player.capabilities.isCreativeMode) {
						itemstack.stackSize--;
						if (itemstack.stackSize == 0)
		                    player.inventory.mainInventory[player.inventory.currentItem] = null;
					}
					setChested(true);
					return true;
				}
			}
		}
        // Tame the Dodo with meat
        else if (itemstack != null && (itemstack.getItem() == GlobalAdditions.porkchop_raw 
        		|| itemstack.getItem() == GlobalAdditions.porkchop_cooked)) {
            if (!player.capabilities.isCreativeMode) {
                --itemstack.stackSize;
            }
            if (itemstack.stackSize <= 0) {
                player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
            }
            if (!this.worldObj.isRemote) {
                if (this.rand.nextInt(2) == 0) {
                    this.setTamed(true);
                    this.navigator.clearPathEntity();
                    this.setHealth(10.0F);
                    this.setOwnerId(player.getUniqueID().toString());
                    this.playTameEffect(true);
                    this.worldObj.setEntityState(this, (byte)7);
                }
                else {
                    this.playTameEffect(false);
                    this.worldObj.setEntityState(this, (byte)6);
                }
            }
            return true;
        }
		return super.interact(player);
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
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		nbt.setInteger("EggLayTime", this.timeUntilNextEgg);
		nbt.setBoolean("IsChested", isChested);
		/***
		 * So, some quick notes (Vastatio):
		 * Basically, I've seen that in EntityHorse the for loop starts with 2 right?
		 * Its because of the two checks of the getStackInSlot(1) and getStackInSlot(2) for armor/saddle.
		 * Because apparently, minecraft needs to register those as two separate nbt slots.
		 * So, here, i check if the EntityDodo isChested() before.
		 * I can get the items that are in each slot of the chest by using this.invDodo.getStackInSlot(i)
		 * if the slots in invDodo are not empty, then write that slot to nbt using a byte.
		 * then I write nbt to the currentItemStack?
		 * Then, after the for loop, I create another tag called "Items" that stores all the values that have been put into the NBTTagList.
		 * This is how I think this whole process works (this is from EntityHorse)
		 */
		
		/** (Bill): This is good, the horse needs a way to remove the saddle without killing it.
		 *  For the Dodo, we will just kill it when we want the saddle back, so we don't need those other two slots.
		 *  Also the Dodo will not have armor that goes in one of those slots. We do need those special slots for
		 *  other dinos though.
		 */
		if(this.isChested()) {
			NBTTagList nbtTags = new NBTTagList();
			for(int i = 0; i < this.invDodo.getSizeInventory(); i++) {
				ItemStack currentItemStack = this.invDodo.getStackInSlot(i);
				if(currentItemStack != null) {
					NBTTagCompound nbtCompound = new NBTTagCompound();
					nbtCompound.setByte("Slot", (byte)i);
					currentItemStack.writeToNBT(nbtCompound);
					nbtTags.appendTag(nbtCompound);
				}
			}
			nbt.setTag("Items", nbtTags);
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
        if (nbt.hasKey("EggLayTime")) {
            this.timeUntilNextEgg = nbt.getInteger("EggLayTime");
        }
        if (nbt.hasKey("IsChested")) {
        	this.setChested(nbt.getBoolean("IsChested"));
        }
		final byte NBT_TYPE_COMPOUND = 10;  
		NBTTagList dataForAllSlots = nbt.getTagList("Items", NBT_TYPE_COMPOUND);

		for (int i = 0; i < dataForAllSlots.tagCount(); ++i) {
			NBTTagCompound dataForOneSlot = dataForAllSlots.getCompoundTagAt(i);
			int slotIndex = dataForOneSlot.getByte("Slot") & 255;

			if (slotIndex >= 0 && slotIndex < this.invDodo.getSizeInventory()) {
				this.invDodo.setInventorySlotContents(slotIndex, ItemStack.loadItemStackFromNBT(dataForOneSlot));
			}
		}
	}

	/**
     * Determines if an entity can despawn, used on idle far away entities
     */
	@Override
    protected boolean canDespawn() {
        return !this.isTamed() && this.ticksExisted > 2400;
    }
	
	// From IInvBasic 
	@Override
	public void onInventoryChanged(InventoryBasic invBasic) {
		// Normally used to add and remove the armor and saddle or pack, but not
		// needed for the Dodo, as we don't have the armor and saddle slots
	}
	
    @Override
    protected String getLivingSound() {
    	int idle = this.rand.nextInt(3) + 1;
		return Main.MODID + ":" + "dodo_idle_" + idle;
    }
    
    @Override
    protected String getHurtSound() {
    	int hurt = this.rand.nextInt(3) + 1;
		return Main.MODID + ":" + "dodo_hurt_" + hurt;
    }
    
    @Override
    protected String getDeathSound() {
		return Main.MODID + ":" + "dodo_death";
    }
    
	@Override
    protected void playStepSound(BlockPos p_180429_1_, Block p_180429_2_) {
        this.playSound("mob.chicken.step", 0.15F, 1.0F);
    }
}
