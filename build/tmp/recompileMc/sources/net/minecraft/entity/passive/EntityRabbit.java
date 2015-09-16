package net.minecraft.entity.passive;

import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCarrot;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityJumpHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityRabbit extends EntityAnimal
{
    private EntityRabbit.AIAvoidEntity field_175539_bk;
    private int field_175540_bm = 0;
    private int field_175535_bn = 0;
    private boolean field_175536_bo = false;
    private boolean field_175537_bp = false;
    private int field_175538_bq = 0;
    private EntityRabbit.EnumMoveType moveType;
    private int carrotTicks;
    private EntityPlayer field_175543_bt;
    private static final String __OBFID = "CL_00002242";

    public EntityRabbit(World worldIn)
    {
        super(worldIn);
        this.moveType = EntityRabbit.EnumMoveType.HOP;
        this.carrotTicks = 0;
        this.field_175543_bt = null;
        this.setSize(0.6F, 0.7F);
        this.jumpHelper = new EntityRabbit.RabbitJumpHelper(this);
        this.moveHelper = new EntityRabbit.RabbitMoveHelper();
        ((PathNavigateGround)this.getNavigator()).func_179690_a(true);
        this.navigator.func_179678_a(2.5F);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityRabbit.AIPanic(1.33D));
        this.tasks.addTask(2, new EntityAITempt(this, 1.0D, Items.carrot, false));
        this.tasks.addTask(3, new EntityAIMate(this, 0.8D));
        this.tasks.addTask(5, new EntityRabbit.AIRaidFarm());
        this.tasks.addTask(5, new EntityAIWander(this, 0.6D));
        this.tasks.addTask(11, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0F));
        this.field_175539_bk = new EntityRabbit.AIAvoidEntity(new Predicate()
        {
            private static final String __OBFID = "CL_00002241";
            public boolean func_180086_a(Entity p_180086_1_)
            {
                return p_180086_1_ instanceof EntityWolf;
            }
            public boolean apply(Object p_apply_1_)
            {
                return this.func_180086_a((Entity)p_apply_1_);
            }
        }, 16.0F, 1.33D, 1.33D);
        this.tasks.addTask(4, this.field_175539_bk);
        this.func_175515_b(0.0D);
    }

    protected float func_175134_bD()
    {
        return this.moveHelper.isUpdating() && this.moveHelper.func_179919_e() > this.posY + 0.5D ? 0.5F : this.moveType.func_180074_b();
    }

    public void func_175522_a(EntityRabbit.EnumMoveType p_175522_1_)
    {
        this.moveType = p_175522_1_;
    }

    @SideOnly(Side.CLIENT)
    public float func_175521_o(float p_175521_1_)
    {
        return this.field_175535_bn == 0 ? 0.0F : ((float)this.field_175540_bm + p_175521_1_) / (float)this.field_175535_bn;
    }

    public void func_175515_b(double p_175515_1_)
    {
        this.getNavigator().setSpeed(p_175515_1_);
        this.moveHelper.setMoveTo(this.moveHelper.func_179917_d(), this.moveHelper.func_179919_e(), this.moveHelper.func_179918_f(), p_175515_1_);
    }

    public void func_175519_a(boolean p_175519_1_, EntityRabbit.EnumMoveType p_175519_2_)
    {
        super.setJumping(p_175519_1_);

        if (!p_175519_1_)
        {
            if (this.moveType == EntityRabbit.EnumMoveType.ATTACK)
            {
                this.moveType = EntityRabbit.EnumMoveType.HOP;
            }
        }
        else
        {
            this.func_175515_b(1.5D * (double)p_175519_2_.func_180072_a());
            this.playSound(this.func_175516_ck(), this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 0.8F);
        }

        this.field_175536_bo = p_175519_1_;
    }

    public void func_175524_b(EntityRabbit.EnumMoveType p_175524_1_)
    {
        this.func_175519_a(true, p_175524_1_);
        this.field_175535_bn = p_175524_1_.func_180073_d();
        this.field_175540_bm = 0;
    }

    public boolean func_175523_cj()
    {
        return this.field_175536_bo;
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(18, Byte.valueOf((byte)0));
    }

    public void updateAITasks()
    {
        if (this.moveHelper.getSpeed() > 0.8D)
        {
            this.func_175522_a(EntityRabbit.EnumMoveType.SPRINT);
        }
        else if (this.moveType != EntityRabbit.EnumMoveType.ATTACK)
        {
            this.func_175522_a(EntityRabbit.EnumMoveType.HOP);
        }

        if (this.field_175538_bq > 0)
        {
            --this.field_175538_bq;
        }

        if (this.carrotTicks > 0)
        {
            this.carrotTicks -= this.rand.nextInt(3);

            if (this.carrotTicks < 0)
            {
                this.carrotTicks = 0;
            }
        }

        if (this.onGround)
        {
            if (!this.field_175537_bp)
            {
                this.func_175519_a(false, EntityRabbit.EnumMoveType.NONE);
                this.func_175517_cu();
            }

            if (this.getRabbitType() == 99 && this.field_175538_bq == 0)
            {
                EntityLivingBase entitylivingbase = this.getAttackTarget();

                if (entitylivingbase != null && this.getDistanceSqToEntity(entitylivingbase) < 16.0D)
                {
                    this.func_175533_a(entitylivingbase.posX, entitylivingbase.posZ);
                    this.moveHelper.setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, this.moveHelper.getSpeed());
                    this.func_175524_b(EntityRabbit.EnumMoveType.ATTACK);
                    this.field_175537_bp = true;
                }
            }

            EntityRabbit.RabbitJumpHelper rabbitjumphelper = (EntityRabbit.RabbitJumpHelper)this.jumpHelper;

            if (!rabbitjumphelper.func_180067_c())
            {
                if (this.moveHelper.isUpdating() && this.field_175538_bq == 0)
                {
                    PathEntity pathentity = this.navigator.getPath();
                    Vec3 vec3 = new Vec3(this.moveHelper.func_179917_d(), this.moveHelper.func_179919_e(), this.moveHelper.func_179918_f());

                    if (pathentity != null && pathentity.getCurrentPathIndex() < pathentity.getCurrentPathLength())
                    {
                        vec3 = pathentity.getPosition(this);
                    }

                    this.func_175533_a(vec3.xCoord, vec3.zCoord);
                    this.func_175524_b(this.moveType);
                }
            }
            else if (!rabbitjumphelper.func_180065_d())
            {
                this.func_175518_cr();
            }
        }

        this.field_175537_bp = this.onGround;
    }

    /**
     * Attempts to create sprinting particles if the entity is sprinting and not in water.
     */
    public void spawnRunningParticles() {}

    private void func_175533_a(double p_175533_1_, double p_175533_3_)
    {
        this.rotationYaw = (float)(Math.atan2(p_175533_3_ - this.posZ, p_175533_1_ - this.posX) * 180.0D / Math.PI) - 90.0F;
    }

    private void func_175518_cr()
    {
        ((EntityRabbit.RabbitJumpHelper)this.jumpHelper).func_180066_a(true);
    }

    private void func_175520_cs()
    {
        ((EntityRabbit.RabbitJumpHelper)this.jumpHelper).func_180066_a(false);
    }

    private void func_175530_ct()
    {
        this.field_175538_bq = this.func_175532_cm();
    }

    private void func_175517_cu()
    {
        this.func_175530_ct();
        this.func_175520_cs();
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if (this.field_175540_bm != this.field_175535_bn)
        {
            if (this.field_175540_bm == 0 && !this.worldObj.isRemote)
            {
                this.worldObj.setEntityState(this, (byte)1);
            }

            ++this.field_175540_bm;
        }
        else if (this.field_175535_bn != 0)
        {
            this.field_175540_bm = 0;
            this.field_175535_bn = 0;
        }
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("RabbitType", this.getRabbitType());
        tagCompound.setInteger("MoreCarrotTicks", this.carrotTicks);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        super.readEntityFromNBT(tagCompund);
        this.setRabbitType(tagCompund.getInteger("RabbitType"));
        this.carrotTicks = tagCompund.getInteger("MoreCarrotTicks");
    }

    protected String func_175516_ck()
    {
        return "mob.rabbit.hop";
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "mob.rabbit.idle";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.rabbit.hurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "mob.rabbit.death";
    }

    public boolean attackEntityAsMob(Entity p_70652_1_)
    {
        if (this.getRabbitType() == 99)
        {
            this.playSound("mob.attack", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            return p_70652_1_.attackEntityFrom(DamageSource.causeMobDamage(this), 8.0F);
        }
        else
        {
            return p_70652_1_.attackEntityFrom(DamageSource.causeMobDamage(this), 3.0F);
        }
    }

    /**
     * Returns the current armor value as determined by a call to InventoryPlayer.getTotalArmorValue
     */
    public int getTotalArmorValue()
    {
        return this.getRabbitType() == 99 ? 8 : super.getTotalArmorValue();
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        return this.isEntityInvulnerable(source) ? false : super.attackEntityFrom(source, amount);
    }

    /**
     * Makes entity wear random armor based on difficulty
     */
    protected void addRandomArmor()
    {
        this.entityDropItem(new ItemStack(Items.rabbit_foot, 1), 0.0F);
    }

    /**
     * Drop 0-2 items of this living's type
     */
    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
    {
        int j = this.rand.nextInt(2) + this.rand.nextInt(1 + p_70628_2_);
        int k;

        for (k = 0; k < j; ++k)
        {
            this.dropItem(Items.rabbit_hide, 1);
        }

        j = this.rand.nextInt(2);

        for (k = 0; k < j; ++k)
        {
            if (this.isBurning())
            {
                this.dropItem(Items.cooked_rabbit, 1);
            }
            else
            {
                this.dropItem(Items.rabbit, 1);
            }
        }
    }

    private boolean func_175525_a(Item p_175525_1_)
    {
        return p_175525_1_ == Items.carrot || p_175525_1_ == Items.golden_carrot || p_175525_1_ == Item.getItemFromBlock(Blocks.yellow_flower);
    }

    public EntityRabbit func_175526_b(EntityAgeable p_175526_1_)
    {
        EntityRabbit entityrabbit = new EntityRabbit(this.worldObj);

        if (p_175526_1_ instanceof EntityRabbit)
        {
            entityrabbit.setRabbitType(this.rand.nextBoolean() ? this.getRabbitType() : ((EntityRabbit)p_175526_1_).getRabbitType());
        }

        return entityrabbit;
    }

    /**
     * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on
     * the animal type)
     */
    public boolean isBreedingItem(ItemStack stack)
    {
        return stack != null && this.func_175525_a(stack.getItem());
    }

    public int getRabbitType()
    {
        return this.dataWatcher.getWatchableObjectByte(18);
    }

    public void setRabbitType(int rabbitTypeId)
    {
        if (rabbitTypeId == 99)
        {
            this.tasks.removeTask(this.field_175539_bk);
            this.tasks.addTask(4, new EntityRabbit.AIEvilAttack());
            this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
            this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
            this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityWolf.class, true));

            if (!this.hasCustomName())
            {
                this.setCustomNameTag(StatCollector.translateToLocal("entity.KillerBunny.name"));
            }
        }

        this.dataWatcher.updateObject(18, Byte.valueOf((byte)rabbitTypeId));
    }

    public IEntityLivingData func_180482_a(DifficultyInstance p_180482_1_, IEntityLivingData p_180482_2_)
    {
        Object p_180482_2_1 = super.func_180482_a(p_180482_1_, p_180482_2_);
        int i = this.rand.nextInt(6);
        boolean flag = false;

        if (p_180482_2_1 instanceof EntityRabbit.RabbitTypeData)
        {
            i = ((EntityRabbit.RabbitTypeData)p_180482_2_1).field_179427_a;
            flag = true;
        }
        else
        {
            p_180482_2_1 = new EntityRabbit.RabbitTypeData(i);
        }

        this.setRabbitType(i);

        if (flag)
        {
            this.setGrowingAge(-24000);
        }

        return (IEntityLivingData)p_180482_2_1;
    }

    private boolean func_175534_cv()
    {
        return this.carrotTicks == 0;
    }

    protected int func_175532_cm()
    {
        return this.moveType.func_180075_c();
    }

    protected void func_175528_cn()
    {
        this.worldObj.spawnParticle(EnumParticleTypes.BLOCK_DUST, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + 0.5D + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, 0.0D, 0.0D, 0.0D, new int[] {Block.getStateId(Blocks.carrots.getStateFromMeta(7))});
        this.carrotTicks = 100;
    }

    @SideOnly(Side.CLIENT)
    public void handleHealthUpdate(byte p_70103_1_)
    {
        if (p_70103_1_ == 1)
        {
            this.createRunningParticles();
            this.field_175535_bn = 10;
            this.field_175540_bm = 0;
        }
        else
        {
            super.handleHealthUpdate(p_70103_1_);
        }
    }

    public EntityAgeable createChild(EntityAgeable ageable)
    {
        return this.func_175526_b(ageable);
    }

    class AIAvoidEntity extends EntityAIAvoidEntity
    {
        private EntityRabbit entityInstance = EntityRabbit.this;
        private static final String __OBFID = "CL_00002238";

        public AIAvoidEntity(Predicate avoidPredicate, float p_i45865_3_, double p_i45865_4_, double p_i45865_6_)
        {
            super(EntityRabbit.this, avoidPredicate, p_i45865_3_, p_i45865_4_, p_i45865_6_);
        }

        /**
         * Updates the task
         */
        public void updateTask()
        {
            super.updateTask();
        }
    }

    class AIEvilAttack extends EntityAIAttackOnCollide
    {
        private static final String __OBFID = "CL_00002240";

        public AIEvilAttack()
        {
            super(EntityRabbit.this, EntityLivingBase.class, 1.4D, true);
        }

        protected double func_179512_a(EntityLivingBase p_179512_1_)
        {
            return (double)(4.0F + p_179512_1_.width);
        }
    }

    class AIPanic extends EntityAIPanic
    {
        private EntityRabbit field_179486_b = EntityRabbit.this;
        private static final String __OBFID = "CL_00002234";

        public AIPanic(double p_i45861_2_)
        {
            super(EntityRabbit.this, p_i45861_2_);
        }

        /**
         * Updates the task
         */
        public void updateTask()
        {
            super.updateTask();
            this.field_179486_b.func_175515_b(this.speed);
        }
    }

    class AIRaidFarm extends EntityAIMoveToBlock
    {
        private boolean field_179498_d;
        private boolean field_179499_e = false;
        private static final String __OBFID = "CL_00002233";

        public AIRaidFarm()
        {
            super(EntityRabbit.this, 0.699999988079071D, 16);
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute()
        {
            if (this.field_179496_a <= 0)
            {
                if (!EntityRabbit.this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing"))
                {
                    return false;
                }

                this.field_179499_e = false;
                this.field_179498_d = EntityRabbit.this.func_175534_cv();
            }

            return super.shouldExecute();
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean continueExecuting()
        {
            return this.field_179499_e && super.continueExecuting();
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting()
        {
            super.startExecuting();
        }

        /**
         * Resets the task
         */
        public void resetTask()
        {
            super.resetTask();
        }

        /**
         * Updates the task
         */
        public void updateTask()
        {
            super.updateTask();
            EntityRabbit.this.getLookHelper().setLookPosition((double)this.destinationBlock.getX() + 0.5D, (double)(this.destinationBlock.getY() + 1), (double)this.destinationBlock.getZ() + 0.5D, 10.0F, (float)EntityRabbit.this.getVerticalFaceSpeed());

            if (this.func_179487_f())
            {
                World world = EntityRabbit.this.worldObj;
                BlockPos blockpos = this.destinationBlock.up();
                IBlockState iblockstate = world.getBlockState(blockpos);
                Block block = iblockstate.getBlock();

                if (this.field_179499_e && block instanceof BlockCarrot && ((Integer)iblockstate.getValue(BlockCarrot.AGE)).intValue() == 7)
                {
                    world.setBlockState(blockpos, Blocks.air.getDefaultState(), 2);
                    world.destroyBlock(blockpos, true);
                    EntityRabbit.this.func_175528_cn();
                }

                this.field_179499_e = false;
                this.field_179496_a = 10;
            }
        }

        protected boolean func_179488_a(World worldIn, BlockPos p_179488_2_)
        {
            Block block = worldIn.getBlockState(p_179488_2_).getBlock();

            if (block == Blocks.farmland)
            {
                p_179488_2_ = p_179488_2_.up();
                IBlockState iblockstate = worldIn.getBlockState(p_179488_2_);
                block = iblockstate.getBlock();

                if (block instanceof BlockCarrot && ((Integer)iblockstate.getValue(BlockCarrot.AGE)).intValue() == 7 && this.field_179498_d && !this.field_179499_e)
                {
                    this.field_179499_e = true;
                    return true;
                }
            }

            return false;
        }
    }

    static enum EnumMoveType
    {
        NONE(0.0F, 0.0F, 30, 1),
        HOP(0.8F, 0.2F, 20, 10),
        STEP(1.0F, 0.45F, 14, 14),
        SPRINT(1.75F, 0.4F, 1, 8),
        ATTACK(2.0F, 0.7F, 7, 8);
        private final float field_180076_f;
        private final float field_180077_g;
        private final int field_180084_h;
        private final int field_180085_i;

        private static final String __OBFID = "CL_00002239";

        private EnumMoveType(float p_i45866_3_, float p_i45866_4_, int p_i45866_5_, int p_i45866_6_)
        {
            this.field_180076_f = p_i45866_3_;
            this.field_180077_g = p_i45866_4_;
            this.field_180084_h = p_i45866_5_;
            this.field_180085_i = p_i45866_6_;
        }

        public float func_180072_a()
        {
            return this.field_180076_f;
        }

        public float func_180074_b()
        {
            return this.field_180077_g;
        }

        public int func_180075_c()
        {
            return this.field_180084_h;
        }

        public int func_180073_d()
        {
            return this.field_180085_i;
        }
    }

    public class RabbitJumpHelper extends EntityJumpHelper
    {
        private EntityRabbit field_180070_c;
        private boolean field_180068_d = false;
        private static final String __OBFID = "CL_00002236";

        public RabbitJumpHelper(EntityRabbit p_i45863_2_)
        {
            super(p_i45863_2_);
            this.field_180070_c = p_i45863_2_;
        }

        public boolean func_180067_c()
        {
            return this.isJumping;
        }

        public boolean func_180065_d()
        {
            return this.field_180068_d;
        }

        public void func_180066_a(boolean p_180066_1_)
        {
            this.field_180068_d = p_180066_1_;
        }

        /**
         * Called to actually make the entity jump if isJumping is true.
         */
        public void doJump()
        {
            if (this.isJumping)
            {
                this.field_180070_c.func_175524_b(EntityRabbit.EnumMoveType.STEP);
                this.isJumping = false;
            }
        }
    }

    class RabbitMoveHelper extends EntityMoveHelper
    {
        private EntityRabbit field_179929_g = EntityRabbit.this;
        private static final String __OBFID = "CL_00002235";

        public RabbitMoveHelper()
        {
            super(EntityRabbit.this);
        }

        public void onUpdateMoveHelper()
        {
            if (this.field_179929_g.onGround && !this.field_179929_g.func_175523_cj())
            {
                this.field_179929_g.func_175515_b(0.0D);
            }

            super.onUpdateMoveHelper();
        }
    }

    public static class RabbitTypeData implements IEntityLivingData
        {
            public int field_179427_a;
            private static final String __OBFID = "CL_00002237";

            public RabbitTypeData(int p_i45864_1_)
            {
                this.field_179427_a = p_i45864_1_;
            }
        }
}