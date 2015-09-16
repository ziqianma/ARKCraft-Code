package net.minecraft.world.storage;

import java.util.List;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.util.IProgressUpdate;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ISaveFormat
{
    /**
     * Returns the name of the save format.
     */
    @SideOnly(Side.CLIENT)
    String getName();

    /**
     * Returns back a loader for the specified save directory
     */
    ISaveHandler getSaveLoader(String p_75804_1_, boolean p_75804_2_);

    @SideOnly(Side.CLIENT)
    List getSaveList() throws AnvilConverterException;

    void flushCache();

    /**
     * Returns the world's WorldInfo object
     *  
     * @param saveName The name of the directory containing the world
     */
    @SideOnly(Side.CLIENT)
    WorldInfo getWorldInfo(String saveName);

    @SideOnly(Side.CLIENT)
    boolean func_154335_d(String p_154335_1_);

    /**
     * @args: Takes one argument - the name of the directory of the world to delete. @desc: Delete the world by deleting
     * the associated directory recursively.
     */
    boolean deleteWorldDirectory(String p_75802_1_);

    /**
     * Renames the world by storing the new name in level.dat. It does *not* rename the directory containing the world
     * data.
     *  
     * @param dirName The name of the directory containing the world.
     * @param newName The new name for the world.
     */
    @SideOnly(Side.CLIENT)
    void renameWorld(String dirName, String newName);

    @SideOnly(Side.CLIENT)
    boolean func_154334_a(String saveName);

    /**
     * gets if the map is old chunk saving (true) or McRegion (false)
     *  
     * @param saveName The name of the directory containing the world
     */
    boolean isOldMapFormat(String saveName);

    /**
     * converts the map to mcRegion
     *  
     * @param filename Filename for the level.dat_mcr backup
     */
    boolean convertMapFormat(String filename, IProgressUpdate p_75805_2_);

    /**
     * Return whether the given world can be loaded.
     */
    @SideOnly(Side.CLIENT)
    boolean canLoadWorld(String p_90033_1_);
}