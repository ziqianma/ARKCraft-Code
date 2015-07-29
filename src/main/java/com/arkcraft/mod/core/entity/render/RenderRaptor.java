package com.arkcraft.mod.core.entity.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

import com.arkcraft.mod.core.Main;
import com.arkcraft.mod.core.entity.EntityRaptor;
import com.arkcraft.mod.core.entity.model.ModelRaptor;

/***
 * 
 * @author Vastatio
 *
 */
public class RenderRaptor extends RenderLiving {

	private static final ResourceLocation texture = 
			new ResourceLocation(Main.MODID, "textures/model/raptor.png");
	
	protected ModelRaptor modelEntity;
	
	public RenderRaptor(ModelBase base, float par2) {
		super(Minecraft.getMinecraft().getRenderManager(), base, par2);
		modelEntity = ((ModelRaptor)mainModel);
	}
	
	
	public void render(EntityRaptor entity, double x, double y, double z, float u, float v) {
		super.doRender(entity, x, y, z, u, v);
	}
	
	public void doRenderLiving(EntityLiving entity, double x, double y, double z, float u, float v) {
		render((EntityRaptor)entity, x, y, z, u, v);
	}
	
	public void doRender(Entity entity, double x, double y, double z, float u, float v) {
		render((EntityRaptor)entity, x, y, z, u, v);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(Entity e) {
		return texture;
	}
	
}
