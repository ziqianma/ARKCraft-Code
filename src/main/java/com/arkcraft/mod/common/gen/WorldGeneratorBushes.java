package com.arkcraft.mod.common.gen;

import com.arkcraft.mod.common.blocks.ARKCraftBlocks;
import net.minecraft.block.BlockGrass;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

/**
 * @author gegy1000
 */
public class WorldGeneratorBushes implements IWorldGenerator
{
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
    {
        if (world.provider.getDimensionId() == 0)
        {
            for (int i = 0; i < 4; i++)
            {
                int x = chunkX * 16 + random.nextInt(16);
                int z = chunkZ * 16 + random.nextInt(16);

                BlockPos pos = world.getHorizon(new BlockPos(x, 0, z));

                if (world.getBlockState(pos.down()).getBlock() instanceof BlockGrass)
                {
                    world.setBlockState(pos, ARKCraftBlocks.berryBush.getDefaultState());
                }
            }
        }
    }
}
