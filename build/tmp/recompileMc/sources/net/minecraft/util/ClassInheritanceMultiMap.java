package net.minecraft.util;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Iterators;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.ClassUtils.Interfaces;

public class ClassInheritanceMultiMap extends AbstractSet
{
    private final Multimap field_180218_a = HashMultimap.create();
    private final Set field_180216_b = Sets.newIdentityHashSet();
    private final Class field_180217_c;
    private static final String __OBFID = "CL_00002266";

    public ClassInheritanceMultiMap(Class p_i45909_1_)
    {
        this.field_180217_c = p_i45909_1_;
        this.field_180216_b.add(p_i45909_1_);
    }

    public void func_180213_a(Class p_180213_1_)
    {
        Iterator iterator = this.field_180218_a.get(this.func_180212_a(p_180213_1_, false)).iterator();

        while (iterator.hasNext())
        {
            Object object = iterator.next();

            if (p_180213_1_.isAssignableFrom(object.getClass()))
            {
                this.field_180218_a.put(p_180213_1_, object);
            }
        }

        this.field_180216_b.add(p_180213_1_);
    }

    protected Class func_180212_a(Class p_180212_1_, boolean p_180212_2_)
    {
        Iterator iterator = ClassUtils.hierarchy(p_180212_1_, Interfaces.INCLUDE).iterator();
        Class oclass1;

        do
        {
            if (!iterator.hasNext())
            {
                throw new IllegalArgumentException("Don\'t know how to search for " + p_180212_1_);
            }

            oclass1 = (Class)iterator.next();
        }
        while (!this.field_180216_b.contains(oclass1));

        if (oclass1 == this.field_180217_c && p_180212_2_)
        {
            this.func_180213_a(p_180212_1_);
        }

        return oclass1;
    }

    public boolean add(Object p_add_1_)
    {
        Iterator iterator = this.field_180216_b.iterator();

        while (iterator.hasNext())
        {
            Class oclass = (Class)iterator.next();

            if (oclass.isAssignableFrom(p_add_1_.getClass()))
            {
                this.field_180218_a.put(oclass, p_add_1_);
            }
        }

        return true;
    }

    public boolean remove(Object p_remove_1_)
    {
        Object object1 = p_remove_1_;
        boolean flag = false;
        Iterator iterator = this.field_180216_b.iterator();

        while (iterator.hasNext())
        {
            Class oclass = (Class)iterator.next();

            if (oclass.isAssignableFrom(object1.getClass()))
            {
                flag |= this.field_180218_a.remove(oclass, object1);
            }
        }

        return flag;
    }

    public Iterable func_180215_b(final Class p_180215_1_)
    {
        return new Iterable()
        {
            private static final String __OBFID = "CL_00002265";
            public Iterator iterator()
            {
                Iterator iterator = ClassInheritanceMultiMap.this.field_180218_a.get(ClassInheritanceMultiMap.this.func_180212_a(p_180215_1_, true)).iterator();
                return Iterators.filter(iterator, p_180215_1_);
            }
        };
    }

    public Iterator iterator()
    {
        final Iterator iterator = this.field_180218_a.get(this.field_180217_c).iterator();
        return new AbstractIterator()
        {
            private static final String __OBFID = "CL_00002264";
            protected Object computeNext()
            {
                return !iterator.hasNext() ? this.endOfData() : iterator.next();
            }
        };
    }

    public int size()
    {
        return this.field_180218_a.get(this.field_180217_c).size();
    }
}