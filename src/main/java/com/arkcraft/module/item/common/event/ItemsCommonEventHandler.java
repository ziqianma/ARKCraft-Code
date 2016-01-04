package com.arkcraft.module.item.common.event;

import com.arkcraft.lib.LogHelper;
import com.arkcraft.module.core.GlobalAdditions;
import com.arkcraft.module.core.common.network.OpenPlayerCrafting;
import com.arkcraft.module.item.common.config.KeyBindings;
import com.arkcraft.module.item.common.entity.item.projectiles.EntityBallista;
import com.arkcraft.module.item.common.entity.item.projectiles.EntityTranqArrow;
import com.arkcraft.module.item.common.entity.player.ARKPlayer;
import com.arkcraft.module.item.common.items.ARKCraftItems;
import com.arkcraft.module.item.common.items.weapons.handlers.IItemWeapon;
import com.arkcraft.module.core.ARKCraft;
import com.arkcraft.module.core.common.entity.EntityTameableDinosaur;
import com.arkcraft.module.item.common.config.ModuleItemBalance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;

/**
 * @author wildbill22
 */
public class ItemsCommonEventHandler
{
    public static void init()
	{
        ItemsCommonEventHandler handler = new ItemsCommonEventHandler();
        FMLCommonHandler.instance().bus().register(handler);
        MinecraftForge.EVENT_BUS.register(handler);
    }

    @SubscribeEvent
    public void onLivingHurtEvent(LivingHurtEvent event)
    {
        if (!event.entity.worldObj.isRemote
                && event.entity instanceof EntityTameableDinosaur)
        {
            if (event.source.isProjectile())
            {
                if (event.source.getEntity() instanceof EntityTranqArrow)
                {
                    if (event.entity instanceof EntityTameableDinosaur)
                    {
                        ((EntityTameableDinosaur) event.entity)
                                .increaseTorpor(ModuleItemBalance.WEAPONS.TRANQ_AMMO_TORPOR_TIME);
                    }
                }
            }
        }
    }

    /**
     * @author Jodelahithit
     * This makes logs drop thatch, all pickaxes and axes are recognised (tools from other mods too). Axe drops 0-2 thatch and pickaxe 1-5.
     */
	
