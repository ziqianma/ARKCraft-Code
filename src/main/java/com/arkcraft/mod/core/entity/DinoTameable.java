package com.arkcraft.mod.core.entity;

import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.world.World;

import com.arkcraft.mod.core.entity.ai.EntityDinoAIFollowOwner;


public abstract class DinoTameable extends EntityTameable {

	protected int torpor = 0;
	protected int progress = 0;
	protected EntityDinoAIFollowOwner dinoAIFollowOwner;
	
	protected DinoTameable(World worldIn) {
		super(worldIn);
		this.setupTamedAI();
	}

	@Override
	public boolean isTamed() {
		return (torpor < 0 && progress == 100);
	}
	
	
	/* Plays the hearts / smoke depending on status. If progress is 100, we always are successful.*/
	@Override
	protected void playTameEffect(boolean p_70908_1_)
    {
       if(progress == 100) super.playTameEffect(true);
       /* We also want a way to play the smoke if torpor reaches 0 from the GUI. If we say that it will play it when torpor is always 0, the effect will play constantly right? */
    }
	
	public boolean isTameable() {
		return torpor > 0;
	}

	public void setTorpor(int i) {
		torpor = i;
	}

	public void addTorpor(int i) {
		torpor += i;
	}

	public void removeTorpor(int i) {
		torpor -= i;
	}

	public void setProgress(int i) {
		progress = i;
	}

	public void addProgress(int i) {
		progress += i;
	}

	public void removeProgress(int i) {
		progress -= i;
	}
	
	@Override
	public void setTamed(boolean tamed) {
		super.setTamed(tamed);
	}	
	
	@Override
	protected void setupTamedAI() {
		/* We can setup the tamed AI here 
		 * Ex: if tamed, the wolf follows the player */
		if(dinoAIFollowOwner == null) dinoAIFollowOwner = new EntityDinoAIFollowOwner(this, 10.0D, 1.0F, 1.2F);
		
	}
	
}
