package com.arkcraft.mod.core.items.weapons;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.arkcraft.mod.core.GlobalAdditions;
import com.arkcraft.mod.core.items.weapons.projectiles.EntitySimpleBullet;

public class ItemSimplePistol extends Item{
	
	private int shotDelay = 100;
	//private int recoil = 20;
	private int velocity = 4;

	//private int recoilDown = recoil / 2;
	
	private int firingDelay;
	private boolean hasFired;
	
	public ItemSimplePistol(String name) {
		super();
		this.setUnlocalizedName(name);
		this.setCreativeTab(GlobalAdditions.tabARK);
		GameRegistry.registerItem(this, name);
	}
	
	@Override
	public void onUpdate(ItemStack itemstack, World world, Entity entity, int i, boolean j) {
		EntityPlayer player = (EntityPlayer)entity;

		firingDelay++;
		//recoilDelay++;	
		
//		if(hasRecoiled){
//			if(recoilDelay >= 4){
//				player.rotationPitch = player.rotationPitch + recoilDown;
//				recoilDelay = 0;
//				hasRecoiled = false;
//			}
//		}
	
		if(hasFired){
			hasFired = false;
		}
		
	}

	@SideOnly (Side.CLIENT)
	public boolean isFull3D(){
		return true;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player) {

		// vvv Only for debugging :P
		System.out.println("Firing Delay: " + firingDelay);

		if(firingDelay >= shotDelay){		
				
				player.inventory.consumeInventoryItem(GlobalAdditions.simple_bullet);
				
			//	if(timesFired >= 2){
			//		player.inventory.consumeInventoryItem(Items.gunpowder);
			//		timesFired = 0;
				}
								
				if(player.capabilities.isCreativeMode || player.inventory.hasItem(GlobalAdditions.tranquilizer))
				{
					if (!world.isRemote)
					{			
						SoundHandler.onEntityPlay("tranqGunShot", world, player, 10F, 1F);
						world.spawnEntityInWorld(new EntitySimpleBullet(world, player, velocity));
						firingDelay = 0;
						hasFired = true;
					}
				}
//				player.rotationPitch = player.rotationPitch - recoil;
//				hasRecoiled = true;
			
				return itemstack;
		}
	
	//public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean on){
	//	list.add("");
	//}
}