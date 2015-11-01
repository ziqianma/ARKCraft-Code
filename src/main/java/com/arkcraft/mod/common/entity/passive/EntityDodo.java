package com.arkcraft.mod.common.entity.passive;

import com.arkcraft.mod.GlobalAdditions.GUI;
import com.arkcraft.mod.client.gui.InventoryDino;
import com.arkcraft.mod.common.ARKCraft;
import com.arkcraft.mod.common.entity.ai.EntityDodoAILookIdle;
import com.arkcraft.mod.common.items.ARKCraftItems;
import com.arkcraft.mod.common.items.ARKFood;
import com.arkcraft.mod.common.lib.LogHelper;
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
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

/***
 * 
 * @author wildbill22
 *
 */
public class EntityDodo extends EntityTameable {
	public InventoryDino invDodo;
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
		
		this.invDodo = new InventoryDino("Items", true, 9);

		this.tasks.taskEntries.clear();
        int p = 0;
		this.tasks.addTask(++p, new EntityAISwimming(this));
        this.tasks.addTask(++p, this.aiSit);
		this.tasks.addTask(++p, new EntityAIPanic(this, 1.4D));
		this.tasks.addTask(++p, new EntityAIMate(this, 1.0D));
		this.tasks.addTask(++p, new EntityAITempt(this, 1.0D,	ARKCraftItems.narcoBerry, false));
		this.tasks.addTask(++p, new EntityAIFollowParent(this, 1.1D));
		this.tasks.addTask(++p, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(++p, new EntityAIFollowOwner(this, 1.0D, 8.0F, 5.0F));
		this.tasks.addTask(++p, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		// Replace Idle task with one that blinks eyes
		this.tasks.addTask(++p, new EntityDodoAILookIdle(this));
		
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
            this.dropItem(ARKCraftItems.dodo_egg, 1);
            this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
        }
        this.field_70886_e += this.field_70889_i * 2.0F;
        if (!this.worldObj.isRemote && !this.isChild() && --this.timeUntilNextEgg <= 0) {
            this.playSound(ARKCraft.MODID + ":" + "dodo_defficating", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            this.dropItem(ARKCraftItems.dodo_feces, 1);
            this.timeUntilNextEgg = this.rand.nextInt(3000) + 3000;
        }
	}
    

    // No fall damage
	@Override
    public void fall(float distance, float damageMultiplier) {}

	@Override
	protected Item getDropItem() {
		return ARKCraftItems.dodo_feather;
	}

	/**
	 * Drop 0-2 items of this living's type
	 */
	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
		int j = this.rand.nextInt(3) + this.rand.nextInt(1 + p_70628_2_);
		for (int k = 0; k < j; ++k) {
			this.dropItem(ARKCraftItems.dodo_feather, 1);
		}
		if (this.isBurning()) {
			this.dropItem(ARKCraftItems.meat_cooked, 1);
		} else {
			this.dropItem(ARKCraftItems.meat_raw, 1);
		}
		if (this.isChested()) {
			this.dropItem(ARKCraftItems.dodo_bag, 1);
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
        if (!this.worldObj.isRemote)
        	LogHelper.info("The player right clicked a Dodo.");
        ItemStack itemstack = player.inventory.getCurrentItem();
        
		if (isTamed()) {
            if (this.isOwner(player)) {
            	if (!this.worldObj.isRemote)
            		LogHelper.info("The Dodo is tamed.");    		
	            if (player.isSneaking()) {
					if (isChested()) {
			            if (!this.worldObj.isRemote) {
			            	player.openGui(ARKCraft.instance, GUI.INV_DODO.getID(), this.worldObj,
			            			(int) Math.floor(this.posX), (int) this.posY, (int) Math.floor(this.posZ));
			            	LogHelper.info("EnityDodo: Opening GUI on Dodo at: " + this.posX + "," + this.posY + "," + this.posZ + " (" +
			            			(int) Math.floor(this.posX) + "," + (int) this.posY  + "," + (int) Math.floor(this.posZ) + ")");
			                this.aiSit.setSitting(this.isSitting());
			            	LogHelper.info("Dodo is sitting");
			                this.isJumping = false;
			                this.navigator.clearPathEntity();
			            }
						return true;
					}
				} // not sneaking
				else {
					if (itemstack != null) {
						if (itemstack.getItem() == ARKCraftItems.dodo_bag) {
							// Put Dodo Bag on Dodo
							if (!player.capabilities.isCreativeMode) {
								itemstack.stackSize--;
								if (itemstack.stackSize == 0)
				                    player.inventory.mainInventory[player.inventory.currentItem] = null;
							}
							setChested(true);
							return true;
						} else {
							// Breeding stuff
							return super.interact(player);
						}
					}
					this.setSitting(!this.isSitting());
				}
	
	            if (!this.worldObj.isRemote) {
		            if (this.isSitting())
		            	LogHelper.info("Dodo is sitting");
		            else
		            	LogHelper.info("Dodo is not sitting");
	            }
            }
            else {
            	if (!this.worldObj.isRemote)
            		LogHelper.info("The Dodo is tamed, but not yours.");
            }
		}
        // Tame the Dodo with meat
		else if (itemstack != null && isFavoriteFood(itemstack)) {
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
                    this.aiSit.setSitting(true);
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
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		nbt.setInteger("EggLayTime", this.timeUntilNextEgg);
		nbt.setBoolean("IsChested", isChested);
		if(this.isChested()) {
			this.invDodo.saveInventoryToNBT(nbt);
//			LogHelper.info("EntityDodo - writeEntityToNBT: Saved chest inventory.");
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
        this.invDodo.loadInventoryFromNBT(dataForAllSlots);
	}

	/**
     * Determines if an entity can despawn, used on idle far away entities
     */
	@Override
    protected boolean canDespawn() {
        return !this.isTamed() && this.ticksExisted > 2400;
    }
	
    @Override
    protected String getLivingSound() {
    	int idle = this.rand.nextInt(3) + 1;
		return ARKCraft.MODID + ":" + "dodo_idle_" + idle;
    }
    
    @Override
    protected String getHurtSound() {
    	int hurt = this.rand.nextInt(3) + 1;
		return ARKCraft.MODID + ":" + "dodo_hurt_" + hurt;
    }
    
    @Override
    protected String getDeathSound() {
		return ARKCraft.MODID + ":" + "dodo_death";
    }
    
	@Override
    protected void playStepSound(BlockPos p_180429_1_, Block p_180429_2_) {
        this.playSound("mob.chicken.step", 0.15F, 1.0F);
    }
	
    /**
     * The age value may be negative or positive or zero. If it's negative, it get's incremented on each tick, if it's
     * positive, it get's decremented each tick. Don't confuse this with EntityLiving.getAge. With a negative value the
     * Entity is considered a child.
     */
    @Override
    public int getGrowingAge() {
    	// Added the null check for the dossier
    	if (this.worldObj != null)
    		return this.worldObj.isRemote ? this.dataWatcher.getWatchableObjectByte(12) : this.field_175504_a;
    	else
    		return this.field_175504_a;
    }

	public boolean isFavoriteFood(ItemStack itemstack) {
		if (itemstack.getItem() instanceof ARKFood && 
				(itemstack.getItem() == ARKCraftItems.amarBerry || itemstack.getItem() == ARKCraftItems.azulBerry
				|| itemstack.getItem() == ARKCraftItems.mejoBerry || itemstack.getItem() == ARKCraftItems.tintoBerry)){
			return true;
		}
		return false;
	}
}
