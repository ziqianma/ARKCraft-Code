package com.arkcraft.mod2.client.render;

import com.arkcraft.mod.common.ARKCraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderStoneSpear extends Render {
	
	private static final ResourceLocation spearTextures = new ResourceLocation(ARKCraft.MODID + ":textures/entity/stone_spear.png");
    
    public RenderStoneSpear() 
	{
		super(Minecraft.getMinecraft().getRenderManager());
	}
    
	public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
		this.getEntityTexture(p_76986_1_);
		GL11.glPushMatrix();

		GL11.glTranslatef((float) p_76986_2_, (float) p_76986_4_, (float) p_76986_6_);
		GL11.glRotatef(p_76986_1_.prevRotationYaw + (p_76986_1_.rotationYaw - p_76986_1_.prevRotationYaw) * p_76986_9_ - 90.0F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(p_76986_1_.prevRotationPitch + (p_76986_1_.rotationPitch - p_76986_1_.prevRotationPitch) * p_76986_9_, 0.0F, 0.0F, 1.0F);

		GL11.glScalef(3F, 3F, 3F);

		Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

		float su1 = 0;
		float sv1 = 2F / 32F;
		float su2 = 19F / 32F;
		float sv2 = 3F / 32F;

		float f10 = 0.05625F;
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);

		GL11.glRotatef(45.0F, 1.0F, 0.0F, 0.0F);
		GL11.glScalef(f10, f10, f10);
		GL11.glTranslatef(-4.0F, 0.0F, 0.0F);

		for (int i = 0; i < 4; ++i) {
			GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
			GL11.glNormal3f(0.0F, 0.0F, f10);
			worldrenderer.startDrawingQuads();
			worldrenderer.addVertexWithUV(-10.0D, -2.0D, 0.0D, su1, sv1);
			worldrenderer.addVertexWithUV(10.0D, -2.0D, 0.0D, su2, sv1);
			worldrenderer.addVertexWithUV(10.0D, 2.0D, 0.0D, su2, sv2);
			worldrenderer.addVertexWithUV(-10.0D, 2.0D, 0.0D, su1, sv2);
			tessellator.draw();
		}

		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return spearTextures;
	}
}