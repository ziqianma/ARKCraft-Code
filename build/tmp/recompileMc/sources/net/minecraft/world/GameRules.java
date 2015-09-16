package net.minecraft.world;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import net.minecraft.nbt.NBTTagCompound;

public class GameRules
{
    private TreeMap theGameRules = new TreeMap();
    private static final String __OBFID = "CL_00000136";

    public GameRules()
    {
        this.addGameRule("doFireTick", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.addGameRule("mobGriefing", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.addGameRule("keepInventory", "false", GameRules.ValueType.BOOLEAN_VALUE);
        this.addGameRule("doMobSpawning", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.addGameRule("doMobLoot", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.addGameRule("doTileDrops", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.addGameRule("commandBlockOutput", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.addGameRule("naturalRegeneration", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.addGameRule("doDaylightCycle", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.addGameRule("logAdminCommands", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.addGameRule("showDeathMessages", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.addGameRule("randomTickSpeed", "3", GameRules.ValueType.NUMERICAL_VALUE);
        this.addGameRule("sendCommandFeedback", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.addGameRule("reducedDebugInfo", "false", GameRules.ValueType.BOOLEAN_VALUE);
    }

    public void addGameRule(String key, String value, GameRules.ValueType type)
    {
        this.theGameRules.put(key, new GameRules.Value(value, type));
    }

    public void setOrCreateGameRule(String key, String ruleValue)
    {
        GameRules.Value value = (GameRules.Value)this.theGameRules.get(key);

        if (value != null)
        {
            value.setValue(ruleValue);
        }
        else
        {
            this.addGameRule(key, ruleValue, GameRules.ValueType.ANY_VALUE);
        }
    }

    /**
     * Gets the string Game Rule value.
     */
    public String getGameRuleStringValue(String name)
    {
        GameRules.Value value = (GameRules.Value)this.theGameRules.get(name);
        return value != null ? value.getGameRuleStringValue() : "";
    }

    /**
     * Gets the boolean Game Rule value.
     */
    public boolean getGameRuleBooleanValue(String name)
    {
        GameRules.Value value = (GameRules.Value)this.theGameRules.get(name);
        return value != null ? value.getGameRuleBooleanValue() : false;
    }

    public int getInt(String name)
    {
        GameRules.Value value = (GameRules.Value)this.theGameRules.get(name);
        return value != null ? value.getInt() : 0;
    }

    /**
     * Return the defined game rules as NBT.
     */
    public NBTTagCompound writeGameRulesToNBT()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        Iterator iterator = this.theGameRules.keySet().iterator();

        while (iterator.hasNext())
        {
            String s = (String)iterator.next();
            GameRules.Value value = (GameRules.Value)this.theGameRules.get(s);
            nbttagcompound.setString(s, value.getGameRuleStringValue());
        }

        return nbttagcompound;
    }

    /**
     * Set defined game rules from NBT.
     */
    public void readGameRulesFromNBT(NBTTagCompound nbt)
    {
        Set set = nbt.getKeySet();
        Iterator iterator = set.iterator();

        while (iterator.hasNext())
        {
            String s = (String)iterator.next();
            String s1 = nbt.getString(s);
            this.setOrCreateGameRule(s, s1);
        }
    }

    /**
     * Return the defined game rules.
     */
    public String[] getRules()
    {
        return (String[])this.theGameRules.keySet().toArray(new String[0]);
    }

    /**
     * Return whether the specified game rule is defined.
     */
    public boolean hasRule(String name)
    {
        return this.theGameRules.containsKey(name);
    }

    public boolean areSameType(String key, GameRules.ValueType otherValue)
    {
        GameRules.Value value = (GameRules.Value)this.theGameRules.get(key);
        return value != null && (value.getType() == otherValue || otherValue == GameRules.ValueType.ANY_VALUE);
    }

    static class Value
        {
            private String valueString;
            private boolean valueBoolean;
            private int valueInteger;
            private double valueDouble;
            private final GameRules.ValueType type;
            private static final String __OBFID = "CL_00000137";

            public Value(String value, GameRules.ValueType type)
            {
                this.type = type;
                this.setValue(value);
            }

            /**
             * Set this game rule value.
             */
            public void setValue(String value)
            {
                this.valueString = value;
                this.valueBoolean = Boolean.parseBoolean(value);
                this.valueInteger = this.valueBoolean ? 1 : 0;

                try
                {
                    this.valueInteger = Integer.parseInt(value);
                }
                catch (NumberFormatException numberformatexception1)
                {
                    ;
                }

                try
                {
                    this.valueDouble = Double.parseDouble(value);
                }
                catch (NumberFormatException numberformatexception)
                {
                    ;
                }
            }

            /**
             * Gets the GameRule's value as String.
             */
            public String getGameRuleStringValue()
            {
                return this.valueString;
            }

            /**
             * Gets the GameRule's value as boolean.
             */
            public boolean getGameRuleBooleanValue()
            {
                return this.valueBoolean;
            }

            public int getInt()
            {
                return this.valueInteger;
            }

            public GameRules.ValueType getType()
            {
                return this.type;
            }
        }

    public static enum ValueType
    {
        ANY_VALUE,
        BOOLEAN_VALUE,
        NUMERICAL_VALUE;

        private static final String __OBFID = "CL_00002151";
    }
}