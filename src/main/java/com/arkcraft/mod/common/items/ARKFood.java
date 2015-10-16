package com.arkcraft.mod.common.items;

import com.arkcraft.mod.GlobalAdditions;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * @author Vastatio
 */
public class ARKFood extends ItemFood {
	
	private PotionEffect[] effects;
	private boolean alwaysEdible;
	public static int globalHealAmount; //Used For Adding XP On Register
	
	public ARKFood(String name, int healAmount, float sat, boolean fav, boolean alwaysEdible, PotionEffect... effects) {
		super(healAmount, sat, fav);
		this.setUnlocalizedName(name);
		this.setCreativeTab(GlobalAdditions.tabARK);
		GameRegistry.registerItem(this, name);
		this.effects = effects;
		this.alwaysEdible = alwaysEdible;
		this.globalHealAmount = healAmount;
		
	}

	@Override
	protected void onFoodEaten(ItemStack stack, World worldIn,
			EntityPlayer player) {
		super.onFoodEaten(stack, worldIn, player);
		for (int i = 0; i < effects.length; i ++) {
	        if (!worldIn.isRemote && effects[i] != null && effects[i].getPotionID() > 0)
	            player.addPotionEffect(new PotionEffect(this.effects[i].getPotionID(), this.effects[i].getDuration(), this.effects[i].getAmplifier(), this.effects[i].getIsAmbient(), this.effects[i].getIsShowParticles()));
	    }
	}
	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
    {
        if (playerIn.canEat(this.alwaysEdible))
        {
            playerIn.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));
        }

        return itemStackIn;
    }
		
	@Override
	  public ItemFood setAlwaysEdible()
    {
        this.alwaysEdible = true;
        return this;
    }
	
	public static ItemStack getSeedForBerry(ItemStack stack) {
		if (stack != null) {
			if (stack.getItem() instanceof ARKSeedItem) {
				if (stack.getItem() == (Item) ARKCraftItems.amarBerrySeed)
					return new ItemStack(ARKCraftItems.amarBerry);
				if (stack.getItem() == (Item) ARKCraftItems.azulBerrySeed)
					return new ItemStack(ARKCraftItems.azulBerry);
				if (stack.getItem() == (Item) ARKCraftItems.mejoBerrySeed)
					return new ItemStack(ARKCraftItems.mejoBerry);
				if (stack.getItem() == (Item) ARKCraftItems.narcoBerrySeed)
					return new ItemStack(ARKCraftItems.narcoBerry);
				if (stack.getItem() == (Item) ARKCraftItems.tintoBerrySeed)
					return new ItemStack(ARKCraftItems.tintoBerry);
			}
		}
		return null;
	}

	// Seconds that this food will tame a Dino
	public static int getItemFeedTime(ItemStack stack) {
		if (stack != null) {
			if (stack.getItem() instanceof ARKFood) {
				if (stack.getItem() == (Item) ARKCraftItems.porkchop_cooked)
					return 25;
				if (stack.getItem() == (Item) ARKCraftItems.porkchop_raw)
					return 10;
				if (stack.getItem() == (Item) ARKCraftItems.primemeat_cooked)
					return 50;
				if (stack.getItem() == (Item) ARKCraftItems.primemeat_raw)
					return 25;
			}
		}
		return 0;
	}
	
	// Seconds that this food will keep a dino unconscious
	public static int getItemTorporTime(ItemStack stack) {
		if (stack != null) {
			if (stack.getItem() instanceof ARKFood) {
				if (stack.getItem() == (Item) ARKCraftItems.narcoBerry)
					return 25;
			}
		}
		return 0;
	}
	
	public static int getXPFromSmelting() {
		return globalHealAmount / 2;
	}
}
