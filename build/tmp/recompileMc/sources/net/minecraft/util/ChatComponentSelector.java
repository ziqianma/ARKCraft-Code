package net.minecraft.util;

import java.util.Iterator;

public class ChatComponentSelector extends ChatComponentStyle
{
    private final String field_179993_b;
    private static final String __OBFID = "CL_00002308";

    public ChatComponentSelector(String p_i45996_1_)
    {
        this.field_179993_b = p_i45996_1_;
    }

    public String func_179992_g()
    {
        return this.field_179993_b;
    }

    /**
     * Gets the text of this component, without any special formatting codes added, for chat.  TODO: why is this two
     * different methods?
     */
    public String getUnformattedTextForChat()
    {
        return this.field_179993_b;
    }

    public ChatComponentSelector func_179991_h()
    {
        ChatComponentSelector chatcomponentselector = new ChatComponentSelector(this.field_179993_b);
        chatcomponentselector.setChatStyle(this.getChatStyle().createShallowCopy());
        Iterator iterator = this.getSiblings().iterator();

        while (iterator.hasNext())
        {
            IChatComponent ichatcomponent = (IChatComponent)iterator.next();
            chatcomponentselector.appendSibling(ichatcomponent.createCopy());
        }

        return chatcomponentselector;
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else if (!(p_equals_1_ instanceof ChatComponentSelector))
        {
            return false;
        }
        else
        {
            ChatComponentSelector chatcomponentselector = (ChatComponentSelector)p_equals_1_;
            return this.field_179993_b.equals(chatcomponentselector.field_179993_b) && super.equals(p_equals_1_);
        }
    }

    public String toString()
    {
        return "SelectorComponent{pattern=\'" + this.field_179993_b + '\'' + ", siblings=" + this.siblings + ", style=" + this.getChatStyle() + '}';
    }

    /**
     * Creates a copy of this component.  Almost a deep copy, except the style is shallow-copied.
     */
    public IChatComponent createCopy()
    {
        return this.func_179991_h();
    }
}