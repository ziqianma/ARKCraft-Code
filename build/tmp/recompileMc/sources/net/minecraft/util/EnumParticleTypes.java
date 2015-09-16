package net.minecraft.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Map;

public enum EnumParticleTypes
{
    EXPLOSION_NORMAL("explode", 0, true),
    EXPLOSION_LARGE("largeexplode", 1, true),
    EXPLOSION_HUGE("hugeexplosion", 2, true),
    FIREWORKS_SPARK("fireworksSpark", 3, false),
    WATER_BUBBLE("bubble", 4, false),
    WATER_SPLASH("splash", 5, false),
    WATER_WAKE("wake", 6, false),
    SUSPENDED("suspended", 7, false),
    SUSPENDED_DEPTH("depthsuspend", 8, false),
    CRIT("crit", 9, false),
    CRIT_MAGIC("magicCrit", 10, false),
    SMOKE_NORMAL("smoke", 11, false),
    SMOKE_LARGE("largesmoke", 12, false),
    SPELL("spell", 13, false),
    SPELL_INSTANT("instantSpell", 14, false),
    SPELL_MOB("mobSpell", 15, false),
    SPELL_MOB_AMBIENT("mobSpellAmbient", 16, false),
    SPELL_WITCH("witchMagic", 17, false),
    DRIP_WATER("dripWater", 18, false),
    DRIP_LAVA("dripLava", 19, false),
    VILLAGER_ANGRY("angryVillager", 20, false),
    VILLAGER_HAPPY("happyVillager", 21, false),
    TOWN_AURA("townaura", 22, false),
    NOTE("note", 23, false),
    PORTAL("portal", 24, false),
    ENCHANTMENT_TABLE("enchantmenttable", 25, false),
    FLAME("flame", 26, false),
    LAVA("lava", 27, false),
    FOOTSTEP("footstep", 28, false),
    CLOUD("cloud", 29, false),
    REDSTONE("reddust", 30, false),
    SNOWBALL("snowballpoof", 31, false),
    SNOW_SHOVEL("snowshovel", 32, false),
    SLIME("slime", 33, false),
    HEART("heart", 34, false),
    BARRIER("barrier", 35, false),
    ITEM_CRACK("iconcrack_", 36, false, 2),
    BLOCK_CRACK("blockcrack_", 37, false, 1),
    BLOCK_DUST("blockdust_", 38, false, 1),
    WATER_DROP("droplet", 39, false),
    ITEM_TAKE("take", 40, false),
    MOB_APPEARANCE("mobappearance", 41, true);
    private final String particleName;
    private final int particleID;
    private final boolean field_179371_S;
    private final int field_179366_T;
    private static final Map field_179365_U = Maps.newHashMap();
    private static final String[] field_179368_V;

    private static final String __OBFID = "CL_00002317";

    private EnumParticleTypes(String particleNameIn, int particleIDIn, boolean p_i46011_5_, int p_i46011_6_)
    {
        this.particleName = particleNameIn;
        this.particleID = particleIDIn;
        this.field_179371_S = p_i46011_5_;
        this.field_179366_T = p_i46011_6_;
    }

    private EnumParticleTypes(String particleNameIn, int particleIDIn, boolean p_i46012_5_)
    {
        this(particleNameIn, particleIDIn, p_i46012_5_, 0);
    }

    public static String[] func_179349_a()
    {
        return field_179368_V;
    }

    public String getParticleName()
    {
        return this.particleName;
    }

    public int getParticleID()
    {
        return this.particleID;
    }

    public int getArgumentCount()
    {
        return this.field_179366_T;
    }

    public boolean func_179344_e()
    {
        return this.field_179371_S;
    }

    public boolean func_179343_f()
    {
        return this.field_179366_T > 0;
    }

    /**
     * Gets the relative EnumParticleTypes by id.
     */
    public static EnumParticleTypes getParticleFromId(int particleId)
    {
        return (EnumParticleTypes)field_179365_U.get(Integer.valueOf(particleId));
    }

    static
    {
        ArrayList var0 = Lists.newArrayList();
        EnumParticleTypes[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3)
        {
            EnumParticleTypes var4 = var1[var3];
            field_179365_U.put(Integer.valueOf(var4.getParticleID()), var4);

            if (!var4.getParticleName().endsWith("_"))
            {
                var0.add(var4.getParticleName());
            }
        }

        field_179368_V = (String[])var0.toArray(new String[var0.size()]);
    }
}