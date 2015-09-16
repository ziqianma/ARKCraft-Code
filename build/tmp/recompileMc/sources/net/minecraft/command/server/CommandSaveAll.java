package net.minecraft.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldServer;

public class CommandSaveAll extends CommandBase
{
    private static final String __OBFID = "CL_00000826";

    /**
     * Get the name of the command
     */
    public String getName()
    {
        return "save-all";
    }

    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.save.usage";
    }

    /**
     * Called when a CommandSender executes this command
     */
    public void execute(ICommandSender sender, String[] args) throws CommandException
    {
        MinecraftServer minecraftserver = MinecraftServer.getServer();
        sender.addChatMessage(new ChatComponentTranslation("commands.save.start", new Object[0]));

        if (minecraftserver.getConfigurationManager() != null)
        {
            minecraftserver.getConfigurationManager().saveAllPlayerData();
        }

        try
        {
            int i;
            WorldServer worldserver;
            boolean flag;

            for (i = 0; i < minecraftserver.worldServers.length; ++i)
            {
                if (minecraftserver.worldServers[i] != null)
                {
                    worldserver = minecraftserver.worldServers[i];
                    flag = worldserver.disableLevelSaving;
                    worldserver.disableLevelSaving = false;
                    worldserver.saveAllChunks(true, (IProgressUpdate)null);
                    worldserver.disableLevelSaving = flag;
                }
            }

            if (args.length > 0 && "flush".equals(args[0]))
            {
                sender.addChatMessage(new ChatComponentTranslation("commands.save.flushStart", new Object[0]));

                for (i = 0; i < minecraftserver.worldServers.length; ++i)
                {
                    if (minecraftserver.worldServers[i] != null)
                    {
                        worldserver = minecraftserver.worldServers[i];
                        flag = worldserver.disableLevelSaving;
                        worldserver.disableLevelSaving = false;
                        worldserver.saveChunkData();
                        worldserver.disableLevelSaving = flag;
                    }
                }

                sender.addChatMessage(new ChatComponentTranslation("commands.save.flushEnd", new Object[0]));
            }
        }
        catch (MinecraftException minecraftexception)
        {
            notifyOperators(sender, this, "commands.save.failed", new Object[] {minecraftexception.getMessage()});
            return;
        }

        notifyOperators(sender, this, "commands.save.success", new Object[0]);
    }
}