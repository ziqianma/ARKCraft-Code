package net.minecraft.command;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;

public class CommandResultStats
{
    /** The number of result command result types that are possible. */
    private static final int NUM_RESULT_TYPES = CommandResultStats.Type.values().length;
    private static final String[] field_179674_b = new String[NUM_RESULT_TYPES];
    private String[] field_179675_c;
    private String[] field_179673_d;
    private static final String __OBFID = "CL_00002364";

    public CommandResultStats()
    {
        this.field_179675_c = field_179674_b;
        this.field_179673_d = field_179674_b;
    }

    public void func_179672_a(ICommandSender sender, CommandResultStats.Type p_179672_2_, int p_179672_3_)
    {
        String s = this.field_179675_c[p_179672_2_.getTypeID()];

        if (s != null)
        {
            String s1;

            try
            {
                s1 = CommandBase.getEntityName(sender, s);
            }
            catch (EntityNotFoundException entitynotfoundexception)
            {
                return;
            }

            String s2 = this.field_179673_d[p_179672_2_.getTypeID()];

            if (s2 != null)
            {
                Scoreboard scoreboard = sender.getEntityWorld().getScoreboard();
                ScoreObjective scoreobjective = scoreboard.getObjective(s2);

                if (scoreobjective != null)
                {
                    if (scoreboard.entityHasObjective(s1, scoreobjective))
                    {
                        Score score = scoreboard.getValueFromObjective(s1, scoreobjective);
                        score.setScorePoints(p_179672_3_);
                    }
                }
            }
        }
    }

    public void func_179668_a(NBTTagCompound p_179668_1_)
    {
        if (p_179668_1_.hasKey("CommandStats", 10))
        {
            NBTTagCompound nbttagcompound1 = p_179668_1_.getCompoundTag("CommandStats");
            CommandResultStats.Type[] atype = CommandResultStats.Type.values();
            int i = atype.length;

            for (int j = 0; j < i; ++j)
            {
                CommandResultStats.Type type = atype[j];
                String s = type.getTypeName() + "Name";
                String s1 = type.getTypeName() + "Objective";

                if (nbttagcompound1.hasKey(s, 8) && nbttagcompound1.hasKey(s1, 8))
                {
                    String s2 = nbttagcompound1.getString(s);
                    String s3 = nbttagcompound1.getString(s1);
                    func_179667_a(this, type, s2, s3);
                }
            }
        }
    }

    public void func_179670_b(NBTTagCompound p_179670_1_)
    {
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();
        CommandResultStats.Type[] atype = CommandResultStats.Type.values();
        int i = atype.length;

        for (int j = 0; j < i; ++j)
        {
            CommandResultStats.Type type = atype[j];
            String s = this.field_179675_c[type.getTypeID()];
            String s1 = this.field_179673_d[type.getTypeID()];

            if (s != null && s1 != null)
            {
                nbttagcompound1.setString(type.getTypeName() + "Name", s);
                nbttagcompound1.setString(type.getTypeName() + "Objective", s1);
            }
        }

        if (!nbttagcompound1.hasNoTags())
        {
            p_179670_1_.setTag("CommandStats", nbttagcompound1);
        }
    }

    public static void func_179667_a(CommandResultStats stats, CommandResultStats.Type resultType, String p_179667_2_, String p_179667_3_)
    {
        if (p_179667_2_ != null && p_179667_2_.length() != 0 && p_179667_3_ != null && p_179667_3_.length() != 0)
        {
            if (stats.field_179675_c == field_179674_b || stats.field_179673_d == field_179674_b)
            {
                stats.field_179675_c = new String[NUM_RESULT_TYPES];
                stats.field_179673_d = new String[NUM_RESULT_TYPES];
            }

            stats.field_179675_c[resultType.getTypeID()] = p_179667_2_;
            stats.field_179673_d[resultType.getTypeID()] = p_179667_3_;
        }
        else
        {
            func_179669_a(stats, resultType);
        }
    }

    private static void func_179669_a(CommandResultStats p_179669_0_, CommandResultStats.Type p_179669_1_)
    {
        if (p_179669_0_.field_179675_c != field_179674_b && p_179669_0_.field_179673_d != field_179674_b)
        {
            p_179669_0_.field_179675_c[p_179669_1_.getTypeID()] = null;
            p_179669_0_.field_179673_d[p_179669_1_.getTypeID()] = null;
            boolean flag = true;
            CommandResultStats.Type[] atype = CommandResultStats.Type.values();
            int i = atype.length;

            for (int j = 0; j < i; ++j)
            {
                CommandResultStats.Type type = atype[j];

                if (p_179669_0_.field_179675_c[type.getTypeID()] != null && p_179669_0_.field_179673_d[type.getTypeID()] != null)
                {
                    flag = false;
                    break;
                }
            }

            if (flag)
            {
                p_179669_0_.field_179675_c = field_179674_b;
                p_179669_0_.field_179673_d = field_179674_b;
            }
        }
    }

    public void func_179671_a(CommandResultStats p_179671_1_)
    {
        CommandResultStats.Type[] atype = CommandResultStats.Type.values();
        int i = atype.length;

        for (int j = 0; j < i; ++j)
        {
            CommandResultStats.Type type = atype[j];
            func_179667_a(this, type, p_179671_1_.field_179675_c[type.getTypeID()], p_179671_1_.field_179673_d[type.getTypeID()]);
        }
    }

    public static enum Type
    {
        SUCCESS_COUNT(0, "SuccessCount"),
        AFFECTED_BLOCKS(1, "AffectedBlocks"),
        AFFECTED_ENTITIES(2, "AffectedEntities"),
        AFFECTED_ITEMS(3, "AffectedItems"),
        QUERY_RESULT(4, "QueryResult");
        /** The integer ID of the Result Type. */
        final int typeID;
        /** The string representation of the type. */
        final String typeName;

        private static final String __OBFID = "CL_00002363";

        private Type(int id, String name)
        {
            this.typeID = id;
            this.typeName = name;
        }

        /**
         * Retrieves the integer ID of the result type.
         */
        public int getTypeID()
        {
            return this.typeID;
        }

        /**
         * Retrieves the name of the type.
         */
        public String getTypeName()
        {
            return this.typeName;
        }

        /**
         * Returns the names of all possible Result Types.
         */
        public static String[] getTypeNames()
        {
            String[] astring = new String[values().length];
            int i = 0;
            CommandResultStats.Type[] atype = values();
            int j = atype.length;

            for (int k = 0; k < j; ++k)
            {
                CommandResultStats.Type type = atype[k];
                astring[i++] = type.getTypeName();
            }

            return astring;
        }

        /**
         * Retrieves the Type indicated by the supplied name string.
         *  
         * @param name The name of the type that is attempting to be retrieved.
         */
        public static CommandResultStats.Type getTypeByName(String name)
        {
            CommandResultStats.Type[] atype = values();
            int i = atype.length;

            for (int j = 0; j < i; ++j)
            {
                CommandResultStats.Type type = atype[j];

                if (type.getTypeName().equals(name))
                {
                    return type;
                }
            }

            return null;
        }
    }
}