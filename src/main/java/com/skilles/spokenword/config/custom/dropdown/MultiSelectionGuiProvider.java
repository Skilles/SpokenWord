package com.skilles.spokenword.config.custom.dropdown;

import com.skilles.spokenword.exceptions.ConfigException;
import me.shedaniel.autoconfig.gui.registry.api.GuiProvider;
import me.shedaniel.autoconfig.gui.registry.api.GuiRegistryAccess;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.gui.entries.DropdownBoxEntry;
import net.minecraft.network.chat.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class MultiSelectionGuiProvider<T, U extends Annotation> implements GuiProvider
{
    private final Function<U, Stream<T>> selectionsFunction;

    private final Function<T, String> textFunction;

    private final Function<String, T> objectFunction;

    private final Function<T, String> keyFunction;

    private final T defaultValue;

    protected MultiSelectionGuiProvider(Stream<T> selections, T defaultValue, Function<T, String> textFunction,
                                     Function<String, T> objectFunction, Function<T, String> keyFunction)
    {
        this((u) -> selections, defaultValue, textFunction, objectFunction, keyFunction);
    }

    /**
     * @param selectionsFunction Function that returns a stream of all possible selections
     * @param defaultValue The default value to use when no selection is made
     * @param textFunction Function that returns the text to display for a given selection
     * @param objectFunction Function that returns the selection for a given key
     * @param keyFunction Function that returns the key to use (within config) for a given selection
     */
    protected MultiSelectionGuiProvider(Function<U, Stream<T>> selectionsFunction, T defaultValue, Function<T, String> textFunction,
                                     Function<String, T> objectFunction, Function<T, String> keyFunction)
    {
        this.selectionsFunction = selectionsFunction;
        this.textFunction = textFunction;
        this.objectFunction = objectFunction;
        this.keyFunction = keyFunction;
        this.defaultValue = defaultValue;
    }

    @Override
    public List<AbstractConfigListEntry> get(String translationKey, Field field, Object configInstance, Object o1,
                                             GuiRegistryAccess guiRegistryAccess)
    {
        var selections = selectionsFunction.apply(field.getAnnotation(getAnnotationClass()));
        return List.of(createSelector(selections, Component.translatable(translationKey), field, configInstance));
    }

    protected abstract Class<U> getAnnotationClass();

    private DropdownBoxEntry<T> createSelector(Stream<T> selections, Component title, Field field, Object config)
    {
        var cellCreator = new MultiSelectionCellCreator<>(getInitialValues(field, config), textFunction);
        return new MultiDropdownMenuBuilder<>(title, new MultiSelectionTopCellElement<>(cellCreator.getSelectedList().size(), defaultValue), cellCreator)
                .setSelections(selections.collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(textFunction)))))
                .setSaveConsumer((value) -> saveSelections(field, config, cellCreator.getSelectedList()))
                .build();
    }

    private Collection<T> getInitialValues(Field field, Object config)
    {
        try
        {
            return ((Collection<String>) field.get(config)).stream().map(objectFunction).collect(Collectors.toSet());
        }
        catch (IllegalAccessException e)
        {
            throw new ConfigException("Error in loading initial values", e);
        }
    }

    private void saveSelections(Field field, Object config, Collection<T> selected)
    {
        try
        {
            field.set(config, selected.stream().map(keyFunction).collect(Collectors.toList()));
        }
        catch (IllegalAccessException e)
        {
            throw new ConfigException("Error in saving multi-dropdown selections", e);
        }
    }
}
