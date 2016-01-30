package com.arkcraft.module.weapon.common.network;

import com.arkcraft.module.core.common.network.ARKMessage;
import com.arkcraft.module.weapon.common.entity.EntityBallista;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MsgBallistaShot extends ARKMessage
{
    private int ballistaEntityID = 0;

    public MsgBallistaShot()
    {
    }

    public MsgBallistaShot(EntityBallista entity)
    {
        ballistaEntityID = entity.getEntityId();
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buf)
    {
        ballistaEntityID = buf.readInt();
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buf)
    {
        buf.writeInt(ballistaEntityID);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleClientSide(EntityPlayer player)
    {
    }

    @Override
    public void handleServerSide(EntityPlayer player)
    {
        Entity entity = player.worldObj.getEntityByID(ballistaEntityID);
        if (entity instanceof EntityBallista)
        {
            ((EntityBallista) entity).fireBallista();
        }
    }

}