	/*
	@SubscribeEvent
	public void onDrops(BlockEvent.HarvestDropsEvent event) {
		
		Random r = new Random();
		
		//Stone Tool
		int j = r.nextInt(2)+1;
		int s = r.nextInt(1)+1;
		//Metal Tool
		int k = r.nextInt(4)+1;
		int p = r.nextInt(3);
					
		if (event.harvester != null && event.harvester.getHeldItem() != null && event.state.getBlock() == Blocks.log || event.state.getBlock() == Blocks.log2) {
	
		LogHelper.info("Harvest Event");
			if(event.harvester.getHeldItem().getItem() instanceof ItemStonePick){	
					event.drops.clear();
					for (int i = 0; i < j; i++)
					{
						event.drops.add(new ItemStack(ARKCraftItems.thatch));
					}
					for (int x = 0; x < s; x++)
					{
						event.drops.add(new ItemStack(ARKCraftItems.wood));	
					}
				}		
		        else if(event.harvester.getHeldItem().getItem() instanceof ItemStoneHatchet){
		        	event.drops.clear();
					for (int i = 0; i < j; i++)
					{
						event.drops.add(new ItemStack(ARKCraftItems.wood));
					}
					for (int x = 0; x < s; x++)
					{
						event.drops.add(new ItemStack(ARKCraftItems.thatch));
					}
				}			
				else if(event.harvester.getHeldItem().getItem() instanceof ItemMetalHatchet){
					event.drops.clear();
					for (int i = 0; i < k; i++)
					{
						event.drops.add(new ItemStack(ARKCraftItems.wood));
					}
					for (int x = 0; x < p; x++)
					{
						event.drops.add(new ItemStack(ARKCraftItems.thatch));
					}
				}
				else if(event.harvester.getHeldItem().getItem() instanceof ItemMetalPick){
					event.drops.clear();
					for (int i = 0; i < k; i++)
					{
						event.drops.add(new ItemStack(ARKCraftItems.thatch));
					}
					for (int x = 0; x < p; x++)
					{
						event.drops.add(new ItemStack(ARKCraftItems.wood));
					}
				}		
			}
			if(event.state.getBlock() == Blocks.stone)
			{
				
				if(event.harvester.getHeldItem().getItem() instanceof ItemStonePick){
					event.drops.clear();
					for (int i = 0; i < j; i++)
					{
						event.drops.add(new ItemStack(ARKCraftItems.flint));
					}
					for (int x = 0; x < s; x++)
					{
						event.drops.add(new ItemStack(ARKCraftItems.rock));	
					}
					for (int y = 0; y < s*0.5; y++)
					{
						event.drops.add(new ItemStack(ARKCraftItems.metal));	
					}
				}
				else if(event.harvester.getHeldItem().getItem() instanceof ItemStoneHatchet){
					event.drops.clear();
					for (int i = 0; i < j; i++)
					{
						event.drops.add(new ItemStack(ARKCraftItems.rock));
					}
					for (int x = 0; x < s; x++)
					{
						event.drops.add(new ItemStack(ARKCraftItems.flint));	
					}
					for (int y = 0; y < s*0.3; y++)
					{
						event.drops.add(new ItemStack(ARKCraftItems.metal));	
					}
				}		
				else if(event.harvester.getHeldItem().getItem() instanceof ItemMetalHatchet){
					event.drops.clear();
					for (int i = 0; i < p; i++)
					{
						event.drops.add(new ItemStack(ARKCraftItems.rock));
					}
					for (int x = 0; x < j; x++)
					{
						event.drops.add(new ItemStack(ARKCraftItems.flint));
					}
					for (int y = 0; y < s+0.5; y++)
					{
						event.drops.add(new ItemStack(ARKCraftItems.metal));	
					}
				 }
				 else if(event.harvester.getHeldItem().getItem() instanceof ItemMetalPick){
					event.drops.clear();
					for (int i = 0; i < p; i++)
					{
						event.drops.add(new ItemStack(ARKCraftItems.flint));
					}
					for (int x = 0; x < j; x++)
					{
						event.drops.add(new ItemStack(ARKCraftItems.rock));
					}
					for (int y = 0; y < s+0.7; y++)
					{
						event.drops.add(new ItemStack(ARKCraftItems.metal));	
					}					
				}		
			}
			if(event.state.getBlock() == Blocks.iron_ore)
			{
				
				if(event.harvester.getHeldItem().getItem() instanceof ItemStonePick){
					event.drops.clear();
					for (int i = 0; i < j; i++)
					{
						event.drops.add(new ItemStack(ARKCraftItems.metal));
					}
				}
				else if(event.harvester.getHeldItem().getItem() instanceof ItemStoneHatchet){
					event.drops.clear();
					for (int i = 0; i < j; i++)
					{
						event.drops.add(new ItemStack(ARKCraftItems.metal));
					}
				}
				else if(event.harvester.getHeldItem().getItem() instanceof ItemMetalHatchet){
					event.drops.clear();
					for (int i = 0; i < k; i++)
					{	
						event.drops.add(new ItemStack(ARKCraftItems.metal));
					}
				}
				else if(event.harvester.getHeldItem().getItem() instanceof ItemMetalPick){
					event.drops.clear();
					for (int i = 0; i < p; i++)
					{
						event.drops.add(new ItemStack(ARKCraftItems.metal));
					}
				}
			}							
		}
	*/

    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final ResourceLocation OVERLAY_TEXTURE = new ResourceLocation(ARKCraft.MODID, "textures/gui/scope.png");
    public boolean ShowScopeOverlap = false;

