package com.arkcraft.mod.core.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

/**
 * 
 * @author Vastatio
 *
 */
public class EntityExplosive extends EntityThrowable {

	public int explosionRadius = 2;
	
	public EntityExplosive(World w) {
		super(w);
	}
	
	public EntityExplosive(World w, EntityLivingBase par2) {
		super(w, par2);

	}
	
	public EntityExplosive(World w, double par2, double par3, double par4) {
		super(w, par2, par3, par4);

	}
	
	@Override
	protected void onImpact(MovingObjectPosition par1) {
		/* Explosion done here */
		 this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, (float)this.explosionRadius, true);
		 this.setDead();
	}
}
