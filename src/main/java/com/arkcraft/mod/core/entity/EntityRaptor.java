package com.arkcraft.mod.core.entity;

import java.util.Random;

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
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import com.arkcraft.mod.core.GlobalAdditions;
import com.arkcraft.mod.core.Main;
import com.arkcraft.mod.lib.LogHelper;
import com.google.common.base.Predicate;

/***
 * 
 * @author Vastatio (color done by Bill)
 *
 */
public class EntityRaptor extends DinoTameable {
	private static final String RAPTOR_TYPE_PROP = "ark_raptor_type";
	public enum RaptorType {
	    ALBINO(0),
	    BREEN_WHITE(1),
	    CYAN_LGREEN(2),
	    RAINBOW(3),
	    GREEN_GREY(4),
	    GREEN_TAN(5),
	    GREEN_WHITE(6),
	    GREY_GREY(7),
	    LBROWN_TAN(8),
	    RED_TAN(9),
	    TAN_WHITE(10);
		private int type;
		public static final int numRaptors = 11;
	    
	    RaptorType(int id) {
	        this.type = id;
	    }

		public int getRaptorId() {
			return type;
		}
		
		public void setRandomRaptorType() {
			type = new Random().nextInt(RaptorType.numRaptors);
		}

		public void setRaptorTypeId(int id) {
	        this.type = id;
		}
		
		public String toString() {
			switch (type) {
			case 1:
				return "Breen White";
			case 2:
				return "Cyan Light Green";
			case 3:
				return "Rainbow";
			case 4:
				return "Green Grey";
			case 5:
				return "Green Tan";
			case 6:
				return "Green White";
			case 7:
				return "Grey Grey";
			case 8:
				return "Light Brown Tan";
			case 9:
				return "Red Tan";
			case 10:
				return "Tan White";
			case 0:
			default:
				return "Albino";
			}
		}
	}
//	public RaptorType type = RaptorType.ALBINO; // Default to this, but set it later
	public RaptorType type;
//	protected EntityAIBase attackPlayerTarget;

	@SuppressWarnings("rawtypes")
	public EntityRaptor(World world) {
		super(world);
        this.setSize(0.8F, 1.5F);

        this.clearAITasks();
//        this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4F));

        ((PathNavigateGround)this.getNavigator()).func_179690_a(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
//        this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
        this.tasks.addTask(2, new EntityAIAttackOnCollide(this, 1.0D, false));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D)); // For going through doors
        this.tasks.addTask(5, new EntityAIFollowOwner(this, 1.0D, 10.0F, 2.0F)); // like wolf
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
        
        type = RaptorType.ALBINO;
        type.setRandomRaptorType(); // Set to a random type for now
        
        this.setTamed(false);
	}

    protected void applyEntityAttributes() {
    	super.applyEntityAttributes();
    	this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextDouble() * 0.05000000074505806D, 0));
    	this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(30.0D);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
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
    	this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.379890125D);
    }
    
	/**
	 * Drop 0-2 items of this living's type
	 */
	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
		int numDrops = this.rand.nextInt(2) + 2;
		if (this.isBurning()) {
			this.dropItem(GlobalAdditions.porkchop_cooked, numDrops);
		} else {
			this.dropItem(GlobalAdditions.porkchop_raw, numDrops);
		}
		if (this.isSaddled()) {
			this.dropItem(GlobalAdditions.saddle_small, 1);
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
		nbt.setInteger(RAPTOR_TYPE_PROP, this.type.getRaptorId());
//		LogHelper.info("EnityRaptor write: Raptor is a " + this.type.toString() + " at: " + this.posX + ", " + this.posY + ", " + this.posZ);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		// Raptor properties
		if (nbt.hasKey(RAPTOR_TYPE_PROP)) {
			type.setRaptorTypeId(nbt.getInteger(RAPTOR_TYPE_PROP));
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
    	// TODO: Add angry sound if not tamed
    	int idle = this.rand.nextInt(3) + 1;
		return Main.MODID + ":" + "Raptor_Idle_" + idle;
    }
    
    @Override
    protected String getHurtSound() {
    	int hurt = this.rand.nextInt(3) + 1;
		return Main.MODID + ":" + "Raptor_Hurt_" + hurt;
    }
    
    @Override
    protected String getDeathSound() {
		return Main.MODID + ":" + "Raptor_Death";
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
	public Item getSaddleType() {
		return GlobalAdditions.saddle_small;
	}
}
