package net.spokenword.core.event;

import net.spokenword.SpokenWord;
import net.spokenword.core.event.context.EventContext;
import net.spokenword.core.event.transformer.EventTransformer;
import net.spokenword.core.event.transformer.EventTransformerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {

    private static final Map<EventType, List<EventListener>> listeners = new HashMap<>();
    private static final Map<EventType, EventTransformerFactory<?>> eventTransformerFactories = new HashMap<>();

    private static final EventTransformerFactory<EventContext<Object>> defaultTransformerFactory = EventTransformer.createBuilder().build();

    public void subscribe(EventType type, EventListener listener) {
        listeners.computeIfAbsent(type, k -> new ArrayList<>()).add(listener);
    }

    public void unsubscribe(EventType type, EventListener listener) {
        listeners.get(type).remove(listener);
    }

    public <T extends EventContext<?>> void dispatchEvent(EventType type, T context) {
        if (!SpokenWord.getConfig().globalEnabled) {
            return;
        }
        if (!listeners.containsKey(type)) {
            return;
        }
        var transformer = getEventTransformerFactory(type).create(context);
        listeners.get(type).forEach(listener -> listener.onEvent(transformer));
    }

    public <T extends EventContext<TFilter>, TFilter> EventTransformerFactory<T> registerEventTransformer(EventTransformer.BuilderCallback<T, TFilter> configureBuilder, EventType... type) {
        var transformer = configureBuilder.configure(EventTransformer.createBuilder()).build();

        for (EventType eventType : type) {
            eventTransformerFactories.put(eventType, transformer);
        }

        return transformer;
    }

    private <T extends EventContext<?>> EventTransformerFactory<T> getEventTransformerFactory(EventType type) {
        return (EventTransformerFactory<T>) eventTransformerFactories.getOrDefault(type, defaultTransformerFactory);
    }
}
