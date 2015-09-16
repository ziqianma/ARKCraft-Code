package net.minecraft.entity.ai.attributes;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.minecraft.server.management.LowerStringMap;

public class ServersideAttributeMap extends BaseAttributeMap
{
    private final Set attributeInstanceSet = Sets.newHashSet();
    protected final Map descriptionToAttributeInstanceMap = new LowerStringMap();
    private static final String __OBFID = "CL_00001569";

    public ModifiableAttributeInstance func_180795_e(IAttribute p_180795_1_)
    {
        return (ModifiableAttributeInstance)super.getAttributeInstance(p_180795_1_);
    }

    public ModifiableAttributeInstance func_180796_b(String p_180796_1_)
    {
        IAttributeInstance iattributeinstance = super.getAttributeInstanceByName(p_180796_1_);

        if (iattributeinstance == null)
        {
            iattributeinstance = (IAttributeInstance)this.descriptionToAttributeInstanceMap.get(p_180796_1_);
        }

        return (ModifiableAttributeInstance)iattributeinstance;
    }

    /**
     * Registers an attribute with this AttributeMap, returns a modifiable AttributeInstance associated with this map
     */
    public IAttributeInstance registerAttribute(IAttribute p_111150_1_)
    {
        IAttributeInstance iattributeinstance = super.registerAttribute(p_111150_1_);

        if (p_111150_1_ instanceof RangedAttribute && ((RangedAttribute)p_111150_1_).getDescription() != null)
        {
            this.descriptionToAttributeInstanceMap.put(((RangedAttribute)p_111150_1_).getDescription(), iattributeinstance);
        }

        return iattributeinstance;
    }

    protected IAttributeInstance func_180376_c(IAttribute p_180376_1_)
    {
        return new ModifiableAttributeInstance(this, p_180376_1_);
    }

    public void func_180794_a(IAttributeInstance p_180794_1_)
    {
        if (p_180794_1_.getAttribute().getShouldWatch())
        {
            this.attributeInstanceSet.add(p_180794_1_);
        }

        Iterator iterator = this.field_180377_c.get(p_180794_1_.getAttribute()).iterator();

        while (iterator.hasNext())
        {
            IAttribute iattribute = (IAttribute)iterator.next();
            ModifiableAttributeInstance modifiableattributeinstance = this.func_180795_e(iattribute);

            if (modifiableattributeinstance != null)
            {
                modifiableattributeinstance.flagForUpdate();
            }
        }
    }

    public Set getAttributeInstanceSet()
    {
        return this.attributeInstanceSet;
    }

    public Collection getWatchedAttributes()
    {
        HashSet hashset = Sets.newHashSet();
        Iterator iterator = this.getAllAttributes().iterator();

        while (iterator.hasNext())
        {
            IAttributeInstance iattributeinstance = (IAttributeInstance)iterator.next();

            if (iattributeinstance.getAttribute().getShouldWatch())
            {
                hashset.add(iattributeinstance);
            }
        }

        return hashset;
    }

    public IAttributeInstance getAttributeInstanceByName(String p_111152_1_)
    {
        return this.func_180796_b(p_111152_1_);
    }

    public IAttributeInstance getAttributeInstance(IAttribute p_111151_1_)
    {
        return this.func_180795_e(p_111151_1_);
    }
}