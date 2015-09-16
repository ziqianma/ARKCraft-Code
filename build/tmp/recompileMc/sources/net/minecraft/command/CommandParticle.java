package net.minecraft.command;

import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class CommandParticle extends CommandBase
{
    private static final String __OBFID = "CL_00002341";

    /**
     * Get the name of the command
     */
    public String getName()
    {
        return "particle";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.particle.usage";
    }

    /**
     * Called when a CommandSender executes this command
     */
    public void execute(ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 8)
        {
            throw new WrongUsageException("commands.particle.usage", new Object[0]);
        }
        else
        {
            boolean flag = false;
            EnumParticleTypes enumparticletypes = null;
            EnumParticleTypes[] aenumparticletypes = EnumParticleTypes.values();
            int i = aenumparticletypes.length;

            for (int j = 0; j < i; ++j)
            {
                EnumParticleTypes enumparticletypes1 = aenumparticletypes[j];

                if (enumparticletypes1.func_179343_f())
                {
                    if (args[0].startsWith(enumparticletypes1.getParticleName()))
                    {
                        flag = true;
                        enumparticletypes = enumparticletypes1;
                        break;
                    }
                }
                else if (args[0].equals(enumparticletypes1.getParticleName()))
                {
                    flag = true;
                    enumparticletypes = enumparticletypes1;
                    break;
                }
            }

            if (!flag)
            {
                throw new CommandException("commands.particle.notFound", new Object[] {args[0]});
            }
            else
            {
                String s = args[0];
                Vec3 vec3 = sender.getPositionVector();
                double d6 = (double)((float)func_175761_b(vec3.xCoord, args[1], true));
                double d0 = (double)((float)func_175761_b(vec3.yCoord, args[2], true));
                double d1 = (double)((float)func_175761_b(vec3.zCoord, args[3], true));
                double d2 = (double)((float)parseDouble(args[4]));
                double d3 = (double)((float)parseDouble(args[5]));
                double d4 = (double)((float)parseDouble(args[6]));
                double d5 = (double)((float)parseDouble(args[7]));
                int k = 0;

                if (args.length > 8)
                {
                    k = parseInt(args[8], 0);
                }

                boolean flag1 = false;

                if (args.length > 9 && "force".equals(args[9]))
                {
                    flag1 = true;
                }

                World world = sender.getEntityWorld();

                if (world instanceof WorldServer)
                {
                    WorldServer worldserver = (WorldServer)world;
                    int[] aint = new int[enumparticletypes.getArgumentCount()];

                    if (enumparticletypes.func_179343_f())
                    {
                        String[] astring1 = args[0].split("_", 3);

                        for (int l = 1; l < astring1.length; ++l)
                        {
                            try
                            {
                                aint[l - 1] = Integer.parseInt(astring1[l]);
                            }
                            catch (NumberFormatException numberformatexception)
                            {
                                throw new CommandException("commands.particle.notFound", new Object[] {args[0]});
                            }
                        }
                    }

                    worldserver.spawnParticle(enumparticletypes, flag1, d6, d0, d1, k, d2, d3, d4, d5, aint);
                    notifyOperators(sender, this, "commands.particle.success", new Object[] {s, Integer.valueOf(Math.max(k, 1))});
                }
            }
        }
    }

    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, EnumParticleTypes.func_179349_a()) : (args.length > 1 && args.length <= 4 ? func_175771_a(args, 1, pos) : (args.length == 9 ? getListOfStringsMatchingLastWord(args, new String[] {"normal", "force"}): null));
    }
}