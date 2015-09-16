package net.minecraft.world.storage;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S34PacketMaps;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec4b;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

public class MapData extends WorldSavedData
{
    public int xCenter;
    public int zCenter;
    public int dimension; //FML byte -> int
    public byte scale;
    /** colours */
    public byte[] colors = new byte[16384];
    /** Holds a reference to the MapInfo of the players who own a copy of the map */
    public List playersArrayList = Lists.newArrayList();
    /** Holds a reference to the players who own a copy of the map and a reference to their MapInfo */
    private Map playersHashMap = Maps.newHashMap();
    public Map playersVisibleOnMap = Maps.newLinkedHashMap();
    private static final String __OBFID = "CL_00000577";

    public MapData(String p_i2140_1_)
    {
        super(p_i2140_1_);
    }

    public void func_176054_a(double p_176054_1_, double p_176054_3_, int p_176054_5_)
    {
        int j = 128 * (1 << p_176054_5_);
        int k = MathHelper.floor_double((p_176054_1_ + 64.0D) / (double)j);
        int l = MathHelper.floor_double((p_176054_3_ + 64.0D) / (double)j);
        this.xCenter = k * j + j / 2 - 64;
        this.zCenter = l * j + j / 2 - 64;
    }

    /**
     * reads in data from the NBTTagCompound into this MapDataBase
     */
    public void readFromNBT(NBTTagCompound nbt)
    {
        net.minecraft.nbt.NBTBase dimension = nbt.getTag("dimension");

        if (dimension instanceof net.minecraft.nbt.NBTTagByte)
        {
            this.dimension = ((net.minecraft.nbt.NBTTagByte)dimension).getByte();
        }
        else
        {
            this.dimension = ((net.minecraft.nbt.NBTTagInt)dimension).getInt();
        }

        this.xCenter = nbt.getInteger("xCenter");
        this.zCenter = nbt.getInteger("zCenter");
        this.scale = nbt.getByte("scale");
        this.scale = (byte)MathHelper.clamp_int(this.scale, 0, 4);
        short short1 = nbt.getShort("width");
        short short2 = nbt.getShort("height");

        if (short1 == 128 && short2 == 128)
        {
            this.colors = nbt.getByteArray("colors");
        }
        else
        {
            byte[] abyte = nbt.getByteArray("colors");
            this.colors = new byte[16384];
            int i = (128 - short1) / 2;
            int j = (128 - short2) / 2;

            for (int k = 0; k < short2; ++k)
            {
                int l = k + j;

                if (l >= 0 || l < 128)
                {
                    for (int i1 = 0; i1 < short1; ++i1)
                    {
                        int j1 = i1 + i;

                        if (j1 >= 0 || j1 < 128)
                        {
                            this.colors[j1 + l * 128] = abyte[i1 + k * short1];
                        }
                    }
                }
            }
        }
    }

    /**
     * write data to NBTTagCompound from this MapDataBase, similar to Entities and TileEntities
     */
    public void writeToNBT(NBTTagCompound nbt)
    {
        nbt.setInteger("dimension", this.dimension);
        nbt.setInteger("xCenter", this.xCenter);
        nbt.setInteger("zCenter", this.zCenter);
        nbt.setByte("scale", this.scale);
        nbt.setShort("width", (short)128);
        nbt.setShort("height", (short)128);
        nbt.setByteArray("colors", this.colors);
    }

    /**
     * Adds the player passed to the list of visible players and checks to see which players are visible
     */
    public void updateVisiblePlayers(EntityPlayer player, ItemStack mapStack)
    {
        if (!this.playersHashMap.containsKey(player))
        {
            MapData.MapInfo mapinfo = new MapData.MapInfo(player);
            this.playersHashMap.put(player, mapinfo);
            this.playersArrayList.add(mapinfo);
        }

        if (!player.inventory.hasItemStack(mapStack))
        {
            this.playersVisibleOnMap.remove(player.getName());
        }

        for (int i = 0; i < this.playersArrayList.size(); ++i)
        {
            MapData.MapInfo mapinfo1 = (MapData.MapInfo)this.playersArrayList.get(i);

            if (!mapinfo1.entityplayerObj.isDead && (mapinfo1.entityplayerObj.inventory.hasItemStack(mapStack) || mapStack.isOnItemFrame()))
            {
                if (!mapStack.isOnItemFrame() && mapinfo1.entityplayerObj.dimension == this.dimension)
                {
                    this.updatePlayersVisibleOnMap(0, mapinfo1.entityplayerObj.worldObj, mapinfo1.entityplayerObj.getName(), mapinfo1.entityplayerObj.posX, mapinfo1.entityplayerObj.posZ, (double)mapinfo1.entityplayerObj.rotationYaw);
                }
            }
            else
            {
                this.playersHashMap.remove(mapinfo1.entityplayerObj);
                this.playersArrayList.remove(mapinfo1);
            }
        }

        if (mapStack.isOnItemFrame())
        {
            EntityItemFrame entityitemframe = mapStack.getItemFrame();
            BlockPos blockpos = entityitemframe.func_174857_n();
            this.updatePlayersVisibleOnMap(1, player.worldObj, "frame-" + entityitemframe.getEntityId(), (double)blockpos.getX(), (double)blockpos.getZ(), (double)(entityitemframe.field_174860_b.getHorizontalIndex() * 90));
        }

        if (mapStack.hasTagCompound() && mapStack.getTagCompound().hasKey("Decorations", 9))
        {
            NBTTagList nbttaglist = mapStack.getTagCompound().getTagList("Decorations", 10);

            for (int j = 0; j < nbttaglist.tagCount(); ++j)
            {
                NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(j);

                if (!this.playersVisibleOnMap.containsKey(nbttagcompound.getString("id")))
                {
                    this.updatePlayersVisibleOnMap(nbttagcompound.getByte("type"), player.worldObj, nbttagcompound.getString("id"), nbttagcompound.getDouble("x"), nbttagcompound.getDouble("z"), nbttagcompound.getDouble("rot"));
                }
            }
        }
    }

