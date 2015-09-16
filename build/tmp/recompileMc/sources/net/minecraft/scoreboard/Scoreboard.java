package net.minecraft.scoreboard;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.util.EnumChatFormatting;

public class Scoreboard
{
    /** Map of objective names to ScoreObjective objects. */
    private final Map scoreObjectives = Maps.newHashMap();
    /** Map of IScoreObjectiveCriteria objects to ScoreObjective objects. */
    private final Map scoreObjectiveCriterias = Maps.newHashMap();
    /** Map of entities name to ScoreObjective objects. */
    private final Map entitiesScoreObjectives = Maps.newHashMap();
    /** Index 0 is tab menu, 1 is sidebar, and 2 is below name */
    private final ScoreObjective[] objectiveDisplaySlots = new ScoreObjective[19];
    /** Map of teamnames to ScorePlayerTeam instances */
    private final Map teams = Maps.newHashMap();
    /** Map of usernames to ScorePlayerTeam objects. */
    private final Map teamMemberships = Maps.newHashMap();
    private static String[] field_178823_g = null;
    private static final String __OBFID = "CL_00000619";

    /**
     * Returns a ScoreObjective for the objective name
     *  
     * @param name The objective name
     */
    public ScoreObjective getObjective(String name)
    {
        return (ScoreObjective)this.scoreObjectives.get(name);
    }

    /**
     * Create and returns the score objective for the given name and ScoreCriteria
     *  
     * @param name The ScoreObjective Name
     * @param criteria The ScoreObjective Criteria
     */
    public ScoreObjective addScoreObjective(String name, IScoreObjectiveCriteria criteria)
    {
        ScoreObjective scoreobjective = this.getObjective(name);

        if (scoreobjective != null)
        {
            throw new IllegalArgumentException("An objective with the name \'" + name + "\' already exists!");
        }
        else
        {
            scoreobjective = new ScoreObjective(this, name, criteria);
            Object object = (List)this.scoreObjectiveCriterias.get(criteria);

            if (object == null)
            {
                object = Lists.newArrayList();
                this.scoreObjectiveCriterias.put(criteria, object);
            }

            ((List)object).add(scoreobjective);
            this.scoreObjectives.put(name, scoreobjective);
            this.func_96522_a(scoreobjective);
            return scoreobjective;
        }
    }

    /**
     * Returns all the objectives for the given criteria
     *  
     * @param criteria The objective criteria
     */
    public Collection getObjectivesFromCriteria(IScoreObjectiveCriteria criteria)
    {
        Collection collection = (Collection)this.scoreObjectiveCriterias.get(criteria);
        return collection == null ? Lists.newArrayList() : Lists.newArrayList(collection);
    }

    /**
     * Returns if the entity has the given ScoreObjective
     *  
     * @param name The Entity name
     */
    public boolean entityHasObjective(String name, ScoreObjective p_178819_2_)
    {
        Map map = (Map)this.entitiesScoreObjectives.get(name);

        if (map == null)
        {
            return false;
        }
        else
        {
            Score score = (Score)map.get(p_178819_2_);
            return score != null;
        }
    }

    /**
     * Returns the value of the given objective for the given entity name
     *  
     * @param name The entity name
     * @param objective The ScoreObjective to get the value from
     */
    public Score getValueFromObjective(String name, ScoreObjective objective)
    {
        Object object = (Map)this.entitiesScoreObjectives.get(name);

        if (object == null)
        {
            object = Maps.newHashMap();
            this.entitiesScoreObjectives.put(name, object);
        }

        Score score = (Score)((Map)object).get(objective);

        if (score == null)
        {
            score = new Score(this, objective, name);
            ((Map)object).put(objective, score);
        }

        return score;
    }

    /**
     * Returns an array of Score objects, sorting by Score.getScorePoints()
     */
    public Collection getSortedScores(ScoreObjective objective)
    {
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = this.entitiesScoreObjectives.values().iterator();

        while (iterator.hasNext())
        {
            Map map = (Map)iterator.next();
            Score score = (Score)map.get(objective);

            if (score != null)
            {
                arraylist.add(score);
            }
        }

        Collections.sort(arraylist, Score.scoreComparator);
        return arraylist;
    }

    public Collection getScoreObjectives()
    {
        return this.scoreObjectives.values();
    }

    public Collection getObjectiveNames()
    {
        return this.entitiesScoreObjectives.keySet();
    }

