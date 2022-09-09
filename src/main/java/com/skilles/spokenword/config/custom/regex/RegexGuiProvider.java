package com.skilles.spokenword.config.custom.regex;

import com.skilles.spokenword.config.SWConfigData;
import com.skilles.spokenword.exceptions.ConfigException;
import me.shedaniel.autoconfig.gui.registry.api.GuiProvider;
import me.shedaniel.autoconfig.gui.registry.api.GuiRegistryAccess;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.StringListListEntry;
import net.minecraft.network.chat.Component;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class RegexGuiProvider implements GuiProvider
{

    @Override
    public List<AbstractConfigListEntry> get(String s, Field field, Object o, Object o1, GuiRegistryAccess guiRegistryAccess)
    {
        return List.of(createRegexList(field, (SWConfigData.Filters) o, s));
    }

    private static StringListListEntry createRegexList(Field field, SWConfigData.Filters config, String translationKey)
    {
        try
        {
            return ConfigEntryBuilder.create().startStrList(Component.translatable(translationKey), (List<String>) field.get(config))
                    .setErrorSupplier(RegexGuiProvider::validateRegex)
                    .setSaveConsumer(value -> saveRegex(field, config, value))
                    .build();
        }
        catch (IllegalAccessException e)
        {
            throw new ConfigException("Error in creating regex list", e);
        }
    }

    private static Optional<Component> validateRegex(List<String> value)
    {
        try
        {
            for (String regex : value)
            {
                Pattern.compile(regex);
            }
        }
        catch (PatternSyntaxException e)
        {
            return Optional.of(Component.literal("Invalid regex (" + e.getDescription() + " at index " + e.getIndex() + ")"));
        }
        return Optional.empty();
    }

    private static void saveRegex(Field field, SWConfigData.Filters config, List<String> value)
    {
        try
        {
            field.set(config, value);
        }
        catch (IllegalAccessException e)
        {
            throw new ConfigException("Error in saving regex list", e);
        }
    }

}
