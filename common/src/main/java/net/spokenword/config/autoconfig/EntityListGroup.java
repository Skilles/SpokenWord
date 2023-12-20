package net.spokenword.config.autoconfig;

import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.ControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigField;
import dev.isxander.yacl3.config.v2.api.autogen.ListGroup;
import dev.isxander.yacl3.config.v2.api.autogen.OptionAccess;
import net.minecraft.world.entity.EntityType;
import net.spokenword.config.custom.controller.EntityControllerBuilder;

import java.util.List;

public class EntityListGroup implements ListGroup.ValueFactory<EntityType<?>>, ListGroup.ControllerFactory<EntityType<?>> {

    @Override
    public ControllerBuilder<EntityType<?>> createController(ListGroup annotation, ConfigField<List<EntityType<?>>> field, OptionAccess storage, Option<EntityType<?>> option) {
        var builder = EntityControllerBuilder.create(option);

        field.defaultAccess().getAnnotation(TargetEntity.class).ifPresent(listGroup ->
        {
            if (listGroup.hideHostiles()) {
                builder.hideHostiles();
            }
            if (listGroup.hidePassives()) {
                builder.hidePassives();
            }
        });


        return builder;
    }

    @Override
    public EntityType<?> provideNewValue() {
        return EntityType.AXOLOTL;
    }
}