    /**
     * Remove the given ScoreObjective for the given Entity name.
     *  
     * @param name The entity Name
     * @param objective The ScoreObjective
     */
    public void removeObjectiveFromEntity(String name, ScoreObjective objective)
    {
        Map map;

        if (objective == null)
        {
            map = (Map)this.entitiesScoreObjectives.remove(name);

            if (map != null)
            {
                this.func_96516_a(name);
            }
        }
        else
        {
            map = (Map)this.entitiesScoreObjectives.get(name);

            if (map != null)
            {
                Score score = (Score)map.remove(objective);

                if (map.size() < 1)
                {
                    Map map1 = (Map)this.entitiesScoreObjectives.remove(name);

                    if (map1 != null)
                    {
                        this.func_96516_a(name);
                    }
                }
                else if (score != null)
                {
                    this.func_178820_a(name, objective);
                }
            }
        }
    }

    public Collection getScores()
    {
        Collection collection = this.entitiesScoreObjectives.values();
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = collection.iterator();

        while (iterator.hasNext())
        {
            Map map = (Map)iterator.next();
            arraylist.addAll(map.values());
        }

        return arraylist;
    }

    /**
     * Returns all the objectives for the given entity
     *  
     * @param name The entity Name
     */
    public Map getObjectivesForEntity(String name)
    {
        Object object = (Map)this.entitiesScoreObjectives.get(name);

        if (object == null)
        {
            object = Maps.newHashMap();
        }

        return (Map)object;
    }

    public void func_96519_k(ScoreObjective p_96519_1_)
    {
        this.scoreObjectives.remove(p_96519_1_.getName());

        for (int i = 0; i < 19; ++i)
        {
            if (this.getObjectiveInDisplaySlot(i) == p_96519_1_)
            {
                this.setObjectiveInDisplaySlot(i, (ScoreObjective)null);
            }
        }

        List list = (List)this.scoreObjectiveCriterias.get(p_96519_1_.getCriteria());

        if (list != null)
        {
            list.remove(p_96519_1_);
        }

        Iterator iterator = this.entitiesScoreObjectives.values().iterator();

        while (iterator.hasNext())
        {
            Map map = (Map)iterator.next();
            map.remove(p_96519_1_);
        }

        this.func_96533_c(p_96519_1_);
    }

    /**
     * 0 is tab menu, 1 is sidebar, 2 is below name
     */
    public void setObjectiveInDisplaySlot(int p_96530_1_, ScoreObjective p_96530_2_)
    {
        this.objectiveDisplaySlots[p_96530_1_] = p_96530_2_;
    }

    /**
     * 0 is tab menu, 1 is sidebar, 2 is below name
     */
    public ScoreObjective getObjectiveInDisplaySlot(int p_96539_1_)
    {
        return this.objectiveDisplaySlots[p_96539_1_];
    }

    /**
     * Retrieve the ScorePlayerTeam instance identified by the passed team name
     */
    public ScorePlayerTeam getTeam(String p_96508_1_)
    {
        return (ScorePlayerTeam)this.teams.get(p_96508_1_);
    }

    public ScorePlayerTeam createTeam(String p_96527_1_)
    {
        ScorePlayerTeam scoreplayerteam = this.getTeam(p_96527_1_);

        if (scoreplayerteam != null)
        {
            throw new IllegalArgumentException("A team with the name \'" + p_96527_1_ + "\' already exists!");
        }
        else
        {
            scoreplayerteam = new ScorePlayerTeam(this, p_96527_1_);
            this.teams.put(p_96527_1_, scoreplayerteam);
            this.broadcastTeamCreated(scoreplayerteam);
            return scoreplayerteam;
        }
    }

    /**
     * Removes the team from the scoreboard, updates all player memberships and broadcasts the deletion to all players
     */
    public void removeTeam(ScorePlayerTeam p_96511_1_)
    {
        this.teams.remove(p_96511_1_.getRegisteredName());
        Iterator iterator = p_96511_1_.getMembershipCollection().iterator();

        while (iterator.hasNext())
        {
            String s = (String)iterator.next();
            this.teamMemberships.remove(s);
        }

        this.func_96513_c(p_96511_1_);
    }

    /**
     * Adds a player to the given team
     *  
     * @param player The name of the player to add
     * @param newTeam The name of the team
     */
    public boolean addPlayerToTeam(String player, String newTeam)
    {
        if (!this.teams.containsKey(newTeam))
        {
            return false;
        }
        else
        {
            ScorePlayerTeam scoreplayerteam = this.getTeam(newTeam);

            if (this.getPlayersTeam(player) != null)
            {
                this.removePlayerFromTeams(player);
            }

            this.teamMemberships.put(player, scoreplayerteam);
            scoreplayerteam.getMembershipCollection().add(player);
            return true;
        }
    }

