package net.spokenword.core.event.context;

import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class EntityEventContext extends AbstractEventContext<Entity> {

    private final Entity entity;

    public EntityEventContext(Entity entity) {
        this.entity = entity;
    }

    @Override
    @NotNull
    public Entity getFilterable() {
        return entity;
    }

    @Override
    @NotNull
    public String getSourceName() {
        return entity.getDisplayName().getString();
    }
}
