package net.spokenword.core.event.context;

import org.jetbrains.annotations.NotNull;

public class ChatEventContext extends AbstractEventContext<String> {
    private final String message;

    private final String sender;

    public ChatEventContext(String message, String sender) {
        this.message = message;
        this.sender = sender;
    }

    @Override
    @NotNull
    public String getFilterable() {
        return message;
    }

    @Override
    @NotNull
    public String getSourceName() {
        return sender;
    }

    @Override
    @NotNull
    public String getTargetName() {
        return message;
    }
}
