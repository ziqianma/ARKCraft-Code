package net.minecraft.stats;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Achievement extends StatBase
{
    /** Is the column (related to center of achievement gui, in 24 pixels unit) that the achievement will be displayed. */
    public final int displayColumn;
    /** Is the row (related to center of achievement gui, in 24 pixels unit) that the achievement will be displayed. */
    public final int displayRow;
    /** Holds the parent achievement, that must be taken before this achievement is avaiable. */
    public final Achievement parentAchievement;
    /** Holds the description of the achievement, ready to be formatted and/or displayed. */
    private final String achievementDescription;
    /**
     * Holds a string formatter for the achievement, some of then needs extra dynamic info - like the key used to open
     * the inventory.
     */
    @SideOnly(Side.CLIENT)
    private IStatStringFormat statStringFormatter;
    /** Holds the ItemStack that will be used to draw the achievement into the GUI. */
    public final ItemStack theItemStack;
    /**
     * Special achievements have a 'spiked' (on normal texture pack) frame, special achievements are the hardest ones to
     * achieve.
     */
    private boolean isSpecial;
    private static final String __OBFID = "CL_00001466";

    public Achievement(String p_i46327_1_, String p_i46327_2_, int column, int row, Item p_i46327_5_, Achievement p_i46327_6_)
    {
        this(p_i46327_1_, p_i46327_2_, column, row, new ItemStack(p_i46327_5_), p_i46327_6_);
    }

    public Achievement(String p_i45301_1_, String p_i45301_2_, int column, int row, Block p_i45301_5_, Achievement p_i45301_6_)
    {
        this(p_i45301_1_, p_i45301_2_, column, row, new ItemStack(p_i45301_5_), p_i45301_6_);
    }

    public Achievement(String p_i45302_1_, String p_i45302_2_, int column, int row, ItemStack p_i45302_5_, Achievement parent)
    {
        super(p_i45302_1_, new ChatComponentTranslation("achievement." + p_i45302_2_, new Object[0]));
        this.theItemStack = p_i45302_5_;
        this.achievementDescription = "achievement." + p_i45302_2_ + ".desc";
        this.displayColumn = column;
        this.displayRow = row;

        if (column < AchievementList.minDisplayColumn)
        {
            AchievementList.minDisplayColumn = column;
        }

        if (row < AchievementList.minDisplayRow)
        {
            AchievementList.minDisplayRow = row;
        }

        if (column > AchievementList.maxDisplayColumn)
        {
            AchievementList.maxDisplayColumn = column;
        }

        if (row > AchievementList.maxDisplayRow)
        {
            AchievementList.maxDisplayRow = row;
        }

        this.parentAchievement = parent;
    }

    public Achievement setIndependent()
    {
        this.isIndependent = true;
        return this;
    }

    /**
     * Special achievements have a 'spiked' (on normal texture pack) frame, special achievements are the hardest ones to
     * achieve.
     */
    public Achievement setSpecial()
    {
        this.isSpecial = true;
        return this;
    }

    public Achievement func_180788_c()
    {
        super.registerStat();
        AchievementList.achievementList.add(this);
        return this;
    }

    /**
     * Returns whether or not the StatBase-derived class is a statistic (running counter) or an achievement (one-shot).
     */
    public boolean isAchievement()
    {
        return true;
    }

    public IChatComponent getStatName()
    {
        IChatComponent ichatcomponent = super.getStatName();
        ichatcomponent.getChatStyle().setColor(this.getSpecial() ? EnumChatFormatting.DARK_PURPLE : EnumChatFormatting.GREEN);
        return ichatcomponent;
    }

    public Achievement func_180787_a(Class p_180787_1_)
    {
        return (Achievement)super.func_150953_b(p_180787_1_);
    }

    /**
     * Returns the fully description of the achievement - ready to be displayed on screen.
     */
    @SideOnly(Side.CLIENT)
    public String getDescription()
    {
        return this.statStringFormatter != null ? this.statStringFormatter.formatString(StatCollector.translateToLocal(this.achievementDescription)) : StatCollector.translateToLocal(this.achievementDescription);
    }

    /**
     * Defines a string formatter for the achievement.
     */
    @SideOnly(Side.CLIENT)
    public Achievement setStatStringFormatter(IStatStringFormat p_75988_1_)
    {
        this.statStringFormatter = p_75988_1_;
        return this;
    }

    /**
     * Special achievements have a 'spiked' (on normal texture pack) frame, special achievements are the hardest ones to
     * achieve.
     */
    public boolean getSpecial()
    {
        return this.isSpecial;
    }

    public StatBase func_150953_b(Class p_150953_1_)
    {
        return this.func_180787_a(p_150953_1_);
    }

    /**
     * Register the stat into StatList.
     */
    public StatBase registerStat()
    {
        return this.func_180788_c();
    }

    /**
     * Initializes the current stat as independent (i.e., lacking prerequisites for being updated) and returns the
     * current instance.
     */
    public StatBase initIndependentStat()
    {
        return this.setIndependent();
    }
}