package com.arkcraft.mod.core.entity.render;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.SharedMonsterAttributes;
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

public class MobDinoBase extends EntityMob {

	private HashMap<SharedMonsterAttributes, Double> attributes = new HashMap<SharedMonsterAttributes, Double>();
	
	public MobDinoBase(World world) {
		super(world);
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
		this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
        this.applyEntityAI(new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
	}
	
	protected void setEntityAttribute(SharedMonsterAttributes attribute, double baseValue) {
		attributes.put(attribute, baseValue);
	}
	
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		for(Map.Entry<SharedMonsterAttributes, Double> e : attributes.entrySet()) {
			SharedMonsterAttributes attribute = e.getKey();
			double baseValue = e.getValue();
			this.getEntityAttribute((IAttribute)attribute).setBaseValue(baseValue);
		}
	}
	
	protected void applyEntityAI(EntityAIBase... tasks) { if (tasks != null) for (EntityAIBase b : tasks) this.targetTasks.addTask(1, b); }

}
