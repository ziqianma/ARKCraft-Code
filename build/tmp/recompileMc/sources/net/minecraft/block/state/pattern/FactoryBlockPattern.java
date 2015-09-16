package net.minecraft.block.state.pattern;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class FactoryBlockPattern
{
    private static final Joiner COMMA_JOIN = Joiner.on(",");
    private final List depth = Lists.newArrayList();
    private final Map symbolMap = Maps.newHashMap();
    private int aisleHeight;
    private int rowWidth;
    private static final String __OBFID = "CL_00002021";

    private FactoryBlockPattern()
    {
        this.symbolMap.put(' ', Predicates.alwaysTrue());
    }

    public FactoryBlockPattern aisle(String ... aisle)
    {
        if (!ArrayUtils.isEmpty(aisle) && !StringUtils.isEmpty(aisle[0]))
        {
            if (this.depth.isEmpty())
            {
                this.aisleHeight = aisle.length;
                this.rowWidth = aisle[0].length();
            }

            if (aisle.length != this.aisleHeight)
            {
                throw new IllegalArgumentException("Expected aisle with height of " + this.aisleHeight + ", but was given one with a height of " + aisle.length + ")");
            }
            else
            {
                String[] astring = aisle;
                int i = aisle.length;

                for (int j = 0; j < i; ++j)
                {
                    String s1 = astring[j];

                    if (s1.length() != this.rowWidth)
                    {
                        throw new IllegalArgumentException("Not all rows in the given aisle are the correct width (expected " + this.rowWidth + ", found one with " + s1.length() + ")");
                    }

                    char[] achar = s1.toCharArray();
                    int k = achar.length;

                    for (int l = 0; l < k; ++l)
                    {
                        char c0 = achar[l];

                        if (!this.symbolMap.containsKey(Character.valueOf(c0)))
                        {
                            this.symbolMap.put(Character.valueOf(c0), (Object)null);
                        }
                    }
                }

                this.depth.add(aisle);
                return this;
            }
        }
        else
        {
            throw new IllegalArgumentException("Empty pattern for aisle");
        }
    }

    public static FactoryBlockPattern start()
    {
        return new FactoryBlockPattern();
    }

    public FactoryBlockPattern where(char symbol, Predicate blockMatcher)
    {
        this.symbolMap.put(Character.valueOf(symbol), blockMatcher);
        return this;
    }

    public BlockPattern build()
    {
        return new BlockPattern(this.makePredicateArray());
    }

    private Predicate[][][] makePredicateArray()
    {
        this.checkMissingPredicates();
        Predicate[][][] apredicate = (Predicate[][][])((Predicate[][][])Array.newInstance(Predicate.class, new int[] {this.depth.size(), this.aisleHeight, this.rowWidth}));

        for (int i = 0; i < this.depth.size(); ++i)
        {
            for (int j = 0; j < this.aisleHeight; ++j)
            {
                for (int k = 0; k < this.rowWidth; ++k)
                {
                    apredicate[i][j][k] = (Predicate)this.symbolMap.get(Character.valueOf(((String[])this.depth.get(i))[j].charAt(k)));
                }
            }
        }

        return apredicate;
    }

    private void checkMissingPredicates()
    {
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = this.symbolMap.entrySet().iterator();

        while (iterator.hasNext())
        {
            Entry entry = (Entry)iterator.next();

            if (entry.getValue() == null)
            {
                arraylist.add(entry.getKey());
            }
        }

        if (!arraylist.isEmpty())
        {
            throw new IllegalStateException("Predicates for character(s) " + COMMA_JOIN.join(arraylist) + " are missing");
        }
    }
}