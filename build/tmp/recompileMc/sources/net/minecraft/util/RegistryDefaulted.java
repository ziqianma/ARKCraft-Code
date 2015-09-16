package net.minecraft.util;

public class RegistryDefaulted extends RegistrySimple
{
    /** Default object for this registry, returned when an object is not found. */
    private final Object defaultObject;
    private static final String __OBFID = "CL_00001198";

    public RegistryDefaulted(Object defaultObjectIn)
    {
        this.defaultObject = defaultObjectIn;
    }

    public Object getObject(Object p_82594_1_)
    {
        Object object1 = super.getObject(p_82594_1_);
        return object1 == null ? this.defaultObject : object1;
    }
}