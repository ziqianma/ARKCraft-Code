package com.arkcraft.module.item.common.tile;

import com.arkcraft.lib.LogHelper;
import com.arkcraft.module.item.common.items.ARKCraftItems;
import com.arkcraft.module.item.common.config.ModuleItemBalance;
import com.arkcraft.module.item.common.items.ItemFeces;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;

import java.util.Arrays;

/**
 * @author wildbill22
 */
public class TileInventoryAttachment2 implements IInventory, IUpdatePlayerListBox
{
    public static final int COMPOST_SLOTS_COUNT = 8;
    public static final int TOTAL_SLOTS_COUNT = COMPOST_SLOTS_COUNT;
    public static final int LAST_INVENTORY_SLOT = TOTAL_SLOTS_COUNT - 1;

    public static final int FIRST_COMPOST_SLOT = 0;

    // Create and initialize the itemStacks variable that will store store the itemStacks
    private ItemStack[] itemStacks = new ItemStack[TOTAL_SLOTS_COUNT];
    private final ItemStack invItem;

    // Other class variables
    int tick = 20;

    /**
     * The number of composting seconds remaining on the current piece of thatch
     */
    private int[] compostTimeRemaining = new int[COMPOST_SLOTS_COUNT];
    /**
     * The initial composting value of the currently burning thatch (in seconds of composting duration)
     */
    private static int compostTimeInitialValue = ModuleItemBalance.COMPOST_BIN.COMPOST_TIME_FOR_THATCH;

    /**
     * Seconds of thatch burn time remaining for the item in a slot
     */
    public int secondsOfThatchRemaining(int i)
    {
        if (itemStacks[i] != null && getItemCompostTime(itemStacks[i]) > 0)
        {
            if (compostTimeRemaining[i] > 0)
            {
                return compostTimeRemaining[i];
            }
        }
        return 0;
    }

    /**
     * The number of seconds the current item has been composting
     */
    private short compostTime;
    /**
     * The number of seconds required to create a fertilizer
     */
    private static final short COMPOST_TIME_FOR_FECES = (short) ModuleItemBalance.COMPOST_BIN.COMPOST_TIME_FOR_FECES;

    /**
     * Returns double between 0 and 1 representing % complete
     */
    public double getFractionCompostTimeComplete()
    {
        double fraction = compostTime / (double) COMPOST_TIME_FOR_FECES;
        return MathHelper.clamp_double(fraction, 0.0, 1.0);
    }

    private int cachedNumberOfDecomposingSlots = -1;

    public TileInventoryAttachment2(ItemStack stack)
    {
        invItem = stack;

        // Create a new NBT Tag Compound if one doesn't already exist, or you will crash
        if (!stack.hasTagCompound())
        {
            stack.setTagCompound(new NBTTagCompound());
        }
        // note that it's okay to use stack instead of invItem right there
        // both reference the same memory location, so whatever you change using
        // either reference will change in the other

        // Read the inventory contents from NBT
        readFromNBT(stack.getTagCompound());
    }


    @Override
    public void update()
    {
        if (tick >= 0)
        {
            tick--;
            return;
        }
        else
        {
            tick = 20;
        }

        int numberDecomposing = decomposeFeces();
        if (cachedNumberOfDecomposingSlots != numberDecomposing)
        {
            cachedNumberOfDecomposingSlots = numberDecomposing;
        }

        // If there is no feces decomposing or there is no thatch or room in the output, don't burn the thatch
        if (isThatchPresent() && numberDecomposing > 0 && canCompost())
        {
            burnThatch();

            compostTime += numberDecomposing;

            // If compostTime is reached, create a fertilizer and reset compostTime
            if (compostTime >= COMPOST_TIME_FOR_FECES)
            {
//				LogHelper.info("TileInventoryCompostBin: About to create fertilizer on " + (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT ? "client" : "server"));
                compostFertilizer();
                compostTime = 0;
            }
        }
        else
        {
            compostTime = 0;
        }
    }

