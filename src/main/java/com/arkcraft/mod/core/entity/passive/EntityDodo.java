package com.arkcraft.mod.core.entity.passive;

import com.arkcraft.mod.core.GlobalAdditions;
import com.arkcraft.mod.core.entity.ai.EntityDodoAILookIdle;
import com.arkcraft.mod.core.items.ARKFood;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/***
 * 
 * @author wildbill22
 *
 */
public class EntityDodo extends EntityChicken {
//	public boolean eyesOpen;
	private int DODO_WATCHER = 21;
	public boolean isEyesOpen() {
        return (this.dataWatcher.getWatchableObjectByte(DODO_WATCHER) & 1) != 0;
	}
	public void setEyesOpen(boolean eyesOpen) {
		byte b0 = (byte) (eyesOpen ? 1 : 0);
        this.dataWatcher.updateObject(DODO_WATCHER, Byte.valueOf(b0));
	}
	
	public EntityDodo(World worldIn) {
		super(worldIn);
		
//		eyesOpen = true;
		this.getDataWatcher().addObject(DODO_WATCHER, Byte.valueOf((byte)1));
//		setEyesOpen(true);

		// Replace Idle task with one that blinks eyes
		this.tasks.taskEntries.clear();
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 1.4D));
        this.tasks.addTask(2, new EntityAIMate(this, 1.0D));
        this.tasks.addTask(3, new EntityAITempt(this, 1.0D, GlobalAdditions.narcoBerry, false));
        this.tasks.addTask(4, new EntityAIFollowParent(this, 1.1D));
        this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(7, new EntityDodoAILookIdle(this));
	}

	// TODO: Update when we have a dodo feather
	@Override
    protected Item getDropItem() {
        return Items.feather;
    }

    /**
     * Drop 0-2 items of this living's type
     */
	// TODO: Update to drop raw meat when we have that
	@Override
    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
        int j = this.rand.nextInt(3) + this.rand.nextInt(1 + p_70628_2_);

        for (int k = 0; k < j; ++k)
        {
            this.dropItem(Items.feather, 1);
        }

        if (this.isBurning())
        {
            this.dropItem(Items.cooked_chicken, 1);
        }
        else
        {
            this.dropItem(Items.chicken, 1);
        }
    }
	
	@Override
    public EntityDodo createChild(EntityAgeable ageable) {
        return new EntityDodo(this.worldObj);
    }

	/**
     * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on
     * the animal type)
     */
	@Override
    public boolean isBreedingItem(ItemStack stack) {
		if (stack != null) {
			return stack.getItem() instanceof ARKFood;
		}
        return false; 
    }
}
