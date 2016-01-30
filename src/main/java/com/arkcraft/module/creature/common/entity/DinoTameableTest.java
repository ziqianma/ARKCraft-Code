package com.arkcraft.module.creature.common.entity;

import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.arkcraft.module.blocks.common.items.ARKCraftItems;
import com.arkcraft.module.core.common.entity.ai.EntityDinoAIFollowOwner;

/**
 * @author wildbill22
 */
public class DinoTameableTest extends EntityMob implements IEntityOwnable
{

	protected int torpor = 0;
	protected int progress = 0;
	protected boolean isTameable = false;
	protected boolean isRideable = false;
	protected EntityDinoAIFollowOwner dinoAIFollowOwner;
	protected EntityAIBase attackPlayerTarget;

	protected DinoTameableTest(World worldIn)
	{
		super(worldIn);
		this.isTameable = true;
	}

	protected void setupTamedAI()
	{
		/*
		 * We can setup the tamed AI here Ex: if tamed, the wolf follows the
		 * player
		 */
		if (dinoAIFollowOwner == null)
		{
			dinoAIFollowOwner = new EntityDinoAIFollowOwner(this, 1.5D, 10.0F, 2.0F); // Set
																						// same
																						// as
																						// wolf
																						// for
																						// follow
																						// distance
			this.tasks.addTask(3, dinoAIFollowOwner);
		}
	}

	protected void entityInit()
	{
		super.entityInit();
		this.dataWatcher.addObject(16, Byte.valueOf((byte) 0));
		this.dataWatcher.addObject(17, "");
	}

	/**
	 * Sets the active target the Task system uses for tracking
	 */
	public void setAttackTarget(EntityLivingBase target)
	{
		super.setAttackTarget(target);
		if (target == null)
		{
			this.setAngry(false);
		}
		else if (!this.isTamed())
		{
			this.setAngry(true);
		}
	}

	/**
	 * Determines whether this wolf is angry or not.
	 */
	public boolean isAngry()
	{
		return (this.dataWatcher.getWatchableObjectByte(16) & 2) != 0;
	}

	/**
	 * Sets whether this wolf is angry or not.
	 */
	public void setAngry(boolean angry)
	{
		byte b0 = this.dataWatcher.getWatchableObjectByte(16);

		if (angry)
		{
			this.dataWatcher.updateObject(16, Byte.valueOf((byte) (b0 | 2)));
		}
		else
		{
			this.dataWatcher.updateObject(16, Byte.valueOf((byte) (b0 & -3)));
		}
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound tagCompound)
	{
		super.writeEntityToNBT(tagCompound);
		if (this.getOwnerId() == null)
		{
			tagCompound.setString("OwnerUUID", "");
		}
		else
		{
			tagCompound.setString("OwnerUUID", this.getOwnerId());
		}
		// tagCompound.setBoolean("Sitting", this.isSitting());
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound tagCompund)
	{
		super.readEntityFromNBT(tagCompund);
		String s = "";
		if (tagCompund.hasKey("OwnerUUID", 8))
		{
			s = tagCompund.getString("OwnerUUID");
		}
		else
		{
			String s1 = tagCompund.getString("Owner");
			s = PreYggdrasilConverter.func_152719_a(s1);
		}
		if (s.length() > 0)
		{
			this.setOwnerId(s);
			this.setTamed(true);
		}
		// this.aiSit.setSitting(tagCompund.getBoolean("Sitting"));
		// this.setSitting(tagCompund.getBoolean("Sitting"));
	}

