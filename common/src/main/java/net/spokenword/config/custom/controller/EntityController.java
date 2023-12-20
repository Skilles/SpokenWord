package net.spokenword.config.custom.controller;

import dev.isxander.yacl3.api.Option;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.spokenword.config.mobhead.MobHeads;

import java.util.function.Function;
import java.util.function.Predicate;

public class EntityController extends AbstractRegistryController<EntityType<?>> {

    private final boolean hideHostiles;

    private final boolean hidePassives;

    public EntityController(Option<EntityType<?>> option, boolean hideHostiles, boolean hidePassives) {
        super(option, BuiltInRegistries.ENTITY_TYPE);
        this.hideHostiles = hideHostiles;
        this.hidePassives = hidePassives;
    }

    @Override
    protected Predicate<EntityType<?>> optionFilter() {
        return (type) ->
        {
            var category = type.getCategory();

            if (category == MobCategory.MISC && !type.equals(EntityType.VILLAGER) && !type.equals(EntityType.SNOW_GOLEM) && !type.equals(EntityType.IRON_GOLEM)) // Why are these misc...
            {
                return false;
            }
            if (hideHostiles && !category.isFriendly()) {
                return false;
            }

            return !hidePassives || !category.isFriendly();
        };
    }

    @Override
    protected Function<EntityType<?>, Component> displayFormatter() {
        return EntityType::getDescription;
    }

    @Override
    protected int getDecorationPadding() {
        return 16;
    }

    @Override
    protected void renderDropdownEntry(GuiGraphics graphics, ResourceLocation identifier, int x, int y) {
        renderValueEntry(graphics, registry.get(identifier), x, y, 0);
    }

    @Override
    protected void renderValueEntry(GuiGraphics graphics, EntityType<?> entityType, int x, int y, float delta) {
        graphics.renderFakeItem(MobHeads.getMobHead(entityType), x, y);
    }
}
