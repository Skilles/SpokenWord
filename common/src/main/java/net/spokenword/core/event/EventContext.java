package net.spokenword.core.event;

import net.minecraft.client.Minecraft;

public record EventContext(Object filterable, String mainArgument, String secondaryArgument) {
    public EventContext(Object filterable, String mainArgument) {
        this(filterable, mainArgument, null);
    }

    public EventContext(Object filterable) {
        this(filterable, null, null);
    }

    public EventContext() {
        this(null, null, null);
    }

    public String localPlayerName() {
        return Minecraft.getInstance().player.getDisplayName().getString();
    }
}

