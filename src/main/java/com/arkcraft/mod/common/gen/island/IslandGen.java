package com.arkcraft.mod.common.gen.island;

import com.arkcraft.mod.common.ARKCraft;
import net.minecraft.world.biome.BiomeGenBase;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author gegy1000
 */
public class IslandGen
{
    public static short[][] heightmap, biomemap;
    public static int width, height, biomeWidth, biomeHeight;

    private static Map<Integer, BiomeGenBase> biomes = new HashMap<Integer, BiomeGenBase>();

    private static double biomemapToHeightmapWidthRatio, biomemapToHeightmapHeightRatio;

    public static void loadHeightmap()
    {
        System.out.println("Loading Island Heightmap...");

        try
        {
            BufferedImage image = ImageIO.read(ARKCraft.class.getResourceAsStream("/assets/arkcraft/textures/map/theisland/heightmap/theisland_heightmap.png"));

            width = image.getWidth();
            height = image.getHeight();

            heightmap = new short[width][height];

            Random random = new Random(Long.MAX_VALUE);

            for (int y = 0; y < height; y++)
            {
                for (int x = 0; x < width; x++)
                {
                    short height = (short) (image.getRGB(x, y) & 0x0000FF);

                    if (height <= 1)
                    {
                        heightmap[x][y] = (short) ((random.nextInt(5) + 25));
                    }
                    else
                    {
                        heightmap[x][y] = (short) ((height / ((2.0F - getScaled(height, 255, 2)) + 1.0F)) + 20);
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void loadBiomemap()
    {
        System.out.println("Loading Island Biomemap..");

        biomes.put(0x006000, BiomeGenBase.forest);
        biomes.put(0x007F7F, BiomeGenBase.swampland);
        biomes.put(0xFFFFFF, BiomeGenBase.icePlains);
        biomes.put(0x9393C4, BiomeGenBase.frozenOcean);

        try
        {
            BufferedImage biomeImage = ImageIO.read(ARKCraft.class.getResourceAsStream("/assets/arkcraft/textures/map/theisland/biomemap/theisland_biomemap.png"));

            biomeWidth = biomeImage.getWidth();
            biomeHeight = biomeImage.getHeight();

            biomemap = new short[biomeWidth][biomeHeight];

            for (int y = 0; y < biomeHeight; y++)
            {
                for (int x = 0; x < biomeWidth; x++)
                {
                    biomemap[x][y] = (short) BiomeGenBase.ocean.biomeID;

                    for (Map.Entry<Integer, BiomeGenBase> entry : IslandGen.biomes.entrySet())
                    {
                        if (isSortaLikeColour(biomeImage.getRGB(x, y), entry.getKey()))
                        {
                            biomemap[x][y] = (short) entry.getValue().biomeID;
                            break;
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        biomemapToHeightmapHeightRatio = (double) biomeHeight / (double) height;
        biomemapToHeightmapWidthRatio = (double) biomeWidth / (double) width;
    }

    private static boolean isSortaLikeColour(int colour, int desiredColour)
    {
        int r1 = (colour) & 0xFF;
        int g1 = (colour >> 8) & 0xFF;
        int b1 = (colour >> 16) & 0xFF;

        int r2 = (desiredColour) & 0xFF;
        int g2 = (desiredColour >> 8) & 0xFF;
        int b2 = (desiredColour >> 16) & 0xFF;

        return Math.abs(r1 - r2) + Math.abs(g1 - g2) + Math.abs(b1 - b2) < 10;
    }

    public static float getScaled(float value, float maxValue, float scale)
    {
        return maxValue != 0 && value != 0 ? value * scale / maxValue : 0;
    }

    private static int interpolate(short[][] pixels, int newX, int newY, int originalWidth, int originalHeight, int newWidth, int newHeight)
    {
        float xRatio = ((float) (originalWidth - 1)) / newWidth;
        float yRatio = ((float) (originalHeight - 1)) / newHeight;

        int originalX = (int) (xRatio * newX);
        int originalY = (int) (yRatio * newY);
        float diffX = (xRatio * newX) - originalX;
        float diffY = (yRatio * newY) - originalY;

        int a = (pixels[originalX][originalY] & 0xff);
        int b = (pixels[originalX + 1][originalY] & 0xff);
        int c = (pixels[originalX][originalY + 1] & 0xff);
        int d = pixels[originalX + 1][originalY + 1] & 0xff;

        return (int) (a * (1 - diffX) * (1 - diffY) + b * (diffX) * (1 - diffY) + c * (diffY) * (1 - diffX) + d * (diffX * diffY));
    }

    public static BiomeGenBase getBiomeAt(int x, int z, double scale)
    {
        int scaledWidth = (int) (width * scale);
        int scaledHeight = (int) (height * scale);

        if (x < 0 || z < 0 || x >= scaledWidth || z >= scaledHeight)
        {
            return BiomeGenBase.ocean;
        }

        double newX = ((x) * biomemapToHeightmapWidthRatio / scale);
        double newZ = ((z) * biomemapToHeightmapHeightRatio / scale);

        return BiomeGenBase.getBiome(biomemap[((int) newX)][((int) newZ)]);
    }

    public static int getHeightForCoords(int x, int z, double scale)
    {
        int scaledWidth = (int) (width * scale);
        int scaledHeight = (int) (height * scale);

        if (x < 0 || z < 0 || x >= scaledWidth || z >= scaledHeight)
        {
            return 10;
        }

        return interpolate(heightmap, x, z, width, height, scaledWidth, scaledHeight);
    }
}
