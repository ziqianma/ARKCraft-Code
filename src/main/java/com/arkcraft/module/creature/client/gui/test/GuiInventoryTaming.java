package com.arkcraft.module.creature.client.gui.test;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.arkcraft.lib.LogHelper;
import com.arkcraft.module.core.ARKCraft;
import com.arkcraft.module.core.common.network.ScrollingMessage;
import com.arkcraft.module.creature.common.container.test.ContainerInventoryTaming;
import com.arkcraft.module.creature.common.entity.EntityARKCreature;

public class GuiInventoryTaming extends GuiContainer
{
	public static final ResourceLocation texture = new ResourceLocation(
			ARKCraft.MODID, "textures/gui/taiming_gui.png");

	private EntityPlayer player;
	private EntityARKCreature creature;

	public GuiInventoryTaming(EntityPlayer player, EntityARKCreature creature)
	{
		super(new ContainerInventoryTaming(player, creature));
		this.player = player;
		this.creature = creature;
		this.guiLeft = 100;
		this.guiTop = 200;
		this.xSize = 229;
		this.ySize = 218;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		// Draw numbers in stack positions
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				drawString(
						Minecraft.getMinecraft().fontRendererObj,
						String.valueOf(i * 9 + j + ((ContainerInventoryTaming) inventorySlots)
								.getScrollingOffset() * 9),
						guiLeft + ContainerInventoryTaming.SLOT_START_X + j * 18,
						guiTop + ContainerInventoryTaming.CREATURE_SLOT_START_Y + i * 18,
						0xFFFFFF);
			}
		}
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
			LogHelper.info("Scrolling possible");
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
