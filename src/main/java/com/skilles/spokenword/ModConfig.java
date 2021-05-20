package com.skilles.spokenword;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import java.util.Arrays;
import java.util.List;

@Config(name = SpokenWord.MOD_ID)
public class ModConfig implements ConfigData {
    public boolean enabled = true;
    @ConfigEntry.Gui.Tooltip
    public boolean death = true;
    @ConfigEntry.Gui.Tooltip
    public boolean join = true;
    public List<String> deathList = Arrays.asList("/sethome");
    public List<String> joinList = Arrays.asList("Welcome %p!");
}
