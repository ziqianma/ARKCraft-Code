package net.minecraft.client.renderer.chunk;

import com.google.common.collect.Lists;
import java.util.BitSet;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.Set;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class VisGraph
{
    private static final int field_178616_a = (int)Math.pow(16.0D, 0.0D);
    private static final int field_178614_b = (int)Math.pow(16.0D, 1.0D);
    private static final int field_178615_c = (int)Math.pow(16.0D, 2.0D);
    private final BitSet field_178612_d = new BitSet(4096);
    private static final int[] field_178613_e = new int[1352];
    private int field_178611_f = 4096;
    private static final String __OBFID = "CL_00002450";

    public void func_178606_a(BlockPos pos)
    {
        this.field_178612_d.set(func_178608_c(pos), true);
        --this.field_178611_f;
    }

    private static int func_178608_c(BlockPos pos)
    {
        return func_178605_a(pos.getX() & 15, pos.getY() & 15, pos.getZ() & 15);
    }

    private static int func_178605_a(int p_178605_0_, int p_178605_1_, int p_178605_2_)
    {
        return p_178605_0_ << 0 | p_178605_1_ << 8 | p_178605_2_ << 4;
    }

    public SetVisibility computeVisibility()
    {
        SetVisibility setvisibility = new SetVisibility();

        if (4096 - this.field_178611_f < 256)
        {
            setvisibility.setAllVisible(true);
        }
        else if (this.field_178611_f == 0)
        {
            setvisibility.setAllVisible(false);
        }
        else
        {
            int[] aint = field_178613_e;
            int i = aint.length;

            for (int j = 0; j < i; ++j)
            {
                int k = aint[j];

                if (!this.field_178612_d.get(k))
                {
                    setvisibility.setManyVisible(this.func_178604_a(k));
                }
            }
        }

        return setvisibility;
    }

    public Set func_178609_b(BlockPos pos)
    {
        return this.func_178604_a(func_178608_c(pos));
    }

    private Set func_178604_a(int p_178604_1_)
    {
        EnumSet enumset = EnumSet.noneOf(EnumFacing.class);
        LinkedList linkedlist = Lists.newLinkedList();
        linkedlist.add(Integer.valueOf(p_178604_1_));
        this.field_178612_d.set(p_178604_1_, true);

        while (!linkedlist.isEmpty())
        {
            int j = ((Integer)linkedlist.poll()).intValue();
            this.func_178610_a(j, enumset);
            EnumFacing[] aenumfacing = EnumFacing.values();
            int k = aenumfacing.length;

            for (int l = 0; l < k; ++l)
            {
                EnumFacing enumfacing = aenumfacing[l];
                int i1 = this.func_178603_a(j, enumfacing);

                if (i1 >= 0 && !this.field_178612_d.get(i1))
                {
                    this.field_178612_d.set(i1, true);
                    linkedlist.add(Integer.valueOf(i1));
                }
            }
        }

        return enumset;
    }

    private void func_178610_a(int p_178610_1_, Set p_178610_2_)
    {
        int j = p_178610_1_ >> 0 & 15;

        if (j == 0)
        {
            p_178610_2_.add(EnumFacing.WEST);
        }
        else if (j == 15)
        {
            p_178610_2_.add(EnumFacing.EAST);
        }

        int k = p_178610_1_ >> 8 & 15;

        if (k == 0)
        {
            p_178610_2_.add(EnumFacing.DOWN);
        }
        else if (k == 15)
        {
            p_178610_2_.add(EnumFacing.UP);
        }

        int l = p_178610_1_ >> 4 & 15;

        if (l == 0)
        {
            p_178610_2_.add(EnumFacing.NORTH);
        }
        else if (l == 15)
        {
            p_178610_2_.add(EnumFacing.SOUTH);
        }
    }

    private int func_178603_a(int p_178603_1_, EnumFacing p_178603_2_)
    {
        switch (VisGraph.SwitchEnumFacing.field_178617_a[p_178603_2_.ordinal()])
        {
            case 1:
                if ((p_178603_1_ >> 8 & 15) == 0)
                {
                    return -1;
                }

                return p_178603_1_ - field_178615_c;
            case 2:
                if ((p_178603_1_ >> 8 & 15) == 15)
                {
                    return -1;
                }

                return p_178603_1_ + field_178615_c;
            case 3:
                if ((p_178603_1_ >> 4 & 15) == 0)
                {
                    return -1;
                }

                return p_178603_1_ - field_178614_b;
            case 4:
                if ((p_178603_1_ >> 4 & 15) == 15)
                {
                    return -1;
                }

                return p_178603_1_ + field_178614_b;
            case 5:
                if ((p_178603_1_ >> 0 & 15) == 0)
                {
                    return -1;
                }

                return p_178603_1_ - field_178616_a;
            case 6:
                if ((p_178603_1_ >> 0 & 15) == 15)
                {
                    return -1;
                }

                return p_178603_1_ + field_178616_a;
            default:
                return -1;
        }
    }

    static
    {
        boolean var0 = false;
        boolean var1 = true;
        int var2 = 0;

        for (int var3 = 0; var3 < 16; ++var3)
        {
            for (int var4 = 0; var4 < 16; ++var4)
            {
                for (int var5 = 0; var5 < 16; ++var5)
                {
                    if (var3 == 0 || var3 == 15 || var4 == 0 || var4 == 15 || var5 == 0 || var5 == 15)
                    {
                        field_178613_e[var2++] = func_178605_a(var3, var4, var5);
                    }
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)

    static final class SwitchEnumFacing
        {
            static final int[] field_178617_a = new int[EnumFacing.values().length];
            private static final String __OBFID = "CL_00002449";

            static
            {
                try
                {
                    field_178617_a[EnumFacing.DOWN.ordinal()] = 1;
                }
                catch (NoSuchFieldError var6)
                {
                    ;
                }

                try
                {
                    field_178617_a[EnumFacing.UP.ordinal()] = 2;
                }
                catch (NoSuchFieldError var5)
                {
                    ;
                }

                try
                {
                    field_178617_a[EnumFacing.NORTH.ordinal()] = 3;
                }
                catch (NoSuchFieldError var4)
                {
                    ;
                }

                try
                {
                    field_178617_a[EnumFacing.SOUTH.ordinal()] = 4;
                }
                catch (NoSuchFieldError var3)
                {
                    ;
                }

                try
                {
                    field_178617_a[EnumFacing.WEST.ordinal()] = 5;
                }
                catch (NoSuchFieldError var2)
                {
                    ;
                }

                try
                {
                    field_178617_a[EnumFacing.EAST.ordinal()] = 6;
                }
                catch (NoSuchFieldError var1)
                {
                    ;
                }
            }
        }
}