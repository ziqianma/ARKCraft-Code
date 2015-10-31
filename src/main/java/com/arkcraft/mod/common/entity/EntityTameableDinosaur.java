package com.arkcraft.mod.common.entity;


import com.arkcraft.mod.GlobalAdditions.GUI;
import com.arkcraft.mod.client.gui.InventoryDino;
import com.arkcraft.mod.client.gui.InventorySaddle;
import com.arkcraft.mod.client.gui.InventoryTaming;
import com.arkcraft.mod.common.ARKCraft;
import com.arkcraft.mod.common.items.ARKCraftItems;
import com.arkcraft.mod.common.lib.LogHelper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

/***
 * 
 * @author wildbill22
 *
 */
public abstract class EntityTameableDinosaur extends EntityTameable {
	// Stuff that needs to be saved to NBT:
	public InventoryDino invTamedDino;
	public InventoryTaming invTaming;
	public InventorySaddle invSaddle;
	protected boolean isSaddled = false;
	protected boolean isTaming = false;

	// Other non-NBT variables:
	protected boolean isRideable = false;
	protected boolean isTameable = false;
	protected int tamingSeconds = 0;
	protected EntityAIBase attackPlayerTarget;
	public SaddleType saddleType;
	
	private int DINO_SADDLED_WATCHER = 22;
	public boolean isSaddled() {
		isSaddled = (this.dataWatcher.getWatchableObjectByte(DINO_SADDLED_WATCHER) & 1) != 0;
		return isSaddled;
	}
	public void setSaddled(boolean saddled) {
		if (!this.isChild() && this.isTamed()) {
			isSaddled = saddled;
			byte b0 = (byte) (saddled ? 1 : 0);
			this.dataWatcher.updateObject(DINO_SADDLED_WATCHER, Byte.valueOf(b0));
		}
	}
	public Item getSaddleType() {
		return this.saddleType.getSaddleItem();
	}
	public void setSaddleType(int type) {
		switch (type) {
		case 0:
			this.saddleType = SaddleType.NONE;
			break;
		case 1:
			this.saddleType = SaddleType.SMALL;
			break;
		case 2:
			this.saddleType = SaddleType.MEDIUM;
			break;
		case 3:
			this.saddleType = SaddleType.LARGE;
			break;
		}
	}
	
	public EntityTameableDinosaur(World worldIn) {
		super(worldIn);
	}

	protected EntityTameableDinosaur(World worldIn, SaddleType saddleType) {
		super(worldIn);
		this.getDataWatcher().addObject(DINO_SADDLED_WATCHER, Byte.valueOf((byte) 0));
        this.isTameable = true;
		this.tamingSeconds = 120; // Set this before the InventoryTaming (this is the default if not set)
		this.invTamedDino = new InventoryDino("Items", true, saddleType.getInventorySize());
		this.invTaming = new InventoryTaming(this);
		this.saddleType = saddleType;
		this.invSaddle = new InventorySaddle(this);
	}
	
	// Use this constructor if you want to create a dino that is not tameable
	protected EntityTameableDinosaur(World worldIn, SaddleType saddleType, boolean isTameable, int tamingSeconds) {
		super(worldIn);
		this.getDataWatcher().addObject(DINO_SADDLED_WATCHER, Byte.valueOf((byte) 0));
        this.isTameable = isTameable;
        this.saddleType = saddleType;
        if (isTameable) {
        	this.tamingSeconds = tamingSeconds; // This must be before the InventoryTaming
        	this.invTamedDino = new InventoryDino("Items", true, saddleType.getInventorySize());
        	this.invTaming = new InventoryTaming(this);
    		this.invSaddle = new InventorySaddle(this);
        }
        
        // Stuff for when tamed
        this.tasks.addTask(1, this.aiSit);
	}
	
	/**
	 * Clears previous AI Tasks, so the ones defined above will
	 * actually perform.
	 */
	protected void clearAITasks() {
		tasks.taskEntries.clear();
		targetTasks.taskEntries.clear();
	}

    /**
     * Sets the active target the Task system uses for tracking
     */
    public void setAttackTarget(EntityLivingBase target) {
        super.setAttackTarget(target);
        if (target == null) {
            this.setAngry(false);
        }
        else if (!this.isTamed()) {
            this.setAngry(true);
        }
    }

    //////////////////////////////////////
    // Properties that all dinos must have
    
    //* This must check that the item is an instanceof ItemFood */
    public abstract boolean isFavoriteFood(ItemStack itemstack);
    
    //////////////////////////////

