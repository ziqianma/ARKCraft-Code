package net.minecraft.client.resources;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IResourcePack
{
    InputStream getInputStream(ResourceLocation p_110590_1_) throws IOException;

    boolean resourceExists(ResourceLocation p_110589_1_);

    Set getResourceDomains();

    IMetadataSection getPackMetadata(IMetadataSerializer p_135058_1_, String p_135058_2_) throws IOException;

    BufferedImage getPackImage() throws IOException;

    String getPackName();
}