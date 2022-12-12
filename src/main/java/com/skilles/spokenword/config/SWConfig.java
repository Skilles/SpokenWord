package com.skilles.spokenword.config;

import com.skilles.spokenword.SpokenWord;
import com.skilles.spokenword.SpokenWordClient;
import me.shedaniel.autoconfig.ConfigHolder;

public class SWConfig
{
    private static SWConfigData CONFIG;

    private SWConfig() {}

    public static void set(SWConfigData config)
    {
        CONFIG = config;
    }

    public static SWConfigData get()
    {
        return CONFIG;
    }

    public static void onLoadConfig(ConfigHolder<SWConfigData> manager, SWConfigData newData)
    {
        SpokenWord.log("Config loaded");
        CONFIG = newData;
    }

    public static void onSaveConfig(ConfigHolder<SWConfigData> manager, SWConfigData data)
    {
        SpokenWord.log("Config saved");
        SpokenWordClient.BEHAVIOR_MANAGER.init(data);
    }

}
