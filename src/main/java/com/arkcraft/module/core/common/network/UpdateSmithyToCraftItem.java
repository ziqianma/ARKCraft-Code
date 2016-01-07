package com.arkcraft.module.core.common.network;

import com.arkcraft.module.item.common.tile.TileInventorySmithy;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Used so server side tileEntity for Smithy can craft items
 *
 * @author William
 */
public class UpdateSmithyToCraftItem implements IMessage
{
    int blueprintSelected;
    boolean craftOneItem;
    boolean craftAllItems;
    boolean guiOpen;
    int x, y, z;

    /**
     * Don't use
     */
    public UpdateSmithyToCraftItem() { }

    public UpdateSmithyToCraftItem(int blueprintSelected, boolean craftOne, boolean craftAll, boolean guiOpen, BlockPos xyz)
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

    public static class Handler implements IMessageHandler<UpdateSmithyToCraftItem, IMessage>
    {
        @Override
        public IMessage onMessage(final UpdateSmithyToCraftItem message, MessageContext ctx)
        {
            if (ctx.side != Side.SERVER)
            {
                System.err.println("UpdateSmithyToCraftItem received on wrong side:" + ctx.side);
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

    static void processMessage(UpdateSmithyToCraftItem message, EntityPlayerMP player)
    {
        BlockPos xyz = new BlockPos(message.x, message.y, message.z);
        TileInventorySmithy tileEntitySmithy = (TileInventorySmithy) player.worldObj.getTileEntity(xyz);
        if (tileEntitySmithy instanceof TileInventorySmithy)
        {
            tileEntitySmithy.setBlueprintSelected(message.blueprintSelected);
            tileEntitySmithy.setCraftAllPressed(message.craftAllItems, false);
            tileEntitySmithy.setCraftOnePressed(message.craftOneItem, false);
            tileEntitySmithy.setGuiOpen(message.guiOpen, false);
        }
    }
}
