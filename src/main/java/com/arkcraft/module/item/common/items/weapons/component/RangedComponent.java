package com.arkcraft.module.item.common.items.weapons.component;

import com.arkcraft.module.item.common.items.weapons.handlers.ReloadHelper;
import com.arkcraft.module.core.ARKCraft;
import com.arkcraft.module.item.common.config.ModuleItemBalance;
import com.arkcraft.module.item.common.entity.item.projectiles.Test;
import com.arkcraft.module.item.common.items.weapons.handlers.WeaponModAttributes;
import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class RangedComponent extends AbstractWeaponComponent
{
    protected static final int MAX_DELAY = 72000;

    public static boolean isReloaded(ItemStack itemstack)
    {
        return ReloadHelper.getReloadState(itemstack) >= ReloadHelper.STATE_RELOADED;
    }

    public static boolean isReadyToFire(ItemStack itemstack)
    {
        return ReloadHelper.getReloadState(itemstack) == ReloadHelper.STATE_READY;
    }

    public static void setReloadState(ItemStack itemstack, int state)
    {
        ReloadHelper.setReloadState(itemstack, state);
    }

    public final RangedSpecs rangedSpecs;

    public RangedComponent(RangedSpecs rangedspecs)
    {
        rangedSpecs = rangedspecs;
    }

    @Override
    protected void onSetItem()
    {
    }

    @Override
    public void setThisItemProperties()
    {
        item.setMaxDamage(rangedSpecs.durability);
        item.setMaxStackSize(rangedSpecs.stackSize);
    }

    @Override
    public boolean canOpenGui()
    {
        return false;
    }

    @Override
    public float getEntityDamageMaterialPart()
    {
        return 0;
    }

    @Override
    public float getEntityDamage()
    {
        return 0;
    }

    @Override
    public float getBlockDamage(ItemStack itemstack, Block block)
    {
        return 0;
    }

    @Override
    public boolean canHarvestBlock(Block block)
    {
        return false;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack itemstack, World world, Block block, int j, int k, int l, EntityLivingBase entityliving)
    {
        return false;
    }

    @Override
    public boolean hitEntity(ItemStack itemstack, EntityLivingBase entityliving, EntityLivingBase attacker)
    {
        return false;
    }

    @Override
    public int getAttackDelay(ItemStack itemstack, EntityLivingBase entityliving, EntityLivingBase attacker)
    {
        return 0;
    }

    @Override
    public float getKnockBack(ItemStack itemstack, EntityLivingBase entityliving, EntityLivingBase attacker)
    {
        return 0;
    }

    @Override
    public int getItemEnchantability()
    {
        return 1;
    }

    @Override
    public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player)
    {
        return null;
    }

    @Override
    public void addItemAttributeModifiers(Multimap<String, AttributeModifier> multimap)
    {
        multimap.put(WeaponModAttributes.RELOAD_TIME.getAttributeUnlocalizedName(), new AttributeModifier("Weapon reloadtime modifier", rangedSpecs.getReloadTime(), 0));
    }

    @Override
    public boolean onLeftClickEntity(ItemStack itemstack, EntityPlayer player, Entity entity)
    {
        return false;
    }

    public void onItemLeftClickTick(EntityPlayer entityplayer, World world1, int i)
    {

    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemstack)
    {
        int state = ReloadHelper.getReloadState(itemstack);
        if (state == ReloadHelper.STATE_NONE)
        {
//			return EnumAction.BLOCK;
            return EnumAction.NONE; // No swing animation
        }
        else if (state == ReloadHelper.STATE_READY)
        {
        }
        return EnumAction.NONE;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack itemstack)
    {
        return MAX_DELAY;
    }

    @Override
    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack)
    {
        return false;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        if (itemstack.stackSize <= 0 || entityplayer.isUsingItem())
        {
            return itemstack;
        }

        //Check can reload
        if (hasAmmo(itemstack, world, entityplayer))
        {
            if (isReadyToFire(itemstack))
            {
                //Start aiming weapon to fire
                soundCharge(itemstack, world, entityplayer);
                entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));

            }
            else
            {
                //Begin reloading
                entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
                if (world.isRemote && !entityplayer.capabilities.isCreativeMode)
                // i.e. "20 ammo"
                {
                    entityplayer.addChatMessage(new ChatComponentText(getAmmoQuantity(entityplayer) + StatCollector.translateToLocal("chat.ammo")));
                }
            }
        }
        else
        {
            //Can't reload; no ammo
            soundEmpty(itemstack, world, entityplayer);
            setReloadState(itemstack, ReloadHelper.STATE_NONE);
        }
        return itemstack;
    }

    @Override
    public void onUsingTick(ItemStack itemstack, EntityPlayer entityplayer, int count)
    {
        if (ReloadHelper.getReloadState(itemstack) == ReloadHelper.STATE_NONE
                && getMaxItemUseDuration(itemstack) - count >= getReloadDuration(itemstack))
        {
            effectReloadDone(itemstack, entityplayer.worldObj, entityplayer);
            entityplayer.clearItemInUse();
            setReloadState(itemstack, ReloadHelper.STATE_RELOADED);
        }
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack itemstack, World world, EntityPlayer entityplayer, int i)
    {
        if (!isReloaded(itemstack))
        {
            return;
        }
        if (isReadyToFire(itemstack))
        {
            if (hasAmmoAndConsume(itemstack, world, entityplayer))
            {
                fire(itemstack, world, entityplayer, i);
            }
            setReloadState(itemstack, ReloadHelper.STATE_NONE);
        }
        else
        {
            setReloadState(itemstack, ReloadHelper.STATE_READY);
        }
    }

    @Override
    public void onUpdate(ItemStack itemstack, World world, Entity entity, int i, boolean flag)
    {
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldRotateAroundWhenRendering()
    {
        return false;
    }

    public void soundEmpty(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        world.playSoundAtEntity(entityplayer, "random.click", 1.0F, 1.0F / 0.8F);
    }

    public void soundCharge(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
    }

    public final void postShootingEffects(ItemStack itemstack, EntityPlayer entityplayer, World world)
    {
        effectPlayer(itemstack, entityplayer, world);
        effectShoot(world, entityplayer.posX, entityplayer.posY, entityplayer.posZ, entityplayer.rotationYaw, entityplayer.rotationPitch);
    }

    public abstract void effectReloadDone(ItemStack itemstack, World world, EntityPlayer entityplayer);

    public abstract void fire(ItemStack itemstack, World world, EntityPlayer entityplayer, int i);

    public abstract void effectPlayer(ItemStack itemstack, EntityPlayer entityplayer, World world);

    public abstract void effectShoot(World world, double x, double y, double z, float yaw, float pitch);

    public void applyProjectileEnchantments(Test entity, ItemStack itemstack)
    {
        int damage = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, itemstack);
        if (damage > 0)
        {
            entity.setDamage(damage);
        }

        int knockback = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, itemstack);
        if (knockback > 0)
        {
            entity.setKnockbackStrength(knockback);
        }

        if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, itemstack) > 0)
        {
            entity.setFire(100);
        }
    }

    public int getReloadDuration(ItemStack itemstack)
    {
        return rangedSpecs.getReloadTime();
    }

    public Item getAmmoItem()
    {
        return rangedSpecs.getAmmoItem();
    }

    public boolean hasAmmoAndConsume(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        return entityplayer.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, itemstack) > 0 || entityplayer.inventory.consumeInventoryItem(getAmmoItem());
    }

    public boolean hasAmmo(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        return entityplayer.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, itemstack) > 0 || entityplayer.inventory.hasItem(getAmmoItem());
    }

    protected int getAmmoQuantity(EntityPlayer entityplayer)
    {
        int numInStack = 0;
        Item ammoItem = getAmmoItem();
        for (int i = 0; i < entityplayer.inventory.getSizeInventory(); ++i)
        {
            ItemStack itemstack = entityplayer.inventory.getStackInSlot(i);
            if (itemstack != null)
            {
                if (itemstack.getItem() == ammoItem)
                {
                    numInStack += itemstack.stackSize;
                }
            }
        }
        return numInStack;
    }

    @Override
    public boolean ifCanScope()
    {

        return false;
    }

    public static enum RangedSpecs
    {
        SIMPLEPISTOL("arkcraft:simple_bullet", 0, 150, 1),
        ROCKETLAUNCHER("arkcraft:rocket_propelled_grenade", 1, 250, 1),
        SHOTGUN("arkcraft:simple_shotgun_ammo", 2, 200, 5),
        LONGNECKRIFLE("arkcraft:simple_rifle_ammo", 3, 350, 1),
        LONGNECKRIFLE_TRANQ("arkcraft:tranquilizer", 3, 350, 1),
        TRANQGUN("arkcraft:tranquilizer", 4, 350, 1),
        CROSSBOW("arkcraft:stone_arrow, metal_arrow, tranq_arrow", 5, 250, 1);

        private int getReloadTime(int id)
        {
            switch (id)
            {
                case 0:
                    return (int) (ModuleItemBalance.WEAPONS.SIMPLE_PISTOL_RELOAD * 20.0);
                case 1:
                    return (int) (ModuleItemBalance.WEAPONS.ROCKET_LAUNCHER_RELOAD * 20.0);
                case 2:
                    return (int) (ModuleItemBalance.WEAPONS.SHOTGUN_RELOAD * 20.0);
                case 3:
                    return (int) (ModuleItemBalance.WEAPONS.LONGNECK_RIFLE_RELOAD * 20.0);
                case 4:
                    return (int) (ModuleItemBalance.WEAPONS.TRANQ_GUN_RELOAD * 20.0);
                case 5:
                    return (int) (ModuleItemBalance.WEAPONS.CROSSBOW_RELOAD * 20.0);
                default:
                    return 6;  // just in case
            }
        }

        RangedSpecs(String ammoitemtag, int reloadtimeid, int durability, int stacksize)
        {
            ammoItemTag = ammoitemtag;
            reloadTimeId = reloadtimeid;
            this.durability = durability;
            this.stackSize = stacksize;
            ammoItem = null;
            reloadTime = -1;

        }

        public int getReloadTime()
        {
            if (reloadTime < 0 && ARKCraft.instance != null)
            {
                reloadTime = getReloadTime(reloadTimeId);
                ARKCraft.modLog.debug("Found reaload time " + reloadTime + " for " + reloadTimeId + " @" + this);
                System.out.println("Found reaload time " + reloadTime + " for " + reloadTimeId + " @" + this);
            }
            return reloadTime;
        }

        public Item getAmmoItem()
        {
            if (ammoItem == null && ammoItemTag != null)
            {
                ammoItem = (Item) Item.itemRegistry.getObject(ammoItemTag);
                ARKCraft.modLog.debug("Found item " + ammoItem + " for " + ammoItemTag + " @" + this);
                System.out.println("Found item " + ammoItem + " for " + ammoItemTag + " @" + this);
                ammoItemTag = null;
            }
            return ammoItem;
        }

        private int reloadTime;
        private Item ammoItem;
        private String ammoItemTag;
        public final int reloadTimeId;
        public final int durability, stackSize;
    }
}
