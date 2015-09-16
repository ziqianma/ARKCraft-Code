package net.minecraft.command;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;

public class PlayerSelector
{
    /** This matches the at-tokens introduced for command blocks, including their arguments, if any. */
    private static final Pattern tokenPattern = Pattern.compile("^@([pare])(?:\\[([\\w=,!-]*)\\])?$");
    /** This matches things like "-1,,4", and is used for getting x,y,z,range from the token's argument list. */
    private static final Pattern intListPattern = Pattern.compile("\\G([-!]?[\\w-]*)(?:$|,)");
    /** This matches things like "rm=4,c=2" and is used for handling named token arguments. */
    private static final Pattern keyValueListPattern = Pattern.compile("\\G(\\w+)=([-!]?[\\w-]*)(?:$|,)");
    private static final Set field_179666_d = Sets.newHashSet(new String[] {"x", "y", "z", "dx", "dy", "dz", "rm", "r"});
    private static final String __OBFID = "CL_00000086";

    /**
     * Returns the one player that matches the given at-token.  Returns null if more than one player matches.
     */
    public static EntityPlayerMP matchOnePlayer(ICommandSender p_82386_0_, String p_82386_1_)
    {
        return (EntityPlayerMP)matchOneEntity(p_82386_0_, p_82386_1_, EntityPlayerMP.class);
    }

    /**
     * Returns one entity of the given class that matches the given at-token. Returns null if more than one entity
     * matches.
     */
    public static Entity matchOneEntity(ICommandSender p_179652_0_, String p_179652_1_, Class p_179652_2_)
    {
        List list = matchEntities(p_179652_0_, p_179652_1_, p_179652_2_);
        return list.size() == 1 ? (Entity)list.get(0) : null;
    }

    public static IChatComponent func_150869_b(ICommandSender p_150869_0_, String p_150869_1_)
    {
        List list = matchEntities(p_150869_0_, p_150869_1_, Entity.class);

        if (list.isEmpty())
        {
            return null;
        }
        else
        {
            ArrayList arraylist = Lists.newArrayList();
            Iterator iterator = list.iterator();

            while (iterator.hasNext())
            {
                Entity entity = (Entity)iterator.next();
                arraylist.add(entity.getDisplayName());
            }

            return CommandBase.join(arraylist);
        }
    }

    /**
     * Returns all entities of the given class that matches the given at-token in a list.
     */
    public static List matchEntities(ICommandSender p_179656_0_, String p_179656_1_, Class p_179656_2_)
    {
        Matcher matcher = tokenPattern.matcher(p_179656_1_);

        if (matcher.matches() && p_179656_0_.canUseCommand(1, "@"))
        {
            Map map = getArgumentMap(matcher.group(2));

            if (!func_179655_b(p_179656_0_, map))
            {
                return Collections.emptyList();
            }
            else
            {
                String s1 = matcher.group(1);
                BlockPos blockpos = func_179664_b(map, p_179656_0_.getPosition());
                List list = func_179654_a(p_179656_0_, map);
                ArrayList arraylist = Lists.newArrayList();
                Iterator iterator = list.iterator();

                while (iterator.hasNext())
                {
                    World world = (World)iterator.next();

                    if (world != null)
                    {
                        ArrayList arraylist1 = Lists.newArrayList();
                        arraylist1.addAll(func_179663_a(map, s1));
                        arraylist1.addAll(func_179648_b(map));
                        arraylist1.addAll(func_179649_c(map));
                        arraylist1.addAll(func_179659_d(map));
                        arraylist1.addAll(func_179657_e(map));
                        arraylist1.addAll(func_179647_f(map));
                        arraylist1.addAll(func_180698_a(map, blockpos));
                        arraylist1.addAll(func_179662_g(map));
                        arraylist.addAll(func_179660_a(map, p_179656_2_, arraylist1, s1, world, blockpos));
                    }
                }

                return func_179658_a(arraylist, map, p_179656_0_, p_179656_2_, s1, blockpos);
            }
        }
        else
        {
            return Collections.emptyList();
        }
    }

    private static List func_179654_a(ICommandSender p_179654_0_, Map p_179654_1_)
    {
        ArrayList arraylist = Lists.newArrayList();

        if (func_179665_h(p_179654_1_))
        {
            arraylist.add(p_179654_0_.getEntityWorld());
        }
        else
        {
            Collections.addAll(arraylist, MinecraftServer.getServer().worldServers);
        }

        return arraylist;
    }

