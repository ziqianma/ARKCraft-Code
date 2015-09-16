package net.minecraft.enchantment;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.WeightedRandom;

public class EnchantmentHelper
{
    /** Is the random seed of enchantment effects. */
    private static final Random enchantmentRand = new Random();
    /** Used to calculate the extra armor of enchantments on armors equipped on player. */
    private static final EnchantmentHelper.ModifierDamage enchantmentModifierDamage = new EnchantmentHelper.ModifierDamage(null);
    /** Used to calculate the (magic) extra damage done by enchantments on current equipped item of player. */
    private static final EnchantmentHelper.ModifierLiving enchantmentModifierLiving = new EnchantmentHelper.ModifierLiving(null);
    private static final EnchantmentHelper.HurtIterator field_151388_d = new EnchantmentHelper.HurtIterator(null);
    private static final EnchantmentHelper.DamageIterator field_151389_e = new EnchantmentHelper.DamageIterator(null);
    private static final String __OBFID = "CL_00000107";

    /**
     * Returns the level of enchantment on the ItemStack passed.
     *  
     * @param enchID The ID for the enchantment you are looking for.
     * @param stack The ItemStack being searched.
     */
    public static int getEnchantmentLevel(int enchID, ItemStack stack)
    {
        if (stack == null)
        {
            return 0;
        }
        else
        {
            NBTTagList nbttaglist = stack.getEnchantmentTagList();

            if (nbttaglist == null)
            {
                return 0;
            }
            else
            {
                for (int j = 0; j < nbttaglist.tagCount(); ++j)
                {
                    short short1 = nbttaglist.getCompoundTagAt(j).getShort("id");
                    short short2 = nbttaglist.getCompoundTagAt(j).getShort("lvl");

                    if (short1 == enchID)
                    {
                        return short2;
                    }
                }

                return 0;
            }
        }
    }

    /**
     * Return the enchantments for the specified stack.
     *  
     * @param stack The stack being searched for enchantments.
     */
    public static Map getEnchantments(ItemStack stack)
    {
        LinkedHashMap linkedhashmap = Maps.newLinkedHashMap();
        NBTTagList nbttaglist = stack.getItem() == Items.enchanted_book ? Items.enchanted_book.getEnchantments(stack) : stack.getEnchantmentTagList();

        if (nbttaglist != null)
        {
            for (int i = 0; i < nbttaglist.tagCount(); ++i)
            {
                short short1 = nbttaglist.getCompoundTagAt(i).getShort("id");
                short short2 = nbttaglist.getCompoundTagAt(i).getShort("lvl");
                linkedhashmap.put(Integer.valueOf(short1), Integer.valueOf(short2));
            }
        }

        return linkedhashmap;
    }

    /**
     * Set the enchantments for the specified stack.
     *  
     * @param enchMap A map containing all the enchantments you wish to add. Enchantments stored with the ID as the key,
     * and the level as the value.
     * @param stack The stack to have enchantments applied to.
     */
    public static void setEnchantments(Map enchMap, ItemStack stack)
    {
        NBTTagList nbttaglist = new NBTTagList();
        Iterator iterator = enchMap.keySet().iterator();

        while (iterator.hasNext())
        {
            int i = ((Integer)iterator.next()).intValue();
            Enchantment enchantment = Enchantment.getEnchantmentById(i);

            if (enchantment != null)
            {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setShort("id", (short)i);
                nbttagcompound.setShort("lvl", (short)((Integer)enchMap.get(Integer.valueOf(i))).intValue());
                nbttaglist.appendTag(nbttagcompound);

                if (stack.getItem() == Items.enchanted_book)
                {
                    Items.enchanted_book.addEnchantment(stack, new EnchantmentData(enchantment, ((Integer)enchMap.get(Integer.valueOf(i))).intValue()));
                }
            }
        }

        if (nbttaglist.tagCount() > 0)
        {
            if (stack.getItem() != Items.enchanted_book)
            {
                stack.setTagInfo("ench", nbttaglist);
            }
        }
        else if (stack.hasTagCompound())
        {
            stack.getTagCompound().removeTag("ench");
        }
    }

