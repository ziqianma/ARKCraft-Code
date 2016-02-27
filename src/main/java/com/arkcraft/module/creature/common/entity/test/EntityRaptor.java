package com.arkcraft.module.creature.common.entity.test;

import com.arkcraft.module.creature.common.entity.EntityARKCreature;
import com.arkcraft.module.creature.common.entity.aggressive.RaptorType;
import com.arkcraft.module.resource.common.item.food.CreatureFoodType;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.world.World;

public class EntityRaptor extends EntityARKCreature
{
	private RaptorType type;

	public EntityRaptor(World worldIn)
	{
		super(worldIn, CreatureFoodType.CARNIVORE);
		this.type = RaptorType.values()[RaptorType.getRandomRaptorType()];
		((PathNavigateGround) this.getNavigator()).func_179690_a(true);
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIAttackOnCollide(this, 1.0D, false));
		this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D)); // For
																				// going
																				// through
																				// doors
		this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.targetTasks.addTask(1,
				new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
	}

	public RaptorType getType()
	{
		return type;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);

		nbt.setInteger("colorType", type.getRaptorTypeInt());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);

		type = RaptorType.values()[nbt.getInteger("colorType")];
	}

	@Override
	public void writeSpawnData(ByteBuf buffer)
	{
		super.writeSpawnData(buffer);
		buffer.writeInt(type.getRaptorTypeInt());
	}

	@Override
	public void readSpawnData(ByteBuf buffer)
	{
		super.readSpawnData(buffer);
		type = RaptorType.values()[buffer.readInt()];
	}
}
