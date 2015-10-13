package com.arkcraft.mod.client.render;

import com.arkcraft.mod.common.ARKCraft;
import com.arkcraft.mod.common.entity.aggressive.EntityRaptor;
import com.arkcraft.mod.common.entity.aggressive.RaptorType;
import com.arkcraft.mod.client.model.ModelRaptor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

/***
 * 
 * @author Vastatio
 *
 */
public class RenderRaptor extends RenderLiving {
	private static ResourceLocation[] texture;
	protected ModelRaptor modelEntity;

	public RenderRaptor(ModelBase base, float par2) {
		super(Minecraft.getMinecraft().getRenderManager(), base, par2);
		modelEntity = ((ModelRaptor) mainModel);
		texture = new ResourceLocation[RaptorType.numRaptors];
		texture[0] = new ResourceLocation(ARKCraft.MODID + ":textures/model/raptor_albino.png");
		texture[1] = new ResourceLocation(ARKCraft.MODID + ":textures/model/raptor_breen_white.png");
		texture[2] = new ResourceLocation(ARKCraft.MODID + ":textures/model/raptor_cyan_lgreen.png");
		texture[3] = new ResourceLocation(ARKCraft.MODID + ":textures/model/raptor_gay_gay.png");
		texture[4] = new ResourceLocation(ARKCraft.MODID + ":textures/model/raptor_green_grey.png");
		texture[5] = new ResourceLocation(ARKCraft.MODID + ":textures/model;raptor_green_tan.png");
		texture[6] = new ResourceLocation(ARKCraft.MODID + ":textures/model/raptor_green_white.png");
		texture[7] = new ResourceLocation(ARKCraft.MODID + ":textures/model/raptor_grey_grey.png");
		texture[8] = new ResourceLocation(ARKCraft.MODID + ":textures/model/raptor_lbrown_tan.png");
		texture[9] = new ResourceLocation(ARKCraft.MODID + ":textures/model/raptor_red_tan.png");
		texture[10] = new ResourceLocation(ARKCraft.MODID + ":textures/model/raptor_tan_white.png");
	}

	public void render(EntityRaptor entity, double x, double y, double z, float u, float v) {
		super.doRender(entity, x, y, z, u, v);
	}

	public void doRenderLiving(EntityLiving entity, double x, double y, double z, float u, float v) {
		render((EntityRaptor) entity, x, y, z, u, v);
	}

	public void doRender(Entity entity, double x, double y, double z, float u, float v) {
		render((EntityRaptor) entity, x, y, z, u, v);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity e) {
		return texture[((EntityRaptor)e).raptorType];
	}
}
