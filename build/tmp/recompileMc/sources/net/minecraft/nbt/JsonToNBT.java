package net.minecraft.nbt;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.Stack;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JsonToNBT
{
    private static final Logger logger = LogManager.getLogger();
    private static final Pattern field_179273_b = Pattern.compile("\\[[-+\\d|,\\s]+\\]");
    private static final String __OBFID = "CL_00001232";

    public static NBTTagCompound func_180713_a(String p_180713_0_) throws NBTException
    {
        p_180713_0_ = p_180713_0_.trim();

        if (!p_180713_0_.startsWith("{"))
        {
            throw new NBTException("Invalid tag encountered, expected \'{\' as first char.");
        }
        else if (func_150310_b(p_180713_0_) != 1)
        {
            throw new NBTException("Encountered multiple top tags, only one expected");
        }
        else
        {
            return (NBTTagCompound)func_150316_a("tag", p_180713_0_).func_150489_a();
        }
    }

    static int func_150310_b(String p_150310_0_) throws NBTException
    {
        int i = 0;
        boolean flag = false;
        Stack stack = new Stack();

        for (int j = 0; j < p_150310_0_.length(); ++j)
        {
            char c0 = p_150310_0_.charAt(j);

            if (c0 == 34)
            {
                if (func_179271_b(p_150310_0_, j))
                {
                    if (!flag)
                    {
                        throw new NBTException("Illegal use of \\\": " + p_150310_0_);
                    }
                }
                else
                {
                    flag = !flag;
                }
            }
            else if (!flag)
            {
                if (c0 != 123 && c0 != 91)
                {
                    if (c0 == 125 && (stack.isEmpty() || ((Character)stack.pop()).charValue() != 123))
                    {
                        throw new NBTException("Unbalanced curly brackets {}: " + p_150310_0_);
                    }

                    if (c0 == 93 && (stack.isEmpty() || ((Character)stack.pop()).charValue() != 91))
                    {
                        throw new NBTException("Unbalanced square brackets []: " + p_150310_0_);
                    }
                }
                else
                {
                    if (stack.isEmpty())
                    {
                        ++i;
                    }

                    stack.push(Character.valueOf(c0));
                }
            }
        }

        if (flag)
        {
            throw new NBTException("Unbalanced quotation: " + p_150310_0_);
        }
        else if (!stack.isEmpty())
        {
            throw new NBTException("Unbalanced brackets: " + p_150310_0_);
        }
        else
        {
            if (i == 0 && !p_150310_0_.isEmpty())
            {
                i = 1;
            }

            return i;
        }
    }

    static JsonToNBT.Any func_179272_a(String ... p_179272_0_) throws NBTException
    {
        return func_150316_a(p_179272_0_[0], p_179272_0_[1]);
    }

    static JsonToNBT.Any func_150316_a(String p_150316_0_, String p_150316_1_) throws NBTException
    {
        p_150316_1_ = p_150316_1_.trim();
        String s2;
        boolean flag;
        char c0;

        if (p_150316_1_.startsWith("{"))
        {
            p_150316_1_ = p_150316_1_.substring(1, p_150316_1_.length() - 1);
            JsonToNBT.Compound compound;

            for (compound = new JsonToNBT.Compound(p_150316_0_); p_150316_1_.length() > 0; p_150316_1_ = p_150316_1_.substring(s2.length() + 1))
            {
                s2 = func_150314_a(p_150316_1_, true);

                if (s2.length() > 0)
                {
                    flag = false;
                    compound.field_150491_b.add(func_179270_a(s2, flag));
                }

                if (p_150316_1_.length() < s2.length() + 1)
                {
                    break;
                }

                c0 = p_150316_1_.charAt(s2.length());

                if (c0 != 44 && c0 != 123 && c0 != 125 && c0 != 91 && c0 != 93)
                {
                    throw new NBTException("Unexpected token \'" + c0 + "\' at: " + p_150316_1_.substring(s2.length()));
                }
            }

            return compound;
        }
        else if (p_150316_1_.startsWith("[") && !field_179273_b.matcher(p_150316_1_).matches())
        {
            p_150316_1_ = p_150316_1_.substring(1, p_150316_1_.length() - 1);
            JsonToNBT.List list;

            for (list = new JsonToNBT.List(p_150316_0_); p_150316_1_.length() > 0; p_150316_1_ = p_150316_1_.substring(s2.length() + 1))
            {
                s2 = func_150314_a(p_150316_1_, false);

                if (s2.length() > 0)
                {
                    flag = true;
                    list.field_150492_b.add(func_179270_a(s2, flag));
                }

                if (p_150316_1_.length() < s2.length() + 1)
                {
                    break;
                }

                c0 = p_150316_1_.charAt(s2.length());

                if (c0 != 44 && c0 != 123 && c0 != 125 && c0 != 91 && c0 != 93)
                {
                    throw new NBTException("Unexpected token \'" + c0 + "\' at: " + p_150316_1_.substring(s2.length()));
                }
            }

            return list;
        }
        else
        {
            return new JsonToNBT.Primitive(p_150316_0_, p_150316_1_);
        }
    }

    private static JsonToNBT.Any func_179270_a(String p_179270_0_, boolean p_179270_1_) throws NBTException
    {
        String s1 = func_150313_b(p_179270_0_, p_179270_1_);
        String s2 = func_150311_c(p_179270_0_, p_179270_1_);
        return func_179272_a(new String[] {s1, s2});
    }

    private static String func_150314_a(String p_150314_0_, boolean p_150314_1_) throws NBTException
    {
        int i = func_150312_a(p_150314_0_, ':');
        int j = func_150312_a(p_150314_0_, ',');

        if (p_150314_1_)
        {
            if (i == -1)
            {
                throw new NBTException("Unable to locate name/value separator for string: " + p_150314_0_);
            }

            if (j != -1 && j < i)
            {
                throw new NBTException("Name error at: " + p_150314_0_);
            }
        }
        else if (i == -1 || i > j)
        {
            i = -1;
        }

        return func_179269_a(p_150314_0_, i);
    }

    private static String func_179269_a(String p_179269_0_, int p_179269_1_) throws NBTException
    {
        Stack stack = new Stack();
        int j = p_179269_1_ + 1;
        boolean flag = false;
        boolean flag1 = false;
        boolean flag2 = false;

        for (int k = 0; j < p_179269_0_.length(); ++j)
        {
            char c0 = p_179269_0_.charAt(j);

            if (c0 == 34)
            {
                if (func_179271_b(p_179269_0_, j))
                {
                    if (!flag)
                    {
                        throw new NBTException("Illegal use of \\\": " + p_179269_0_);
                    }
                }
                else
                {
                    flag = !flag;

                    if (flag && !flag2)
                    {
                        flag1 = true;
                    }

                    if (!flag)
                    {
                        k = j;
                    }
                }
            }
            else if (!flag)
            {
                if (c0 != 123 && c0 != 91)
                {
                    if (c0 == 125 && (stack.isEmpty() || ((Character)stack.pop()).charValue() != 123))
                    {
                        throw new NBTException("Unbalanced curly brackets {}: " + p_179269_0_);
                    }

                    if (c0 == 93 && (stack.isEmpty() || ((Character)stack.pop()).charValue() != 91))
                    {
                        throw new NBTException("Unbalanced square brackets []: " + p_179269_0_);
                    }

                    if (c0 == 44 && stack.isEmpty())
                    {
                        return p_179269_0_.substring(0, j);
                    }
                }
                else
                {
                    stack.push(Character.valueOf(c0));
                }
            }

            if (!Character.isWhitespace(c0))
            {
                if (!flag && flag1 && k != j)
                {
                    return p_179269_0_.substring(0, k + 1);
                }

                flag2 = true;
            }
        }

        return p_179269_0_.substring(0, j);
    }

    private static String func_150313_b(String p_150313_0_, boolean p_150313_1_) throws NBTException
    {
        if (p_150313_1_)
        {
            p_150313_0_ = p_150313_0_.trim();

            if (p_150313_0_.startsWith("{") || p_150313_0_.startsWith("["))
            {
                return "";
            }
        }

        int i = func_150312_a(p_150313_0_, ':');

        if (i == -1)
        {
            if (p_150313_1_)
            {
                return "";
            }
            else
            {
                throw new NBTException("Unable to locate name/value separator for string: " + p_150313_0_);
            }
        }
        else
        {
            return p_150313_0_.substring(0, i).trim();
        }
    }

    private static String func_150311_c(String p_150311_0_, boolean p_150311_1_) throws NBTException
    {
        if (p_150311_1_)
        {
            p_150311_0_ = p_150311_0_.trim();

            if (p_150311_0_.startsWith("{") || p_150311_0_.startsWith("["))
            {
                return p_150311_0_;
            }
        }

        int i = func_150312_a(p_150311_0_, ':');

        if (i == -1)
        {
            if (p_150311_1_)
            {
                return p_150311_0_;
            }
            else
            {
                throw new NBTException("Unable to locate name/value separator for string: " + p_150311_0_);
            }
        }
        else
        {
            return p_150311_0_.substring(i + 1).trim();
        }
    }

    private static int func_150312_a(String p_150312_0_, char p_150312_1_)
    {
        int i = 0;

        for (boolean flag = true; i < p_150312_0_.length(); ++i)
        {
            char c1 = p_150312_0_.charAt(i);

            if (c1 == 34)
            {
                if (!func_179271_b(p_150312_0_, i))
                {
                    flag = !flag;
                }
            }
            else if (flag)
            {
                if (c1 == p_150312_1_)
                {
                    return i;
                }

                if (c1 == 123 || c1 == 91)
                {
                    return -1;
                }
            }
        }

        return -1;
    }

    private static boolean func_179271_b(String p_179271_0_, int p_179271_1_)
    {
        return p_179271_1_ > 0 && p_179271_0_.charAt(p_179271_1_ - 1) == 92 && !func_179271_b(p_179271_0_, p_179271_1_ - 1);
    }

    abstract static class Any
        {
            protected String field_150490_a;
            private static final String __OBFID = "CL_00001233";

            public abstract NBTBase func_150489_a();
        }

    static class Compound extends JsonToNBT.Any
        {
            protected java.util.List field_150491_b = Lists.newArrayList();
            private static final String __OBFID = "CL_00001234";

            public Compound(String p_i45137_1_)
            {
                this.field_150490_a = p_i45137_1_;
            }

            public NBTBase func_150489_a()
            {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                Iterator iterator = this.field_150491_b.iterator();

                while (iterator.hasNext())
                {
                    JsonToNBT.Any any = (JsonToNBT.Any)iterator.next();
                    nbttagcompound.setTag(any.field_150490_a, any.func_150489_a());
                }

                return nbttagcompound;
            }
        }

    static class List extends JsonToNBT.Any
        {
            protected java.util.List field_150492_b = Lists.newArrayList();
            private static final String __OBFID = "CL_00001235";

            public List(String p_i45138_1_)
            {
                this.field_150490_a = p_i45138_1_;
            }

            public NBTBase func_150489_a()
            {
                NBTTagList nbttaglist = new NBTTagList();
                Iterator iterator = this.field_150492_b.iterator();

                while (iterator.hasNext())
                {
                    JsonToNBT.Any any = (JsonToNBT.Any)iterator.next();
                    nbttaglist.appendTag(any.func_150489_a());
                }

                return nbttaglist;
            }
        }

    static class Primitive extends JsonToNBT.Any
        {
            private static final Pattern field_179265_c = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+[d|D]");
            private static final Pattern field_179263_d = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+[f|F]");
            private static final Pattern field_179264_e = Pattern.compile("[-+]?[0-9]+[b|B]");
            private static final Pattern field_179261_f = Pattern.compile("[-+]?[0-9]+[l|L]");
            private static final Pattern field_179262_g = Pattern.compile("[-+]?[0-9]+[s|S]");
            private static final Pattern field_179267_h = Pattern.compile("[-+]?[0-9]+");
            private static final Pattern field_179268_i = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+");
            private static final Splitter field_179266_j = Splitter.on(',').omitEmptyStrings();
            protected String field_150493_b;
            private static final String __OBFID = "CL_00001236";

            public Primitive(String p_i45139_1_, String p_i45139_2_)
            {
                this.field_150490_a = p_i45139_1_;
                this.field_150493_b = p_i45139_2_;
            }

            public NBTBase func_150489_a()
            {
                try
                {
                    if (field_179265_c.matcher(this.field_150493_b).matches())
                    {
                        return new NBTTagDouble(Double.parseDouble(this.field_150493_b.substring(0, this.field_150493_b.length() - 1)));
                    }

                    if (field_179263_d.matcher(this.field_150493_b).matches())
                    {
                        return new NBTTagFloat(Float.parseFloat(this.field_150493_b.substring(0, this.field_150493_b.length() - 1)));
                    }

                    if (field_179264_e.matcher(this.field_150493_b).matches())
                    {
                        return new NBTTagByte(Byte.parseByte(this.field_150493_b.substring(0, this.field_150493_b.length() - 1)));
                    }

                    if (field_179261_f.matcher(this.field_150493_b).matches())
                    {
                        return new NBTTagLong(Long.parseLong(this.field_150493_b.substring(0, this.field_150493_b.length() - 1)));
                    }

                    if (field_179262_g.matcher(this.field_150493_b).matches())
                    {
                        return new NBTTagShort(Short.parseShort(this.field_150493_b.substring(0, this.field_150493_b.length() - 1)));
                    }

                    if (field_179267_h.matcher(this.field_150493_b).matches())
                    {
                        return new NBTTagInt(Integer.parseInt(this.field_150493_b));
                    }

                    if (field_179268_i.matcher(this.field_150493_b).matches())
                    {
                        return new NBTTagDouble(Double.parseDouble(this.field_150493_b));
                    }

                    if (this.field_150493_b.equalsIgnoreCase("true") || this.field_150493_b.equalsIgnoreCase("false"))
                    {
                        return new NBTTagByte((byte)(Boolean.parseBoolean(this.field_150493_b) ? 1 : 0));
                    }
                }
                catch (NumberFormatException numberformatexception1)
                {
                    this.field_150493_b = this.field_150493_b.replaceAll("\\\\\"", "\"");
                    return new NBTTagString(this.field_150493_b);
                }

                if (this.field_150493_b.startsWith("[") && this.field_150493_b.endsWith("]"))
                {
                    String s = this.field_150493_b.substring(1, this.field_150493_b.length() - 1);
                    String[] astring = (String[])Iterables.toArray(field_179266_j.split(s), String.class);

                    try
                    {
                        int[] aint = new int[astring.length];

                        for (int j = 0; j < astring.length; ++j)
                        {
                            aint[j] = Integer.parseInt(astring[j].trim());
                        }

                        return new NBTTagIntArray(aint);
                    }
                    catch (NumberFormatException numberformatexception)
                    {
                        return new NBTTagString(this.field_150493_b);
                    }
                }
                else
                {
                    if (this.field_150493_b.startsWith("\"") && this.field_150493_b.endsWith("\""))
                    {
                        this.field_150493_b = this.field_150493_b.substring(1, this.field_150493_b.length() - 1);
                    }

                    this.field_150493_b = this.field_150493_b.replaceAll("\\\\\"", "\"");
                    StringBuilder stringbuilder = new StringBuilder();

                    for (int i = 0; i < this.field_150493_b.length(); ++i)
                    {
                        if (i < this.field_150493_b.length() - 1 && this.field_150493_b.charAt(i) == 92 && this.field_150493_b.charAt(i + 1) == 92)
                        {
                            stringbuilder.append('\\');
                            ++i;
                        }
                        else
                        {
                            stringbuilder.append(this.field_150493_b.charAt(i));
                        }
                    }

                    return new NBTTagString(stringbuilder.toString());
                }
            }
        }
}