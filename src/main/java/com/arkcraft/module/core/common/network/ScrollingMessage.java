package com.arkcraft.module.core.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import com.arkcraft.module.creature.common.container.test.IContainerScrollable;

public class ScrollingMessage implements IMessage
{
	private int scrollAmount = 0;

	public ScrollingMessage(int scrollAmount)
	{
		this.scrollAmount = scrollAmount;
	}

	public ScrollingMessage()
	{
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		scrollAmount = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(scrollAmount);
	}

	public static class Handler implements IMessageHandler<ScrollingMessage, IMessage>
	{
		@Override
		public IMessage onMessage(final ScrollingMessage message, MessageContext ctx)
		{
			if (ctx.side != Side.SERVER)
			{
				System.err.println("MPUpdateScroll received on wrong side:" + ctx.side);
				return null;
			}
			processMessage(message, ctx.getServerHandler().playerEntity);
			return null;
		}
	}

	static void processMessage(ScrollingMessage message, EntityPlayer player)
	{
		if (player != null)
		{
			Container c = player.openContainer;
			if (c instanceof IContainerScrollable)
			{
				((IContainerScrollable) c).scroll(message.scrollAmount);
			}
		}
	}

}
