package com.arkcraft.mod.common.items.weapons.component;

import com.arkcraft.mod.common.ARKCraft;
import com.arkcraft.mod.common.items.weapons.handlers.ReloadHelper;
import com.arkcraft.mod.common.items.weapons.handlers.WeaponModAttributes;
import com.arkcraft.mod.common.items.weapons.projectiles.EntityShootable;
import com.arkcraft.mod.common.lib.BALANCE;
import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class RangedComponent extends AbstractWeaponComponent
{
	protected static final int	MAX_DELAY	= 72000;
	
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
	
	public final RangedSpecs	rangedSpecs;
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
	public void addItemAttributeModifiers(Multimap<String, AttributeModifier> multimap)
	{
		multimap.put(WeaponModAttributes.RELOAD_TIME.getAttributeUnlocalizedName(), new AttributeModifier(weapon.getUUID(), "Weapon reloadtime modifier", rangedSpecs.getReloadTime(), 0));
	}
	
	@Override
	public boolean onLeftClickEntity(ItemStack itemstack, EntityPlayer player, Entity entity)
	{
		return false;
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack itemstack)
	{
		int state = ReloadHelper.getReloadState(itemstack);
		if (state == ReloadHelper.STATE_NONE)
		{
			return EnumAction.BLOCK;
		} else if (state == ReloadHelper.STATE_READY)
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
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
	{
		if (itemstack.stackSize <= 0 || entityplayer.isUsingItem()) return itemstack;	
		
		if (hasAmmo(itemstack, world, entityplayer)) //Check can reload
		{
				if (isReadyToFire(itemstack))
				{
					//Start aiming weapon to fire
					soundCharge(itemstack, world, entityplayer);
					entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
	
				} else
				{
					//Begin reloading
					entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));		
				}
				
		} else
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
		if (ReloadHelper.getReloadState(itemstack) == ReloadHelper.STATE_NONE && getMaxItemUseDuration(itemstack) - count >= getReloadDuration(itemstack))
		{
			effectReloadDone(itemstack, entityplayer.worldObj, entityplayer);
			entityplayer.clearItemInUse();
			setReloadState(itemstack, ReloadHelper.STATE_RELOADED);
		}
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack itemstack, World world, EntityPlayer entityplayer, int i)
	{
		if (!isReloaded(itemstack)) return;
		if (isReadyToFire(itemstack))
		{
			if (hasAmmoAndConsume(itemstack, world, entityplayer))
			{
				fire(itemstack, world, entityplayer, i);
			}
			setReloadState(itemstack, ReloadHelper.STATE_NONE);
		} else
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
	
	public void applyProjectileEnchantments(EntityShootable entity, ItemStack itemstack)
	{
		int damage = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, itemstack);
		if (damage > 0)
		{
			entity.setExtraDamage(damage);
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
	
	public float getFOVMultiplier(int ticksinuse)
	{
		float f1 = ticksinuse / getMaxAimTimeTicks();
		
		if (f1 > 1.0F)
		{
			f1 = 1.0F;
		} else
		{
			f1 *= f1;
		}
		
		return 1.0F - f1 * getMaxZoom();
	}
	
	protected float getMaxAimTimeTicks()
	{
		return 20.0f;
	}
	
	protected float getMaxZoom()
	{
		return 0.15f;
	}
	
	public static enum RangedSpecs {
		SIMPLEPISTOL("arkcraft:simple_bullet", 0,  150, 1),
		ROCKETLAUNCHER("arkcraft:rocket_propelled_grenade", 1, 250, 1),
		SHOTGUN("arkcraft:simple_shotgun_ammo", 2, 200, 5),
		LONGNECKRIFLE("arkcraft:simple_rifle_ammo", 3, 350, 1),
		TRANQGUN("arkcraft:tranquilizer", 4, 350, 1),
		CROSSBOW("arkcraft:stone_arrow, metal_arrow, tranq_arrow", 5, 250, 1);
		
		private int getReloadTime(int id){
			switch (id) {
			case 0:
				return BALANCE.WEAPONS.SIMPLE_PISTOL_RELOAD;
			case 1:
				return BALANCE.WEAPONS.ROCKET_LAUNCHER_RELOAD;
			case 2:
				return BALANCE.WEAPONS.SHOTGUN_RELOAD;
			case 3:
				return BALANCE.WEAPONS.LONGNECK_RIFLE_RELOAD;
			case 4:
				return BALANCE.WEAPONS.TRANQ_GUN_RELOAD;
			case 5:
				return BALANCE.WEAPONS.CROSSBOW_RELOAD;
			default:
				return 6;  // just in case					
			}
		}
			
		RangedSpecs(String ammoitemtag, int reloadtimeid, int durability, int stacksize){
			ammoItemTag = ammoitemtag;
			reloadTimeId = reloadtimeid;
			this.durability = durability;
			this.stackSize = stacksize;
			ammoItem = null;
			reloadTime = -1;
				
		}
		
		public int getReloadTime(){
			if (reloadTime < 0 && ARKCraft.instance != null){
				reloadTime = getReloadTime(reloadTimeId);
				ARKCraft.modLog.debug("Found reaload time " + reloadTime + " for " + reloadTimeId + " @" + this);
			}
			return reloadTime;
		}
		
		public Item getAmmoItem(){
			if (ammoItem == null && ammoItemTag != null){
				ammoItem = (Item) Item.itemRegistry.getObject(ammoItemTag);
				ARKCraft.modLog.debug("Found item " + ammoItem + " for " + ammoItemTag + " @" + this);
				ammoItemTag = null;
			}
			return ammoItem;
		}
		
		private int			reloadTime;
		private Item		ammoItem;
		private String		ammoItemTag;
		public final int	reloadTimeId;
		public final int	durability, stackSize;
	}
}
