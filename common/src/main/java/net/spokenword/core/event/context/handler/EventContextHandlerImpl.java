package net.spokenword.core.event.context.handler;

import net.spokenword.core.event.context.EventContext;

public final class EventContextHandlerImpl<T extends EventContext<?>> implements EventContextHandler<T> {

    private final BehaviorMessageFormatter<T> formatter;

    EventContextHandlerImpl(BehaviorMessageFormatter<T> formatter) {
        this.formatter = formatter;
    }

    @Override
    public String formatMessage(T context, String message) {
        return formatter.parse(context, message);
    }
}
