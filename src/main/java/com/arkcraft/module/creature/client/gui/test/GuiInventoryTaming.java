package com.arkcraft.module.creature.client.gui.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
			BAR_WIDTH = 160, BAR_HEIGHT = 16, TAMING_BAR_U = 17,
			TAMING_BAR_V = 36, TORPOR_BAR_FILLED_U = 0,
			TORPOR_BAR_FILLED_V = 219, TAMING_BAR_FILLED_U = 0,
			TAMING_BAR_FILLED_V = 235, STATS_Y_START = 57, STATS_X_START = 24,
			STATS_U_START = 195, STATS_V_START = 13;

	private EntityARKCreature creature;

	private int scrollPosition;
	private boolean scrollClicked = false;

	public GuiInventoryTaming(EntityPlayer player, EntityARKCreature creature)
	{
		super(new ContainerInventoryTaming(player, creature));
		this.creature = creature;
		this.xSize = WIDTH;
		this.ySize = HEIGHT;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		mc.getTextureManager().bindTexture(texture);
		GL11.glColor4f(1F, 1F, 1F, 1F);

		// Gui Texture
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		// Scrollbar button
		scrollPosition = (int) (((IContainerScrollable) this.inventorySlots)
				.getRelativeScrollingOffset() * SCROLL_BAR_HEIGHT);
		this.drawTexturedModalRect(guiLeft + SCROLL_BAR_U,
				guiTop + SCROLL_BAR_V + scrollPosition, SCROLL_BUTTON_U,
				SCROLL_BUTTON_V, SCROLL_BUTTON_WIDTH, SCROLL_BUTTON_HEIGHT);

		// Torpor level
		double relativeTorpor = creature.getRelativeTorpor();
		int torporWidth = (int) (relativeTorpor * 160);

		this.drawTexturedModalRect(guiLeft + TORPOR_BAR_U,
				guiTop + TORPOR_BAR_V, TORPOR_BAR_FILLED_U,
				TORPOR_BAR_FILLED_V, torporWidth, 16);

		// Taming progress
		double relativeTamingProgress = creature.getRelativeTamingProgress();
		int tamingWidth = (int) (relativeTamingProgress * 160);

		this.drawTexturedModalRect(guiLeft + TAMING_BAR_U,
				guiTop + TAMING_BAR_V, TAMING_BAR_FILLED_U,
				TAMING_BAR_FILLED_V, tamingWidth, 16);

		// Text in bars
		int centerX = TORPOR_BAR_U + BAR_WIDTH / 2;
		int centerTorporY = TORPOR_BAR_V + BAR_HEIGHT / 2 - 4;
		int centerTamingY = TAMING_BAR_V + BAR_HEIGHT / 2 - 4;

		this.drawCenteredString(mc.fontRendererObj, "Unconscious",
				guiLeft + centerX, guiTop + centerTorporY, 0xFFFFFF);
		this.drawCenteredString(mc.fontRendererObj, "Taming",
				guiLeft + centerX, guiTop + centerTamingY, 0xFFFFFF);

		// stat overlays

		mc.getTextureManager().bindTexture(texture);

		double relativeHealth = creature.getRelativeHealth();
		double relativeWeight = creature.getRelativeWeight();
		double relativeOxygen = creature.getRelativeOxygen();
		double relativeFood = creature.getRelativeFood();
		double relativeStamina = creature.getRelativeStamina();

		int baseHeight = 18;

		int healthHeight = (int) (relativeHealth * baseHeight);
		int weightHeight = (int) (relativeWeight * baseHeight);
		int oxygenHeight = (int) (relativeOxygen * baseHeight);
		int foodHeight = (int) (relativeFood * baseHeight);
		int staminaHeight = (int) (relativeStamina * baseHeight);

		int stats_u = STATS_U_START;
		int stats_v = STATS_V_START;
		int stats_x = STATS_X_START;
		int stats_y = STATS_Y_START;

		// health
		drawTexturedModalRect(guiLeft + stats_x,
				guiTop + stats_y + baseHeight - healthHeight, stats_u,
				stats_v + baseHeight - healthHeight, baseHeight, healthHeight);
		stats_x += 22;
		stats_v += 18;

		// weight
		drawTexturedModalRect(guiLeft + stats_x,
				guiTop + stats_y + baseHeight - weightHeight, stats_u,
				stats_v + baseHeight - weightHeight, baseHeight, weightHeight);
		stats_x += 22;
		stats_v += 18;

		// oxygen
		drawTexturedModalRect(guiLeft + stats_x,
				guiTop + stats_y + baseHeight - oxygenHeight, stats_u,
				stats_v + baseHeight - oxygenHeight, baseHeight, oxygenHeight);
		stats_x += 22;
		stats_v += 18;

		// food
		drawTexturedModalRect(guiLeft + stats_x,
				guiTop + stats_y + baseHeight - foodHeight, stats_u,
				stats_v + baseHeight - foodHeight, baseHeight, foodHeight);
		stats_x += 22;
		stats_v += 18;

		// damage
		drawTexturedModalRect(guiLeft + stats_x, guiTop + stats_y, stats_u,
				stats_v, baseHeight, baseHeight);
		stats_x += 22;
		stats_v += 18;

		// speed
		drawTexturedModalRect(guiLeft + stats_x, guiTop + stats_y, stats_u,
				stats_v, baseHeight, baseHeight);
		stats_x += 22;
		stats_v += 18;

		// stamina
		drawTexturedModalRect(guiLeft + stats_x,
				guiTop + stats_y + baseHeight - staminaHeight, stats_u,
				stats_v + baseHeight - staminaHeight, baseHeight, staminaHeight);
		stats_x += 22;
		stats_v += 18;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		mouseX = mouseX - guiLeft;
		mouseY = mouseY - guiTop;
		List<String> tipList = new ArrayList<String>();
		// torpor bar
		if (mouseX >= TORPOR_BAR_U + 1 && mouseX <= TORPOR_BAR_U + 160 && mouseY >= TORPOR_BAR_V + 1 && mouseY <= TORPOR_BAR_V + 16)
		{
			tipList.add(creature.getTorpor() + "/" + creature.getMaxTorpor());
		}
		else if (mouseX >= TAMING_BAR_U + 1 && mouseX <= TAMING_BAR_U + 160 && mouseY >= TAMING_BAR_V + 1 && mouseY <= TAMING_BAR_V + 16)
		{
			tipList.add(creature.getRelativeTamingProgress() * 100 + "%");
		}
		else if (mouseX >= STATS_X_START && mouseX <= STATS_X_START + 18 && mouseY >= STATS_Y_START && mouseY <= STATS_Y_START + 18)
		{
			tipList.add((int) creature.getHealth() + "/" + (int) creature
					.getMaxHealth());
		}
		else if (mouseX >= STATS_X_START + 22 && mouseX <= STATS_X_START + 22 + 18 && mouseY >= STATS_Y_START && mouseY <= STATS_Y_START + 18)
		{
			tipList.add(creature.countStacks() + "/" + creature.getMaxWeight());
		}
		else if (mouseX >= STATS_X_START + 22 * 2 && mouseX <= STATS_X_START + 22 * 2 + 18 && mouseY >= STATS_Y_START && mouseY <= STATS_Y_START + 18)
		{
			tipList.add(creature.getOxygen() + "/" + creature.getMaxOxygen());
		}
		else if (mouseX >= STATS_X_START + 22 * 3 && mouseX <= STATS_X_START + 22 * 3 + 18 && mouseY >= STATS_Y_START && mouseY <= STATS_Y_START + 18)
		{
			tipList.add(creature.getFood() + "/" + creature.getMaxFood());
		}
		else if (mouseX >= STATS_X_START + 22 * 4 && mouseX <= STATS_X_START + 22 * 4 + 18 && mouseY >= STATS_Y_START && mouseY <= STATS_Y_START + 18)
		{
			tipList.add(creature.getMaxDamage() + "%");
		}
		else if (mouseX >= STATS_X_START + 22 * 5 && mouseX <= STATS_X_START + 22 * 5 + 18 && mouseY >= STATS_Y_START && mouseY <= STATS_Y_START + 18)
		{
			tipList.add(creature.getMaxSpeed() + "%");
		}
		else if (mouseX >= STATS_X_START + 22 * 6 && mouseX <= STATS_X_START + 22 * 6 + 18 && mouseY >= STATS_Y_START && mouseY <= STATS_Y_START + 18)
		{
			tipList.add(creature.getStamina() + "/" + creature.getMaxStamina());
		}

		drawHoveringText(tipList, mouseX, mouseY);
	}

	@Override
	public void handleMouseInput() throws IOException
	{
		int scrollAmount = Mouse.getEventDWheel();
		if (scrollAmount != 0 && needsScrollBar())
		{
			if (scrollAmount > 0) scrollAmount = 1;
			else if (scrollAmount < 0) scrollAmount = -1;
			adjustScroll(scrollAmount);
		}
		else super.handleMouseInput();
	}

	private void adjustScroll(int scrollAmount)
	{
		((ContainerInventoryTaming) this.inventorySlots).scroll(scrollAmount);
		ARKCraft.modChannel.sendToServer(new ScrollingMessage(scrollAmount));
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		if (mouseX >= guiLeft + SCROLL_BAR_U && mouseX <= guiLeft + SCROLL_BAR_U + SCROLL_BUTTON_WIDTH && mouseY >= guiTop + SCROLL_BAR_V && mouseY <= guiTop + SCROLL_BAR_V + SCROLL_BAR_HEIGHT)
		{
			adjustScrollFromMouseY(mouseY);
			scrollClicked = true;
		}
		if (mouseX >= guiLeft + SCROLL_BAR_U && mouseX <= guiLeft + SCROLL_BAR_U + SCROLL_BUTTON_WIDTH && mouseY >= guiTop + SCROLL_BAR_V + scrollPosition && mouseY < guiTop + SCROLL_BAR_V + scrollPosition + SCROLL_BUTTON_HEIGHT)
		{
			scrollClicked = true;
		}
		else super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick)
	{
		if (scrollClicked)
		{
			adjustScrollFromMouseY(mouseY);
		}
		else super.mouseClickMove(mouseX, mouseY, clickedMouseButton,
				timeSinceLastClick);
	}

	private void adjustScrollFromMouseY(int mouseY)
	{
		ContainerInventoryTaming inv = (ContainerInventoryTaming) inventorySlots;
		int minScroll = 0;
		int maxScroll = SCROLL_BAR_HEIGHT;
		int newScrollPos = mouseY - guiTop - SCROLL_BAR_V - 7;
		if (newScrollPos < minScroll)
		{
			newScrollPos = minScroll;
		}
		else if (newScrollPos > maxScroll)
		{
			newScrollPos = maxScroll;
		}
		int scrollDiv = SCROLL_BAR_HEIGHT / inv.getMaxOffset();
		int adjustScroll = newScrollPos / scrollDiv - inv.getScrollingOffset();
		adjustScroll(adjustScroll);
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state)
	{
		if (scrollClicked)
		{
			scrollClicked = false;
		}
		else super.mouseReleased(mouseX, mouseY, state);
	}

	private boolean needsScrollBar()
	{
		return ((ContainerInventoryTaming) this.inventorySlots).canScroll();
	}
}
