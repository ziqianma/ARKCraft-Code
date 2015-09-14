package com.arkcraft.mod.core.entity.render;

import org.lwjgl.opengl.GL11;

import com.arkcraft.mod.core.Main;
import com.arkcraft.mod.core.entity.model.ModelAmmo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderTranqAmmo extends Render
{
	private static final ResourceLocation texture = new ResourceLocation(Main.MODID, "textures/models/item/tranqAmmo.png");
	private final ModelAmmo model = new ModelAmmo();

	public RenderTranqAmmo() 
	{
		super(Minecraft.getMinecraft().getRenderManager());
	}

    @Override
	public void doRender(Entity var1, double var2, double var4, double var6, float var8, float var9) {
		GL11.glPushMatrix();
        GL11.glTranslatef((float)var2, (float)var4, (float)var6);
        
        GL11.glRotatef(var8, 0.0F, 1.0F, 0.0F);
        GL11.glScalef(1F, 1F, 1F);
		model.render(var1, 0, 0, 0, 0, 0, 0);
        GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) 
	{
		return texture;
	}
}

