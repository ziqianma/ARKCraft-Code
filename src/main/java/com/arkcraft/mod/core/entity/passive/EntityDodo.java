package com.arkcraft.mod.core.entity.passive;

import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.world.World;

/***
 * 
 * @author wildbill22
 *
 */
public class EntityDodo extends EntityChicken {

	public EntityDodo(World worldIn) {
		super(worldIn);
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
    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
    {
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
}
