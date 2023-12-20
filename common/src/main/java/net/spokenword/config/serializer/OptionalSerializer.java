package net.spokenword.config.serializer;

import com.google.gson.*;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

public class OptionalSerializer implements JsonSerializer<Optional<?>>, JsonDeserializer<Optional<?>> {

    @Override
    public Optional<?> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return Optional.of(jsonDeserializationContext.deserialize(jsonElement, ((ParameterizedType) type).getActualTypeArguments()[0]));
    }

    @Override
    public JsonElement serialize(Optional<?> o, Type type, JsonSerializationContext jsonSerializationContext) {
        return jsonSerializationContext.serialize(o.orElse(null));
    }
}