    private static boolean func_179655_b(ICommandSender p_179655_0_, Map p_179655_1_)
    {
        String s = func_179651_b(p_179655_1_, "type");
        s = s != null && s.startsWith("!") ? s.substring(1) : s;

        if (s != null && !EntityList.isStringValidEntityName(s))
        {
            ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("commands.generic.entity.invalidType", new Object[] {s});
            chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.RED);
            p_179655_0_.addChatMessage(chatcomponenttranslation);
            return false;
        }
        else
        {
            return true;
        }
    }

    private static List func_179663_a(Map p_179663_0_, String p_179663_1_)
    {
        ArrayList arraylist = Lists.newArrayList();
        String s1 = func_179651_b(p_179663_0_, "type");
        final boolean flag = s1 != null && s1.startsWith("!");

        if (flag)
        {
            s1 = s1.substring(1);
        }

        boolean flag1 = !p_179663_1_.equals("e");
        boolean flag2 = p_179663_1_.equals("r") && s1 != null;

        if ((s1 == null || !p_179663_1_.equals("e")) && !flag2)
        {
            if (flag1)
            {
                arraylist.add(new Predicate()
                {
                    private static final String __OBFID = "CL_00002358";
                    public boolean func_179624_a(Entity p_179624_1_)
                    {
                        return p_179624_1_ instanceof EntityPlayer;
                    }
                    public boolean apply(Object p_apply_1_)
                    {
                        return this.func_179624_a((Entity)p_apply_1_);
                    }
                });
            }
        }
        else
        {
            final String s1_f = s1;
            arraylist.add(new Predicate()
            {
                private static final String __OBFID = "CL_00002362";
                public boolean func_179613_a(Entity p_179613_1_)
                {
                    return EntityList.isStringEntityName(p_179613_1_, s1_f) != flag;
                }
                public boolean apply(Object p_apply_1_)
                {
                    return this.func_179613_a((Entity)p_apply_1_);
                }
            });
        }

        return arraylist;
    }

    private static List func_179648_b(Map p_179648_0_)
    {
        ArrayList arraylist = Lists.newArrayList();
        final int i = func_179653_a(p_179648_0_, "lm", -1);
        final int j = func_179653_a(p_179648_0_, "l", -1);

        if (i > -1 || j > -1)
        {
            arraylist.add(new Predicate()
            {
                private static final String __OBFID = "CL_00002357";
                public boolean func_179625_a(Entity p_179625_1_)
                {
                    if (!(p_179625_1_ instanceof EntityPlayerMP))
                    {
                        return false;
                    }
                    else
                    {
                        EntityPlayerMP entityplayermp = (EntityPlayerMP)p_179625_1_;
                        return (i <= -1 || entityplayermp.experienceLevel >= i) && (j <= -1 || entityplayermp.experienceLevel <= j);
                    }
                }
                public boolean apply(Object p_apply_1_)
                {
                    return this.func_179625_a((Entity)p_apply_1_);
                }
            });
        }

        return arraylist;
    }

    private static List func_179649_c(Map p_179649_0_)
    {
        ArrayList arraylist = Lists.newArrayList();
        final int i = func_179653_a(p_179649_0_, "m", WorldSettings.GameType.NOT_SET.getID());

        if (i != WorldSettings.GameType.NOT_SET.getID())
        {
            arraylist.add(new Predicate()
            {
                private static final String __OBFID = "CL_00002356";
                public boolean func_179619_a(Entity p_179619_1_)
                {
                    if (!(p_179619_1_ instanceof EntityPlayerMP))
                    {
                        return false;
                    }
                    else
                    {
                        EntityPlayerMP entityplayermp = (EntityPlayerMP)p_179619_1_;
                        return entityplayermp.theItemInWorldManager.getGameType().getID() == i;
                    }
                }
                public boolean apply(Object p_apply_1_)
                {
                    return this.func_179619_a((Entity)p_apply_1_);
                }
            });
        }

        return arraylist;
    }

    private static List func_179659_d(Map p_179659_0_)
    {
        ArrayList arraylist = Lists.newArrayList();
        String s = func_179651_b(p_179659_0_, "team");
        final boolean flag = s != null && s.startsWith("!");

        if (flag)
        {
            s = s.substring(1);
        }

        if (s != null)
        {
            final String s_f = s;
            arraylist.add(new Predicate()
            {
                private static final String __OBFID = "CL_00002355";
                public boolean func_179621_a(Entity p_179621_1_)
                {
                    if (!(p_179621_1_ instanceof EntityLivingBase))
                    {
                        return false;
                    }
                    else
                    {
                        EntityLivingBase entitylivingbase = (EntityLivingBase)p_179621_1_;
                        Team team = entitylivingbase.getTeam();
                        String s1 = team == null ? "" : team.getRegisteredName();
                        return s1.equals(s_f) != flag;
                    }
                }
                public boolean apply(Object p_apply_1_)
                {
                    return this.func_179621_a((Entity)p_apply_1_);
                }
            });
        }

        return arraylist;
    }

    private static List func_179657_e(Map p_179657_0_)
    {
        ArrayList arraylist = Lists.newArrayList();
        final Map map1 = func_96560_a(p_179657_0_);

        if (map1 != null && map1.size() > 0)
        {
            arraylist.add(new Predicate()
            {
                private static final String __OBFID = "CL_00002354";
                public boolean func_179603_a(Entity p_179603_1_)
                {
                    Scoreboard scoreboard = MinecraftServer.getServer().worldServerForDimension(0).getScoreboard();
                    Iterator iterator = map1.entrySet().iterator();
                    Entry entry;
                    boolean flag;
                    int i;

                    do
                    {
                        if (!iterator.hasNext())
                        {
                            return true;
                        }

                        entry = (Entry)iterator.next();
                        String s = (String)entry.getKey();
                        flag = false;

                        if (s.endsWith("_min") && s.length() > 4)
                        {
                            flag = true;
                            s = s.substring(0, s.length() - 4);
                        }

                        ScoreObjective scoreobjective = scoreboard.getObjective(s);

                        if (scoreobjective == null)
                        {
                            return false;
                        }

                        String s1 = p_179603_1_ instanceof EntityPlayerMP ? p_179603_1_.getName() : p_179603_1_.getUniqueID().toString();

                        if (!scoreboard.entityHasObjective(s1, scoreobjective))
                        {
                            return false;
                        }

                        Score score = scoreboard.getValueFromObjective(s1, scoreobjective);
                        i = score.getScorePoints();

                        if (i < ((Integer)entry.getValue()).intValue() && flag)
                        {
                            return false;
                        }
                    }
                    while (i <= ((Integer)entry.getValue()).intValue() || flag);

                    return false;
                }
                public boolean apply(Object p_apply_1_)
                {
                    return this.func_179603_a((Entity)p_apply_1_);
                }
            });
        }

        return arraylist;
    }

    private static List func_179647_f(Map p_179647_0_)
    {
        ArrayList arraylist = Lists.newArrayList();
        String s = func_179651_b(p_179647_0_, "name");
        final boolean flag = s != null && s.startsWith("!");

        if (flag)
        {
            s = s.substring(1);
        }

        if (s != null)
        {
            final String s_f = s;
            arraylist.add(new Predicate()
            {
                private static final String __OBFID = "CL_00002353";
                public boolean func_179600_a(Entity p_179600_1_)
                {
                    return p_179600_1_.getName().equals(s_f) != flag;
                }
                public boolean apply(Object p_apply_1_)
                {
                    return this.func_179600_a((Entity)p_apply_1_);
                }
            });
        }

        return arraylist;
    }

    private static List func_180698_a(Map p_180698_0_, final BlockPos p_180698_1_)
    {
        ArrayList arraylist = Lists.newArrayList();
        final int i = func_179653_a(p_180698_0_, "rm", -1);
        final int j = func_179653_a(p_180698_0_, "r", -1);

        if (p_180698_1_ != null && (i >= 0 || j >= 0))
        {
            final int k = i * i;
            final int l = j * j;
            arraylist.add(new Predicate()
            {
                private static final String __OBFID = "CL_00002352";
                public boolean func_179594_a(Entity p_179594_1_)
                {
                    int i1 = (int)p_179594_1_.getDistanceSqToCenter(p_180698_1_);
                    return (i < 0 || i1 >= k) && (j < 0 || i1 <= l);
                }
                public boolean apply(Object p_apply_1_)
                {
                    return this.func_179594_a((Entity)p_apply_1_);
                }
            });
        }

        return arraylist;
    }

    private static List func_179662_g(Map p_179662_0_)
    {
        ArrayList arraylist = Lists.newArrayList();

        if (p_179662_0_.containsKey("rym") || p_179662_0_.containsKey("ry"))
        {
            final int i = func_179650_a(func_179653_a(p_179662_0_, "rym", 0));
            final int j = func_179650_a(func_179653_a(p_179662_0_, "ry", 359));
            arraylist.add(new Predicate()
            {
                private static final String __OBFID = "CL_00002351";
                public boolean func_179591_a(Entity p_179591_1_)
                {
                    int k = PlayerSelector.func_179650_a((int)Math.floor((double)p_179591_1_.rotationYaw));
                    return i > j ? k >= i || k <= j : k >= i && k <= j;
                }
                public boolean apply(Object p_apply_1_)
                {
                    return this.func_179591_a((Entity)p_apply_1_);
                }
            });
        }

        if (p_179662_0_.containsKey("rxm") || p_179662_0_.containsKey("rx"))
        {
            final int i = func_179650_a(func_179653_a(p_179662_0_, "rxm", 0));
            final int j = func_179650_a(func_179653_a(p_179662_0_, "rx", 359));
            arraylist.add(new Predicate()
            {
                private static final String __OBFID = "CL_00002361";
                public boolean func_179616_a(Entity p_179616_1_)
                {
                    int k = PlayerSelector.func_179650_a((int)Math.floor((double)p_179616_1_.rotationPitch));
                    return i > j ? k >= i || k <= j : k >= i && k <= j;
                }
                public boolean apply(Object p_apply_1_)
                {
                    return this.func_179616_a((Entity)p_apply_1_);
                }
            });
        }

        return arraylist;
    }

    private static List func_179660_a(Map p_179660_0_, Class p_179660_1_, List p_179660_2_, String p_179660_3_, World worldIn, BlockPos p_179660_5_)
    {
        ArrayList arraylist = Lists.newArrayList();
        String s1 = func_179651_b(p_179660_0_, "type");
        s1 = s1 != null && s1.startsWith("!") ? s1.substring(1) : s1;
        boolean flag = !p_179660_3_.equals("e");
        boolean flag1 = p_179660_3_.equals("r") && s1 != null;
        int i = func_179653_a(p_179660_0_, "dx", 0);
        int j = func_179653_a(p_179660_0_, "dy", 0);
        int k = func_179653_a(p_179660_0_, "dz", 0);
        int l = func_179653_a(p_179660_0_, "r", -1);
        Predicate predicate = Predicates.and(p_179660_2_);
        Predicate predicate1 = Predicates.and(IEntitySelector.selectAnything, predicate);

        if (p_179660_5_ != null)
        {
            int i1 = worldIn.playerEntities.size();
            int j1 = worldIn.loadedEntityList.size();
            boolean flag2 = i1 < j1 / 16;
            final AxisAlignedBB axisalignedbb;

            if (!p_179660_0_.containsKey("dx") && !p_179660_0_.containsKey("dy") && !p_179660_0_.containsKey("dz"))
            {
                if (l >= 0)
                {
                    axisalignedbb = new AxisAlignedBB((double)(p_179660_5_.getX() - l), (double)(p_179660_5_.getY() - l), (double)(p_179660_5_.getZ() - l), (double)(p_179660_5_.getX() + l + 1), (double)(p_179660_5_.getY() + l + 1), (double)(p_179660_5_.getZ() + l + 1));

                    if (flag && flag2 && !flag1)
                    {
                        arraylist.addAll(worldIn.getPlayers(p_179660_1_, predicate1));
                    }
                    else
                    {
                        arraylist.addAll(worldIn.getEntitiesWithinAABB(p_179660_1_, axisalignedbb, predicate1));
                    }
                }
                else if (p_179660_3_.equals("a"))
                {
                    arraylist.addAll(worldIn.getPlayers(p_179660_1_, predicate));
                }
                else if (!p_179660_3_.equals("p") && (!p_179660_3_.equals("r") || flag1))
                {
                    arraylist.addAll(worldIn.getEntities(p_179660_1_, predicate1));
                }
                else
                {
                    arraylist.addAll(worldIn.getPlayers(p_179660_1_, predicate1));
                }
            }
            else
            {
                axisalignedbb = func_179661_a(p_179660_5_, i, j, k);

                if (flag && flag2 && !flag1)
                {
                    Predicate predicate2 = new Predicate()
                    {
                        private static final String __OBFID = "CL_00002360";
                        public boolean func_179609_a(Entity p_179609_1_)
                        {
                            return p_179609_1_.posX >= axisalignedbb.minX && p_179609_1_.posY >= axisalignedbb.minY && p_179609_1_.posZ >= axisalignedbb.minZ ? p_179609_1_.posX < axisalignedbb.maxX && p_179609_1_.posY < axisalignedbb.maxY && p_179609_1_.posZ < axisalignedbb.maxZ : false;
                        }
                        public boolean apply(Object p_apply_1_)
                        {
                            return this.func_179609_a((Entity)p_apply_1_);
                        }
                    };
                    arraylist.addAll(worldIn.getPlayers(p_179660_1_, Predicates.and(predicate1, predicate2)));
                }
                else
                {
                    arraylist.addAll(worldIn.getEntitiesWithinAABB(p_179660_1_, axisalignedbb, predicate1));
                }
            }
        }
        else if (p_179660_3_.equals("a"))
        {
            arraylist.addAll(worldIn.getPlayers(p_179660_1_, predicate));
        }
        else if (!p_179660_3_.equals("p") && (!p_179660_3_.equals("r") || flag1))
        {
            arraylist.addAll(worldIn.getEntities(p_179660_1_, predicate1));
        }
        else
        {
            arraylist.addAll(worldIn.getPlayers(p_179660_1_, predicate1));
        }

        return arraylist;
    }

    private static List func_179658_a(List p_179658_0_, Map p_179658_1_, ICommandSender p_179658_2_, Class p_179658_3_, String p_179658_4_, final BlockPos p_179658_5_)
    {
        int i = func_179653_a(p_179658_1_, "c", !p_179658_4_.equals("a") && !p_179658_4_.equals("e") ? 1 : 0);

        if (!p_179658_4_.equals("p") && !p_179658_4_.equals("a") && !p_179658_4_.equals("e"))
        {
            if (p_179658_4_.equals("r"))
            {
                Collections.shuffle((List)p_179658_0_);
            }
        }
        else if (p_179658_5_ != null)
        {
            Collections.sort((List)p_179658_0_, new Comparator()
            {
                private static final String __OBFID = "CL_00002359";
                public int func_179611_a(Entity p_179611_1_, Entity p_179611_2_)
                {
                    return ComparisonChain.start().compare(p_179611_1_.getDistanceSq(p_179658_5_), p_179611_2_.getDistanceSq(p_179658_5_)).result();
                }
                public int compare(Object p_compare_1_, Object p_compare_2_)
                {
                    return this.func_179611_a((Entity)p_compare_1_, (Entity)p_compare_2_);
                }
            });
        }

        Entity entity = p_179658_2_.getCommandSenderEntity();

        if (entity != null && p_179658_3_.isAssignableFrom(entity.getClass()) && i == 1 && ((List)p_179658_0_).contains(entity) && !"r".equals(p_179658_4_))
        {
            p_179658_0_ = Lists.newArrayList(new Entity[] {entity});
        }

        if (i != 0)
        {
            if (i < 0)
            {
                Collections.reverse((List)p_179658_0_);
            }

            p_179658_0_ = ((List)p_179658_0_).subList(0, Math.min(Math.abs(i), ((List)p_179658_0_).size()));
        }

        return (List)p_179658_0_;
    }

    private static AxisAlignedBB func_179661_a(BlockPos p_179661_0_, int p_179661_1_, int p_179661_2_, int p_179661_3_)
    {
        boolean flag = p_179661_1_ < 0;
        boolean flag1 = p_179661_2_ < 0;
        boolean flag2 = p_179661_3_ < 0;
        int l = p_179661_0_.getX() + (flag ? p_179661_1_ : 0);
        int i1 = p_179661_0_.getY() + (flag1 ? p_179661_2_ : 0);
        int j1 = p_179661_0_.getZ() + (flag2 ? p_179661_3_ : 0);
        int k1 = p_179661_0_.getX() + (flag ? 0 : p_179661_1_) + 1;
        int l1 = p_179661_0_.getY() + (flag1 ? 0 : p_179661_2_) + 1;
        int i2 = p_179661_0_.getZ() + (flag2 ? 0 : p_179661_3_) + 1;
        return new AxisAlignedBB((double)l, (double)i1, (double)j1, (double)k1, (double)l1, (double)i2);
    }

    public static int func_179650_a(int p_179650_0_)
    {
        p_179650_0_ %= 360;

        if (p_179650_0_ >= 160)
        {
            p_179650_0_ -= 360;
        }

        if (p_179650_0_ < 0)
        {
            p_179650_0_ += 360;
        }

        return p_179650_0_;
    }

    private static BlockPos func_179664_b(Map p_179664_0_, BlockPos p_179664_1_)
    {
        return new BlockPos(func_179653_a(p_179664_0_, "x", p_179664_1_.getX()), func_179653_a(p_179664_0_, "y", p_179664_1_.getY()), func_179653_a(p_179664_0_, "z", p_179664_1_.getZ()));
    }

    private static boolean func_179665_h(Map p_179665_0_)
    {
        Iterator iterator = field_179666_d.iterator();
        String s;

        do
        {
            if (!iterator.hasNext())
            {
                return false;
            }

            s = (String)iterator.next();
        }
        while (!p_179665_0_.containsKey(s));

        return true;
    }

    private static int func_179653_a(Map p_179653_0_, String p_179653_1_, int p_179653_2_)
    {
        return p_179653_0_.containsKey(p_179653_1_) ? MathHelper.parseIntWithDefault((String)p_179653_0_.get(p_179653_1_), p_179653_2_) : p_179653_2_;
    }

    private static String func_179651_b(Map p_179651_0_, String p_179651_1_)
    {
        return (String)p_179651_0_.get(p_179651_1_);
    }

    public static Map func_96560_a(Map p_96560_0_)
    {
        HashMap hashmap = Maps.newHashMap();
        Iterator iterator = p_96560_0_.keySet().iterator();

        while (iterator.hasNext())
        {
            String s = (String)iterator.next();

            if (s.startsWith("score_") && s.length() > "score_".length())
            {
                hashmap.put(s.substring("score_".length()), Integer.valueOf(MathHelper.parseIntWithDefault((String)p_96560_0_.get(s), 1)));
            }
        }

        return hashmap;
    }

    /**
     * Returns whether the given pattern can match more than one player.
     */
    public static boolean matchesMultiplePlayers(String p_82377_0_)
    {
        Matcher matcher = tokenPattern.matcher(p_82377_0_);

        if (!matcher.matches())
        {
            return false;
        }
        else
        {
            Map map = getArgumentMap(matcher.group(2));
            String s1 = matcher.group(1);
            int i = !"a".equals(s1) && !"e".equals(s1) ? 1 : 0;
            return func_179653_a(map, "c", i) != 1;
        }
    }

    /**
     * Returns whether the given token has any arguments set.
     */
    public static boolean hasArguments(String p_82378_0_)
    {
        return tokenPattern.matcher(p_82378_0_).matches();
    }

    /**
     * Parses the given argument string, turning it into a HashMap&lt;String, String&gt; of name-&gt;value.
     */
    private static Map getArgumentMap(String p_82381_0_)
    {
        HashMap hashmap = Maps.newHashMap();

        if (p_82381_0_ == null)
        {
            return hashmap;
        }
        else
        {
            int i = 0;
            int j = -1;

            for (Matcher matcher = intListPattern.matcher(p_82381_0_); matcher.find(); j = matcher.end())
            {
                String s1 = null;

                switch (i++)
                {
                    case 0:
                        s1 = "x";
                        break;
                    case 1:
                        s1 = "y";
                        break;
                    case 2:
                        s1 = "z";
                        break;
                    case 3:
                        s1 = "r";
                }

                if (s1 != null && matcher.group(1).length() > 0)
                {
                    hashmap.put(s1, matcher.group(1));
                }
            }

            if (j < p_82381_0_.length())
            {
                Matcher matcher1 = keyValueListPattern.matcher(j == -1 ? p_82381_0_ : p_82381_0_.substring(j));

                while (matcher1.find())
                {
                    hashmap.put(matcher1.group(1), matcher1.group(2));
                }
            }

            return hashmap;
        }
    }
}