package net.minecraft.client.resources.model;

import java.util.List;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/*
 * @deprecated use {@link net.minecraftforge.client.model.IFlexibleBakedModel}, {@link net.minecraftforge.client.model.IPerspectiveAwareModel}
 */
@SideOnly(Side.CLIENT)
@Deprecated
public interface IBakedModel
{
    List getFaceQuads(EnumFacing p_177551_1_);

    List getGeneralQuads();

    boolean isAmbientOcclusion();

    boolean isGui3d();

    boolean isBuiltInRenderer();

    TextureAtlasSprite getTexture();

    @Deprecated
    ItemCameraTransforms getItemCameraTransforms();
}