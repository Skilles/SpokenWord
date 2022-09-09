package com.skilles.spokenword.config;

import com.skilles.spokenword.SpokenWord;
import com.skilles.spokenword.SpokenWordClient;
import me.shedaniel.autoconfig.ConfigHolder;

public class ConfigManager
{

    public static void onLoadConfig(ConfigHolder<SWConfigData> manager, SWConfigData newData)
    {
        SpokenWord.log("Config loaded");
        SWConfig.set(newData);
    }

    public static void onSaveConfig(ConfigHolder<SWConfigData> manager, SWConfigData data)
    {
        SpokenWord.log("Config saved");
        SpokenWordClient.BEHAVIOR_MANAGER.init(data);
    }

}