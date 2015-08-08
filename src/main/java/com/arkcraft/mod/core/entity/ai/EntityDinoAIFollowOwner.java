package com.arkcraft.mod.core.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.arkcraft.mod.core.entity.DinoTameable;

/***
 * 
 * @author Vastatio
 *
 */
public class EntityDinoAIFollowOwner extends EntityAIBase {

	private DinoTameable pet;
	private EntityLivingBase owner;
	/* Capsulated */
	protected World world;
	private double followSpeed;
	private PathNavigate pathFinder;
	protected float minDist, maxDist;

	public EntityDinoAIFollowOwner(DinoTameable pet, double followSpeed,
			float minDist, float maxDist) {
		this.pet = pet;
		this.world = pet.worldObj;
		this.followSpeed = followSpeed;
		this.minDist = minDist;
		this.maxDist = maxDist;
		this.setMutexBits(3);

		if (!(pet.getNavigator() instanceof PathNavigateGround)) {
			throw new IllegalArgumentException(
					"Unsupported Mob Type for FollowOwnerGoal");
		}
	}

	/* Returns whether the EntityAIBase should begin execution */
	public boolean shouldExecute() {
		EntityLivingBase owner = this.pet.getOwnerEntity();
		if (owner == null)
			return false;
		else if (this.pet.getDistanceSqToEntity(owner) < (double) (this.minDist * this.minDist))
			return false;
		this.owner = owner;
		return true;
	}

	/* Should the in-progress EntityAIBase continue executing? */
	public boolean continueExecuting() {
		return !this.pathFinder.noPath()
				&& this.pet.getDistanceSqToEntity(this.owner) > (double) (this.maxDist * this.maxDist);
	}

	public void startExecuting() {
		/*
		 * Not sure if in default, the animal avoids water. 1.7 was
		 * setAvoidsWater, not sure if func_179690_a is that.
		 */
		((PathNavigateGround) pet.getNavigator()).func_179690_a(false);
	}

	public void resetTask() {
		this.owner = null;
		this.pathFinder.clearPathEntity();
		((PathNavigateGround) pet.getNavigator()).func_179690_a(true);
	}

	public void updateTask() {
		this.pet.getLookHelper().setLookPositionWithEntity(owner, 10.0F,
				(float) this.pet.getVerticalFaceSpeed());
		if (!this.pathFinder
				.tryMoveToEntityLiving(this.owner, this.followSpeed)) {
			if (!this.pet.getLeashed()) {
				if (this.pet.getDistanceSqToEntity(this.owner) >= 144.0D) {
					int i = MathHelper.floor_double(this.owner.posX) - 2;
					int j = MathHelper.floor_double(this.owner.posZ) - 2;
					int k = MathHelper.floor_double(this.owner
							.getEntityBoundingBox().minY);

					for (int l = 0; l <= 4; ++l) {
						for (int i1 = 0; i1 <= 4; ++i1) {
							if ((l < 1 || i1 < 1 || l > 3 || i1 > 3)
									&& World.doesBlockHaveSolidTopSurface(
											this.world, new BlockPos(i + l,
													k - 1, j + i1))
									&& !this.world
											.getBlockState(
													new BlockPos(i + l, k, j
															+ i1)).getBlock()
											.isFullCube()
									&& !this.world
											.getBlockState(
													new BlockPos(i + l, k + 1,
															j + i1)).getBlock()
											.isFullCube()) {
								this.pet.setLocationAndAngles(
										(double) ((float) (i + l) + 0.5F),
										(double) k,
										(double) ((float) (j + i1) + 0.5F),
										this.pet.rotationYaw,
										this.pet.rotationPitch);
								this.pathFinder.clearPathEntity();
								return;
							}
						}
					}
				}
			}

		}
	}
}
