package com.arkcraft.mod.common.items;

import com.arkcraft.mod.GlobalAdditions;
import com.arkcraft.mod.common.items.weapons.projectiles.EntitySpear;
import com.arkcraft.mod.common.lib.BALANCE;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ARKWeaponThrowable extends ItemSword {
	
	public static double spearDamage = BALANCE.WEAPONS.SPEAR_DAMAGE;

	public ARKWeaponThrowable(String name, ToolMaterial m) {
		super(m);
		this.setUnlocalizedName(name);
		this.setCreativeTab(GlobalAdditions.tabARK);
		GameRegistry.registerItem(this, name);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		// Only spawn new entities on the server!
		if (!world.isRemote) {
			//Decrease an item from the stack because you just used it!
			if (!player.capabilities.isCreativeMode)
				--stack.stackSize;
	
			world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
		
			/* This method will spawn an entity in the World, you can use with anything that extends
			* the Entity class, in this case it's the EntitySpear class
			*/
			world.spawnEntityInWorld(new EntitySpear(world, player, 1F));
		}
		return stack;
	}
	
}
