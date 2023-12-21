package net.spokenword.core.behavior;

import net.spokenword.core.event.AbstractEventListener;
import net.spokenword.core.event.EventType;
import net.spokenword.core.event.context.EventContext;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Behavior<T> extends AbstractEventListener<T> {

    private List<String> messages;

    @Nullable
    private List<T> filter;

    private final String name;

    public Behavior(String name, List<String> messages, @Nullable List<T> filter, EventType... eventType) {
        super(eventType);
        this.name = name;
        this.messages = messages;
        this.filter = filter;
    }

    @Override
    public void onEvent(EventType type, EventContext<T> event) {
        if (filter != null && !filter.isEmpty() && !filter.contains(event.getFilterable())) {
            return;
        }

        for (String message : messages) {
            BehaviorUtil.sendChatMessage(event.parseMessage(type, message), null);
        }
    }

    public void update(List<String> messages, @Nullable List<T> filter) {
        this.messages = messages;
        this.filter = filter;
    }

    @Override
    public String toString() {
        return name;
    }
}
