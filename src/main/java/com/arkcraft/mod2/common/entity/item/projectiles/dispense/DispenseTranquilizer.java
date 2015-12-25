package com.arkcraft.mod2.common.entity.item.projectiles.dispense;

import com.arkcraft.mod.common.ARKCraft;
import com.arkcraft.mod2.common.entity.item.projectiles.EntityTranquilizer;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class DispenseTranquilizer extends DispenseWeaponProjectile
{
	@Override
	protected IProjectile getProjectileEntity(World world, IPosition pos)
	{
		return new EntityTranquilizer(world, pos.getX(), pos.getY(), pos.getZ());
	}
	
	@Override
	public double getYVel()
	{
		return 0D;
	}
	
	@Override
	public float getDeviation()
	{
		return 3F;
	}
	
	@Override
	public float getVelocity()
	{
		return 5F;
	}
	
	@Override
	protected void playDispenseSound(IBlockSource blocksource)
	{
		blocksource.getWorld().playSoundEffect(blocksource.getX(), blocksource.getY(), blocksource.getZ(), ARKCraft.MODID + ":" + "shoot_tranq_gun", 3.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.7F));
	}
	
	@Override
	protected void spawnDispenseParticles(IBlockSource blocksource, EnumFacing face)
	{
		super.spawnDispenseParticles(blocksource, face);
		IPosition pos = BlockDispenser.getDispensePosition(blocksource);
		blocksource.getWorld().spawnParticle(EnumParticleTypes.FLAME, pos.getX() + face.getFrontOffsetX(), pos.getY() + face.getFrontOffsetY(), pos.getZ() + face.getFrontOffsetZ(), 0.0D, 0.2D, 0.0D);
	}
}