package net.minecraft.block;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockFlower extends BlockBush
{
    protected PropertyEnum type;
    private static final String __OBFID = "CL_00000246";

    protected BlockFlower()
    {
        super(Material.plants);
        this.setDefaultState(this.blockState.getBaseState().withProperty(this.getTypeProperty(), this.getBlockType() == BlockFlower.EnumFlowerColor.RED ? BlockFlower.EnumFlowerType.POPPY : BlockFlower.EnumFlowerType.DANDELION));
    }

    /**
     * Get the damage value that this Block should drop
     */
    public int damageDropped(IBlockState state)
    {
        return ((BlockFlower.EnumFlowerType)state.getValue(this.getTypeProperty())).getMeta();
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
    {
        BlockFlower.EnumFlowerType[] aenumflowertype = BlockFlower.EnumFlowerType.getTypes(this.getBlockType());
        int i = aenumflowertype.length;

        for (int j = 0; j < i; ++j)
        {
            BlockFlower.EnumFlowerType enumflowertype = aenumflowertype[j];
            list.add(new ItemStack(itemIn, 1, enumflowertype.getMeta()));
        }
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(this.getTypeProperty(), BlockFlower.EnumFlowerType.getType(this.getBlockType(), meta));
    }

    /**
     * Get the Type of this flower (Yellow/Red)
     */
    public abstract BlockFlower.EnumFlowerColor getBlockType();

    public IProperty getTypeProperty()
    {
        if (this.type == null)
        {
            this.type = PropertyEnum.create("type", BlockFlower.EnumFlowerType.class, new Predicate()
            {
                private static final String __OBFID = "CL_00002120";
                public boolean apply(BlockFlower.EnumFlowerType type)
                {
                    return type.getBlockType() == BlockFlower.this.getBlockType();
                }
                public boolean apply(Object p_apply_1_)
                {
                    return this.apply((BlockFlower.EnumFlowerType)p_apply_1_);
                }
            });
        }

        return this.type;
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        return ((BlockFlower.EnumFlowerType)state.getValue(this.getTypeProperty())).getMeta();
    }

    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {this.getTypeProperty()});
    }

    /**
     * Get the OffsetType for this Block. Determines if the model is rendered slightly offset.
     */
    @SideOnly(Side.CLIENT)
    public Block.EnumOffsetType getOffsetType()
    {
        return Block.EnumOffsetType.XZ;
    }

    public static enum EnumFlowerColor
    {
        YELLOW,
        RED;

        private static final String __OBFID = "CL_00002117";

        public BlockFlower getBlock()
        {
            return this == YELLOW ? Blocks.yellow_flower : Blocks.red_flower;
        }
    }

    public static enum EnumFlowerType implements IStringSerializable
    {
        DANDELION(BlockFlower.EnumFlowerColor.YELLOW, 0, "dandelion"),
        POPPY(BlockFlower.EnumFlowerColor.RED, 0, "poppy"),
        BLUE_ORCHID(BlockFlower.EnumFlowerColor.RED, 1, "blue_orchid", "blueOrchid"),
        ALLIUM(BlockFlower.EnumFlowerColor.RED, 2, "allium"),
        HOUSTONIA(BlockFlower.EnumFlowerColor.RED, 3, "houstonia"),
        RED_TULIP(BlockFlower.EnumFlowerColor.RED, 4, "red_tulip", "tulipRed"),
        ORANGE_TULIP(BlockFlower.EnumFlowerColor.RED, 5, "orange_tulip", "tulipOrange"),
        WHITE_TULIP(BlockFlower.EnumFlowerColor.RED, 6, "white_tulip", "tulipWhite"),
        PINK_TULIP(BlockFlower.EnumFlowerColor.RED, 7, "pink_tulip", "tulipPink"),
        OXEYE_DAISY(BlockFlower.EnumFlowerColor.RED, 8, "oxeye_daisy", "oxeyeDaisy");
        private static final BlockFlower.EnumFlowerType[][] TYPES_FOR_BLOCK = new BlockFlower.EnumFlowerType[BlockFlower.EnumFlowerColor.values().length][];
        private final BlockFlower.EnumFlowerColor blockType;
        private final int meta;
        private final String name;
        private final String unlocalizedName;

        private static final String __OBFID = "CL_00002119";

        private EnumFlowerType(BlockFlower.EnumFlowerColor blockType, int meta, String name)
        {
            this(blockType, meta, name, name);
        }

        private EnumFlowerType(BlockFlower.EnumFlowerColor blockType, int meta, String name, String unlocalizedName)
        {
            this.blockType = blockType;
            this.meta = meta;
            this.name = name;
            this.unlocalizedName = unlocalizedName;
        }

        public BlockFlower.EnumFlowerColor getBlockType()
        {
            return this.blockType;
        }

        public int getMeta()
        {
            return this.meta;
        }

        /**
         * Get the given FlowerType from BlockType & metadata
         */
        public static BlockFlower.EnumFlowerType getType(BlockFlower.EnumFlowerColor blockType, int meta)
        {
            BlockFlower.EnumFlowerType[] aenumflowertype = TYPES_FOR_BLOCK[blockType.ordinal()];

            if (meta < 0 || meta >= aenumflowertype.length)
            {
                meta = 0;
            }

            return aenumflowertype[meta];
        }

        /**
         * Get all FlowerTypes that are applicable for the given Flower block ("yellow", "red")
         */
        @SideOnly(Side.CLIENT)
        public static BlockFlower.EnumFlowerType[] getTypes(BlockFlower.EnumFlowerColor p_176966_0_)
        {
            return TYPES_FOR_BLOCK[p_176966_0_.ordinal()];
        }

        public String toString()
        {
            return this.name;
        }

        public String getName()
        {
            return this.name;
        }

        public String getUnlocalizedName()
        {
            return this.unlocalizedName;
        }

        static
        {
            BlockFlower.EnumFlowerColor[] var0 = BlockFlower.EnumFlowerColor.values();
            int var1 = var0.length;

            for (int var2 = 0; var2 < var1; ++var2)
            {
                final BlockFlower.EnumFlowerColor var3 = var0[var2];
                Collection var4 = Collections2.filter(Lists.newArrayList(values()), new Predicate()
                {
                    private static final String __OBFID = "CL_00002118";
                    public boolean apply(BlockFlower.EnumFlowerType type)
                    {
                        return type.getBlockType() == var3;
                    }
                    public boolean apply(Object p_apply_1_)
                    {
                        return this.apply((BlockFlower.EnumFlowerType)p_apply_1_);
                    }
                });
                TYPES_FOR_BLOCK[var3.ordinal()] = (BlockFlower.EnumFlowerType[])var4.toArray(new BlockFlower.EnumFlowerType[var4.size()]);
            }
        }
    }
}