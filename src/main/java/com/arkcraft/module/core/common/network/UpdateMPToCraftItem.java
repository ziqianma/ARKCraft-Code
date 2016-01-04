package com.arkcraft.module.core.common.network;

import com.arkcraft.module.item.common.tile.TileInventoryMP;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Used so server side tileEntity for Mortar and Pestle can craft items
 *
 * @author William
 */
public class UpdateMPToCraftItem implements IMessage
{
    int blueprintSelected;
    boolean craftOneItem;
    boolean craftAllItems;
    boolean guiOpen;
    int x, y, z;

    /**
     * Don't use
     */
    public UpdateMPToCraftItem() { }

    public UpdateMPToCraftItem(int blueprintSelected, boolean craftOne, boolean craftAll, boolean guiOpen, BlockPos xyz)
    {
        this.blueprintSelected = blueprintSelected;
        this.craftOneItem = craftOne;
        this.craftAllItems = craftAll;
        this.guiOpen = guiOpen;
        this.x = xyz.getX();
        this.y = xyz.getY();
        this.z = xyz.getZ();
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.blueprintSelected = buf.readInt();
        this.craftOneItem = buf.readBoolean();
        this.craftAllItems = buf.readBoolean();
        this.guiOpen = buf.readBoolean();
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.blueprintSelected);
        buf.writeBoolean(this.craftOneItem);
        buf.writeBoolean(this.craftAllItems);
        buf.writeBoolean(this.guiOpen);
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
    }

    public static class Handler implements IMessageHandler<UpdateMPToCraftItem, IMessage>
    {
        @Override
        public IMessage onMessage(final UpdateMPToCraftItem message, MessageContext ctx)
        {
            if (ctx.side != Side.SERVER)
            {
                System.err.println("UpdateMPToCraftItem received on wrong side:" + ctx.side);
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

    static void processMessage(UpdateMPToCraftItem message, EntityPlayerMP player)
    {
        BlockPos xyz = new BlockPos(message.x, message.y, message.z);
        TileInventoryMP tileEntityMP = (TileInventoryMP) player.worldObj.getTileEntity(xyz);
        if (tileEntityMP instanceof TileInventoryMP)
        {
            tileEntityMP.setBlueprintSelected(message.blueprintSelected);
            tileEntityMP.setCraftAllPressed(message.craftAllItems, false);
            tileEntityMP.setCraftOnePressed(message.craftOneItem, false);
            tileEntityMP.setGuiOpen(message.guiOpen, false);
        }
    }
}
