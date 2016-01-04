package com.arkcraft.module.item.common.items;

import com.arkcraft.module.item.common.config.ModuleItemBalance;
import com.arkcraft.module.item.common.entity.item.projectiles.EntitySpear;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;

public class ItemARKThrowableWeaponBase extends ItemSword
{

    public static double spearDamage = ModuleItemBalance.WEAPONS.SPEAR_DAMAGE;

    public ItemARKThrowableWeaponBase(ToolMaterial m)
    {
        super(m);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        // Only spawn new entities on the server!
        if (!world.isRemote)
        {
            //Decrease an item from the stack because you just used it!
            if (!player.capabilities.isCreativeMode)
            {
                --stack.stackSize;
            }

            world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

			/* This method will spawn an entity in the World, you can use with anything that extends
			* the Entity class, in this case it's the EntitySpear class
			*/
            world.spawnEntityInWorld(new EntitySpear(world, player, 1F));
        }
        return stack;
    }

}
