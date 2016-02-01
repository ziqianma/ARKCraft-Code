package com.arkcraft.module.creature.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

import com.arkcraft.module.core.ARKCraft;
import com.arkcraft.module.creature.client.model.ModelRaptorNew;
import com.arkcraft.module.creature.common.entity.aggressive.RaptorType;
import com.arkcraft.module.creature.common.entitytest.EntityRaptor;

/**
 * @author Vastatio
 */
public class RenderRaptor extends RenderLiving
{
	private static ResourceLocation[] texture;
	protected ModelRaptorNew modelEntity;
	private static final ResourceLocation raptorTexture = new ResourceLocation(
			ARKCraft.MODID + ":textures/model/raptor.png");

	public RenderRaptor(ModelBase base, float par2)
	{
		super(Minecraft.getMinecraft().getRenderManager(), base, par2);
		modelEntity = ((ModelRaptorNew) mainModel);
		texture = new ResourceLocation[RaptorType.numRaptors];
		texture[0] = new ResourceLocation(ARKCraft.MODID + ":textures/model/raptor_albino.png");
		texture[1] = new ResourceLocation(ARKCraft.MODID + ":textures/model/raptor_breen_white.png");
		texture[2] = new ResourceLocation(ARKCraft.MODID + ":textures/model/raptor_cyan_lgreen.png");
		texture[3] = new ResourceLocation(ARKCraft.MODID + ":textures/model/raptor_gay_gay.png");
		texture[4] = new ResourceLocation(ARKCraft.MODID + ":textures/model/raptor_green_grey.png");
		texture[5] = new ResourceLocation(ARKCraft.MODID + ":textures/model/raptor_green_tan.png");
		texture[6] = new ResourceLocation(ARKCraft.MODID + ":textures/model/raptor_green_white.png");
		texture[7] = new ResourceLocation(ARKCraft.MODID + ":textures/model/raptor_grey_grey.png");
		texture[8] = new ResourceLocation(ARKCraft.MODID + ":textures/model/raptor_lbrown_tan.png");
		texture[9] = new ResourceLocation(ARKCraft.MODID + ":textures/model/raptor_red_tan.png");
		texture[10] = new ResourceLocation(ARKCraft.MODID + ":textures/model/raptor_tan_white.png");
	}

	public void render(EntityRaptor entity, double x, double y, double z, float u, float v)
	{
		super.doRender(entity, x, y, z, u, v);
	}

	public void doRenderLiving(EntityLiving entity, double x, double y, double z, float u, float v)
	{
		render((EntityRaptor) entity, x, y, z, u, v);
	}

	public void doRender(Entity entity, double x, double y, double z, float u, float v)
	{
		render((EntityRaptor) entity, x, y, z, u, v);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity e)
	{
		return raptorTexture;
		/*
		 * EntityRaptor r = (EntityRaptor) e; int type = r.getRaptorType(); //
		 * return new ResourceLocation(ARKCraft.MODID + //
		 * ":textures/model/raptor.png"); // return (texture[type]);//
		 * texture[((EntityRaptor)e).raptorType];
		 */
	}
}
