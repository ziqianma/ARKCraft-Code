package com.arkcraft.module.weapon.client.event;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.arkcraft.module.core.ARKCraft;
import com.arkcraft.module.core.GlobalAdditions;
import com.arkcraft.module.items.ARKCraftItems;
import com.arkcraft.module.weapon.common.container.inventory.InventoryAttachment;
import com.arkcraft.module.weapon.common.item.attachment.supporting.NonSupporting;
import com.arkcraft.module.weapon.common.item.ranged.ItemRangedWeapon;
import com.arkcraft.module.weapon.common.network.OpenAttachmentInventory;
import com.arkcraft.module.weapon.common.network.ReloadStarted;

public class ClientEventHandler
{
	private static KeyBinding reload, attachment;

	private static Minecraft mc = Minecraft.getMinecraft();

	private static Random random = new Random();

	private static int swayTicks;
	private static final int maxTicks = 20;
	private static float yawSway;
	private static float pitchSway;

	private ItemStack selected;
	private static final ResourceLocation OVERLAY_TEXTURE = new ResourceLocation(ARKCraft.MODID,
			"textures/gui/scope.png");
	public boolean showScopeOverlap = false;

	public static void init()
	{
		ClientEventHandler handler = new ClientEventHandler();
		FMLCommonHandler.instance().bus().register(handler);
		MinecraftForge.EVENT_BUS.register(handler);

		reload = new KeyBinding("key.arkcraft.reload", Keyboard.KEY_R, ARKCraft.NAME);
		ClientRegistry.registerKeyBinding(reload);

		attachment = new KeyBinding("key.attachment", Keyboard.KEY_M, "ARKCraft");
		ClientRegistry.registerKeyBinding(attachment);
	}

	@SubscribeEvent
	public void onMouseEvent(MouseEvent evt)
	{
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer thePlayer = mc.thePlayer;

		if (evt.button == 0)
		{
			ItemStack stack = thePlayer.getCurrentEquippedItem();
			InventoryAttachment att = InventoryAttachment.create(stack);
			if (att != null)
			{
				showScopeOverlap = stack != null && (att.isScopePresent() || stack.getItem()
						.equals(ARKCraftItems.spy_glass)) && evt.buttonstate;
				selected = stack;
				if (stack != null && att.isScopePresent()) evt.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onFOVUpdate(FOVUpdateEvent evt)
	{
		if (mc.gameSettings.thirdPersonView == 0 && showScopeOverlap)
		{
			evt.newfov = 1 / 6.0F;
		}
	}

	@SubscribeEvent
	public void onRenderHand(RenderHandEvent evt)
	{
		if (showScopeOverlap)
		{
			evt.setCanceled(true);
		}
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onRender(RenderGameOverlayEvent evt)
	{

		if (showScopeOverlap && (Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem() != selected || !Mouse
				.isButtonDown(0)))
		{
			showScopeOverlap = false;
		}
		if (showScopeOverlap)
		{
			// Render scope
			if (evt.type == RenderGameOverlayEvent.ElementType.HELMET)
			{
				if (mc.gameSettings.thirdPersonView == 0)
				{
					evt.setCanceled(true);
					showScope();
				}
			}
			// Remove crosshairs
			else if (evt.type == RenderGameOverlayEvent.ElementType.CROSSHAIRS && showScopeOverlap) evt
					.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void holding(RenderLivingEvent.Pre event)
	{
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer thePlayer = mc.thePlayer;
		ItemStack stack = thePlayer.getCurrentEquippedItem();
		if (!event.isCanceled() && event.entity.equals(thePlayer) && stack != null)
		{
			if (stack.getItem() instanceof ItemRangedWeapon)
			{
				ModelPlayer model = (ModelPlayer) event.renderer.getMainModel();
				model.aimedBow = true;
			}
		}
	}

	public void showScope()
	{
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer thePlayer = mc.thePlayer;

		// add sway
		swayTicks++;
		if (swayTicks > maxTicks)
		{
			swayTicks = 0;
			if (!thePlayer.isSneaking())
			{
				yawSway = ((random.nextFloat() * 2 - 1) / 5) / maxTicks;
				pitchSway = ((random.nextFloat() * 2 - 1) / 5) / maxTicks;
			}
			else
			{
				yawSway = ((random.nextFloat() * 2 - 1) / 16) / maxTicks;
				pitchSway = ((random.nextFloat() * 2 - 1) / 16) / maxTicks;
			}
		}

		EntityPlayer p = mc.thePlayer;
		p.rotationPitch += yawSway;
		p.rotationYaw += pitchSway;

		GL11.glPushMatrix();
		mc.entityRenderer.setupOverlayRendering();
		GL11.glEnable(GL11.GL_BLEND);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_ALPHA_TEST);

		mc.renderEngine.bindTexture(OVERLAY_TEXTURE);

		ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		double width = res.getScaledWidth_double();
		double height = res.getScaledHeight_double();

		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();

		worldrenderer.startDrawingQuads();
		worldrenderer.addVertexWithUV(0.0D, height, -90.0D, 0.0D, 1.0D);
		worldrenderer.addVertexWithUV(width, height, -90.0D, 1.0D, 1.0D);
		worldrenderer.addVertexWithUV(width, 0.0D, -90.0D, 1.0D, 0.0D);
		worldrenderer.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
		tessellator.draw();

		GL11.glPopMatrix();
	}

	public static void doReload()
	{
		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
		ItemStack stack = player.getCurrentEquippedItem();
		if (stack != null && stack.getItem() instanceof ItemRangedWeapon)
		{
			ItemRangedWeapon weapon = (ItemRangedWeapon) stack.getItem();
			if (!weapon.isReloading(stack) && weapon.canReload(stack, player))
			{
				ARKCraft.modChannel.sendToServer(new ReloadStarted());
				weapon.setReloading(stack, player, true);
			}
		}
	}

	@SubscribeEvent
	public void onPlayerKeypressed(InputEvent.KeyInputEvent event)
	{
		if (reload.isPressed())
		{
			doReload();
		}
		else if (attachment.isPressed())
		{
			EntityPlayer player = mc.thePlayer;
			if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem()
					.getItem() instanceof ItemRangedWeapon && !(player.getCurrentEquippedItem()
					.getItem() instanceof NonSupporting))
			{
				player.openGui(ARKCraft.instance, GlobalAdditions.GUI.ATTACHMENT_GUI.getID(),
						player.worldObj, 0, 0, 0);
				ARKCraft.modChannel.sendToServer(new OpenAttachmentInventory());
			}
		}
	}
}
