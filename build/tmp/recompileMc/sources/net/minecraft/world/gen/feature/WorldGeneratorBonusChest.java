package net.minecraft.world.gen.feature;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;

public class WorldGeneratorBonusChest extends WorldGenerator
{
    private final List field_175909_a;
    /** Value of this int will determine how much items gonna generate in Bonus Chest. */
    private final int itemsToGenerateInBonusChest;
    private static final String __OBFID = "CL_00000403";

    public WorldGeneratorBonusChest(List p_i45634_1_, int p_i45634_2_)
    {
        this.field_175909_a = p_i45634_1_;
        this.itemsToGenerateInBonusChest = p_i45634_2_;
    }

    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_)
    {
        Block block;

        do
        {
            block = worldIn.getBlockState(p_180709_3_).getBlock();
            if (!block.isAir(worldIn, p_180709_3_) && !block.isLeaves(worldIn, p_180709_3_)) break;
            p_180709_3_ = p_180709_3_.down();
        } while (p_180709_3_.getY() > 0);

        if (p_180709_3_.getY() < 1)
        {
            return false;
        }
        else
        {
            p_180709_3_ = p_180709_3_.up();

            for (int i = 0; i < 4; ++i)
            {
                BlockPos blockpos1 = p_180709_3_.add(p_180709_2_.nextInt(4) - p_180709_2_.nextInt(4), p_180709_2_.nextInt(3) - p_180709_2_.nextInt(3), p_180709_2_.nextInt(4) - p_180709_2_.nextInt(4));

                if (worldIn.isAirBlock(blockpos1) && World.doesBlockHaveSolidTopSurface(worldIn, blockpos1.down()))
                {
                    worldIn.setBlockState(blockpos1, Blocks.chest.getDefaultState(), 2);
                    TileEntity tileentity = worldIn.getTileEntity(blockpos1);

                    if (tileentity instanceof TileEntityChest)
                    {
                        WeightedRandomChestContent.generateChestContents(p_180709_2_, this.field_175909_a, (TileEntityChest)tileentity, this.itemsToGenerateInBonusChest);
                    }

                    BlockPos blockpos2 = blockpos1.east();
                    BlockPos blockpos3 = blockpos1.west();
                    BlockPos blockpos4 = blockpos1.north();
                    BlockPos blockpos5 = blockpos1.south();

                    if (worldIn.isAirBlock(blockpos3) && World.doesBlockHaveSolidTopSurface(worldIn, blockpos3.down()))
                    {
                        worldIn.setBlockState(blockpos3, Blocks.torch.getDefaultState(), 2);
                    }

                    if (worldIn.isAirBlock(blockpos2) && World.doesBlockHaveSolidTopSurface(worldIn, blockpos2.down()))
                    {
                        worldIn.setBlockState(blockpos2, Blocks.torch.getDefaultState(), 2);
                    }

                    if (worldIn.isAirBlock(blockpos4) && World.doesBlockHaveSolidTopSurface(worldIn, blockpos4.down()))
                    {
                        worldIn.setBlockState(blockpos4, Blocks.torch.getDefaultState(), 2);
                    }

                    if (worldIn.isAirBlock(blockpos5) && World.doesBlockHaveSolidTopSurface(worldIn, blockpos5.down()))
                    {
                        worldIn.setBlockState(blockpos5, Blocks.torch.getDefaultState(), 2);
                    }

                    return true;
                }
            }

            return false;
        }
    }
}