    /**
     * Returns the biggest level of the enchantment on the array of ItemStack passed.
     *  
     * @param enchID The ID of the enchantment being searched for.
     * @param stacks The array of stacks being searched.
     */
    public static int getMaxEnchantmentLevel(int enchID, ItemStack[] stacks)
    {
        if (stacks == null)
        {
            return 0;
        }
        else
        {
            int j = 0;
            ItemStack[] aitemstack1 = stacks;
            int k = stacks.length;

            for (int l = 0; l < k; ++l)
            {
                ItemStack itemstack = aitemstack1[l];
                int i1 = getEnchantmentLevel(enchID, itemstack);

                if (i1 > j)
                {
                    j = i1;
                }
            }

            return j;
        }
    }

    /**
     * Executes the enchantment modifier on the ItemStack passed.
     *  
     * @param modifier The modifier being applied.
     * @param stack The ItemStack having a modifier applied to.
     */
    private static void applyEnchantmentModifier(EnchantmentHelper.IModifier modifier, ItemStack stack)
    {
        if (stack != null)
        {
            NBTTagList nbttaglist = stack.getEnchantmentTagList();

            if (nbttaglist != null)
            {
                for (int i = 0; i < nbttaglist.tagCount(); ++i)
                {
                    short short1 = nbttaglist.getCompoundTagAt(i).getShort("id");
                    short short2 = nbttaglist.getCompoundTagAt(i).getShort("lvl");

                    if (Enchantment.getEnchantmentById(short1) != null)
                    {
                        modifier.calculateModifier(Enchantment.getEnchantmentById(short1), short2);
                    }
                }
            }
        }
    }

    /**
     * Executes the enchantment modifier on the array of ItemStack passed.
     *  
     * @param modifier The modifier being applied.
     * @param stacks An array of ItemStacks that will have the modifier applied to them.
     */
    private static void applyEnchantmentModifierArray(EnchantmentHelper.IModifier modifier, ItemStack[] stacks)
    {
        ItemStack[] aitemstack1 = stacks;
        int i = stacks.length;

        for (int j = 0; j < i; ++j)
        {
            ItemStack itemstack = aitemstack1[j];
            applyEnchantmentModifier(modifier, itemstack);
        }
    }

    /**
     * Returns the modifier of protection enchantments on armors equipped on player.
     *  
     * @param stacks An array of ItemStacks being checked.
     * @param source The source of the damage.
     */
    public static int getEnchantmentModifierDamage(ItemStack[] stacks, DamageSource source)
    {
        enchantmentModifierDamage.damageModifier = 0;
        enchantmentModifierDamage.source = source;
        applyEnchantmentModifierArray(enchantmentModifierDamage, stacks);

        if (enchantmentModifierDamage.damageModifier > 25)
        {
            enchantmentModifierDamage.damageModifier = 25;
        }

        return (enchantmentModifierDamage.damageModifier + 1 >> 1) + enchantmentRand.nextInt((enchantmentModifierDamage.damageModifier >> 1) + 1);
    }

    public static float func_152377_a(ItemStack p_152377_0_, EnumCreatureAttribute p_152377_1_)
    {
        enchantmentModifierLiving.livingModifier = 0.0F;
        enchantmentModifierLiving.entityLiving = p_152377_1_;
        applyEnchantmentModifier(enchantmentModifierLiving, p_152377_0_);
        return enchantmentModifierLiving.livingModifier;
    }

    public static void func_151384_a(EntityLivingBase p_151384_0_, Entity p_151384_1_)
    {
        field_151388_d.field_151363_b = p_151384_1_;
        field_151388_d.field_151364_a = p_151384_0_;

        if (p_151384_0_ != null)
        {
            applyEnchantmentModifierArray(field_151388_d, p_151384_0_.getInventory());
        }

        if (p_151384_1_ instanceof EntityPlayer)
        {
            applyEnchantmentModifier(field_151388_d, p_151384_0_.getHeldItem());
        }
    }

