package net.spokenword.config;

import dev.architectury.platform.Mod;
import dev.architectury.platform.Platform;

public class ConfigScreenFactory {
    public static Mod.ConfigurationScreenProvider create() {
        if (Platform.isModLoaded("yet_another_config_lib_v3")) {
            return SpokenWordConfigScreen::create;
        }

        return YaclMissingScreen::create;
    }
}