	/*
	 * Plays the hearts / smoke depending on status. If progress is 100, we
	 * always are successful.
	 */
	protected void playTameEffect(boolean p_70908_1_)
	{
		/*
		 * We also want a way to play the smoke if torpor reaches 0 from the
		 * GUI. If we say that it will play it when torpor is always 0, the
		 * effect will play constantly right?
		 */
		if (progress == 100)
		{
			EnumParticleTypes enumparticletypes = EnumParticleTypes.HEART;

			if (!p_70908_1_)
			{
				enumparticletypes = EnumParticleTypes.SMOKE_NORMAL;
			}

			for (int i = 0; i < 7; ++i)
			{
				double d0 = this.rand.nextGaussian() * 0.02D;
				double d1 = this.rand.nextGaussian() * 0.02D;
				double d2 = this.rand.nextGaussian() * 0.02D;
				this.worldObj
						.spawnParticle(
								enumparticletypes,
								this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width,
								this.posY + 0.5D + (double) (this.rand.nextFloat() * this.height),
								this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width,
								d0, d1, d2, new int[0]);
			}
		}
	}

	// TODO: Not sure if we need this!
	@SideOnly(Side.CLIENT)
	public void handleHealthUpdate(byte p_70103_1_)
	{
		if (p_70103_1_ == 7)
		{
			this.playTameEffect(true);
		}
		else if (p_70103_1_ == 6)
		{
			this.playTameEffect(false);
		}
		else
		{
			super.handleHealthUpdate(p_70103_1_);
		}
	}

	public boolean isTameable()
	{
		return torpor > 0;
	}

	public void setTorpor(int i)
	{
		torpor = i;
	}

	public void addTorpor(int i)
	{
		torpor += i;
	}

	public void removeTorpor(int i)
	{
		torpor -= i;
	}

	public void setProgress(int i)
	{
		progress = i;
	}

	public void addProgress(int i)
	{
		progress += i;
	}

	public void removeProgress(int i)
	{
		progress -= i;
	}

	/**
	 * Called when the entity is attacked. (Needed to attack other mobs)
	 */
	@Override
	public boolean attackEntityFrom(DamageSource damageSource, float damage)
	{
		if (this.isEntityInvulnerable(damageSource))
		{
			return false;
		}
		else
		{
			Entity entity = damageSource.getEntity();
			if (entity != null)
			{
				// Reduce damage if from player
				if ((entity instanceof EntityPlayer) && this.isTamed())
				{
					damage = Math.min(damage, 1.0F);
				}
			}
			return super.attackEntityFrom(damageSource, damage);
		}
	}

	/**
	 * Called when attacking an entity
	 */
	@Override
	public boolean attackEntityAsMob(Entity entity)
	{
		return super.attackEntityAsMob(entity);
	}

	public boolean isTamed()
	{
		if (this.isTameable)
		{
			return (this.dataWatcher.getWatchableObjectByte(16) & 4) != 0;
		}
		else
		{
			return false;
		}
		// return (torpor < 0 && progress == 100);
	}

	public void setTamed(boolean tamed)
	{
		byte b0 = this.dataWatcher.getWatchableObjectByte(16);
		if (tamed)
		{
			this.dataWatcher.updateObject(16, Byte.valueOf((byte) (b0 | 4)));
		}
		else
		{
			this.dataWatcher.updateObject(16, Byte.valueOf((byte) (b0 & -5)));
		}
		this.setupTamedAI();
	}

	public String getOwnerId()
	{
		return this.dataWatcher.getWatchableObjectString(17);
	}

	public void setOwnerId(String ownerUuid)
	{
		this.dataWatcher.updateObject(17, ownerUuid);
	}

	public EntityLivingBase getOwnerEntity()
	{
		try
		{
			UUID uuid = UUID.fromString(this.getOwnerId());
			return uuid == null ? null : this.worldObj.getPlayerEntityByUUID(uuid);
		}
		catch (IllegalArgumentException illegalargumentexception)
		{
			return null;
		}
	}

	public boolean isOwner(EntityLivingBase entityIn)
	{
		return entityIn == this.getOwnerEntity();
	}

	// This is in EntityTameable, not sure its purpose, but adding so that
	// EntityDinoAIOwnerHurtByTarget can use it
	public boolean func_142018_a(EntityLivingBase p_142018_1_, EntityLivingBase p_142018_2_)
	{
		return true;
	}