    public static void func_151385_b(EntityLivingBase p_151385_0_, Entity p_151385_1_)
    {
        field_151389_e.field_151366_a = p_151385_0_;
        field_151389_e.field_151365_b = p_151385_1_;

        if (p_151385_0_ != null)
        {
            applyEnchantmentModifierArray(field_151389_e, p_151385_0_.getInventory());
        }

        if (p_151385_0_ instanceof EntityPlayer)
        {
            applyEnchantmentModifier(field_151389_e, p_151385_0_.getHeldItem());
        }
    }

    /**
     * Returns the Knockback modifier of the enchantment on the players held item.
     *  
     * @param player The player being checked.
     */
    public static int getKnockbackModifier(EntityLivingBase player)
    {
        /**
         * Returns the level of enchantment on the ItemStack passed.
         *  
         * @param enchID The ID for the enchantment you are looking for.
         * @param stack The ItemStack being searched.
         */
        return getEnchantmentLevel(Enchantment.knockback.effectId, player.getHeldItem());
    }

    /**
     * Returns the fire aspect modifier of the players held item.
     *  
     * @param player The player being checked.
     */
    public static int getFireAspectModifier(EntityLivingBase player)
    {
        /**
         * Returns the level of enchantment on the ItemStack passed.
         *  
         * @param enchID The ID for the enchantment you are looking for.
         * @param stack The ItemStack being searched.
         */
        return getEnchantmentLevel(Enchantment.fireAspect.effectId, player.getHeldItem());
    }

    /**
     * Returns the 'Water Breathing' modifier of enchantments on player equipped armors.
     *  
     * @param player The player being checked.
     */
    public static int getRespiration(Entity player)
    {
        /**
         * Returns the biggest level of the enchantment on the array of ItemStack passed.
         *  
         * @param enchID The ID of the enchantment being searched for.
         * @param stacks The array of stacks being searched.
         */
        return getMaxEnchantmentLevel(Enchantment.respiration.effectId, player.getInventory());
    }

    /**
     * Returns the level of the Depth Strider enchantment.
     *  
     * @param player The player being checked.
     */
    public static int getDepthStriderModifier(Entity player)
    {
        /**
         * Returns the biggest level of the enchantment on the array of ItemStack passed.
         *  
         * @param enchID The ID of the enchantment being searched for.
         * @param stacks The array of stacks being searched.
         */
        return getMaxEnchantmentLevel(Enchantment.depthStrider.effectId, player.getInventory());
    }

    /**
     * Return the extra efficiency of tools based on enchantments on equipped player item.
     *  
     * @param player The player being checked.
     */
    public static int getEfficiencyModifier(EntityLivingBase player)
    {
        /**
         * Returns the level of enchantment on the ItemStack passed.
         *  
         * @param enchID The ID for the enchantment you are looking for.
         * @param stack The ItemStack being searched.
         */
        return getEnchantmentLevel(Enchantment.efficiency.effectId, player.getHeldItem());
    }

    /**
     * Returns the silk touch status of enchantments on current equipped item of player.
     *  
     * @param player The player being checked.
     */
    public static boolean getSilkTouchModifier(EntityLivingBase player)
    {
        /**
         * Returns the level of enchantment on the ItemStack passed.
         *  
         * @param enchID The ID for the enchantment you are looking for.
         * @param stack The ItemStack being searched.
         */
        return getEnchantmentLevel(Enchantment.silkTouch.effectId, player.getHeldItem()) > 0;
    }

