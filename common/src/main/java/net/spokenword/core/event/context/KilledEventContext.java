package net.spokenword.core.event.context;

import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KilledEventContext extends EntityEventContext {

    private final Entity killer;

    public KilledEventContext(@Nullable Entity killer, Entity killed) {
        super(killed);
        this.killer = killer;
    }

    @Override
    @NotNull
    public String getTargetName() {
        if (killer == null) {
            return "Unknown Forces";
        }
        return killer.getDisplayName().getString();
    }
}
