package net.minecraft.util;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityDispenser;

public class WeightedRandomChestContent extends WeightedRandom.Item
{
    /** The Item/Block ID to generate in the Chest. */
    public ItemStack theItemId;
    /** The minimum chance of item generating. */
    public int theMinimumChanceToGenerateItem;
    /** The maximum chance of item generating. */
    public int theMaximumChanceToGenerateItem;
    private static final String __OBFID = "CL_00001505";

    public WeightedRandomChestContent(Item p_i45311_1_, int p_i45311_2_, int minimumChance, int maximumChance, int itemWeightIn)
    {
        super(itemWeightIn);
        this.theItemId = new ItemStack(p_i45311_1_, 1, p_i45311_2_);
        this.theMinimumChanceToGenerateItem = minimumChance;
        this.theMaximumChanceToGenerateItem = maximumChance;
    }

    public WeightedRandomChestContent(ItemStack stack, int minimumChance, int maximumChance, int itemWeightIn)
    {
        super(itemWeightIn);
        this.theItemId = stack;
        this.theMinimumChanceToGenerateItem = minimumChance;
        this.theMaximumChanceToGenerateItem = maximumChance;
    }

    public static void generateChestContents(Random random, List p_177630_1_, IInventory p_177630_2_, int p_177630_3_)
    {
        for (int j = 0; j < p_177630_3_; ++j)
        {
            WeightedRandomChestContent weightedrandomchestcontent = (WeightedRandomChestContent)WeightedRandom.getRandomItem(random, p_177630_1_);
            ItemStack[] stacks = weightedrandomchestcontent.generateChestContent(random, p_177630_2_);

            for (ItemStack itemstack : stacks)
            {
                p_177630_2_.setInventorySlotContents(random.nextInt(p_177630_2_.getSizeInventory()), itemstack);
            }
        }
    }

    public static void generateDispenserContents(Random random, List p_177631_1_, TileEntityDispenser dispenser, int p_177631_3_)
    {
        for (int j = 0; j < p_177631_3_; ++j)
        {
            WeightedRandomChestContent weightedrandomchestcontent = (WeightedRandomChestContent)WeightedRandom.getRandomItem(random, p_177631_1_);
            ItemStack[] stacks = weightedrandomchestcontent.generateChestContent(random, dispenser);

            for (ItemStack itemstack : stacks)
            {
                dispenser.setInventorySlotContents(random.nextInt(dispenser.getSizeInventory()), itemstack);
            }
        }
    }

    public static List func_177629_a(List p_177629_0_, WeightedRandomChestContent ... p_177629_1_)
    {
        ArrayList arraylist = Lists.newArrayList(p_177629_0_);
        Collections.addAll(arraylist, p_177629_1_);
        return arraylist;
    }

    // -- Forge hooks
    /**
     * Allow a mod to submit a custom implementation that can delegate item stack generation beyond simple stack lookup
     *
     * @param random The current random for generation
     * @param newInventory The inventory being generated (do not populate it, but you can refer to it)
     * @return An array of {@link ItemStack} to put into the chest
     */
    protected ItemStack[] generateChestContent(Random random, IInventory newInventory)
    {
        return net.minecraftforge.common.ChestGenHooks.generateStacks(random, theItemId, theMinimumChanceToGenerateItem, theMaximumChanceToGenerateItem);
    }
}