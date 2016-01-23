package com.arkcraft.module.item.client.gui.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import org.lwjgl.opengl.GL11;

import com.arkcraft.module.item.common.items.weapons.ranged.ItemRangedWeapon;

public class GuiOverlayReloading extends Gui
{
	private static final Minecraft mc = Minecraft.getMinecraft();

	@SubscribeEvent
	public void renderGUIOverlay(RenderGameOverlayEvent.Post e)
	{
		EntityPlayer p = mc.thePlayer;
		ItemStack stack = p.getCurrentEquippedItem();
		if (e.type.equals(ElementType.HOTBAR))
		{
			if (stack != null && stack.getItem() instanceof ItemRangedWeapon)
			{
				ItemRangedWeapon weapon = (ItemRangedWeapon) stack.getItem();
				boolean rld = weapon.isLoaded(stack, p);
				GL11.glColor4f(1F, 1F, 1F, 1F);
				GL11.glDisable(GL11.GL_LIGHTING);
				int x0 = e.resolution.getScaledWidth() / 2 - 88 + p.inventory.currentItem * 20;
				int y0 = e.resolution.getScaledHeight() - 3;
				float f;
				int color;
				if (rld)
				{
					f = 1F;
					if (p.getItemInUse() == stack)
					{
						color = 0x60C60000;
					}
					else
					{
						color = 0x60348E00;
					}

				}
				else if (weapon.isReloading(stack))
				{
					f = Math.min((float) weapon.getReloadTicks(stack) / weapon.getReloadDuration(),
							1F);
					color = 0x60EAA800;
				}
				else
				{
					f = 0F;
					color = 0;
				}
				drawRect(x0, y0, x0 + 16, y0 - (int) (f * 16), color);
			}
		}
		else if (e.type.equals(ElementType.FOOD))
		{
			if (stack != null && stack.getItem() instanceof ItemRangedWeapon)
			{
				ItemRangedWeapon weapon = (ItemRangedWeapon) stack.getItem();
				String text = weapon.getAmmoQuantity(stack) + "/" + weapon
						.getAmmoQuantityInInventory(stack, p);
				int x = e.resolution.getScaledWidth() - 4 - mc.fontRendererObj.getStringWidth(text);
				int y = 20;
				drawString(mc.fontRendererObj, text, x, y - 16, 0xFFFFFFFF);
			}
		}
	}
}
