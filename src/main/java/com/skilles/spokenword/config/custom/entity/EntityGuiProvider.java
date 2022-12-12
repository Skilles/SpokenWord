package com.skilles.spokenword.config.custom.entity;

import com.skilles.spokenword.config.ConfigUtil;
import com.skilles.spokenword.config.SWConfigData;
import com.skilles.spokenword.exceptions.ConfigException;
import me.shedaniel.autoconfig.gui.registry.api.GuiProvider;
import me.shedaniel.autoconfig.gui.registry.api.GuiRegistryAccess;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.DropdownBoxEntry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class EntityGuiProvider implements GuiProvider
{

    @Override
    public List<AbstractConfigListEntry> get(String s, Field field, Object o, Object o1, GuiRegistryAccess guiRegistryAccess)
    {
        var annotation = field.getAnnotation(EntityDropdown.class);

        return List.of(getEntitySelector(s, annotation.mode(), field, (SWConfigData.Filters) o));
    }

    private static DropdownBoxEntry<EntityType<?>> getEntitySelector(String name, ListModes mode, Field field, SWConfigData.Filters config)
    {
        var cellCreator = createCellCreator(field, config);
        return ConfigEntryBuilder.create()
                .startDropdownMenu(
                        Component.translatable(name),
                        new EntityTopCell(cellCreator.getSelectedEntityList().size()),
                        cellCreator)
                .setSelections(getSelections(mode))
                .setSaveConsumer((value) -> saveEntities(field, config, cellCreator.getSelectedEntityList())) // SWConfig.saveEntities(cellCreator.getSelectedEntityList(), mode)
                .build();
    }

    private static EntityCellCreator createCellCreator(Field field, SWConfigData.Filters config)
    {
        try
        {
            return new EntityCellCreator(ConfigUtil.keysToEntities((Collection<String>) field.get(config)));
        }
        catch (IllegalAccessException e)
        {
            throw new ConfigException("Error in creating entity cell creator", e);
        }
    }

    private static Iterable<EntityType<?>> getSelections(ListModes mode)
    {
        var entityStream = BuiltInRegistries.ENTITY_TYPE.stream();

        if (mode.equals(ListModes.HOSTILE))
        {
            entityStream = entityStream.filter(e -> !e.getCategory().isFriendly());
        }

        return entityStream.collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(EntityType::toShortString))));
    }

    private static void saveEntities(Field field, SWConfigData.Filters config, Collection<EntityType<?>> entities)
    {
        try
        {
            field.set(config, ConfigUtil.entitiesToKeys(entities));
        }
        catch (IllegalAccessException e)
        {
            throw new ConfigException("Error in saving entity list", e);
        }
    }

}
