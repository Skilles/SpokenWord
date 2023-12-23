package net.spokenword.core.event;

import net.minecraft.client.Minecraft;
import net.spokenword.SpokenWord;
import net.spokenword.core.event.context.EventContext;
import net.spokenword.core.event.transformer.EventTransformer;
import net.spokenword.core.event.transformer.EventTransformerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {

    private final Map<EventType, List<EventListener>> listeners = new HashMap<>();
    private final Map<EventType, EventTransformerFactory<?>> eventTransformerFactories = new HashMap<>();

    private static final EventTransformerFactory<EventContext<Object>> defaultTransformerFactory = EventTransformer.createBuilder().build();

    public void subscribe(EventType type, EventListener listener) {
        listeners.computeIfAbsent(type, k -> new ArrayList<>()).add(listener);
    }

    public void unsubscribe(EventType type, EventListener listener) {
        listeners.get(type).remove(listener);
    }

    public <T extends EventContext<TValue>, TValue> void dispatchEvent(EventType type, T context) {
        SpokenWord.getLogger().info("Dispatching event " + type.name());
        if (!SpokenWord.getConfig().globalEnabled || !checkIpFilter()) {
            SpokenWord.getLogger().info("Event " + type.name() + " was cancelled by globalEnabled or ipFilter");
            return;
        }
        if (!listeners.containsKey(type)) {
            SpokenWord.getLogger().info("Event " + type.name() + " was cancelled by no listeners");
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

    private static boolean checkIpFilter() {
        var currentServer = Minecraft.getInstance().getCurrentServer();
        if (currentServer == null) {
            return true;
        }
        var ipFilter = SpokenWord.getConfig().ipFilter;

        return ipFilter.isEmpty() || ipFilter.contains(currentServer.ip);
    }
}
