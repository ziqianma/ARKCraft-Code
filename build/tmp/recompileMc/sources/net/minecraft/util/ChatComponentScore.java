package net.minecraft.util;

import java.util.Iterator;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;

public class ChatComponentScore extends ChatComponentStyle
{
    private final String field_179999_b;
    private final String field_180000_c;
    private String field_179998_d = "";
    private static final String __OBFID = "CL_00002309";

    public ChatComponentScore(String p_i45997_1_, String p_i45997_2_)
    {
        this.field_179999_b = p_i45997_1_;
        this.field_180000_c = p_i45997_2_;
    }

    public String func_179995_g()
    {
        return this.field_179999_b;
    }

    public String func_179994_h()
    {
        return this.field_180000_c;
    }

    public void func_179997_b(String p_179997_1_)
    {
        this.field_179998_d = p_179997_1_;
    }

    /**
     * Gets the text of this component, without any special formatting codes added, for chat.  TODO: why is this two
     * different methods?
     */
    public String getUnformattedTextForChat()
    {
        MinecraftServer minecraftserver = MinecraftServer.getServer();

        if (minecraftserver != null && minecraftserver.func_175578_N() && StringUtils.isNullOrEmpty(this.field_179998_d))
        {
            Scoreboard scoreboard = minecraftserver.worldServerForDimension(0).getScoreboard();
            ScoreObjective scoreobjective = scoreboard.getObjective(this.field_180000_c);

            if (scoreboard.entityHasObjective(this.field_179999_b, scoreobjective))
            {
                Score score = scoreboard.getValueFromObjective(this.field_179999_b, scoreobjective);
                this.func_179997_b(String.format("%d", new Object[] {Integer.valueOf(score.getScorePoints())}));
            }
            else
            {
                this.field_179998_d = "";
            }
        }

        return this.field_179998_d;
    }

    public ChatComponentScore func_179996_i()
    {
        ChatComponentScore chatcomponentscore = new ChatComponentScore(this.field_179999_b, this.field_180000_c);
        chatcomponentscore.func_179997_b(this.field_179998_d);
        chatcomponentscore.setChatStyle(this.getChatStyle().createShallowCopy());
        Iterator iterator = this.getSiblings().iterator();

        while (iterator.hasNext())
        {
            IChatComponent ichatcomponent = (IChatComponent)iterator.next();
            chatcomponentscore.appendSibling(ichatcomponent.createCopy());
        }

        return chatcomponentscore;
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else if (!(p_equals_1_ instanceof ChatComponentScore))
        {
            return false;
        }
        else
        {
            ChatComponentScore chatcomponentscore = (ChatComponentScore)p_equals_1_;
            return this.field_179999_b.equals(chatcomponentscore.field_179999_b) && this.field_180000_c.equals(chatcomponentscore.field_180000_c) && super.equals(p_equals_1_);
        }
    }

    public String toString()
    {
        return "ScoreComponent{name=\'" + this.field_179999_b + '\'' + "objective=\'" + this.field_180000_c + '\'' + ", siblings=" + this.siblings + ", style=" + this.getChatStyle() + '}';
    }

    /**
     * Creates a copy of this component.  Almost a deep copy, except the style is shallow-copied.
     */
    public IChatComponent createCopy()
    {
        return this.func_179996_i();
    }
}