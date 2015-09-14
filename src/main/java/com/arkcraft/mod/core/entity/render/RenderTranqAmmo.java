package com.arkcraft.mod.core.entity.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.arkcraft.mod.core.Main;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class RenderTranqAmmo extends Render
{
	private static final ResourceLocation texture = new ResourceLocation(Main.MODID, "textures/models/item/tranqAmmo.png");

	public RenderTranqAmmo() 
	{
		super(Minecraft.getMinecraft().getRenderManager());
	}

	@Override
	public void doRender(Entity entity, double d0, double d1, double d2,
			float f, float f1) 
	{
	
		int red = 120;
		int green = 40;
		int blue = 40;
		int alpha = 120;
		
        this.bindEntityTexture(entity);
        GL11.glPushMatrix();
        
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        
        GL11.glTranslatef((float)d0, (float)d1, (float)d2);
        GL11.glRotatef(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * f1 - 90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * f1, 0.0F, 0.0F, 1.0F);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        byte b0 = 0;
        float f2 = 0.0F;
        float f3 = 0.5F;
        float f4 = (float)(0 + b0 * 10) / 32.0F;
        float f5 = (float)(5 + b0 * 10) / 32.0F;
        float f6 = 0.0F;
        float f7 = 0.15625F;
        float f8 = (float)(5 + b0 * 10) / 32.0F;
        float f9 = (float)(10 + b0 * 10) / 32.0F;
        float f10 = 0.05625F;
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        float f11 = - f1;

        if (f11 > 0.0F)
        {
            float f12 = -MathHelper.sin(f11 * 3.0F) * f11;
            GL11.glRotatef(f12, 0.0F, 0.0F, 1.0F);
        }

        GL11.glRotatef(45.0F, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(f10, f10, f10);
        GL11.glTranslatef(-4.0F, 0.0F, 0.0F);
        GL11.glNormal3f(f10, 0.0F, 0.0F);


        for (int i = 0; i < 4; ++i)
        {
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glNormal3f(0.0F, 0.0F, f10);
            worldrenderer.startDrawingQuads();
            worldrenderer.setColorRGBA(red, green, blue, alpha);
            worldrenderer.addVertex(-8.0D, -2.0D, 0.0D);
            worldrenderer.addVertex(8.0D, -2.0D, 0.0D);
            worldrenderer.addVertex(8.0D, 2.0D, 0.0D);
            worldrenderer.addVertex(-8.0D, 2.0D, 0.0D);
            tessellator.draw();
        }

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
		
        
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
    /*

    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return this.getEntityTexture((EntityTranqAmmo)entity);
    }
    
    public void doRender(Entity entity, double x, double y, double z, float p_76986_8_, float partialTicks)
    {
        this.doRender((EntityTranqAmmo)entity, x, y, z, p_76986_8_, partialTicks);
    }	*/

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		
		return texture;
	}

}

	 