    /**
     * Returns the fortune enchantment modifier of the current equipped item of player.
     *  
     * @param player The player being checked.
     */
    public static int getFortuneModifier(EntityLivingBase player)
    {
        /**
         * Returns the level of enchantment on the ItemStack passed.
         *  
         * @param enchID The ID for the enchantment you are looking for.
         * @param stack The ItemStack being searched.
         */
        return getEnchantmentLevel(Enchantment.fortune.effectId, player.getHeldItem());
    }

    /**
     * Returns the level of the 'Luck Of The Sea' enchantment.
     *  
     * @param player The player being checked.
     */
    public static int getLuckOfSeaModifier(EntityLivingBase player)
    {
        /**
         * Returns the level of enchantment on the ItemStack passed.
         *  
         * @param enchID The ID for the enchantment you are looking for.
         * @param stack The ItemStack being searched.
         */
        return getEnchantmentLevel(Enchantment.luckOfTheSea.effectId, player.getHeldItem());
    }

    /**
     * Returns the level of the 'Lure' enchantment on the players held item.
     *  
     * @param player The player being checked.
     */
    public static int getLureModifier(EntityLivingBase player)
    {
        /**
         * Returns the level of enchantment on the ItemStack passed.
         *  
         * @param enchID The ID for the enchantment you are looking for.
         * @param stack The ItemStack being searched.
         */
        return getEnchantmentLevel(Enchantment.lure.effectId, player.getHeldItem());
    }

    /**
     * Returns the looting enchantment modifier of the current equipped item of player.
     *  
     * @param player The player being checked.
     */
    public static int getLootingModifier(EntityLivingBase player)
    {
        /**
         * Returns the level of enchantment on the ItemStack passed.
         *  
         * @param enchID The ID for the enchantment you are looking for.
         * @param stack The ItemStack being searched.
         */
        return getEnchantmentLevel(Enchantment.looting.effectId, player.getHeldItem());
    }

    /**
     * Returns the aqua affinity status of enchantments on current equipped item of player.
     *  
     * @param player The player being checked.
     */
    public static boolean getAquaAffinityModifier(EntityLivingBase player)
    {
        /**
         * Returns the biggest level of the enchantment on the array of ItemStack passed.
         *  
         * @param enchID The ID of the enchantment being searched for.
         * @param stacks The array of stacks being searched.
         */
        return getMaxEnchantmentLevel(Enchantment.aquaAffinity.effectId, player.getInventory()) > 0;
    }

    public static ItemStack getEnchantedItem(Enchantment p_92099_0_, EntityLivingBase p_92099_1_)
    {
        ItemStack[] aitemstack = p_92099_1_.getInventory();
        int i = aitemstack.length;

        for (int j = 0; j < i; ++j)
        {
            ItemStack itemstack = aitemstack[j];

            if (itemstack != null && getEnchantmentLevel(p_92099_0_.effectId, itemstack) > 0)
            {
                return itemstack;
            }
        }

        return null;
    }

    /**
     * Returns the enchantability of itemstack, it's uses a singular formula for each index (2nd parameter: 0, 1 and 2),
     * cutting to the max enchantability power of the table (3rd parameter)
     */
    public static int calcItemStackEnchantability(Random p_77514_0_, int p_77514_1_, int p_77514_2_, ItemStack p_77514_3_)
    {
        Item item = p_77514_3_.getItem();
        int k = item.getItemEnchantability(p_77514_3_);

        if (k <= 0)
        {
            return 0;
        }
        else
        {
            if (p_77514_2_ > 15)
            {
                p_77514_2_ = 15;
            }

            int l = p_77514_0_.nextInt(8) + 1 + (p_77514_2_ >> 1) + p_77514_0_.nextInt(p_77514_2_ + 1);
            return p_77514_1_ == 0 ? Math.max(l / 3, 1) : (p_77514_1_ == 1 ? l * 2 / 3 + 1 : Math.max(l, p_77514_2_ * 2));
        }
    }

