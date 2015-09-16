package net.minecraft.world;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ChunkCache implements IBlockAccess
{
    protected int chunkX;
    protected int chunkZ;
    protected Chunk[][] chunkArray;
    /** set by !chunk.getAreLevelsEmpty */
    protected boolean hasExtendedLevels;
    /** Reference to the World object. */
    protected World worldObj;
    private static final String __OBFID = "CL_00000155";

    public ChunkCache(World worldIn, BlockPos posFromIn, BlockPos posToIn, int subIn)
    {
        this.worldObj = worldIn;
        this.chunkX = posFromIn.getX() - subIn >> 4;
        this.chunkZ = posFromIn.getZ() - subIn >> 4;
        int j = posToIn.getX() + subIn >> 4;
        int k = posToIn.getZ() + subIn >> 4;
        this.chunkArray = new Chunk[j - this.chunkX + 1][k - this.chunkZ + 1];
        this.hasExtendedLevels = true;
        int l;
        int i1;

        for (l = this.chunkX; l <= j; ++l)
        {
            for (i1 = this.chunkZ; i1 <= k; ++i1)
            {
                this.chunkArray[l - this.chunkX][i1 - this.chunkZ] = worldIn.getChunkFromChunkCoords(l, i1);
            }
        }

        for (l = posFromIn.getX() >> 4; l <= posToIn.getX() >> 4; ++l)
        {
            for (i1 = posFromIn.getZ() >> 4; i1 <= posToIn.getZ() >> 4; ++i1)
            {
                Chunk chunk = this.chunkArray[l - this.chunkX][i1 - this.chunkZ];

                if (chunk != null && !chunk.getAreLevelsEmpty(posFromIn.getY(), posToIn.getY()))
                {
                    this.hasExtendedLevels = false;
                }
            }
        }
    }

    /**
     * set by !chunk.getAreLevelsEmpty
     */
    @SideOnly(Side.CLIENT)
    public boolean extendedLevelsInChunkCache()
    {
        return this.hasExtendedLevels;
    }

    public TileEntity getTileEntity(BlockPos pos)
    {
        int i = (pos.getX() >> 4) - this.chunkX;
        int j = (pos.getZ() >> 4) - this.chunkZ;
        if (i < 0 || i >= chunkArray.length || j < 0 || j >= chunkArray[i].length) return null;
        if (chunkArray[i][j] == null) return null;
        return this.chunkArray[i][j].getTileEntity(pos, Chunk.EnumCreateEntityType.IMMEDIATE);
    }

    @SideOnly(Side.CLIENT)
    public int getCombinedLight(BlockPos pos, int p_175626_2_)
    {
        int j = this.getLightForExt(EnumSkyBlock.SKY, pos);
        int k = this.getLightForExt(EnumSkyBlock.BLOCK, pos);

        if (k < p_175626_2_)
        {
            k = p_175626_2_;
        }

        return j << 20 | k << 4;
    }

    public IBlockState getBlockState(BlockPos pos)
    {
        if (pos.getY() >= 0 && pos.getY() < 256)
        {
            int i = (pos.getX() >> 4) - this.chunkX;
            int j = (pos.getZ() >> 4) - this.chunkZ;
            if (i < 0 || i >= chunkArray.length || j < 0 || i >= chunkArray[i].length) return Blocks.air.getDefaultState();

            if (i >= 0 && i < this.chunkArray.length && j >= 0 && j < this.chunkArray[i].length)
            {
                Chunk chunk = this.chunkArray[i][j];

                if (chunk != null)
                {
                    return chunk.getBlockState(pos);
                }
            }
        }

        return Blocks.air.getDefaultState();
    }

    @SideOnly(Side.CLIENT)
    public BiomeGenBase getBiomeGenForCoords(BlockPos pos)
    {
        return this.worldObj.getBiomeGenForCoords(pos);
    }

    @SideOnly(Side.CLIENT)
    private int getLightForExt(EnumSkyBlock p_175629_1_, BlockPos pos)
    {
        if (p_175629_1_ == EnumSkyBlock.SKY && this.worldObj.provider.getHasNoSky())
        {
            return 0;
        }
        else if (pos.getY() >= 0 && pos.getY() < 256)
        {
            int i;

            if (this.getBlockState(pos).getBlock().getUseNeighborBrightness())
            {
                i = 0;
                EnumFacing[] aenumfacing = EnumFacing.values();
                int k = aenumfacing.length;

                for (int l = 0; l < k; ++l)
                {
                    EnumFacing enumfacing = aenumfacing[l];
                    int i1 = this.getLightFor(p_175629_1_, pos.offset(enumfacing));

                    if (i1 > i)
                    {
                        i = i1;
                    }

                    if (i >= 15)
                    {
                        return i;
                    }
                }

                return i;
            }
            else
            {
                i = (pos.getX() >> 4) - this.chunkX;
                int j = (pos.getZ() >> 4) - this.chunkZ;
                if (i < 0 || i >= chunkArray.length || j < 0 || j >= chunkArray[i].length) return p_175629_1_.defaultLightValue;
                if (chunkArray[i][j] == null) return p_175629_1_.defaultLightValue;
                return this.chunkArray[i][j].getLightFor(p_175629_1_, pos);
            }
        }
        else
        {
            return p_175629_1_.defaultLightValue;
        }
    }

    /**
     * Checks to see if an air block exists at the provided location. Note that this only checks to see if the blocks
     * material is set to air, meaning it is possible for non-vanilla blocks to still pass this check.
     *  
     * @param pos The position of the block being checked.
     */
    public boolean isAirBlock(BlockPos pos)
    {
        return this.getBlockState(pos).getBlock().isAir(this, pos);
    }

    @SideOnly(Side.CLIENT)
    public int getLightFor(EnumSkyBlock p_175628_1_, BlockPos pos)
    {
        if (pos.getY() >= 0 && pos.getY() < 256)
        {
            int i = (pos.getX() >> 4) - this.chunkX;
            int j = (pos.getZ() >> 4) - this.chunkZ;
            if (i < 0 || i >= chunkArray.length || j < 0 || i >= chunkArray[i].length) return p_175628_1_.defaultLightValue;
            return this.chunkArray[i][j].getLightFor(p_175628_1_, pos);
        }
        else
        {
            return p_175628_1_.defaultLightValue;
        }
    }

    public int getStrongPower(BlockPos pos, EnumFacing direction)
    {
        IBlockState iblockstate = this.getBlockState(pos);
        return iblockstate.getBlock().isProvidingStrongPower(this, pos, iblockstate, direction);
    }

    @SideOnly(Side.CLIENT)
    public WorldType getWorldType()
    {
        return this.worldObj.getWorldType();
    }

    @Override
    public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default)
    {
        int x = (pos.getX() >> 4) - this.chunkX;
        int z = (pos.getZ() >> 4) - this.chunkZ;
        if (pos.getY() >= 0 && pos.getY() < 256) return _default;
        if (x < 0 || x >= chunkArray.length || z < 0 || x >= chunkArray[x].length) return _default;

        return getBlockState(pos).getBlock().isSideSolid(this, pos, side);
    }
}