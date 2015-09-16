package net.minecraft.util;

import java.util.Iterator;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.EntityNotFoundException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerSelector;
import net.minecraft.entity.Entity;

public class ChatComponentProcessor
{
    private static final String __OBFID = "CL_00002310";

    public static IChatComponent func_179985_a(ICommandSender commandSender, IChatComponent component, Entity p_179985_2_) throws CommandException
    {
        Object object = null;

        if (component instanceof ChatComponentScore)
        {
            ChatComponentScore chatcomponentscore = (ChatComponentScore)component;
            String s = chatcomponentscore.func_179995_g();

            if (PlayerSelector.hasArguments(s))
            {
                List list = PlayerSelector.matchEntities(commandSender, s, Entity.class);

                if (list.size() != 1)
                {
                    throw new EntityNotFoundException();
                }

                s = ((Entity)list.get(0)).getName();
            }

            object = p_179985_2_ != null && s.equals("*") ? new ChatComponentScore(p_179985_2_.getName(), chatcomponentscore.func_179994_h()) : new ChatComponentScore(s, chatcomponentscore.func_179994_h());
            ((ChatComponentScore)object).func_179997_b(chatcomponentscore.getUnformattedTextForChat());
        }
        else if (component instanceof ChatComponentSelector)
        {
            String s1 = ((ChatComponentSelector)component).func_179992_g();
            object = PlayerSelector.func_150869_b(commandSender, s1);

            if (object == null)
            {
                object = new ChatComponentText("");
            }
        }
        else if (component instanceof ChatComponentText)
        {
            object = new ChatComponentText(((ChatComponentText)component).getChatComponentText_TextValue());
        }
        else
        {
            if (!(component instanceof ChatComponentTranslation))
            {
                return component;
            }

            Object[] aobject = ((ChatComponentTranslation)component).getFormatArgs();

            for (int i = 0; i < aobject.length; ++i)
            {
                Object object1 = aobject[i];

                if (object1 instanceof IChatComponent)
                {
                    aobject[i] = func_179985_a(commandSender, (IChatComponent)object1, p_179985_2_);
                }
            }

            object = new ChatComponentTranslation(((ChatComponentTranslation)component).getKey(), aobject);
        }

        ChatStyle chatstyle = component.getChatStyle();

        if (chatstyle != null)
        {
            ((IChatComponent)object).setChatStyle(chatstyle.createShallowCopy());
        }

        Iterator iterator = component.getSiblings().iterator();

        while (iterator.hasNext())
        {
            IChatComponent ichatcomponent1 = (IChatComponent)iterator.next();
            ((IChatComponent)object).appendSibling(func_179985_a(commandSender, ichatcomponent1, p_179985_2_));
        }

        return (IChatComponent)object;
    }
}