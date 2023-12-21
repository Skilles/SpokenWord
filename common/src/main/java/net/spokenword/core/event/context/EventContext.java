package net.spokenword.core.event.context;

import java.util.Map;

public interface EventContext<T> {

    T getFilterable();

    String getSourceName();

    String getTargetName();

    String getMeta(String key);

    static EventContext<Void> simple() {
        return new SimpleEventContext();
    }

    static EventContext<Void> simple(String key, String value) {
        return new SimpleEventContext(Map.of(key, value));
    }
}
