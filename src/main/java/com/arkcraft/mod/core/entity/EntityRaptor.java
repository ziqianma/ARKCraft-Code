package com.arkcraft.mod.core.entity;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.arkcraft.mod.core.Main;
import com.arkcraft.mod.lib.LogHelper;

/***
 * 
 * @author Vastatio (color done by Bill)
 *
 */
public class EntityRaptor extends MobDinoBase {
	
	private static final String RAPTOR_TYPE_PROP = "ark_raptor_type";
	EntityZombie e;
/*	public RaptorType type = RaptorType.ALBINO; // Default to this, but set it later */
	public RaptorType type;
	
	public EntityRaptor(World world) {
		super(world, "raptor", 100);
        type = RaptorType.ALBINO;
/*        type.setRandomRaptorType(); // Set to a random type for now */
	}
    
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		// Raptor properties
		nbt.setInteger(RAPTOR_TYPE_PROP, this.type.getRaptorId());
		LogHelper.info("EnityRaptor write: Raptor is a " + this.type.toString() + ".");
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		// Raptor properties
		if (nbt.hasKey(RAPTOR_TYPE_PROP)) {
			type.setRaptorTypeId(nbt.getInteger(RAPTOR_TYPE_PROP));
			LogHelper.info("EntityRaptor read: Raptor is a " + this.type.toString() + ".");
		}
		LogHelper.error("EntityRaptor read: No raptor type property!");
	}
	
	@Override
	public void applyEntityAttributes() {
		this.setEntityAttribute(SharedMonsterAttributes.knockbackResistance, 1.0D);
    	this.setEntityAttribute(SharedMonsterAttributes.followRange, 45.0D);
    	this.setEntityAttribute(SharedMonsterAttributes.maxHealth, 8.0D); //tested this at 5.0 (too low) setting to 8.
    	this.setEntityAttribute(SharedMonsterAttributes.movementSpeed, 0.37D);
    	this.setEntityAttribute(SharedMonsterAttributes.attackDamage, 4D); //2.5 hearts without armor
	}
	
    @Override
    protected String getLivingSound() {
    	int idle = this.rand.nextInt(3);
		return Main.MODID + ":" + "Idle_" + idle;
    }
    
    @Override
    protected String getHurtSound() {
    	int hurt = this.rand.nextInt(3);
		return Main.MODID + ":" + "Hurt_" + hurt;
    }
    
    @Override
    protected String getDeathSound() { return Main.MODID + ":" + "Death"; }
}
