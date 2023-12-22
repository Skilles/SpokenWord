package net.spokenword.config;

import dev.architectury.platform.Mod;
import net.spokenword.SpokenWord;

public class ConfigScreenFactory {
    public static Mod.ConfigurationScreenProvider getProvider() {
        if (SpokenWord.YACL_LOADED) {
            SpokenWordConfigScreen.getHandler().load();
            return SpokenWordConfigScreen::create;
        }

        return YaclMissingScreen::create;
    }
}
