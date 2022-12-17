package com.skilles.spokenword.config.custom.dropdown.entity;

import com.skilles.spokenword.SpokenWord;
import com.skilles.spokenword.config.custom.dropdown.MultiSelectionGuiProvider;
import com.skilles.spokenword.exceptions.ConfigException;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;

import java.util.stream.Stream;

public class EntitySelectionGuiProvider extends MultiSelectionGuiProvider<EntityType<?>, EntityDropdown>
{

    public EntitySelectionGuiProvider()
    {
        super(annotation -> getSelections(annotation.mode()),
                SpokenWord.DEFAULT_ENTITY,
                EntityType::toShortString,
                string -> EntityType.byString(string).orElseThrow(() -> new ConfigException("Invalid entity type " + string)),
                entityType -> EntityType.getKey(entityType).toString()
        );
    }

    private static Stream<EntityType<?>> getSelections(ListModes mode)
    {
        var stream = BuiltInRegistries.ENTITY_TYPE.stream();

        return switch (mode)
        {
            case ALL -> stream;
            case HOSTILE -> stream.filter(entityType -> !entityType.getCategory().isFriendly());
            case FRIENDLY -> stream.filter(entityType -> entityType.getCategory().isFriendly());
        };
    }

    @Override
    protected Class<EntityDropdown> getAnnotationClass()
    {
        return EntityDropdown.class;
    }
}
