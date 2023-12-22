package net.spokenword;

import dev.architectury.platform.Platform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.spokenword.config.ConfigScreenFactory;
import net.spokenword.config.SpokenWordConfig;
import net.spokenword.config.SpokenWordConfigScreen;
import net.spokenword.config.autoconfig.CustomListGroupImpl;
import net.spokenword.config.mobhead.MobHeadReloadListener;
import net.spokenword.core.behavior.BehaviorManager;
import net.spokenword.core.event.EventManager;
import net.spokenword.core.event.transformer.EventTransformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class SpokenWord {

    public static final String MOD_ID = "spokenword";

    // We can use this if we don't want to use DeferredRegister
    // public static final Supplier<RegistrarManager> REGISTRIES = Suppliers.memoize(() -> RegistrarManager.get(MOD_ID));

    private static final Logger LOGGER = LoggerFactory.getLogger("Spoken Word");

    private static final BehaviorManager BEHAVIOR_MANAGER = new BehaviorManager();

    private static final EventManager EVENT_MANAGER = new EventManager();

    public static final boolean YACL_LOADED = Platform.isModLoaded("yet_another_config_lib_v3");

    public static SpokenWordConfig getConfig() {
        if (!YACL_LOADED) {
            return new SpokenWordConfig();
        }
        return SpokenWordConfigScreen.getHandler().instance();
    }

    public static EventManager getEventManager() {
        return EVENT_MANAGER;
    }

    public static BehaviorManager getBehaviorManager() {
        return BEHAVIOR_MANAGER;
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    public static void init() {
        Platform.getMod(MOD_ID).registerConfigurationScreen(ConfigScreenFactory.getProvider());

        if (YACL_LOADED) {
            MobHeadReloadListener.register();
            CustomListGroupImpl.register();
            EventTransformers.register();
            BEHAVIOR_MANAGER.refreshBehaviors();
        }
    }
}