    /**
     * Determines whether this wolf is angry or not.
     */
    public boolean isAngry() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 2) != 0;
    }

    /**
     * Sets whether this wolf is angry or not.
     */
    public void setAngry(boolean angry) {
        byte b0 = this.dataWatcher.getWatchableObjectByte(16);

        if (angry) {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 | 2)));
        }
        else {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 & -3)));
        }
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
			this.dropItem(ARKCraftItems.meat_cooked, 1);
		} else {
			this.dropItem(ARKCraftItems.meat_raw, 1);
		}
		if (this.isSaddled()) {
			this.dropItem(ARKCraftItems.saddle_large, 1);
		}
		if (this.isTamed()){
			this.dropItemsInChest(this, this.invTamedDino);			
		}
	}
	
	// TODO: Make these drop in a random circle around where the entity dies
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

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
	@Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        if (isTameable) {
        	nbt.setBoolean("isSaddled", isSaddled);
        	nbt.setBoolean("isTaming", isTaming);
        }
        if (this.isTamed()){
   			this.invTamedDino.saveInventoryToNBT(nbt);        	
        } else {
        	this.invTaming.saveInventoryToNBT(nbt);        	
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
	@Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        if (isTameable) {
	        if (nbt.hasKey("isSaddled")) {
	        	this.setSaddled(nbt.getBoolean("isSaddled"));
	        }
	        if (nbt.hasKey("isTaming")) {
	        	this.setIsTaming(nbt.getBoolean("isTaming"));
	        }
	        if (this.isTamed()){
				final byte NBT_TYPE_COMPOUND = 10;
				NBTTagList dataForAllSlots = nbt.getTagList("Items", NBT_TYPE_COMPOUND);
		        this.invTamedDino.loadInventoryFromNBT(dataForAllSlots);
	        } else {
	        	this.invTaming.loadInventoryFromNBT(nbt);
	        }
        }
    }

	public void setTamed(EntityPlayer player, boolean tamed) {
		if (player != null && tamed) {
			player.addChatMessage(new ChatComponentText("EntityTameableDinosaur: You have tamed the dino!"));
            this.setAttackTarget((EntityLivingBase)null);
//            this.aiSit.setSitting(true);
            this.setHealth(25.0F);
            this.setOwnerId(player.getUniqueID().toString());
            this.playTameEffect(true);
            this.worldObj.setEntityState(this, (byte)7);
    		if (attackPlayerTarget != null)
    			this.targetTasks.removeTask(attackPlayerTarget);
		}
		super.setTamed(tamed);
	}
    
	/* Plays the hearts / smoke depending on status. If progress is 100, we always are successful.*/
	protected void playTameEffect(boolean p_70908_1_) {
		super.playTameEffect(p_70908_1_);
    }

	public boolean isTameable() {
		return this.isTameable;
	}

	public boolean isTaming() {
		return this.isTaming;
	}

	public void setIsTaming(boolean isTaming) {
		this.isTaming = isTaming;
	}
	
	/** Used only when tranquilizing a Dino */
	public void increaseTorpor(int i) {
		if (this.isTameable()) {
			int currTorpor = this.invTaming.getTorporTime();
			if (this.invTaming.setTorporTime((short) (currTorpor + i))) {
				setIsTaming(true);
				LogHelper.info("EntityTameableDinosaur: Just set taming to true!");
			}
			LogHelper.info("EntityTameableDinosaur: Set torpor to " + this.invTaming.getTorporTime());
		}
	}
	
	public int getTorpor() {
		if (!this.isTamed() && this.isTameable())
			return this.invTaming.getTorporTime();
		else
			return 0;
	}

	/**
     * Called when the entity is attacked. (Needed to attack other mobs)
     */
    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float damage) {
        if (this.isEntityInvulnerable(damageSource)) {
            return false;
        }
        else {
            Entity entity = damageSource.getEntity();
            if (entity != null) {
                // Reduce damage if from player
            	if ((entity instanceof EntityPlayer) && this.isTamed()) {
            		damage = Math.min(damage, 1.0F);
            	}
            }
            return super.attackEntityFrom(damageSource, damage);
        }
    }

    /**
     * Called when attacking an entity
     */
    @Override
    public boolean attackEntityAsMob(Entity entity) {
        super.attackEntityAsMob(entity);
        double attackDamage = this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getBaseValue();
        LogHelper.info("DinoTamable: Attacking with " + attackDamage + " attackDamage.");
        return entity.attackEntityFrom(DamageSource.causeMobDamage(this), (float)attackDamage);
    }
	
	/**
     * Determines if an entity can despawn, used on idle far away entities
     */
	@Override
    protected boolean canDespawn() {
        return false;
    }
	
    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    @Override
    public boolean interact(EntityPlayer player) {
        if (!this.worldObj.isRemote)
        	LogHelper.info("The player right clicked a Dino.");
        ItemStack itemstack = player.inventory.getCurrentItem();

		if (isTamed()) {
            if (this.isOwner(player)) {
            	if (!this.worldObj.isRemote){
            		player.addChatMessage(new ChatComponentText("This dino is tamed."));
            	}
            	if (player.isSneaking()) {
    	    		if (isTamed()) {
    	    			player.openGui(ARKCraft.instance, GUI.TAMED_DINO.getID(), this.worldObj,
		            			(int) Math.floor(this.posX), (int) this.posY, (int) Math.floor(this.posZ));
		                this.aiSit.setSitting(this.isSitting());
		            	LogHelper.info("Dino is sitting");
		                this.isJumping = false;
		                this.navigator.clearPathEntity();
    	    			return true;
    	    		}
            	} // not sneaking     	
	    		else if (itemstack != null) {
			        // Heal the Dino with its favorite food
					if (itemstack != null && isFavoriteFood(itemstack)) {
	                    ItemFood itemfood = (ItemFood)itemstack.getItem();
	                    if (!player.capabilities.isCreativeMode) {
	                         --itemstack.stackSize;
	                    }
	                    this.heal((float)itemfood.getHealAmount(itemstack));
	                    if (itemstack.stackSize <= 0) {
	                        player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
	                    }
	                	if (!this.worldObj.isRemote)
	                    	player.addChatMessage(new ChatComponentText("This dino's health is: " + this.getHealth() + " Max is: " 
								+ this.getEntityAttribute(SharedMonsterAttributes.maxHealth).getBaseValue()));					
	                    return true;
		            }
					else {
	            		this.setSitting(!this.isSitting());
					}
				}
            	else {
            		if (isSaddled()){
		            	LogHelper.info("Dino is saddled.");
		            	this.setSitting(false);
		            	if (!this.worldObj.isRemote) 
		            		player.mountEntity(this);
		            	return true;
            		}
            		else
		            	LogHelper.info("Dino is not saddled.");
//            		this.setSitting(!this.isSitting());
            	}
			} else { // end of owner's dino
            	player.addChatMessage(new ChatComponentText("EntityTameableDinosaur: This dino is tamed, but not yours."));
			}
		} // end of is tamed
        // Tame the dino with its favorite food
        else if (isTameable() && itemstack != null && isFavoriteFood(itemstack)) {
            if (!player.capabilities.isCreativeMode) {
                --itemstack.stackSize;
            }
            if (itemstack.stackSize <= 0) {
                player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
            }
            if (!this.worldObj.isRemote) {
                if (this.rand.nextInt(2) == 0) {
                    this.setTamed(player, true);
                }
                else {
        			player.addChatMessage(new ChatComponentText("EntityTameableDinosaur: Taming the dino failed, try again!"));
                    this.playTameEffect(false);
                    this.worldObj.setEntityState(this, (byte)6);
                }
            }
            return true;
        }
		// Tame the dino with the GUI
        else if (isTaming()) {
            if (!this.worldObj.isRemote) {
            	player.openGui(ARKCraft.instance, GUI.TAMING_GUI.getID(), this.worldObj,
            			(int) Math.floor(this.posX), (int) this.posY, (int) Math.floor(this.posZ));
            	LogHelper.info("EntityTameableDinosaur: Opening GUI on Dino at: " + this.posX + "," + this.posY + "," + this.posZ + " (" +
            			(int) Math.floor(this.posX) + "," + (int) this.posY  + "," + (int) Math.floor(this.posZ) + ")");
                return true;
            }
//			player.addChatMessage(new ChatComponentText("EntityTameableDinosaur: Use a Raw Porkchop to tame the dino."));
        }
		return false;
//        return super.interact(player);
    }
    