    /**
     * Adds a random enchantment to the specified item. Args: random, itemStack, enchantabilityLevel
     */
    public static ItemStack addRandomEnchantment(Random p_77504_0_, ItemStack p_77504_1_, int p_77504_2_)
    {
        List list = buildEnchantmentList(p_77504_0_, p_77504_1_, p_77504_2_);
        boolean flag = p_77504_1_.getItem() == Items.book;

        if (flag)
        {
            p_77504_1_.setItem(Items.enchanted_book);
        }

        if (list != null)
        {
            Iterator iterator = list.iterator();

            while (iterator.hasNext())
            {
                EnchantmentData enchantmentdata = (EnchantmentData)iterator.next();

                if (flag)
                {
                    Items.enchanted_book.addEnchantment(p_77504_1_, enchantmentdata);
                }
                else
                {
                    p_77504_1_.addEnchantment(enchantmentdata.enchantmentobj, enchantmentdata.enchantmentLevel);
                }
            }
        }

        return p_77504_1_;
    }

    /**
     * Create a list of random EnchantmentData (enchantments) that can be added together to the ItemStack, the 3rd
     * parameter is the total enchantability level.
     */
    public static List buildEnchantmentList(Random p_77513_0_, ItemStack p_77513_1_, int p_77513_2_)
    {
        Item item = p_77513_1_.getItem();
        int j = item.getItemEnchantability(p_77513_1_);

        if (j <= 0)
        {
            return null;
        }
        else
        {
            j /= 2;
            j = 1 + p_77513_0_.nextInt((j >> 1) + 1) + p_77513_0_.nextInt((j >> 1) + 1);
            int k = j + p_77513_2_;
            float f = (p_77513_0_.nextFloat() + p_77513_0_.nextFloat() - 1.0F) * 0.15F;
            int l = (int)((float)k * (1.0F + f) + 0.5F);

            if (l < 1)
            {
                l = 1;
            }

            ArrayList arraylist = null;
            Map map = mapEnchantmentData(l, p_77513_1_);

            if (map != null && !map.isEmpty())
            {
                EnchantmentData enchantmentdata = (EnchantmentData)WeightedRandom.getRandomItem(p_77513_0_, map.values());

                if (enchantmentdata != null)
                {
                    arraylist = Lists.newArrayList();
                    arraylist.add(enchantmentdata);

                    for (int i1 = l; p_77513_0_.nextInt(50) <= i1; i1 >>= 1)
                    {
                        Iterator iterator = map.keySet().iterator();

                        while (iterator.hasNext())
                        {
                            Integer integer = (Integer)iterator.next();
                            boolean flag = true;
                            Iterator iterator1 = arraylist.iterator();

                            while (true)
                            {
                                if (iterator1.hasNext())
                                {
                                    EnchantmentData enchantmentdata1 = (EnchantmentData)iterator1.next();

                                    Enchantment e1 = enchantmentdata1.enchantmentobj;
                                    Enchantment e2 = Enchantment.getEnchantmentById(integer.intValue());
                                    if (e1.canApplyTogether(e2) && e2.canApplyTogether(e1)) //Forge BugFix: Let Both enchantments veto being together
                                    {
                                        continue;
                                    }

                                    flag = false;
                                }

                                if (!flag)
                                {
                                    iterator.remove();
                                }

                                break;
                            }
                        }

                        if (!map.isEmpty())
                        {
                            EnchantmentData enchantmentdata2 = (EnchantmentData)WeightedRandom.getRandomItem(p_77513_0_, map.values());
                            arraylist.add(enchantmentdata2);
                        }
                    }
                }
            }

            return arraylist;
        }
    }