    @SubscribeEvent
    public void onMouseEvent(MouseEvent evt)
    {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer thePlayer = mc.thePlayer;

        if (thePlayer != null && evt.button == 0)
        {
            LogHelper.info("mouse down");
            ItemStack stack = thePlayer.getCurrentEquippedItem();
            if (stack != null)
            {
                IItemWeapon i_item_weapon;
                if (stack.getItem() instanceof IItemWeapon)
                {
                    i_item_weapon = (IItemWeapon) stack.getItem();
                }
                else
                {
                    i_item_weapon = null;
                }
                // Weapon with scope?
                if (i_item_weapon != null && i_item_weapon.ifCanScope())
                {
					if (evt.buttonstate)
					{
						ShowScopeOverlap = true;
					}
					else
					{
						ShowScopeOverlap = false;
					}
                    evt.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public void onFOVUpdate(FOVUpdateEvent evt)
    {
        if (mc.gameSettings.thirdPersonView == 0 && ShowScopeOverlap)
        {
            evt.newfov = 1 / 6.0F;
        }
    }

    @SubscribeEvent
    public void onRenderHand(RenderHandEvent evt)
    {
        if (ShowScopeOverlap)
        {
            evt.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onRender(RenderGameOverlayEvent evt)
    {
        if (evt.type == RenderGameOverlayEvent.ElementType.HELMET)
        {
            if (mc.gameSettings.thirdPersonView == 0 && ShowScopeOverlap)
            {
                evt.setCanceled(true); // Removes the normal crosshairs and uses just the overlay crosshairs
                LogHelper.info("onRender ShowScopeOverlap = true");
                ShowScope();
            }
        }
    }

    @SubscribeEvent
    public void Holding(RenderLivingEvent.Pre event)
    {
        //!event.isCanceled() &
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer thePlayer = mc.thePlayer;
        if (!event.isCanceled() & event.entity instanceof EntityPlayer && ShowScopeOverlap)
        {
            ItemStack stack = thePlayer.getCurrentEquippedItem();
            if (stack != null)
            {
                IItemWeapon i_item_weapon;
                if (stack.getItem() instanceof IItemWeapon)
                {
                    i_item_weapon = (IItemWeapon) stack.getItem();
                }
                else
                {
                    i_item_weapon = null;
                }
                if (i_item_weapon != null)
                {

                    ModelPlayer model = (ModelPlayer) event.renderer.getMainModel();

                    model.aimedBow = true;
                }

		        /*	
		        ModelPlayer model = (ModelPlayer)event.renderer.getMainModel();
				model.bipedLeftArm.rotateAngleX = model.bipedLeftArm.rotateAngleX * 0.5F - ((float)Math.PI / 10F) * (float)model.heldItemLeft;
				model.heldItemRight = 0;
				
				/*
				if (model.heldItemLeft != 0)
				{
					model.bipedLeftArm.rotateAngleX = model.bipedLeftArm.rotateAngleX * 0.5F - ((float)Math.PI / 10F) * (float)model.heldItemLeft;
				}
				switch (model.heldItemRight)
				{
				    case 0:
				    case 2:
				    default:
				        break;
				    case 1:
				    	model.bipedRightArm.rotateAngleX = model.bipedRightArm.rotateAngleX * 0.5F - ((float)Math.PI / 10F) * (float)model.heldItemRight;
				        break;
				    case 3:
				    	model.bipedRightArm.rotateAngleX = model.bipedRightArm.rotateAngleX * 0.5F - ((float)Math.PI / 10F) * (float)model.heldItemRight;
				    	model.bipedRightArm.rotateAngleY = -0.5235988F;
				}	*/

            }
        }
    }

    public void ShowScope()
    {
        LogHelper.info("In ShowScope");
        Minecraft mc = Minecraft.getMinecraft();
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

	@SubscribeEvent
	public void onPlayerKeypressed(InputEvent.KeyInputEvent event)
	{
		if (KeyBindings.playerPooping.isPressed())
		{
			EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
			if (player instanceof EntityPlayerSP)
			{
				ARKPlayer.get(player).Poop();
			}
		}
		else if (KeyBindings.playerCrafting.isPressed())
		{
			EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
			if (player instanceof EntityPlayerSP)
			{
				player.openGui(ARKCraft.instance(), GlobalAdditions.GUI.PLAYER.getID(), player.worldObj, 0, 0, 0);
				ARKCraft.modChannel.sendToServer(new OpenPlayerCrafting(true));
			}
		}
		else if (KeyBindings.attachment.isPressed())
		{
			EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
			if (player instanceof EntityPlayerSP && player.getHeldItem().getItem() == ARKCraftItems.longneck_rifle || player.getHeldItem().getItem() == ARKCraftItems.longneck_rifle_scoped)
			{
				LogHelper("Found Longneckrifle");
				player.openGui(ARKCraft.instance(), GlobalAdditions.GUI.ATTACHMENT_GUI.getID(), player.worldObj, 0, 0, 0);
				//		ARKCraft.modChannel.sendToServer(new OpenPlayerCrafting(true));
			}

		}

	}

	private void LogHelper(String string)
	{
		// TODO Auto-generated method stub

	}


	@SubscribeEvent
	public void onPlayerTickEvent(TickEvent.PlayerTickEvent event)
	{
		// Update CraftingInventory
		if (ARKPlayer.get(event.player).getInventoryBlueprints().isCrafting())
		{
			ARKPlayer.get(event.player).getInventoryBlueprints().update();
		}
		else if (event.phase == TickEvent.Phase.START && event.player instanceof EntityPlayerSP)
		{
			EntityPlayerSP entity = (EntityPlayerSP) event.player;
			if (entity.movementInput.jump && entity.ridingEntity instanceof EntityBallista && ((EntityBallista) entity.ridingEntity).isLoaded())
			{
				((EntityBallista) entity.ridingEntity).fireBallista();
				//	MsgBallistaShot msg = new MsgBallistaShot((EntityBallista)entity.ridingEntity);
				//	ARKCraft.instance.messagePipeline.sendToServer(msg);
			}
		}
		//	else if(event.player instanceof EntityPlayer){
		//		EntityPlayer player = (EntityPlayer) event.player;
		//     	ItemStack stack = player.getHeldItem();
		//  IItemWeapon i_item_weapon = null;
		//    if (stack.getItem() instanceof IItemWeapon){
		//  	i_item_weapon = (IItemWeapon) stack.getItem();
		//		TileInventoryAttachment tileInventoryAttachment = new TileInventoryAttachment(stack);
		//		if(stack != null && stack.getItem() == ARKCraftItems.longneck_rifle)
		//		{
		//			LogHelper("It is working mate");
		//	i_item_weapon.ifCanScope();

	}
	//}
	//}

	public boolean canShowScope()
	{
		return true;
	}

	@SubscribeEvent
	public void damagePlayerFromPunching(PlayerEvent.BreakSpeed event)
	{
		EntityPlayer player = event.entityPlayer;

		if (player.getHeldItem() == null)
		{
			player.attackEntityFrom(DamageSource.generic, 1.0F); //TODO new damage source
		}
	}

	// Add ARKPlayer properties to player
	@SubscribeEvent
	public void onEntityConstructing(EntityEvent.EntityConstructing event)
	{
		if (event.entity instanceof EntityPlayer && ARKPlayer.get((EntityPlayer) event.entity) == null)
		{
			ARKPlayer.register((EntityPlayer) event.entity, event.entity.worldObj);
			if (event.entity.worldObj.isRemote) // On client
			{
				LogHelper.info("ARKPlayerEventHandler: Registered a new ARKPlayer on client.");
			}
			else
			{
				LogHelper.info("ARKPlayerEventHandler: Registered a new ARKPlayer on server.");
			}
		}
	}

	@SubscribeEvent
	public void onClonePlayer(PlayerEvent.Clone event)
	{
		LogHelper.info("ARKPlayerEventHandler: Cloning player extended properties");
		ARKPlayer.get(event.entityPlayer).copy(ARKPlayer.get(event.original));
	}

	@SubscribeEvent
	public void onLivingUpdateEvent(LivingEvent.LivingUpdateEvent event)
	{
		if (event.entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.entity;
			// Enable pooping once every (the value in the config) ticks
			if (player.ticksExisted % ModuleItemBalance.PLAYER.TICKS_BETWEEN_PLAYER_POOP == 0)
			{
				ARKPlayer.get(player).setCanPoop(true);
			}
		}
	}
}
