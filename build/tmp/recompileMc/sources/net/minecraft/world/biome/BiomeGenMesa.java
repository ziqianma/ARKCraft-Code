package net.minecraft.world.biome;

import java.util.Arrays;
import java.util.Random;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BiomeGenMesa extends BiomeGenBase
{
    private IBlockState[] field_150621_aC;
    private long field_150622_aD;
    private NoiseGeneratorPerlin field_150623_aE;
    private NoiseGeneratorPerlin field_150624_aF;
    private NoiseGeneratorPerlin field_150625_aG;
    private boolean field_150626_aH;
    private boolean field_150620_aI;
    private static final String __OBFID = "CL_00000176";

    public BiomeGenMesa(int p_i45380_1_, boolean p_i45380_2_, boolean p_i45380_3_)
    {
        super(p_i45380_1_);
        this.field_150626_aH = p_i45380_2_;
        this.field_150620_aI = p_i45380_3_;
        this.setDisableRain();
        this.setTemperatureRainfall(2.0F, 0.0F);
        this.spawnableCreatureList.clear();
        this.topBlock = Blocks.sand.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND);
        this.fillerBlock = Blocks.stained_hardened_clay.getDefaultState();
        this.theBiomeDecorator.treesPerChunk = -999;
        this.theBiomeDecorator.deadBushPerChunk = 20;
        this.theBiomeDecorator.reedsPerChunk = 3;
        this.theBiomeDecorator.cactiPerChunk = 5;
        this.theBiomeDecorator.flowersPerChunk = 0;
        this.spawnableCreatureList.clear();

        if (p_i45380_3_)
        {
            this.theBiomeDecorator.treesPerChunk = 5;
        }
    }

    public WorldGenAbstractTree genBigTreeChance(Random p_150567_1_)
    {
        return this.worldGeneratorTrees;
    }

    public void decorate(World worldIn, Random p_180624_2_, BlockPos p_180624_3_)
    {
        super.decorate(worldIn, p_180624_2_, p_180624_3_);
    }

    public void genTerrainBlocks(World worldIn, Random p_180622_2_, ChunkPrimer p_180622_3_, int p_180622_4_, int p_180622_5_, double p_180622_6_)
    {
        if (this.field_150621_aC == null || this.field_150622_aD != worldIn.getSeed())
        {
            this.func_150619_a(worldIn.getSeed());
        }

        if (this.field_150623_aE == null || this.field_150624_aF == null || this.field_150622_aD != worldIn.getSeed())
        {
            Random random1 = new Random(this.field_150622_aD);
            this.field_150623_aE = new NoiseGeneratorPerlin(random1, 4);
            this.field_150624_aF = new NoiseGeneratorPerlin(random1, 1);
        }

        this.field_150622_aD = worldIn.getSeed();
        double d5 = 0.0D;
        int k;
        int l;

        if (this.field_150626_aH)
        {
            k = (p_180622_4_ & -16) + (p_180622_5_ & 15);
            l = (p_180622_5_ & -16) + (p_180622_4_ & 15);
            double d1 = Math.min(Math.abs(p_180622_6_), this.field_150623_aE.func_151601_a((double)k * 0.25D, (double)l * 0.25D));

            if (d1 > 0.0D)
            {
                double d2 = 0.001953125D;
                double d3 = Math.abs(this.field_150624_aF.func_151601_a((double)k * d2, (double)l * d2));
                d5 = d1 * d1 * 2.5D;
                double d4 = Math.ceil(d3 * 50.0D) + 14.0D;

                if (d5 > d4)
                {
                    d5 = d4;
                }

                d5 += 64.0D;
            }
        }

        k = p_180622_4_ & 15;
        l = p_180622_5_ & 15;
        boolean flag = true;
        IBlockState iblockstate = Blocks.stained_hardened_clay.getDefaultState();
        IBlockState iblockstate3 = this.fillerBlock;
        int i1 = (int)(p_180622_6_ / 3.0D + 3.0D + p_180622_2_.nextDouble() * 0.25D);
        boolean flag1 = Math.cos(p_180622_6_ / 3.0D * Math.PI) > 0.0D;
        int j1 = -1;
        boolean flag2 = false;

        for (int k1 = 255; k1 >= 0; --k1)
        {
            if (p_180622_3_.getBlockState(l, k1, k).getBlock().getMaterial() == Material.air && k1 < (int)d5)
            {
                p_180622_3_.setBlockState(l, k1, k, Blocks.stone.getDefaultState());
            }

            if (k1 <= p_180622_2_.nextInt(5))
            {
                p_180622_3_.setBlockState(l, k1, k, Blocks.bedrock.getDefaultState());
            }
            else
            {
                IBlockState iblockstate1 = p_180622_3_.getBlockState(l, k1, k);

                if (iblockstate1.getBlock().getMaterial() == Material.air)
                {
                    j1 = -1;
                }
                else if (iblockstate1.getBlock() == Blocks.stone)
                {
                    IBlockState iblockstate2;

                    if (j1 == -1)
                    {
                        flag2 = false;

                        if (i1 <= 0)
                        {
                            iblockstate = null;
                            iblockstate3 = Blocks.stone.getDefaultState();
                        }
                        else if (k1 >= 59 && k1 <= 64)
                        {
                            iblockstate = Blocks.stained_hardened_clay.getDefaultState();
                            iblockstate3 = this.fillerBlock;
                        }

                        if (k1 < 63 && (iblockstate == null || iblockstate.getBlock().getMaterial() == Material.air))
                        {
                            iblockstate = Blocks.water.getDefaultState();
                        }

                        j1 = i1 + Math.max(0, k1 - 63);

                        if (k1 >= 62)
                        {
                            if (this.field_150620_aI && k1 > 86 + i1 * 2)
                            {
                                if (flag1)
                                {
                                    p_180622_3_.setBlockState(l, k1, k, Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT));
                                }
                                else
                                {
                                    p_180622_3_.setBlockState(l, k1, k, Blocks.grass.getDefaultState());
                                }
                            }
                            else if (k1 > 66 + i1)
                            {
                                if (k1 >= 64 && k1 <= 127)
                                {
                                    if (flag1)
                                    {
                                        iblockstate2 = Blocks.hardened_clay.getDefaultState();
                                    }
                                    else
                                    {
                                        iblockstate2 = this.func_180629_a(p_180622_4_, k1, p_180622_5_);
                                    }
                                }
                                else
                                {
                                    iblockstate2 = Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.ORANGE);
                                }

                                p_180622_3_.setBlockState(l, k1, k, iblockstate2);
                            }
                            else
                            {
                                p_180622_3_.setBlockState(l, k1, k, this.topBlock);
                                flag2 = true;
                            }
                        }
                        else
                        {
                            p_180622_3_.setBlockState(l, k1, k, iblockstate3);

                            if (iblockstate3.getBlock() == Blocks.stained_hardened_clay)
                            {
                                p_180622_3_.setBlockState(l, k1, k, iblockstate3.getBlock().getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.ORANGE));
                            }
                        }
                    }
                    else if (j1 > 0)
                    {
                        --j1;

                        if (flag2)
                        {
                            p_180622_3_.setBlockState(l, k1, k, Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.ORANGE));
                        }
                        else
                        {
                            iblockstate2 = this.func_180629_a(p_180622_4_, k1, p_180622_5_);
                            p_180622_3_.setBlockState(l, k1, k, iblockstate2);
                        }
                    }
                }
            }
        }
    }

    public void func_150619_a(long p_150619_1_)
    {
        this.field_150621_aC = new IBlockState[64];
        Arrays.fill(this.field_150621_aC, Blocks.hardened_clay.getDefaultState());
        Random random = new Random(p_150619_1_);
        this.field_150625_aG = new NoiseGeneratorPerlin(random, 1);
        int j;

        for (j = 0; j < 64; ++j)
        {
            j += random.nextInt(5) + 1;

            if (j < 64)
            {
                this.field_150621_aC[j] = Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.ORANGE);
            }
        }

        j = random.nextInt(4) + 2;
        int k;
        int l;
        int i1;
        int j1;

        for (k = 0; k < j; ++k)
        {
            l = random.nextInt(3) + 1;
            i1 = random.nextInt(64);

            for (j1 = 0; i1 + j1 < 64 && j1 < l; ++j1)
            {
                this.field_150621_aC[i1 + j1] = Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.YELLOW);
            }
        }

        k = random.nextInt(4) + 2;
        int k1;

        for (l = 0; l < k; ++l)
        {
            i1 = random.nextInt(3) + 2;
            j1 = random.nextInt(64);

            for (k1 = 0; j1 + k1 < 64 && k1 < i1; ++k1)
            {
                this.field_150621_aC[j1 + k1] = Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.BROWN);
            }
        }

        l = random.nextInt(4) + 2;

        for (i1 = 0; i1 < l; ++i1)
        {
            j1 = random.nextInt(3) + 1;
            k1 = random.nextInt(64);

            for (int l1 = 0; k1 + l1 < 64 && l1 < j1; ++l1)
            {
                this.field_150621_aC[k1 + l1] = Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.RED);
            }
        }

        i1 = random.nextInt(3) + 3;
        j1 = 0;

        for (k1 = 0; k1 < i1; ++k1)
        {
            byte b0 = 1;
            j1 += random.nextInt(16) + 4;

            for (int i2 = 0; j1 + i2 < 64 && i2 < b0; ++i2)
            {
                this.field_150621_aC[j1 + i2] = Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.WHITE);

                if (j1 + i2 > 1 && random.nextBoolean())
                {
                    this.field_150621_aC[j1 + i2 - 1] = Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.SILVER);
                }

                if (j1 + i2 < 63 && random.nextBoolean())
                {
                    this.field_150621_aC[j1 + i2 + 1] = Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.SILVER);
                }
            }
        }
    }

    public IBlockState func_180629_a(int p_180629_1_, int p_180629_2_, int p_180629_3_)
    {
        int l = (int)Math.round(this.field_150625_aG.func_151601_a((double)p_180629_1_ * 1.0D / 512.0D, (double)p_180629_1_ * 1.0D / 512.0D) * 2.0D);
        return this.field_150621_aC[(p_180629_2_ + l + 64) % 64];
    }

    @SideOnly(Side.CLIENT)
    public int getFoliageColorAtPos(BlockPos p_180625_1_)
    {
        return 10387789;
    }

    @SideOnly(Side.CLIENT)
    public int getGrassColorAtPos(BlockPos p_180627_1_)
    {
        return 9470285;
    }

    public BiomeGenBase createMutatedBiome(int p_180277_1_)
    {
        boolean flag = this.biomeID == BiomeGenBase.mesa.biomeID;
        BiomeGenMesa biomegenmesa = new BiomeGenMesa(p_180277_1_, flag, this.field_150620_aI);

        if (!flag)
        {
            biomegenmesa.setHeight(height_LowHills);
            biomegenmesa.setBiomeName(this.biomeName + " M");
        }
        else
        {
            biomegenmesa.setBiomeName(this.biomeName + " (Bryce)");
        }

        biomegenmesa.func_150557_a(this.color, true);
        return biomegenmesa;
    }
}