//	private boolean itemIsSaddle(ItemStack itemstack) {
//		if (itemstack.getItem() == ARKCraftItems.saddle_small ||
//			itemstack.getItem() == ARKCraftItems.saddle_medium ||
//			itemstack.getItem() == ARKCraftItems.saddle_large)
//			return true;
//		else
//			return false;
//	}
	
	public boolean isUseableByPlayer(EntityPlayer player) {
		final double X_CENTRE_OFFSET = 0.5;
		final double Y_CENTRE_OFFSET = 0.5;
		final double Z_CENTRE_OFFSET = 0.5;
		final double MAXIMUM_DISTANCE_SQ = 8.0 * 8.0;
		return player.getDistanceSq(this.posX + X_CENTRE_OFFSET, this.posY + Y_CENTRE_OFFSET, this.posZ + Z_CENTRE_OFFSET) < MAXIMUM_DISTANCE_SQ;
	}
	
	public void markDirty() {
        if (isTameable) {
        	invTamedDino.markDirty();;
        	invTaming.markDirty();
        }
	}
	
	public int getTamingSeconds() {
		return this.tamingSeconds;
	}    

    @Override
	public void onLivingUpdate() {
    	if (getTorpor() == 0) {
    		super.onLivingUpdate();
    	}
    	// Paralyze
    	else {
            this.isJumping = false;
            this.moveStrafing = 0.0F;
            this.moveForward = 0.0F;
            this.randomYawVelocity = 0.0F;
            this.invTaming.update();
    	}
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
    
    ////////////////////////////// Stuff so you can ride the dino /////////////////////////////////////////
    /**
     * Returns the Y offset from the entity's position for any entity riding this one.
     * Override this as needed.
     */
    @Override
    public double getMountedYOffset() {
        return (double)this.height * 0.75D;
    }

    /**
     * Dead and sleeping entities cannot move
     * This keeps dino from moving on its own when you ride it (I think)
     */
    protected boolean isMovementBlocked(){
        return (this.riddenByEntity != null && this.isSaddled()) || (this.getHealth() <= 0.0F);
    }
    
    /** Handle any special cases, such as rearing */
    public void updateRiderPosition(){
        super.updateRiderPosition();
    }
    
    /**
     * Moves the entity based on the specified heading.  Args: strafe, forward
     * This is needed for rider to move dino!
     * TODO: Add ability to jump if desired
     */
    public void moveEntityWithHeading(float strafe, float forward) {
        if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer && this.isSaddled()) {
            this.prevRotationYaw = this.rotationYaw = this.riddenByEntity.rotationYaw;
            this.rotationPitch = this.riddenByEntity.rotationPitch * 0.5F;
            this.setRotation(this.rotationYaw, this.rotationPitch);
            this.rotationYawHead = this.renderYawOffset = this.rotationYaw;
            strafe = ((EntityLivingBase)this.riddenByEntity).moveStrafing * 0.5F;
            forward = ((EntityLivingBase)this.riddenByEntity).moveForward;

            if (forward <= 0.0F)
            {
                forward *= 0.25F;
//                this.gallopTime = 0;
            }

//            if (this.onGround && this.jumpPower == 0.0F && this.isRearing() && !this.field_110294_bI)
//            {
//                strafe = 0.0F;
//                forward = 0.0F;
//            }

//            if (this.jumpPower > 0.0F && !this.isHorseJumping() && this.onGround)
//            {
//                this.motionY = this.getHorseJumpStrength() * (double)this.jumpPower;
//
//                if (this.isPotionActive(Potion.jump))
//                {
//                    this.motionY += (double)((float)(this.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
//                }
//
//                this.setHorseJumping(true);
//                this.isAirBorne = true;
//
//                if (forward > 0.0F)
//                {
//                    float f2 = MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F);
//                    float f3 = MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F);
//                    this.motionX += (double)(-0.4F * f2 * this.jumpPower);
//                    this.motionZ += (double)(0.4F * f3 * this.jumpPower);
//                    this.playSound("mob.horse.jump", 0.4F, 1.0F);
//                }
//
//                this.jumpPower = 0.0F;
//                net.minecraftforge.common.ForgeHooks.onLivingJump(this);
//            }

            this.stepHeight = 1.0F;
            this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1F;

            if (!this.worldObj.isRemote)
            {
                this.setAIMoveSpeed((float)this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue());
                super.moveEntityWithHeading(strafe, forward);
            }

//            if (this.onGround)
//            {
//                this.jumpPower = 0.0F;
//                this.setHorseJumping(false);
//            }

            this.prevLimbSwingAmount = this.limbSwingAmount;
            double d1 = this.posX - this.prevPosX;
            double d0 = this.posZ - this.prevPosZ;
            float f4 = MathHelper.sqrt_double(d1 * d1 + d0 * d0) * 4.0F;

            if (f4 > 1.0F)
            {
                f4 = 1.0F;
            }

            this.limbSwingAmount += (f4 - this.limbSwingAmount) * 0.4F;
            this.limbSwing += this.limbSwingAmount;
        }
        else
        {
            this.stepHeight = 0.5F;
            this.jumpMovementFactor = 0.02F;
            super.moveEntityWithHeading(strafe, forward);
        }
    }
}
