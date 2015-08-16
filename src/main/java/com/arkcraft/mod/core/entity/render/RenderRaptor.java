package com.arkcraft.mod.core.entity.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

import java.util.Random;

import com.arkcraft.mod.core.Main;
<<<<<<< .merge_file_dR0CCj
import com.arkcraft.mod.core.entity.EntityRaptor;
=======
import com.arkcraft.mod.core.entity.aggressive.EntityRaptor;
import com.arkcraft.mod.core.entity.aggressive.RaptorType;
>>>>>>> .merge_file_4tvEXm
import com.arkcraft.mod.core.entity.model.ModelRaptor;

/***
 * 
 * @author Vastatio
 *
 */
public class RenderRaptor extends RenderLiving {
<<<<<<< .merge_file_dR0CCj

	private static final ResourceLocation texture = new ResourceLocation(Main.MODID, "textures/model/raptor.png");
	private static final ResourceLocation texture_rainbow = new ResourceLocation(Main.MODID,
			"textures/model/raptor_rainbow.png");

=======
	private static ResourceLocation[] texture;
>>>>>>> .merge_file_4tvEXm
	protected ModelRaptor modelEntity;

	public RenderRaptor(ModelBase base, float par2) {
		super(Minecraft.getMinecraft().getRenderManager(), base, par2);
		modelEntity = ((ModelRaptor) mainModel);
		texture = new ResourceLocation[RaptorType.numRaptors];
		texture[0] = new ResourceLocation(Main.MODID + ":textures/model/raptor_albino.png");
		texture[1] = new ResourceLocation(Main.MODID + ":textures/model/raptor_breen_white.png");
		texture[2] = new ResourceLocation(Main.MODID + ":textures/model/raptor_cyan_lgreen.png");
		texture[3] = new ResourceLocation(Main.MODID + ":textures/model/raptor_gay_gay.png");
		texture[4] = new ResourceLocation(Main.MODID + ":textures/model/raptor_green_grey.png");
		texture[5] = new ResourceLocation(Main.MODID + ":textures/model;raptor_green_tan.png");
		texture[6] = new ResourceLocation(Main.MODID + ":textures/model/raptor_green_white.png");
		texture[7] = new ResourceLocation(Main.MODID + ":textures/model/raptor_grey_grey.png");
		texture[8] = new ResourceLocation(Main.MODID + ":textures/model/raptor_lbrown_tan.png");
		texture[9] = new ResourceLocation(Main.MODID + ":textures/model/raptor_red_tan.png");
		texture[10] = new ResourceLocation(Main.MODID + ":textures/model/raptor_tan_white.png");
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
<<<<<<< .merge_file_dR0CCj
		return new Random().nextInt(1000) == 1000 ? texture_rainbow : texture;
=======
		return texture[((EntityRaptor)e).raptorType];
>>>>>>> .merge_file_4tvEXm
	}

}
