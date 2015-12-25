package com.arkcraft.mod2.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class ModelLeftArm extends ModelBase
{

    public ModelLeftArm()
    {
    	textureWidth = 64;
    	textureWidth = 32;
        LeftArm = new ModelRenderer(this, 40, 16);
        LeftArm.showModel = true;
        LeftArm.setRotationPoint(5F, 2.0F, 0.0F);
        LeftArm.addBox(-1F, -2F, -2F, 4, 12, 4, 0.0F);
    }

    public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        GL11.glPushMatrix();
        GL11.glScaled(1.0D, 2.5D, 1.0D);
        LeftArm.render(f5);
        GL11.glPopMatrix();
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
    {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    public ModelRenderer LeftArm;
}