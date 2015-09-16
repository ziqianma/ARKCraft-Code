package net.minecraft.client.renderer.chunk;

import java.util.BitSet;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SetVisibility
{
    private static final int COUNT_FACES = EnumFacing.values().length;
    private final BitSet bitSet;
    private static final String __OBFID = "CL_00002448";

    public SetVisibility()
    {
        this.bitSet = new BitSet(COUNT_FACES * COUNT_FACES);
    }

    public void setManyVisible(Set p_178620_1_)
    {
        Iterator iterator = p_178620_1_.iterator();

        while (iterator.hasNext())
        {
            EnumFacing enumfacing = (EnumFacing)iterator.next();
            Iterator iterator1 = p_178620_1_.iterator();

            while (iterator1.hasNext())
            {
                EnumFacing enumfacing1 = (EnumFacing)iterator1.next();
                this.setVisible(enumfacing, enumfacing1, true);
            }
        }
    }

    public void setVisible(EnumFacing p_178619_1_, EnumFacing p_178619_2_, boolean p_178619_3_)
    {
        this.bitSet.set(p_178619_1_.ordinal() + p_178619_2_.ordinal() * COUNT_FACES, p_178619_3_);
        this.bitSet.set(p_178619_2_.ordinal() + p_178619_1_.ordinal() * COUNT_FACES, p_178619_3_);
    }

    public void setAllVisible(boolean p_178618_1_)
    {
        this.bitSet.set(0, this.bitSet.size(), p_178618_1_);
    }

    public boolean isVisible(EnumFacing p_178621_1_, EnumFacing p_178621_2_)
    {
        return this.bitSet.get(p_178621_1_.ordinal() + p_178621_2_.ordinal() * COUNT_FACES);
    }

    public String toString()
    {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append(' ');
        EnumFacing[] aenumfacing = EnumFacing.values();
        int i = aenumfacing.length;
        int j;
        EnumFacing enumfacing;

        for (j = 0; j < i; ++j)
        {
            enumfacing = aenumfacing[j];
            stringbuilder.append(' ').append(enumfacing.toString().toUpperCase().charAt(0));
        }

        stringbuilder.append('\n');
        aenumfacing = EnumFacing.values();
        i = aenumfacing.length;

        for (j = 0; j < i; ++j)
        {
            enumfacing = aenumfacing[j];
            stringbuilder.append(enumfacing.toString().toUpperCase().charAt(0));
            EnumFacing[] aenumfacing1 = EnumFacing.values();
            int k = aenumfacing1.length;

            for (int l = 0; l < k; ++l)
            {
                EnumFacing enumfacing1 = aenumfacing1[l];

                if (enumfacing == enumfacing1)
                {
                    stringbuilder.append("  ");
                }
                else
                {
                    boolean flag = this.isVisible(enumfacing, enumfacing1);
                    stringbuilder.append(' ').append((char)(flag ? 'Y' : 'n'));
                }
            }

            stringbuilder.append('\n');
        }

        return stringbuilder.toString();
    }
}