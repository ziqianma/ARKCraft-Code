package com.arkcraft.mod.core.entity;


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
import net.minecraft.world.World;

import com.arkcraft.mod.core.GlobalAdditions;
import com.arkcraft.mod.core.GlobalAdditions.GUI;
import com.arkcraft.mod.core.Main;
import com.arkcraft.mod.core.lib.LogHelper;
import com.arkcraft.mod.core.machine.gui.InventoryDino;
import com.arkcraft.mod.core.machine.gui.InventoryTaming;

/***
 * 
 * @author wildbill22
 *
 */
public abstract class DinoTameable extends EntityTameable {

	public InventoryDino invDino;
	public InventoryTaming invTaming;
	protected boolean isSaddled = false;
	protected int torpor = 0;
	protected int progress = 0;
	protected boolean isTameable = false;
	protected int tamingSeconds = 0;
	protected boolean isRideable = false;
	protected EntityAIBase attackPlayerTarget;
	SaddleType saddleType;
	
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
		return this.saddleType.getSaddleType();
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

	protected DinoTameable(World worldIn, SaddleType saddleType) {
		super(worldIn);
		this.getDataWatcher().addObject(DINO_SADDLED_WATCHER, Byte.valueOf((byte) 0));
        this.isTameable = true;
		this.tamingSeconds = 120; // Set this before the InventoryTaming
		this.invDino = new InventoryDino("Items", true, saddleType.getInventorySize());
		this.invTaming = new InventoryTaming(this);
		this.saddleType = saddleType;
	}
	
	// Use this constructor if you want to create a dino that is not tameable
	protected DinoTameable(World worldIn, SaddleType saddleType, boolean isTameable, int tamingSeconds) {
		super(worldIn);
		this.getDataWatcher().addObject(DINO_SADDLED_WATCHER, Byte.valueOf((byte) 0));
        this.isTameable = isTameable;
        if (isTameable) {
        	this.tamingSeconds = tamingSeconds; // This must be before the InventoryTaming
        	this.invDino = new InventoryDino("Items", true, saddleType.getInventorySize());
        	this.invTaming = new InventoryTaming(this);
        }
        this.saddleType = saddleType;
        
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
			this.dropItem(GlobalAdditions.porkchop_cooked, 1);
		} else {
			this.dropItem(GlobalAdditions.porkchop_raw, 1);
		}
		if (this.isSaddled()) {
			this.dropItem(GlobalAdditions.saddle_large, 1);
			this.dropItemsInChest(this, this.invDino);
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
		nbt.setBoolean("IsSaddled", isSaddled);
		this.invTaming.saveInventoryToNBT(nbt);
		if(this.isSaddled()) {
			this.invDino.saveInventoryToNBT(nbt);
//			LogHelper.info("EntityDodo - writeEntityToNBT: Saved chest inventory.");
		}
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
	@Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        if (nbt.hasKey("IsSaddled")) {
        	this.setSaddled(nbt.getBoolean("IsSaddled"));
        }
        // Dino taming inventory
        this.invTaming.loadInventoryFromNBT(nbt);
        // Dino Inventory
		final byte NBT_TYPE_COMPOUND = 10;
		NBTTagList dataForAllSlots = nbt.getTagList("Items", NBT_TYPE_COMPOUND);
        this.invDino.loadInventoryFromNBT(dataForAllSlots);
    }

