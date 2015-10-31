package com.arkcraft.mod.client.model.override;

import net.ilexiconn.llibrary.client.render.IModelExtension;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author gegy1000
 */
public class PlayerModelOverride implements IModelExtension
{
    @Override
    public void setRotationAngles(ModelBiped model, float limbSwing, float limbSwingAmount, float rotationFloat, float rotationYaw, float rotationPitch, float partialTicks, Entity entity)
    {
    }

    @Override
    public void preRender(EntityPlayer entity, ModelBase model, float partialTicks)
    {

    }

    @Override
    public void postRender(EntityPlayer entity, ModelBase model, float partialTicks)
    {

    }

    @Override
    public void init(ModelBase model)
    {

    }
}
