package com.arkcraft.module.item.client.gui.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import org.lwjgl.opengl.GL11;

import com.arkcraft.module.item.common.items.weapons.guns.ItemRangedWeapon;

public class GuiOverlayReloading extends Gui
{
	private static final Minecraft mc = Minecraft.getMinecraft();

	@SubscribeEvent
	public void renderGUIOverlay(RenderGameOverlayEvent e)
	{
		if (e instanceof RenderGameOverlayEvent.Post || e.type != ElementType.HOTBAR) { return; }

		EntityPlayer p = mc.thePlayer;
		if (p != null)
		{
			// TODO
			ItemStack is = p.getCurrentEquippedItem();
			if (is != null && is.getItem() instanceof ItemRangedWeapon)
			{
				ItemRangedWeapon weapon = (ItemRangedWeapon) is.getItem();
				boolean rld = weapon.isLoaded(is);
				GL11.glColor4f(1F, 1F, 1F, 1F);
				GL11.glDisable(GL11.GL_LIGHTING);
				float f;
				int color;
				if (rld)
				{
					f = 1F;
					if (p.getItemInUse() == is)
					{
						color = 0x60C60000;
					}
					else
					{
						color = 0x60348E00;
					}
				}
				else if (p.getItemInUse() == is)
				{
					f = Math.min((float) p.getItemInUseDuration() / weapon.getReloadDuration(), 1F);
					color = 0x60EAA800;
				}
				else
				{
					f = 0F;
					color = 0;
				}
				int x0 = e.resolution.getScaledWidth() / 2 - 88 + p.inventory.currentItem * 20;
				int y0 = e.resolution.getScaledHeight() - 3;
				drawRect(x0, y0, x0 + 16, y0 - (int) (f * 16), color);
			}
		}
	}
}
