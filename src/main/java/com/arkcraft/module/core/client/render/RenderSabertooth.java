package com.arkcraft.module.core.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

import com.arkcraft.module.core.ARKCraft;
import com.arkcraft.module.core.client.model.ModelSabertooth;
import com.arkcraft.module.core.common.entity.DodoType;
import com.arkcraft.module.core.common.entity.aggressive.EntitySabertooth;

/**
 * @author wildbill22
 */
public class RenderSabertooth extends RenderLiving
{
	private static ResourceLocation[] texture;
	protected ModelSabertooth modelEntity;
    private static final ResourceLocation saberTexture = new ResourceLocation(ARKCraft.MODID + ":textures/model/saber.png");

    public RenderSabertooth(ModelBase base, float par2)
    {
     //   super(Minecraft.getMinecraft().getRenderManager(), base, par2);
        super(Minecraft.getMinecraft().getRenderManager(), base, par2);
        modelEntity = ((ModelSabertooth) mainModel);
        texture = new ResourceLocation[DodoType.numDodos];
        texture[0] = new ResourceLocation(ARKCraft.MODID + ":textures/model/dodo_pink_brown.png");
   //     texture[1] = new ResourceLocation(ARKCraft.MODID + ":textures/model/dodo_pink_brown.png");

    }

    // Stuff to make the Dodo rotate when floating?
    public void render(EntitySabertooth entity, double x, double y, double z, float u, float v)
    {
        super.doRender(entity, x, y, z, u, v);
    }

    public void doRenderLiving(EntityLiving entity, double x, double y, double z, float u, float v)
    {
        render((EntitySabertooth) entity, x, y, z, u, v);
    }

    public void doRender(Entity entity, double x, double y, double z, float u, float v)
    {
        render((EntitySabertooth) entity, x, y, z, u, v);
    }
    /**
     * Returns the location of an entity's texture.
     */
    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return saberTexture;
    }
}
