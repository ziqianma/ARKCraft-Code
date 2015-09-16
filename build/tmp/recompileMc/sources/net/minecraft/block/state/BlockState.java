package net.minecraft.block.state;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.Cartesian;
import net.minecraft.util.MapPopulator;

public class BlockState
{
    private static final Joiner COMMA_JOINER = Joiner.on(", ");
    /** Function that converts a Property into it's name. */
    private static final Function GET_NAME_FUNC = new Function()
    {
        private static final String __OBFID = "CL_00002029";
        public String apply(IProperty property)
        {
            return property == null ? "<NULL>" : property.getName();
        }
        public Object apply(Object p_apply_1_)
        {
            return this.apply((IProperty)p_apply_1_);
        }
    };
    private final Block block;
    private final ImmutableList properties;
    private final ImmutableList validStates;
    private static final String __OBFID = "CL_00002030";

    public BlockState(Block blockIn, IProperty ... properties)
    {
        this(blockIn, properties, null);
    }

    protected StateImplementation createState(Block block, ImmutableMap properties, ImmutableMap unlistedProperties)
    {
        return new StateImplementation(block, properties);
    }

    protected BlockState(Block blockIn, IProperty[] properties, ImmutableMap unlistedProperties)
    {
        this.block = blockIn;
        Arrays.sort(properties, new Comparator()
        {
            private static final String __OBFID = "CL_00002028";
            public int compare(IProperty left, IProperty right)
            {
                return left.getName().compareTo(right.getName());
            }
            public int compare(Object p_compare_1_, Object p_compare_2_)
            {
                return this.compare((IProperty)p_compare_1_, (IProperty)p_compare_2_);
            }
        });
        this.properties = ImmutableList.copyOf(properties);
        LinkedHashMap linkedhashmap = Maps.newLinkedHashMap();
        ArrayList arraylist = Lists.newArrayList();
        Iterable iterable = Cartesian.cartesianProduct(this.getAllowedValues());
        Iterator iterator = iterable.iterator();

        while (iterator.hasNext())
        {
            List list = (List)iterator.next();
            Map map = MapPopulator.createMap(this.properties, list);
            BlockState.StateImplementation stateimplementation = createState(blockIn, ImmutableMap.copyOf(map), unlistedProperties);
            linkedhashmap.put(map, stateimplementation);
            arraylist.add(stateimplementation);
        }

        iterator = arraylist.iterator();

        while (iterator.hasNext())
        {
            BlockState.StateImplementation stateimplementation1 = (BlockState.StateImplementation)iterator.next();
            stateimplementation1.buildPropertyValueTable(linkedhashmap);
        }

        this.validStates = ImmutableList.copyOf(arraylist);
    }

    public ImmutableList getValidStates()
    {
        return this.validStates;
    }

    private List getAllowedValues()
    {
        ArrayList arraylist = Lists.newArrayList();

        for (int i = 0; i < this.properties.size(); ++i)
        {
            arraylist.add(((IProperty)this.properties.get(i)).getAllowedValues());
        }

        return arraylist;
    }

    public IBlockState getBaseState()
    {
        return (IBlockState)this.validStates.get(0);
    }

    public Block getBlock()
    {
        return this.block;
    }

    public Collection getProperties()
    {
        return this.properties;
    }

    public String toString()
    {
        return Objects.toStringHelper(this).add("block", Block.blockRegistry.getNameForObject(this.block)).add("properties", Iterables.transform(this.properties, GET_NAME_FUNC)).toString();
    }

    public static class StateImplementation extends BlockStateBase
        {
            private final Block block;
            /** Maps properties to their respective current value */
            private final ImmutableMap properties;
            /** Lookup-table for IBlockState instances. This is a Table<Property, Value, State>. */
            protected ImmutableTable propertyValueTable;
            private static final String __OBFID = "CL_00002027";

            protected StateImplementation(Block blockIn, ImmutableMap propertiesIn)
            {
                this.block = blockIn;
                this.properties = propertiesIn;
            }
            
            protected StateImplementation(Block blockIn, ImmutableMap propertiesIn, ImmutableTable propertyValueTable)
            {
                this.block = blockIn;
                this.properties = propertiesIn;
                this.propertyValueTable = propertyValueTable;
            }

            /**
             * Get the names of all properties defined for this BlockState
             */
            public Collection getPropertyNames()
            {
                return Collections.unmodifiableCollection(this.properties.keySet());
            }

            /**
             * Get the value of the given Property for this BlockState
             */
            public Comparable getValue(IProperty property)
            {
                if (!this.properties.containsKey(property))
                {
                    throw new IllegalArgumentException("Cannot get property " + property + " as it does not exist in " + this.block.getBlockState());
                }
                else
                {
                    return (Comparable)property.getValueClass().cast(this.properties.get(property));
                }
            }

            /**
             * Get a version of this BlockState with the given Property now set to the given value
             */
            public IBlockState withProperty(IProperty property, Comparable value)
            {
                if (!this.properties.containsKey(property))
                {
                    throw new IllegalArgumentException("Cannot set property " + property + " as it does not exist in " + this.block.getBlockState());
                }
                else if (!property.getAllowedValues().contains(value))
                {
                    throw new IllegalArgumentException("Cannot set property " + property + " to " + value + " on block " + Block.blockRegistry.getNameForObject(this.block) + ", it is not an allowed value");
                }
                else
                {
                    return (IBlockState)(this.properties.get(property) == value ? this : (IBlockState)this.propertyValueTable.get(property, value));
                }
            }

            /**
             * Get all properties of this BlockState. The returned Map maps from properties (IProperty) to the
             * respective current value.
             */
            public ImmutableMap getProperties()
            {
                return this.properties;
            }

            public Block getBlock()
            {
                return this.block;
            }

            public boolean equals(Object p_equals_1_)
            {
                return this == p_equals_1_;
            }

            public int hashCode()
            {
                return this.properties.hashCode();
            }

            public void buildPropertyValueTable(Map map)
            {
                if (this.propertyValueTable != null)
                {
                    throw new IllegalStateException();
                }
                else
                {
                    HashBasedTable hashbasedtable = HashBasedTable.create();
                    Iterator iterator = this.properties.keySet().iterator();

                    while (iterator.hasNext())
                    {
                        IProperty iproperty = (IProperty)iterator.next();
                        Iterator iterator1 = iproperty.getAllowedValues().iterator();

                        while (iterator1.hasNext())
                        {
                            Comparable comparable = (Comparable)iterator1.next();

                            if (comparable != this.properties.get(iproperty))
                            {
                                hashbasedtable.put(iproperty, comparable, map.get(this.setPropertyValue(iproperty, comparable)));
                            }
                        }
                    }

                    this.propertyValueTable = ImmutableTable.copyOf(hashbasedtable);
                }
            }

            /**
             * Create a new version of this.properties with the given property now set to the given value
             */
            private Map setPropertyValue(IProperty property, Comparable value)
            {
                HashMap hashmap = Maps.newHashMap(this.properties);
                hashmap.put(property, value);
                return hashmap;
            }

            StateImplementation(Block p_i45661_1_, ImmutableMap p_i45661_2_, Object p_i45661_3_)
            {
                this(p_i45661_1_, p_i45661_2_);
            }

            public ImmutableTable<IProperty, Comparable, IBlockState> getPropertyValueTable()
            {
                /** Lookup-table for IBlockState instances. This is a Table<Property, Value, State>. */
                return propertyValueTable;
            }
        }
}