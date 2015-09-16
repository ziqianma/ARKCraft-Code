package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraftforge.common.ChestGenHooks;
import static net.minecraftforge.common.ChestGenHooks.*;

public class StructureMineshaftPieces
{
    private static final List field_175893_a = Lists.newArrayList(new WeightedRandomChestContent[] {new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10), new WeightedRandomChestContent(Items.gold_ingot, 0, 1, 3, 5), new WeightedRandomChestContent(Items.redstone, 0, 4, 9, 5), new WeightedRandomChestContent(Items.dye, EnumDyeColor.BLUE.getDyeDamage(), 4, 9, 5), new WeightedRandomChestContent(Items.diamond, 0, 1, 2, 3), new WeightedRandomChestContent(Items.coal, 0, 3, 8, 10), new WeightedRandomChestContent(Items.bread, 0, 1, 3, 15), new WeightedRandomChestContent(Items.iron_pickaxe, 0, 1, 1, 1), new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.rail), 0, 4, 8, 1), new WeightedRandomChestContent(Items.melon_seeds, 0, 2, 4, 10), new WeightedRandomChestContent(Items.pumpkin_seeds, 0, 2, 4, 10), new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 3), new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 1)});
    private static final String __OBFID = "CL_00000444";

    public static void registerStructurePieces()
    {
        MapGenStructureIO.registerStructureComponent(StructureMineshaftPieces.Corridor.class, "MSCorridor");
        MapGenStructureIO.registerStructureComponent(StructureMineshaftPieces.Cross.class, "MSCrossing");
        MapGenStructureIO.registerStructureComponent(StructureMineshaftPieces.Room.class, "MSRoom");
        MapGenStructureIO.registerStructureComponent(StructureMineshaftPieces.Stairs.class, "MSStairs");
    }

    static
    {
        ChestGenHooks.init(MINESHAFT_CORRIDOR, field_175893_a, 8, 8);
        ChestGenHooks.addItem(MINESHAFT_CORRIDOR, new WeightedRandomChestContent(new net.minecraft.item.ItemStack(Items.enchanted_book, 1, 0), 1, 1, 1));
    }

    private static StructureComponent func_175892_a(List p_175892_0_, Random p_175892_1_, int p_175892_2_, int p_175892_3_, int p_175892_4_, EnumFacing p_175892_5_, int p_175892_6_)
    {
        int i1 = p_175892_1_.nextInt(100);
        StructureBoundingBox structureboundingbox;

        if (i1 >= 80)
        {
            structureboundingbox = StructureMineshaftPieces.Cross.func_175813_a(p_175892_0_, p_175892_1_, p_175892_2_, p_175892_3_, p_175892_4_, p_175892_5_);

            if (structureboundingbox != null)
            {
                return new StructureMineshaftPieces.Cross(p_175892_6_, p_175892_1_, structureboundingbox, p_175892_5_);
            }
        }
        else if (i1 >= 70)
        {
            structureboundingbox = StructureMineshaftPieces.Stairs.func_175812_a(p_175892_0_, p_175892_1_, p_175892_2_, p_175892_3_, p_175892_4_, p_175892_5_);

            if (structureboundingbox != null)
            {
                return new StructureMineshaftPieces.Stairs(p_175892_6_, p_175892_1_, structureboundingbox, p_175892_5_);
            }
        }
        else
        {
            structureboundingbox = StructureMineshaftPieces.Corridor.func_175814_a(p_175892_0_, p_175892_1_, p_175892_2_, p_175892_3_, p_175892_4_, p_175892_5_);

            if (structureboundingbox != null)
            {
                return new StructureMineshaftPieces.Corridor(p_175892_6_, p_175892_1_, structureboundingbox, p_175892_5_);
            }
        }

        return null;
    }

    private static StructureComponent func_175890_b(StructureComponent p_175890_0_, List p_175890_1_, Random p_175890_2_, int p_175890_3_, int p_175890_4_, int p_175890_5_, EnumFacing p_175890_6_, int p_175890_7_)
    {
        if (p_175890_7_ > 8)
        {
            return null;
        }
        else if (Math.abs(p_175890_3_ - p_175890_0_.getBoundingBox().minX) <= 80 && Math.abs(p_175890_5_ - p_175890_0_.getBoundingBox().minZ) <= 80)
        {
            StructureComponent structurecomponent1 = func_175892_a(p_175890_1_, p_175890_2_, p_175890_3_, p_175890_4_, p_175890_5_, p_175890_6_, p_175890_7_ + 1);

            if (structurecomponent1 != null)
            {
                p_175890_1_.add(structurecomponent1);
                structurecomponent1.buildComponent(p_175890_0_, p_175890_1_, p_175890_2_);
            }

            return structurecomponent1;
        }
        else
        {
            return null;
        }
    }

    public static class Corridor extends StructureComponent
        {
            private boolean hasRails;
            private boolean hasSpiders;
            private boolean spawnerPlaced;
            /** A count of the different sections of this mine. The space between ceiling supports. */
            private int sectionCount;
            private static final String __OBFID = "CL_00000445";

            public Corridor() {}

            /**
             * (abstract) Helper method to write subclass data to NBT
             */
            protected void writeStructureToNBT(NBTTagCompound p_143012_1_)
            {
                p_143012_1_.setBoolean("hr", this.hasRails);
                p_143012_1_.setBoolean("sc", this.hasSpiders);
                p_143012_1_.setBoolean("hps", this.spawnerPlaced);
                p_143012_1_.setInteger("Num", this.sectionCount);
            }

            /**
             * (abstract) Helper method to read subclass data from NBT
             */
            protected void readStructureFromNBT(NBTTagCompound p_143011_1_)
            {
                this.hasRails = p_143011_1_.getBoolean("hr");
                this.hasSpiders = p_143011_1_.getBoolean("sc");
                this.spawnerPlaced = p_143011_1_.getBoolean("hps");
                this.sectionCount = p_143011_1_.getInteger("Num");
            }

            public Corridor(int p_i45625_1_, Random p_i45625_2_, StructureBoundingBox p_i45625_3_, EnumFacing p_i45625_4_)
            {
                super(p_i45625_1_);
                this.coordBaseMode = p_i45625_4_;
                this.boundingBox = p_i45625_3_;
                this.hasRails = p_i45625_2_.nextInt(3) == 0;
                this.hasSpiders = !this.hasRails && p_i45625_2_.nextInt(23) == 0;

                if (this.coordBaseMode != EnumFacing.NORTH && this.coordBaseMode != EnumFacing.SOUTH)
                {
                    this.sectionCount = p_i45625_3_.getXSize() / 5;
                }
                else
                {
                    this.sectionCount = p_i45625_3_.getZSize() / 5;
                }
            }

            public static StructureBoundingBox func_175814_a(List p_175814_0_, Random p_175814_1_, int p_175814_2_, int p_175814_3_, int p_175814_4_, EnumFacing p_175814_5_)
            {
                StructureBoundingBox structureboundingbox = new StructureBoundingBox(p_175814_2_, p_175814_3_, p_175814_4_, p_175814_2_, p_175814_3_ + 2, p_175814_4_);
                int l;

                for (l = p_175814_1_.nextInt(3) + 2; l > 0; --l)
                {
                    int i1 = l * 5;

                    switch (StructureMineshaftPieces.SwitchEnumFacing.field_175894_a[p_175814_5_.ordinal()])
                    {
                        case 1:
                            structureboundingbox.maxX = p_175814_2_ + 2;
                            structureboundingbox.minZ = p_175814_4_ - (i1 - 1);
                            break;
                        case 2:
                            structureboundingbox.maxX = p_175814_2_ + 2;
                            structureboundingbox.maxZ = p_175814_4_ + (i1 - 1);
                            break;
                        case 3:
                            structureboundingbox.minX = p_175814_2_ - (i1 - 1);
                            structureboundingbox.maxZ = p_175814_4_ + 2;
                            break;
                        case 4:
                            structureboundingbox.maxX = p_175814_2_ + (i1 - 1);
                            structureboundingbox.maxZ = p_175814_4_ + 2;
                    }

                    if (StructureComponent.findIntersecting(p_175814_0_, structureboundingbox) == null)
                    {
                        break;
                    }
                }

                return l > 0 ? structureboundingbox : null;
            }

            /**
             * Initiates construction of the Structure Component picked, at the current Location of StructGen
             */
            public void buildComponent(StructureComponent p_74861_1_, List p_74861_2_, Random p_74861_3_)
            {
                int i = this.getComponentType();
                int j = p_74861_3_.nextInt(4);

                if (this.coordBaseMode != null)
                {
                    switch (StructureMineshaftPieces.SwitchEnumFacing.field_175894_a[this.coordBaseMode.ordinal()])
                    {
                        case 1:
                            if (j <= 1)
                            {
                                StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX, this.boundingBox.minY - 1 + p_74861_3_.nextInt(3), this.boundingBox.minZ - 1, this.coordBaseMode, i);
                            }
                            else if (j == 2)
                            {
                                StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX - 1, this.boundingBox.minY - 1 + p_74861_3_.nextInt(3), this.boundingBox.minZ, EnumFacing.WEST, i);
                            }
                            else
                            {
                                StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX + 1, this.boundingBox.minY - 1 + p_74861_3_.nextInt(3), this.boundingBox.minZ, EnumFacing.EAST, i);
                            }

                            break;
                        case 2:
                            if (j <= 1)
                            {
                                StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX, this.boundingBox.minY - 1 + p_74861_3_.nextInt(3), this.boundingBox.maxZ + 1, this.coordBaseMode, i);
                            }
                            else if (j == 2)
                            {
                                StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX - 1, this.boundingBox.minY - 1 + p_74861_3_.nextInt(3), this.boundingBox.maxZ - 3, EnumFacing.WEST, i);
                            }
                            else
                            {
                                StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX + 1, this.boundingBox.minY - 1 + p_74861_3_.nextInt(3), this.boundingBox.maxZ - 3, EnumFacing.EAST, i);
                            }

                            break;
                        case 3:
                            if (j <= 1)
                            {
                                StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX - 1, this.boundingBox.minY - 1 + p_74861_3_.nextInt(3), this.boundingBox.minZ, this.coordBaseMode, i);
                            }
                            else if (j == 2)
                            {
                                StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX, this.boundingBox.minY - 1 + p_74861_3_.nextInt(3), this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
                            }
                            else
                            {
                                StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX, this.boundingBox.minY - 1 + p_74861_3_.nextInt(3), this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
                            }

                            break;
                        case 4:
                            if (j <= 1)
                            {
                                StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX + 1, this.boundingBox.minY - 1 + p_74861_3_.nextInt(3), this.boundingBox.minZ, this.coordBaseMode, i);
                            }
                            else if (j == 2)
                            {
                                StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX - 3, this.boundingBox.minY - 1 + p_74861_3_.nextInt(3), this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
                            }
                            else
                            {
                                StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX - 3, this.boundingBox.minY - 1 + p_74861_3_.nextInt(3), this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
                            }
                    }
                }

                if (i < 8)
                {
                    int k;
                    int l;

                    if (this.coordBaseMode != EnumFacing.NORTH && this.coordBaseMode != EnumFacing.SOUTH)
                    {
                        for (k = this.boundingBox.minX + 3; k + 3 <= this.boundingBox.maxX; k += 5)
                        {
                            l = p_74861_3_.nextInt(5);

                            if (l == 0)
                            {
                                StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, k, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, i + 1);
                            }
                            else if (l == 1)
                            {
                                StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, k, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i + 1);
                            }
                        }
                    }
                    else
                    {
                        for (k = this.boundingBox.minZ + 3; k + 3 <= this.boundingBox.maxZ; k += 5)
                        {
                            l = p_74861_3_.nextInt(5);

                            if (l == 0)
                            {
                                StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX - 1, this.boundingBox.minY, k, EnumFacing.WEST, i + 1);
                            }
                            else if (l == 1)
                            {
                                StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX + 1, this.boundingBox.minY, k, EnumFacing.EAST, i + 1);
                            }
                        }
                    }
                }
            }

            protected boolean func_180778_a(World worldIn, StructureBoundingBox p_180778_2_, Random p_180778_3_, int p_180778_4_, int p_180778_5_, int p_180778_6_, List p_180778_7_, int p_180778_8_)
            {
                BlockPos blockpos = new BlockPos(this.getXWithOffset(p_180778_4_, p_180778_6_), this.getYWithOffset(p_180778_5_), this.getZWithOffset(p_180778_4_, p_180778_6_));

                if (p_180778_2_.func_175898_b(blockpos) && worldIn.getBlockState(blockpos).getBlock().getMaterial() == Material.air)
                {
                    int i1 = p_180778_3_.nextBoolean() ? 1 : 0;
                    worldIn.setBlockState(blockpos, Blocks.rail.getStateFromMeta(this.getMetadataWithOffset(Blocks.rail, i1)), 2);
                    EntityMinecartChest entityminecartchest = new EntityMinecartChest(worldIn, (double)((float)blockpos.getX() + 0.5F), (double)((float)blockpos.getY() + 0.5F), (double)((float)blockpos.getZ() + 0.5F));
                    WeightedRandomChestContent.generateChestContents(p_180778_3_, p_180778_7_, entityminecartchest, p_180778_8_);
                    worldIn.spawnEntityInWorld(entityminecartchest);
                    return true;
                }
                else
                {
                    return false;
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
                    boolean flag = false;
                    boolean flag1 = true;
                    boolean flag2 = false;
                    boolean flag3 = true;
                    int i = this.sectionCount * 5 - 1;
                    this.func_175804_a(worldIn, p_74875_3_, 0, 0, 0, 2, 1, i, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                    this.func_175805_a(worldIn, p_74875_3_, p_74875_2_, 0.8F, 0, 2, 0, 2, 2, i, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);

                    if (this.hasSpiders)
                    {
                        this.func_175805_a(worldIn, p_74875_3_, p_74875_2_, 0.6F, 0, 0, 0, 2, 1, i, Blocks.web.getDefaultState(), Blocks.air.getDefaultState(), false);
                    }

                    int j;
                    int k;

                    for (j = 0; j < this.sectionCount; ++j)
                    {
                        k = 2 + j * 5;
                        this.func_175804_a(worldIn, p_74875_3_, 0, 0, k, 0, 1, k, Blocks.oak_fence.getDefaultState(), Blocks.air.getDefaultState(), false);
                        this.func_175804_a(worldIn, p_74875_3_, 2, 0, k, 2, 1, k, Blocks.oak_fence.getDefaultState(), Blocks.air.getDefaultState(), false);

                        if (p_74875_2_.nextInt(4) == 0)
                        {
                            this.func_175804_a(worldIn, p_74875_3_, 0, 2, k, 0, 2, k, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
                            this.func_175804_a(worldIn, p_74875_3_, 2, 2, k, 2, 2, k, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
                        }
                        else
                        {
                            this.func_175804_a(worldIn, p_74875_3_, 0, 2, k, 2, 2, k, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
                        }

                        this.func_175809_a(worldIn, p_74875_3_, p_74875_2_, 0.1F, 0, 2, k - 1, Blocks.web.getDefaultState());
                        this.func_175809_a(worldIn, p_74875_3_, p_74875_2_, 0.1F, 2, 2, k - 1, Blocks.web.getDefaultState());
                        this.func_175809_a(worldIn, p_74875_3_, p_74875_2_, 0.1F, 0, 2, k + 1, Blocks.web.getDefaultState());
                        this.func_175809_a(worldIn, p_74875_3_, p_74875_2_, 0.1F, 2, 2, k + 1, Blocks.web.getDefaultState());
                        this.func_175809_a(worldIn, p_74875_3_, p_74875_2_, 0.05F, 0, 2, k - 2, Blocks.web.getDefaultState());
                        this.func_175809_a(worldIn, p_74875_3_, p_74875_2_, 0.05F, 2, 2, k - 2, Blocks.web.getDefaultState());
                        this.func_175809_a(worldIn, p_74875_3_, p_74875_2_, 0.05F, 0, 2, k + 2, Blocks.web.getDefaultState());
                        this.func_175809_a(worldIn, p_74875_3_, p_74875_2_, 0.05F, 2, 2, k + 2, Blocks.web.getDefaultState());
                        this.func_175809_a(worldIn, p_74875_3_, p_74875_2_, 0.05F, 1, 2, k - 1, Blocks.torch.getStateFromMeta(EnumFacing.UP.getIndex()));
                        this.func_175809_a(worldIn, p_74875_3_, p_74875_2_, 0.05F, 1, 2, k + 1, Blocks.torch.getStateFromMeta(EnumFacing.UP.getIndex()));

                        ChestGenHooks info = ChestGenHooks.getInfo(MINESHAFT_CORRIDOR);
                        if (p_74875_2_.nextInt(100) == 0)
                        {
                            this.func_180778_a(worldIn, p_74875_3_, p_74875_2_, 2, 0, k - 1, info.getItems(p_74875_2_), info.getCount(p_74875_2_));
                        }

                        if (p_74875_2_.nextInt(100) == 0)
                        {
                            this.func_180778_a(worldIn, p_74875_3_, p_74875_2_, 0, 0, k + 1, info.getItems(p_74875_2_), info.getCount(p_74875_2_));
                        }

                        if (this.hasSpiders && !this.spawnerPlaced)
                        {
                            int l = this.getYWithOffset(0);
                            int i1 = k - 1 + p_74875_2_.nextInt(3);
                            int j1 = this.getXWithOffset(1, i1);
                            i1 = this.getZWithOffset(1, i1);
                            BlockPos blockpos = new BlockPos(j1, l, i1);

                            if (p_74875_3_.func_175898_b(blockpos))
                            {
                                this.spawnerPlaced = true;
                                worldIn.setBlockState(blockpos, Blocks.mob_spawner.getDefaultState(), 2);
                                TileEntity tileentity = worldIn.getTileEntity(blockpos);

                                if (tileentity instanceof TileEntityMobSpawner)
                                {
                                    ((TileEntityMobSpawner)tileentity).getSpawnerBaseLogic().setEntityName("CaveSpider");
                                }
                            }
                        }
                    }

                    for (j = 0; j <= 2; ++j)
                    {
                        for (k = 0; k <= i; ++k)
                        {
                            byte b0 = -1;
                            IBlockState iblockstate1 = this.func_175807_a(worldIn, j, b0, k, p_74875_3_);

                            if (iblockstate1.getBlock().getMaterial() == Material.air)
                            {
                                byte b1 = -1;
                                this.func_175811_a(worldIn, Blocks.planks.getDefaultState(), j, b1, k, p_74875_3_);
                            }
                        }
                    }

                    if (this.hasRails)
                    {
                        for (j = 0; j <= i; ++j)
                        {
                            IBlockState iblockstate = this.func_175807_a(worldIn, 1, -1, j, p_74875_3_);

                            if (iblockstate.getBlock().getMaterial() != Material.air && iblockstate.getBlock().isFullBlock())
                            {
                                this.func_175809_a(worldIn, p_74875_3_, p_74875_2_, 0.7F, 1, 0, j, Blocks.rail.getStateFromMeta(this.getMetadataWithOffset(Blocks.rail, 0)));
                            }
                        }
                    }

                    return true;
                }
            }
        }

    public static class Cross extends StructureComponent
        {
            private EnumFacing corridorDirection;
            private boolean isMultipleFloors;
            private static final String __OBFID = "CL_00000446";

            public Cross() {}

            /**
             * (abstract) Helper method to write subclass data to NBT
             */
            protected void writeStructureToNBT(NBTTagCompound p_143012_1_)
            {
                p_143012_1_.setBoolean("tf", this.isMultipleFloors);
                p_143012_1_.setInteger("D", this.corridorDirection.getHorizontalIndex());
            }

            /**
             * (abstract) Helper method to read subclass data from NBT
             */
            protected void readStructureFromNBT(NBTTagCompound p_143011_1_)
            {
                this.isMultipleFloors = p_143011_1_.getBoolean("tf");
                this.corridorDirection = EnumFacing.getHorizontal(p_143011_1_.getInteger("D"));
            }

            public Cross(int p_i45624_1_, Random p_i45624_2_, StructureBoundingBox p_i45624_3_, EnumFacing p_i45624_4_)
            {
                super(p_i45624_1_);
                this.corridorDirection = p_i45624_4_;
                this.boundingBox = p_i45624_3_;
                this.isMultipleFloors = p_i45624_3_.getYSize() > 3;
            }

            public static StructureBoundingBox func_175813_a(List p_175813_0_, Random p_175813_1_, int p_175813_2_, int p_175813_3_, int p_175813_4_, EnumFacing p_175813_5_)
            {
                StructureBoundingBox structureboundingbox = new StructureBoundingBox(p_175813_2_, p_175813_3_, p_175813_4_, p_175813_2_, p_175813_3_ + 2, p_175813_4_);

                if (p_175813_1_.nextInt(4) == 0)
                {
                    structureboundingbox.maxY += 4;
                }

                switch (StructureMineshaftPieces.SwitchEnumFacing.field_175894_a[p_175813_5_.ordinal()])
                {
                    case 1:
                        structureboundingbox.minX = p_175813_2_ - 1;
                        structureboundingbox.maxX = p_175813_2_ + 3;
                        structureboundingbox.minZ = p_175813_4_ - 4;
                        break;
                    case 2:
                        structureboundingbox.minX = p_175813_2_ - 1;
                        structureboundingbox.maxX = p_175813_2_ + 3;
                        structureboundingbox.maxZ = p_175813_4_ + 4;
                        break;
                    case 3:
                        structureboundingbox.minX = p_175813_2_ - 4;
                        structureboundingbox.minZ = p_175813_4_ - 1;
                        structureboundingbox.maxZ = p_175813_4_ + 3;
                        break;
                    case 4:
                        structureboundingbox.maxX = p_175813_2_ + 4;
                        structureboundingbox.minZ = p_175813_4_ - 1;
                        structureboundingbox.maxZ = p_175813_4_ + 3;
                }

                return StructureComponent.findIntersecting(p_175813_0_, structureboundingbox) != null ? null : structureboundingbox;
            }

            /**
             * Initiates construction of the Structure Component picked, at the current Location of StructGen
             */
            public void buildComponent(StructureComponent p_74861_1_, List p_74861_2_, Random p_74861_3_)
            {
                int i = this.getComponentType();

                switch (StructureMineshaftPieces.SwitchEnumFacing.field_175894_a[this.corridorDirection.ordinal()])
                {
                    case 1:
                        StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
                        StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.WEST, i);
                        StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.EAST, i);
                        break;
                    case 2:
                        StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
                        StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.WEST, i);
                        StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.EAST, i);
                        break;
                    case 3:
                        StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
                        StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
                        StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.WEST, i);
                        break;
                    case 4:
                        StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
                        StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
                        StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.EAST, i);
                }

                if (this.isMultipleFloors)
                {
                    if (p_74861_3_.nextBoolean())
                    {
                        StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX + 1, this.boundingBox.minY + 3 + 1, this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
                    }

                    if (p_74861_3_.nextBoolean())
                    {
                        StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX - 1, this.boundingBox.minY + 3 + 1, this.boundingBox.minZ + 1, EnumFacing.WEST, i);
                    }

                    if (p_74861_3_.nextBoolean())
                    {
                        StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX + 1, this.boundingBox.minY + 3 + 1, this.boundingBox.minZ + 1, EnumFacing.EAST, i);
                    }

                    if (p_74861_3_.nextBoolean())
                    {
                        StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX + 1, this.boundingBox.minY + 3 + 1, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
                    }
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
                    if (this.isMultipleFloors)
                    {
                        this.func_175804_a(worldIn, p_74875_3_, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.minY + 3 - 1, this.boundingBox.maxZ, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                        this.func_175804_a(worldIn, p_74875_3_, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.minY + 3 - 1, this.boundingBox.maxZ - 1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                        this.func_175804_a(worldIn, p_74875_3_, this.boundingBox.minX + 1, this.boundingBox.maxY - 2, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.maxZ, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                        this.func_175804_a(worldIn, p_74875_3_, this.boundingBox.minX, this.boundingBox.maxY - 2, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ - 1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                        this.func_175804_a(worldIn, p_74875_3_, this.boundingBox.minX + 1, this.boundingBox.minY + 3, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.minY + 3, this.boundingBox.maxZ - 1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                    }
                    else
                    {
                        this.func_175804_a(worldIn, p_74875_3_, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.maxZ, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                        this.func_175804_a(worldIn, p_74875_3_, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ - 1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                    }

                    this.func_175804_a(worldIn, p_74875_3_, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.minX + 1, this.boundingBox.maxY, this.boundingBox.minZ + 1, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
                    this.func_175804_a(worldIn, p_74875_3_, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ - 1, this.boundingBox.minX + 1, this.boundingBox.maxY, this.boundingBox.maxZ - 1, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
                    this.func_175804_a(worldIn, p_74875_3_, this.boundingBox.maxX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.minZ + 1, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
                    this.func_175804_a(worldIn, p_74875_3_, this.boundingBox.maxX - 1, this.boundingBox.minY, this.boundingBox.maxZ - 1, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.maxZ - 1, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);

                    for (int i = this.boundingBox.minX; i <= this.boundingBox.maxX; ++i)
                    {
                        for (int j = this.boundingBox.minZ; j <= this.boundingBox.maxZ; ++j)
                        {
                            if (this.func_175807_a(worldIn, i, this.boundingBox.minY - 1, j, p_74875_3_).getBlock().getMaterial() == Material.air)
                            {
                                this.func_175811_a(worldIn, Blocks.planks.getDefaultState(), i, this.boundingBox.minY - 1, j, p_74875_3_);
                            }
                        }
                    }

                    return true;
                }
            }
        }

    public static class Room extends StructureComponent
        {
            /** List of other Mineshaft components linked to this room. */
            private List roomsLinkedToTheRoom = Lists.newLinkedList();
            private static final String __OBFID = "CL_00000447";

            public Room() {}

            public Room(int p_i2037_1_, Random p_i2037_2_, int p_i2037_3_, int p_i2037_4_)
            {
                super(p_i2037_1_);
                this.boundingBox = new StructureBoundingBox(p_i2037_3_, 50, p_i2037_4_, p_i2037_3_ + 7 + p_i2037_2_.nextInt(6), 54 + p_i2037_2_.nextInt(6), p_i2037_4_ + 7 + p_i2037_2_.nextInt(6));
            }

            /**
             * Initiates construction of the Structure Component picked, at the current Location of StructGen
             */
            public void buildComponent(StructureComponent p_74861_1_, List p_74861_2_, Random p_74861_3_)
            {
                int i = this.getComponentType();
                int k = this.boundingBox.getYSize() - 3 - 1;

                if (k <= 0)
                {
                    k = 1;
                }

                int j;
                StructureComponent structurecomponent1;
                StructureBoundingBox structureboundingbox;

                for (j = 0; j < this.boundingBox.getXSize(); j += 4)
                {
                    j += p_74861_3_.nextInt(this.boundingBox.getXSize());

                    if (j + 3 > this.boundingBox.getXSize())
                    {
                        break;
                    }

                    structurecomponent1 = StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX + j, this.boundingBox.minY + p_74861_3_.nextInt(k) + 1, this.boundingBox.minZ - 1, EnumFacing.NORTH, i);

                    if (structurecomponent1 != null)
                    {
                        structureboundingbox = structurecomponent1.getBoundingBox();
                        this.roomsLinkedToTheRoom.add(new StructureBoundingBox(structureboundingbox.minX, structureboundingbox.minY, this.boundingBox.minZ, structureboundingbox.maxX, structureboundingbox.maxY, this.boundingBox.minZ + 1));
                    }
                }

                for (j = 0; j < this.boundingBox.getXSize(); j += 4)
                {
                    j += p_74861_3_.nextInt(this.boundingBox.getXSize());

                    if (j + 3 > this.boundingBox.getXSize())
                    {
                        break;
                    }

                    structurecomponent1 = StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX + j, this.boundingBox.minY + p_74861_3_.nextInt(k) + 1, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);

                    if (structurecomponent1 != null)
                    {
                        structureboundingbox = structurecomponent1.getBoundingBox();
                        this.roomsLinkedToTheRoom.add(new StructureBoundingBox(structureboundingbox.minX, structureboundingbox.minY, this.boundingBox.maxZ - 1, structureboundingbox.maxX, structureboundingbox.maxY, this.boundingBox.maxZ));
                    }
                }

                for (j = 0; j < this.boundingBox.getZSize(); j += 4)
                {
                    j += p_74861_3_.nextInt(this.boundingBox.getZSize());

                    if (j + 3 > this.boundingBox.getZSize())
                    {
                        break;
                    }

                    structurecomponent1 = StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX - 1, this.boundingBox.minY + p_74861_3_.nextInt(k) + 1, this.boundingBox.minZ + j, EnumFacing.WEST, i);

                    if (structurecomponent1 != null)
                    {
                        structureboundingbox = structurecomponent1.getBoundingBox();
                        this.roomsLinkedToTheRoom.add(new StructureBoundingBox(this.boundingBox.minX, structureboundingbox.minY, structureboundingbox.minZ, this.boundingBox.minX + 1, structureboundingbox.maxY, structureboundingbox.maxZ));
                    }
                }

                for (j = 0; j < this.boundingBox.getZSize(); j += 4)
                {
                    j += p_74861_3_.nextInt(this.boundingBox.getZSize());

                    if (j + 3 > this.boundingBox.getZSize())
                    {
                        break;
                    }

                    structurecomponent1 = StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX + 1, this.boundingBox.minY + p_74861_3_.nextInt(k) + 1, this.boundingBox.minZ + j, EnumFacing.EAST, i);

                    if (structurecomponent1 != null)
                    {
                        structureboundingbox = structurecomponent1.getBoundingBox();
                        this.roomsLinkedToTheRoom.add(new StructureBoundingBox(this.boundingBox.maxX - 1, structureboundingbox.minY, structureboundingbox.minZ, this.boundingBox.maxX, structureboundingbox.maxY, structureboundingbox.maxZ));
                    }
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
                    this.func_175804_a(worldIn, p_74875_3_, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.minY, this.boundingBox.maxZ, Blocks.dirt.getDefaultState(), Blocks.air.getDefaultState(), true);
                    this.func_175804_a(worldIn, p_74875_3_, this.boundingBox.minX, this.boundingBox.minY + 1, this.boundingBox.minZ, this.boundingBox.maxX, Math.min(this.boundingBox.minY + 3, this.boundingBox.maxY), this.boundingBox.maxZ, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                    Iterator iterator = this.roomsLinkedToTheRoom.iterator();

                    while (iterator.hasNext())
                    {
                        StructureBoundingBox structureboundingbox1 = (StructureBoundingBox)iterator.next();
                        this.func_175804_a(worldIn, p_74875_3_, structureboundingbox1.minX, structureboundingbox1.maxY - 2, structureboundingbox1.minZ, structureboundingbox1.maxX, structureboundingbox1.maxY, structureboundingbox1.maxZ, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                    }

                    this.func_180777_a(worldIn, p_74875_3_, this.boundingBox.minX, this.boundingBox.minY + 4, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ, Blocks.air.getDefaultState(), false);
                    return true;
                }
            }

            /**
             * (abstract) Helper method to write subclass data to NBT
             */
            protected void writeStructureToNBT(NBTTagCompound p_143012_1_)
            {
                NBTTagList nbttaglist = new NBTTagList();
                Iterator iterator = this.roomsLinkedToTheRoom.iterator();

                while (iterator.hasNext())
                {
                    StructureBoundingBox structureboundingbox = (StructureBoundingBox)iterator.next();
                    nbttaglist.appendTag(structureboundingbox.func_151535_h());
                }

                p_143012_1_.setTag("Entrances", nbttaglist);
            }

            /**
             * (abstract) Helper method to read subclass data from NBT
             */
            protected void readStructureFromNBT(NBTTagCompound p_143011_1_)
            {
                NBTTagList nbttaglist = p_143011_1_.getTagList("Entrances", 11);

                for (int i = 0; i < nbttaglist.tagCount(); ++i)
                {
                    this.roomsLinkedToTheRoom.add(new StructureBoundingBox(nbttaglist.getIntArray(i)));
                }
            }
        }

    public static class Stairs extends StructureComponent
        {
            private static final String __OBFID = "CL_00000449";

            public Stairs() {}

            public Stairs(int p_i45623_1_, Random p_i45623_2_, StructureBoundingBox p_i45623_3_, EnumFacing p_i45623_4_)
            {
                super(p_i45623_1_);
                this.coordBaseMode = p_i45623_4_;
                this.boundingBox = p_i45623_3_;
            }

            /**
             * (abstract) Helper method to write subclass data to NBT
             */
            protected void writeStructureToNBT(NBTTagCompound p_143012_1_) {}

            /**
             * (abstract) Helper method to read subclass data from NBT
             */
            protected void readStructureFromNBT(NBTTagCompound p_143011_1_) {}

            public static StructureBoundingBox func_175812_a(List p_175812_0_, Random p_175812_1_, int p_175812_2_, int p_175812_3_, int p_175812_4_, EnumFacing p_175812_5_)
            {
                StructureBoundingBox structureboundingbox = new StructureBoundingBox(p_175812_2_, p_175812_3_ - 5, p_175812_4_, p_175812_2_, p_175812_3_ + 2, p_175812_4_);

                switch (StructureMineshaftPieces.SwitchEnumFacing.field_175894_a[p_175812_5_.ordinal()])
                {
                    case 1:
                        structureboundingbox.maxX = p_175812_2_ + 2;
                        structureboundingbox.minZ = p_175812_4_ - 8;
                        break;
                    case 2:
                        structureboundingbox.maxX = p_175812_2_ + 2;
                        structureboundingbox.maxZ = p_175812_4_ + 8;
                        break;
                    case 3:
                        structureboundingbox.minX = p_175812_2_ - 8;
                        structureboundingbox.maxZ = p_175812_4_ + 2;
                        break;
                    case 4:
                        structureboundingbox.maxX = p_175812_2_ + 8;
                        structureboundingbox.maxZ = p_175812_4_ + 2;
                }

                return StructureComponent.findIntersecting(p_175812_0_, structureboundingbox) != null ? null : structureboundingbox;
            }

            /**
             * Initiates construction of the Structure Component picked, at the current Location of StructGen
             */
            public void buildComponent(StructureComponent p_74861_1_, List p_74861_2_, Random p_74861_3_)
            {
                int i = this.getComponentType();

                if (this.coordBaseMode != null)
                {
                    switch (StructureMineshaftPieces.SwitchEnumFacing.field_175894_a[this.coordBaseMode.ordinal()])
                    {
                        case 1:
                            StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
                            break;
                        case 2:
                            StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
                            break;
                        case 3:
                            StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.WEST, i);
                            break;
                        case 4:
                            StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.EAST, i);
                    }
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
                    this.func_175804_a(worldIn, p_74875_3_, 0, 5, 0, 2, 7, 1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                    this.func_175804_a(worldIn, p_74875_3_, 0, 0, 7, 2, 2, 8, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);

                    for (int i = 0; i < 5; ++i)
                    {
                        this.func_175804_a(worldIn, p_74875_3_, 0, 5 - i - (i < 4 ? 1 : 0), 2 + i, 2, 7 - i, 2 + i, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                    }

                    return true;
                }
            }
        }

    static final class SwitchEnumFacing
        {
            static final int[] field_175894_a = new int[EnumFacing.values().length];
            private static final String __OBFID = "CL_00001998";

            static
            {
                try
                {
                    field_175894_a[EnumFacing.NORTH.ordinal()] = 1;
                }
                catch (NoSuchFieldError var4)
                {
                    ;
                }

                try
                {
                    field_175894_a[EnumFacing.SOUTH.ordinal()] = 2;
                }
                catch (NoSuchFieldError var3)
                {
                    ;
                }

                try
                {
                    field_175894_a[EnumFacing.WEST.ordinal()] = 3;
                }
                catch (NoSuchFieldError var2)
                {
                    ;
                }

                try
                {
                    field_175894_a[EnumFacing.EAST.ordinal()] = 4;
                }
                catch (NoSuchFieldError var1)
                {
                    ;
                }
            }
        }
}