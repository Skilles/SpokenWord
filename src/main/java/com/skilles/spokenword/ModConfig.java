package com.skilles.spokenword;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

import java.util.Arrays;
import java.util.List;

@Config(name = SpokenWord.MOD_ID)
public class ModConfig implements ConfigData {
    public boolean enabled = true;
    public List<String> textList = Arrays.asList("/sethome");
}
