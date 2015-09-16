package net.minecraft.command;

import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public interface ICommandSender
{
    /**
     * Gets the name of this command sender (usually username, but possibly "Rcon")
     */
    String getName();

    IChatComponent getDisplayName();

    /**
     * Notifies this sender of some sort of information.  This is for messages intended to display to the user.  Used
     * for typical output (like "you asked for whether or not this game rule is set, so here's your answer"), warnings
     * (like "I fetched this block for you by ID, but I'd like you to know that every time you do this, I die a little
     * inside"), and errors (like "it's not called iron_pixacke, silly").
     */
    void addChatMessage(IChatComponent message);

    /**
     * Returns true if the CommandSender may execute the given command
     *  
     * @param permLevel The permission level required to execute the command
     * @param commandName The name of the command
     */
    boolean canUseCommand(int permLevel, String commandName);

    BlockPos getPosition();

    Vec3 getPositionVector();

    World getEntityWorld();

    /**
     * Returns the entity associated with the command sender. MAY BE NULL!
     */
    Entity getCommandSenderEntity();

    /**
     * Returns true if the command sender should be sent feedback about executed commands
     */
    boolean sendCommandFeedback();

    void setCommandStat(CommandResultStats.Type type, int amount);
}