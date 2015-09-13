package com.arkcraft.mod.core.entity.render;

import org.lwjgl.opengl.GL11;

import com.arkcraft.mod.core.Main;
import com.arkcraft.mod.core.entity.EntityTranqAmmo;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderTranqAmmo extends Render
{
	private WorldRenderer worldRenderer;
    private WorldVertexBufferUploader vboUploader = new WorldVertexBufferUploader();
	private static final ResourceLocation texture = new ResourceLocation(Main.MODID, "textures//.png");

	public RenderTranqAmmo(RenderManager renderManager) 
	{
		super(renderManager);
	}
	
    public WorldRenderer getWorldRenderer()
    {
        return this.worldRenderer;
    }
    
    public int draw()
    {
        return this.vboUploader.draw(this.worldRenderer, this.worldRenderer.finishDrawing());
    }
    
	public void render(EntityTranqAmmo ammo, double d, double d1, double d2, float f, float f1)
	{
		bindEntityTexture(ammo);
		GL11.glPushMatrix();
		GL11.glTranslatef((float) d, (float) d1, (float) d2);
		Tessellator tessellator = Tessellator.getInstance();
		float f2 = 0.0F;
		float f3 = 5F / 16F;
		float f10 = 0.05625F;
		GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
		GL11.glScalef(0.07F, 0.07F, 0.07F);
		GL11.glNormal3f(f10, 0.0F, 0.0F);
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertexWithUV(0D, -1D, -1D, f2, f2);
		worldRenderer.addVertexWithUV(0D, -1D, 1D, f3, f2);
		worldRenderer.addVertexWithUV(0D, 1D, 1D, f3, f3);
		worldRenderer.addVertexWithUV(0D, 1D, -1D, f2, f3);
		tessellator.draw();
		GL11.glNormal3f(-f10, 0.0F, 0.0F);
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertexWithUV(0D, 1D, -1D, f2, f2);
		worldRenderer.addVertexWithUV(0D, 1D, 1D, f3, f2);
		worldRenderer.addVertexWithUV(0D, -1D, 1D, f3, f3);
		worldRenderer.addVertexWithUV(0D, -1D, -1D, f2, f3);
		tessellator.draw();
		for (int j = 0; j < 4; j++)
		{
			GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
			GL11.glNormal3f(0.0F, 0.0F, f10);
			worldRenderer.startDrawingQuads();
			worldRenderer.addVertexWithUV(-1D, -1D, 0.0D, f2, f2);
			worldRenderer.addVertexWithUV(1D, -1D, 0.0D, f3, f2);
			worldRenderer.addVertexWithUV(1D, 1D, 0.0D, f3, f3);
			worldRenderer.addVertexWithUV(-1D, 1D, 0.0D, f2, f3);
			tessellator.draw();
		}
		
		GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);
		GL11.glPopMatrix();
	}
	
	@Override
	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1)
	{
		render((EntityTranqAmmo) entity, d, d1, d2, f, f1);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(Entity entity) 
	{
		return texture;
	}
}

