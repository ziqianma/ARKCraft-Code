package com.arkcraft.module.weapon.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import com.arkcraft.module.weapon.common.item.ranged.ItemRangedWeapon;

public class ReloadFinished implements IMessage
{
	public ReloadFinished()
	{

	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
	}

	public static class Handler implements IMessageHandler<ReloadFinished, IMessage>
	{
		@Override
		public IMessage onMessage(final ReloadFinished message, MessageContext ctx)
		{
			if (ctx.side != Side.CLIENT)
			{
				System.err.println("MPUpdateDoReloadFinished received on wrong side:" + ctx.side);
				return null;
			}
			processMessage(message, Minecraft.getMinecraft().thePlayer);
			return null;
		}
	}

	static void processMessage(ReloadFinished message, EntityPlayerSP player)
	{
		if (player != null)
		{
			ItemStack stack = player.getCurrentEquippedItem();
			if (stack != null && stack.getItem() instanceof ItemRangedWeapon) ((ItemRangedWeapon) stack
					.getItem()).setReloading(stack, player, false);
		}
	}

}
