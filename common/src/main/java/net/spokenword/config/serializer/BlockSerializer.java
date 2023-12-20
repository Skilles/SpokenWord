package net.spokenword.config.serializer;

import com.google.gson.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.lang.reflect.Type;

public class BlockSerializer
        implements JsonSerializer<Block>, JsonDeserializer<Block>
{

    @Override
    public JsonElement serialize(Block src, Type typeOfSrc, JsonSerializationContext context)
    {
        return new JsonPrimitive(BuiltInRegistries.BLOCK.getKey(src).toString());
    }

    @Override
    public Block deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        var entityId = new ResourceLocation(json.getAsString());
        return BuiltInRegistries.BLOCK.get(entityId);
    }

}
