package com.arkcraft.module.creature.init;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import com.arkcraft.module.blocks.common.entity.EntityDodoEgg;
import com.arkcraft.module.blocks.common.items.ARKCraftItems;
import com.arkcraft.module.creature.client.model.ModelBrontosaurus;
import com.arkcraft.module.creature.client.model.ModelDodo;
import com.arkcraft.module.creature.client.model.ModelRaptorNew;
import com.arkcraft.module.creature.client.model.ModelSabertooth;
import com.arkcraft.module.creature.client.render.RenderBrontosaurus;
import com.arkcraft.module.creature.client.render.RenderDodo;
import com.arkcraft.module.creature.client.render.RenderRaptor;
import com.arkcraft.module.creature.client.render.RenderSabertooth;
import com.arkcraft.module.creature.common.entity.aggressive.EntitySabertooth;
import com.arkcraft.module.creature.common.entity.neutral.EntityBrontosaurus;
import com.arkcraft.module.creature.common.entity.passive.EntityDodo;
import com.arkcraft.module.creature.common.entity.test.EntityRaptor;

public class Models
{
	public static void init()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityDodoEgg.class,
				new RenderSnowball(Minecraft.getMinecraft().getRenderManager(),
						ARKCraftItems.dodo_egg, Minecraft.getMinecraft()
								.getRenderItem()));
		RenderingRegistry.registerEntityRenderingHandler(EntityRaptor.class,
				new RenderRaptor(new ModelRaptorNew(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(
				EntitySabertooth.class, new RenderSabertooth(
						new ModelSabertooth(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntityDodo.class,
				new RenderDodo(new ModelDodo(), 0.3F));
		RenderingRegistry.registerEntityRenderingHandler(
				EntityBrontosaurus.class, new RenderBrontosaurus(
						new ModelBrontosaurus(), 0.5f));
	}
}
