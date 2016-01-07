package com.arkcraft.module.core.common.network;

import com.arkcraft.module.core.ARKCraft;
import com.arkcraft.module.item.common.entity.player.ARKPlayer;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author gegy1000
 */
public class SyncPlayerData implements IMessage
{
    private NBTTagCompound nbt;

    /**
     * Don't use
     */
    public SyncPlayerData() { }

    public SyncPlayerData(boolean all, ARKPlayer player)
    {
        this.nbt = new NBTTagCompound();
        player.saveNBTData(nbt);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        nbt = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeTag(buf, nbt);
    }

    public static class Handler implements IMessageHandler<SyncPlayerData, IMessage>
    {
        @Override
        public IMessage onMessage(final SyncPlayerData message, MessageContext ctx)
        {
            if (ctx.side != Side.CLIENT)
            {
                System.err.println("SyncPlayerData received on wrong side:" + ctx.side);
                return null;
            }

            final EntityPlayer player = ARKCraft.proxy.getPlayer();

            Minecraft.getMinecraft().addScheduledTask(new Runnable()
            {
                public void run()
                {
                    processMessage(message, player);
                }
            });

            return null;
        }
    }

    static void processMessage(SyncPlayerData message, EntityPlayer player)
    {
        if (player != null)
        {
            ARKPlayer.get(player).loadNBTData(message.nbt);
        }
    }
}
