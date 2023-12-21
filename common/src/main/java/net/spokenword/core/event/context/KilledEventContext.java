package net.spokenword.core.event.context;

import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class KilledEventContext extends EntityEventContext {

    private final Entity killed;

    public KilledEventContext(Entity killer, Entity killed) {
        super(killer);
        this.killed = killed;
    }

    @Override
    @NotNull
    public String getTargetName() {
        return killed.getDisplayName().getString();
    }
}
