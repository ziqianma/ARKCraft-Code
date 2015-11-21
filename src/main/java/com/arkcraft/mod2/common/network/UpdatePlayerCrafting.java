package com.arkcraft.mod2.common.network;

import com.arkcraft.mod2.common.entity.player.ARKPlayer;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Used so server side tileEntity for Smithy can craft items
 * @author William
 *
 */
public class UpdatePlayerCrafting implements IMessage {
	boolean craftOneItem;
	int blueprintPressed;
	
	/**
	 * Don't use
	 */
	public UpdatePlayerCrafting() {	}

	public UpdatePlayerCrafting(boolean craftOne, int blueprintPressed) {
		this.craftOneItem = craftOne;
		this.blueprintPressed = blueprintPressed;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.craftOneItem = buf.readBoolean();
		this.blueprintPressed = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(this.craftOneItem);
		buf.writeInt(this.blueprintPressed);
	}

	public static class Handler implements IMessageHandler<UpdatePlayerCrafting, IMessage> {
		@Override
		public IMessage onMessage(final UpdatePlayerCrafting message, MessageContext ctx) {
		    if (ctx.side != Side.SERVER) {
		        System.err.println("UpdatePlayerCrafting received on wrong side:" + ctx.side);
		        return null;
		    }
			final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
			player.getServerForPlayer().addScheduledTask(new Runnable(){
				public void run(){
					processMessage(message, player);
				}
			});	    
			return null;
		}
	}
	
	static void processMessage(UpdatePlayerCrafting message, EntityPlayerMP player){
		if (player != null) {
			ARKPlayer.get(player).getInventoryBlueprints()
					.setCraftOnePressed(message.craftOneItem, message.blueprintPressed, false);
		}
	}
}
