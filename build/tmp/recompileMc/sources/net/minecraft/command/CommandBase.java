package net.minecraft.command;

import com.google.common.base.Functions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.primitives.Doubles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;

public abstract class CommandBase implements ICommand
{
    private static IAdminCommand theAdmin;
    private static final String __OBFID = "CL_00001739";

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 4;
    }

    /**
     * Gets a list of aliases for this command
     */
    public List getAliases()
    {
        return Collections.emptyList();
    }

    /**
     * Returns true if the given command sender is allowed to execute this command
     */
    public boolean canCommandSenderUse(ICommandSender sender)
    {
        return sender.canUseCommand(this.getRequiredPermissionLevel(), this.getName());
    }

    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        return null;
    }

    public static int parseInt(String input) throws NumberInvalidException
    {
        try
        {
            return Integer.parseInt(input);
        }
        catch (NumberFormatException numberformatexception)
        {
            throw new NumberInvalidException("commands.generic.num.invalid", new Object[] {input});
        }
    }

    public static int parseInt(String input, int min) throws NumberInvalidException
    {
        return parseInt(input, min, Integer.MAX_VALUE);
    }

    public static int parseInt(String input, int min, int max) throws NumberInvalidException
    {
        int k = parseInt(input);

        if (k < min)
        {
            throw new NumberInvalidException("commands.generic.num.tooSmall", new Object[] {Integer.valueOf(k), Integer.valueOf(min)});
        }
        else if (k > max)
        {
            throw new NumberInvalidException("commands.generic.num.tooBig", new Object[] {Integer.valueOf(k), Integer.valueOf(max)});
        }
        else
        {
            return k;
        }
    }

    public static long parseLong(String input) throws NumberInvalidException
    {
        try
        {
            return Long.parseLong(input);
        }
        catch (NumberFormatException numberformatexception)
        {
            throw new NumberInvalidException("commands.generic.num.invalid", new Object[] {input});
        }
    }

    public static long parseLong(String input, long min, long max) throws NumberInvalidException
    {
        long k = parseLong(input);

        if (k < min)
        {
            throw new NumberInvalidException("commands.generic.num.tooSmall", new Object[] {Long.valueOf(k), Long.valueOf(min)});
        }
        else if (k > max)
        {
            throw new NumberInvalidException("commands.generic.num.tooBig", new Object[] {Long.valueOf(k), Long.valueOf(max)});
        }
        else
        {
            return k;
        }
    }

    public static BlockPos func_175757_a(ICommandSender sender, String[] args, int p_175757_2_, boolean p_175757_3_) throws NumberInvalidException
    {
        BlockPos blockpos = sender.getPosition();
        return new BlockPos(func_175769_b((double)blockpos.getX(), args[p_175757_2_], -30000000, 30000000, p_175757_3_), func_175769_b((double)blockpos.getY(), args[p_175757_2_ + 1], 0, 256, false), func_175769_b((double)blockpos.getZ(), args[p_175757_2_ + 2], -30000000, 30000000, p_175757_3_));
    }

    public static double parseDouble(String input) throws NumberInvalidException
    {
        try
        {
            double d0 = Double.parseDouble(input);

            if (!Doubles.isFinite(d0))
            {
                throw new NumberInvalidException("commands.generic.num.invalid", new Object[] {input});
            }
            else
            {
                return d0;
            }
        }
        catch (NumberFormatException numberformatexception)
        {
            throw new NumberInvalidException("commands.generic.num.invalid", new Object[] {input});
        }
    }

    public static double parseDouble(String input, double min) throws NumberInvalidException
    {
        return parseDouble(input, min, Double.MAX_VALUE);
    }

    public static double parseDouble(String input, double min, double max) throws NumberInvalidException
    {
        double d2 = parseDouble(input);

        if (d2 < min)
        {
            throw new NumberInvalidException("commands.generic.double.tooSmall", new Object[] {Double.valueOf(d2), Double.valueOf(min)});
        }
        else if (d2 > max)
        {
            throw new NumberInvalidException("commands.generic.double.tooBig", new Object[] {Double.valueOf(d2), Double.valueOf(max)});
        }
        else
        {
            return d2;
        }
    }

    public static boolean parseBoolean(String input) throws CommandException
    {
        if (!input.equals("true") && !input.equals("1"))
        {
            if (!input.equals("false") && !input.equals("0"))
            {
                throw new CommandException("commands.generic.boolean.invalid", new Object[] {input});
            }
            else
            {
                return false;
            }
        }
        else
        {
            return true;
        }
    }

    /**
     * Returns the given ICommandSender as a EntityPlayer or throw an exception.
     */
    public static EntityPlayerMP getCommandSenderAsPlayer(ICommandSender sender) throws PlayerNotFoundException
    {
        if (sender instanceof EntityPlayerMP)
        {
            return (EntityPlayerMP)sender;
        }
        else
        {
            throw new PlayerNotFoundException("You must specify which player you wish to perform this action on.", new Object[0]);
        }
    }

    public static EntityPlayerMP getPlayer(ICommandSender sender, String username) throws PlayerNotFoundException
    {
        EntityPlayerMP entityplayermp = PlayerSelector.matchOnePlayer(sender, username);

        if (entityplayermp == null)
        {
            try
            {
                entityplayermp = MinecraftServer.getServer().getConfigurationManager().getPlayerByUUID(UUID.fromString(username));
            }
            catch (IllegalArgumentException illegalargumentexception)
            {
                ;
            }
        }

        if (entityplayermp == null)
        {
            entityplayermp = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(username);
        }

        if (entityplayermp == null)
        {
            throw new PlayerNotFoundException();
        }
        else
        {
            return entityplayermp;
        }
    }

    public static Entity func_175768_b(ICommandSender p_175768_0_, String p_175768_1_) throws EntityNotFoundException
    {
        return func_175759_a(p_175768_0_, p_175768_1_, Entity.class);
    }

    public static Entity func_175759_a(ICommandSender p_175759_0_, String p_175759_1_, Class p_175759_2_) throws EntityNotFoundException
    {
        Object object = PlayerSelector.matchOneEntity(p_175759_0_, p_175759_1_, p_175759_2_);
        MinecraftServer minecraftserver = MinecraftServer.getServer();

        if (object == null)
        {
            object = minecraftserver.getConfigurationManager().getPlayerByUsername(p_175759_1_);
        }

        if (object == null)
        {
            try
            {
                UUID uuid = UUID.fromString(p_175759_1_);
                object = minecraftserver.getEntityFromUuid(uuid);

                if (object == null)
                {
                    object = minecraftserver.getConfigurationManager().getPlayerByUUID(uuid);
                }
            }
            catch (IllegalArgumentException illegalargumentexception)
            {
                throw new EntityNotFoundException("commands.generic.entity.invalidUuid", new Object[0]);
            }
        }

        if (object != null && p_175759_2_.isAssignableFrom(object.getClass()))
        {
            return (Entity)object;
        }
        else
        {
            throw new EntityNotFoundException();
        }
    }

    public static List func_175763_c(ICommandSender p_175763_0_, String p_175763_1_) throws EntityNotFoundException
    {
        return (List)(PlayerSelector.hasArguments(p_175763_1_) ? PlayerSelector.matchEntities(p_175763_0_, p_175763_1_, Entity.class) : Lists.newArrayList(new Entity[] {func_175768_b(p_175763_0_, p_175763_1_)}));
    }

    public static String getPlayerName(ICommandSender sender, String query) throws PlayerNotFoundException
    {
        try
        {
            return getPlayer(sender, query).getName();
        }
        catch (PlayerNotFoundException playernotfoundexception)
        {
            if (PlayerSelector.hasArguments(query))
            {
                throw playernotfoundexception;
            }
            else
            {
                return query;
            }
        }
    }

    /**
     * Attempts to retrieve an entity's name, first assuming that the entity is a player, and then exhausting all other
     * possibilities.
     */
    public static String getEntityName(ICommandSender p_175758_0_, String p_175758_1_) throws EntityNotFoundException
    {
        try
        {
            return getPlayer(p_175758_0_, p_175758_1_).getName();
        }
        catch (PlayerNotFoundException playernotfoundexception)
        {
            try
            {
                return func_175768_b(p_175758_0_, p_175758_1_).getUniqueID().toString();
            }
            catch (EntityNotFoundException entitynotfoundexception)
            {
                if (PlayerSelector.hasArguments(p_175758_1_))
                {
                    throw entitynotfoundexception;
                }
                else
                {
                    return p_175758_1_;
                }
            }
        }
    }

    public static IChatComponent getChatComponentFromNthArg(ICommandSender sender, String[] args, int p_147178_2_) throws CommandException
    {
        return getChatComponentFromNthArg(sender, args, p_147178_2_, false);
    }

    public static IChatComponent getChatComponentFromNthArg(ICommandSender sender, String[] args, int index, boolean p_147176_3_) throws PlayerNotFoundException
    {
        ChatComponentText chatcomponenttext = new ChatComponentText("");

        for (int j = index; j < args.length; ++j)
        {
            if (j > index)
            {
                chatcomponenttext.appendText(" ");
            }

            Object object = new ChatComponentText(args[j]);

            if (p_147176_3_)
            {
                IChatComponent ichatcomponent = PlayerSelector.func_150869_b(sender, args[j]);

                if (ichatcomponent == null)
                {
                    if (PlayerSelector.hasArguments(args[j]))
                    {
                        throw new PlayerNotFoundException();
                    }
                }
                else
                {
                    object = ichatcomponent;
                }
            }

            chatcomponenttext.appendSibling((IChatComponent)object);
        }

        return chatcomponenttext;
    }

    public static String func_180529_a(String[] p_180529_0_, int p_180529_1_)
    {
        StringBuilder stringbuilder = new StringBuilder();

        for (int j = p_180529_1_; j < p_180529_0_.length; ++j)
        {
            if (j > p_180529_1_)
            {
                stringbuilder.append(" ");
            }

            String s = p_180529_0_[j];
            stringbuilder.append(s);
        }

        return stringbuilder.toString();
    }

    public static CommandBase.CoordinateArg func_175770_a(double p_175770_0_, String p_175770_2_, boolean p_175770_3_) throws NumberInvalidException
    {
        return func_175767_a(p_175770_0_, p_175770_2_, -30000000, 30000000, p_175770_3_);
    }

    public static CommandBase.CoordinateArg func_175767_a(double p_175767_0_, String p_175767_2_, int p_175767_3_, int p_175767_4_, boolean p_175767_5_) throws NumberInvalidException
    {
        boolean flag1 = p_175767_2_.startsWith("~");

        if (flag1 && Double.isNaN(p_175767_0_))
        {
            throw new NumberInvalidException("commands.generic.num.invalid", new Object[] {Double.valueOf(p_175767_0_)});
        }
        else
        {
            double d1 = 0.0D;

            if (!flag1 || p_175767_2_.length() > 1)
            {
                boolean flag2 = p_175767_2_.contains(".");

                if (flag1)
                {
                    p_175767_2_ = p_175767_2_.substring(1);
                }

                d1 += parseDouble(p_175767_2_);

                if (!flag2 && !flag1 && p_175767_5_)
                {
                    d1 += 0.5D;
                }
            }

            if (p_175767_3_ != 0 || p_175767_4_ != 0)
            {
                if (d1 < (double)p_175767_3_)
                {
                    throw new NumberInvalidException("commands.generic.double.tooSmall", new Object[] {Double.valueOf(d1), Integer.valueOf(p_175767_3_)});
                }

                if (d1 > (double)p_175767_4_)
                {
                    throw new NumberInvalidException("commands.generic.double.tooBig", new Object[] {Double.valueOf(d1), Integer.valueOf(p_175767_4_)});
                }
            }

            return new CommandBase.CoordinateArg(d1 + (flag1 ? p_175767_0_ : 0.0D), d1, flag1);
        }
    }

    public static double func_175761_b(double p_175761_0_, String p_175761_2_, boolean p_175761_3_) throws NumberInvalidException
    {
        return func_175769_b(p_175761_0_, p_175761_2_, -30000000, 30000000, p_175761_3_);
    }

    public static double func_175769_b(double base, String input, int min, int max, boolean centerBlock) throws NumberInvalidException
    {
        boolean flag1 = input.startsWith("~");

        if (flag1 && Double.isNaN(base))
        {
            throw new NumberInvalidException("commands.generic.num.invalid", new Object[] {Double.valueOf(base)});
        }
        else
        {
            double d1 = flag1 ? base : 0.0D;

            if (!flag1 || input.length() > 1)
            {
                boolean flag2 = input.contains(".");

                if (flag1)
                {
                    input = input.substring(1);
                }

                d1 += parseDouble(input);

                if (!flag2 && !flag1 && centerBlock)
                {
                    d1 += 0.5D;
                }
            }

            if (min != 0 || max != 0)
            {
                if (d1 < (double)min)
                {
                    throw new NumberInvalidException("commands.generic.double.tooSmall", new Object[] {Double.valueOf(d1), Integer.valueOf(min)});
                }

                if (d1 > (double)max)
                {
                    throw new NumberInvalidException("commands.generic.double.tooBig", new Object[] {Double.valueOf(d1), Integer.valueOf(max)});
                }
            }

            return d1;
        }
    }

    /**
     * Gets the Item specified by the given text string.  First checks the item registry, then tries by parsing the
     * string as an integer ID (deprecated).  Warns the sender if we matched by parsing the ID.  Throws if the item
     * wasn't found.  Returns the item if it was found.
     */
    public static Item getItemByText(ICommandSender sender, String id) throws NumberInvalidException
    {
        ResourceLocation resourcelocation = new ResourceLocation(id);
        Item item = (Item)Item.itemRegistry.getObject(resourcelocation);

        if (item == null)
        {
            throw new NumberInvalidException("commands.give.notFound", new Object[] {resourcelocation});
        }
        else
        {
            return item;
        }
    }

    /**
     * Gets the Block specified by the given text string.  First checks the block registry, then tries by parsing the
     * string as an integer ID (deprecated).  Warns the sender if we matched by parsing the ID.  Throws if the block
     * wasn't found.  Returns the block if it was found.
     */
    public static Block getBlockByText(ICommandSender sender, String id) throws NumberInvalidException
    {
        ResourceLocation resourcelocation = new ResourceLocation(id);

        if (!Block.blockRegistry.containsKey(resourcelocation))
        {
            throw new NumberInvalidException("commands.give.notFound", new Object[] {resourcelocation});
        }
        else
        {
            Block block = (Block)Block.blockRegistry.getObject(resourcelocation);

            if (block == null)
            {
                throw new NumberInvalidException("commands.give.notFound", new Object[] {resourcelocation});
            }
            else
            {
                return block;
            }
        }
    }

    /**
     * Creates a linguistic series joining the input objects together.  Examples: 1) {} --> "",  2) {"Steve"} -->
     * "Steve",  3) {"Steve", "Phil"} --> "Steve and Phil",  4) {"Steve", "Phil", "Mark"} --> "Steve, Phil and Mark"
     */
    public static String joinNiceString(Object[] elements)
    {
        StringBuilder stringbuilder = new StringBuilder();

        for (int i = 0; i < elements.length; ++i)
        {
            String s = elements[i].toString();

            if (i > 0)
            {
                if (i == elements.length - 1)
                {
                    stringbuilder.append(" and ");
                }
                else
                {
                    stringbuilder.append(", ");
                }
            }

            stringbuilder.append(s);
        }

        return stringbuilder.toString();
    }

    public static IChatComponent join(List components)
    {
        ChatComponentText chatcomponenttext = new ChatComponentText("");

        for (int i = 0; i < components.size(); ++i)
        {
            if (i > 0)
            {
                if (i == components.size() - 1)
                {
                    chatcomponenttext.appendText(" and ");
                }
                else if (i > 0)
                {
                    chatcomponenttext.appendText(", ");
                }
            }

            chatcomponenttext.appendSibling((IChatComponent)components.get(i));
        }

        return chatcomponenttext;
    }

    /**
     * Creates a linguistic series joining together the elements of the given collection.  Examples: 1) {} --> "",  2)
     * {"Steve"} --> "Steve",  3) {"Steve", "Phil"} --> "Steve and Phil",  4) {"Steve", "Phil", "Mark"} --> "Steve, Phil
     * and Mark"
     */
    public static String joinNiceStringFromCollection(Collection strings)
    {
        /**
         * Creates a linguistic series joining the input objects together.  Examples: 1) {} --> "",  2) {"Steve"} -->
         * "Steve",  3) {"Steve", "Phil"} --> "Steve and Phil",  4) {"Steve", "Phil", "Mark"} --> "Steve, Phil and Mark"
         */
        return joinNiceString(strings.toArray(new String[strings.size()]));
    }

    public static List func_175771_a(String[] p_175771_0_, int p_175771_1_, BlockPos p_175771_2_)
    {
        if (p_175771_2_ == null)
        {
            return null;
        }
        else
        {
            String s;

            if (p_175771_0_.length - 1 == p_175771_1_)
            {
                s = Integer.toString(p_175771_2_.getX());
            }
            else if (p_175771_0_.length - 1 == p_175771_1_ + 1)
            {
                s = Integer.toString(p_175771_2_.getY());
            }
            else
            {
                if (p_175771_0_.length - 1 != p_175771_1_ + 2)
                {
                    return null;
                }

                s = Integer.toString(p_175771_2_.getZ());
            }

            return Lists.newArrayList(new String[] {s});
        }
    }

    /**
     * Returns true if the given substring is exactly equal to the start of the given string (case insensitive).
     */
    public static boolean doesStringStartWith(String original, String region)
    {
        return region.regionMatches(true, 0, original, 0, original.length());
    }

    /**
     * Returns a List of strings (chosen from the given strings) which the last word in the given string array is a
     * beginning-match for. (Tab completion).
     */
    public static List getListOfStringsMatchingLastWord(String[] args, String ... possibilities)
    {
        return func_175762_a(args, Arrays.asList(possibilities));
    }

    public static List func_175762_a(String[] p_175762_0_, Collection p_175762_1_)
    {
        String s = p_175762_0_[p_175762_0_.length - 1];
        ArrayList arraylist = Lists.newArrayList();

        if (!p_175762_1_.isEmpty())
        {
            Iterator iterator = Iterables.transform(p_175762_1_, Functions.toStringFunction()).iterator();

            while (iterator.hasNext())
            {
                String s1 = (String)iterator.next();

                if (doesStringStartWith(s, s1))
                {
                    arraylist.add(s1);
                }
            }

            if (arraylist.isEmpty())
            {
                iterator = p_175762_1_.iterator();

                while (iterator.hasNext())
                {
                    Object object = iterator.next();

                    if (object instanceof ResourceLocation && doesStringStartWith(s, ((ResourceLocation)object).getResourcePath()))
                    {
                        arraylist.add(String.valueOf(object));
                    }
                }
            }
        }

        return arraylist;
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] args, int index)
    {
        return false;
    }

    public static void notifyOperators(ICommandSender sender, ICommand command, String msgFormat, Object ... msgParams)
    {
        notifyOperators(sender, command, 0, msgFormat, msgParams);
    }

    public static void notifyOperators(ICommandSender sender, ICommand command, int p_152374_2_, String msgFormat, Object ... msgParams)
    {
        if (theAdmin != null)
        {
            theAdmin.notifyOperators(sender, command, p_152374_2_, msgFormat, msgParams);
        }
    }

    /**
     * Sets the static IAdminCommander.
     */
    public static void setAdminCommander(IAdminCommand command)
    {
        theAdmin = command;
    }

    public int compareTo(ICommand p_compareTo_1_)
    {
        return this.getName().compareTo(p_compareTo_1_.getName());
    }

    public int compareTo(Object p_compareTo_1_)
    {
        return this.compareTo((ICommand)p_compareTo_1_);
    }

    public static class CoordinateArg
        {
            private final double field_179633_a;
            private final double field_179631_b;
            private final boolean field_179632_c;
            private static final String __OBFID = "CL_00002365";

            protected CoordinateArg(double p_i46051_1_, double p_i46051_3_, boolean p_i46051_5_)
            {
                this.field_179633_a = p_i46051_1_;
                this.field_179631_b = p_i46051_3_;
                this.field_179632_c = p_i46051_5_;
            }

            public double func_179628_a()
            {
                return this.field_179633_a;
            }

            public double func_179629_b()
            {
                return this.field_179631_b;
            }

            public boolean func_179630_c()
            {
                return this.field_179632_c;
            }
        }
}