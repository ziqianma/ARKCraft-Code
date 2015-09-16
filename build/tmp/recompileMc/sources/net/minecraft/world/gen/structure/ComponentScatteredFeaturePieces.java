package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.BlockTripWire;
import net.minecraft.block.BlockTripWireHook;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraftforge.common.ChestGenHooks;
import static net.minecraftforge.common.ChestGenHooks.*;

public class ComponentScatteredFeaturePieces
{
    private static final String __OBFID = "CL_00000473";

    public static void registerScatteredFeaturePieces()
    {
        MapGenStructureIO.registerStructureComponent(ComponentScatteredFeaturePieces.DesertPyramid.class, "TeDP");
        MapGenStructureIO.registerStructureComponent(ComponentScatteredFeaturePieces.JunglePyramid.class, "TeJP");
        MapGenStructureIO.registerStructureComponent(ComponentScatteredFeaturePieces.SwampHut.class, "TeSH");
    }

    public static class DesertPyramid extends ComponentScatteredFeaturePieces.Feature
        {
            private boolean[] field_74940_h = new boolean[4];
            /** List of items to generate in chests of Temples. */
            private static final List itemsToGenerateInTemple = Lists.newArrayList(new WeightedRandomChestContent[] {new WeightedRandomChestContent(Items.diamond, 0, 1, 3, 3), new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10), new WeightedRandomChestContent(Items.gold_ingot, 0, 2, 7, 15), new WeightedRandomChestContent(Items.emerald, 0, 1, 3, 2), new WeightedRandomChestContent(Items.bone, 0, 4, 6, 20), new WeightedRandomChestContent(Items.rotten_flesh, 0, 3, 7, 16), new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 3), new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 1), new WeightedRandomChestContent(Items.golden_horse_armor, 0, 1, 1, 1), new WeightedRandomChestContent(Items.diamond_horse_armor, 0, 1, 1, 1)});
            private static final String __OBFID = "CL_00000476";

            public DesertPyramid() {}

            static
            {
                ChestGenHooks.init(PYRAMID_DESERT_CHEST, itemsToGenerateInTemple, 2, 7);
                ChestGenHooks.addItem(PYRAMID_DESERT_CHEST, new WeightedRandomChestContent(new ItemStack(Items.enchanted_book, 1, 0), 1, 1, 1));
            }

            public DesertPyramid(Random p_i2062_1_, int p_i2062_2_, int p_i2062_3_)
            {
                super(p_i2062_1_, p_i2062_2_, 64, p_i2062_3_, 21, 15, 21);
            }

            /**
             * (abstract) Helper method to write subclass data to NBT
             */
            protected void writeStructureToNBT(NBTTagCompound p_143012_1_)
            {
                super.writeStructureToNBT(p_143012_1_);
                p_143012_1_.setBoolean("hasPlacedChest0", this.field_74940_h[0]);
                p_143012_1_.setBoolean("hasPlacedChest1", this.field_74940_h[1]);
                p_143012_1_.setBoolean("hasPlacedChest2", this.field_74940_h[2]);
                p_143012_1_.setBoolean("hasPlacedChest3", this.field_74940_h[3]);
            }

            /**
             * (abstract) Helper method to read subclass data from NBT
             */
            protected void readStructureFromNBT(NBTTagCompound p_143011_1_)
            {
                super.readStructureFromNBT(p_143011_1_);
                this.field_74940_h[0] = p_143011_1_.getBoolean("hasPlacedChest0");
                this.field_74940_h[1] = p_143011_1_.getBoolean("hasPlacedChest1");
                this.field_74940_h[2] = p_143011_1_.getBoolean("hasPlacedChest2");
                this.field_74940_h[3] = p_143011_1_.getBoolean("hasPlacedChest3");
            }

            /**
             * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes
             * Mineshafts at the end, it adds Fences...
             */
            public boolean addComponentParts(World worldIn, Random p_74875_2_, StructureBoundingBox p_74875_3_)
            {
                this.func_175804_a(worldIn, p_74875_3_, 0, -4, 0, this.scatteredFeatureSizeX - 1, 0, this.scatteredFeatureSizeZ - 1, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
                int i;

                for (i = 1; i <= 9; ++i)
                {
                    this.func_175804_a(worldIn, p_74875_3_, i, i, i, this.scatteredFeatureSizeX - 1 - i, i, this.scatteredFeatureSizeZ - 1 - i, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
                    this.func_175804_a(worldIn, p_74875_3_, i + 1, i, i + 1, this.scatteredFeatureSizeX - 2 - i, i, this.scatteredFeatureSizeZ - 2 - i, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                }

                int j;

                for (i = 0; i < this.scatteredFeatureSizeX; ++i)
                {
                    for (j = 0; j < this.scatteredFeatureSizeZ; ++j)
                    {
                        byte b0 = -5;
                        this.func_175808_b(worldIn, Blocks.sandstone.getDefaultState(), i, b0, j, p_74875_3_);
                    }
                }

                i = this.getMetadataWithOffset(Blocks.sandstone_stairs, 3);
                j = this.getMetadataWithOffset(Blocks.sandstone_stairs, 2);
                int i2 = this.getMetadataWithOffset(Blocks.sandstone_stairs, 0);
                int k = this.getMetadataWithOffset(Blocks.sandstone_stairs, 1);
                int l = ~EnumDyeColor.ORANGE.getDyeDamage() & 15;
                int i1 = ~EnumDyeColor.BLUE.getDyeDamage() & 15;
                this.func_175804_a(worldIn, p_74875_3_, 0, 0, 0, 4, 9, 4, Blocks.sandstone.getDefaultState(), Blocks.air.getDefaultState(), false);
                this.func_175804_a(worldIn, p_74875_3_, 1, 10, 1, 3, 10, 3, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
                this.func_175811_a(worldIn, Blocks.sandstone_stairs.getStateFromMeta(i), 2, 10, 0, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.sandstone_stairs.getStateFromMeta(j), 2, 10, 4, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.sandstone_stairs.getStateFromMeta(i2), 0, 10, 2, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.sandstone_stairs.getStateFromMeta(k), 4, 10, 2, p_74875_3_);
                this.func_175804_a(worldIn, p_74875_3_, this.scatteredFeatureSizeX - 5, 0, 0, this.scatteredFeatureSizeX - 1, 9, 4, Blocks.sandstone.getDefaultState(), Blocks.air.getDefaultState(), false);
                this.func_175804_a(worldIn, p_74875_3_, this.scatteredFeatureSizeX - 4, 10, 1, this.scatteredFeatureSizeX - 2, 10, 3, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
                this.func_175811_a(worldIn, Blocks.sandstone_stairs.getStateFromMeta(i), this.scatteredFeatureSizeX - 3, 10, 0, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.sandstone_stairs.getStateFromMeta(j), this.scatteredFeatureSizeX - 3, 10, 4, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.sandstone_stairs.getStateFromMeta(i2), this.scatteredFeatureSizeX - 5, 10, 2, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.sandstone_stairs.getStateFromMeta(k), this.scatteredFeatureSizeX - 1, 10, 2, p_74875_3_);
                this.func_175804_a(worldIn, p_74875_3_, 8, 0, 0, 12, 4, 4, Blocks.sandstone.getDefaultState(), Blocks.air.getDefaultState(), false);
                this.func_175804_a(worldIn, p_74875_3_, 9, 1, 0, 11, 3, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                this.func_175811_a(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 9, 1, 1, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 9, 2, 1, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 9, 3, 1, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 10, 3, 1, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 11, 3, 1, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 11, 2, 1, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 11, 1, 1, p_74875_3_);
                this.func_175804_a(worldIn, p_74875_3_, 4, 1, 1, 8, 3, 3, Blocks.sandstone.getDefaultState(), Blocks.air.getDefaultState(), false);
                this.func_175804_a(worldIn, p_74875_3_, 4, 1, 2, 8, 2, 2, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                this.func_175804_a(worldIn, p_74875_3_, 12, 1, 1, 16, 3, 3, Blocks.sandstone.getDefaultState(), Blocks.air.getDefaultState(), false);
                this.func_175804_a(worldIn, p_74875_3_, 12, 1, 2, 16, 2, 2, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                this.func_175804_a(worldIn, p_74875_3_, 5, 4, 5, this.scatteredFeatureSizeX - 6, 4, this.scatteredFeatureSizeZ - 6, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
                this.func_175804_a(worldIn, p_74875_3_, 9, 4, 9, 11, 4, 11, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                this.func_175804_a(worldIn, p_74875_3_, 8, 1, 8, 8, 3, 8, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
                this.func_175804_a(worldIn, p_74875_3_, 12, 1, 8, 12, 3, 8, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
                this.func_175804_a(worldIn, p_74875_3_, 8, 1, 12, 8, 3, 12, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
                this.func_175804_a(worldIn, p_74875_3_, 12, 1, 12, 12, 3, 12, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
                this.func_175804_a(worldIn, p_74875_3_, 1, 1, 5, 4, 4, 11, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
                this.func_175804_a(worldIn, p_74875_3_, this.scatteredFeatureSizeX - 5, 1, 5, this.scatteredFeatureSizeX - 2, 4, 11, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
                this.func_175804_a(worldIn, p_74875_3_, 6, 7, 9, 6, 7, 11, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
                this.func_175804_a(worldIn, p_74875_3_, this.scatteredFeatureSizeX - 7, 7, 9, this.scatteredFeatureSizeX - 7, 7, 11, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
                this.func_175804_a(worldIn, p_74875_3_, 5, 5, 9, 5, 7, 11, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
                this.func_175804_a(worldIn, p_74875_3_, this.scatteredFeatureSizeX - 6, 5, 9, this.scatteredFeatureSizeX - 6, 7, 11, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
                this.func_175811_a(worldIn, Blocks.air.getDefaultState(), 5, 5, 10, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.air.getDefaultState(), 5, 6, 10, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.air.getDefaultState(), 6, 6, 10, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.air.getDefaultState(), this.scatteredFeatureSizeX - 6, 5, 10, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.air.getDefaultState(), this.scatteredFeatureSizeX - 6, 6, 10, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.air.getDefaultState(), this.scatteredFeatureSizeX - 7, 6, 10, p_74875_3_);
                this.func_175804_a(worldIn, p_74875_3_, 2, 4, 4, 2, 6, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                this.func_175804_a(worldIn, p_74875_3_, this.scatteredFeatureSizeX - 3, 4, 4, this.scatteredFeatureSizeX - 3, 6, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                this.func_175811_a(worldIn, Blocks.sandstone_stairs.getStateFromMeta(i), 2, 4, 5, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.sandstone_stairs.getStateFromMeta(i), 2, 3, 4, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.sandstone_stairs.getStateFromMeta(i), this.scatteredFeatureSizeX - 3, 4, 5, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.sandstone_stairs.getStateFromMeta(i), this.scatteredFeatureSizeX - 3, 3, 4, p_74875_3_);
                this.func_175804_a(worldIn, p_74875_3_, 1, 1, 3, 2, 2, 3, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
                this.func_175804_a(worldIn, p_74875_3_, this.scatteredFeatureSizeX - 3, 1, 3, this.scatteredFeatureSizeX - 2, 2, 3, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
                this.func_175811_a(worldIn, Blocks.sandstone_stairs.getDefaultState(), 1, 1, 2, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.sandstone_stairs.getDefaultState(), this.scatteredFeatureSizeX - 2, 1, 2, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SAND.getMetadata()), 1, 2, 2, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SAND.getMetadata()), this.scatteredFeatureSizeX - 2, 2, 2, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.sandstone_stairs.getStateFromMeta(k), 2, 1, 2, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.sandstone_stairs.getStateFromMeta(i2), this.scatteredFeatureSizeX - 3, 1, 2, p_74875_3_);
                this.func_175804_a(worldIn, p_74875_3_, 4, 3, 5, 4, 3, 18, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
                this.func_175804_a(worldIn, p_74875_3_, this.scatteredFeatureSizeX - 5, 3, 5, this.scatteredFeatureSizeX - 5, 3, 17, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
                this.func_175804_a(worldIn, p_74875_3_, 3, 1, 5, 4, 2, 16, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                this.func_175804_a(worldIn, p_74875_3_, this.scatteredFeatureSizeX - 6, 1, 5, this.scatteredFeatureSizeX - 5, 2, 16, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                int j1;

                for (j1 = 5; j1 <= 17; j1 += 2)
                {
                    this.func_175811_a(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 4, 1, j1, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 4, 2, j1, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), this.scatteredFeatureSizeX - 5, 1, j1, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), this.scatteredFeatureSizeX - 5, 2, j1, p_74875_3_);
                }

                this.func_175811_a(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(l), 10, 0, 7, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(l), 10, 0, 8, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(l), 9, 0, 9, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(l), 11, 0, 9, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(l), 8, 0, 10, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(l), 12, 0, 10, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(l), 7, 0, 10, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(l), 13, 0, 10, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(l), 9, 0, 11, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(l), 11, 0, 11, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(l), 10, 0, 12, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(l), 10, 0, 13, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), 10, 0, 10, p_74875_3_);

                for (j1 = 0; j1 <= this.scatteredFeatureSizeX - 1; j1 += this.scatteredFeatureSizeX - 1)
                {
                    this.func_175811_a(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j1, 2, 1, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(l), j1, 2, 2, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j1, 2, 3, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j1, 3, 1, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(l), j1, 3, 2, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j1, 3, 3, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(l), j1, 4, 1, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), j1, 4, 2, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(l), j1, 4, 3, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j1, 5, 1, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(l), j1, 5, 2, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j1, 5, 3, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(l), j1, 6, 1, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), j1, 6, 2, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(l), j1, 6, 3, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(l), j1, 7, 1, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(l), j1, 7, 2, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(l), j1, 7, 3, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j1, 8, 1, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j1, 8, 2, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j1, 8, 3, p_74875_3_);
                }

                for (j1 = 2; j1 <= this.scatteredFeatureSizeX - 3; j1 += this.scatteredFeatureSizeX - 3 - 2)
                {
                    this.func_175811_a(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j1 - 1, 2, 0, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(l), j1, 2, 0, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j1 + 1, 2, 0, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j1 - 1, 3, 0, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(l), j1, 3, 0, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j1 + 1, 3, 0, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(l), j1 - 1, 4, 0, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), j1, 4, 0, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(l), j1 + 1, 4, 0, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j1 - 1, 5, 0, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(l), j1, 5, 0, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j1 + 1, 5, 0, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(l), j1 - 1, 6, 0, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), j1, 6, 0, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(l), j1 + 1, 6, 0, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(l), j1 - 1, 7, 0, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(l), j1, 7, 0, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(l), j1 + 1, 7, 0, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j1 - 1, 8, 0, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j1, 8, 0, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j1 + 1, 8, 0, p_74875_3_);
                }

                this.func_175804_a(worldIn, p_74875_3_, 8, 4, 0, 12, 6, 0, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
                this.func_175811_a(worldIn, Blocks.air.getDefaultState(), 8, 6, 0, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.air.getDefaultState(), 12, 6, 0, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(l), 9, 5, 0, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 10, 5, 0, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(l), 11, 5, 0, p_74875_3_);
                this.func_175804_a(worldIn, p_74875_3_, 8, -14, 8, 12, -11, 12, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
                this.func_175804_a(worldIn, p_74875_3_, 8, -10, 8, 12, -10, 12, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), false);
                this.func_175804_a(worldIn, p_74875_3_, 8, -9, 8, 12, -9, 12, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
                this.func_175804_a(worldIn, p_74875_3_, 8, -8, 8, 12, -1, 12, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
                this.func_175804_a(worldIn, p_74875_3_, 9, -11, 9, 11, -1, 11, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                this.func_175811_a(worldIn, Blocks.stone_pressure_plate.getDefaultState(), 10, -11, 10, p_74875_3_);
                this.func_175804_a(worldIn, p_74875_3_, 9, -13, 9, 11, -13, 11, Blocks.tnt.getDefaultState(), Blocks.air.getDefaultState(), false);
                this.func_175811_a(worldIn, Blocks.air.getDefaultState(), 8, -11, 10, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.air.getDefaultState(), 8, -10, 10, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 7, -10, 10, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 7, -11, 10, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.air.getDefaultState(), 12, -11, 10, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.air.getDefaultState(), 12, -10, 10, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 13, -10, 10, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 13, -11, 10, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.air.getDefaultState(), 10, -11, 8, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.air.getDefaultState(), 10, -10, 8, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 10, -10, 7, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 10, -11, 7, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.air.getDefaultState(), 10, -11, 12, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.air.getDefaultState(), 10, -10, 12, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 10, -10, 13, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 10, -11, 13, p_74875_3_);
                Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

                while (iterator.hasNext())
                {
                    EnumFacing enumfacing = (EnumFacing)iterator.next();

                    if (!this.field_74940_h[enumfacing.getHorizontalIndex()])
                    {
                        int k1 = enumfacing.getFrontOffsetX() * 2;
                        int l1 = enumfacing.getFrontOffsetZ() * 2;
                        this.field_74940_h[enumfacing.getHorizontalIndex()] = this.func_180778_a(worldIn, p_74875_3_, p_74875_2_, 10 + k1, -11, 10 + l1, ChestGenHooks.getItems(PYRAMID_DESERT_CHEST, p_74875_2_), ChestGenHooks.getCount(PYRAMID_DESERT_CHEST, p_74875_2_));
                    }
                }

                return true;
            }
        }

    abstract static class Feature extends StructureComponent
        {
            /** The size of the bounding box for this feature in the X axis */
            protected int scatteredFeatureSizeX;
            /** The size of the bounding box for this feature in the Y axis */
            protected int scatteredFeatureSizeY;
            /** The size of the bounding box for this feature in the Z axis */
            protected int scatteredFeatureSizeZ;
            protected int field_74936_d = -1;
            private static final String __OBFID = "CL_00000479";

            public Feature() {}

            protected Feature(Random p_i2065_1_, int p_i2065_2_, int p_i2065_3_, int p_i2065_4_, int p_i2065_5_, int p_i2065_6_, int p_i2065_7_)
            {
                super(0);
                this.scatteredFeatureSizeX = p_i2065_5_;
                this.scatteredFeatureSizeY = p_i2065_6_;
                this.scatteredFeatureSizeZ = p_i2065_7_;
                this.coordBaseMode = EnumFacing.Plane.HORIZONTAL.random(p_i2065_1_);

                switch (ComponentScatteredFeaturePieces.SwitchEnumFacing.field_175956_a[this.coordBaseMode.ordinal()])
                {
                    case 1:
                    case 2:
                        this.boundingBox = new StructureBoundingBox(p_i2065_2_, p_i2065_3_, p_i2065_4_, p_i2065_2_ + p_i2065_5_ - 1, p_i2065_3_ + p_i2065_6_ - 1, p_i2065_4_ + p_i2065_7_ - 1);
                        break;
                    default:
                        this.boundingBox = new StructureBoundingBox(p_i2065_2_, p_i2065_3_, p_i2065_4_, p_i2065_2_ + p_i2065_7_ - 1, p_i2065_3_ + p_i2065_6_ - 1, p_i2065_4_ + p_i2065_5_ - 1);
                }
            }

            /**
             * (abstract) Helper method to write subclass data to NBT
             */
            protected void writeStructureToNBT(NBTTagCompound p_143012_1_)
            {
                p_143012_1_.setInteger("Width", this.scatteredFeatureSizeX);
                p_143012_1_.setInteger("Height", this.scatteredFeatureSizeY);
                p_143012_1_.setInteger("Depth", this.scatteredFeatureSizeZ);
                p_143012_1_.setInteger("HPos", this.field_74936_d);
            }

            /**
             * (abstract) Helper method to read subclass data from NBT
             */
            protected void readStructureFromNBT(NBTTagCompound p_143011_1_)
            {
                this.scatteredFeatureSizeX = p_143011_1_.getInteger("Width");
                this.scatteredFeatureSizeY = p_143011_1_.getInteger("Height");
                this.scatteredFeatureSizeZ = p_143011_1_.getInteger("Depth");
                this.field_74936_d = p_143011_1_.getInteger("HPos");
            }

            protected boolean func_74935_a(World worldIn, StructureBoundingBox p_74935_2_, int p_74935_3_)
            {
                if (this.field_74936_d >= 0)
                {
                    return true;
                }
                else
                {
                    int j = 0;
                    int k = 0;

                    for (int l = this.boundingBox.minZ; l <= this.boundingBox.maxZ; ++l)
                    {
                        for (int i1 = this.boundingBox.minX; i1 <= this.boundingBox.maxX; ++i1)
                        {
                            BlockPos blockpos = new BlockPos(i1, 64, l);

                            if (p_74935_2_.func_175898_b(blockpos))
                            {
                                j += Math.max(worldIn.getTopSolidOrLiquidBlock(blockpos).getY(), worldIn.provider.getAverageGroundLevel());
                                ++k;
                            }
                        }
                    }

                    if (k == 0)
                    {
                        return false;
                    }
                    else
                    {
                        this.field_74936_d = j / k;
                        this.boundingBox.offset(0, this.field_74936_d - this.boundingBox.minY + p_74935_3_, 0);
                        return true;
                    }
                }
            }
        }

    public static class JunglePyramid extends ComponentScatteredFeaturePieces.Feature
        {
            private boolean field_74947_h;
            private boolean field_74948_i;
            private boolean field_74945_j;
            private boolean field_74946_k;
            private static final List field_175816_i = Lists.newArrayList(new WeightedRandomChestContent[] {new WeightedRandomChestContent(Items.diamond, 0, 1, 3, 3), new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10), new WeightedRandomChestContent(Items.gold_ingot, 0, 2, 7, 15), new WeightedRandomChestContent(Items.emerald, 0, 1, 3, 2), new WeightedRandomChestContent(Items.bone, 0, 4, 6, 20), new WeightedRandomChestContent(Items.rotten_flesh, 0, 3, 7, 16), new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 3), new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 1), new WeightedRandomChestContent(Items.golden_horse_armor, 0, 1, 1, 1), new WeightedRandomChestContent(Items.diamond_horse_armor, 0, 1, 1, 1)});
            private static final List field_175815_j = Lists.newArrayList(new WeightedRandomChestContent[] {new WeightedRandomChestContent(Items.arrow, 0, 2, 7, 30)});
            /** List of random stones to be generated in the Jungle Pyramid. */
            private static ComponentScatteredFeaturePieces.JunglePyramid.Stones junglePyramidsRandomScatteredStones = new ComponentScatteredFeaturePieces.JunglePyramid.Stones((ComponentScatteredFeaturePieces.SwitchEnumFacing)null);
            private static final String __OBFID = "CL_00000477";

            static
            {
                ChestGenHooks.init(PYRAMID_JUNGLE_DISPENSER, field_175815_j, 2, 2);
                ChestGenHooks.init(PYRAMID_JUNGLE_CHEST, field_175816_i, 2, 7);
                ChestGenHooks.addItem(PYRAMID_JUNGLE_CHEST, new WeightedRandomChestContent(new ItemStack(Items.enchanted_book, 1, 0), 1, 1, 1));
            }

            public JunglePyramid() {}

            public JunglePyramid(Random p_i2064_1_, int p_i2064_2_, int p_i2064_3_)
            {
                super(p_i2064_1_, p_i2064_2_, 64, p_i2064_3_, 12, 10, 15);
            }

            /**
             * (abstract) Helper method to write subclass data to NBT
             */
            protected void writeStructureToNBT(NBTTagCompound p_143012_1_)
            {
                super.writeStructureToNBT(p_143012_1_);
                p_143012_1_.setBoolean("placedMainChest", this.field_74947_h);
                p_143012_1_.setBoolean("placedHiddenChest", this.field_74948_i);
                p_143012_1_.setBoolean("placedTrap1", this.field_74945_j);
                p_143012_1_.setBoolean("placedTrap2", this.field_74946_k);
            }

            /**
             * (abstract) Helper method to read subclass data from NBT
             */
            protected void readStructureFromNBT(NBTTagCompound p_143011_1_)
            {
                super.readStructureFromNBT(p_143011_1_);
                this.field_74947_h = p_143011_1_.getBoolean("placedMainChest");
                this.field_74948_i = p_143011_1_.getBoolean("placedHiddenChest");
                this.field_74945_j = p_143011_1_.getBoolean("placedTrap1");
                this.field_74946_k = p_143011_1_.getBoolean("placedTrap2");
            }

            /**
             * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes
             * Mineshafts at the end, it adds Fences...
             */
            public boolean addComponentParts(World worldIn, Random p_74875_2_, StructureBoundingBox p_74875_3_)
            {
                if (!this.func_74935_a(worldIn, p_74875_3_, 0))
                {
                    return false;
                }
                else
                {
                    int i = this.getMetadataWithOffset(Blocks.stone_stairs, 3);
                    int j = this.getMetadataWithOffset(Blocks.stone_stairs, 2);
                    int k = this.getMetadataWithOffset(Blocks.stone_stairs, 0);
                    int l = this.getMetadataWithOffset(Blocks.stone_stairs, 1);
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 0, -4, 0, this.scatteredFeatureSizeX - 1, 0, this.scatteredFeatureSizeZ - 1, false, p_74875_2_, junglePyramidsRandomScatteredStones);
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 2, 1, 2, 9, 2, 2, false, p_74875_2_, junglePyramidsRandomScatteredStones);
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 2, 1, 12, 9, 2, 12, false, p_74875_2_, junglePyramidsRandomScatteredStones);
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 2, 1, 3, 2, 2, 11, false, p_74875_2_, junglePyramidsRandomScatteredStones);
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 9, 1, 3, 9, 2, 11, false, p_74875_2_, junglePyramidsRandomScatteredStones);
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 1, 3, 1, 10, 6, 1, false, p_74875_2_, junglePyramidsRandomScatteredStones);
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 1, 3, 13, 10, 6, 13, false, p_74875_2_, junglePyramidsRandomScatteredStones);
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 1, 3, 2, 1, 6, 12, false, p_74875_2_, junglePyramidsRandomScatteredStones);
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 10, 3, 2, 10, 6, 12, false, p_74875_2_, junglePyramidsRandomScatteredStones);
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 2, 3, 2, 9, 3, 12, false, p_74875_2_, junglePyramidsRandomScatteredStones);
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 2, 6, 2, 9, 6, 12, false, p_74875_2_, junglePyramidsRandomScatteredStones);
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 3, 7, 3, 8, 7, 11, false, p_74875_2_, junglePyramidsRandomScatteredStones);
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 4, 8, 4, 7, 8, 10, false, p_74875_2_, junglePyramidsRandomScatteredStones);
                    this.fillWithAir(worldIn, p_74875_3_, 3, 1, 3, 8, 2, 11);
                    this.fillWithAir(worldIn, p_74875_3_, 4, 3, 6, 7, 3, 9);
                    this.fillWithAir(worldIn, p_74875_3_, 2, 4, 2, 9, 5, 12);
                    this.fillWithAir(worldIn, p_74875_3_, 4, 6, 5, 7, 6, 9);
                    this.fillWithAir(worldIn, p_74875_3_, 5, 7, 6, 6, 7, 8);
                    this.fillWithAir(worldIn, p_74875_3_, 5, 1, 2, 6, 2, 2);
                    this.fillWithAir(worldIn, p_74875_3_, 5, 2, 12, 6, 2, 12);
                    this.fillWithAir(worldIn, p_74875_3_, 5, 5, 1, 6, 5, 1);
                    this.fillWithAir(worldIn, p_74875_3_, 5, 5, 13, 6, 5, 13);
                    this.func_175811_a(worldIn, Blocks.air.getDefaultState(), 1, 5, 5, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.air.getDefaultState(), 10, 5, 5, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.air.getDefaultState(), 1, 5, 9, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.air.getDefaultState(), 10, 5, 9, p_74875_3_);
                    int i1;

                    for (i1 = 0; i1 <= 14; i1 += 14)
                    {
                        this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 2, 4, i1, 2, 5, i1, false, p_74875_2_, junglePyramidsRandomScatteredStones);
                        this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 4, 4, i1, 4, 5, i1, false, p_74875_2_, junglePyramidsRandomScatteredStones);
                        this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 7, 4, i1, 7, 5, i1, false, p_74875_2_, junglePyramidsRandomScatteredStones);
                        this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 9, 4, i1, 9, 5, i1, false, p_74875_2_, junglePyramidsRandomScatteredStones);
                    }

                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 5, 6, 0, 6, 6, 0, false, p_74875_2_, junglePyramidsRandomScatteredStones);

                    for (i1 = 0; i1 <= 11; i1 += 11)
                    {
                        for (int j1 = 2; j1 <= 12; j1 += 2)
                        {
                            this.fillWithRandomizedBlocks(worldIn, p_74875_3_, i1, 4, j1, i1, 5, j1, false, p_74875_2_, junglePyramidsRandomScatteredStones);
                        }

                        this.fillWithRandomizedBlocks(worldIn, p_74875_3_, i1, 6, 5, i1, 6, 5, false, p_74875_2_, junglePyramidsRandomScatteredStones);
                        this.fillWithRandomizedBlocks(worldIn, p_74875_3_, i1, 6, 9, i1, 6, 9, false, p_74875_2_, junglePyramidsRandomScatteredStones);
                    }

                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 2, 7, 2, 2, 9, 2, false, p_74875_2_, junglePyramidsRandomScatteredStones);
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 9, 7, 2, 9, 9, 2, false, p_74875_2_, junglePyramidsRandomScatteredStones);
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 2, 7, 12, 2, 9, 12, false, p_74875_2_, junglePyramidsRandomScatteredStones);
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 9, 7, 12, 9, 9, 12, false, p_74875_2_, junglePyramidsRandomScatteredStones);
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 4, 9, 4, 4, 9, 4, false, p_74875_2_, junglePyramidsRandomScatteredStones);
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 7, 9, 4, 7, 9, 4, false, p_74875_2_, junglePyramidsRandomScatteredStones);
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 4, 9, 10, 4, 9, 10, false, p_74875_2_, junglePyramidsRandomScatteredStones);
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 7, 9, 10, 7, 9, 10, false, p_74875_2_, junglePyramidsRandomScatteredStones);
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 5, 9, 7, 6, 9, 7, false, p_74875_2_, junglePyramidsRandomScatteredStones);
                    this.func_175811_a(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 5, 9, 6, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 6, 9, 6, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stone_stairs.getStateFromMeta(j), 5, 9, 8, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stone_stairs.getStateFromMeta(j), 6, 9, 8, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 4, 0, 0, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 5, 0, 0, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 6, 0, 0, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 7, 0, 0, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 4, 1, 8, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 4, 2, 9, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 4, 3, 10, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 7, 1, 8, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 7, 2, 9, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 7, 3, 10, p_74875_3_);
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 4, 1, 9, 4, 1, 9, false, p_74875_2_, junglePyramidsRandomScatteredStones);
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 7, 1, 9, 7, 1, 9, false, p_74875_2_, junglePyramidsRandomScatteredStones);
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 4, 1, 10, 7, 2, 10, false, p_74875_2_, junglePyramidsRandomScatteredStones);
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 5, 4, 5, 6, 4, 5, false, p_74875_2_, junglePyramidsRandomScatteredStones);
                    this.func_175811_a(worldIn, Blocks.stone_stairs.getStateFromMeta(k), 4, 4, 5, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stone_stairs.getStateFromMeta(l), 7, 4, 5, p_74875_3_);

                    for (i1 = 0; i1 < 4; ++i1)
                    {
                        this.func_175811_a(worldIn, Blocks.stone_stairs.getStateFromMeta(j), 5, 0 - i1, 6 + i1, p_74875_3_);
                        this.func_175811_a(worldIn, Blocks.stone_stairs.getStateFromMeta(j), 6, 0 - i1, 6 + i1, p_74875_3_);
                        this.fillWithAir(worldIn, p_74875_3_, 5, 0 - i1, 7 + i1, 6, 0 - i1, 9 + i1);
                    }

                    this.fillWithAir(worldIn, p_74875_3_, 1, -3, 12, 10, -1, 13);
                    this.fillWithAir(worldIn, p_74875_3_, 1, -3, 1, 3, -1, 13);
                    this.fillWithAir(worldIn, p_74875_3_, 1, -3, 1, 9, -1, 5);

                    for (i1 = 1; i1 <= 13; i1 += 2)
                    {
                        this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 1, -3, i1, 1, -2, i1, false, p_74875_2_, junglePyramidsRandomScatteredStones);
                    }

                    for (i1 = 2; i1 <= 12; i1 += 2)
                    {
                        this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 1, -1, i1, 3, -1, i1, false, p_74875_2_, junglePyramidsRandomScatteredStones);
                    }

                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 2, -2, 1, 5, -2, 1, false, p_74875_2_, junglePyramidsRandomScatteredStones);
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 7, -2, 1, 9, -2, 1, false, p_74875_2_, junglePyramidsRandomScatteredStones);
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 6, -3, 1, 6, -3, 1, false, p_74875_2_, junglePyramidsRandomScatteredStones);
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 6, -1, 1, 6, -1, 1, false, p_74875_2_, junglePyramidsRandomScatteredStones);
                    this.func_175811_a(worldIn, Blocks.tripwire_hook.getStateFromMeta(this.getMetadataWithOffset(Blocks.tripwire_hook, EnumFacing.EAST.getHorizontalIndex())).withProperty(BlockTripWireHook.ATTACHED, Boolean.valueOf(true)), 1, -3, 8, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.tripwire_hook.getStateFromMeta(this.getMetadataWithOffset(Blocks.tripwire_hook, EnumFacing.WEST.getHorizontalIndex())).withProperty(BlockTripWireHook.ATTACHED, Boolean.valueOf(true)), 4, -3, 8, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.tripwire.getDefaultState().withProperty(BlockTripWire.ATTACHED, Boolean.valueOf(true)), 2, -3, 8, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.tripwire.getDefaultState().withProperty(BlockTripWire.ATTACHED, Boolean.valueOf(true)), 3, -3, 8, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.redstone_wire.getDefaultState(), 5, -3, 7, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.redstone_wire.getDefaultState(), 5, -3, 6, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.redstone_wire.getDefaultState(), 5, -3, 5, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.redstone_wire.getDefaultState(), 5, -3, 4, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.redstone_wire.getDefaultState(), 5, -3, 3, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.redstone_wire.getDefaultState(), 5, -3, 2, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.redstone_wire.getDefaultState(), 5, -3, 1, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.redstone_wire.getDefaultState(), 4, -3, 1, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.mossy_cobblestone.getDefaultState(), 3, -3, 1, p_74875_3_);

                    ChestGenHooks dispenser = ChestGenHooks.getInfo(PYRAMID_JUNGLE_DISPENSER);
                    ChestGenHooks chest = ChestGenHooks.getInfo(PYRAMID_JUNGLE_CHEST);

                    if (!this.field_74945_j)
                    {
                        this.field_74945_j = this.func_175806_a(worldIn, p_74875_3_, p_74875_2_, 3, -2, 1, EnumFacing.NORTH.getIndex(), dispenser.getItems(p_74875_2_), dispenser.getCount(p_74875_2_));
                    }

                    this.func_175811_a(worldIn, Blocks.vine.getStateFromMeta(15), 3, -2, 2, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.tripwire_hook.getStateFromMeta(this.getMetadataWithOffset(Blocks.tripwire_hook, EnumFacing.NORTH.getHorizontalIndex())).withProperty(BlockTripWireHook.ATTACHED, Boolean.valueOf(true)), 7, -3, 1, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.tripwire_hook.getStateFromMeta(this.getMetadataWithOffset(Blocks.tripwire_hook, EnumFacing.SOUTH.getHorizontalIndex())).withProperty(BlockTripWireHook.ATTACHED, Boolean.valueOf(true)), 7, -3, 5, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.tripwire.getDefaultState().withProperty(BlockTripWire.ATTACHED, Boolean.valueOf(true)), 7, -3, 2, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.tripwire.getDefaultState().withProperty(BlockTripWire.ATTACHED, Boolean.valueOf(true)), 7, -3, 3, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.tripwire.getDefaultState().withProperty(BlockTripWire.ATTACHED, Boolean.valueOf(true)), 7, -3, 4, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.redstone_wire.getDefaultState(), 8, -3, 6, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.redstone_wire.getDefaultState(), 9, -3, 6, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.redstone_wire.getDefaultState(), 9, -3, 5, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.mossy_cobblestone.getDefaultState(), 9, -3, 4, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.redstone_wire.getDefaultState(), 9, -2, 4, p_74875_3_);

                    if (!this.field_74946_k)
                    {
                        this.field_74946_k = this.func_175806_a(worldIn, p_74875_3_, p_74875_2_, 9, -2, 3, EnumFacing.WEST.getIndex(), dispenser.getItems(p_74875_2_), dispenser.getCount(p_74875_2_));
                    }

                    this.func_175811_a(worldIn, Blocks.vine.getStateFromMeta(15), 8, -1, 3, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.vine.getStateFromMeta(15), 8, -2, 3, p_74875_3_);

                    if (!this.field_74947_h)
                    {
                        this.field_74947_h = this.func_180778_a(worldIn, p_74875_3_, p_74875_2_, 8, -3, 3, chest.getItems(p_74875_2_), chest.getCount(p_74875_2_));
                    }

                    this.func_175811_a(worldIn, Blocks.mossy_cobblestone.getDefaultState(), 9, -3, 2, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.mossy_cobblestone.getDefaultState(), 8, -3, 1, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.mossy_cobblestone.getDefaultState(), 4, -3, 5, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.mossy_cobblestone.getDefaultState(), 5, -2, 5, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.mossy_cobblestone.getDefaultState(), 5, -1, 5, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.mossy_cobblestone.getDefaultState(), 6, -3, 5, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.mossy_cobblestone.getDefaultState(), 7, -2, 5, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.mossy_cobblestone.getDefaultState(), 7, -1, 5, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.mossy_cobblestone.getDefaultState(), 8, -3, 5, p_74875_3_);
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 9, -1, 1, 9, -1, 5, false, p_74875_2_, junglePyramidsRandomScatteredStones);
                    this.fillWithAir(worldIn, p_74875_3_, 8, -3, 8, 10, -1, 10);
                    this.func_175811_a(worldIn, Blocks.stonebrick.getStateFromMeta(BlockStoneBrick.CHISELED_META), 8, -2, 11, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stonebrick.getStateFromMeta(BlockStoneBrick.CHISELED_META), 9, -2, 11, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stonebrick.getStateFromMeta(BlockStoneBrick.CHISELED_META), 10, -2, 11, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.lever.getStateFromMeta(BlockLever.getMetadataForFacing(EnumFacing.getFront(this.getMetadataWithOffset(Blocks.lever, EnumFacing.NORTH.getIndex())))), 8, -2, 12, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.lever.getStateFromMeta(BlockLever.getMetadataForFacing(EnumFacing.getFront(this.getMetadataWithOffset(Blocks.lever, EnumFacing.NORTH.getIndex())))), 9, -2, 12, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.lever.getStateFromMeta(BlockLever.getMetadataForFacing(EnumFacing.getFront(this.getMetadataWithOffset(Blocks.lever, EnumFacing.NORTH.getIndex())))), 10, -2, 12, p_74875_3_);
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 8, -3, 8, 8, -3, 10, false, p_74875_2_, junglePyramidsRandomScatteredStones);
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 10, -3, 8, 10, -3, 10, false, p_74875_2_, junglePyramidsRandomScatteredStones);
                    this.func_175811_a(worldIn, Blocks.mossy_cobblestone.getDefaultState(), 10, -2, 9, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.redstone_wire.getDefaultState(), 8, -2, 9, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.redstone_wire.getDefaultState(), 8, -2, 10, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.redstone_wire.getDefaultState(), 10, -1, 9, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.sticky_piston.getStateFromMeta(EnumFacing.UP.getIndex()), 9, -2, 8, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.sticky_piston.getStateFromMeta(this.getMetadataWithOffset(Blocks.sticky_piston, EnumFacing.WEST.getIndex())), 10, -2, 8, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.sticky_piston.getStateFromMeta(this.getMetadataWithOffset(Blocks.sticky_piston, EnumFacing.WEST.getIndex())), 10, -1, 8, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.unpowered_repeater.getStateFromMeta(this.getMetadataWithOffset(Blocks.unpowered_repeater, EnumFacing.NORTH.getHorizontalIndex())), 10, -2, 10, p_74875_3_);

                    if (!this.field_74948_i)
                    {
                        this.field_74948_i = this.func_180778_a(worldIn, p_74875_3_, p_74875_2_, 9, -3, 10, chest.getItems(p_74875_2_), chest.getCount(p_74875_2_));
                    }

                    return true;
                }
            }

            static class Stones extends StructureComponent.BlockSelector
                {
                    private static final String __OBFID = "CL_00000478";

                    private Stones() {}

                    /**
                     * picks Block Ids and Metadata (Silverfish)
                     */
                    public void selectBlocks(Random p_75062_1_, int p_75062_2_, int p_75062_3_, int p_75062_4_, boolean p_75062_5_)
                    {
                        if (p_75062_1_.nextFloat() < 0.4F)
                        {
                            this.field_151562_a = Blocks.cobblestone.getDefaultState();
                        }
                        else
                        {
                            this.field_151562_a = Blocks.mossy_cobblestone.getDefaultState();
                        }
                    }

                    Stones(ComponentScatteredFeaturePieces.SwitchEnumFacing p_i45583_1_)
                    {
                        this();
                    }
                }
        }

    public static class SwampHut extends ComponentScatteredFeaturePieces.Feature
        {
            /** Whether this swamp hut has a witch. */
            private boolean hasWitch;
            private static final String __OBFID = "CL_00000480";

            public SwampHut() {}

            public SwampHut(Random p_i2066_1_, int p_i2066_2_, int p_i2066_3_)
            {
                super(p_i2066_1_, p_i2066_2_, 64, p_i2066_3_, 7, 5, 9);
            }

            /**
             * (abstract) Helper method to write subclass data to NBT
             */
            protected void writeStructureToNBT(NBTTagCompound p_143012_1_)
            {
                super.writeStructureToNBT(p_143012_1_);
                p_143012_1_.setBoolean("Witch", this.hasWitch);
            }

            /**
             * (abstract) Helper method to read subclass data from NBT
             */
            protected void readStructureFromNBT(NBTTagCompound p_143011_1_)
            {
                super.readStructureFromNBT(p_143011_1_);
                this.hasWitch = p_143011_1_.getBoolean("Witch");
            }

            /**
             * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes
             * Mineshafts at the end, it adds Fences...
             */
            public boolean addComponentParts(World worldIn, Random p_74875_2_, StructureBoundingBox p_74875_3_)
            {
                if (!this.func_74935_a(worldIn, p_74875_3_, 0))
                {
                    return false;
                }
                else
                {
                    this.func_175804_a(worldIn, p_74875_3_, 1, 1, 1, 5, 1, 7, Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
                    this.func_175804_a(worldIn, p_74875_3_, 1, 4, 2, 5, 4, 7, Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
                    this.func_175804_a(worldIn, p_74875_3_, 2, 1, 0, 4, 1, 0, Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
                    this.func_175804_a(worldIn, p_74875_3_, 2, 2, 2, 3, 3, 2, Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
                    this.func_175804_a(worldIn, p_74875_3_, 1, 2, 3, 1, 3, 6, Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
                    this.func_175804_a(worldIn, p_74875_3_, 5, 2, 3, 5, 3, 6, Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
                    this.func_175804_a(worldIn, p_74875_3_, 2, 2, 7, 4, 3, 7, Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
                    this.func_175804_a(worldIn, p_74875_3_, 1, 0, 2, 1, 3, 2, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
                    this.func_175804_a(worldIn, p_74875_3_, 5, 0, 2, 5, 3, 2, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
                    this.func_175804_a(worldIn, p_74875_3_, 1, 0, 7, 1, 3, 7, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
                    this.func_175804_a(worldIn, p_74875_3_, 5, 0, 7, 5, 3, 7, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
                    this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), 2, 3, 2, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), 3, 3, 7, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.air.getDefaultState(), 1, 3, 4, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.air.getDefaultState(), 5, 3, 4, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.air.getDefaultState(), 5, 3, 5, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.flower_pot.getDefaultState().withProperty(BlockFlowerPot.CONTENTS, BlockFlowerPot.EnumFlowerType.MUSHROOM_RED), 1, 3, 5, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.crafting_table.getDefaultState(), 3, 2, 6, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.cauldron.getDefaultState(), 4, 2, 6, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), 1, 2, 1, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), 5, 2, 1, p_74875_3_);
                    int i = this.getMetadataWithOffset(Blocks.oak_stairs, 3);
                    int j = this.getMetadataWithOffset(Blocks.oak_stairs, 1);
                    int k = this.getMetadataWithOffset(Blocks.oak_stairs, 0);
                    int l = this.getMetadataWithOffset(Blocks.oak_stairs, 2);
                    this.func_175804_a(worldIn, p_74875_3_, 0, 4, 1, 6, 4, 1, Blocks.spruce_stairs.getStateFromMeta(i), Blocks.spruce_stairs.getStateFromMeta(i), false);
                    this.func_175804_a(worldIn, p_74875_3_, 0, 4, 2, 0, 4, 7, Blocks.spruce_stairs.getStateFromMeta(k), Blocks.spruce_stairs.getStateFromMeta(k), false);
                    this.func_175804_a(worldIn, p_74875_3_, 6, 4, 2, 6, 4, 7, Blocks.spruce_stairs.getStateFromMeta(j), Blocks.spruce_stairs.getStateFromMeta(j), false);
                    this.func_175804_a(worldIn, p_74875_3_, 0, 4, 8, 6, 4, 8, Blocks.spruce_stairs.getStateFromMeta(l), Blocks.spruce_stairs.getStateFromMeta(l), false);
                    int i1;
                    int j1;

                    for (i1 = 2; i1 <= 7; i1 += 5)
                    {
                        for (j1 = 1; j1 <= 5; j1 += 4)
                        {
                            this.func_175808_b(worldIn, Blocks.log.getDefaultState(), j1, -1, i1, p_74875_3_);
                        }
                    }

                    if (!this.hasWitch)
                    {
                        i1 = this.getXWithOffset(2, 5);
                        j1 = this.getYWithOffset(2);
                        int k1 = this.getZWithOffset(2, 5);

                        if (p_74875_3_.func_175898_b(new BlockPos(i1, j1, k1)))
                        {
                            this.hasWitch = true;
                            EntityWitch entitywitch = new EntityWitch(worldIn);
                            entitywitch.setLocationAndAngles((double)i1 + 0.5D, (double)j1, (double)k1 + 0.5D, 0.0F, 0.0F);
                            entitywitch.func_180482_a(worldIn.getDifficultyForLocation(new BlockPos(i1, j1, k1)), (IEntityLivingData)null);
                            worldIn.spawnEntityInWorld(entitywitch);
                        }
                    }

                    return true;
                }
            }
        }

    static final class SwitchEnumFacing
        {
            static final int[] field_175956_a = new int[EnumFacing.values().length];
            private static final String __OBFID = "CL_00001971";

            static
            {
                try
                {
                    field_175956_a[EnumFacing.NORTH.ordinal()] = 1;
                }
                catch (NoSuchFieldError var2)
                {
                    ;
                }

                try
                {
                    field_175956_a[EnumFacing.SOUTH.ordinal()] = 2;
                }
                catch (NoSuchFieldError var1)
                {
                    ;
                }
            }
        }
}