package com.arkcraft.mod.core.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.arkcraft.mod.core.GlobalAdditions;
/**
 * @author Vastatio
 */
public class ARKFood extends ItemFood {
	
	private PotionEffect[] effects;
	private boolean alwaysEdible;
	
	public ARKFood(String name, int healAmount, float sat, boolean fav, boolean alwaysEdible, PotionEffect... effects) {
		super(healAmount, sat, fav);
		this.setUnlocalizedName(name);
		this.setCreativeTab(GlobalAdditions.tabARK);
		GameRegistry.registerItem(this, name);
		this.effects = effects;
		this.alwaysEdible = alwaysEdible;
		
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
				if (stack.getItem() == (Item) GlobalAdditions.amarBerrySeed)
					return new ItemStack(GlobalAdditions.amarBerry);
				if (stack.getItem() == (Item) GlobalAdditions.azulBerrySeed)
					return new ItemStack(GlobalAdditions.azulBerry);
				if (stack.getItem() == (Item) GlobalAdditions.mejoBerrySeed)
					return new ItemStack(GlobalAdditions.mejoBerry);
				if (stack.getItem() == (Item) GlobalAdditions.narcoBerrySeed)
					return new ItemStack(GlobalAdditions.narcoBerry);
				if (stack.getItem() == (Item) GlobalAdditions.tintoBerrySeed)
					return new ItemStack(GlobalAdditions.tintoBerry);
			}
		}
		return null;
	}

	// ticks that this food will tame a Dino
	public static int getItemFeedTime(ItemStack stack) {
		if (stack != null) {
			if (stack.getItem() instanceof ARKFood) {
				if (stack.getItem() == (Item) GlobalAdditions.porkchop_cooked)
					return 500;
				if (stack.getItem() == (Item) GlobalAdditions.porkchop_raw)
					return 250;
				if (stack.getItem() == (Item) GlobalAdditions.primemeat_cooked)
					return 1000;
				if (stack.getItem() == (Item) GlobalAdditions.primemeat_raw)
					return 500;
			}
		}
		return 0;
	}
	
	// ticks that this food will keep a dino unconscious
	public static int getItemTorporTime(ItemStack stack) {
		if (stack != null) {
			if (stack.getItem() instanceof ARKFood) {
				if (stack.getItem() == (Item) GlobalAdditions.narcoBerry)
					return 500;
			}
		}
		return 0;
	}
}
