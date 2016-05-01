package com.arkcraft.module.creature.client.render;

import com.arkcraft.module.core.ARKCraft;
import com.arkcraft.module.creature.client.model.ModelRaptor;
import com.arkcraft.module.creature.common.entity.aggressive.RaptorType;
import com.arkcraft.module.creature.common.entity.test.EntityRaptor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

/**
 * @author Vastatio
 * @author Lewis McReu
 */
public class RenderRaptor extends RenderLiving
{
	private static final ResourceLocation[] textures = new ResourceLocation[RaptorType
			.values().length];
	protected ModelRaptor modelEntity;

	public RenderRaptor(ModelBase base, float par2)
	{
		super(Minecraft.getMinecraft().getRenderManager(), base, par2);
		modelEntity = ((ModelRaptor) mainModel);
		for (int i = 0; i < RaptorType.values().length; i++)
		{
			textures[i] = new ResourceLocation(
					ARKCraft.MODID + ":textures/model/" + RaptorType.values()[i].getResourceName());
		}
	}

	public void render(EntityRaptor entity, double x, double y, double z, float u, float v)
	{
		super.doRender(entity, x, y, z, u, v);
	}

	public void doRender(Entity entity, double x, double y, double z, float u, float v)
	{
		render((EntityRaptor) entity, x, y, z, u, v);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity e)
	{
		return textures[((EntityRaptor) e).getType().getRaptorTypeInt()];
	}
}