    private void updatePlayersVisibleOnMap(int p_82567_1_, World worldIn, String p_82567_3_, double p_82567_4_, double p_82567_6_, double p_82567_8_)
    {
        int j = 1 << this.scale;
        float f = (float)(p_82567_4_ - (double)this.xCenter) / (float)j;
        float f1 = (float)(p_82567_6_ - (double)this.zCenter) / (float)j;
        byte b0 = (byte)((int)((double)(f * 2.0F) + 0.5D));
        byte b1 = (byte)((int)((double)(f1 * 2.0F) + 0.5D));
        byte b3 = 63;
        byte b2;

        if (f >= (float)(-b3) && f1 >= (float)(-b3) && f <= (float)b3 && f1 <= (float)b3)
        {
            p_82567_8_ += p_82567_8_ < 0.0D ? -8.0D : 8.0D;
            b2 = (byte)((int)(p_82567_8_ * 16.0D / 360.0D));

            if (worldIn.provider.shouldMapSpin(p_82567_3_, p_82567_4_, p_82567_6_, p_82567_8_))
            {
                int k = (int)(worldIn.getWorldInfo().getWorldTime() / 10L);
                b2 = (byte)(k * k * 34187121 + k * 121 >> 15 & 15);
            }
        }
        else
        {
            if (Math.abs(f) >= 320.0F || Math.abs(f1) >= 320.0F)
            {
                this.playersVisibleOnMap.remove(p_82567_3_);
                return;
            }

            p_82567_1_ = 6;
            b2 = 0;

            if (f <= (float)(-b3))
            {
                b0 = (byte)((int)((double)(b3 * 2) + 2.5D));
            }

            if (f1 <= (float)(-b3))
            {
                b1 = (byte)((int)((double)(b3 * 2) + 2.5D));
            }

            if (f >= (float)b3)
            {
                b0 = (byte)(b3 * 2 + 1);
            }

            if (f1 >= (float)b3)
            {
                b1 = (byte)(b3 * 2 + 1);
            }
        }

        this.playersVisibleOnMap.put(p_82567_3_, new Vec4b((byte)p_82567_1_, b0, b1, b2));
    }

    public Packet getMapPacket(ItemStack mapStack, World worldIn, EntityPlayer player)
    {
        MapData.MapInfo mapinfo = (MapData.MapInfo)this.playersHashMap.get(player);
        return mapinfo == null ? null : mapinfo.getPacket(mapStack);
    }

    public void updateMapData(int p_176053_1_, int p_176053_2_)
    {
        super.markDirty();
        Iterator iterator = this.playersArrayList.iterator();

        while (iterator.hasNext())
        {
            MapData.MapInfo mapinfo = (MapData.MapInfo)iterator.next();
            mapinfo.update(p_176053_1_, p_176053_2_);
        }
    }

    public MapData.MapInfo getMapInfo(EntityPlayer p_82568_1_)
    {
        MapData.MapInfo mapinfo = (MapData.MapInfo)this.playersHashMap.get(p_82568_1_);

        if (mapinfo == null)
        {
            mapinfo = new MapData.MapInfo(p_82568_1_);
            this.playersHashMap.put(p_82568_1_, mapinfo);
            this.playersArrayList.add(mapinfo);
        }

        return mapinfo;
    }

    public class MapInfo
    {
        /** Reference for EntityPlayer object in MapInfo */
        public final EntityPlayer entityplayerObj;
        private boolean field_176105_d = true;
        private int minX = 0;
        private int minY = 0;
        private int maxX = 127;
        private int maxY = 127;
        private int field_176109_i;
        public int field_82569_d;
        private static final String __OBFID = "CL_00000578";

        public MapInfo(EntityPlayer p_i2138_2_)
        {
            this.entityplayerObj = p_i2138_2_;
        }

        public Packet getPacket(ItemStack p_176101_1_)
        {
            if (this.field_176105_d)
            {
                this.field_176105_d = false;
                return new S34PacketMaps(p_176101_1_.getMetadata(), MapData.this.scale, MapData.this.playersVisibleOnMap.values(), MapData.this.colors, this.minX, this.minY, this.maxX + 1 - this.minX, this.maxY + 1 - this.minY);
            }
            else
            {
                return this.field_176109_i++ % 5 == 0 ? new S34PacketMaps(p_176101_1_.getMetadata(), MapData.this.scale, MapData.this.playersVisibleOnMap.values(), MapData.this.colors, 0, 0, 0, 0) : null;
            }
        }

        public void update(int p_176102_1_, int p_176102_2_)
        {
            if (this.field_176105_d)
            {
                this.minX = Math.min(this.minX, p_176102_1_);
                this.minY = Math.min(this.minY, p_176102_2_);
                this.maxX = Math.max(this.maxX, p_176102_1_);
                this.maxY = Math.max(this.maxY, p_176102_2_);
            }
            else
            {
                this.field_176105_d = true;
                this.minX = p_176102_1_;
                this.minY = p_176102_2_;
                this.maxX = p_176102_1_;
                this.maxY = p_176102_2_;
            }
        }
    }
}