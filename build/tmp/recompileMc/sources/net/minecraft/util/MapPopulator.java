package net.minecraft.util;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

public class MapPopulator
{
    private static final String __OBFID = "CL_00002318";

    /**
     * Create a Map from the given keys and values. This method creates a LinkedHashMap.
     *  
     * @param keys the keys for the map, in order
     * @param values the values for the map, in order
     */
    public static Map createMap(Iterable keys, Iterable values)
    {
        /**
         * Populate the given Map with the given keys and values.
         */
        return populateMap(keys, values, Maps.newLinkedHashMap());
    }

    /**
     * Populate the given Map with the given keys and values.
     */
    public static Map populateMap(Iterable keys, Iterable values, Map map)
    {
        Iterator iterator = values.iterator();
        Iterator iterator1 = keys.iterator();

        while (iterator1.hasNext())
        {
            Object object = iterator1.next();
            map.put(object, iterator.next());
        }

        if (iterator.hasNext())
        {
            throw new NoSuchElementException();
        }
        else
        {
            return map;
        }
    }
}