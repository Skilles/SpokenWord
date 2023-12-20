package net.spokenword.config.custom.controller.impl;

import dev.isxander.yacl3.api.Controller;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.impl.controller.AbstractControllerBuilderImpl;
import net.minecraft.world.entity.EntityType;
import net.spokenword.config.custom.controller.EntityController;
import net.spokenword.config.custom.controller.EntityControllerBuilder;

public class EntityControllerBuilderImpl extends AbstractControllerBuilderImpl<EntityType<?>> implements EntityControllerBuilder {

    private boolean hideHostiles = false;

    private boolean hidePassives = false;

    public EntityControllerBuilderImpl(Option<EntityType<?>> option) {
        super(option);
    }

    @Override
    public Controller<EntityType<?>> build() {
        return new EntityController(option, hideHostiles, hidePassives);
    }

    @Override
    public EntityControllerBuilder hideHostiles() {
        hideHostiles = true;

        return this;
    }

    @Override
    public EntityControllerBuilder hidePassives() {
        hidePassives = true;

        return this;
    }
}