    /**
     * for each slot with feces: decreases the burn time, checks if burnTimeRemaining = 0
     * and tries to consume a new piece of thatch if one is available
     */
    private int decomposeFeces()
    {
        int decomposingCount = 0;
        boolean inventoryChanged = false;

        // Iterate over all the compost bin slots
        for (int i = 0; i < COMPOST_SLOTS_COUNT; i++)
        {
            if (itemStacks[i] != null && getItemDecompostTime(itemStacks[i]) > 0 && itemStacks[i].getItem() != ARKCraftItems.fertilizer)
            {
                if (increaseStackDamage(itemStacks[i]))
                {
                    itemStacks[i] = null;
                    inventoryChanged = true;
                }
                ++decomposingCount;
            }
        }
        if (inventoryChanged)
        {
            markDirty();
        }
        return decomposingCount;
    }

    /**
     * for each slot with thatch: decreases the burn time, checks if burnTimeRemaining = 0
     * and tries to consume a new piece of thatch if one is available
     */
    private void burnThatch()
    {
        boolean inventoryChanged = false;

        // Iterate over all the compost bin slots
        for (int i = 0; i < COMPOST_SLOTS_COUNT; i++)
        {
            if (itemStacks[i] != null && getItemCompostTime(itemStacks[i]) > 0)
            {
                if (compostTimeRemaining[i] > 0)
                {
                    compostTimeRemaining[i]--;
                    if (compostTimeRemaining[i] <= 0)
                    {
                        itemStacks[i].stackSize--;
                        inventoryChanged = true;
                        if (itemStacks[i].stackSize == 0)
                        {
                            itemStacks[i] = null;
                        }
                        else
                        {
                            compostTimeRemaining[i] = compostTimeInitialValue;
                        }
                    }
                }
                // Initialize a new stack just added
                else
                {
                    compostTimeRemaining[i] = compostTimeInitialValue;
                }
                break; // Just burn one thatch at a time
            }
        }
        if (inventoryChanged)
        {
            markDirty();
        }
    }

