package com.arkcraft.module.weapon.common.entity;

public enum ProjectileType
{
	SIMPLE_BULLET("EntitySimpleBullet"),
	SIMPLE_RIFLE_AMMO("EntitySimpleRifleAmmo"),
	SIMPLE_SHOTGUN_AMMO("EntitySimpleShotgunAmmo"),
	TRANQUILIZER("EntityTranquilizer"),
	ROCKET_PROPELLED_GRENADE("EntityRocketPropelledGrenade"),
	ADVANCED_BULLET("EntityAdvancedBullet"),
	METAL_ARROW("EntityMetalArrow"),
	STONE_ARROW("EntityStoneArrow"),
	TRANQ_ARROW("EntityTranqArrow");

	String entity;

	ProjectileType(String entity)
	{
		this.entity = entity;
	}

	public String getEntity()
	{
		return entity;
	}
}