	public Team getTeam()
	{
		if (this.isTamed())
		{
			EntityLivingBase entitylivingbase = this.getOwnerEntity();
			if (entitylivingbase != null) { return entitylivingbase.getTeam(); }
		}
		return super.getTeam();
	}

	public boolean isOnSameTeam(EntityLivingBase otherEntity)
	{
		if (this.isTamed())
		{
			EntityLivingBase entitylivingbase1 = this.getOwnerEntity();
			if (otherEntity == entitylivingbase1) { return true; }
			if (entitylivingbase1 != null) { return entitylivingbase1.isOnSameTeam(otherEntity); }
		}
		return super.isOnSameTeam(otherEntity);
	}

	/**
	 * Called when the mob's health reaches 0.
	 */
	public void onDeath(DamageSource cause)
	{
		if (!this.worldObj.isRemote && this.worldObj.getGameRules().getGameRuleBooleanValue(
				"showDeathMessages") && this.hasCustomName() && this.getOwnerEntity() instanceof EntityPlayerMP)
		{
			((EntityPlayerMP) this.getOwnerEntity()).addChatMessage(this.getCombatTracker()
					.getDeathMessage());
		}
		super.onDeath(cause);
	}

	public Entity getOwner()
	{
		return this.getOwnerEntity();
	}

	/**
	 * Determines if an entity can despawn, used on idle far away entities
	 */
	@Override
	protected boolean canDespawn()
	{
		return false;
	}

	/**
	 * Called when a player interacts with a mob. e.g. gets milk from a cow,
	 * gets into the saddle on a pig.
	 */
	@Override
	public boolean interact(EntityPlayer player)
	{
		ItemStack itemstack = player.inventory.getCurrentItem();

		if (this.isTamed())
		{
			if (itemstack != null)
			{
				player.addChatMessage(new ChatComponentText(
						"EntityTameableDinosaur: This dino is tamed."));
				if (itemstack.getItem() instanceof ItemFood)
				{
					ItemFood itemfood = (ItemFood) itemstack.getItem();
					if (!player.capabilities.isCreativeMode)
					{
						--itemstack.stackSize;
					}
					this.heal((float) itemfood.getHealAmount(itemstack));
					if (itemstack.stackSize <= 0)
					{
						player.inventory.setInventorySlotContents(player.inventory.currentItem,
								(ItemStack) null);
					}
					return true;
				}
			}
		}
		// Tame the dino with meat
		else if (itemstack != null && itemstack.getItem() == ARKCraftItems.meat_raw)
		{
			if (!player.capabilities.isCreativeMode)
			{
				--itemstack.stackSize;
			}
			if (itemstack.stackSize <= 0)
			{
				player.inventory.setInventorySlotContents(player.inventory.currentItem,
						(ItemStack) null);
			}

			if (!this.worldObj.isRemote)
			{
				if (this.rand.nextInt(2) == 0)
				{
					this.setTamed(true);
					player.addChatMessage(new ChatComponentText(
							"EntityTameableDinosaur: You have tamed the dino!"));
					this.setAttackTarget((EntityLivingBase) null);
					// this.aiSit.setSitting(true);
					this.setHealth(25.0F);
					this.setOwnerId(player.getUniqueID().toString());
					this.playTameEffect(true);
					this.worldObj.setEntityState(this, (byte) 7);
				}
				else
				{
					player.addChatMessage(new ChatComponentText(
							"EntityTameableDinosaur: Taming the dino failed, try again!"));
					this.playTameEffect(false);
					this.worldObj.setEntityState(this, (byte) 6);
				}
			}
			return true;
		}
		else
		{
			player.addChatMessage(new ChatComponentText(
					"EntityTameableDinosaur: Use a Raw Porkchop to tame the dino."));
		}
		return super.interact(player);
	}
}
