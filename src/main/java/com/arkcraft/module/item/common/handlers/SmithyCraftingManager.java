package com.arkcraft.module.item.common.handlers;

import com.arkcraft.module.item.common.items.ARKCraftItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

/**
 * @author wildbill22
 *         Notes about adding recipes:
 *         1) If a block has meta data:
 *         a) If not enter in ItemStack as 3rd param, it is set to 0
 *         b) If entered, then just a stack with that meta will match
 *         c) If ARKShapelessRecipe.ANY (32767) is used, all the different meta types for the block will match
 *         2) Do not have two recipes for the same ItemStack, only the first will be used
 */
public class SmithyCraftingManager extends ARKCraftingManager
{

    private static SmithyCraftingManager instance = null;

    public SmithyCraftingManager()
    {
        super();
        instance = this;
    }

    public static SmithyCraftingManager getInstance()
    {
        if (instance == null)
        {
            instance = new SmithyCraftingManager();
        }
        return instance;
    }

    public static void registerSmithyCraftingRecipes()
    {

        //Small Saddle
        getInstance().addShapelessRecipe(
                new ItemStack(ARKCraftItems.saddle_small, 1),
                new ItemStack(ARKCraftItems.hide, 15),
                new ItemStack(ARKCraftItems.metal, 10),
                new ItemStack(ARKCraftItems.fiber, 15)
        );
        //Medium Saddle
        getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.saddle_medium, 1),
                new ItemStack(ARKCraftItems.hide, 48),
                new ItemStack(ARKCraftItems.metal, 32),
                new ItemStack(ARKCraftItems.fiber, 32),
                new ItemStack(Items.diamond, 1)
        );
        //Large Saddle
        getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.saddle_large, 1),
                new ItemStack(ARKCraftItems.chitin, 32),
                new ItemStack(ARKCraftItems.metal, 16),
                new ItemStack(ARKCraftItems.fiber, 32),
                new ItemStack(Items.diamond, 4)
        );

        // Chitin Armor
        getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.chitinChest, 1),
                new ItemStack(ARKCraftItems.fiber, 4),
                new ItemStack(ARKCraftItems.hide, 10),
                new ItemStack(ARKCraftItems.chitin, 20)
        );
        getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.chitinLegs, 1),
                new ItemStack(ARKCraftItems.fiber, 5),
                new ItemStack(ARKCraftItems.hide, 12),
                new ItemStack(ARKCraftItems.chitin, 25)
        );
        getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.chitinHelm, 1),
                new ItemStack(ARKCraftItems.fiber, 3),
                new ItemStack(ARKCraftItems.hide, 7),
                new ItemStack(ARKCraftItems.chitin, 15)
        );
        getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.chitinBoots, 1),
                new ItemStack(ARKCraftItems.fiber, 4),
                new ItemStack(ARKCraftItems.hide, 6),
                new ItemStack(ARKCraftItems.chitin, 12)
        );

        //Spy Glass
        getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.spy_glass, 1),
                new ItemStack(ARKCraftItems.wood, 5),
                new ItemStack(ARKCraftItems.hide, 10),
                new ItemStack(ARKCraftItems.fiber, 10),
                new ItemStack(ARKCraftItems.item_crystal, 2)
        );

        //Scope
        getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.scope, 1),
                new ItemStack(ARKCraftItems.rock, 5),
                new ItemStack(ARKCraftItems.metal, 40),
                new ItemStack(ARKCraftItems.item_crystal, 20)
        );

        //Pike
        getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.ironPike, 1),
                new ItemStack(ARKCraftItems.metal, 10),
                new ItemStack(ARKCraftItems.wood, 10),
                new ItemStack(ARKCraftItems.hide, 20)
        );

        //Metal Pick
        getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.metal_pick, 1),
                new ItemStack(ARKCraftItems.metal, 1),
                new ItemStack(ARKCraftItems.wood, 1),
                new ItemStack(ARKCraftItems.hide, 10)
        );
        //Metal Hatchet
        getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.metal_hatchet, 1),
                new ItemStack(ARKCraftItems.metal, 8),
                new ItemStack(ARKCraftItems.wood, 1),
                new ItemStack(ARKCraftItems.hide, 10)
        );

        //Guns
        //Simple Pistol
        getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.simple_pistol, 1),
                new ItemStack(ARKCraftItems.metal, 60),
                new ItemStack(ARKCraftItems.wood, 5),
                new ItemStack(ARKCraftItems.hide, 15)
        );

        //Longneck Rifle
        getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.longneck_rifle, 1),
                new ItemStack(ARKCraftItems.metal, 95),
                new ItemStack(ARKCraftItems.wood, 20),
                new ItemStack(ARKCraftItems.hide, 25)
        );

        //Shotgun
        getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.shotgun, 1),
                new ItemStack(ARKCraftItems.metal, 80),
                new ItemStack(ARKCraftItems.wood, 20),
                new ItemStack(ARKCraftItems.hide, 25)
        );

        //Crossbow
        getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.crossbow, 1),
                new ItemStack(ARKCraftItems.metal, 7),
                new ItemStack(ARKCraftItems.wood, 10),
                new ItemStack(ARKCraftItems.fiber, 35)
        );

        //Simple Pistol with Scope
        getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.simple_pistol_scoped, 1),
                new ItemStack(ARKCraftItems.simple_pistol, 1),
                new ItemStack(ARKCraftItems.scope, 1)
        );

        //Scope with Longneck Rifle
        getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.longneck_rifle_scoped, 1),
                new ItemStack(ARKCraftItems.longneck_rifle, 1),
                new ItemStack(ARKCraftItems.scope, 1)
        );

        //Bullets
        //Simple Bullet
        getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.simple_bullet, 1),
                new ItemStack(ARKCraftItems.gun_powder, 6),
                new ItemStack(ARKCraftItems.metal, 1)
        );
        //Simple Rifle Ammo
        getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.simple_rifle_ammo, 1),
                new ItemStack(ARKCraftItems.gun_powder, 12),
                new ItemStack(ARKCraftItems.metal, 2)
        );
        //Simple Shotgun Ammo
        getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.simple_shotgun_ammo, 1),
                new ItemStack(ARKCraftItems.gun_powder, 3),
                new ItemStack(ARKCraftItems.metal, 1),
                new ItemStack(ARKCraftItems.simple_bullet, 3)
        );
        //Tranquilizer
        getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.tranquilizer, 1),
                new ItemStack(ARKCraftItems.narcotics, 3),
                new ItemStack(ARKCraftItems.metal, 2),
                new ItemStack(ARKCraftItems.simple_rifle_ammo, 1)
        );
        //Grenade
        getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.grenade, 1),
                new ItemStack(ARKCraftItems.gun_powder, 30),
                new ItemStack(ARKCraftItems.fiber, 15),
                new ItemStack(ARKCraftItems.rock, 20),
                new ItemStack(ARKCraftItems.metal, 2),
                new ItemStack(ARKCraftItems.hide, 5)
                //		new ItemStack(ARKCraftItems.oil, 4)
        );


        //Rocket Launcher
        //Second Release
        /*
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.rocket_launcher, 1),
				new ItemStack(Items.iron_ingot, 48),
				new ItemStack(Items.diamond, 4),
				new ItemStack(Items.redstone, 16)
				);	*/

    }
}
