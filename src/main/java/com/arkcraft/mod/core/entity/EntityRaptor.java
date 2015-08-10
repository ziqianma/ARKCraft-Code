package com.arkcraft.mod.core.entity;

import java.util.Random;

import com.arkcraft.mod.core.Main;
import com.arkcraft.mod.lib.LogHelper;

import net.minecraft.block.Block;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

/***
 * 
 * @author Vastatio (color done by Bill)
 *
 */
public class EntityRaptor extends EntityMob {
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

	public EntityRaptor(World world) {
		super(world);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.applyEntityAI(new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
        type = RaptorType.ALBINO;
//        type.setRandomRaptorType(); // Set to a random type for now
	}
	
    protected void applyEntityAI(EntityAIBase... tasks) {
    	if(tasks != null) for(EntityAIBase b : tasks) this.targetTasks.addTask(1, b);
    }
    
    protected void applyEntityAttributes() {
    	super.applyEntityAttributes();
    	this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextDouble() * 0.05000000074505806D, 0));
    	this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(45.0D);
    	this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0D); //tested this at 5.0 (too low) setting to 8.
    	this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.379890125D);
    	this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4D); //2.5 hearts without armor
    }
    
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		// Raptor properties
		nbt.setInteger(RAPTOR_TYPE_PROP, this.type.getRaptorId());
		LogHelper.info("EnityRaptor write: Raptor is a " + this.type.toString() + " at: " + this.posX + ", " + this.posY + ", " + this.posZ);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		// Raptor properties
		if (nbt.hasKey(RAPTOR_TYPE_PROP)) {
			type.setRaptorTypeId(nbt.getInteger(RAPTOR_TYPE_PROP));
			LogHelper.info("EnityRaptor read: Raptor is a " + this.type.toString() + " at: " + this.posX + ", " + this.posY + ", " + this.posZ);
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
    	int idle = this.rand.nextInt(3) + 1;
		return Main.MODID + ":" + "Idle_" + idle;
    }
    
    @Override
    protected String getHurtSound() {
    	int hurt = this.rand.nextInt(3) + 1;
		return Main.MODID + ":" + "Hurt_" + hurt;
    }
    
    @Override
    protected String getDeathSound() {
		return Main.MODID + ":" + "Death";
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    @Override
    protected float getSoundVolume() {
        return 1.0F;
    }
    
    /**
     * Called when the entity is attacked. (Needed to attack other mobs)
     */
    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float damage) {
    	int angry = this.rand.nextInt(3) + 1;
        this.playSound(Main.MODID + ":" + "Angry_" + angry, 0.15F, 1.0F);    	
		return super.attackEntityFrom(damageSource, damage);	
    }
    
	/**
     * Determines if an entity can despawn, used on idle far away entities
     */
	@Override
    protected boolean canDespawn() {
        return false;
    }
	
	public String toString() {
		return "Raptor[" + this.getPosition().getX() + ", " + this.getPosition().getY() + ", " + this.getPosition().getZ() + "]";
	}
}
