package net.spokenword.config;

import dev.architectury.platform.Platform;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.spokenword.config.serializer.BlockSerializer;
import net.spokenword.config.serializer.EntityTypeSerializer;
import net.spokenword.config.serializer.OptionalSerializer;
import net.spokenword.config.serializer.SpokenWordConfigHandler;

import java.util.Optional;

public class SpokenWordConfigScreen {

    private static final ConfigClassHandler<SpokenWordConfig> HANDLER =
            SpokenWordConfigHandler.createBuilder()
                                   .serializer(config -> GsonConfigSerializerBuilder.create(config)
                                                                               .setJson5(true)
                                                                               .appendGsonBuilder(builder -> builder
                                                                                       .registerTypeAdapter(Block.class, new BlockSerializer())
                                                                                       .registerTypeAdapter(EntityType.class, new EntityTypeSerializer())
                                                                                       .registerTypeAdapter(Optional.class, new OptionalSerializer())) // this serializer is unused but otherwise block serializer does not get used???
                                                                               .setPath(Platform.getConfigFolder().resolve(SpokenWordConfig.FILE_NAME))
                                                                               .build())
                                   .build();

    public static Screen create(Screen parent) {
        return HANDLER.generateGui().generateScreen(parent);
    }

    public static ConfigClassHandler<SpokenWordConfig> getHandler() {
        return HANDLER;
    }
}
