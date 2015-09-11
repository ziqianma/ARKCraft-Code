package com.arkcraft.mod.core.book;

import java.lang.reflect.Type;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class EntityLivingBaseDeserializer implements JsonDeserializer<EntityLivingBase> {

	public EntityLivingBase deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		Class<? extends EntityLivingBase> clazz = PageData.getModelClass(json.getAsString());
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
