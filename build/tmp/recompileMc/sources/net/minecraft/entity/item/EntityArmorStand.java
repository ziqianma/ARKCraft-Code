package net.minecraft.entity.item;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Rotations;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityArmorStand extends EntityLivingBase
{
    private static final Rotations DEFAULT_HEAD_ROTATION = new Rotations(0.0F, 0.0F, 0.0F);
    private static final Rotations DEFAULT_BODY_ROTATION = new Rotations(0.0F, 0.0F, 0.0F);
    private static final Rotations DEFAULT_LEFTARM_ROTATION = new Rotations(-10.0F, 0.0F, -10.0F);
    private static final Rotations DEFAULT_RIGHTARM_ROTATION = new Rotations(-15.0F, 0.0F, 10.0F);
    private static final Rotations DEFAULT_LEFTLEG_ROTATION = new Rotations(-1.0F, 0.0F, -1.0F);
    private static final Rotations DEFAULT_RIGHTLEG_ROTATION = new Rotations(1.0F, 0.0F, 1.0F);
    private final ItemStack[] contents;
    private boolean canInteract;
    /** After punching the stand, the cooldown before you can punch it again without breaking it. */
    private long punchCooldown;
    private int disabledSlots;
    private Rotations headRotation;
    private Rotations bodyRotation;
    private Rotations leftArmRotation;
    private Rotations rightArmRotation;
    private Rotations leftLegRotation;
    private Rotations rightLegRotation;
    private static final String __OBFID = "CL_00002228";

    public EntityArmorStand(World worldIn)
    {
        super(worldIn);
        this.contents = new ItemStack[5];
        this.headRotation = DEFAULT_HEAD_ROTATION;
        this.bodyRotation = DEFAULT_BODY_ROTATION;
        this.leftArmRotation = DEFAULT_LEFTARM_ROTATION;
        this.rightArmRotation = DEFAULT_RIGHTARM_ROTATION;
        this.leftLegRotation = DEFAULT_LEFTLEG_ROTATION;
        this.rightLegRotation = DEFAULT_RIGHTLEG_ROTATION;
        this.setSilent(true);
        this.noClip = this.hasNoGravity();
        this.setSize(0.5F, 1.975F);
    }

    public EntityArmorStand(World worldIn, double posX, double posY, double posZ)
    {
        this(worldIn);
        this.setPosition(posX, posY, posZ);
    }

    /**
     * Returns whether the entity is in a server world
     */
    public boolean isServerWorld()
    {
        return super.isServerWorld() && !this.hasNoGravity();
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(10, Byte.valueOf((byte)0));
        this.dataWatcher.addObject(11, DEFAULT_HEAD_ROTATION);
        this.dataWatcher.addObject(12, DEFAULT_BODY_ROTATION);
        this.dataWatcher.addObject(13, DEFAULT_LEFTARM_ROTATION);
        this.dataWatcher.addObject(14, DEFAULT_RIGHTARM_ROTATION);
        this.dataWatcher.addObject(15, DEFAULT_LEFTLEG_ROTATION);
        this.dataWatcher.addObject(16, DEFAULT_RIGHTLEG_ROTATION);
    }

    /**
     * Returns the item that this EntityLiving is holding, if any.
     */
    public ItemStack getHeldItem()
    {
        return this.contents[0];
    }

    /**
     * 0: Tool in Hand; 1-4: Armor
     */
    public ItemStack getEquipmentInSlot(int slotIn)
    {
        return this.contents[slotIn];
    }

    @SideOnly(Side.CLIENT)
    public ItemStack getCurrentArmor(int slotIn)
    {
        return this.contents[slotIn + 1];
    }

    /**
     * Sets the held item, or an armor slot. Slot 0 is held item. Slot 1-4 is armor. Params: Item, slot
     */
    public void setCurrentItemOrArmor(int slotIn, ItemStack stack)
    {
        this.contents[slotIn] = stack;
    }

    /**
     * returns the inventory of this entity (only used in EntityPlayerMP it seems)
     */
    public ItemStack[] getInventory()
    {
        return this.contents;
    }

    public boolean replaceItemInInventory(int p_174820_1_, ItemStack p_174820_2_)
    {
        int j;

        if (p_174820_1_ == 99)
        {
            j = 0;
        }
        else
        {
            j = p_174820_1_ - 100 + 1;

            if (j < 0 || j >= this.contents.length)
            {
                return false;
            }
        }

        if (p_174820_2_ != null && EntityLiving.getArmorPosition(p_174820_2_) != j && (j != 4 || !(p_174820_2_.getItem() instanceof ItemBlock)))
        {
            return false;
        }
        else
        {
            this.setCurrentItemOrArmor(j, p_174820_2_);
            return true;
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.contents.length; ++i)
        {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();

            if (this.contents[i] != null)
            {
                this.contents[i].writeToNBT(nbttagcompound1);
            }

            nbttaglist.appendTag(nbttagcompound1);
        }

        tagCompound.setTag("Equipment", nbttaglist);

        if (this.getAlwaysRenderNameTag() && (this.getCustomNameTag() == null || this.getCustomNameTag().length() == 0))
        {
            tagCompound.setBoolean("CustomNameVisible", this.getAlwaysRenderNameTag());
        }

        tagCompound.setBoolean("Invisible", this.isInvisible());
        tagCompound.setBoolean("Small", this.isSmall());
        tagCompound.setBoolean("ShowArms", this.getShowArms());
        tagCompound.setInteger("DisabledSlots", this.disabledSlots);
        tagCompound.setBoolean("NoGravity", this.hasNoGravity());
        tagCompound.setBoolean("NoBasePlate", this.hasNoBasePlate());
        tagCompound.setTag("Pose", this.readPoseFromNBT());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        super.readEntityFromNBT(tagCompund);

        if (tagCompund.hasKey("Equipment", 9))
        {
            NBTTagList nbttaglist = tagCompund.getTagList("Equipment", 10);

            for (int i = 0; i < this.contents.length; ++i)
            {
                this.contents[i] = ItemStack.loadItemStackFromNBT(nbttaglist.getCompoundTagAt(i));
            }
        }

        this.setInvisible(tagCompund.getBoolean("Invisible"));
        this.setSmall(tagCompund.getBoolean("Small"));
        this.setShowArms(tagCompund.getBoolean("ShowArms"));
        this.disabledSlots = tagCompund.getInteger("DisabledSlots");
        this.setNoGravity(tagCompund.getBoolean("NoGravity"));
        this.setNoBasePlate(tagCompund.getBoolean("NoBasePlate"));
        this.noClip = this.hasNoGravity();
        NBTTagCompound nbttagcompound1 = tagCompund.getCompoundTag("Pose");
        this.writePoseToNBT(nbttagcompound1);
    }

    /**
     * Saves the pose to an NBTTagCompound.
     *  
     * @param tagCompound The tag to save the Pose information to.
     */
    private void writePoseToNBT(NBTTagCompound tagCompound)
    {
        NBTTagList nbttaglist = tagCompound.getTagList("Head", 5);

        if (nbttaglist.tagCount() > 0)
        {
            this.setHeadRotation(new Rotations(nbttaglist));
        }
        else
        {
            this.setHeadRotation(DEFAULT_HEAD_ROTATION);
        }

        NBTTagList nbttaglist1 = tagCompound.getTagList("Body", 5);

        if (nbttaglist1.tagCount() > 0)
        {
            this.setBodyRotation(new Rotations(nbttaglist1));
        }
        else
        {
            this.setBodyRotation(DEFAULT_BODY_ROTATION);
        }

        NBTTagList nbttaglist2 = tagCompound.getTagList("LeftArm", 5);

        if (nbttaglist2.tagCount() > 0)
        {
            this.setLeftArmRotation(new Rotations(nbttaglist2));
        }
        else
        {
            this.setLeftArmRotation(DEFAULT_LEFTARM_ROTATION);
        }

        NBTTagList nbttaglist3 = tagCompound.getTagList("RightArm", 5);

        if (nbttaglist3.tagCount() > 0)
        {
            this.setRightArmRotation(new Rotations(nbttaglist3));
        }
        else
        {
            this.setRightArmRotation(DEFAULT_RIGHTARM_ROTATION);
        }

        NBTTagList nbttaglist4 = tagCompound.getTagList("LeftLeg", 5);

        if (nbttaglist4.tagCount() > 0)
        {
            this.setLeftLegRotation(new Rotations(nbttaglist4));
        }
        else
        {
            this.setLeftLegRotation(DEFAULT_LEFTLEG_ROTATION);
        }

        NBTTagList nbttaglist5 = tagCompound.getTagList("RightLeg", 5);

        if (nbttaglist5.tagCount() > 0)
        {
            this.setRightLegRotation(new Rotations(nbttaglist5));
        }
        else
        {
            this.setRightLegRotation(DEFAULT_RIGHTLEG_ROTATION);
        }
    }

    private NBTTagCompound readPoseFromNBT()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        if (!DEFAULT_HEAD_ROTATION.equals(this.headRotation))
        {
            nbttagcompound.setTag("Head", this.headRotation.writeToNBT());
        }

        if (!DEFAULT_BODY_ROTATION.equals(this.bodyRotation))
        {
            nbttagcompound.setTag("Body", this.bodyRotation.writeToNBT());
        }

        if (!DEFAULT_LEFTARM_ROTATION.equals(this.leftArmRotation))
        {
            nbttagcompound.setTag("LeftArm", this.leftArmRotation.writeToNBT());
        }

        if (!DEFAULT_RIGHTARM_ROTATION.equals(this.rightArmRotation))
        {
            nbttagcompound.setTag("RightArm", this.rightArmRotation.writeToNBT());
        }

        if (!DEFAULT_LEFTLEG_ROTATION.equals(this.leftLegRotation))
        {
            nbttagcompound.setTag("LeftLeg", this.leftLegRotation.writeToNBT());
        }

        if (!DEFAULT_RIGHTLEG_ROTATION.equals(this.rightLegRotation))
        {
            nbttagcompound.setTag("RightLeg", this.rightLegRotation.writeToNBT());
        }

        return nbttagcompound;
    }

    /**
     * Returns true if this entity should push and be pushed by other entities when colliding.
     */
    public boolean canBePushed()
    {
        return false;
    }

    protected void collideWithEntity(Entity p_82167_1_) {}

    protected void collideWithNearbyEntities()
    {
        List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox());

        if (list != null && !list.isEmpty())
        {
            for (int i = 0; i < list.size(); ++i)
            {
                Entity entity = (Entity)list.get(i);

                if (entity instanceof EntityMinecart && ((EntityMinecart)entity).getMinecartType() == EntityMinecart.EnumMinecartType.RIDEABLE && this.getDistanceSqToEntity(entity) <= 0.2D)
                {
                    entity.applyEntityCollision(this);
                }
            }
        }
    }

    public boolean func_174825_a(EntityPlayer p_174825_1_, Vec3 p_174825_2_)
    {
        if (!this.worldObj.isRemote && !p_174825_1_.isSpectator())
        {
            byte b0 = 0;
            ItemStack itemstack = p_174825_1_.getCurrentEquippedItem();
            boolean flag = itemstack != null;

            if (flag && itemstack.getItem() instanceof ItemArmor)
            {
                ItemArmor itemarmor = (ItemArmor)itemstack.getItem();

                if (itemarmor.armorType == 3)
                {
                    b0 = 1;
                }
                else if (itemarmor.armorType == 2)
                {
                    b0 = 2;
                }
                else if (itemarmor.armorType == 1)
                {
                    b0 = 3;
                }
                else if (itemarmor.armorType == 0)
                {
                    b0 = 4;
                }
            }

            if (flag && (itemstack.getItem() == Items.skull || itemstack.getItem() == Item.getItemFromBlock(Blocks.pumpkin)))
            {
                b0 = 4;
            }

            double d4 = 0.1D;
            double d0 = 0.9D;
            double d1 = 0.4D;
            double d2 = 1.6D;
            byte b1 = 0;
            boolean flag1 = this.isSmall();
            double d3 = flag1 ? p_174825_2_.yCoord * 2.0D : p_174825_2_.yCoord;

            if (d3 >= 0.1D && d3 < 0.1D + (flag1 ? 0.8D : 0.45D) && this.contents[1] != null)
            {
                b1 = 1;
            }
            else if (d3 >= 0.9D + (flag1 ? 0.3D : 0.0D) && d3 < 0.9D + (flag1 ? 1.0D : 0.7D) && this.contents[3] != null)
            {
                b1 = 3;
            }
            else if (d3 >= 0.4D && d3 < 0.4D + (flag1 ? 1.0D : 0.8D) && this.contents[2] != null)
            {
                b1 = 2;
            }
            else if (d3 >= 1.6D && this.contents[4] != null)
            {
                b1 = 4;
            }

            boolean flag2 = this.contents[b1] != null;

            if ((this.disabledSlots & 1 << b1) != 0 || (this.disabledSlots & 1 << b0) != 0)
            {
                b1 = b0;

                if ((this.disabledSlots & 1 << b0) != 0)
                {
                    if ((this.disabledSlots & 1) != 0)
                    {
                        return true;
                    }

                    b1 = 0;
                }
            }

            if (flag && b0 == 0 && !this.getShowArms())
            {
                return true;
            }
            else
            {
                if (flag)
                {
                    this.func_175422_a(p_174825_1_, b0);
                }
                else if (flag2)
                {
                    this.func_175422_a(p_174825_1_, b1);
                }

                return true;
            }
        }
        else
        {
            return true;
        }
    }

    private void func_175422_a(EntityPlayer p_175422_1_, int p_175422_2_)
    {
        ItemStack itemstack = this.contents[p_175422_2_];

        if (itemstack == null || (this.disabledSlots & 1 << p_175422_2_ + 8) == 0)
        {
            if (itemstack != null || (this.disabledSlots & 1 << p_175422_2_ + 16) == 0)
            {
                int j = p_175422_1_.inventory.currentItem;
                ItemStack itemstack1 = p_175422_1_.inventory.getStackInSlot(j);
                ItemStack itemstack2;

                if (p_175422_1_.capabilities.isCreativeMode && (itemstack == null || itemstack.getItem() == Item.getItemFromBlock(Blocks.air)) && itemstack1 != null)
                {
                    itemstack2 = itemstack1.copy();
                    itemstack2.stackSize = 1;
                    this.setCurrentItemOrArmor(p_175422_2_, itemstack2);
                }
                else if (itemstack1 != null && itemstack1.stackSize > 1)
                {
                    if (itemstack == null)
                    {
                        itemstack2 = itemstack1.copy();
                        itemstack2.stackSize = 1;
                        this.setCurrentItemOrArmor(p_175422_2_, itemstack2);
                        --itemstack1.stackSize;
                    }
                }
                else
                {
                    this.setCurrentItemOrArmor(p_175422_2_, itemstack1);
                    p_175422_1_.inventory.setInventorySlotContents(j, itemstack);
                }
            }
        }
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (!this.worldObj.isRemote && !this.canInteract)
        {
            if (DamageSource.outOfWorld.equals(source))
            {
                this.setDead();
                return false;
            }
            else if (this.isEntityInvulnerable(source))
            {
                return false;
            }
            else if (source.isExplosion())
            {
                this.dropContents();
                this.setDead();
                return false;
            }
            else if (DamageSource.inFire.equals(source))
            {
                if (!this.isBurning())
                {
                    this.setFire(5);
                }
                else
                {
                    this.damageArmorStand(0.15F);
                }

                return false;
            }
            else if (DamageSource.onFire.equals(source) && this.getHealth() > 0.5F)
            {
                this.damageArmorStand(4.0F);
                return false;
            }
            else
            {
                boolean flag = "arrow".equals(source.getDamageType());
                boolean flag1 = "player".equals(source.getDamageType());

                if (!flag1 && !flag)
                {
                    return false;
                }
                else
                {
                    if (source.getSourceOfDamage() instanceof EntityArrow)
                    {
                        source.getSourceOfDamage().setDead();
                    }

                    if (source.getEntity() instanceof EntityPlayer && !((EntityPlayer)source.getEntity()).capabilities.allowEdit)
                    {
                        return false;
                    }
                    else if (source.isCreativePlayer())
                    {
                        this.playParticles();
                        this.setDead();
                        return false;
                    }
                    else
                    {
                        long i = this.worldObj.getTotalWorldTime();

                        if (i - this.punchCooldown > 5L && !flag)
                        {
                            this.punchCooldown = i;
                        }
                        else
                        {
                            this.dropBlock();
                            this.playParticles();
                            this.setDead();
                        }

                        return false;
                    }
                }
            }
        }
        else
        {
            return false;
        }
    }

    private void playParticles()
    {
        if (this.worldObj instanceof WorldServer)
        {
            ((WorldServer)this.worldObj).spawnParticle(EnumParticleTypes.BLOCK_DUST, this.posX, this.posY + (double)this.height / 1.5D, this.posZ, 10, (double)(this.width / 4.0F), (double)(this.height / 4.0F), (double)(this.width / 4.0F), 0.05D, new int[] {Block.getStateId(Blocks.planks.getDefaultState())});
        }
    }

    private void damageArmorStand(float p_175406_1_)
    {
        float f1 = this.getHealth();
        f1 -= p_175406_1_;

        if (f1 <= 0.5F)
        {
            this.dropContents();
            this.setDead();
        }
        else
        {
            this.setHealth(f1);
        }
    }

    private void dropBlock()
    {
        Block.spawnAsEntity(this.worldObj, new BlockPos(this), new ItemStack(Items.armor_stand));
        this.dropContents();
    }

    private void dropContents()
    {
        for (int i = 0; i < this.contents.length; ++i)
        {
            if (this.contents[i] != null && this.contents[i].stackSize > 0)
            {
                if (this.contents[i] != null)
                {
                    Block.spawnAsEntity(this.worldObj, (new BlockPos(this)).up(), this.contents[i]);
                }

                this.contents[i] = null;
            }
        }
    }

    protected float func_110146_f(float p_110146_1_, float p_110146_2_)
    {
        this.prevRenderYawOffset = this.prevRotationYaw;
        this.renderYawOffset = this.rotationYaw;
        return 0.0F;
    }

    public float getEyeHeight()
    {
        return this.isChild() ? this.height * 0.5F : this.height * 0.9F;
    }

    /**
     * Moves the entity based on the specified heading.  Args: strafe, forward
     */
    public void moveEntityWithHeading(float p_70612_1_, float p_70612_2_)
    {
        if (!this.hasNoGravity())
        {
            super.moveEntityWithHeading(p_70612_1_, p_70612_2_);
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();
        Rotations rotations = this.dataWatcher.getWatchableObjectRotations(11);

        if (!this.headRotation.equals(rotations))
        {
            this.setHeadRotation(rotations);
        }

        Rotations rotations1 = this.dataWatcher.getWatchableObjectRotations(12);

        if (!this.bodyRotation.equals(rotations1))
        {
            this.setBodyRotation(rotations1);
        }

        Rotations rotations2 = this.dataWatcher.getWatchableObjectRotations(13);

        if (!this.leftArmRotation.equals(rotations2))
        {
            this.setLeftArmRotation(rotations2);
        }

        Rotations rotations3 = this.dataWatcher.getWatchableObjectRotations(14);

        if (!this.rightArmRotation.equals(rotations3))
        {
            this.setRightArmRotation(rotations3);
        }

        Rotations rotations4 = this.dataWatcher.getWatchableObjectRotations(15);

        if (!this.leftLegRotation.equals(rotations4))
        {
            this.setLeftLegRotation(rotations4);
        }

        Rotations rotations5 = this.dataWatcher.getWatchableObjectRotations(16);

        if (!this.rightLegRotation.equals(rotations5))
        {
            this.setRightLegRotation(rotations5);
        }
    }

    protected void func_175135_B()
    {
        this.setInvisible(this.canInteract);
    }

    public void setInvisible(boolean invisible)
    {
        this.canInteract = invisible;
        super.setInvisible(invisible);
    }

    /**
     * If Animal, checks if the age timer is negative
     */
    public boolean isChild()
    {
        return this.isSmall();
    }

    /**
     * Called by the /kill command.
     */
    public void onKillCommand()
    {
        this.setDead();
    }

    public boolean func_180427_aV()
    {
        return this.isInvisible();
    }

    private void setSmall(boolean p_175420_1_)
    {
        byte b0 = this.dataWatcher.getWatchableObjectByte(10);

        if (p_175420_1_)
        {
            b0 = (byte)(b0 | 1);
        }
        else
        {
            b0 &= -2;
        }

        this.dataWatcher.updateObject(10, Byte.valueOf(b0));
    }

    public boolean isSmall()
    {
        return (this.dataWatcher.getWatchableObjectByte(10) & 1) != 0;
    }

    private void setNoGravity(boolean p_175425_1_)
    {
        byte b0 = this.dataWatcher.getWatchableObjectByte(10);

        if (p_175425_1_)
        {
            b0 = (byte)(b0 | 2);
        }
        else
        {
            b0 &= -3;
        }

        this.dataWatcher.updateObject(10, Byte.valueOf(b0));
    }

    public boolean hasNoGravity()
    {
        return (this.dataWatcher.getWatchableObjectByte(10) & 2) != 0;
    }

    private void setShowArms(boolean p_175413_1_)
    {
        byte b0 = this.dataWatcher.getWatchableObjectByte(10);

        if (p_175413_1_)
        {
            b0 = (byte)(b0 | 4);
        }
        else
        {
            b0 &= -5;
        }

        this.dataWatcher.updateObject(10, Byte.valueOf(b0));
    }

    public boolean getShowArms()
    {
        return (this.dataWatcher.getWatchableObjectByte(10) & 4) != 0;
    }

    private void setNoBasePlate(boolean p_175426_1_)
    {
        byte b0 = this.dataWatcher.getWatchableObjectByte(10);

        if (p_175426_1_)
        {
            b0 = (byte)(b0 | 8);
        }
        else
        {
            b0 &= -9;
        }

        this.dataWatcher.updateObject(10, Byte.valueOf(b0));
    }

    public boolean hasNoBasePlate()
    {
        return (this.dataWatcher.getWatchableObjectByte(10) & 8) != 0;
    }

    public void setHeadRotation(Rotations p_175415_1_)
    {
        this.headRotation = p_175415_1_;
        this.dataWatcher.updateObject(11, p_175415_1_);
    }

    public void setBodyRotation(Rotations p_175424_1_)
    {
        this.bodyRotation = p_175424_1_;
        this.dataWatcher.updateObject(12, p_175424_1_);
    }

    public void setLeftArmRotation(Rotations p_175405_1_)
    {
        this.leftArmRotation = p_175405_1_;
        this.dataWatcher.updateObject(13, p_175405_1_);
    }

    public void setRightArmRotation(Rotations p_175428_1_)
    {
        this.rightArmRotation = p_175428_1_;
        this.dataWatcher.updateObject(14, p_175428_1_);
    }

    public void setLeftLegRotation(Rotations p_175417_1_)
    {
        this.leftLegRotation = p_175417_1_;
        this.dataWatcher.updateObject(15, p_175417_1_);
    }

    public void setRightLegRotation(Rotations p_175427_1_)
    {
        this.rightLegRotation = p_175427_1_;
        this.dataWatcher.updateObject(16, p_175427_1_);
    }

    public Rotations getHeadRotation()
    {
        return this.headRotation;
    }

    public Rotations getBodyRotation()
    {
        return this.bodyRotation;
    }

    @SideOnly(Side.CLIENT)
    public Rotations getLeftArmRotation()
    {
        return this.leftArmRotation;
    }

    @SideOnly(Side.CLIENT)
    public Rotations getRightArmRotation()
    {
        return this.rightArmRotation;
    }

    @SideOnly(Side.CLIENT)
    public Rotations getLeftLegRotation()
    {
        return this.leftLegRotation;
    }

    @SideOnly(Side.CLIENT)
    public Rotations getRightLegRotation()
    {
        return this.rightLegRotation;
    }
}