    /**
     * Check each slot for thatch
     */
    private boolean isThatchPresent()
    {
        // Iterate over all the compost bin slots
        for (int i = 0; i < COMPOST_SLOTS_COUNT; i++)
        {
            if (itemStacks[i] != null && getItemCompostTime(itemStacks[i]) > 0)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the compost bin is composting and there is sufficient space in the output slots
     *
     * @return true if harvesting a berry is possible
     */
    private boolean canCompost() {return compostFeces(false);}

    /**
     * Harvest a berry from the seed, if possible
     */
    private void compostFertilizer() {compostFeces(true);}

    /**
     * checks that there is an item to be composted in one of the input slots and that there is room for the result in the output slots
     * If desired, performs the compost to fertilizer
     *
     * @param compostFeces If true, harvest a berry. If false, check whether harvesting is possible, but don't change the inventory
     * @return false if no fertilizer can be composted, true otherwise
     */
    private boolean compostFeces(boolean compostFeces)
    {
        Integer firstSuitableOutputSlot = null;

        // find the first suitable output slot- either empty, or with identical item that has enough space
        for (int outputSlot = LAST_INVENTORY_SLOT; outputSlot > FIRST_COMPOST_SLOT; outputSlot--)
        {
            ItemStack outputStack = itemStacks[outputSlot];
            // Empty slot?
            if (outputStack == null)
            {
                firstSuitableOutputSlot = outputSlot;
                break;
            }
            // Fertilizer item and with enough space?
            if (itemStacks[outputSlot].getItem() == ARKCraftItems.fertilizer)
            {
                int combinedSize = itemStacks[outputSlot].stackSize + 1;
                if (combinedSize <= getInventoryStackLimit() && combinedSize <= itemStacks[outputSlot].getMaxStackSize())
                {
                    firstSuitableOutputSlot = outputSlot;
                    break;
                }
            }
        }

        // If no suitable output slot, return false
        if (firstSuitableOutputSlot == null)
        {
            return false;
        }

        // If true, we create a new fertilizer
        if (!compostFeces)
        {
            return true;
        }

        // alter output slot
        if (itemStacks[firstSuitableOutputSlot] == null)
        {
            itemStacks[firstSuitableOutputSlot] = new ItemStack(ARKCraftItems.fertilizer);
        }
        else
        {
            itemStacks[firstSuitableOutputSlot].stackSize += 1;
        }
        markDirty();
        return true;
    }

    /**
     * returns the number of seconds the given item will decompose.
     *
     * @param stack
     * @return Returns 0 if the given item is not a valid feces
     */
    public static short getItemDecompostTime(ItemStack stack)
    {
        int growtime = 0;
        if (stack != null)
        {
            if (stack.getItem() instanceof ItemFeces)
            {
                growtime = ItemFeces.getItemGrowTime(stack);
            }
        }
        return (short) MathHelper.clamp_int(growtime, 0, Short.MAX_VALUE);
    }

    /**
     * returns the number of seconds the given item will decompose.
     *
     * @param stack
     * @return Returns 0 if the given item is not a valid thatching item
     */
    public static short getItemCompostTime(ItemStack stack)
    {
        int compostTime = 0;
        if (stack != null)
        {
            if (stack.getItem() == ARKCraftItems.thatch)
            {
                compostTime = compostTimeInitialValue;
            }
        }
        return (short) MathHelper.clamp_int(compostTime, 0, Short.MAX_VALUE);
    }

    /**
     * Adds one one damage to stack
     *
     * @param itemStack
     * @return true if stack is destroyed
     */
    private static boolean increaseStackDamage(ItemStack itemStack)
    {
        if (itemStack == null)
        {
            return true;
        }
        int itemDamage = itemStack.getItemDamage();
        itemStack.setItemDamage(++itemDamage);
        if (itemStack.getItemDamage() >= itemStack.getItem().getMaxDamage())
        {
            return true;
        }
        return false;
    }

    @Override
    public String getName()
    {
        return "tile.compost_bin.name";
    }

    @Override
    public boolean hasCustomName()
    {
        return false;
    }

    @Override
    public IChatComponent getDisplayName()
    {
        return this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName());
    }

    @Override
    public int getSizeInventory()
    {
        return itemStacks.length;
    }

    @Override
    public ItemStack getStackInSlot(int index)
    {
        return itemStacks[index];
    }

    @Override
    public ItemStack decrStackSize(int slotIndex, int count)
    {
        ItemStack itemStackInSlot = getStackInSlot(slotIndex);
        if (itemStackInSlot == null)
        {
            return null;
        }

        ItemStack itemStackRemoved;
        if (itemStackInSlot.stackSize <= count)
        {
            itemStackRemoved = itemStackInSlot;
            setInventorySlotContents(slotIndex, null);
        }
        else
        {
            itemStackRemoved = itemStackInSlot.splitStack(count);
            if (itemStackInSlot.stackSize == 0)
            {
                setInventorySlotContents(slotIndex, null);
            }
        }
        markDirty();
        return itemStackRemoved;
    }

    // Nothing to do, this is a furnace type inventory
    @Override
    public ItemStack getStackInSlotOnClosing(int index)
    {
        return null;
    }

    @Override
    public void setInventorySlotContents(int slotIndex, ItemStack itemstack)
    {
        itemStacks[slotIndex] = itemstack;
        if (itemstack != null && itemstack.stackSize > getInventoryStackLimit())
        {
            itemstack.stackSize = getInventoryStackLimit();
        }
        markDirty();
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player)
    {
    }

    @Override
    public void closeInventory(EntityPlayer player)
    {
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return isItemValidForSlot(stack);
    }

    // Return true if stack is a valid item for the compost bin
    public boolean isItemValidForSlot(ItemStack stack)
    {
        if (stack != null)
        {
            // Feces?
            if (getItemDecompostTime(stack) > 0)
            {
                return true;
            }
            // Thatch?
            if (getItemCompostTime(stack) > 0)
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public void clear()
    {
        Arrays.fill(itemStacks, null);
    }

    //------------------------------

    // This is where you save any data that you don't want to lose when the tile entity unloads
    // In this case, it saves the state of the compost bin (burn time etc) and the itemstacks in the inventory

    public void writeToNBT(NBTTagCompound parentNBTTagCompound)
    {
        //	super.writeToNBT(parentNBTTagCompound);
        //	NBTTagList items = new NBTTagList();
        // The super call is required to save and load the tiles location

        // Save the stored item stacks

        // to use an analogy with Java, this code generates an array of hashmaps
        // The itemStack in each slot is converted to an NBTTagCompound, which is effectively a hashmap of key->value pairs such
        //   as slot=1, id=2353, count=1, etc
        // Each of these NBTTagCompound are then inserted into NBTTagList, which is similar to an array.
        NBTTagList dataForAllSlots = new NBTTagList();
        for (int i = 0; i < this.itemStacks.length; ++i)
        {
            if (this.itemStacks[i] != null)
            {
                NBTTagCompound dataForThisSlot = new NBTTagCompound();
                dataForThisSlot.setByte("Slot", (byte) i);
                this.itemStacks[i].writeToNBT(dataForThisSlot);
                dataForAllSlots.appendTag(dataForThisSlot);
            }
        }
        // the array of hashmaps is then inserted into the parent hashmap for the container
        parentNBTTagCompound.setTag("Items", dataForAllSlots);

        // Save everything else
        parentNBTTagCompound.setShort("compostTime", compostTime);
        parentNBTTagCompound.setTag("compostTimeRemaining", new NBTTagIntArray(compostTimeRemaining));
        LogHelper.info("TileInventoryCompostBin: Wrote inventory.");
    }

    // This is where you load the data that you saved in writeToNBT
    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
        //	super.readFromNBT(nbtTagCompound); // The super call is required to save and load the tiles location
        final byte NBT_TYPE_COMPOUND = 10;       // See NBTBase.createNewByType() for a listing
        NBTTagList dataForAllSlots = nbtTagCompound.getTagList("Items", NBT_TYPE_COMPOUND);

        Arrays.fill(itemStacks, null);           // set all slots to empty
        for (int i = 0; i < dataForAllSlots.tagCount(); ++i)
        {
            NBTTagCompound dataForOneSlot = dataForAllSlots.getCompoundTagAt(i);
            byte slotNumber = dataForOneSlot.getByte("Slot");
            if (slotNumber >= 0 && slotNumber < this.itemStacks.length)
            {
                this.itemStacks[slotNumber] = ItemStack.loadItemStackFromNBT(dataForOneSlot);
            }
        }

        // Load everything else.  Trim the arrays (or pad with 0) to make sure they have the correct number of elements
        compostTime = nbtTagCompound.getShort("compostTime");
        compostTimeRemaining = Arrays.copyOf(nbtTagCompound.getIntArray("compostTimeRemaining"), COMPOST_SLOTS_COUNT);
        cachedNumberOfDecomposingSlots = -1;
        LogHelper.info("TileInventoryCompostBin: Read inventory.");
    }


    private static final byte COMPOST_FIELD_ID = 0;
    private static final byte FIRST_COMPOST_TIME_FIELD_ID = 1;
    private static final byte NUMBER_OF_FIELDS = FIRST_COMPOST_TIME_FIELD_ID + (byte) COMPOST_SLOTS_COUNT;

    @Override
    public int getField(int id)
    {
        if (id == COMPOST_FIELD_ID)
        {
            return compostTime;
        }
        if (id >= FIRST_COMPOST_TIME_FIELD_ID && id < NUMBER_OF_FIELDS)
        {
            return compostTimeRemaining[id - FIRST_COMPOST_TIME_FIELD_ID];
        }
        System.err.println("Invalid field ID in TileInventoryCompost.getField:" + id);
        return 0;
    }

    @Override
    public void setField(int id, int value)
    {
        if (id == COMPOST_FIELD_ID)
        {
            compostTime = (short) value;
        }
        else if (id >= FIRST_COMPOST_TIME_FIELD_ID && id < NUMBER_OF_FIELDS)
        {
            compostTimeRemaining[id - FIRST_COMPOST_TIME_FIELD_ID] = value;
        }
        else
        {
            System.err.println("Invalid field ID in TileInventoryCompost.setField:" + id);
        }
    }

    @Override
    public int getFieldCount()
    {
        return NUMBER_OF_FIELDS;
    }

    @Override
    public void markDirty()
    {
        for (int i = 0; i < getSizeInventory(); ++i)
        {
            if (getStackInSlot(i) != null && getStackInSlot(i).stackSize == 0)
            {
                itemStacks[i] = null;
            }
        }

        // This line here does the work:
        writeToNBT(invItem.getTagCompound());
    }
}
