package net.minecraft.tileentity;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockHopper;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerHopper;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class TileEntityHopper extends TileEntityLockable implements IHopper, IUpdatePlayerListBox
{
    private ItemStack[] inventory = new ItemStack[5];
    private String customName;
    private int transferCooldown = -1;
    private static final String __OBFID = "CL_00000359";

    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        NBTTagList nbttaglist = compound.getTagList("Items", 10);
        this.inventory = new ItemStack[this.getSizeInventory()];

        if (compound.hasKey("CustomName", 8))
        {
            this.customName = compound.getString("CustomName");
        }

        this.transferCooldown = compound.getInteger("TransferCooldown");

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");

            if (b0 >= 0 && b0 < this.inventory.length)
            {
                this.inventory[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
    }

    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.inventory.length; ++i)
        {
            if (this.inventory[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.inventory[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        compound.setTag("Items", nbttaglist);
        compound.setInteger("TransferCooldown", this.transferCooldown);

        if (this.hasCustomName())
        {
            compound.setString("CustomName", this.customName);
        }
    }

    /**
     * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think it
     * hasn't changed and skip it.
     */
    public void markDirty()
    {
        super.markDirty();
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return this.inventory.length;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int index)
    {
        return this.inventory[index];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int index, int count)
    {
        if (this.inventory[index] != null)
        {
            ItemStack itemstack;

            if (this.inventory[index].stackSize <= count)
            {
                itemstack = this.inventory[index];
                this.inventory[index] = null;
                return itemstack;
            }
            else
            {
                itemstack = this.inventory[index].splitStack(count);

                if (this.inventory[index].stackSize == 0)
                {
                    this.inventory[index] = null;
                }

                return itemstack;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int index)
    {
        if (this.inventory[index] != null)
        {
            ItemStack itemstack = this.inventory[index];
            this.inventory[index] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        this.inventory[index] = stack;

        if (stack != null && stack.stackSize > this.getInventoryStackLimit())
        {
            stack.stackSize = this.getInventoryStackLimit();
        }
    }

    /**
     * Gets the name of this command sender (usually username, but possibly "Rcon")
     */
    public String getName()
    {
        return this.hasCustomName() ? this.customName : "container.hopper";
    }

    /**
     * Returns true if this thing is named
     */
    public boolean hasCustomName()
    {
        return this.customName != null && this.customName.length() > 0;
    }

    public void setCustomName(String customNameIn)
    {
        this.customName = customNameIn;
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
     * this more of a set than a get?*
     */
    public int getInventoryStackLimit()
    {
        return 64;
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return this.worldObj.getTileEntity(this.pos) != this ? false : player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
    }

    public void openInventory(EntityPlayer player) {}

    public void closeInventory(EntityPlayer player) {}

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return true;
    }

    /**
     * Updates the JList with a new model.
     */
    public void update()
    {
        if (this.worldObj != null && !this.worldObj.isRemote)
        {
            --this.transferCooldown;

            if (!this.isOnTransferCooldown())
            {
                this.setTransferCooldown(0);
                this.func_145887_i();
            }
        }
    }

    public boolean func_145887_i()
    {
        if (this.worldObj != null && !this.worldObj.isRemote)
        {
            if (!this.isOnTransferCooldown() && BlockHopper.isEnabled(this.getBlockMetadata()))
            {
                boolean flag = false;

                if (!this.func_152104_k())
                {
                    flag = this.func_145883_k();
                }

                if (!this.func_152105_l())
                {
                    flag = func_145891_a(this) || flag;
                }

                if (flag)
                {
                    this.setTransferCooldown(8);
                    this.markDirty();
                    return true;
                }
            }

            return false;
        }
        else
        {
            return false;
        }
    }

    private boolean func_152104_k()
    {
        ItemStack[] aitemstack = this.inventory;
        int i = aitemstack.length;

        for (int j = 0; j < i; ++j)
        {
            ItemStack itemstack = aitemstack[j];

            if (itemstack != null)
            {
                return false;
            }
        }

        return true;
    }

    private boolean func_152105_l()
    {
        ItemStack[] aitemstack = this.inventory;
        int i = aitemstack.length;

        for (int j = 0; j < i; ++j)
        {
            ItemStack itemstack = aitemstack[j];

            if (itemstack == null || itemstack.stackSize != itemstack.getMaxStackSize())
            {
                return false;
            }
        }

        return true;
    }

    private boolean func_145883_k()
    {
        IInventory iinventory = this.func_145895_l();

        if (iinventory == null)
        {
            return false;
        }
        else
        {
            EnumFacing enumfacing = BlockHopper.getFacing(this.getBlockMetadata()).getOpposite();

            if (this.func_174919_a(iinventory, enumfacing))
            {
                return false;
            }
            else
            {
                for (int i = 0; i < this.getSizeInventory(); ++i)
                {
                    if (this.getStackInSlot(i) != null)
                    {
                        ItemStack itemstack = this.getStackInSlot(i).copy();
                        ItemStack itemstack1 = func_174918_a(iinventory, this.decrStackSize(i, 1), enumfacing);

                        if (itemstack1 == null || itemstack1.stackSize == 0)
                        {
                            iinventory.markDirty();
                            return true;
                        }

                        this.setInventorySlotContents(i, itemstack);
                    }
                }

                return false;
            }
        }
    }

    private boolean func_174919_a(IInventory p_174919_1_, EnumFacing p_174919_2_)
    {
        if (p_174919_1_ instanceof ISidedInventory)
        {
            ISidedInventory isidedinventory = (ISidedInventory)p_174919_1_;
            int[] aint = isidedinventory.getSlotsForFace(p_174919_2_);

            for (int i = 0; i < aint.length; ++i)
            {
                ItemStack itemstack = isidedinventory.getStackInSlot(aint[i]);

                if (itemstack == null || itemstack.stackSize != itemstack.getMaxStackSize())
                {
                    return false;
                }
            }
        }
        else
        {
            int j = p_174919_1_.getSizeInventory();

            for (int k = 0; k < j; ++k)
            {
                ItemStack itemstack1 = p_174919_1_.getStackInSlot(k);

                if (itemstack1 == null || itemstack1.stackSize != itemstack1.getMaxStackSize())
                {
                    return false;
                }
            }
        }

        return true;
    }

    private static boolean func_174917_b(IInventory p_174917_0_, EnumFacing p_174917_1_)
    {
        if (p_174917_0_ instanceof ISidedInventory)
        {
            ISidedInventory isidedinventory = (ISidedInventory)p_174917_0_;
            int[] aint = isidedinventory.getSlotsForFace(p_174917_1_);

            for (int i = 0; i < aint.length; ++i)
            {
                if (isidedinventory.getStackInSlot(aint[i]) != null)
                {
                    return false;
                }
            }
        }
        else
        {
            int j = p_174917_0_.getSizeInventory();

            for (int k = 0; k < j; ++k)
            {
                if (p_174917_0_.getStackInSlot(k) != null)
                {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean func_145891_a(IHopper p_145891_0_)
    {
        IInventory iinventory = func_145884_b(p_145891_0_);

        if (iinventory != null)
        {
            EnumFacing enumfacing = EnumFacing.DOWN;

            if (func_174917_b(iinventory, enumfacing))
            {
                return false;
            }

            if (iinventory instanceof ISidedInventory)
            {
                ISidedInventory isidedinventory = (ISidedInventory)iinventory;
                int[] aint = isidedinventory.getSlotsForFace(enumfacing);

                for (int i = 0; i < aint.length; ++i)
                {
                    if (func_174915_a(p_145891_0_, iinventory, aint[i], enumfacing))
                    {
                        return true;
                    }
                }
            }
            else
            {
                int j = iinventory.getSizeInventory();

                for (int k = 0; k < j; ++k)
                {
                    if (func_174915_a(p_145891_0_, iinventory, k, enumfacing))
                    {
                        return true;
                    }
                }
            }
        }
        else
        {
            EntityItem entityitem = func_145897_a(p_145891_0_.getWorld(), p_145891_0_.getXPos(), p_145891_0_.getYPos() + 1.0D, p_145891_0_.getZPos());

            if (entityitem != null)
            {
                return func_145898_a(p_145891_0_, entityitem);
            }
        }

        return false;
    }

    private static boolean func_174915_a(IHopper p_174915_0_, IInventory p_174915_1_, int p_174915_2_, EnumFacing p_174915_3_)
    {
        ItemStack itemstack = p_174915_1_.getStackInSlot(p_174915_2_);

        if (itemstack != null && func_174921_b(p_174915_1_, itemstack, p_174915_2_, p_174915_3_))
        {
            ItemStack itemstack1 = itemstack.copy();
            ItemStack itemstack2 = func_174918_a(p_174915_0_, p_174915_1_.decrStackSize(p_174915_2_, 1), (EnumFacing)null);

            if (itemstack2 == null || itemstack2.stackSize == 0)
            {
                p_174915_1_.markDirty();
                return true;
            }

            p_174915_1_.setInventorySlotContents(p_174915_2_, itemstack1);
        }

        return false;
    }

    public static boolean func_145898_a(IInventory p_145898_0_, EntityItem p_145898_1_)
    {
        boolean flag = false;

        if (p_145898_1_ == null)
        {
            return false;
        }
        else
        {
            ItemStack itemstack = p_145898_1_.getEntityItem().copy();
            ItemStack itemstack1 = func_174918_a(p_145898_0_, itemstack, (EnumFacing)null);

            if (itemstack1 != null && itemstack1.stackSize != 0)
            {
                p_145898_1_.setEntityItemStack(itemstack1);
            }
            else
            {
                flag = true;
                p_145898_1_.setDead();
            }

            return flag;
        }
    }

    public static ItemStack func_174918_a(IInventory p_174918_0_, ItemStack p_174918_1_, EnumFacing p_174918_2_)
    {
        if (p_174918_0_ instanceof ISidedInventory && p_174918_2_ != null)
        {
            ISidedInventory isidedinventory = (ISidedInventory)p_174918_0_;
            int[] aint = isidedinventory.getSlotsForFace(p_174918_2_);

            for (int k = 0; k < aint.length && p_174918_1_ != null && p_174918_1_.stackSize > 0; ++k)
            {
                p_174918_1_ = func_174916_c(p_174918_0_, p_174918_1_, aint[k], p_174918_2_);
            }
        }
        else
        {
            int i = p_174918_0_.getSizeInventory();

            for (int j = 0; j < i && p_174918_1_ != null && p_174918_1_.stackSize > 0; ++j)
            {
                p_174918_1_ = func_174916_c(p_174918_0_, p_174918_1_, j, p_174918_2_);
            }
        }

        if (p_174918_1_ != null && p_174918_1_.stackSize == 0)
        {
            p_174918_1_ = null;
        }

        return p_174918_1_;
    }

    private static boolean func_174920_a(IInventory p_174920_0_, ItemStack p_174920_1_, int p_174920_2_, EnumFacing p_174920_3_)
    {
        return !p_174920_0_.isItemValidForSlot(p_174920_2_, p_174920_1_) ? false : !(p_174920_0_ instanceof ISidedInventory) || ((ISidedInventory)p_174920_0_).canInsertItem(p_174920_2_, p_174920_1_, p_174920_3_);
    }

    private static boolean func_174921_b(IInventory p_174921_0_, ItemStack p_174921_1_, int p_174921_2_, EnumFacing p_174921_3_)
    {
        return !(p_174921_0_ instanceof ISidedInventory) || ((ISidedInventory)p_174921_0_).canExtractItem(p_174921_2_, p_174921_1_, p_174921_3_);
    }

    private static ItemStack func_174916_c(IInventory p_174916_0_, ItemStack p_174916_1_, int p_174916_2_, EnumFacing p_174916_3_)
    {
        ItemStack itemstack1 = p_174916_0_.getStackInSlot(p_174916_2_);

        if (func_174920_a(p_174916_0_, p_174916_1_, p_174916_2_, p_174916_3_))
        {
            boolean flag = false;

            if (itemstack1 == null)
            {
                //Forge: BUGFIX: Again, make things respect max stack sizes.
                int max = Math.min(p_174916_1_.getMaxStackSize(), p_174916_0_.getInventoryStackLimit());
                if (max >= p_174916_1_.stackSize)
                {
                    p_174916_0_.setInventorySlotContents(p_174916_2_, p_174916_1_);
                    p_174916_1_ = null;
                }
                else
                {
                    p_174916_0_.setInventorySlotContents(p_174916_2_, p_174916_1_.splitStack(max));
                }
                flag = true;
            }
            else if (canCombine(itemstack1, p_174916_1_))
            {
                //Forge: BUGFIX: Again, make things respect max stack sizes.
                int max = Math.min(p_174916_1_.getMaxStackSize(), p_174916_0_.getInventoryStackLimit());
                if (max > itemstack1.stackSize)
                {
                    int size = Math.min(p_174916_1_.stackSize, max - itemstack1.stackSize);
                    p_174916_1_.stackSize -= size;
                    itemstack1.stackSize += size;
                    flag = size > 0;
                }
            }

            if (flag)
            {
                if (p_174916_0_ instanceof TileEntityHopper)
                {
                    TileEntityHopper tileentityhopper = (TileEntityHopper)p_174916_0_;

                    if (tileentityhopper.mayTransfer())
                    {
                        tileentityhopper.setTransferCooldown(8);
                    }

                    p_174916_0_.markDirty();
                }

                p_174916_0_.markDirty();
            }
        }

        return p_174916_1_;
    }

    private IInventory func_145895_l()
    {
        EnumFacing enumfacing = BlockHopper.getFacing(this.getBlockMetadata());
        return func_145893_b(this.getWorld(), (double)(this.pos.getX() + enumfacing.getFrontOffsetX()), (double)(this.pos.getY() + enumfacing.getFrontOffsetY()), (double)(this.pos.getZ() + enumfacing.getFrontOffsetZ()));
    }

    public static IInventory func_145884_b(IHopper p_145884_0_)
    {
        return func_145893_b(p_145884_0_.getWorld(), p_145884_0_.getXPos(), p_145884_0_.getYPos() + 1.0D, p_145884_0_.getZPos());
    }

    public static EntityItem func_145897_a(World worldIn, double p_145897_1_, double p_145897_3_, double p_145897_5_)
    {
        List list = worldIn.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(p_145897_1_, p_145897_3_, p_145897_5_, p_145897_1_ + 1.0D, p_145897_3_ + 1.0D, p_145897_5_ + 1.0D), IEntitySelector.selectAnything);
        return list.size() > 0 ? (EntityItem)list.get(0) : null;
    }

    public static IInventory func_145893_b(World worldIn, double p_145893_1_, double p_145893_3_, double p_145893_5_)
    {
        Object object = null;
        int i = MathHelper.floor_double(p_145893_1_);
        int j = MathHelper.floor_double(p_145893_3_);
        int k = MathHelper.floor_double(p_145893_5_);
        BlockPos blockpos = new BlockPos(i, j, k);
        TileEntity tileentity = worldIn.getTileEntity(new BlockPos(i, j, k));

        if (tileentity instanceof IInventory)
        {
            object = (IInventory)tileentity;

            if (object instanceof TileEntityChest)
            {
                Block block = worldIn.getBlockState(new BlockPos(i, j, k)).getBlock();

                if (block instanceof BlockChest)
                {
                    object = ((BlockChest)block).getLockableContainer(worldIn, blockpos);
                }
            }
        }

        if (object == null)
        {
            List list = worldIn.func_175674_a((Entity)null, new AxisAlignedBB(p_145893_1_, p_145893_3_, p_145893_5_, p_145893_1_ + 1.0D, p_145893_3_ + 1.0D, p_145893_5_ + 1.0D), IEntitySelector.selectInventories);

            if (list.size() > 0)
            {
                object = (IInventory)list.get(worldIn.rand.nextInt(list.size()));
            }
        }

        return (IInventory)object;
    }

    private static boolean canCombine(ItemStack stack1, ItemStack stack2)
    {
        return stack1.getItem() != stack2.getItem() ? false : (stack1.getMetadata() != stack2.getMetadata() ? false : (stack1.stackSize > stack1.getMaxStackSize() ? false : ItemStack.areItemStackTagsEqual(stack1, stack2)));
    }

    /**
     * Gets the world X position for this hopper entity.
     */
    public double getXPos()
    {
        return (double)this.pos.getX();
    }

    /**
     * Gets the world Y position for this hopper entity.
     */
    public double getYPos()
    {
        return (double)this.pos.getY();
    }

    /**
     * Gets the world Z position for this hopper entity.
     */
    public double getZPos()
    {
        return (double)this.pos.getZ();
    }

    public void setTransferCooldown(int ticks)
    {
        this.transferCooldown = ticks;
    }

    public boolean isOnTransferCooldown()
    {
        return this.transferCooldown > 0;
    }

    public boolean mayTransfer()
    {
        return this.transferCooldown <= 1;
    }

    public String getGuiID()
    {
        return "minecraft:hopper";
    }

    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        return new ContainerHopper(playerInventory, this, playerIn);
    }

    public int getField(int id)
    {
        return 0;
    }

    public void setField(int id, int value) {}

    public int getFieldCount()
    {
        return 0;
    }

    public void clear()
    {
        for (int i = 0; i < this.inventory.length; ++i)
        {
            this.inventory[i] = null;
        }
    }
}