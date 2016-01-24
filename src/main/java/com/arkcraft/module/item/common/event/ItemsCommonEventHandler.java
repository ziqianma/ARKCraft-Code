package com.arkcraft.module.item.common.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.arkcraft.lib.LogHelper;
import com.arkcraft.module.core.common.entity.EntityTameableDinosaur;
import com.arkcraft.module.item.common.config.ModuleItemBalance;
import com.arkcraft.module.item.common.entity.item.projectiles.EntityTranqArrow;
import com.arkcraft.module.item.common.entity.player.ARKPlayer;

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
		if (!event.entity.worldObj.isRemote && event.entity instanceof EntityTameableDinosaur)
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
	 * @author Jodelahithit This makes logs drop thatch, all pickaxes and axes
	 *         are recognised (tools from other mods too). Axe drops 0-2 thatch
	 *         and pickaxe 1-5.
	 */

	// @SubscribeEvent public void onDrops(BlockEvent.HarvestDropsEvent event) {
	//
	// Random r = new Random();
	//
	// //Stone Tool int j = r.nextInt(2)+1; int s = r.nextInt(1)+1; //Metal Tool
	// int k = r.nextInt(4)+1; int p = r.nextInt(3);
	//
	// if (event.harvester != null && event.harvester.getHeldItem() != null &&
	// event.state.getBlock() == Blocks.log || event.state.getBlock() ==
	// Blocks.log2) {
	//
	// LogHelper.info("Harvest Event");
	// if(event.harvester.getHeldItem().getItem() instanceof ItemStonePick){
	// event.drops.clear(); for (int i = 0; i < j; i++) { event.drops.add(new
	// ItemStack(ARKCraftItems.thatch)); } for (int x = 0; x < s; x++) {
	// event.drops.add(new ItemStack(ARKCraftItems.wood)); } } else
	// if(event.harvester.getHeldItem().getItem() instanceof ItemStoneHatchet){
	// event.drops.clear(); for (int i = 0; i < j; i++) { event.drops.add(new
	// ItemStack(ARKCraftItems.wood)); } for (int x = 0; x < s; x++) {
	// event.drops.add(new ItemStack(ARKCraftItems.thatch)); } } else
	// if(event.harvester.getHeldItem().getItem() instanceof ItemMetalHatchet){
	// event.drops.clear(); for (int i = 0; i < k; i++) { event.drops.add(new
	// ItemStack(ARKCraftItems.wood)); } for (int x = 0; x < p; x++) {
	// event.drops.add(new ItemStack(ARKCraftItems.thatch)); } } else
	// if(event.harvester.getHeldItem().getItem() instanceof ItemMetalPick){
	// event.drops.clear(); for (int i = 0; i < k; i++) { event.drops.add(new
	// ItemStack(ARKCraftItems.thatch)); } for (int x = 0; x < p; x++) {
	// event.drops.add(new ItemStack(ARKCraftItems.wood)); } } }
	// if(event.state.getBlock() == Blocks.stone) {
	//
	// if(event.harvester.getHeldItem().getItem() instanceof ItemStonePick){
	// event.drops.clear(); for (int i = 0; i < j; i++) { event.drops.add(new
	// ItemStack(ARKCraftItems.flint)); } for (int x = 0; x < s; x++) {
	// event.drops.add(new ItemStack(ARKCraftItems.rock)); } for (int y = 0; y <
	// s*0.5; y++) { event.drops.add(new ItemStack(ARKCraftItems.metal)); } }
	// else if(event.harvester.getHeldItem().getItem() instanceof
	// ItemStoneHatchet){ event.drops.clear(); for (int i = 0; i < j; i++) {
	// event.drops.add(new ItemStack(ARKCraftItems.rock)); } for (int x = 0; x <
	// s; x++) { event.drops.add(new ItemStack(ARKCraftItems.flint)); } for (int
	// y = 0; y < s*0.3; y++) { event.drops.add(new
	// ItemStack(ARKCraftItems.metal)); } } else
	// if(event.harvester.getHeldItem().getItem() instanceof ItemMetalHatchet){
	// event.drops.clear(); for (int i = 0; i < p; i++) { event.drops.add(new
	// ItemStack(ARKCraftItems.rock)); } for (int x = 0; x < j; x++) {
	// event.drops.add(new ItemStack(ARKCraftItems.flint)); } for (int y = 0; y
	// < s+0.5; y++) { event.drops.add(new ItemStack(ARKCraftItems.metal)); } }
	// else if(event.harvester.getHeldItem().getItem() instanceof
	// ItemMetalPick){ event.drops.clear(); for (int i = 0; i < p; i++) {
	// event.drops.add(new ItemStack(ARKCraftItems.flint)); } for (int x = 0; x
	// < j; x++) { event.drops.add(new ItemStack(ARKCraftItems.rock)); } for
	// (int y = 0; y < s+0.7; y++) { event.drops.add(new
	// ItemStack(ARKCraftItems.metal)); } } } if(event.state.getBlock() ==
	// Blocks.iron_ore) {
	//
	// if(event.harvester.getHeldItem().getItem() instanceof ItemStonePick){
	// event.drops.clear(); for (int i = 0; i < j; i++) { event.drops.add(new
	// ItemStack(ARKCraftItems.metal)); } } else
	// if(event.harvester.getHeldItem().getItem() instanceof ItemStoneHatchet){
	// event.drops.clear(); for (int i = 0; i < j; i++) { event.drops.add(new
	// ItemStack(ARKCraftItems.metal)); } } else
	// if(event.harvester.getHeldItem().getItem() instanceof ItemMetalHatchet){
	// event.drops.clear(); for (int i = 0; i < k; i++) { event.drops.add(new
	// ItemStack(ARKCraftItems.metal)); } } else
	// if(event.harvester.getHeldItem().getItem() instanceof ItemMetalPick){
	// event.drops.clear(); for (int i = 0; i < p; i++) { event.drops.add(new
	// ItemStack(ARKCraftItems.metal)); } } } }

	@SubscribeEvent
	public void damagePlayerFromPunching(PlayerEvent.BreakSpeed event)
	{
		EntityPlayer player = event.entityPlayer;

		if (player.getHeldItem() == null)
		{
			player.attackEntityFrom(DamageSource.generic, 1.0F); // TODO new
																	// damage
																	// source
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

	/*
	@SubscribeEvent
	public void playerTick(TickEvent.PlayerTickEvent event)
	{
		EntityPlayer thePlayer = event.player;

		ItemStack stack = thePlayer.getCurrentEquippedItem();

		if (stack != null && stack.getItem() instanceof IItemWeapon)
		{
			TileInventoryAttachment inv = new TileInventoryAttachment(stack);
			MovingObjectPosition mop = rayTrace(thePlayer, 20, 1.0F);

			if (inv.isFlashPresent())
			{
				if (mop != null)
				{
					if (!(mop.typeOfHit == MovingObjectPosition.MovingObjectType.MISS))
					{
						BlockPos pos;

						if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY)
						{
							pos = mop.entityHit.getPosition();
						}
						else
						{
							pos = mop.getBlockPos();
							pos = pos.offset(mop.sideHit);
						}

						if (thePlayer.worldObj.getBlockState(pos).getBlock() == ARKCraftBlocks.block_flashlight)
						{
							TileFlashlight tileLight = (TileFlashlight) thePlayer.worldObj
									.getTileEntity(pos);
							tileLight.ticks = 0;
						}
						else if (thePlayer.worldObj.isAirBlock(pos))
						{
							thePlayer.worldObj.setBlockState(pos,
									ARKCraftBlocks.block_flashlight.getDefaultState());

						}
					}
				}
			}
		}
	}

	public Vec3 getPositionEyes(EntityPlayer player, float partialTick)
	{
		if (partialTick == 1.0F)
		{
			return new Vec3(player.posX, player.posY + (double) player.getEyeHeight(), player.posZ);
		}
		else
		{
			double d0 = player.prevPosX + (player.posX - player.prevPosX) * (double) partialTick;
			double d1 = player.prevPosY + (player.posY - player.prevPosY) * (double) partialTick + (double) player
					.getEyeHeight();
			double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * (double) partialTick;
			return new Vec3(d0, d1, d2);
		}
	}

	public MovingObjectPosition rayTrace(EntityPlayer player, double distance, float partialTick)
	{
		Vec3 vec3 = getPositionEyes(player, partialTick);
		Vec3 vec31 = player.getLook(partialTick);
		Vec3 vec32 = vec3.addVector(vec31.xCoord * distance, vec31.yCoord * distance,
				vec31.zCoord * distance);
		return player.worldObj.rayTraceBlocks(vec3, vec32, false, false, true);
	}	*/

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
