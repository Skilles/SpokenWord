package com.skilles.spokenword.config;

import com.skilles.spokenword.exceptions.ConfigException;
import net.minecraft.world.entity.EntityType;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigUtil
{

    public static List<String> entitiesToKeys(Collection<EntityType<?>> entities)
    {
        return entities.stream().map(ConfigUtil::entityToString).toList();
    }

    public static String entityToString(EntityType<?> entity)
    {
        return EntityType.getKey(entity).toString();
    }

    public static Collection<EntityType<?>> keysToEntities(Collection<String> strings)
    {
        return strings.stream().map(ConfigUtil::stringToEntity).collect(Collectors.toSet());
    }

    public static EntityType<?> stringToEntity(String string)
    {
        return EntityType.byString(string).orElseThrow(() -> new ConfigException("Invalid entity type " + string));
    }

}
