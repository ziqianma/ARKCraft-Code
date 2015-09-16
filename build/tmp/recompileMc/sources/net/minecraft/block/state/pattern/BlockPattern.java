package net.minecraft.block.state.pattern;

import com.google.common.base.Predicate;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import java.util.Iterator;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;

public class BlockPattern
{
    private final Predicate[][][] blockMatches;
    private final int fingerLength;
    private final int thumbLength;
    private final int palmLength;
    private static final String __OBFID = "CL_00002024";

    public BlockPattern(Predicate[][][] predicatesIn)
    {
        this.blockMatches = predicatesIn;
        this.fingerLength = predicatesIn.length;

        if (this.fingerLength > 0)
        {
            this.thumbLength = predicatesIn[0].length;

            if (this.thumbLength > 0)
            {
                this.palmLength = predicatesIn[0][0].length;
            }
            else
            {
                this.palmLength = 0;
            }
        }
        else
        {
            this.thumbLength = 0;
            this.palmLength = 0;
        }
    }

    public int getThumbLength()
    {
        return this.thumbLength;
    }

    public int getPalmLength()
    {
        return this.palmLength;
    }

    /**
     * checks that the given pattern & rotation is at the block co-ordinates.
     */
    private BlockPattern.PatternHelper checkPatternAt(BlockPos pos, EnumFacing finger, EnumFacing thumb, LoadingCache lcache)
    {
        for (int i = 0; i < this.palmLength; ++i)
        {
            for (int j = 0; j < this.thumbLength; ++j)
            {
                for (int k = 0; k < this.fingerLength; ++k)
                {
                    if (!this.blockMatches[k][j][i].apply(lcache.getUnchecked(translateOffset(pos, finger, thumb, i, j, k))))
                    {
                        return null;
                    }
                }
            }
        }

        return new BlockPattern.PatternHelper(pos, finger, thumb, lcache);
    }

    /**
     * Calculates whether the given world position matches the pattern. Warning, fairly heavy function. @return a
     * BlockPattern.PatternHelper if found, null otherwise.
     */
    public BlockPattern.PatternHelper match(World worldIn, BlockPos pos)
    {
        LoadingCache loadingcache = CacheBuilder.newBuilder().build(new BlockPattern.CacheLoader(worldIn));
        int i = Math.max(Math.max(this.palmLength, this.thumbLength), this.fingerLength);
        Iterator iterator = BlockPos.getAllInBox(pos, pos.add(i - 1, i - 1, i - 1)).iterator();

        while (iterator.hasNext())
        {
            BlockPos blockpos1 = (BlockPos)iterator.next();
            EnumFacing[] aenumfacing = EnumFacing.values();
            int j = aenumfacing.length;

            for (int k = 0; k < j; ++k)
            {
                EnumFacing enumfacing = aenumfacing[k];
                EnumFacing[] aenumfacing1 = EnumFacing.values();
                int l = aenumfacing1.length;

                for (int i1 = 0; i1 < l; ++i1)
                {
                    EnumFacing enumfacing1 = aenumfacing1[i1];

                    if (enumfacing1 != enumfacing && enumfacing1 != enumfacing.getOpposite())
                    {
                        BlockPattern.PatternHelper patternhelper = this.checkPatternAt(blockpos1, enumfacing, enumfacing1, loadingcache);

                        if (patternhelper != null)
                        {
                            return patternhelper;
                        }
                    }
                }
            }
        }

        return null;
    }

    /**
     * Offsets the position of pos in the direction of finger and thumb facing by offset amounts, follows the right-hand
     * rule for cross products (finger, thumb, palm) @return A new BlockPos offset in the facing directions
     *  
     * @param pos The original block position
     * @param finger Finger direction
     * @param thumb Thumb direction
     * @param palmOffset An amount to offset in the palm direction. Palm is the direction of the result of the cross-
     * product between finger and thumb
     * @param thumbOffset An amount to offset in the thumb direction
     * @param fingerOffset An amount to offset in the finger direction
     */
    protected static BlockPos translateOffset(BlockPos pos, EnumFacing finger, EnumFacing thumb, int palmOffset, int thumbOffset, int fingerOffset)
    {
        if (finger != thumb && finger != thumb.getOpposite())
        {
            Vec3i vec3i = new Vec3i(finger.getFrontOffsetX(), finger.getFrontOffsetY(), finger.getFrontOffsetZ());
            Vec3i vec3i1 = new Vec3i(thumb.getFrontOffsetX(), thumb.getFrontOffsetY(), thumb.getFrontOffsetZ());
            Vec3i vec3i2 = vec3i.crossProduct(vec3i1);
            return pos.add(vec3i1.getX() * -thumbOffset + vec3i2.getX() * palmOffset + vec3i.getX() * fingerOffset, vec3i1.getY() * -thumbOffset + vec3i2.getY() * palmOffset + vec3i.getY() * fingerOffset, vec3i1.getZ() * -thumbOffset + vec3i2.getZ() * palmOffset + vec3i.getZ() * fingerOffset);
        }
        else
        {
            throw new IllegalArgumentException("Invalid forwards & up combination");
        }
    }

    static class CacheLoader extends com.google.common.cache.CacheLoader
        {
            private final World world;
            private static final String __OBFID = "CL_00002023";

            public CacheLoader(World worldIn)
            {
                this.world = worldIn;
            }

            public BlockWorldState loadState(BlockPos pos)
            {
                return new BlockWorldState(this.world, pos);
            }

            public Object load(Object p_load_1_)
            {
                return this.loadState((BlockPos)p_load_1_);
            }
        }

    public static class PatternHelper
        {
            private final BlockPos pos;
            private final EnumFacing finger;
            private final EnumFacing thumb;
            private final LoadingCache lcache;
            private static final String __OBFID = "CL_00002022";

            public PatternHelper(BlockPos pos, EnumFacing fingerIn, EnumFacing thumbIn, LoadingCache loadingCache)
            {
                this.pos = pos;
                this.finger = fingerIn;
                this.thumb = thumbIn;
                this.lcache = loadingCache;
            }

            public EnumFacing getFinger()
            {
                return this.finger;
            }

            public EnumFacing getThumb()
            {
                return this.thumb;
            }

            public BlockWorldState translateOffset(int palmOffset, int thumbOffset, int fingerOffset)
            {
                return (BlockWorldState)this.lcache.getUnchecked(BlockPattern.translateOffset(this.pos, this.getFinger(), this.getThumb(), palmOffset, thumbOffset, fingerOffset));
            }
        }
}