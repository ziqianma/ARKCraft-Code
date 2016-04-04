package com.arkcraft.module.weapon.client.gui;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.arkcraft.module.core.ARKCraft;
import com.arkcraft.module.weapon.common.container.ContainerInventoryAttachment;
import com.arkcraft.module.weapon.common.container.inventory.InventoryAttachment;

public class GUIAttachment extends GuiContainer
{
	private static final ResourceLocation iconLocation = new ResourceLocation(ARKCraft.MODID,
			"textures/gui/attachment_gui.png");

	/** The inventory to render on screen */
	private final InventoryAttachment inventory;

	public GUIAttachment(EntityPlayer player, InventoryPlayer InvPlayer, InventoryAttachment tileEntity)
	{
		super(new ContainerInventoryAttachment(player, InvPlayer, tileEntity));
		this.inventory = tileEntity;
		this.xSize = 175;
		this.ySize = 165;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		String name = inventory.getDisplayName().getUnformattedText();
		final int LABEL_YPOS = 7;
		final int LABEL_XPOS = (xSize / 2) - (name.length() * 5 / 2);
		this.fontRendererObj.drawString(name, LABEL_XPOS, LABEL_YPOS, Color.darkGray.getRGB());
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(iconLocation);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
		drawEntityOnScreen(k + 51, l + 75, 30, (float) (k + 51) - this.xSize,
				(float) (l + 75 - 50) - this.ySize, this.mc.thePlayer);
	}

	public static void drawEntityOnScreen(int p_147046_0_, int p_147046_1_, int p_147046_2_, float p_147046_3_, float p_147046_4_, EntityLivingBase p_147046_5_)
	{
		GlStateManager.enableColorMaterial();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) p_147046_0_, (float) p_147046_1_, 50.0F);
		GlStateManager.scale((float) (-p_147046_2_), (float) p_147046_2_, (float) p_147046_2_);
		GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
		float f2 = p_147046_5_.renderYawOffset;
		float f3 = p_147046_5_.rotationYaw;
		float f4 = p_147046_5_.rotationPitch;
		float f5 = p_147046_5_.prevRotationYawHead;
		float f6 = p_147046_5_.rotationYawHead;
		GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-((float) Math.atan((double) (p_147046_4_ / 40.0F))) * 20.0F, 1.0F,
				0.0F, 0.0F);
		p_147046_5_.renderYawOffset = (float) Math.atan((double) (p_147046_3_ / 40.0F)) * 20.0F;
		p_147046_5_.rotationYaw = (float) Math.atan((double) (p_147046_3_ / 40.0F)) * 40.0F;
		p_147046_5_.rotationPitch = -((float) Math.atan((double) (p_147046_4_ / 40.0F))) * 20.0F;
		p_147046_5_.rotationYawHead = p_147046_5_.rotationYaw;
		p_147046_5_.prevRotationYawHead = p_147046_5_.rotationYaw;
		GlStateManager.translate(0.0F, 0.0F, 0.0F);
		RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
		rendermanager.setPlayerViewY(180.0F);
		rendermanager.setRenderShadow(false);
		rendermanager.renderEntityWithPosYaw(p_147046_5_, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
		rendermanager.setRenderShadow(true);
		p_147046_5_.renderYawOffset = f2;
		p_147046_5_.rotationYaw = f3;
		p_147046_5_.rotationPitch = f4;
		p_147046_5_.prevRotationYawHead = f5;
		p_147046_5_.rotationYawHead = f6;
		GlStateManager.popMatrix();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.disableTexture2D();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}
}