package com.arkcraft.mod.client.gui.book.deserialization;

import java.lang.reflect.Type;

import com.arkcraft.mod.client.gui.book.pages.PageData;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

import com.arkcraft.mod.common.lib.LogHelper;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
/***
 * 
 * @author Vastatio
 *
 */
public class EntityLivingBaseDeserializer implements JsonDeserializer<EntityLivingBase> {

	public EntityLivingBase deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		Class<? extends EntityLivingBase> clazz = PageData.getModelClass(json.getAsString());
		LogHelper.info(clazz == null ? "EntityLivingBaseDeserializer: CLAZZ is null." : "EntityLivingBaseDeserializer: CLAZZ is not null.");
		if(clazz != null) {
			try {
				return clazz.getConstructor(World.class).newInstance(Minecraft.getMinecraft().theWorld);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
