package net.minecraft.block.properties;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.HashSet;

public class PropertyInteger extends PropertyHelper
{
    private final ImmutableSet allowedValues;
    private static final String __OBFID = "CL_00002014";

    protected PropertyInteger(String name, int min, int max)
    {
        super(name, Integer.class);

        if (min < 0)
        {
            throw new IllegalArgumentException("Min value of " + name + " must be 0 or greater");
        }
        else if (max <= min)
        {
            throw new IllegalArgumentException("Max value of " + name + " must be greater than min (" + min + ")");
        }
        else
        {
            HashSet hashset = Sets.newHashSet();

            for (int k = min; k <= max; ++k)
            {
                hashset.add(Integer.valueOf(k));
            }

            this.allowedValues = ImmutableSet.copyOf(hashset);
        }
    }

    public Collection getAllowedValues()
    {
        return this.allowedValues;
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass())
        {
            if (!super.equals(p_equals_1_))
            {
                return false;
            }
            else
            {
                PropertyInteger propertyinteger = (PropertyInteger)p_equals_1_;
                return this.allowedValues.equals(propertyinteger.allowedValues);
            }
        }
        else
        {
            return false;
        }
    }

    public int hashCode()
    {
        int i = super.hashCode();
        i = 31 * i + this.allowedValues.hashCode();
        return i;
    }

    public static PropertyInteger create(String name, int min, int max)
    {
        return new PropertyInteger(name, min, max);
    }

    public String getName0(Integer value)
    {
        return value.toString();
    }

    /**
     * Get the name for the given value.
     */
    public String getName(Comparable value)
    {
        return this.getName0((Integer)value);
    }
}