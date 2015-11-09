package com.arkcraft.mod.common.entity.aggressive;

import com.arkcraft.mod.common.ARKCraft;
import com.arkcraft.mod.common.entity.EntityTameableDinosaur;
import com.arkcraft.mod.common.entity.SaddleType;
import com.arkcraft.mod.common.items.ARKCraftItems;
import com.arkcraft.mod.common.items.ItemARKFood;
import com.arkcraft.mod.common.lib.BALANCE;
import com.arkcraft.mod.common.lib.LogHelper;
import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITargetNonTamed;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

/***
 * 
 * @author Vastatio (color done by Bill)
 *
 */
public class EntityRaptor extends EntityTameableDinosaur
{
	private static final String RAPTOR_TYPE_PROP = "ark_raptor_type";
	public int raptorType;

	public EntityRaptor(World world) {
		this(world, SaddleType.SMALL.getSaddleID());
	}
	
	@SuppressWarnings("rawtypes")
	public EntityRaptor(World world, int raptorType) {
		super(world, SaddleType.SMALL, true, BALANCE.DINO_PROPERTIES.SECONDS_TO_TAME_RAPTOR);
        this.setSize(0.8F, 1.5F);

        ((PathNavigateGround)this.getNavigator()).func_179690_a(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
//        this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
        this.tasks.addTask(2, new EntityAIAttackOnCollide(this, 1.0D, false));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D)); // For going through doors
        this.tasks.addTask(5, new EntityAIFollowOwner(this, 1.5D, 10.0F, 2.0F)); // like wolf, but faster (wolf is 1.0D speed)
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));

        int p = 1;
        attackPlayerTarget = new EntityAINearestAttackableTarget(this, EntityPlayer.class, true);
        this.targetTasks.addTask(p++, attackPlayerTarget);
        this.targetTasks.addTask(p++, new EntityAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(p++, new EntityAIOwnerHurtTarget(this));
//        this.targetTasks.addTask(p++, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.targetTasks.addTask(p++, new EntityAITargetNonTamed(this, EntityAnimal.class, false, new Predicate() {
            public boolean func_180094_a(Entity p_180094_1_) {
                return p_180094_1_ instanceof EntitySheep || p_180094_1_ instanceof EntityRabbit;
            }
            public boolean apply(Object p_apply_1_) {
                return this.func_180094_a((Entity)p_apply_1_);
            }
        }));
        
        this.raptorType = raptorType;
        this.setTamed(false);
	}

    protected void applyEntityAttributes() {
    	super.applyEntityAttributes();
    	this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextDouble() * 0.05000000074505806D, 0));
    	this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(30.0D);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
    	this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.379890125D);
        // This code below must be duplicated in setTamed() for the change to take place immediately after taming 
    	if (this.isTamed()) {
    		// Double when tamed
    		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(16.0D); // Double the health for now
        	this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(8.0D); //5 hearts without armor
    	}
    	else {
    		// weaker when not tamed
    		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0D); //tested this at 5.0 (too low) setting to 8.
        	this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0D); //2.5 hearts without armor
    	}
    }
    
	@Override
	public void setTamed(boolean tamed) {
		super.setTamed(tamed);
        if (tamed) {
    		// Double when tamed
    		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(16.0D); // Double the health for now
        	this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(8.0D); //5 hearts without armor
        }
        else {
    		// weaker when not tamed
    		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0D); //tested this at 5.0 (too low) setting to 8.
        	this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0D); //2.5 hearts without armor
        }
	}
        
	/**
	 * Drop 0-2 items of this living's type
	 */
	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
		int numDrops = this.rand.nextInt(2) + 2;
		if (this.isBurning()) {
			this.dropItem(ARKCraftItems.meat_cooked, numDrops);
		} else {
			this.dropItem(ARKCraftItems.meat_raw, numDrops);
		}
		if (this.isSaddled()) {
			this.dropItem(ARKCraftItems.saddle_small, 1);
		}
		// Drop 1 - 3 leather
		numDrops = this.rand.nextInt(3) + 1;
		this.dropItem(Items.leather, numDrops);
		
		// TODO: Replace with Dino bones, Drop 1 - 3 bones
		numDrops = this.rand.nextInt(3) + 1;
		this.dropItem(Items.bone, numDrops);
	}
    
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		// Raptor properties
		nbt.setInteger(RAPTOR_TYPE_PROP, raptorType);
//		LogHelper.info("EnityRaptor write: Raptor is a " + this.type.toString() + " at: " + this.posX + ", " + this.posY + ", " + this.posZ);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		// Raptor properties
		if (nbt.hasKey(RAPTOR_TYPE_PROP)) {
			raptorType = nbt.getInteger(RAPTOR_TYPE_PROP);
//			LogHelper.info("EnityRaptor read: Raptor is a " + this.type.toString() + " at: " + this.posX + ", " + this.posY + ", " + this.posZ);
		}
		else
			LogHelper.error("EnityRaptor read: No raptor type property!");
	}
	
	@Override
    protected void playStepSound(BlockPos p_180429_1_, Block p_180429_2_)  {
        this.playSound("mob.cow.step", 0.15F, 1.0F);
    }

    @Override
    protected String getLivingSound() {
    	if (this.isAngry()) {
	    	int idle = this.rand.nextInt(3) + 1;
			return ARKCraft.MODID + ":" + "Raptor_Idle_" + idle;
    	} else {
        	int angry = this.rand.nextInt(3) + 1;
    		return ARKCraft.MODID + ":" + "Raptor_Angry_" + angry;
    	}
    }
    
    @Override
    protected String getHurtSound() {
    	int hurt = this.rand.nextInt(3) + 1;
		return ARKCraft.MODID + ":" + "Raptor_Hurt_" + hurt;
    }
    
    @Override
    protected String getDeathSound() {
		return ARKCraft.MODID + ":" + "Raptor_Death";
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    @Override
    protected float getSoundVolume() {
        return 1.0F;
    }
    
	public String toString() {
		return "EntityRaptor[" + this.getPosition().getX() + ", " + this.getPosition().getY() + ", " + this.getPosition().getZ() + "]";
	}

	@Override
	public EntityAgeable createChild(EntityAgeable ageable) {
		return new EntityRaptor(this.worldObj);
	}

	@Override
	public boolean isFavoriteFood(ItemStack itemstack) {
		if (itemstack.getItem() instanceof ItemARKFood &&
				(itemstack.getItem() == ARKCraftItems.meat_raw || itemstack.getItem() == ARKCraftItems.meat_cooked
				|| itemstack.getItem() == ARKCraftItems.primemeat_raw || itemstack.getItem() == ARKCraftItems.primemeat_cooked)){
			return true;
		}
		return false;
	}
}
