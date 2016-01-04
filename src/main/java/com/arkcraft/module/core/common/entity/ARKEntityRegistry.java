package com.arkcraft.module.core.common.entity;

import com.arkcraft.module.core.ARKCraft;
import com.arkcraft.module.core.common.creature.Creature;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gegy1000
 */
public class ARKEntityRegistry
{
    private static Map<Class, Creature> creatureMap = new HashMap<Class, Creature>();

    public static void register()
    {

    }

    public static Creature getCreature(EntityARKCreature dino)
    {
        return creatureMap.get(dino.getClass());
    }

    public static void registerCreature(Creature creature)
    {
        Class<? extends EntityARKCreature> entityClass = creature.getEntityClass();

        creatureMap.put(entityClass, creature);

        int uniqueId = EntityRegistry.findGlobalUniqueEntityId();

        String entityName = creature.getName().replaceAll(" ", "");
        EntityRegistry.registerGlobalEntityID(entityClass, entityName, uniqueId);
        EntityRegistry.registerModEntity(entityClass, entityName, uniqueId, ARKCraft.instance, 256, 1, true);
    }
}
