package com.arkcraft.mod2.common.network;

import com.arkcraft.mod.GlobalAdditions;
import com.arkcraft.mod.common.ARKCraft;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Used so the player can open the Crafting GUI
 * @author William
 *
 */
public class OpenPlayerCrafting implements IMessage {
	boolean doIt;  // Not used yet
	
	/**
	 * Don't use
	 */
	public OpenPlayerCrafting() {	}

	public OpenPlayerCrafting(boolean doIt) {
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

	public static class Handler implements IMessageHandler<OpenPlayerCrafting, IMessage> {
		@Override
		public IMessage onMessage(final OpenPlayerCrafting message, MessageContext ctx) {
		    if (ctx.side != Side.SERVER) {
		        System.err.println("MPUpdateDoCraft received on wrong side:" + ctx.side);
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

	// On Server
	static void processMessage(OpenPlayerCrafting message, EntityPlayerMP player){
		if (player != null) {
			player.openGui(ARKCraft.instance(), GlobalAdditions.GUI.PLAYER.getID(),	player.worldObj, 0, 0, 0);
		}
	}
}
