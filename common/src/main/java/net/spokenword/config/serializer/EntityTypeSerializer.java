package net.spokenword.config.serializer;

import com.google.gson.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import java.lang.reflect.Type;

public class EntityTypeSerializer
        implements JsonSerializer<EntityType<?>>, JsonDeserializer<EntityType<?>> {

    @Override
    public JsonElement serialize(EntityType<?> src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(BuiltInRegistries.ENTITY_TYPE.getKey(src).toString());
    }

    @Override
    public EntityType<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        var entityId = new ResourceLocation(json.getAsString());
        return BuiltInRegistries.ENTITY_TYPE.get(entityId);
    }
}