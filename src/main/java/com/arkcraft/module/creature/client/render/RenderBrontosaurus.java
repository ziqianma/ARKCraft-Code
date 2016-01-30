package com.arkcraft.module.creature.client.render;

import com.arkcraft.module.core.ARKCraft;
import com.arkcraft.module.core.common.entity.neutral.EntityBrontosaurus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

public class RenderBrontosaurus extends RenderLiving
{

    public static final ResourceLocation entityTexture = new ResourceLocation(ARKCraft.MODID, "textures/model/brontosaurus.png");

    public RenderBrontosaurus(ModelBase base, float par2)
    {
        super(Minecraft.getMinecraft().getRenderManager(), base, par2);
    }

    public void render(EntityBrontosaurus entity, double x, double y, double z, float u, float v)
    {
        super.doRender(entity, x, y, z, u, v);
    }

    public void doRenderLiving(EntityLiving entity, double x, double y, double z, float u, float v)
    {
        render((EntityBrontosaurus) entity, x, y, z, u, v);
    }

    public void doRender(Entity entity, double x, double y, double z, float u, float v)
    {
        render((EntityBrontosaurus) entity, x, y, z, u, v);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return entityTexture;
    }

}
