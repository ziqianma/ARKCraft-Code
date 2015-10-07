package com.arkcraft.mod.core.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

import com.google.common.collect.Lists;

public class ARKShapelessRecipe implements IARKRecipe {
    /** Is the ItemStack that you get when craft the recipe. */
    private final ItemStack recipeOutput;
    /** Is a List of ItemStack that composes the recipe. */
    @SuppressWarnings("rawtypes")
	public final List recipeItems;

    @SuppressWarnings("rawtypes")
	public ARKShapelessRecipe(ItemStack output, List inputList) {
        this.recipeOutput = output;
        this.recipeItems = inputList;
    }

    public ItemStack getRecipeOutput(){
        return this.recipeOutput;
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     * Only call with craft = true after ensuring enough inventory with a previous call with it set to false
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean matches(ItemStack[] itemStacksInventory, boolean craft){
    	ArrayList recipelist = Lists.newArrayList(this.recipeItems);
        for (int i = 0; i < itemStacksInventory.length; ++i){
        	ItemStack itemstack = itemStacksInventory[i];
            if (itemstack != null){
            	boolean sufficientInventory = false;
                Iterator recipeIterator = recipelist.iterator();
                while (recipeIterator.hasNext()){
                	ItemStack itemstackInRecipe = (ItemStack)recipeIterator.next();
                	if (itemstack.getItem() == itemstackInRecipe.getItem() && itemstack.stackSize >= itemstackInRecipe.stackSize 
                        && (itemstackInRecipe.getMetadata() == 32767 || itemstack.getMetadata() == itemstackInRecipe.getMetadata())){
                        sufficientInventory = true;
                        recipelist.remove(itemstackInRecipe);
                        if (craft) {
                        	itemStacksInventory[i].stackSize -= itemstackInRecipe.stackSize;
                        	if (itemStacksInventory[i].stackSize <= 0)
                        		itemStacksInventory[i] = null;
                        }
                        break;
                    }
                }
                if (!sufficientInventory){
                	return false;
                }
            }
        }
        return recipelist.isEmpty();
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     */
//    @SuppressWarnings({ "rawtypes", "unchecked" })
//    public boolean matches(InventoryCrafting p_77569_1_, World worldIn){
//		ArrayList arraylist = Lists.newArrayList(this.recipeItems);
//        for (int i = 0; i < p_77569_1_.getHeight(); ++i){
//            for (int j = 0; j < p_77569_1_.getWidth(); ++j){
//                ItemStack itemstack = p_77569_1_.getStackInRowAndColumn(j, i);
//                if (itemstack != null){
//                    boolean flag = false;
//                    Iterator iterator = arraylist.iterator();
//                    while (iterator.hasNext()){
//                        ItemStack itemstack1 = (ItemStack)iterator.next();
//                        if (itemstack.getItem() == itemstack1.getItem() && 
//                        		(itemstack1.getMetadata() == 32767 || itemstack.getMetadata() == itemstack1.getMetadata())){
//                            flag = true;
//                            arraylist.remove(itemstack1);
//                            break;
//                        }
//                    }
//                    if (!flag){
//                        return false;
//                    }
//                }
//            }
//        }
//        return arraylist.isEmpty();
//    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack getCraftingResult(InventoryCrafting p_77572_1_)  {
        return this.recipeOutput.copy();
    }

    /**
     * Returns the size of the recipe area
     */
    public int getRecipeSize() {
        return this.recipeItems.size();
    }
}