    /**
     * Creates a 'Map' of EnchantmentData (enchantments) possible to add on the ItemStack and the enchantability level
     * passed.
     */
    public static Map mapEnchantmentData(int p_77505_0_, ItemStack p_77505_1_)
    {
        Item item = p_77505_1_.getItem();
        HashMap hashmap = null;
        boolean flag = p_77505_1_.getItem() == Items.book;
        Enchantment[] aenchantment = Enchantment.enchantmentsBookList;
        int j = aenchantment.length;

        for (int k = 0; k < j; ++k)
        {
            Enchantment enchantment = aenchantment[k];

            if (enchantment == null) continue;
            if (enchantment.canApplyAtEnchantingTable(p_77505_1_) || ((item == Items.book) && enchantment.isAllowedOnBooks()))
            {
                for (int l = enchantment.getMinLevel(); l <= enchantment.getMaxLevel(); ++l)
                {
                    if (p_77505_0_ >= enchantment.getMinEnchantability(l) && p_77505_0_ <= enchantment.getMaxEnchantability(l))
                    {
                        if (hashmap == null)
                        {
                            hashmap = Maps.newHashMap();
                        }

                        hashmap.put(Integer.valueOf(enchantment.effectId), new EnchantmentData(enchantment, l));
                    }
                }
            }
        }

        return hashmap;
    }

    static final class DamageIterator implements EnchantmentHelper.IModifier
        {
            public EntityLivingBase field_151366_a;
            public Entity field_151365_b;
            private static final String __OBFID = "CL_00000109";

            private DamageIterator() {}

            /**
             * Generic method use to calculate modifiers of offensive or defensive enchantment values.
             */
            public void calculateModifier(Enchantment p_77493_1_, int p_77493_2_)
            {
                p_77493_1_.onEntityDamaged(this.field_151366_a, this.field_151365_b, p_77493_2_);
            }

            DamageIterator(Object p_i45359_1_)
            {
                this();
            }
        }

    static final class HurtIterator implements EnchantmentHelper.IModifier
        {
            public EntityLivingBase field_151364_a;
            public Entity field_151363_b;
            private static final String __OBFID = "CL_00000110";

            private HurtIterator() {}

            /**
             * Generic method use to calculate modifiers of offensive or defensive enchantment values.
             */
            public void calculateModifier(Enchantment p_77493_1_, int p_77493_2_)
            {
                p_77493_1_.onUserHurt(this.field_151364_a, this.field_151363_b, p_77493_2_);
            }

            HurtIterator(Object p_i45360_1_)
            {
                this();
            }
        }

    interface IModifier
    {
        /**
         * Generic method use to calculate modifiers of offensive or defensive enchantment values.
         */
        void calculateModifier(Enchantment p_77493_1_, int p_77493_2_);
    }

    static final class ModifierDamage implements EnchantmentHelper.IModifier
        {
            /**
             * Used to calculate the damage modifier (extra armor) on enchantments that the player have on equipped
             * armors.
             */
            public int damageModifier;
            /**
             * Used as parameter to calculate the damage modifier (extra armor) on enchantments that the player have on
             * equipped armors.
             */
            public DamageSource source;
            private static final String __OBFID = "CL_00000114";

            private ModifierDamage() {}

            /**
             * Generic method use to calculate modifiers of offensive or defensive enchantment values.
             */
            public void calculateModifier(Enchantment p_77493_1_, int p_77493_2_)
            {
                this.damageModifier += p_77493_1_.calcModifierDamage(p_77493_2_, this.source);
            }

            ModifierDamage(Object p_i1929_1_)
            {
                this();
            }
        }

    static final class ModifierLiving implements EnchantmentHelper.IModifier
        {
            /** Used to calculate the (magic) extra damage based on enchantments of current equipped player item. */
            public float livingModifier;
            public EnumCreatureAttribute entityLiving;
            private static final String __OBFID = "CL_00000112";

            private ModifierLiving() {}

            /**
             * Generic method use to calculate modifiers of offensive or defensive enchantment values.
             */
            public void calculateModifier(Enchantment p_77493_1_, int p_77493_2_)
            {
                this.livingModifier += p_77493_1_.calcDamageByCreature(p_77493_2_, this.entityLiving);
            }

            ModifierLiving(Object p_i1928_1_)
            {
                this();
            }
        }
}