    public boolean removePlayerFromTeams(String p_96524_1_)
    {
        ScorePlayerTeam scoreplayerteam = this.getPlayersTeam(p_96524_1_);

        if (scoreplayerteam != null)
        {
            this.removePlayerFromTeam(p_96524_1_, scoreplayerteam);
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Removes the given username from the given ScorePlayerTeam. If the player is not on the team then an
     * IllegalStateException is thrown.
     */
    public void removePlayerFromTeam(String p_96512_1_, ScorePlayerTeam p_96512_2_)
    {
        if (this.getPlayersTeam(p_96512_1_) != p_96512_2_)
        {
            throw new IllegalStateException("Player is either on another team or not on any team. Cannot remove from team \'" + p_96512_2_.getRegisteredName() + "\'.");
        }
        else
        {
            this.teamMemberships.remove(p_96512_1_);
            p_96512_2_.getMembershipCollection().remove(p_96512_1_);
        }
    }

    /**
     * Retrieve all registered ScorePlayerTeam names
     */
    public Collection getTeamNames()
    {
        return this.teams.keySet();
    }

    /**
     * Retrieve all registered ScorePlayerTeam instances
     */
    public Collection getTeams()
    {
        return this.teams.values();
    }

    /**
     * Gets the ScorePlayerTeam object for the given username.
     */
    public ScorePlayerTeam getPlayersTeam(String p_96509_1_)
    {
        return (ScorePlayerTeam)this.teamMemberships.get(p_96509_1_);
    }

    public void func_96522_a(ScoreObjective p_96522_1_) {}

    public void func_96532_b(ScoreObjective p_96532_1_) {}

    public void func_96533_c(ScoreObjective p_96533_1_) {}

    public void func_96536_a(Score p_96536_1_) {}

    public void func_96516_a(String p_96516_1_) {}

    public void func_178820_a(String p_178820_1_, ScoreObjective p_178820_2_) {}

    /**
     * This packet will notify the players that this team is created, and that will register it on the client
     */
    public void broadcastTeamCreated(ScorePlayerTeam playerTeam) {}

    /**
     * This packet will notify the players that this team is updated
     */
    public void sendTeamUpdate(ScorePlayerTeam playerTeam) {}

    public void func_96513_c(ScorePlayerTeam playerTeam) {}

    /**
     * Returns 'list' for 0, 'sidebar' for 1, 'belowName for 2, otherwise null.
     */
    public static String getObjectiveDisplaySlot(int p_96517_0_)
    {
        switch (p_96517_0_)
        {
            case 0:
                return "list";
            case 1:
                return "sidebar";
            case 2:
                return "belowName";
            default:
                if (p_96517_0_ >= 3 && p_96517_0_ <= 18)
                {
                    EnumChatFormatting enumchatformatting = EnumChatFormatting.func_175744_a(p_96517_0_ - 3);

                    if (enumchatformatting != null && enumchatformatting != EnumChatFormatting.RESET)
                    {
                        return "sidebar.team." + enumchatformatting.getFriendlyName();
                    }
                }

                return null;
        }
    }

    /**
     * Returns 0 for (case-insensitive) 'list', 1 for 'sidebar', 2 for 'belowName', otherwise -1.
     */
    public static int getObjectiveDisplaySlotNumber(String p_96537_0_)
    {
        if (p_96537_0_.equalsIgnoreCase("list"))
        {
            return 0;
        }
        else if (p_96537_0_.equalsIgnoreCase("sidebar"))
        {
            return 1;
        }
        else if (p_96537_0_.equalsIgnoreCase("belowName"))
        {
            return 2;
        }
        else
        {
            if (p_96537_0_.startsWith("sidebar.team."))
            {
                String s1 = p_96537_0_.substring("sidebar.team.".length());
                EnumChatFormatting enumchatformatting = EnumChatFormatting.getValueByName(s1);

                if (enumchatformatting != null && enumchatformatting.getColorIndex() >= 0)
                {
                    return enumchatformatting.getColorIndex() + 3;
                }
            }

            return -1;
        }
    }

    public static String[] func_178821_h()
    {
        if (field_178823_g == null)
        {
            field_178823_g = new String[19];

            for (int i = 0; i < 19; ++i)
            {
                field_178823_g[i] = getObjectiveDisplaySlot(i);
            }
        }

        return field_178823_g;
    }
}