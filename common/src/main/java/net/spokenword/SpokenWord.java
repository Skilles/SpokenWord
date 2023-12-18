package net.spokenword;

import com.google.common.base.Suppliers;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.platform.Platform;
import dev.architectury.registry.registries.RegistrarManager;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.config.GsonConfigInstance;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.minecraft.resources.ResourceLocation;
import net.spokenword.config.SpokenWordConfig;
import net.spokenword.config.SpokenWordConfigBuilder;

import java.util.function.Supplier;

public class SpokenWord
{
    public static final String MOD_ID = "spokenword";
    // We can use this if we don't want to use DeferredRegister
    public static final Supplier<RegistrarManager> REGISTRIES = Suppliers.memoize(() -> RegistrarManager.get(MOD_ID));

    private static final SpokenWordConfig CONFIG = new SpokenWordConfig();
    
    public static void init() {
        EntityEvent.LIVING_DEATH.register((entity, source) -> {
            System.out.println("Entity " + entity + " died!");
            return EventResult.interruptFalse();
        });

        Platform.getMod(MOD_ID).registerConfigurationScreen(SpokenWordConfigBuilder::createScreen);

        System.out.println(ExampleExpectPlatform.getConfigDirectory().toAbsolutePath().normalize().toString());
    }

    public static SpokenWordConfig getConfig() {
        return CONFIG;
    }
}
