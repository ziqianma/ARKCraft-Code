package com.arkcraft.mod.core.entity.passive;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.world.World;

import com.arkcraft.mod.core.GlobalAdditions;

public class EntityBrontosaurus extends EntityTameable {

	public EntityBrontosaurus(World w) {
		super(w); 
		this.setSize(4.5F, 4.5F);
		((PathNavigateGround)this.getNavigator()).func_179690_a(true);
		this.tasks.taskEntries.clear();
        int p = 0;
		this.tasks.addTask(++p, new EntityAISwimming(this));
		this.tasks.addTask(++p, new EntityAIWander(this, 1.0D));
    	this.tasks.addTask(++p, new EntityAILookIdle(this));
		this.tasks.addTask(++p, new EntityAIMate(this, 1.0D));
		this.tasks.addTask(++p, new EntityAITempt(this, 1.0D,	GlobalAdditions.narcoBerry, false));
		this.tasks.addTask(++p, new EntityAIFollowParent(this, 1.1D));
		this.tasks.addTask(++p, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(++p, new EntityAIFollowOwner(this, 1.0D, 8.0F, 5.0F));
		this.tasks.addTask(++p, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
	}
	
	@Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(30.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.7D);
    }

	@Override
	public EntityAgeable createChild(EntityAgeable ageable) {
		return new EntityBrontosaurus(this.worldObj);
	}
	
	@Override
    public void setTamed(boolean tamed) {
        super.setTamed(tamed);
        if (tamed) {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
            this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.23000000417232513D); //zombie move speed
        }
    }
	
	@Override
    protected boolean canDespawn() {
        return !this.isTamed() && this.ticksExisted > 2400;
    }
}
