package com.arkcraft.module.creature.client.gui.test;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.arkcraft.module.core.ARKCraft;
import com.arkcraft.module.core.common.network.ScrollingMessage;
import com.arkcraft.module.creature.common.container.test.ContainerInventoryTaming;
import com.arkcraft.module.creature.common.container.test.IContainerScrollable;
import com.arkcraft.module.creature.common.entity.EntityARKCreature;

/**
 * @author Lewis_McReu
 */
public class GuiInventoryTaming extends GuiContainer
{
	public static final ResourceLocation texture = new ResourceLocation(
			ARKCraft.MODID, "textures/gui/gui_taming.png");

	public static final int WIDTH = 195, HEIGHT = 218, SCROLL_BUTTON_U = 195,
			SCROLL_BUTTON_V = 0, SCROLL_BUTTON_WIDTH = 12,
			SCROLL_BUTTON_HEIGHT = 13,
			SCROLL_BAR_HEIGHT = 52 - SCROLL_BUTTON_HEIGHT, SCROLL_BAR_U = 175,
			SCROLL_BAR_V = 79, TORPOR_BAR_U = 17, TORPOR_BAR_V = 18,
			TAMING_BAR_U = 17, TAMING_BAR_V = 36, TORPOR_BAR_FILLED_U = 0,
			TORPOR_BAR_FILLED_V = 219, TAMING_BAR_FILLED_U = 0,
			TAMING_BAR_FILLED_V = 235;

	private EntityARKCreature creature;

	public GuiInventoryTaming(EntityPlayer player, EntityARKCreature creature)
	{
		super(new ContainerInventoryTaming(player, creature));
		this.creature = creature;
		this.xSize = 195;
		this.ySize = 218;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		GL11.glColor4f(1F, 1F, 1F, 1F);

		// Gui Texture
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		// Scrollbar button
		int position = (int) (((IContainerScrollable) this.inventorySlots)
				.getRelativeScrollingOffset() * SCROLL_BAR_HEIGHT);
		this.drawTexturedModalRect(guiLeft + SCROLL_BAR_U,
				guiTop + SCROLL_BAR_V + position, SCROLL_BUTTON_U,
				SCROLL_BUTTON_V, SCROLL_BUTTON_WIDTH, SCROLL_BUTTON_HEIGHT);

		// Torpor level
		int torpor = creature.getTorpor();
		int maxTorpor = creature.getMaxTorpor();
		double relativeTorpor = (double) torpor / (double) maxTorpor;
		int torporWidth = (int) (relativeTorpor * 160);

		this.drawTexturedModalRect(guiLeft + TORPOR_BAR_U,
				guiTop + TORPOR_BAR_V, TORPOR_BAR_FILLED_U,
				TORPOR_BAR_FILLED_V, torporWidth, 16);

		// Taming progress
		int tamingProgress = creature.getTamingProgress();
		int tamingProgressRequired = creature.getTamingProgressRequired();
		double relativeTamingProgress = (double) tamingProgress / (double) tamingProgressRequired;
		int tamingWidth = (int) (relativeTamingProgress * 160);

		this.drawTexturedModalRect(guiLeft + TAMING_BAR_U,
				guiTop + TAMING_BAR_V, TAMING_BAR_FILLED_U,
				TAMING_BAR_FILLED_V, tamingWidth, 16);

		// TODO draw text on bars

		// // Draw numbers in stack positions
		// TODO maybe use for future testing
		// for (int i = 0; i < 3; i++)
		// {
		// for (int j = 0; j < 9; j++)
		// {
		// drawString(
		// Minecraft.getMinecraft().fontRendererObj,
		// String.valueOf(i * 9 + j + ((ContainerInventoryTaming)
		// inventorySlots)
		// .getScrollingOffset() * 9),
		// guiLeft + ContainerInventoryTaming.SLOT_START_X + j * 18,
		// guiTop + ContainerInventoryTaming.CREATURE_SLOT_START_Y + i * 18,
		// 0xFFFFFF);
		// }
		// }
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		// TODO Auto-generated method stub
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}

	@Override
	public void handleMouseInput() throws IOException
	{
		int scrollAmount = Mouse.getEventDWheel();
		if (scrollAmount != 0 && needsScrollBar())
		{
			if (scrollAmount > 0) scrollAmount = 1;
			else if (scrollAmount < 0) scrollAmount = -1;
			((ContainerInventoryTaming) this.inventorySlots)
					.scroll(scrollAmount);
			ARKCraft.modChannel
					.sendToServer(new ScrollingMessage(scrollAmount));
		}
		super.handleMouseInput();
	}

	private boolean needsScrollBar()
	{
		return ((ContainerInventoryTaming) this.inventorySlots).canScroll();
	}
}
