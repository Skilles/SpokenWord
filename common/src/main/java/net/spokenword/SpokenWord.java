package net.spokenword;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.BlockEvent;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.platform.Platform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.spokenword.config.ConfigScreenFactory;
import net.spokenword.config.SpokenWordConfig;
import net.spokenword.config.SpokenWordConfigScreen;
import net.spokenword.config.autoconfig.CustomListGroupImpl;
import net.spokenword.config.mobhead.MobHeadReloadListener;
import net.spokenword.core.behavior.BehaviorManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class SpokenWord {

    public static final String MOD_ID = "spokenword";

    // We can use this if we don't want to use DeferredRegister
    // public static final Supplier<RegistrarManager> REGISTRIES = Suppliers.memoize(() -> RegistrarManager.get(MOD_ID));

    public static final Logger LOGGER = LoggerFactory.getLogger("Spoken Word");

    public static final BehaviorManager BEHAVIOR_MANAGER = new BehaviorManager();

    public static SpokenWordConfig getConfig() {
        return SpokenWordConfigScreen.getHandler().instance();
    }

    public static void init() {
        EntityEvent.LIVING_DEATH.register((entity, source) ->
        {
            System.out.println("Entity " + entity + " died!");
            return EventResult.interruptFalse();
        });

        BlockEvent.BREAK.register((world, pos, state, player, f) ->
        {
            System.out.println("Player " + player + " broke block " + state.getBlock());
            return EventResult.interruptFalse();
        });

        Platform.getMod(MOD_ID).registerConfigurationScreen(ConfigScreenFactory.create());

        MobHeadReloadListener.register();
        CustomListGroupImpl.register();
        BEHAVIOR_MANAGER.refreshBehaviors();
    }
}
