package net.spokenword.config.custom.controller;

import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.ControllerBuilder;
import net.minecraft.world.entity.EntityType;
import net.spokenword.config.custom.controller.impl.EntityControllerBuilderImpl;

public interface EntityControllerBuilder extends ControllerBuilder<EntityType<?>>
{
    static EntityControllerBuilder create(Option<EntityType<?>> option)
    {
        return new EntityControllerBuilderImpl(option);
    }

    EntityControllerBuilder hideHostiles();

    EntityControllerBuilder hidePassives();
}
