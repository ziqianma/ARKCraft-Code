package com.arkcraft.mod.core.network;

import com.arkcraft.mod.core.items.ModItems;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Used so the player can poop
 * @author William
 *
 */
public class PlayerPoop  implements IMessage {
	boolean doIt;  // Not used yet
	
	/**
	 * Don't use
	 */
	public PlayerPoop() {	}

	public PlayerPoop(boolean doIt) {
		this.doIt = doIt;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.doIt = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(this.doIt);
	}

	public static class Handler implements IMessageHandler<PlayerPoop, IMessage> {
		@Override
		public IMessage onMessage(PlayerPoop message, MessageContext ctx) {
			EntityPlayer player = ctx.getServerHandler().playerEntity;
			if (player != null) {
				player.dropItem(ModItems.player_feces, 1);
			}
			return null;
		}
	}
}
