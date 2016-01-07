package com.arkcraft.module.core.common.entity;

import com.arkcraft.lib.LogHelper;
import com.arkcraft.module.core.ARKCraft;
import com.arkcraft.module.core.common.entity.ai.EntityDinoAIOwnerHurtByTarget;
import com.arkcraft.module.core.common.entity.ai.EntityDinoAIOwnerHurtTarget;
import com.arkcraft.module.core.common.entity.ai.EntityDinoAITargetNonTamed;
import net.minecraft.block.Block;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

/**
 * @author Vastatio (color done by Bill)
 */
public class EntityRaptorTest extends DinoTameableTest
{
    private static final String RAPTOR_TYPE_PROP = "ark_raptor_type";

    public enum RaptorType
    {
        ALBINO(0),
        BREEN_WHITE(1),
        CYAN_LGREEN(2),
        RAINBOW(3),
        GREEN_GREY(4),
        GREEN_TAN(5),
        GREEN_WHITE(6),
        GREY_GREY(7),
        LBROWN_TAN(8),
        RED_TAN(9),
        TAN_WHITE(10);
        private int type;
        public static final int numRaptors = 11;

        RaptorType(int id)
        {
            this.type = id;
        }

        public int getRaptorId()
        {
            return type;
        }

        public void setRandomRaptorType()
        {
            type = new Random().nextInt(RaptorType.numRaptors);
        }

        public void setRaptorTypeId(int id)
        {
            this.type = id;
        }

        public String toString()
        {
            switch (type)
            {
                case 1:
                    return "Breen White";
                case 2:
                    return "Cyan Light Green";
                case 3:
                    return "Rainbow";
                case 4:
                    return "Green Grey";
                case 5:
                    return "Green Tan";
                case 6:
                    return "Green White";
                case 7:
                    return "Grey Grey";
                case 8:
                    return "Light Brown Tan";
                case 9:
                    return "Red Tan";
                case 10:
                    return "Tan White";
                case 0:
                default:
                    return "Albino";
            }
        }
    }

    //	public RaptorType type = RaptorType.ALBINO; // Default to this, but set it later
    public RaptorType type;
//	protected EntityAIBase attackPlayerTarget;

    public EntityRaptorTest(World world)
    {
        super(world);

//        this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4F));

        ((PathNavigateGround) this.getNavigator()).func_179690_a(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        attackPlayerTarget = new EntityAINearestAttackableTarget(this, EntityPlayer.class, true);
        this.targetTasks.addTask(1, attackPlayerTarget);

        int p = 1;
        this.targetTasks.addTask(p++, new EntityDinoAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(p++, new EntityDinoAIOwnerHurtTarget(this));
        this.targetTasks.addTask(p++, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(p++, new EntityDinoAITargetNonTamed(this, EntitySheep.class, false));

        type = RaptorType.ALBINO;
//        type.setRandomRaptorType(); // Set to a random type for now
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextDouble() * 0.05000000074505806D, 0));
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(45.0D);
        if (this.isTamed())
        {
            // Double when tamed
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(64.0D); // Double the health for now
            this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(8D); //5 hearts without armor
        }
        else
        {
            // weaker when not tamed
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(32.0D); //tested this at 5.0 (too low) setting to 8.
            this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4D); //2.5 hearts without armor
        }
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.379890125D);
    }

    @Override
    public void setTamed(boolean tamed)
    {
        this.targetTasks.removeTask(attackPlayerTarget);
        super.setTamed(tamed);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);
        // Raptor properties
        nbt.setInteger(RAPTOR_TYPE_PROP, this.type.getRaptorId());
//		LogHelper.info("EnityRaptor write: Raptor is a " + this.type.toString() + " at: " + this.posX + ", " + this.posY + ", " + this.posZ);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);
        // Raptor properties
        if (nbt.hasKey(RAPTOR_TYPE_PROP))
        {
            type.setRaptorTypeId(nbt.getInteger(RAPTOR_TYPE_PROP));
//			LogHelper.info("EnityRaptor read: Raptor is a " + this.type.toString() + " at: " + this.posX + ", " + this.posY + ", " + this.posZ);
        }
        else
        {
            LogHelper.error("EnityRaptor read: No raptor type property!");
        }
    }

    @Override
    protected void playStepSound(BlockPos p_180429_1_, Block p_180429_2_)
    {
        this.playSound("mob.cow.step", 0.15F, 1.0F);
    }

    @Override
    protected String getLivingSound()
    {
        int idle = this.rand.nextInt(3) + 1;
        return ARKCraft.MODID + ":" + "Raptor_Idle_" + idle;
    }

    @Override
    protected String getHurtSound()
    {
        int hurt = this.rand.nextInt(3) + 1;
        return ARKCraft.MODID + ":" + "Raptor_Hurt_" + hurt;
    }

    @Override
    protected String getDeathSound()
    {
        return ARKCraft.MODID + ":" + "Raptor_Death";
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    @Override
    protected float getSoundVolume()
    {
        return 1.0F;
    }

    public String toString()
    {
        return "EntityRaptor[" + this.getPosition().getX() + ", " + this.getPosition().getY() + ", " + this.getPosition().getZ() + "]";
    }
}
