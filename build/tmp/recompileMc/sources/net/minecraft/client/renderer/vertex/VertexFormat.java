package net.minecraft.client.renderer.vertex;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SideOnly(Side.CLIENT)
public class VertexFormat
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final List elements;
    private final List offsets;
    /** The next available offset in this vertex format */
    private int nextOffset;
    private int colorElementOffset;
    private List elementOffsetsById;
    private int normalElementOffset;
    private static final String __OBFID = "CL_00002401";

    public VertexFormat(VertexFormat p_i46097_1_)
    {
        this();

        for (int i = 0; i < p_i46097_1_.getElementCount(); ++i)
        {
            this.setElement(p_i46097_1_.getElement(i));
        }

        this.nextOffset = p_i46097_1_.getNextOffset();
    }

    public VertexFormat()
    {
        this.elements = Lists.newArrayList();
        this.offsets = Lists.newArrayList();
        this.nextOffset = 0;
        this.colorElementOffset = -1;
        this.elementOffsetsById = Lists.newArrayList();
        this.normalElementOffset = -1;
    }

    public void clear()
    {
        this.elements.clear();
        this.offsets.clear();
        this.colorElementOffset = -1;
        this.elementOffsetsById.clear();
        this.normalElementOffset = -1;
        this.nextOffset = 0;
    }

    public void setElement(VertexFormatElement p_177349_1_)
    {
        if (p_177349_1_.isPositionElement() && this.hasPosition())
        {
            LOGGER.warn("VertexFormat error: Trying to add a position VertexFormatElement when one already exists, ignoring.");
        }
        else
        {
            this.elements.add(p_177349_1_);
            this.offsets.add(Integer.valueOf(this.nextOffset));
            p_177349_1_.setOffset(this.nextOffset);
            this.nextOffset += p_177349_1_.getSize();

            switch (VertexFormat.SwitchEnumUsage.field_177382_a[p_177349_1_.getUsage().ordinal()])
            {
                case 1:
                    this.normalElementOffset = p_177349_1_.getOffset();
                    break;
                case 2:
                    this.colorElementOffset = p_177349_1_.getOffset();
                    break;
                case 3:
                    this.elementOffsetsById.add(p_177349_1_.getIndex(), Integer.valueOf(p_177349_1_.getOffset()));
            }
        }
    }

    public boolean hasNormal()
    {
        return this.normalElementOffset >= 0;
    }

    public int getNormalOffset()
    {
        return this.normalElementOffset;
    }

    public boolean hasColor()
    {
        return this.colorElementOffset >= 0;
    }

    public int getColorOffset()
    {
        return this.colorElementOffset;
    }

    public boolean hasElementOffset(int id)
    {
        return this.elementOffsetsById.size() - 1 >= id;
    }

    public int getElementOffsetById(int id)
    {
        return ((Integer)this.elementOffsetsById.get(id)).intValue();
    }

    public String toString()
    {
        String s = "format: " + this.elements.size() + " elements: ";

        for (int i = 0; i < this.elements.size(); ++i)
        {
            s = s + ((VertexFormatElement)this.elements.get(i)).toString();

            if (i != this.elements.size() - 1)
            {
                s = s + " ";
            }
        }

        return s;
    }

    private boolean hasPosition()
    {
        Iterator iterator = this.elements.iterator();
        VertexFormatElement vertexformatelement;

        do
        {
            if (!iterator.hasNext())
            {
                return false;
            }

            vertexformatelement = (VertexFormatElement)iterator.next();
        }
        while (!vertexformatelement.isPositionElement());

        return true;
    }

    public int getNextOffset()
    {
        return this.nextOffset;
    }

    public List getElements()
    {
        return this.elements;
    }

    public int getElementCount()
    {
        return this.elements.size();
    }

    public VertexFormatElement getElement(int p_177348_1_)
    {
        return (VertexFormatElement)this.elements.get(p_177348_1_);
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass())
        {
            VertexFormat vertexformat = (VertexFormat)p_equals_1_;
            return this.nextOffset != vertexformat.nextOffset ? false : (!this.elements.equals(vertexformat.elements) ? false : this.offsets.equals(vertexformat.offsets));
        }
        else
        {
            return false;
        }
    }

    public int hashCode()
    {
        int i = this.elements.hashCode();
        i = 31 * i + this.offsets.hashCode();
        i = 31 * i + this.nextOffset;
        return i;
    }

    @SideOnly(Side.CLIENT)

    static final class SwitchEnumUsage
        {
            static final int[] field_177382_a = new int[VertexFormatElement.EnumUsage.values().length];
            private static final String __OBFID = "CL_00002400";

            static
            {
                try
                {
                    field_177382_a[VertexFormatElement.EnumUsage.NORMAL.ordinal()] = 1;
                }
                catch (NoSuchFieldError var3)
                {
                    ;
                }

                try
                {
                    field_177382_a[VertexFormatElement.EnumUsage.COLOR.ordinal()] = 2;
                }
                catch (NoSuchFieldError var2)
                {
                    ;
                }

                try
                {
                    field_177382_a[VertexFormatElement.EnumUsage.UV.ordinal()] = 3;
                }
                catch (NoSuchFieldError var1)
                {
                    ;
                }
            }
        }
}