package net.minecraft.entity.ai.attributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModifiableAttributeInstance implements IAttributeInstance
{
    /** The BaseAttributeMap this attributeInstance can be found in */
    private final BaseAttributeMap attributeMap;
    /** The Attribute this is an instance of */
    private final IAttribute genericAttribute;
    private final Map mapByOperation = Maps.newHashMap();
    private final Map mapByName = Maps.newHashMap();
    private final Map mapByUUID = Maps.newHashMap();
    private double baseValue;
    private boolean needsUpdate = true;
    private double cachedValue;
    private static final String __OBFID = "CL_00001567";

    public ModifiableAttributeInstance(BaseAttributeMap p_i1608_1_, IAttribute p_i1608_2_)
    {
        this.attributeMap = p_i1608_1_;
        this.genericAttribute = p_i1608_2_;
        this.baseValue = p_i1608_2_.getDefaultValue();

        for (int i = 0; i < 3; ++i)
        {
            this.mapByOperation.put(Integer.valueOf(i), Sets.newHashSet());
        }
    }

    /**
     * Get the Attribute this is an instance of
     */
    public IAttribute getAttribute()
    {
        return this.genericAttribute;
    }

    public double getBaseValue()
    {
        return this.baseValue;
    }

    public void setBaseValue(double p_111128_1_)
    {
        if (p_111128_1_ != this.getBaseValue())
        {
            this.baseValue = p_111128_1_;
            this.flagForUpdate();
        }
    }

    public Collection getModifiersByOperation(int p_111130_1_)
    {
        return (Collection)this.mapByOperation.get(Integer.valueOf(p_111130_1_));
    }

    public Collection func_111122_c()
    {
        HashSet hashset = Sets.newHashSet();

        for (int i = 0; i < 3; ++i)
        {
            hashset.addAll(this.getModifiersByOperation(i));
        }

        return hashset;
    }

    /**
     * Returns attribute modifier, if any, by the given UUID
     */
    public AttributeModifier getModifier(UUID p_111127_1_)
    {
        return (AttributeModifier)this.mapByUUID.get(p_111127_1_);
    }

    public boolean func_180374_a(AttributeModifier p_180374_1_)
    {
        return this.mapByUUID.get(p_180374_1_.getID()) != null;
    }

    public void applyModifier(AttributeModifier p_111121_1_)
    {
        if (this.getModifier(p_111121_1_.getID()) != null)
        {
            throw new IllegalArgumentException("Modifier is already applied on this attribute!");
        }
        else
        {
            Object object = (Set)this.mapByName.get(p_111121_1_.getName());

            if (object == null)
            {
                object = Sets.newHashSet();
                this.mapByName.put(p_111121_1_.getName(), object);
            }

            ((Set)this.mapByOperation.get(Integer.valueOf(p_111121_1_.getOperation()))).add(p_111121_1_);
            ((Set)object).add(p_111121_1_);
            this.mapByUUID.put(p_111121_1_.getID(), p_111121_1_);
            this.flagForUpdate();
        }
    }

    protected void flagForUpdate()
    {
        this.needsUpdate = true;
        this.attributeMap.func_180794_a(this);
    }

    public void removeModifier(AttributeModifier p_111124_1_)
    {
        for (int i = 0; i < 3; ++i)
        {
            Set set = (Set)this.mapByOperation.get(Integer.valueOf(i));
            set.remove(p_111124_1_);
        }

        Set set1 = (Set)this.mapByName.get(p_111124_1_.getName());

        if (set1 != null)
        {
            set1.remove(p_111124_1_);

            if (set1.isEmpty())
            {
                this.mapByName.remove(p_111124_1_.getName());
            }
        }

        this.mapByUUID.remove(p_111124_1_.getID());
        this.flagForUpdate();
    }

    @SideOnly(Side.CLIENT)
    public void removeAllModifiers()
    {
        Collection collection = this.func_111122_c();

        if (collection != null)
        {
            ArrayList arraylist = Lists.newArrayList(collection);
            Iterator iterator = arraylist.iterator();

            while (iterator.hasNext())
            {
                AttributeModifier attributemodifier = (AttributeModifier)iterator.next();
                this.removeModifier(attributemodifier);
            }
        }
    }

    public double getAttributeValue()
    {
        if (this.needsUpdate)
        {
            this.cachedValue = this.computeValue();
            this.needsUpdate = false;
        }

        return this.cachedValue;
    }

    private double computeValue()
    {
        double d0 = this.getBaseValue();
        AttributeModifier attributemodifier;

        for (Iterator iterator = this.func_180375_b(0).iterator(); iterator.hasNext(); d0 += attributemodifier.getAmount())
        {
            attributemodifier = (AttributeModifier)iterator.next();
        }

        double d1 = d0;
        Iterator iterator1;
        AttributeModifier attributemodifier1;

        for (iterator1 = this.func_180375_b(1).iterator(); iterator1.hasNext(); d1 += d0 * attributemodifier1.getAmount())
        {
            attributemodifier1 = (AttributeModifier)iterator1.next();
        }

        for (iterator1 = this.func_180375_b(2).iterator(); iterator1.hasNext(); d1 *= 1.0D + attributemodifier1.getAmount())
        {
            attributemodifier1 = (AttributeModifier)iterator1.next();
        }

        return this.genericAttribute.clampValue(d1);
    }

    private Collection func_180375_b(int p_180375_1_)
    {
        HashSet hashset = Sets.newHashSet(this.getModifiersByOperation(p_180375_1_));

        for (IAttribute iattribute = this.genericAttribute.func_180372_d(); iattribute != null; iattribute = iattribute.func_180372_d())
        {
            IAttributeInstance iattributeinstance = this.attributeMap.getAttributeInstance(iattribute);

            if (iattributeinstance != null)
            {
                hashset.addAll(iattributeinstance.getModifiersByOperation(p_180375_1_));
            }
        }

        return hashset;
    }
}