package com.arkcraft.mod.core.entity;

import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/***
 * 
 * @author Vastatio
 *
 */

public class MobDinoBase extends EntityMob/* implements ITamable */ {
	
	//private static final String TORPOR_TAG = "ark_dino_torpor";
	//int torpor; //we put this in the interface once its done or something
	
	public MobDinoBase(World world, String name, int torporMax) {
		super(world);
		//torpor = torporMax;
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
		this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
        this.applyEntityAI(new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
	}
	
	/* Did this just so that i don't have to get the attribute and invoke another method on the IAttribute */
	protected void setEntityAttribute(IAttribute attribute, double baseValue) { this.getEntityAttribute(attribute).setBaseValue(baseValue); }
	
	/* INVOCATION TARGET EXCEPTION TODO fix
	 * 
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		nbt.setInteger(TORPOR_TAG, torpor);
		LogHelper.info("This entity has a max torpor of: " + torpor);
	}
	
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		if(nbt.hasKey(TORPOR_TAG)) torpor = nbt.getInteger(TORPOR_TAG);
	}
	
	*/
	
	protected void applyEntityAI(EntityAIBase... tasks) {
		if (tasks != null) for (EntityAIBase b : tasks) this.targetTasks.addTask(1, b); 
	}


	
}
