package net.spokenword.core.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {

    private static final Map<EventType, List<EventCallback>> listeners = new HashMap<>();

    public static void subscribe(EventType type, EventCallback callback) {
        listeners.computeIfAbsent(type, k -> new ArrayList<>()).add(callback);
    }

    public static void unsubscribe(EventType type, EventCallback listener) {
        listeners.get(type).remove(listener);
    }

    public static void dispatchEvent(EventType type, EventContext context) {
        listeners.computeIfAbsent(type, k -> new ArrayList<>()).forEach(listener -> listener.onEvent(context));
    }

    @FunctionalInterface
    public interface EventCallback {

        void onEvent(EventContext event);
    }
}
