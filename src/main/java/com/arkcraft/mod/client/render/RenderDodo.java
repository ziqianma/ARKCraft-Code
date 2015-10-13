package com.arkcraft.mod.client.render;

import com.arkcraft.mod.common.ARKCraft;
import com.arkcraft.mod.common.entity.passive.EntityDodo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

/***
 * 
 * @author wildbill22
 *
 */
public class RenderDodo extends RenderLiving {
	private static final ResourceLocation dodoTexture = new ResourceLocation(ARKCraft.MODID + ":textures/model/dodo.png");

	public RenderDodo(ModelBase base, float par2) {
		super(Minecraft.getMinecraft().getRenderManager(), base, par2);
	}

	// Stuff to make the Dodo rotate when floating?
    protected float func_180569_a(EntityDodo entity, float par2) {
        float f1 = entity.field_70888_h + (entity.field_70886_e - entity.field_70888_h) * par2;
        float f2 = entity.field_70884_g + (entity.destPos - entity.field_70884_g) * par2;
        return (MathHelper.sin(f1) + 1.0F) * f2;
    }

    /**
     * Defines what float the third param in setRotationAngles of ModelBase is
     */
	@Override
    protected float handleRotationFloat(EntityLivingBase entity, float par2) {
        return this.func_180569_a((EntityDodo)entity, par2);
    }

    /**
     * Returns the location of an entity's texture.
     */
	@Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return dodoTexture;
    }
}
