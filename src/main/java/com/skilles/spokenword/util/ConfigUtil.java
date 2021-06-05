package com.skilles.spokenword.util;

import com.skilles.spokenword.SpokenWord;
import com.skilles.spokenword.config.ConfigManager;
import com.skilles.spokenword.config.ConfigPojo;
import com.skilles.spokenword.config.EntitySelectorCreator;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.DropdownBoxEntry;
import me.shedaniel.clothconfig2.impl.builders.BooleanToggleBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.skilles.spokenword.SpokenWord.*;
import static com.skilles.spokenword.SpokenWord.log;
import static com.skilles.spokenword.config.ConfigManager.deathConfig;
import static com.skilles.spokenword.config.ConfigPojo.*;

public class ConfigUtil {
    public static List<String> tempAllEntityList = new ArrayList<>();
    public static List<String> tempHostileEntityList = new ArrayList<>();

    public static void initEntities() {
        tempAllEntityList.clear();
        tempAllEntityList.addAll(deathGroup.entityDeathList);
        tempHostileEntityList.clear();
        tempHostileEntityList.addAll(deathGroup.entityKillList);
        log("Entities initialized: " + tempAllEntityList);
    }

    public static BooleanToggleBuilder getBooleanEntry(String name, boolean bool, boolean defaultValue, ConfigEntryBuilder entryBuilder) {
        return entryBuilder.startBooleanToggle(new TranslatableText("config.spokenword.mode."+name), bool)
                .setDefaultValue(defaultValue)
                .setTooltip(new TranslatableText("config.spokenword.mode."+name+".tooltip"));
    }
    public static DropdownBoxEntry<String> getEntitySelector(String name, String string, ListModes mode, ConfigEntryBuilder entryBuilder) {
        String entityList = mode.equals(ListModes.ALL) ? listToString(deathGroup.entityDeathList) : listToString(deathGroup.entityKillList);
        return entryBuilder.startDropdownMenu(new TranslatableText("config.spokenword.list."+name), entityList, ConfigUtil::stringFunction, new EntitySelectorCreator(mode))
                .setSelections(modeToList(mode))
                .setDefaultValue("")
                .setSaveConsumer((value) -> {
                    saveEntities((String) value, mode);
                })
                .setTooltip(new TranslatableText("config.spokenword.list."+name+".tooltip"))
                .build();
    }

    public static String stringFunction(String string) {
        if(string.equals("")) {
            tempAllEntityList.clear();
        }
        return string;
    }

    public static void addToList(String string, List<String> list) {
        assert list.equals(tempAllEntityList) || list.equals(tempHostileEntityList);
        if(string.equals("ANY")) list.clear();
        list.add(string);
    }

    public static List<String> stringToList(String string) {
        if(string.contains("[") || string.contains("]")) {
            string = StringUtils.removeAll(string, "^\\[.*\\]$");
        }
        String[] strParts = string.split( "," );
        List<String> outputList = new ArrayList<>();
        for(String stringPart : strParts) {
            EntityTypes type = EntityTypes.fromString(stringPart.trim());
            if(type != null) {
                outputList.add(stringPart.trim());
            }
        }
        return outputList.stream().distinct().collect(Collectors.toList()); // remove duplicates
    }

    public static void saveEntities(String value, ListModes modes) {
        if(modes.equals(ListModes.ALL)) {
            tempAllEntityList.clear();
            tempAllEntityList.addAll(stringToList(value));
            deathGroup.entityDeathList = tempAllEntityList;
        } else if(modes.equals(ListModes.HOSTILE)) {
            tempHostileEntityList.clear();
            tempHostileEntityList.addAll(stringToList(value));
            deathGroup.entityKillList = tempHostileEntityList;
        }
    }
    public static boolean containsEntity(Entity entity, ListModes mode) {
        //String name = entity.getDisplayName().getString();
        EntityType<?> entityType = entity.getType();
        return getList(mode).stream().findFirst().filter(type -> Objects.requireNonNull(EntityTypes.fromString(type)).getType().equals(entityType)).isPresent();
    }
    public static List<String> getList(ListModes mode) {
        return mode.equals(ListModes.ALL) ? deathConfig().entityDeathList : deathConfig().entityKillList;
    }
    public static String listToString(List<String> entityList) {
        return String.join(", ", entityList);
    }
    @Deprecated
    public static String listToString(List<String> entityList, String entryToAdd) {
        entityList.add(entryToAdd);
        return String.join(", ", entityList);
    }
    private static Enum<? extends Enum<?>>[] modeToValues(ListModes mode) {
        return mode.equals(ListModes.ALL) ? EntityTypes.values() : HostileTypes.values();
    }
    public static List<String> modeToList(ListModes mode) {
        return Arrays.stream(modeToValues(mode)).map(Enum::name).sorted(Comparator.naturalOrder()).collect(Collectors.toList());
    }
    public static List<String> modeToTemp(ListModes mode) {
        return mode.equals(ListModes.ALL) ? tempAllEntityList : tempHostileEntityList;
    }
    public static boolean containsEntity(String string, ListModes mode) {
        return modeToTemp(mode).contains(string) || modeToTemp(mode).contains("ANY");
    }
    public static void setTempList(List<String> stringToList, ListModes mode) {
        if(mode.equals(ListModes.ALL)) {
            tempAllEntityList = stringToList;
        } else {
            tempHostileEntityList = stringToList;
        }
    }
    public enum ListModes {
        ALL,
        HOSTILE;
    }
}
