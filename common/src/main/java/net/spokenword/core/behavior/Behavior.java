package net.spokenword.core.behavior;

import net.spokenword.ReflectionUtil;
import net.spokenword.core.event.EventContext;
import net.spokenword.core.event.EventListener;
import net.spokenword.core.event.EventType;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Behavior<T> extends EventListener {

    private final List<String> messages;

    @Nullable
    private final List<T> filter;

    private final String name;

    public Behavior(String name, List<String> messages, @Nullable List<T> filter, EventType... eventType) {
        super(eventType);
        this.name = name;
        this.messages = messages;
        this.filter = filter;
    }

    @Override
    public void onEvent(EventContext event) {
        validateEventContext(event);
        if (filter != null && !filter.contains(event.filterable())) {
            return;
        }

        for (String message : messages) {
            BehaviorUtil.sendChatMessage(message, null);
        }
    }

    @Override
    public String toString() {
        return name;
    }

    private void validateEventContext(EventContext event) {
        if (filter == null) {
            return;
        }
        var filterable = event.filterable();
        // Check if filterable class is the same as the inner filter class
        try {
            ReflectionUtil.getInnerClass(Behavior.class.getDeclaredField("filter")).isAssignableFrom(filterable.getClass());
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
