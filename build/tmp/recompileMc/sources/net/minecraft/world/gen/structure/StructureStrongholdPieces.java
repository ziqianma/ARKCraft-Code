package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraftforge.common.ChestGenHooks;
import static net.minecraftforge.common.ChestGenHooks.*;

public class StructureStrongholdPieces
{
    private static final StructureStrongholdPieces.PieceWeight[] pieceWeightArray = new StructureStrongholdPieces.PieceWeight[] {new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.Straight.class, 40, 0), new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.Prison.class, 5, 5), new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.LeftTurn.class, 20, 0), new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.RightTurn.class, 20, 0), new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.RoomCrossing.class, 10, 6), new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.StairsStraight.class, 5, 5), new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.Stairs.class, 5, 5), new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.Crossing.class, 5, 4), new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.ChestCorridor.class, 5, 4), new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.Library.class, 10, 2)
    {
        private static final String __OBFID = "CL_00000484";
        public boolean canSpawnMoreStructuresOfType(int p_75189_1_)
        {
            return super.canSpawnMoreStructuresOfType(p_75189_1_) && p_75189_1_ > 4;
        }
    }, new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.PortalRoom.class, 20, 1)
    {
        private static final String __OBFID = "CL_00000485";
        public boolean canSpawnMoreStructuresOfType(int p_75189_1_)
        {
            return super.canSpawnMoreStructuresOfType(p_75189_1_) && p_75189_1_ > 5;
        }
    }
                                                                                                                             };
    private static List structurePieceList;
    private static Class strongComponentType;
    static int totalWeight;
    private static final StructureStrongholdPieces.Stones strongholdStones = new StructureStrongholdPieces.Stones(null);
    private static final String __OBFID = "CL_00000483";

    public static void registerStrongholdPieces()
    {
        MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.ChestCorridor.class, "SHCC");
        MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.Corridor.class, "SHFC");
        MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.Crossing.class, "SH5C");
        MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.LeftTurn.class, "SHLT");
        MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.Library.class, "SHLi");
        MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.PortalRoom.class, "SHPR");
        MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.Prison.class, "SHPH");
        MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.RightTurn.class, "SHRT");
        MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.RoomCrossing.class, "SHRC");
        MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.Stairs.class, "SHSD");
        MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.Stairs2.class, "SHStart");
        MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.Straight.class, "SHS");
        MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.StairsStraight.class, "SHSSD");
    }

    /**
     * sets up Arrays with the Structure pieces and their weights
     */
    public static void prepareStructurePieces()
    {
        structurePieceList = Lists.newArrayList();
        StructureStrongholdPieces.PieceWeight[] apieceweight = pieceWeightArray;
        int i = apieceweight.length;

        for (int j = 0; j < i; ++j)
        {
            StructureStrongholdPieces.PieceWeight pieceweight = apieceweight[j];
            pieceweight.instancesSpawned = 0;
            structurePieceList.add(pieceweight);
        }

        strongComponentType = null;
    }

    private static boolean canAddStructurePieces()
    {
        boolean flag = false;
        totalWeight = 0;
        StructureStrongholdPieces.PieceWeight pieceweight;

        for (Iterator iterator = structurePieceList.iterator(); iterator.hasNext(); totalWeight += pieceweight.pieceWeight)
        {
            pieceweight = (StructureStrongholdPieces.PieceWeight)iterator.next();

            if (pieceweight.instancesLimit > 0 && pieceweight.instancesSpawned < pieceweight.instancesLimit)
            {
                flag = true;
            }
        }

        return flag;
    }

    private static StructureStrongholdPieces.Stronghold func_175954_a(Class p_175954_0_, List p_175954_1_, Random p_175954_2_, int p_175954_3_, int p_175954_4_, int p_175954_5_, EnumFacing p_175954_6_, int p_175954_7_)
    {
        Object object = null;

        if (p_175954_0_ == StructureStrongholdPieces.Straight.class)
        {
            object = StructureStrongholdPieces.Straight.func_175862_a(p_175954_1_, p_175954_2_, p_175954_3_, p_175954_4_, p_175954_5_, p_175954_6_, p_175954_7_);
        }
        else if (p_175954_0_ == StructureStrongholdPieces.Prison.class)
        {
            object = StructureStrongholdPieces.Prison.func_175860_a(p_175954_1_, p_175954_2_, p_175954_3_, p_175954_4_, p_175954_5_, p_175954_6_, p_175954_7_);
        }
        else if (p_175954_0_ == StructureStrongholdPieces.LeftTurn.class)
        {
            object = StructureStrongholdPieces.LeftTurn.func_175867_a(p_175954_1_, p_175954_2_, p_175954_3_, p_175954_4_, p_175954_5_, p_175954_6_, p_175954_7_);
        }
        else if (p_175954_0_ == StructureStrongholdPieces.RightTurn.class)
        {
            object = StructureStrongholdPieces.RightTurn.func_175867_a(p_175954_1_, p_175954_2_, p_175954_3_, p_175954_4_, p_175954_5_, p_175954_6_, p_175954_7_);
        }
        else if (p_175954_0_ == StructureStrongholdPieces.RoomCrossing.class)
        {
            object = StructureStrongholdPieces.RoomCrossing.func_175859_a(p_175954_1_, p_175954_2_, p_175954_3_, p_175954_4_, p_175954_5_, p_175954_6_, p_175954_7_);
        }
        else if (p_175954_0_ == StructureStrongholdPieces.StairsStraight.class)
        {
            object = StructureStrongholdPieces.StairsStraight.func_175861_a(p_175954_1_, p_175954_2_, p_175954_3_, p_175954_4_, p_175954_5_, p_175954_6_, p_175954_7_);
        }
        else if (p_175954_0_ == StructureStrongholdPieces.Stairs.class)
        {
            object = StructureStrongholdPieces.Stairs.func_175863_a(p_175954_1_, p_175954_2_, p_175954_3_, p_175954_4_, p_175954_5_, p_175954_6_, p_175954_7_);
        }
        else if (p_175954_0_ == StructureStrongholdPieces.Crossing.class)
        {
            object = StructureStrongholdPieces.Crossing.func_175866_a(p_175954_1_, p_175954_2_, p_175954_3_, p_175954_4_, p_175954_5_, p_175954_6_, p_175954_7_);
        }
        else if (p_175954_0_ == StructureStrongholdPieces.ChestCorridor.class)
        {
            object = StructureStrongholdPieces.ChestCorridor.func_175868_a(p_175954_1_, p_175954_2_, p_175954_3_, p_175954_4_, p_175954_5_, p_175954_6_, p_175954_7_);
        }
        else if (p_175954_0_ == StructureStrongholdPieces.Library.class)
        {
            object = StructureStrongholdPieces.Library.func_175864_a(p_175954_1_, p_175954_2_, p_175954_3_, p_175954_4_, p_175954_5_, p_175954_6_, p_175954_7_);
        }
        else if (p_175954_0_ == StructureStrongholdPieces.PortalRoom.class)
        {
            object = StructureStrongholdPieces.PortalRoom.func_175865_a(p_175954_1_, p_175954_2_, p_175954_3_, p_175954_4_, p_175954_5_, p_175954_6_, p_175954_7_);
        }

        return (StructureStrongholdPieces.Stronghold)object;
    }

    private static StructureStrongholdPieces.Stronghold func_175955_b(StructureStrongholdPieces.Stairs2 p_175955_0_, List p_175955_1_, Random p_175955_2_, int p_175955_3_, int p_175955_4_, int p_175955_5_, EnumFacing p_175955_6_, int p_175955_7_)
    {
        if (!canAddStructurePieces())
        {
            return null;
        }
        else
        {
            if (strongComponentType != null)
            {
                StructureStrongholdPieces.Stronghold stronghold = func_175954_a(strongComponentType, p_175955_1_, p_175955_2_, p_175955_3_, p_175955_4_, p_175955_5_, p_175955_6_, p_175955_7_);
                strongComponentType = null;

                if (stronghold != null)
                {
                    return stronghold;
                }
            }

            int j1 = 0;

            while (j1 < 5)
            {
                ++j1;
                int i1 = p_175955_2_.nextInt(totalWeight);
                Iterator iterator = structurePieceList.iterator();

                while (iterator.hasNext())
                {
                    StructureStrongholdPieces.PieceWeight pieceweight = (StructureStrongholdPieces.PieceWeight)iterator.next();
                    i1 -= pieceweight.pieceWeight;

                    if (i1 < 0)
                    {
                        if (!pieceweight.canSpawnMoreStructuresOfType(p_175955_7_) || pieceweight == p_175955_0_.strongholdPieceWeight)
                        {
                            break;
                        }

                        StructureStrongholdPieces.Stronghold stronghold1 = func_175954_a(pieceweight.pieceClass, p_175955_1_, p_175955_2_, p_175955_3_, p_175955_4_, p_175955_5_, p_175955_6_, p_175955_7_);

                        if (stronghold1 != null)
                        {
                            ++pieceweight.instancesSpawned;
                            p_175955_0_.strongholdPieceWeight = pieceweight;

                            if (!pieceweight.canSpawnMoreStructures())
                            {
                                structurePieceList.remove(pieceweight);
                            }

                            return stronghold1;
                        }
                    }
                }
            }

            StructureBoundingBox structureboundingbox = StructureStrongholdPieces.Corridor.func_175869_a(p_175955_1_, p_175955_2_, p_175955_3_, p_175955_4_, p_175955_5_, p_175955_6_);

            if (structureboundingbox != null && structureboundingbox.minY > 1)
            {
                return new StructureStrongholdPieces.Corridor(p_175955_7_, p_175955_2_, structureboundingbox, p_175955_6_);
            }
            else
            {
                return null;
            }
        }
    }

    private static StructureComponent func_175953_c(StructureStrongholdPieces.Stairs2 p_175953_0_, List p_175953_1_, Random p_175953_2_, int p_175953_3_, int p_175953_4_, int p_175953_5_, EnumFacing p_175953_6_, int p_175953_7_)
    {
        if (p_175953_7_ > 50)
        {
            return null;
        }
        else if (Math.abs(p_175953_3_ - p_175953_0_.getBoundingBox().minX) <= 112 && Math.abs(p_175953_5_ - p_175953_0_.getBoundingBox().minZ) <= 112)
        {
            StructureStrongholdPieces.Stronghold stronghold = func_175955_b(p_175953_0_, p_175953_1_, p_175953_2_, p_175953_3_, p_175953_4_, p_175953_5_, p_175953_6_, p_175953_7_ + 1);

            if (stronghold != null)
            {
                p_175953_1_.add(stronghold);
                p_175953_0_.field_75026_c.add(stronghold);
            }

            return stronghold;
        }
        else
        {
            return null;
        }
    }

    public static class ChestCorridor extends StructureStrongholdPieces.Stronghold
        {
            /** List of items that Stronghold chests can contain. */
            private static final List strongholdChestContents = Lists.newArrayList(new WeightedRandomChestContent[] {new WeightedRandomChestContent(Items.ender_pearl, 0, 1, 1, 10), new WeightedRandomChestContent(Items.diamond, 0, 1, 3, 3), new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10), new WeightedRandomChestContent(Items.gold_ingot, 0, 1, 3, 5), new WeightedRandomChestContent(Items.redstone, 0, 4, 9, 5), new WeightedRandomChestContent(Items.bread, 0, 1, 3, 15), new WeightedRandomChestContent(Items.apple, 0, 1, 3, 15), new WeightedRandomChestContent(Items.iron_pickaxe, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_sword, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_chestplate, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_helmet, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_leggings, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_boots, 0, 1, 1, 5), new WeightedRandomChestContent(Items.golden_apple, 0, 1, 1, 1), new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 1), new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 1), new WeightedRandomChestContent(Items.golden_horse_armor, 0, 1, 1, 1), new WeightedRandomChestContent(Items.diamond_horse_armor, 0, 1, 1, 1)});
            private boolean hasMadeChest;
            private static final String __OBFID = "CL_00000487";

            public ChestCorridor() {}

            static
            {
                ChestGenHooks.init(STRONGHOLD_CORRIDOR, strongholdChestContents, 2, 4);
                ChestGenHooks.addItem(STRONGHOLD_CORRIDOR, new WeightedRandomChestContent(new net.minecraft.item.ItemStack(Items.enchanted_book, 1, 0), 1, 1, 1));
            }

            public ChestCorridor(int p_i45582_1_, Random p_i45582_2_, StructureBoundingBox p_i45582_3_, EnumFacing p_i45582_4_)
            {
                super(p_i45582_1_);
                this.coordBaseMode = p_i45582_4_;
                this.field_143013_d = this.getRandomDoor(p_i45582_2_);
                this.boundingBox = p_i45582_3_;
            }

            /**
             * (abstract) Helper method to write subclass data to NBT
             */
            protected void writeStructureToNBT(NBTTagCompound p_143012_1_)
            {
                super.writeStructureToNBT(p_143012_1_);
                p_143012_1_.setBoolean("Chest", this.hasMadeChest);
            }

            /**
             * (abstract) Helper method to read subclass data from NBT
             */
            protected void readStructureFromNBT(NBTTagCompound p_143011_1_)
            {
                super.readStructureFromNBT(p_143011_1_);
                this.hasMadeChest = p_143011_1_.getBoolean("Chest");
            }

            /**
             * Initiates construction of the Structure Component picked, at the current Location of StructGen
             */
            public void buildComponent(StructureComponent p_74861_1_, List p_74861_2_, Random p_74861_3_)
            {
                this.getNextComponentNormal((StructureStrongholdPieces.Stairs2)p_74861_1_, p_74861_2_, p_74861_3_, 1, 1);
            }

            public static StructureStrongholdPieces.ChestCorridor func_175868_a(List p_175868_0_, Random p_175868_1_, int p_175868_2_, int p_175868_3_, int p_175868_4_, EnumFacing p_175868_5_, int p_175868_6_)
            {
                StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175897_a(p_175868_2_, p_175868_3_, p_175868_4_, -1, -1, 0, 5, 5, 7, p_175868_5_);
                /**
                 * returns false if the Structure Bounding Box goes below 10
                 */
                return canStrongholdGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(p_175868_0_, structureboundingbox) == null ? new StructureStrongholdPieces.ChestCorridor(p_175868_6_, p_175868_1_, structureboundingbox, p_175868_5_) : null;
            }

            /**
             * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes
             * Mineshafts at the end, it adds Fences...
             */
            public boolean addComponentParts(World worldIn, Random p_74875_2_, StructureBoundingBox p_74875_3_)
            {
                if (this.isLiquidInStructureBoundingBox(worldIn, p_74875_3_))
                {
                    return false;
                }
                else
                {
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 0, 0, 0, 4, 4, 6, true, p_74875_2_, StructureStrongholdPieces.strongholdStones);
                    this.placeDoor(worldIn, p_74875_2_, p_74875_3_, this.field_143013_d, 1, 1, 0);
                    this.placeDoor(worldIn, p_74875_2_, p_74875_3_, StructureStrongholdPieces.Stronghold.Door.OPENING, 1, 1, 6);
                    this.func_175804_a(worldIn, p_74875_3_, 3, 1, 2, 3, 1, 4, Blocks.stonebrick.getDefaultState(), Blocks.stonebrick.getDefaultState(), false);
                    this.func_175811_a(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()), 3, 1, 1, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()), 3, 1, 5, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()), 3, 2, 2, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()), 3, 2, 4, p_74875_3_);

                    for (int i = 2; i <= 4; ++i)
                    {
                        this.func_175811_a(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()), 2, 1, i, p_74875_3_);
                    }

                    if (!this.hasMadeChest && p_74875_3_.func_175898_b(new BlockPos(this.getXWithOffset(3, 3), this.getYWithOffset(2), this.getZWithOffset(3, 3))))
                    {
                        this.hasMadeChest = true;
                        this.func_180778_a(worldIn, p_74875_3_, p_74875_2_, 3, 2, 3, ChestGenHooks.getItems(STRONGHOLD_CORRIDOR, p_74875_2_), ChestGenHooks.getCount(STRONGHOLD_CORRIDOR, p_74875_2_));
                    }

                    return true;
                }
            }
        }

    public static class Corridor extends StructureStrongholdPieces.Stronghold
        {
            private int field_74993_a;
            private static final String __OBFID = "CL_00000488";

            public Corridor() {}

            public Corridor(int p_i45581_1_, Random p_i45581_2_, StructureBoundingBox p_i45581_3_, EnumFacing p_i45581_4_)
            {
                super(p_i45581_1_);
                this.coordBaseMode = p_i45581_4_;
                this.boundingBox = p_i45581_3_;
                this.field_74993_a = p_i45581_4_ != EnumFacing.NORTH && p_i45581_4_ != EnumFacing.SOUTH ? p_i45581_3_.getXSize() : p_i45581_3_.getZSize();
            }

            /**
             * (abstract) Helper method to write subclass data to NBT
             */
            protected void writeStructureToNBT(NBTTagCompound p_143012_1_)
            {
                super.writeStructureToNBT(p_143012_1_);
                p_143012_1_.setInteger("Steps", this.field_74993_a);
            }

            /**
             * (abstract) Helper method to read subclass data from NBT
             */
            protected void readStructureFromNBT(NBTTagCompound p_143011_1_)
            {
                super.readStructureFromNBT(p_143011_1_);
                this.field_74993_a = p_143011_1_.getInteger("Steps");
            }

            public static StructureBoundingBox func_175869_a(List p_175869_0_, Random p_175869_1_, int p_175869_2_, int p_175869_3_, int p_175869_4_, EnumFacing p_175869_5_)
            {
                boolean flag = true;
                StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175897_a(p_175869_2_, p_175869_3_, p_175869_4_, -1, -1, 0, 5, 5, 4, p_175869_5_);
                StructureComponent structurecomponent = StructureComponent.findIntersecting(p_175869_0_, structureboundingbox);

                if (structurecomponent == null)
                {
                    return null;
                }
                else
                {
                    if (structurecomponent.getBoundingBox().minY == structureboundingbox.minY)
                    {
                        for (int l = 3; l >= 1; --l)
                        {
                            structureboundingbox = StructureBoundingBox.func_175897_a(p_175869_2_, p_175869_3_, p_175869_4_, -1, -1, 0, 5, 5, l - 1, p_175869_5_);

                            if (!structurecomponent.getBoundingBox().intersectsWith(structureboundingbox))
                            {
                                return StructureBoundingBox.func_175897_a(p_175869_2_, p_175869_3_, p_175869_4_, -1, -1, 0, 5, 5, l, p_175869_5_);
                            }
                        }
                    }

                    return null;
                }
            }

            /**
             * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes
             * Mineshafts at the end, it adds Fences...
             */
            public boolean addComponentParts(World worldIn, Random p_74875_2_, StructureBoundingBox p_74875_3_)
            {
                if (this.isLiquidInStructureBoundingBox(worldIn, p_74875_3_))
                {
                    return false;
                }
                else
                {
                    for (int i = 0; i < this.field_74993_a; ++i)
                    {
                        this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), 0, 0, i, p_74875_3_);
                        this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), 1, 0, i, p_74875_3_);
                        this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), 2, 0, i, p_74875_3_);
                        this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), 3, 0, i, p_74875_3_);
                        this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), 4, 0, i, p_74875_3_);

                        for (int j = 1; j <= 3; ++j)
                        {
                            this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), 0, j, i, p_74875_3_);
                            this.func_175811_a(worldIn, Blocks.air.getDefaultState(), 1, j, i, p_74875_3_);
                            this.func_175811_a(worldIn, Blocks.air.getDefaultState(), 2, j, i, p_74875_3_);
                            this.func_175811_a(worldIn, Blocks.air.getDefaultState(), 3, j, i, p_74875_3_);
                            this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), 4, j, i, p_74875_3_);
                        }

                        this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), 0, 4, i, p_74875_3_);
                        this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), 1, 4, i, p_74875_3_);
                        this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), 2, 4, i, p_74875_3_);
                        this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), 3, 4, i, p_74875_3_);
                        this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), 4, 4, i, p_74875_3_);
                    }

                    return true;
                }
            }
        }

    public static class Crossing extends StructureStrongholdPieces.Stronghold
        {
            private boolean field_74996_b;
            private boolean field_74997_c;
            private boolean field_74995_d;
            private boolean field_74999_h;
            private static final String __OBFID = "CL_00000489";

            public Crossing() {}

            public Crossing(int p_i45580_1_, Random p_i45580_2_, StructureBoundingBox p_i45580_3_, EnumFacing p_i45580_4_)
            {
                super(p_i45580_1_);
                this.coordBaseMode = p_i45580_4_;
                this.field_143013_d = this.getRandomDoor(p_i45580_2_);
                this.boundingBox = p_i45580_3_;
                this.field_74996_b = p_i45580_2_.nextBoolean();
                this.field_74997_c = p_i45580_2_.nextBoolean();
                this.field_74995_d = p_i45580_2_.nextBoolean();
                this.field_74999_h = p_i45580_2_.nextInt(3) > 0;
            }

            /**
             * (abstract) Helper method to write subclass data to NBT
             */
            protected void writeStructureToNBT(NBTTagCompound p_143012_1_)
            {
                super.writeStructureToNBT(p_143012_1_);
                p_143012_1_.setBoolean("leftLow", this.field_74996_b);
                p_143012_1_.setBoolean("leftHigh", this.field_74997_c);
                p_143012_1_.setBoolean("rightLow", this.field_74995_d);
                p_143012_1_.setBoolean("rightHigh", this.field_74999_h);
            }

            /**
             * (abstract) Helper method to read subclass data from NBT
             */
            protected void readStructureFromNBT(NBTTagCompound p_143011_1_)
            {
                super.readStructureFromNBT(p_143011_1_);
                this.field_74996_b = p_143011_1_.getBoolean("leftLow");
                this.field_74997_c = p_143011_1_.getBoolean("leftHigh");
                this.field_74995_d = p_143011_1_.getBoolean("rightLow");
                this.field_74999_h = p_143011_1_.getBoolean("rightHigh");
            }

            /**
             * Initiates construction of the Structure Component picked, at the current Location of StructGen
             */
            public void buildComponent(StructureComponent p_74861_1_, List p_74861_2_, Random p_74861_3_)
            {
                int i = 3;
                int j = 5;

                if (this.coordBaseMode == EnumFacing.WEST || this.coordBaseMode == EnumFacing.NORTH)
                {
                    i = 8 - i;
                    j = 8 - j;
                }

                this.getNextComponentNormal((StructureStrongholdPieces.Stairs2)p_74861_1_, p_74861_2_, p_74861_3_, 5, 1);

                if (this.field_74996_b)
                {
                    this.getNextComponentX((StructureStrongholdPieces.Stairs2)p_74861_1_, p_74861_2_, p_74861_3_, i, 1);
                }

                if (this.field_74997_c)
                {
                    this.getNextComponentX((StructureStrongholdPieces.Stairs2)p_74861_1_, p_74861_2_, p_74861_3_, j, 7);
                }

                if (this.field_74995_d)
                {
                    this.getNextComponentZ((StructureStrongholdPieces.Stairs2)p_74861_1_, p_74861_2_, p_74861_3_, i, 1);
                }

                if (this.field_74999_h)
                {
                    this.getNextComponentZ((StructureStrongholdPieces.Stairs2)p_74861_1_, p_74861_2_, p_74861_3_, j, 7);
                }
            }

            public static StructureStrongholdPieces.Crossing func_175866_a(List p_175866_0_, Random p_175866_1_, int p_175866_2_, int p_175866_3_, int p_175866_4_, EnumFacing p_175866_5_, int p_175866_6_)
            {
                StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175897_a(p_175866_2_, p_175866_3_, p_175866_4_, -4, -3, 0, 10, 9, 11, p_175866_5_);
                /**
                 * returns false if the Structure Bounding Box goes below 10
                 */
                return canStrongholdGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(p_175866_0_, structureboundingbox) == null ? new StructureStrongholdPieces.Crossing(p_175866_6_, p_175866_1_, structureboundingbox, p_175866_5_) : null;
            }

            /**
             * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes
             * Mineshafts at the end, it adds Fences...
             */
            public boolean addComponentParts(World worldIn, Random p_74875_2_, StructureBoundingBox p_74875_3_)
            {
                if (this.isLiquidInStructureBoundingBox(worldIn, p_74875_3_))
                {
                    return false;
                }
                else
                {
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 0, 0, 0, 9, 8, 10, true, p_74875_2_, StructureStrongholdPieces.strongholdStones);
                    this.placeDoor(worldIn, p_74875_2_, p_74875_3_, this.field_143013_d, 4, 3, 0);

                    if (this.field_74996_b)
                    {
                        this.func_175804_a(worldIn, p_74875_3_, 0, 3, 1, 0, 5, 3, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                    }

                    if (this.field_74995_d)
                    {
                        this.func_175804_a(worldIn, p_74875_3_, 9, 3, 1, 9, 5, 3, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                    }

                    if (this.field_74997_c)
                    {
                        this.func_175804_a(worldIn, p_74875_3_, 0, 5, 7, 0, 7, 9, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                    }

                    if (this.field_74999_h)
                    {
                        this.func_175804_a(worldIn, p_74875_3_, 9, 5, 7, 9, 7, 9, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                    }

                    this.func_175804_a(worldIn, p_74875_3_, 5, 1, 10, 7, 3, 10, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 1, 2, 1, 8, 2, 6, false, p_74875_2_, StructureStrongholdPieces.strongholdStones);
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 4, 1, 5, 4, 4, 9, false, p_74875_2_, StructureStrongholdPieces.strongholdStones);
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 8, 1, 5, 8, 4, 9, false, p_74875_2_, StructureStrongholdPieces.strongholdStones);
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 1, 4, 7, 3, 4, 9, false, p_74875_2_, StructureStrongholdPieces.strongholdStones);
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 1, 3, 5, 3, 3, 6, false, p_74875_2_, StructureStrongholdPieces.strongholdStones);
                    this.func_175804_a(worldIn, p_74875_3_, 1, 3, 4, 3, 3, 4, Blocks.stone_slab.getDefaultState(), Blocks.stone_slab.getDefaultState(), false);
                    this.func_175804_a(worldIn, p_74875_3_, 1, 4, 6, 3, 4, 6, Blocks.stone_slab.getDefaultState(), Blocks.stone_slab.getDefaultState(), false);
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 5, 1, 7, 7, 1, 8, false, p_74875_2_, StructureStrongholdPieces.strongholdStones);
                    this.func_175804_a(worldIn, p_74875_3_, 5, 1, 9, 7, 1, 9, Blocks.stone_slab.getDefaultState(), Blocks.stone_slab.getDefaultState(), false);
                    this.func_175804_a(worldIn, p_74875_3_, 5, 2, 7, 7, 2, 7, Blocks.stone_slab.getDefaultState(), Blocks.stone_slab.getDefaultState(), false);
                    this.func_175804_a(worldIn, p_74875_3_, 4, 5, 7, 4, 5, 9, Blocks.stone_slab.getDefaultState(), Blocks.stone_slab.getDefaultState(), false);
                    this.func_175804_a(worldIn, p_74875_3_, 8, 5, 7, 8, 5, 9, Blocks.stone_slab.getDefaultState(), Blocks.stone_slab.getDefaultState(), false);
                    this.func_175804_a(worldIn, p_74875_3_, 5, 5, 7, 7, 5, 9, Blocks.double_stone_slab.getDefaultState(), Blocks.double_stone_slab.getDefaultState(), false);
                    this.func_175811_a(worldIn, Blocks.torch.getDefaultState(), 6, 5, 6, p_74875_3_);
                    return true;
                }
            }
        }

    public static class LeftTurn extends StructureStrongholdPieces.Stronghold
        {
            private static final String __OBFID = "CL_00000490";

            public LeftTurn() {}

            public LeftTurn(int p_i45579_1_, Random p_i45579_2_, StructureBoundingBox p_i45579_3_, EnumFacing p_i45579_4_)
            {
                super(p_i45579_1_);
                this.coordBaseMode = p_i45579_4_;
                this.field_143013_d = this.getRandomDoor(p_i45579_2_);
                this.boundingBox = p_i45579_3_;
            }

            /**
             * Initiates construction of the Structure Component picked, at the current Location of StructGen
             */
            public void buildComponent(StructureComponent p_74861_1_, List p_74861_2_, Random p_74861_3_)
            {
                if (this.coordBaseMode != EnumFacing.NORTH && this.coordBaseMode != EnumFacing.EAST)
                {
                    this.getNextComponentZ((StructureStrongholdPieces.Stairs2)p_74861_1_, p_74861_2_, p_74861_3_, 1, 1);
                }
                else
                {
                    this.getNextComponentX((StructureStrongholdPieces.Stairs2)p_74861_1_, p_74861_2_, p_74861_3_, 1, 1);
                }
            }

            public static StructureStrongholdPieces.LeftTurn func_175867_a(List p_175867_0_, Random p_175867_1_, int p_175867_2_, int p_175867_3_, int p_175867_4_, EnumFacing p_175867_5_, int p_175867_6_)
            {
                StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175897_a(p_175867_2_, p_175867_3_, p_175867_4_, -1, -1, 0, 5, 5, 5, p_175867_5_);
                /**
                 * returns false if the Structure Bounding Box goes below 10
                 */
                return canStrongholdGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(p_175867_0_, structureboundingbox) == null ? new StructureStrongholdPieces.LeftTurn(p_175867_6_, p_175867_1_, structureboundingbox, p_175867_5_) : null;
            }

            /**
             * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes
             * Mineshafts at the end, it adds Fences...
             */
            public boolean addComponentParts(World worldIn, Random p_74875_2_, StructureBoundingBox p_74875_3_)
            {
                if (this.isLiquidInStructureBoundingBox(worldIn, p_74875_3_))
                {
                    return false;
                }
                else
                {
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 0, 0, 0, 4, 4, 4, true, p_74875_2_, StructureStrongholdPieces.strongholdStones);
                    this.placeDoor(worldIn, p_74875_2_, p_74875_3_, this.field_143013_d, 1, 1, 0);

                    if (this.coordBaseMode != EnumFacing.NORTH && this.coordBaseMode != EnumFacing.EAST)
                    {
                        this.func_175804_a(worldIn, p_74875_3_, 4, 1, 1, 4, 3, 3, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                    }
                    else
                    {
                        this.func_175804_a(worldIn, p_74875_3_, 0, 1, 1, 0, 3, 3, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                    }

                    return true;
                }
            }
        }

    public static class Library extends StructureStrongholdPieces.Stronghold
        {
            /** List of items that Stronghold Library chests can contain. */
            private static final List strongholdLibraryChestContents = Lists.newArrayList(new WeightedRandomChestContent[] {new WeightedRandomChestContent(Items.book, 0, 1, 3, 20), new WeightedRandomChestContent(Items.paper, 0, 2, 7, 20), new WeightedRandomChestContent(Items.map, 0, 1, 1, 1), new WeightedRandomChestContent(Items.compass, 0, 1, 1, 1)});
            private boolean isLargeRoom;
            private static final String __OBFID = "CL_00000491";

            static
            {
                ChestGenHooks.init(STRONGHOLD_CORRIDOR, strongholdLibraryChestContents, 1, 5);
                ChestGenHooks.addItem(STRONGHOLD_CORRIDOR, new WeightedRandomChestContent(new net.minecraft.item.ItemStack(Items.enchanted_book, 1, 0), 1, 5, 2));
            }

            public Library() {}

            public Library(int p_i45578_1_, Random p_i45578_2_, StructureBoundingBox p_i45578_3_, EnumFacing p_i45578_4_)
            {
                super(p_i45578_1_);
                this.coordBaseMode = p_i45578_4_;
                this.field_143013_d = this.getRandomDoor(p_i45578_2_);
                this.boundingBox = p_i45578_3_;
                this.isLargeRoom = p_i45578_3_.getYSize() > 6;
            }

            /**
             * (abstract) Helper method to write subclass data to NBT
             */
            protected void writeStructureToNBT(NBTTagCompound p_143012_1_)
            {
                super.writeStructureToNBT(p_143012_1_);
                p_143012_1_.setBoolean("Tall", this.isLargeRoom);
            }

            /**
             * (abstract) Helper method to read subclass data from NBT
             */
            protected void readStructureFromNBT(NBTTagCompound p_143011_1_)
            {
                super.readStructureFromNBT(p_143011_1_);
                this.isLargeRoom = p_143011_1_.getBoolean("Tall");
            }

            public static StructureStrongholdPieces.Library func_175864_a(List p_175864_0_, Random p_175864_1_, int p_175864_2_, int p_175864_3_, int p_175864_4_, EnumFacing p_175864_5_, int p_175864_6_)
            {
                StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175897_a(p_175864_2_, p_175864_3_, p_175864_4_, -4, -1, 0, 14, 11, 15, p_175864_5_);

                if (!canStrongholdGoDeeper(structureboundingbox) || StructureComponent.findIntersecting(p_175864_0_, structureboundingbox) != null)
                {
                    structureboundingbox = StructureBoundingBox.func_175897_a(p_175864_2_, p_175864_3_, p_175864_4_, -4, -1, 0, 14, 6, 15, p_175864_5_);

                    if (!canStrongholdGoDeeper(structureboundingbox) || StructureComponent.findIntersecting(p_175864_0_, structureboundingbox) != null)
                    {
                        return null;
                    }
                }

                return new StructureStrongholdPieces.Library(p_175864_6_, p_175864_1_, structureboundingbox, p_175864_5_);
            }

            /**
             * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes
             * Mineshafts at the end, it adds Fences...
             */
            public boolean addComponentParts(World worldIn, Random p_74875_2_, StructureBoundingBox p_74875_3_)
            {
                if (this.isLiquidInStructureBoundingBox(worldIn, p_74875_3_))
                {
                    return false;
                }
                else
                {
                    byte b0 = 11;

                    if (!this.isLargeRoom)
                    {
                        b0 = 6;
                    }

                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 0, 0, 0, 13, b0 - 1, 14, true, p_74875_2_, StructureStrongholdPieces.strongholdStones);
                    this.placeDoor(worldIn, p_74875_2_, p_74875_3_, this.field_143013_d, 4, 1, 0);
                    this.func_175805_a(worldIn, p_74875_3_, p_74875_2_, 0.07F, 2, 1, 1, 11, 4, 13, Blocks.web.getDefaultState(), Blocks.web.getDefaultState(), false);
                    boolean flag = true;
                    boolean flag1 = true;
                    int i;

                    for (i = 1; i <= 13; ++i)
                    {
                        if ((i - 1) % 4 == 0)
                        {
                            this.func_175804_a(worldIn, p_74875_3_, 1, 1, i, 1, 4, i, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
                            this.func_175804_a(worldIn, p_74875_3_, 12, 1, i, 12, 4, i, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
                            this.func_175811_a(worldIn, Blocks.torch.getDefaultState(), 2, 3, i, p_74875_3_);
                            this.func_175811_a(worldIn, Blocks.torch.getDefaultState(), 11, 3, i, p_74875_3_);

                            if (this.isLargeRoom)
                            {
                                this.func_175804_a(worldIn, p_74875_3_, 1, 6, i, 1, 9, i, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
                                this.func_175804_a(worldIn, p_74875_3_, 12, 6, i, 12, 9, i, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
                            }
                        }
                        else
                        {
                            this.func_175804_a(worldIn, p_74875_3_, 1, 1, i, 1, 4, i, Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
                            this.func_175804_a(worldIn, p_74875_3_, 12, 1, i, 12, 4, i, Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);

                            if (this.isLargeRoom)
                            {
                                this.func_175804_a(worldIn, p_74875_3_, 1, 6, i, 1, 9, i, Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
                                this.func_175804_a(worldIn, p_74875_3_, 12, 6, i, 12, 9, i, Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
                            }
                        }
                    }

                    for (i = 3; i < 12; i += 2)
                    {
                        this.func_175804_a(worldIn, p_74875_3_, 3, 1, i, 4, 3, i, Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
                        this.func_175804_a(worldIn, p_74875_3_, 6, 1, i, 7, 3, i, Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
                        this.func_175804_a(worldIn, p_74875_3_, 9, 1, i, 10, 3, i, Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
                    }

                    if (this.isLargeRoom)
                    {
                        this.func_175804_a(worldIn, p_74875_3_, 1, 5, 1, 3, 5, 13, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
                        this.func_175804_a(worldIn, p_74875_3_, 10, 5, 1, 12, 5, 13, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
                        this.func_175804_a(worldIn, p_74875_3_, 4, 5, 1, 9, 5, 2, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
                        this.func_175804_a(worldIn, p_74875_3_, 4, 5, 12, 9, 5, 13, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
                        this.func_175811_a(worldIn, Blocks.planks.getDefaultState(), 9, 5, 11, p_74875_3_);
                        this.func_175811_a(worldIn, Blocks.planks.getDefaultState(), 8, 5, 11, p_74875_3_);
                        this.func_175811_a(worldIn, Blocks.planks.getDefaultState(), 9, 5, 10, p_74875_3_);
                        this.func_175804_a(worldIn, p_74875_3_, 3, 6, 2, 3, 6, 12, Blocks.oak_fence.getDefaultState(), Blocks.oak_fence.getDefaultState(), false);
                        this.func_175804_a(worldIn, p_74875_3_, 10, 6, 2, 10, 6, 10, Blocks.oak_fence.getDefaultState(), Blocks.oak_fence.getDefaultState(), false);
                        this.func_175804_a(worldIn, p_74875_3_, 4, 6, 2, 9, 6, 2, Blocks.oak_fence.getDefaultState(), Blocks.oak_fence.getDefaultState(), false);
                        this.func_175804_a(worldIn, p_74875_3_, 4, 6, 12, 8, 6, 12, Blocks.oak_fence.getDefaultState(), Blocks.oak_fence.getDefaultState(), false);
                        this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), 9, 6, 11, p_74875_3_);
                        this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), 8, 6, 11, p_74875_3_);
                        this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), 9, 6, 10, p_74875_3_);
                        i = this.getMetadataWithOffset(Blocks.ladder, 3);
                        this.func_175811_a(worldIn, Blocks.ladder.getStateFromMeta(i), 10, 1, 13, p_74875_3_);
                        this.func_175811_a(worldIn, Blocks.ladder.getStateFromMeta(i), 10, 2, 13, p_74875_3_);
                        this.func_175811_a(worldIn, Blocks.ladder.getStateFromMeta(i), 10, 3, 13, p_74875_3_);
                        this.func_175811_a(worldIn, Blocks.ladder.getStateFromMeta(i), 10, 4, 13, p_74875_3_);
                        this.func_175811_a(worldIn, Blocks.ladder.getStateFromMeta(i), 10, 5, 13, p_74875_3_);
                        this.func_175811_a(worldIn, Blocks.ladder.getStateFromMeta(i), 10, 6, 13, p_74875_3_);
                        this.func_175811_a(worldIn, Blocks.ladder.getStateFromMeta(i), 10, 7, 13, p_74875_3_);
                        byte b1 = 7;
                        byte b2 = 7;
                        this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), b1 - 1, 9, b2, p_74875_3_);
                        this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), b1, 9, b2, p_74875_3_);
                        this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), b1 - 1, 8, b2, p_74875_3_);
                        this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), b1, 8, b2, p_74875_3_);
                        this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), b1 - 1, 7, b2, p_74875_3_);
                        this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), b1, 7, b2, p_74875_3_);
                        this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), b1 - 2, 7, b2, p_74875_3_);
                        this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), b1 + 1, 7, b2, p_74875_3_);
                        this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), b1 - 1, 7, b2 - 1, p_74875_3_);
                        this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), b1 - 1, 7, b2 + 1, p_74875_3_);
                        this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), b1, 7, b2 - 1, p_74875_3_);
                        this.func_175811_a(worldIn, Blocks.oak_fence.getDefaultState(), b1, 7, b2 + 1, p_74875_3_);
                        this.func_175811_a(worldIn, Blocks.torch.getDefaultState(), b1 - 2, 8, b2, p_74875_3_);
                        this.func_175811_a(worldIn, Blocks.torch.getDefaultState(), b1 + 1, 8, b2, p_74875_3_);
                        this.func_175811_a(worldIn, Blocks.torch.getDefaultState(), b1 - 1, 8, b2 - 1, p_74875_3_);
                        this.func_175811_a(worldIn, Blocks.torch.getDefaultState(), b1 - 1, 8, b2 + 1, p_74875_3_);
                        this.func_175811_a(worldIn, Blocks.torch.getDefaultState(), b1, 8, b2 - 1, p_74875_3_);
                        this.func_175811_a(worldIn, Blocks.torch.getDefaultState(), b1, 8, b2 + 1, p_74875_3_);
                    }

                    ChestGenHooks info = ChestGenHooks.getInfo(STRONGHOLD_LIBRARY);
                    this.func_180778_a(worldIn, p_74875_3_, p_74875_2_, 3, 3, 5, info.getItems(p_74875_2_), info.getCount(p_74875_2_));

                    if (this.isLargeRoom)
                    {
                        this.func_175811_a(worldIn, Blocks.air.getDefaultState(), 12, 9, 1, p_74875_3_);
                        this.func_180778_a(worldIn, p_74875_3_, p_74875_2_, 12, 8, 1, info.getItems(p_74875_2_), info.getCount(p_74875_2_));
                    }

                    return true;
                }
            }
        }

    static class PieceWeight
        {
            public Class pieceClass;
            /**
             * This basically keeps track of the 'epicness' of a structure. Epic structure components have a higher
             * 'weight', and Structures may only grow up to a certain 'weight' before generation is stopped
             */
            public final int pieceWeight;
            public int instancesSpawned;
            /** How many Structure Pieces of this type may spawn in a structure */
            public int instancesLimit;
            private static final String __OBFID = "CL_00000492";

            public PieceWeight(Class p_i2076_1_, int p_i2076_2_, int p_i2076_3_)
            {
                this.pieceClass = p_i2076_1_;
                this.pieceWeight = p_i2076_2_;
                this.instancesLimit = p_i2076_3_;
            }

            public boolean canSpawnMoreStructuresOfType(int p_75189_1_)
            {
                return this.instancesLimit == 0 || this.instancesSpawned < this.instancesLimit;
            }

            public boolean canSpawnMoreStructures()
            {
                return this.instancesLimit == 0 || this.instancesSpawned < this.instancesLimit;
            }
        }

    public static class PortalRoom extends StructureStrongholdPieces.Stronghold
        {
            private boolean hasSpawner;
            private static final String __OBFID = "CL_00000493";

            public PortalRoom() {}

            public PortalRoom(int p_i45577_1_, Random p_i45577_2_, StructureBoundingBox p_i45577_3_, EnumFacing p_i45577_4_)
            {
                super(p_i45577_1_);
                this.coordBaseMode = p_i45577_4_;
                this.boundingBox = p_i45577_3_;
            }

            /**
             * (abstract) Helper method to write subclass data to NBT
             */
            protected void writeStructureToNBT(NBTTagCompound p_143012_1_)
            {
                super.writeStructureToNBT(p_143012_1_);
                p_143012_1_.setBoolean("Mob", this.hasSpawner);
            }

            /**
             * (abstract) Helper method to read subclass data from NBT
             */
            protected void readStructureFromNBT(NBTTagCompound p_143011_1_)
            {
                super.readStructureFromNBT(p_143011_1_);
                this.hasSpawner = p_143011_1_.getBoolean("Mob");
            }

            /**
             * Initiates construction of the Structure Component picked, at the current Location of StructGen
             */
            public void buildComponent(StructureComponent p_74861_1_, List p_74861_2_, Random p_74861_3_)
            {
                if (p_74861_1_ != null)
                {
                    ((StructureStrongholdPieces.Stairs2)p_74861_1_).strongholdPortalRoom = this;
                }
            }

            public static StructureStrongholdPieces.PortalRoom func_175865_a(List p_175865_0_, Random p_175865_1_, int p_175865_2_, int p_175865_3_, int p_175865_4_, EnumFacing p_175865_5_, int p_175865_6_)
            {
                StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175897_a(p_175865_2_, p_175865_3_, p_175865_4_, -4, -1, 0, 11, 8, 16, p_175865_5_);
                /**
                 * returns false if the Structure Bounding Box goes below 10
                 */
                return canStrongholdGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(p_175865_0_, structureboundingbox) == null ? new StructureStrongholdPieces.PortalRoom(p_175865_6_, p_175865_1_, structureboundingbox, p_175865_5_) : null;
            }

            /**
             * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes
             * Mineshafts at the end, it adds Fences...
             */
            public boolean addComponentParts(World worldIn, Random p_74875_2_, StructureBoundingBox p_74875_3_)
            {
                this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 0, 0, 0, 10, 7, 15, false, p_74875_2_, StructureStrongholdPieces.strongholdStones);
                this.placeDoor(worldIn, p_74875_2_, p_74875_3_, StructureStrongholdPieces.Stronghold.Door.GRATES, 4, 1, 0);
                byte b0 = 6;
                this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 1, b0, 1, 1, b0, 14, false, p_74875_2_, StructureStrongholdPieces.strongholdStones);
                this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 9, b0, 1, 9, b0, 14, false, p_74875_2_, StructureStrongholdPieces.strongholdStones);
                this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 2, b0, 1, 8, b0, 2, false, p_74875_2_, StructureStrongholdPieces.strongholdStones);
                this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 2, b0, 14, 8, b0, 14, false, p_74875_2_, StructureStrongholdPieces.strongholdStones);
                this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 1, 1, 1, 2, 1, 4, false, p_74875_2_, StructureStrongholdPieces.strongholdStones);
                this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 8, 1, 1, 9, 1, 4, false, p_74875_2_, StructureStrongholdPieces.strongholdStones);
                this.func_175804_a(worldIn, p_74875_3_, 1, 1, 1, 1, 1, 3, Blocks.flowing_lava.getDefaultState(), Blocks.flowing_lava.getDefaultState(), false);
                this.func_175804_a(worldIn, p_74875_3_, 9, 1, 1, 9, 1, 3, Blocks.flowing_lava.getDefaultState(), Blocks.flowing_lava.getDefaultState(), false);
                this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 3, 1, 8, 7, 1, 12, false, p_74875_2_, StructureStrongholdPieces.strongholdStones);
                this.func_175804_a(worldIn, p_74875_3_, 4, 1, 9, 6, 1, 11, Blocks.flowing_lava.getDefaultState(), Blocks.flowing_lava.getDefaultState(), false);
                int i;

                for (i = 3; i < 14; i += 2)
                {
                    this.func_175804_a(worldIn, p_74875_3_, 0, 3, i, 0, 4, i, Blocks.iron_bars.getDefaultState(), Blocks.iron_bars.getDefaultState(), false);
                    this.func_175804_a(worldIn, p_74875_3_, 10, 3, i, 10, 4, i, Blocks.iron_bars.getDefaultState(), Blocks.iron_bars.getDefaultState(), false);
                }

                for (i = 2; i < 9; i += 2)
                {
                    this.func_175804_a(worldIn, p_74875_3_, i, 3, 15, i, 4, 15, Blocks.iron_bars.getDefaultState(), Blocks.iron_bars.getDefaultState(), false);
                }

                i = this.getMetadataWithOffset(Blocks.stone_brick_stairs, 3);
                this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 4, 1, 5, 6, 1, 7, false, p_74875_2_, StructureStrongholdPieces.strongholdStones);
                this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 4, 2, 6, 6, 2, 7, false, p_74875_2_, StructureStrongholdPieces.strongholdStones);
                this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 4, 3, 7, 6, 3, 7, false, p_74875_2_, StructureStrongholdPieces.strongholdStones);
                int j;

                for (j = 4; j <= 6; ++j)
                {
                    this.func_175811_a(worldIn, Blocks.stone_brick_stairs.getStateFromMeta(i), j, 1, 4, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stone_brick_stairs.getStateFromMeta(i), j, 2, 5, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stone_brick_stairs.getStateFromMeta(i), j, 3, 6, p_74875_3_);
                }

                j = EnumFacing.NORTH.getHorizontalIndex();
                int k = EnumFacing.SOUTH.getHorizontalIndex();
                int l = EnumFacing.EAST.getHorizontalIndex();
                int i1 = EnumFacing.WEST.getHorizontalIndex();

                if (this.coordBaseMode != null)
                {
                    switch (StructureStrongholdPieces.SwitchEnumFacing.field_175951_b[this.coordBaseMode.ordinal()])
                    {
                        case 2:
                            j = EnumFacing.SOUTH.getHorizontalIndex();
                            k = EnumFacing.NORTH.getHorizontalIndex();
                            break;
                        case 3:
                            j = EnumFacing.WEST.getHorizontalIndex();
                            k = EnumFacing.EAST.getHorizontalIndex();
                            l = EnumFacing.SOUTH.getHorizontalIndex();
                            i1 = EnumFacing.NORTH.getHorizontalIndex();
                            break;
                        case 4:
                            j = EnumFacing.EAST.getHorizontalIndex();
                            k = EnumFacing.WEST.getHorizontalIndex();
                            l = EnumFacing.SOUTH.getHorizontalIndex();
                            i1 = EnumFacing.NORTH.getHorizontalIndex();
                    }
                }

                this.func_175811_a(worldIn, Blocks.end_portal_frame.getStateFromMeta(j).withProperty(BlockEndPortalFrame.EYE, Boolean.valueOf(p_74875_2_.nextFloat() > 0.9F)), 4, 3, 8, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.end_portal_frame.getStateFromMeta(j).withProperty(BlockEndPortalFrame.EYE, Boolean.valueOf(p_74875_2_.nextFloat() > 0.9F)), 5, 3, 8, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.end_portal_frame.getStateFromMeta(j).withProperty(BlockEndPortalFrame.EYE, Boolean.valueOf(p_74875_2_.nextFloat() > 0.9F)), 6, 3, 8, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.end_portal_frame.getStateFromMeta(k).withProperty(BlockEndPortalFrame.EYE, Boolean.valueOf(p_74875_2_.nextFloat() > 0.9F)), 4, 3, 12, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.end_portal_frame.getStateFromMeta(k).withProperty(BlockEndPortalFrame.EYE, Boolean.valueOf(p_74875_2_.nextFloat() > 0.9F)), 5, 3, 12, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.end_portal_frame.getStateFromMeta(k).withProperty(BlockEndPortalFrame.EYE, Boolean.valueOf(p_74875_2_.nextFloat() > 0.9F)), 6, 3, 12, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.end_portal_frame.getStateFromMeta(l).withProperty(BlockEndPortalFrame.EYE, Boolean.valueOf(p_74875_2_.nextFloat() > 0.9F)), 3, 3, 9, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.end_portal_frame.getStateFromMeta(l).withProperty(BlockEndPortalFrame.EYE, Boolean.valueOf(p_74875_2_.nextFloat() > 0.9F)), 3, 3, 10, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.end_portal_frame.getStateFromMeta(l).withProperty(BlockEndPortalFrame.EYE, Boolean.valueOf(p_74875_2_.nextFloat() > 0.9F)), 3, 3, 11, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.end_portal_frame.getStateFromMeta(i1).withProperty(BlockEndPortalFrame.EYE, Boolean.valueOf(p_74875_2_.nextFloat() > 0.9F)), 7, 3, 9, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.end_portal_frame.getStateFromMeta(i1).withProperty(BlockEndPortalFrame.EYE, Boolean.valueOf(p_74875_2_.nextFloat() > 0.9F)), 7, 3, 10, p_74875_3_);
                this.func_175811_a(worldIn, Blocks.end_portal_frame.getStateFromMeta(i1).withProperty(BlockEndPortalFrame.EYE, Boolean.valueOf(p_74875_2_.nextFloat() > 0.9F)), 7, 3, 11, p_74875_3_);

                if (!this.hasSpawner)
                {
                    int j1 = this.getYWithOffset(3);
                    BlockPos blockpos = new BlockPos(this.getXWithOffset(5, 6), j1, this.getZWithOffset(5, 6));

                    if (p_74875_3_.func_175898_b(blockpos))
                    {
                        this.hasSpawner = true;
                        worldIn.setBlockState(blockpos, Blocks.mob_spawner.getDefaultState(), 2);
                        TileEntity tileentity = worldIn.getTileEntity(blockpos);

                        if (tileentity instanceof TileEntityMobSpawner)
                        {
                            ((TileEntityMobSpawner)tileentity).getSpawnerBaseLogic().setEntityName("Silverfish");
                        }
                    }
                }

                return true;
            }
        }

    public static class Prison extends StructureStrongholdPieces.Stronghold
        {
            private static final String __OBFID = "CL_00000494";

            public Prison() {}

            public Prison(int p_i45576_1_, Random p_i45576_2_, StructureBoundingBox p_i45576_3_, EnumFacing p_i45576_4_)
            {
                super(p_i45576_1_);
                this.coordBaseMode = p_i45576_4_;
                this.field_143013_d = this.getRandomDoor(p_i45576_2_);
                this.boundingBox = p_i45576_3_;
            }

            /**
             * Initiates construction of the Structure Component picked, at the current Location of StructGen
             */
            public void buildComponent(StructureComponent p_74861_1_, List p_74861_2_, Random p_74861_3_)
            {
                this.getNextComponentNormal((StructureStrongholdPieces.Stairs2)p_74861_1_, p_74861_2_, p_74861_3_, 1, 1);
            }

            public static StructureStrongholdPieces.Prison func_175860_a(List p_175860_0_, Random p_175860_1_, int p_175860_2_, int p_175860_3_, int p_175860_4_, EnumFacing p_175860_5_, int p_175860_6_)
            {
                StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175897_a(p_175860_2_, p_175860_3_, p_175860_4_, -1, -1, 0, 9, 5, 11, p_175860_5_);
                /**
                 * returns false if the Structure Bounding Box goes below 10
                 */
                return canStrongholdGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(p_175860_0_, structureboundingbox) == null ? new StructureStrongholdPieces.Prison(p_175860_6_, p_175860_1_, structureboundingbox, p_175860_5_) : null;
            }

            /**
             * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes
             * Mineshafts at the end, it adds Fences...
             */
            public boolean addComponentParts(World worldIn, Random p_74875_2_, StructureBoundingBox p_74875_3_)
            {
                if (this.isLiquidInStructureBoundingBox(worldIn, p_74875_3_))
                {
                    return false;
                }
                else
                {
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 0, 0, 0, 8, 4, 10, true, p_74875_2_, StructureStrongholdPieces.strongholdStones);
                    this.placeDoor(worldIn, p_74875_2_, p_74875_3_, this.field_143013_d, 1, 1, 0);
                    this.func_175804_a(worldIn, p_74875_3_, 1, 1, 10, 3, 3, 10, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 4, 1, 1, 4, 3, 1, false, p_74875_2_, StructureStrongholdPieces.strongholdStones);
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 4, 1, 3, 4, 3, 3, false, p_74875_2_, StructureStrongholdPieces.strongholdStones);
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 4, 1, 7, 4, 3, 7, false, p_74875_2_, StructureStrongholdPieces.strongholdStones);
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 4, 1, 9, 4, 3, 9, false, p_74875_2_, StructureStrongholdPieces.strongholdStones);
                    this.func_175804_a(worldIn, p_74875_3_, 4, 1, 4, 4, 3, 6, Blocks.iron_bars.getDefaultState(), Blocks.iron_bars.getDefaultState(), false);
                    this.func_175804_a(worldIn, p_74875_3_, 5, 1, 5, 7, 3, 5, Blocks.iron_bars.getDefaultState(), Blocks.iron_bars.getDefaultState(), false);
                    this.func_175811_a(worldIn, Blocks.iron_bars.getDefaultState(), 4, 3, 2, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.iron_bars.getDefaultState(), 4, 3, 8, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.iron_door.getStateFromMeta(this.getMetadataWithOffset(Blocks.iron_door, 3)), 4, 1, 2, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.iron_door.getStateFromMeta(this.getMetadataWithOffset(Blocks.iron_door, 3) + 8), 4, 2, 2, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.iron_door.getStateFromMeta(this.getMetadataWithOffset(Blocks.iron_door, 3)), 4, 1, 8, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.iron_door.getStateFromMeta(this.getMetadataWithOffset(Blocks.iron_door, 3) + 8), 4, 2, 8, p_74875_3_);
                    return true;
                }
            }
        }

    public static class RightTurn extends StructureStrongholdPieces.LeftTurn
        {
            private static final String __OBFID = "CL_00000495";

            /**
             * Initiates construction of the Structure Component picked, at the current Location of StructGen
             */
            public void buildComponent(StructureComponent p_74861_1_, List p_74861_2_, Random p_74861_3_)
            {
                if (this.coordBaseMode != EnumFacing.NORTH && this.coordBaseMode != EnumFacing.EAST)
                {
                    this.getNextComponentX((StructureStrongholdPieces.Stairs2)p_74861_1_, p_74861_2_, p_74861_3_, 1, 1);
                }
                else
                {
                    this.getNextComponentZ((StructureStrongholdPieces.Stairs2)p_74861_1_, p_74861_2_, p_74861_3_, 1, 1);
                }
            }

            /**
             * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes
             * Mineshafts at the end, it adds Fences...
             */
            public boolean addComponentParts(World worldIn, Random p_74875_2_, StructureBoundingBox p_74875_3_)
            {
                if (this.isLiquidInStructureBoundingBox(worldIn, p_74875_3_))
                {
                    return false;
                }
                else
                {
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 0, 0, 0, 4, 4, 4, true, p_74875_2_, StructureStrongholdPieces.strongholdStones);
                    this.placeDoor(worldIn, p_74875_2_, p_74875_3_, this.field_143013_d, 1, 1, 0);

                    if (this.coordBaseMode != EnumFacing.NORTH && this.coordBaseMode != EnumFacing.EAST)
                    {
                        this.func_175804_a(worldIn, p_74875_3_, 0, 1, 1, 0, 3, 3, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                    }
                    else
                    {
                        this.func_175804_a(worldIn, p_74875_3_, 4, 1, 1, 4, 3, 3, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                    }

                    return true;
                }
            }
        }

    public static class RoomCrossing extends StructureStrongholdPieces.Stronghold
        {
            /** Items that could generate in the chest that is located in Stronghold Room Crossing. */
            private static final List strongholdRoomCrossingChestContents = Lists.newArrayList(new WeightedRandomChestContent[] {new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10), new WeightedRandomChestContent(Items.gold_ingot, 0, 1, 3, 5), new WeightedRandomChestContent(Items.redstone, 0, 4, 9, 5), new WeightedRandomChestContent(Items.coal, 0, 3, 8, 10), new WeightedRandomChestContent(Items.bread, 0, 1, 3, 15), new WeightedRandomChestContent(Items.apple, 0, 1, 3, 15), new WeightedRandomChestContent(Items.iron_pickaxe, 0, 1, 1, 1)});
            protected int roomType;
            private static final String __OBFID = "CL_00000496";

            static
            {
                ChestGenHooks.init(STRONGHOLD_CROSSING, strongholdRoomCrossingChestContents, 1, 5);
                ChestGenHooks.addItem(STRONGHOLD_CROSSING, new WeightedRandomChestContent(new net.minecraft.item.ItemStack(Items.enchanted_book, 1, 0), 1, 1, 1));
            }

            public RoomCrossing() {}

            public RoomCrossing(int p_i45575_1_, Random p_i45575_2_, StructureBoundingBox p_i45575_3_, EnumFacing p_i45575_4_)
            {
                super(p_i45575_1_);
                this.coordBaseMode = p_i45575_4_;
                this.field_143013_d = this.getRandomDoor(p_i45575_2_);
                this.boundingBox = p_i45575_3_;
                this.roomType = p_i45575_2_.nextInt(5);
            }

            /**
             * (abstract) Helper method to write subclass data to NBT
             */
            protected void writeStructureToNBT(NBTTagCompound p_143012_1_)
            {
                super.writeStructureToNBT(p_143012_1_);
                p_143012_1_.setInteger("Type", this.roomType);
            }

            /**
             * (abstract) Helper method to read subclass data from NBT
             */
            protected void readStructureFromNBT(NBTTagCompound p_143011_1_)
            {
                super.readStructureFromNBT(p_143011_1_);
                this.roomType = p_143011_1_.getInteger("Type");
            }

            /**
             * Initiates construction of the Structure Component picked, at the current Location of StructGen
             */
            public void buildComponent(StructureComponent p_74861_1_, List p_74861_2_, Random p_74861_3_)
            {
                this.getNextComponentNormal((StructureStrongholdPieces.Stairs2)p_74861_1_, p_74861_2_, p_74861_3_, 4, 1);
                this.getNextComponentX((StructureStrongholdPieces.Stairs2)p_74861_1_, p_74861_2_, p_74861_3_, 1, 4);
                this.getNextComponentZ((StructureStrongholdPieces.Stairs2)p_74861_1_, p_74861_2_, p_74861_3_, 1, 4);
            }

            public static StructureStrongholdPieces.RoomCrossing func_175859_a(List p_175859_0_, Random p_175859_1_, int p_175859_2_, int p_175859_3_, int p_175859_4_, EnumFacing p_175859_5_, int p_175859_6_)
            {
                StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175897_a(p_175859_2_, p_175859_3_, p_175859_4_, -4, -1, 0, 11, 7, 11, p_175859_5_);
                /**
                 * returns false if the Structure Bounding Box goes below 10
                 */
                return canStrongholdGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(p_175859_0_, structureboundingbox) == null ? new StructureStrongholdPieces.RoomCrossing(p_175859_6_, p_175859_1_, structureboundingbox, p_175859_5_) : null;
            }

            /**
             * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes
             * Mineshafts at the end, it adds Fences...
             */
            public boolean addComponentParts(World worldIn, Random p_74875_2_, StructureBoundingBox p_74875_3_)
            {
                if (this.isLiquidInStructureBoundingBox(worldIn, p_74875_3_))
                {
                    return false;
                }
                else
                {
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 0, 0, 0, 10, 6, 10, true, p_74875_2_, StructureStrongholdPieces.strongholdStones);
                    this.placeDoor(worldIn, p_74875_2_, p_74875_3_, this.field_143013_d, 4, 1, 0);
                    this.func_175804_a(worldIn, p_74875_3_, 4, 1, 10, 6, 3, 10, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                    this.func_175804_a(worldIn, p_74875_3_, 0, 1, 4, 0, 3, 6, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                    this.func_175804_a(worldIn, p_74875_3_, 10, 1, 4, 10, 3, 6, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                    int i;

                    switch (this.roomType)
                    {
                        case 0:
                            this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), 5, 1, 5, p_74875_3_);
                            this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), 5, 2, 5, p_74875_3_);
                            this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), 5, 3, 5, p_74875_3_);
                            this.func_175811_a(worldIn, Blocks.torch.getDefaultState(), 4, 3, 5, p_74875_3_);
                            this.func_175811_a(worldIn, Blocks.torch.getDefaultState(), 6, 3, 5, p_74875_3_);
                            this.func_175811_a(worldIn, Blocks.torch.getDefaultState(), 5, 3, 4, p_74875_3_);
                            this.func_175811_a(worldIn, Blocks.torch.getDefaultState(), 5, 3, 6, p_74875_3_);
                            this.func_175811_a(worldIn, Blocks.stone_slab.getDefaultState(), 4, 1, 4, p_74875_3_);
                            this.func_175811_a(worldIn, Blocks.stone_slab.getDefaultState(), 4, 1, 5, p_74875_3_);
                            this.func_175811_a(worldIn, Blocks.stone_slab.getDefaultState(), 4, 1, 6, p_74875_3_);
                            this.func_175811_a(worldIn, Blocks.stone_slab.getDefaultState(), 6, 1, 4, p_74875_3_);
                            this.func_175811_a(worldIn, Blocks.stone_slab.getDefaultState(), 6, 1, 5, p_74875_3_);
                            this.func_175811_a(worldIn, Blocks.stone_slab.getDefaultState(), 6, 1, 6, p_74875_3_);
                            this.func_175811_a(worldIn, Blocks.stone_slab.getDefaultState(), 5, 1, 4, p_74875_3_);
                            this.func_175811_a(worldIn, Blocks.stone_slab.getDefaultState(), 5, 1, 6, p_74875_3_);
                            break;
                        case 1:
                            for (i = 0; i < 5; ++i)
                            {
                                this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), 3, 1, 3 + i, p_74875_3_);
                                this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), 7, 1, 3 + i, p_74875_3_);
                                this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), 3 + i, 1, 3, p_74875_3_);
                                this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), 3 + i, 1, 7, p_74875_3_);
                            }

                            this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), 5, 1, 5, p_74875_3_);
                            this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), 5, 2, 5, p_74875_3_);
                            this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), 5, 3, 5, p_74875_3_);
                            this.func_175811_a(worldIn, Blocks.flowing_water.getDefaultState(), 5, 4, 5, p_74875_3_);
                            break;
                        case 2:
                            for (i = 1; i <= 9; ++i)
                            {
                                this.func_175811_a(worldIn, Blocks.cobblestone.getDefaultState(), 1, 3, i, p_74875_3_);
                                this.func_175811_a(worldIn, Blocks.cobblestone.getDefaultState(), 9, 3, i, p_74875_3_);
                            }

                            for (i = 1; i <= 9; ++i)
                            {
                                this.func_175811_a(worldIn, Blocks.cobblestone.getDefaultState(), i, 3, 1, p_74875_3_);
                                this.func_175811_a(worldIn, Blocks.cobblestone.getDefaultState(), i, 3, 9, p_74875_3_);
                            }

                            this.func_175811_a(worldIn, Blocks.cobblestone.getDefaultState(), 5, 1, 4, p_74875_3_);
                            this.func_175811_a(worldIn, Blocks.cobblestone.getDefaultState(), 5, 1, 6, p_74875_3_);
                            this.func_175811_a(worldIn, Blocks.cobblestone.getDefaultState(), 5, 3, 4, p_74875_3_);
                            this.func_175811_a(worldIn, Blocks.cobblestone.getDefaultState(), 5, 3, 6, p_74875_3_);
                            this.func_175811_a(worldIn, Blocks.cobblestone.getDefaultState(), 4, 1, 5, p_74875_3_);
                            this.func_175811_a(worldIn, Blocks.cobblestone.getDefaultState(), 6, 1, 5, p_74875_3_);
                            this.func_175811_a(worldIn, Blocks.cobblestone.getDefaultState(), 4, 3, 5, p_74875_3_);
                            this.func_175811_a(worldIn, Blocks.cobblestone.getDefaultState(), 6, 3, 5, p_74875_3_);

                            for (i = 1; i <= 3; ++i)
                            {
                                this.func_175811_a(worldIn, Blocks.cobblestone.getDefaultState(), 4, i, 4, p_74875_3_);
                                this.func_175811_a(worldIn, Blocks.cobblestone.getDefaultState(), 6, i, 4, p_74875_3_);
                                this.func_175811_a(worldIn, Blocks.cobblestone.getDefaultState(), 4, i, 6, p_74875_3_);
                                this.func_175811_a(worldIn, Blocks.cobblestone.getDefaultState(), 6, i, 6, p_74875_3_);
                            }

                            this.func_175811_a(worldIn, Blocks.torch.getDefaultState(), 5, 3, 5, p_74875_3_);

                            for (i = 2; i <= 8; ++i)
                            {
                                this.func_175811_a(worldIn, Blocks.planks.getDefaultState(), 2, 3, i, p_74875_3_);
                                this.func_175811_a(worldIn, Blocks.planks.getDefaultState(), 3, 3, i, p_74875_3_);

                                if (i <= 3 || i >= 7)
                                {
                                    this.func_175811_a(worldIn, Blocks.planks.getDefaultState(), 4, 3, i, p_74875_3_);
                                    this.func_175811_a(worldIn, Blocks.planks.getDefaultState(), 5, 3, i, p_74875_3_);
                                    this.func_175811_a(worldIn, Blocks.planks.getDefaultState(), 6, 3, i, p_74875_3_);
                                }

                                this.func_175811_a(worldIn, Blocks.planks.getDefaultState(), 7, 3, i, p_74875_3_);
                                this.func_175811_a(worldIn, Blocks.planks.getDefaultState(), 8, 3, i, p_74875_3_);
                            }

                            this.func_175811_a(worldIn, Blocks.ladder.getStateFromMeta(this.getMetadataWithOffset(Blocks.ladder, EnumFacing.WEST.getIndex())), 9, 1, 3, p_74875_3_);
                            this.func_175811_a(worldIn, Blocks.ladder.getStateFromMeta(this.getMetadataWithOffset(Blocks.ladder, EnumFacing.WEST.getIndex())), 9, 2, 3, p_74875_3_);
                            this.func_175811_a(worldIn, Blocks.ladder.getStateFromMeta(this.getMetadataWithOffset(Blocks.ladder, EnumFacing.WEST.getIndex())), 9, 3, 3, p_74875_3_);
                            this.func_180778_a(worldIn, p_74875_3_, p_74875_2_, 3, 4, 8, ChestGenHooks.getItems(STRONGHOLD_CROSSING, p_74875_2_), ChestGenHooks.getCount(STRONGHOLD_CROSSING, p_74875_2_));
                    }

                    return true;
                }
            }
        }

    public static class Stairs extends StructureStrongholdPieces.Stronghold
        {
            private boolean field_75024_a;
            private static final String __OBFID = "CL_00000498";

            public Stairs() {}

            public Stairs(int p_i2081_1_, Random p_i2081_2_, int p_i2081_3_, int p_i2081_4_)
            {
                super(p_i2081_1_);
                this.field_75024_a = true;
                this.coordBaseMode = EnumFacing.Plane.HORIZONTAL.random(p_i2081_2_);
                this.field_143013_d = StructureStrongholdPieces.Stronghold.Door.OPENING;

                switch (StructureStrongholdPieces.SwitchEnumFacing.field_175951_b[this.coordBaseMode.ordinal()])
                {
                    case 1:
                    case 2:
                        this.boundingBox = new StructureBoundingBox(p_i2081_3_, 64, p_i2081_4_, p_i2081_3_ + 5 - 1, 74, p_i2081_4_ + 5 - 1);
                        break;
                    default:
                        this.boundingBox = new StructureBoundingBox(p_i2081_3_, 64, p_i2081_4_, p_i2081_3_ + 5 - 1, 74, p_i2081_4_ + 5 - 1);
                }
            }

            public Stairs(int p_i45574_1_, Random p_i45574_2_, StructureBoundingBox p_i45574_3_, EnumFacing p_i45574_4_)
            {
                super(p_i45574_1_);
                this.field_75024_a = false;
                this.coordBaseMode = p_i45574_4_;
                this.field_143013_d = this.getRandomDoor(p_i45574_2_);
                this.boundingBox = p_i45574_3_;
            }

            /**
             * (abstract) Helper method to write subclass data to NBT
             */
            protected void writeStructureToNBT(NBTTagCompound p_143012_1_)
            {
                super.writeStructureToNBT(p_143012_1_);
                p_143012_1_.setBoolean("Source", this.field_75024_a);
            }

            /**
             * (abstract) Helper method to read subclass data from NBT
             */
            protected void readStructureFromNBT(NBTTagCompound p_143011_1_)
            {
                super.readStructureFromNBT(p_143011_1_);
                this.field_75024_a = p_143011_1_.getBoolean("Source");
            }

            /**
             * Initiates construction of the Structure Component picked, at the current Location of StructGen
             */
            public void buildComponent(StructureComponent p_74861_1_, List p_74861_2_, Random p_74861_3_)
            {
                if (this.field_75024_a)
                {
                    StructureStrongholdPieces.strongComponentType = StructureStrongholdPieces.Crossing.class;
                }

                this.getNextComponentNormal((StructureStrongholdPieces.Stairs2)p_74861_1_, p_74861_2_, p_74861_3_, 1, 1);
            }

            public static StructureStrongholdPieces.Stairs func_175863_a(List p_175863_0_, Random p_175863_1_, int p_175863_2_, int p_175863_3_, int p_175863_4_, EnumFacing p_175863_5_, int p_175863_6_)
            {
                StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175897_a(p_175863_2_, p_175863_3_, p_175863_4_, -1, -7, 0, 5, 11, 5, p_175863_5_);
                /**
                 * returns false if the Structure Bounding Box goes below 10
                 */
                return canStrongholdGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(p_175863_0_, structureboundingbox) == null ? new StructureStrongholdPieces.Stairs(p_175863_6_, p_175863_1_, structureboundingbox, p_175863_5_) : null;
            }

            /**
             * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes
             * Mineshafts at the end, it adds Fences...
             */
            public boolean addComponentParts(World worldIn, Random p_74875_2_, StructureBoundingBox p_74875_3_)
            {
                if (this.isLiquidInStructureBoundingBox(worldIn, p_74875_3_))
                {
                    return false;
                }
                else
                {
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 0, 0, 0, 4, 10, 4, true, p_74875_2_, StructureStrongholdPieces.strongholdStones);
                    this.placeDoor(worldIn, p_74875_2_, p_74875_3_, this.field_143013_d, 1, 7, 0);
                    this.placeDoor(worldIn, p_74875_2_, p_74875_3_, StructureStrongholdPieces.Stronghold.Door.OPENING, 1, 1, 4);
                    this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), 2, 6, 1, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), 1, 5, 1, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 1, 6, 1, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), 1, 5, 2, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), 1, 4, 3, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 1, 5, 3, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), 2, 4, 3, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), 3, 3, 3, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 3, 4, 3, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), 3, 3, 2, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), 3, 2, 1, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 3, 3, 1, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), 2, 2, 1, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), 1, 1, 1, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 1, 2, 1, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), 1, 1, 2, p_74875_3_);
                    this.func_175811_a(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 1, 1, 3, p_74875_3_);
                    return true;
                }
            }
        }

    public static class Stairs2 extends StructureStrongholdPieces.Stairs
        {
            public StructureStrongholdPieces.PieceWeight strongholdPieceWeight;
            public StructureStrongholdPieces.PortalRoom strongholdPortalRoom;
            public List field_75026_c = Lists.newArrayList();
            private static final String __OBFID = "CL_00000499";

            public Stairs2() {}

            public Stairs2(int p_i2083_1_, Random p_i2083_2_, int p_i2083_3_, int p_i2083_4_)
            {
                super(0, p_i2083_2_, p_i2083_3_, p_i2083_4_);
            }

            public BlockPos func_180776_a()
            {
                return this.strongholdPortalRoom != null ? this.strongholdPortalRoom.func_180776_a() : super.func_180776_a();
            }
        }

    public static class StairsStraight extends StructureStrongholdPieces.Stronghold
        {
            private static final String __OBFID = "CL_00000501";

            public StairsStraight() {}

            public StairsStraight(int p_i45572_1_, Random p_i45572_2_, StructureBoundingBox p_i45572_3_, EnumFacing p_i45572_4_)
            {
                super(p_i45572_1_);
                this.coordBaseMode = p_i45572_4_;
                this.field_143013_d = this.getRandomDoor(p_i45572_2_);
                this.boundingBox = p_i45572_3_;
            }

            /**
             * Initiates construction of the Structure Component picked, at the current Location of StructGen
             */
            public void buildComponent(StructureComponent p_74861_1_, List p_74861_2_, Random p_74861_3_)
            {
                this.getNextComponentNormal((StructureStrongholdPieces.Stairs2)p_74861_1_, p_74861_2_, p_74861_3_, 1, 1);
            }

            public static StructureStrongholdPieces.StairsStraight func_175861_a(List p_175861_0_, Random p_175861_1_, int p_175861_2_, int p_175861_3_, int p_175861_4_, EnumFacing p_175861_5_, int p_175861_6_)
            {
                StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175897_a(p_175861_2_, p_175861_3_, p_175861_4_, -1, -7, 0, 5, 11, 8, p_175861_5_);
                /**
                 * returns false if the Structure Bounding Box goes below 10
                 */
                return canStrongholdGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(p_175861_0_, structureboundingbox) == null ? new StructureStrongholdPieces.StairsStraight(p_175861_6_, p_175861_1_, structureboundingbox, p_175861_5_) : null;
            }

            /**
             * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes
             * Mineshafts at the end, it adds Fences...
             */
            public boolean addComponentParts(World worldIn, Random p_74875_2_, StructureBoundingBox p_74875_3_)
            {
                if (this.isLiquidInStructureBoundingBox(worldIn, p_74875_3_))
                {
                    return false;
                }
                else
                {
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 0, 0, 0, 4, 10, 7, true, p_74875_2_, StructureStrongholdPieces.strongholdStones);
                    this.placeDoor(worldIn, p_74875_2_, p_74875_3_, this.field_143013_d, 1, 7, 0);
                    this.placeDoor(worldIn, p_74875_2_, p_74875_3_, StructureStrongholdPieces.Stronghold.Door.OPENING, 1, 1, 7);
                    int i = this.getMetadataWithOffset(Blocks.stone_stairs, 2);

                    for (int j = 0; j < 6; ++j)
                    {
                        this.func_175811_a(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 1, 6 - j, 1 + j, p_74875_3_);
                        this.func_175811_a(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 2, 6 - j, 1 + j, p_74875_3_);
                        this.func_175811_a(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 3, 6 - j, 1 + j, p_74875_3_);

                        if (j < 5)
                        {
                            this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), 1, 5 - j, 1 + j, p_74875_3_);
                            this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), 2, 5 - j, 1 + j, p_74875_3_);
                            this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), 3, 5 - j, 1 + j, p_74875_3_);
                        }
                    }

                    return true;
                }
            }
        }

    static class Stones extends StructureComponent.BlockSelector
        {
            private static final String __OBFID = "CL_00000497";

            private Stones() {}

            /**
             * picks Block Ids and Metadata (Silverfish)
             */
            public void selectBlocks(Random p_75062_1_, int p_75062_2_, int p_75062_3_, int p_75062_4_, boolean p_75062_5_)
            {
                if (p_75062_5_)
                {
                    float f = p_75062_1_.nextFloat();

                    if (f < 0.2F)
                    {
                        this.field_151562_a = Blocks.stonebrick.getStateFromMeta(BlockStoneBrick.CRACKED_META);
                    }
                    else if (f < 0.5F)
                    {
                        this.field_151562_a = Blocks.stonebrick.getStateFromMeta(BlockStoneBrick.MOSSY_META);
                    }
                    else if (f < 0.55F)
                    {
                        this.field_151562_a = Blocks.monster_egg.getStateFromMeta(BlockSilverfish.EnumType.STONEBRICK.getMetadata());
                    }
                    else
                    {
                        this.field_151562_a = Blocks.stonebrick.getDefaultState();
                    }
                }
                else
                {
                    this.field_151562_a = Blocks.air.getDefaultState();
                }
            }

            Stones(Object p_i2080_1_)
            {
                this();
            }
        }

    public static class Straight extends StructureStrongholdPieces.Stronghold
        {
            private boolean expandsX;
            private boolean expandsZ;
            private static final String __OBFID = "CL_00000500";

            public Straight() {}

            public Straight(int p_i45573_1_, Random p_i45573_2_, StructureBoundingBox p_i45573_3_, EnumFacing p_i45573_4_)
            {
                super(p_i45573_1_);
                this.coordBaseMode = p_i45573_4_;
                this.field_143013_d = this.getRandomDoor(p_i45573_2_);
                this.boundingBox = p_i45573_3_;
                this.expandsX = p_i45573_2_.nextInt(2) == 0;
                this.expandsZ = p_i45573_2_.nextInt(2) == 0;
            }

            /**
             * (abstract) Helper method to write subclass data to NBT
             */
            protected void writeStructureToNBT(NBTTagCompound p_143012_1_)
            {
                super.writeStructureToNBT(p_143012_1_);
                p_143012_1_.setBoolean("Left", this.expandsX);
                p_143012_1_.setBoolean("Right", this.expandsZ);
            }

            /**
             * (abstract) Helper method to read subclass data from NBT
             */
            protected void readStructureFromNBT(NBTTagCompound p_143011_1_)
            {
                super.readStructureFromNBT(p_143011_1_);
                this.expandsX = p_143011_1_.getBoolean("Left");
                this.expandsZ = p_143011_1_.getBoolean("Right");
            }

            /**
             * Initiates construction of the Structure Component picked, at the current Location of StructGen
             */
            public void buildComponent(StructureComponent p_74861_1_, List p_74861_2_, Random p_74861_3_)
            {
                this.getNextComponentNormal((StructureStrongholdPieces.Stairs2)p_74861_1_, p_74861_2_, p_74861_3_, 1, 1);

                if (this.expandsX)
                {
                    this.getNextComponentX((StructureStrongholdPieces.Stairs2)p_74861_1_, p_74861_2_, p_74861_3_, 1, 2);
                }

                if (this.expandsZ)
                {
                    this.getNextComponentZ((StructureStrongholdPieces.Stairs2)p_74861_1_, p_74861_2_, p_74861_3_, 1, 2);
                }
            }

            public static StructureStrongholdPieces.Straight func_175862_a(List p_175862_0_, Random p_175862_1_, int p_175862_2_, int p_175862_3_, int p_175862_4_, EnumFacing p_175862_5_, int p_175862_6_)
            {
                StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175897_a(p_175862_2_, p_175862_3_, p_175862_4_, -1, -1, 0, 5, 5, 7, p_175862_5_);
                /**
                 * returns false if the Structure Bounding Box goes below 10
                 */
                return canStrongholdGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(p_175862_0_, structureboundingbox) == null ? new StructureStrongholdPieces.Straight(p_175862_6_, p_175862_1_, structureboundingbox, p_175862_5_) : null;
            }

            /**
             * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes
             * Mineshafts at the end, it adds Fences...
             */
            public boolean addComponentParts(World worldIn, Random p_74875_2_, StructureBoundingBox p_74875_3_)
            {
                if (this.isLiquidInStructureBoundingBox(worldIn, p_74875_3_))
                {
                    return false;
                }
                else
                {
                    this.fillWithRandomizedBlocks(worldIn, p_74875_3_, 0, 0, 0, 4, 4, 6, true, p_74875_2_, StructureStrongholdPieces.strongholdStones);
                    this.placeDoor(worldIn, p_74875_2_, p_74875_3_, this.field_143013_d, 1, 1, 0);
                    this.placeDoor(worldIn, p_74875_2_, p_74875_3_, StructureStrongholdPieces.Stronghold.Door.OPENING, 1, 1, 6);
                    this.func_175809_a(worldIn, p_74875_3_, p_74875_2_, 0.1F, 1, 2, 1, Blocks.torch.getDefaultState());
                    this.func_175809_a(worldIn, p_74875_3_, p_74875_2_, 0.1F, 3, 2, 1, Blocks.torch.getDefaultState());
                    this.func_175809_a(worldIn, p_74875_3_, p_74875_2_, 0.1F, 1, 2, 5, Blocks.torch.getDefaultState());
                    this.func_175809_a(worldIn, p_74875_3_, p_74875_2_, 0.1F, 3, 2, 5, Blocks.torch.getDefaultState());

                    if (this.expandsX)
                    {
                        this.func_175804_a(worldIn, p_74875_3_, 0, 1, 2, 0, 3, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                    }

                    if (this.expandsZ)
                    {
                        this.func_175804_a(worldIn, p_74875_3_, 4, 1, 2, 4, 3, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                    }

                    return true;
                }
            }
        }

    public abstract static class Stronghold extends StructureComponent
        {
            protected StructureStrongholdPieces.Stronghold.Door field_143013_d;
            private static final String __OBFID = "CL_00000503";

            public Stronghold()
            {
                this.field_143013_d = StructureStrongholdPieces.Stronghold.Door.OPENING;
            }

            protected Stronghold(int p_i2087_1_)
            {
                super(p_i2087_1_);
                this.field_143013_d = StructureStrongholdPieces.Stronghold.Door.OPENING;
            }

            /**
             * (abstract) Helper method to write subclass data to NBT
             */
            protected void writeStructureToNBT(NBTTagCompound p_143012_1_)
            {
                p_143012_1_.setString("EntryDoor", this.field_143013_d.name());
            }

            /**
             * (abstract) Helper method to read subclass data from NBT
             */
            protected void readStructureFromNBT(NBTTagCompound p_143011_1_)
            {
                this.field_143013_d = StructureStrongholdPieces.Stronghold.Door.valueOf(p_143011_1_.getString("EntryDoor"));
            }

            /**
             * builds a door of the enumerated types (empty opening is a door)
             */
            protected void placeDoor(World worldIn, Random p_74990_2_, StructureBoundingBox p_74990_3_, StructureStrongholdPieces.Stronghold.Door p_74990_4_, int p_74990_5_, int p_74990_6_, int p_74990_7_)
            {
                switch (StructureStrongholdPieces.SwitchEnumFacing.doorEnum[p_74990_4_.ordinal()])
                {
                    case 1:
                    default:
                        this.func_175804_a(worldIn, p_74990_3_, p_74990_5_, p_74990_6_, p_74990_7_, p_74990_5_ + 3 - 1, p_74990_6_ + 3 - 1, p_74990_7_, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                        break;
                    case 2:
                        this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_, p_74990_6_, p_74990_7_, p_74990_3_);
                        this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_, p_74990_6_ + 1, p_74990_7_, p_74990_3_);
                        this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_, p_74990_6_ + 2, p_74990_7_, p_74990_3_);
                        this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_ + 1, p_74990_6_ + 2, p_74990_7_, p_74990_3_);
                        this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_ + 2, p_74990_6_ + 2, p_74990_7_, p_74990_3_);
                        this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_ + 2, p_74990_6_ + 1, p_74990_7_, p_74990_3_);
                        this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_ + 2, p_74990_6_, p_74990_7_, p_74990_3_);
                        this.func_175811_a(worldIn, Blocks.oak_door.getDefaultState(), p_74990_5_ + 1, p_74990_6_, p_74990_7_, p_74990_3_);
                        this.func_175811_a(worldIn, Blocks.oak_door.getStateFromMeta(8), p_74990_5_ + 1, p_74990_6_ + 1, p_74990_7_, p_74990_3_);
                        break;
                    case 3:
                        this.func_175811_a(worldIn, Blocks.air.getDefaultState(), p_74990_5_ + 1, p_74990_6_, p_74990_7_, p_74990_3_);
                        this.func_175811_a(worldIn, Blocks.air.getDefaultState(), p_74990_5_ + 1, p_74990_6_ + 1, p_74990_7_, p_74990_3_);
                        this.func_175811_a(worldIn, Blocks.iron_bars.getDefaultState(), p_74990_5_, p_74990_6_, p_74990_7_, p_74990_3_);
                        this.func_175811_a(worldIn, Blocks.iron_bars.getDefaultState(), p_74990_5_, p_74990_6_ + 1, p_74990_7_, p_74990_3_);
                        this.func_175811_a(worldIn, Blocks.iron_bars.getDefaultState(), p_74990_5_, p_74990_6_ + 2, p_74990_7_, p_74990_3_);
                        this.func_175811_a(worldIn, Blocks.iron_bars.getDefaultState(), p_74990_5_ + 1, p_74990_6_ + 2, p_74990_7_, p_74990_3_);
                        this.func_175811_a(worldIn, Blocks.iron_bars.getDefaultState(), p_74990_5_ + 2, p_74990_6_ + 2, p_74990_7_, p_74990_3_);
                        this.func_175811_a(worldIn, Blocks.iron_bars.getDefaultState(), p_74990_5_ + 2, p_74990_6_ + 1, p_74990_7_, p_74990_3_);
                        this.func_175811_a(worldIn, Blocks.iron_bars.getDefaultState(), p_74990_5_ + 2, p_74990_6_, p_74990_7_, p_74990_3_);
                        break;
                    case 4:
                        this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_, p_74990_6_, p_74990_7_, p_74990_3_);
                        this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_, p_74990_6_ + 1, p_74990_7_, p_74990_3_);
                        this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_, p_74990_6_ + 2, p_74990_7_, p_74990_3_);
                        this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_ + 1, p_74990_6_ + 2, p_74990_7_, p_74990_3_);
                        this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_ + 2, p_74990_6_ + 2, p_74990_7_, p_74990_3_);
                        this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_ + 2, p_74990_6_ + 1, p_74990_7_, p_74990_3_);
                        this.func_175811_a(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_ + 2, p_74990_6_, p_74990_7_, p_74990_3_);
                        this.func_175811_a(worldIn, Blocks.iron_door.getDefaultState(), p_74990_5_ + 1, p_74990_6_, p_74990_7_, p_74990_3_);
                        this.func_175811_a(worldIn, Blocks.iron_door.getStateFromMeta(8), p_74990_5_ + 1, p_74990_6_ + 1, p_74990_7_, p_74990_3_);
                        this.func_175811_a(worldIn, Blocks.stone_button.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_button, 4)), p_74990_5_ + 2, p_74990_6_ + 1, p_74990_7_ + 1, p_74990_3_);
                        this.func_175811_a(worldIn, Blocks.stone_button.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_button, 3)), p_74990_5_ + 2, p_74990_6_ + 1, p_74990_7_ - 1, p_74990_3_);
                }
            }

            protected StructureStrongholdPieces.Stronghold.Door getRandomDoor(Random p_74988_1_)
            {
                int i = p_74988_1_.nextInt(5);

                switch (i)
                {
                    case 0:
                    case 1:
                    default:
                        return StructureStrongholdPieces.Stronghold.Door.OPENING;
                    case 2:
                        return StructureStrongholdPieces.Stronghold.Door.WOOD_DOOR;
                    case 3:
                        return StructureStrongholdPieces.Stronghold.Door.GRATES;
                    case 4:
                        return StructureStrongholdPieces.Stronghold.Door.IRON_DOOR;
                }
            }

            /**
             * Gets the next component in any cardinal direction
             */
            protected StructureComponent getNextComponentNormal(StructureStrongholdPieces.Stairs2 p_74986_1_, List p_74986_2_, Random p_74986_3_, int p_74986_4_, int p_74986_5_)
            {
                if (this.coordBaseMode != null)
                {
                    switch (StructureStrongholdPieces.SwitchEnumFacing.field_175951_b[this.coordBaseMode.ordinal()])
                    {
                        case 1:
                            return StructureStrongholdPieces.func_175953_c(p_74986_1_, p_74986_2_, p_74986_3_, this.boundingBox.minX + p_74986_4_, this.boundingBox.minY + p_74986_5_, this.boundingBox.minZ - 1, this.coordBaseMode, this.getComponentType());
                        case 2:
                            return StructureStrongholdPieces.func_175953_c(p_74986_1_, p_74986_2_, p_74986_3_, this.boundingBox.minX + p_74986_4_, this.boundingBox.minY + p_74986_5_, this.boundingBox.maxZ + 1, this.coordBaseMode, this.getComponentType());
                        case 3:
                            return StructureStrongholdPieces.func_175953_c(p_74986_1_, p_74986_2_, p_74986_3_, this.boundingBox.minX - 1, this.boundingBox.minY + p_74986_5_, this.boundingBox.minZ + p_74986_4_, this.coordBaseMode, this.getComponentType());
                        case 4:
                            return StructureStrongholdPieces.func_175953_c(p_74986_1_, p_74986_2_, p_74986_3_, this.boundingBox.maxX + 1, this.boundingBox.minY + p_74986_5_, this.boundingBox.minZ + p_74986_4_, this.coordBaseMode, this.getComponentType());
                    }
                }

                return null;
            }

            /**
             * Gets the next component in the +/- X direction
             */
            protected StructureComponent getNextComponentX(StructureStrongholdPieces.Stairs2 p_74989_1_, List p_74989_2_, Random p_74989_3_, int p_74989_4_, int p_74989_5_)
            {
                if (this.coordBaseMode != null)
                {
                    switch (StructureStrongholdPieces.SwitchEnumFacing.field_175951_b[this.coordBaseMode.ordinal()])
                    {
                        case 1:
                            return StructureStrongholdPieces.func_175953_c(p_74989_1_, p_74989_2_, p_74989_3_, this.boundingBox.minX - 1, this.boundingBox.minY + p_74989_4_, this.boundingBox.minZ + p_74989_5_, EnumFacing.WEST, this.getComponentType());
                        case 2:
                            return StructureStrongholdPieces.func_175953_c(p_74989_1_, p_74989_2_, p_74989_3_, this.boundingBox.minX - 1, this.boundingBox.minY + p_74989_4_, this.boundingBox.minZ + p_74989_5_, EnumFacing.WEST, this.getComponentType());
                        case 3:
                            return StructureStrongholdPieces.func_175953_c(p_74989_1_, p_74989_2_, p_74989_3_, this.boundingBox.minX + p_74989_5_, this.boundingBox.minY + p_74989_4_, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
                        case 4:
                            return StructureStrongholdPieces.func_175953_c(p_74989_1_, p_74989_2_, p_74989_3_, this.boundingBox.minX + p_74989_5_, this.boundingBox.minY + p_74989_4_, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
                    }
                }

                return null;
            }

            /**
             * Gets the next component in the +/- Z direction
             */
            protected StructureComponent getNextComponentZ(StructureStrongholdPieces.Stairs2 p_74987_1_, List p_74987_2_, Random p_74987_3_, int p_74987_4_, int p_74987_5_)
            {
                if (this.coordBaseMode != null)
                {
                    switch (StructureStrongholdPieces.SwitchEnumFacing.field_175951_b[this.coordBaseMode.ordinal()])
                    {
                        case 1:
                            return StructureStrongholdPieces.func_175953_c(p_74987_1_, p_74987_2_, p_74987_3_, this.boundingBox.maxX + 1, this.boundingBox.minY + p_74987_4_, this.boundingBox.minZ + p_74987_5_, EnumFacing.EAST, this.getComponentType());
                        case 2:
                            return StructureStrongholdPieces.func_175953_c(p_74987_1_, p_74987_2_, p_74987_3_, this.boundingBox.maxX + 1, this.boundingBox.minY + p_74987_4_, this.boundingBox.minZ + p_74987_5_, EnumFacing.EAST, this.getComponentType());
                        case 3:
                            return StructureStrongholdPieces.func_175953_c(p_74987_1_, p_74987_2_, p_74987_3_, this.boundingBox.minX + p_74987_5_, this.boundingBox.minY + p_74987_4_, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
                        case 4:
                            return StructureStrongholdPieces.func_175953_c(p_74987_1_, p_74987_2_, p_74987_3_, this.boundingBox.minX + p_74987_5_, this.boundingBox.minY + p_74987_4_, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
                    }
                }

                return null;
            }

            /**
             * returns false if the Structure Bounding Box goes below 10
             */
            protected static boolean canStrongholdGoDeeper(StructureBoundingBox p_74991_0_)
            {
                return p_74991_0_ != null && p_74991_0_.minY > 10;
            }

            public static enum Door
            {
                OPENING,
                WOOD_DOOR,
                GRATES,
                IRON_DOOR;

                private static final String __OBFID = "CL_00000504";
            }
        }

    static final class SwitchEnumFacing
        {
            static final int[] doorEnum;

            static final int[] field_175951_b = new int[EnumFacing.values().length];
            private static final String __OBFID = "CL_00001970";

            static
            {
                try
                {
                    field_175951_b[EnumFacing.NORTH.ordinal()] = 1;
                }
                catch (NoSuchFieldError var8)
                {
                    ;
                }

                try
                {
                    field_175951_b[EnumFacing.SOUTH.ordinal()] = 2;
                }
                catch (NoSuchFieldError var7)
                {
                    ;
                }

                try
                {
                    field_175951_b[EnumFacing.WEST.ordinal()] = 3;
                }
                catch (NoSuchFieldError var6)
                {
                    ;
                }

                try
                {
                    field_175951_b[EnumFacing.EAST.ordinal()] = 4;
                }
                catch (NoSuchFieldError var5)
                {
                    ;
                }

                doorEnum = new int[StructureStrongholdPieces.Stronghold.Door.values().length];

                try
                {
                    doorEnum[StructureStrongholdPieces.Stronghold.Door.OPENING.ordinal()] = 1;
                }
                catch (NoSuchFieldError var4)
                {
                    ;
                }

                try
                {
                    doorEnum[StructureStrongholdPieces.Stronghold.Door.WOOD_DOOR.ordinal()] = 2;
                }
                catch (NoSuchFieldError var3)
                {
                    ;
                }

                try
                {
                    doorEnum[StructureStrongholdPieces.Stronghold.Door.GRATES.ordinal()] = 3;
                }
                catch (NoSuchFieldError var2)
                {
                    ;
                }

                try
                {
                    doorEnum[StructureStrongholdPieces.Stronghold.Door.IRON_DOOR.ordinal()] = 4;
                }
                catch (NoSuchFieldError var1)
                {
                    ;
                }
            }
        }
}