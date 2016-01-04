package com.arkcraft.module.core.common.network;

import com.arkcraft.module.item.common.items.ARKCraftItems;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Used so the player can poop
 *
 * @author William
 */
public class PlayerPoop implements IMessage
{
    boolean doIt;  // Not used yet

    /**
     * Don't use
     */
    public PlayerPoop() { }

    public PlayerPoop(boolean doIt)
    {
        this.doIt = doIt;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.doIt = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeBoolean(this.doIt);
    }

    public static class Handler implements IMessageHandler<PlayerPoop, IMessage>
    {
        @Override
        public IMessage onMessage(final PlayerPoop message, MessageContext ctx)
        {
            if (ctx.side != Side.SERVER)
            {
                System.err.println("MPUpdateDoCraft received on wrong side:" + ctx.side);
                return null;
            }
            final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            player.getServerForPlayer().addScheduledTask(new Runnable()
            {
                public void run()
                {
                    processMessage(message, player);
                }
            });
            return null;
        }
    }

    static void processMessage(PlayerPoop message, EntityPlayerMP player)
    {
        if (player != null)
        {
            player.dropItem(ARKCraftItems.player_feces, 1);
        }
    }
}
