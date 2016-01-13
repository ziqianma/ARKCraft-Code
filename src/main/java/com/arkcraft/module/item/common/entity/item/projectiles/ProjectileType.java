package com.arkcraft.module.item.common.entity.item.projectiles;

public enum ProjectileType
{
	SIMPLE_BULLET("EntitySimpleBullet"),
	SIMPLE_RIFLE_AMMO("EntitySimpleBullet"),
	SIMPLE_SHOTGUN_AMMO("EntitySimpleBullet"),
	TRANQUILIZER("EntityTranquilizer");

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
