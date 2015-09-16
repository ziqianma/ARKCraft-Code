package net.minecraft.client.resources.model;

import java.util.List;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BuiltInModel implements IBakedModel
{
    private ItemCameraTransforms cameraTransforms;
    private static final String __OBFID = "CL_00002392";

    public BuiltInModel(ItemCameraTransforms p_i46086_1_)
    {
        this.cameraTransforms = p_i46086_1_;
    }

    public List getFaceQuads(EnumFacing p_177551_1_)
    {
        return null;
    }

    public List getGeneralQuads()
    {
        return null;
    }

    public boolean isAmbientOcclusion()
    {
        return false;
    }

    public boolean isGui3d()
    {
        return true;
    }

    public boolean isBuiltInRenderer()
    {
        return true;
    }

    public TextureAtlasSprite getTexture()
    {
        return null;
    }

    public ItemCameraTransforms getItemCameraTransforms()
    {
        return this.cameraTransforms;
    }
}