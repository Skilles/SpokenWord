package net.spokenword.config;

import com.google.gson.GsonBuilder;
import dev.architectury.platform.Platform;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.spokenword.SpokenWord;
import net.spokenword.config.autoconfig.AutoConfig;

public class SpokenWordConfigBuilder
{
    private static final String CONFIG_FILE_NAME = "spokenword.json";

    public static ConfigClassHandler<SpokenWordConfig> HANDLER =
            ConfigClassHandler.createBuilder(SpokenWordConfig.class)
                    .id(new ResourceLocation(SpokenWord.MOD_ID))
                    .serializer(config -> GsonConfigSerializerBuilder.create(config)
                            .setJson5(true)
                            .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                            .setPath(Platform.getConfigFolder().resolve(CONFIG_FILE_NAME))
                            .build())
                    .build();

    /*public static YetAnotherConfigLib create() {
        var builder = YetAnotherConfigLib.createBuilder()
                .title(Component.literal("SpokenWord"));

        builder.save(HANDLER::save);

        return YetAnotherConfigLib.create(HANDLER, (defaults, config, builder) -> {
            builder.title(Component.literal("SpokenWord"));

            AutoConfig.initialize(defaults, config, builder);

            return builder;
        });
    }*/

    public static Screen createScreen(Screen parent) {
        //return create().generateScreen(parent);
        return HANDLER.generateGui().generateScreen(parent);
    }
}