	public void setTamed(EntityPlayer player, boolean tamed) {
		if (player != null && tamed) {
			player.addChatMessage(new ChatComponentText("DinoTameable: You have tamed the dino!"));
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
//		return torpor > 0;
	}

	public void setTorpor(int i) {
		torpor = i;
	}
	public int getTorpor() {
		return torpor;
	}
//
//	public void addTorpor(int i) {
//		torpor += i;
//	}
//
//	public void removeTorpor(int i) {
//		torpor -= i;
//	}
//
//	public void setProgress(int i) {
//		progress = i;
//	}
//
//	public void addProgress(int i) {
//		progress += i;
//	}
//
//	public void removeProgress(int i) {
//		progress -= i;
//	}
	
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
            	if (!this.worldObj.isRemote)
            		player.addChatMessage(new ChatComponentText("DinoTameable: This dino is tamed."));
            	if (player.isSneaking()) {
    	    		if (isSaddled()) {
    	    			player.openGui(Main.instance, GUI.INV_DODO.getID(), this.worldObj, 
		            			(int) Math.floor(this.posX), (int) this.posY, (int) Math.floor(this.posZ));
		                this.aiSit.setSitting(this.isSitting());
		            	LogHelper.info("Dino is sitting");
		                this.isJumping = false;
		                this.navigator.clearPathEntity();
    	    			return true;
    	    		}
            	} // not sneaking     	
	    		else if (itemstack != null) {
					if (itemIsSaddle(itemstack)) {
						if (this.isSaddled) {
			            	LogHelper.info("Dino is saddled.");							
						} else if (itemstack.getItem() == this.getSaddleType()) {
							if (!player.capabilities.isCreativeMode) {
								itemstack.stackSize--;
								if (itemstack.stackSize == 0)
				                    player.inventory.mainInventory[player.inventory.currentItem] = null;
							}
							setSaddled(true);
			            	LogHelper.info("Dino is saddled.");
							return true;
						}
						else {
							player.addChatMessage(new ChatComponentText("This dino can only be saddled with a: " + this.saddleType + " saddle."));
						}
					}
			        // Heal the Dino with meat
					else if (itemstack != null && itemstack.getItem() instanceof ItemFood) {
	                    ItemFood itemfood = (ItemFood)itemstack.getItem();
	                    if (!player.capabilities.isCreativeMode) {
	                         --itemstack.stackSize;
	                    }
	                    this.heal((float)itemfood.getHealAmount(itemstack));
	                    if (itemstack.stackSize <= 0) {
	                        player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
	                    }
						player.addChatMessage(new ChatComponentText("This dino's health is: " + this.getHealth() + " Max is: " 
								+ this.getEntityAttribute(SharedMonsterAttributes.maxHealth).getBaseValue()));					
	                    return true;
		            }
					else {
	            		this.setSitting(!this.isSitting());
					}
				}
            	else {
            		this.setSitting(!this.isSitting());
            	}
			} else { // end of owner's dino
            	player.addChatMessage(new ChatComponentText("DinoTameable: This dino is tamed, but not yours."));				
			}
		} // end of is tamed
        // Tame the dino with meat
        else if (isTameable() && itemstack != null && itemstack.getItem() == GlobalAdditions.porkchop_raw) {
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
        			player.addChatMessage(new ChatComponentText("DinoTameable: Taming the dino failed, try again!"));
                    this.playTameEffect(false);
                    this.worldObj.setEntityState(this, (byte)6);
                }
            }
            return true;
        }
        else if (isTameable()) {
            if (!this.worldObj.isRemote) {
            	player.openGui(Main.instance, GUI.TAMING_GUI.getID(), this.worldObj, 
            			(int) Math.floor(this.posX), (int) this.posY, (int) Math.floor(this.posZ));
            	LogHelper.info("DinoTameable: Opening GUI on Dino at: " + this.posX + "," + this.posY + "," + this.posZ + " (" +
            			(int) Math.floor(this.posX) + "," + (int) this.posY  + "," + (int) Math.floor(this.posZ) + ")");
                return true;
            }
//			player.addChatMessage(new ChatComponentText("DinoTameable: Use a Raw Porkchop to tame the dino."));
        }
		return false;
//        return super.interact(player);
    }
    
	private boolean itemIsSaddle(ItemStack itemstack) {
		if (itemstack.getItem() == GlobalAdditions.saddle_small ||
			itemstack.getItem() == GlobalAdditions.saddle_medium ||
			itemstack.getItem() == GlobalAdditions.saddle_large)
			return true;
		else
			return false;
	}
	
	public boolean isUseableByPlayer(EntityPlayer player) {
		final double X_CENTRE_OFFSET = 0.5;
		final double Y_CENTRE_OFFSET = 0.5;
		final double Z_CENTRE_OFFSET = 0.5;
		final double MAXIMUM_DISTANCE_SQ = 8.0 * 8.0;
		return player.getDistanceSq(this.posX + X_CENTRE_OFFSET, this.posY + Y_CENTRE_OFFSET, this.posZ + Z_CENTRE_OFFSET) < MAXIMUM_DISTANCE_SQ;
	}
	
	public void markDirty() {
		// TODO Auto-generated method stub		
	}
	
	public int getTamingSeconds() {
		return this.tamingSeconds;
	}    

    @Override
	public void onLivingUpdate() {
    	if (torpor == 0) {
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
}
