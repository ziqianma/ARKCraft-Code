package net.minecraft.command.server;

import java.util.EnumSet;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class CommandTeleport extends CommandBase
{
    private static final String __OBFID = "CL_00001180";

    /**
     * Get the name of the command
     */
    public String getName()
    {
        return "tp";
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
        return "commands.tp.usage";
    }

    /**
     * Called when a CommandSender executes this command
     */
    public void execute(ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 1)
        {
            throw new WrongUsageException("commands.tp.usage", new Object[0]);
        }
        else
        {
            byte b0 = 0;
            Object object;

            if (args.length != 2 && args.length != 4 && args.length != 6)
            {
                object = getCommandSenderAsPlayer(sender);
            }
            else
            {
                object = func_175768_b(sender, args[0]);
                b0 = 1;
            }

            if (args.length != 1 && args.length != 2)
            {
                if (args.length < b0 + 3)
                {
                    throw new WrongUsageException("commands.tp.usage", new Object[0]);
                }
                else if (((Entity)object).worldObj != null)
                {
                    int i = b0 + 1;
                    CommandBase.CoordinateArg coordinatearg = func_175770_a(((Entity)object).posX, args[b0], true);
                    CommandBase.CoordinateArg coordinatearg1 = func_175767_a(((Entity)object).posY, args[i++], 0, 0, false);
                    CommandBase.CoordinateArg coordinatearg2 = func_175770_a(((Entity)object).posZ, args[i++], true);
                    CommandBase.CoordinateArg coordinatearg3 = func_175770_a((double)((Entity)object).rotationYaw, args.length > i ? args[i++] : "~", false);
                    CommandBase.CoordinateArg coordinatearg4 = func_175770_a((double)((Entity)object).rotationPitch, args.length > i ? args[i] : "~", false);
                    float f;

                    if (object instanceof EntityPlayerMP)
                    {
                        EnumSet enumset = EnumSet.noneOf(S08PacketPlayerPosLook.EnumFlags.class);

                        if (coordinatearg.func_179630_c())
                        {
                            enumset.add(S08PacketPlayerPosLook.EnumFlags.X);
                        }

                        if (coordinatearg1.func_179630_c())
                        {
                            enumset.add(S08PacketPlayerPosLook.EnumFlags.Y);
                        }

                        if (coordinatearg2.func_179630_c())
                        {
                            enumset.add(S08PacketPlayerPosLook.EnumFlags.Z);
                        }

                        if (coordinatearg4.func_179630_c())
                        {
                            enumset.add(S08PacketPlayerPosLook.EnumFlags.X_ROT);
                        }

                        if (coordinatearg3.func_179630_c())
                        {
                            enumset.add(S08PacketPlayerPosLook.EnumFlags.Y_ROT);
                        }

                        f = (float)coordinatearg3.func_179629_b();

                        if (!coordinatearg3.func_179630_c())
                        {
                            f = MathHelper.wrapAngleTo180_float(f);
                        }

                        float f1 = (float)coordinatearg4.func_179629_b();

                        if (!coordinatearg4.func_179630_c())
                        {
                            f1 = MathHelper.wrapAngleTo180_float(f1);
                        }

                        if (f1 > 90.0F || f1 < -90.0F)
                        {
                            f1 = MathHelper.wrapAngleTo180_float(180.0F - f1);
                            f = MathHelper.wrapAngleTo180_float(f + 180.0F);
                        }

                        ((Entity)object).mountEntity((Entity)null);
                        ((EntityPlayerMP)object).playerNetServerHandler.setPlayerLocation(coordinatearg.func_179629_b(), coordinatearg1.func_179629_b(), coordinatearg2.func_179629_b(), f, f1, enumset);
                        ((Entity)object).setRotationYawHead(f);
                    }
                    else
                    {
                        float f2 = (float)MathHelper.wrapAngleTo180_double(coordinatearg3.func_179628_a());
                        f = (float)MathHelper.wrapAngleTo180_double(coordinatearg4.func_179628_a());

                        if (f > 90.0F || f < -90.0F)
                        {
                            f = MathHelper.wrapAngleTo180_float(180.0F - f);
                            f2 = MathHelper.wrapAngleTo180_float(f2 + 180.0F);
                        }

                        ((Entity)object).setLocationAndAngles(coordinatearg.func_179628_a(), coordinatearg1.func_179628_a(), coordinatearg2.func_179628_a(), f2, f);
                        ((Entity)object).setRotationYawHead(f2);
                    }

                    notifyOperators(sender, this, "commands.tp.success.coordinates", new Object[] {((Entity)object).getName(), Double.valueOf(coordinatearg.func_179628_a()), Double.valueOf(coordinatearg1.func_179628_a()), Double.valueOf(coordinatearg2.func_179628_a())});
                }
            }
            else
            {
                Entity entity = func_175768_b(sender, args[args.length - 1]);

                if (entity.worldObj != ((Entity)object).worldObj)
                {
                    throw new CommandException("commands.tp.notSameDimension", new Object[0]);
                }
                else
                {
                    ((Entity)object).mountEntity((Entity)null);

                    if (object instanceof EntityPlayerMP)
                    {
                        ((EntityPlayerMP)object).playerNetServerHandler.setPlayerLocation(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
                    }
                    else
                    {
                        ((Entity)object).setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
                    }

                    notifyOperators(sender, this, "commands.tp.success", new Object[] {((Entity)object).getName(), entity.getName()});
                }
            }
        }
    }

    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        return args.length != 1 && args.length != 2 ? null : getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }
}