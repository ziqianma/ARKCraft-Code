package com.arkcraft.module.creature.common.entity.test;

import com.arkcraft.module.creature.common.entity.EntityARKCreature;
import com.arkcraft.module.creature.common.entity.aggressive.RaptorType;
import com.arkcraft.module.resource.common.item.food.CreatureFoodType;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityRaptor extends EntityARKCreature
{
	private RaptorType type;

	public EntityRaptor(World worldIn)
	{
		super(worldIn, CreatureFoodType.CARNIVORE);
		this.type = RaptorType.values()[RaptorType.getRandomRaptorType()];

		this.tasks.addTask(2, new EntityAIAttackOnCollide(this, 1.0D, false));
		this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D)); // For
																				// going
																				// through
																				// doors
		this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.targetTasks.addTask(1,
				new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
		this.targetTasks.addTask(2, new EntityAILeapAtTarget(this, 1.1f));
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
