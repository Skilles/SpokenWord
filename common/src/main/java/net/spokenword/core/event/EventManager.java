package net.spokenword.core.event;

import net.minecraft.client.Minecraft;
import net.spokenword.core.event.context.EventContext;
import net.spokenword.core.event.context.handler.EventContextHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {

    private static final Map<EventType, List<EventListener>> listeners = new HashMap<>();

    private static final Map<EventType, EventContextHandler> contextHandlers = new HashMap<>();

    public void subscribe(EventType type, EventListener<?> listener) {
        listeners.computeIfAbsent(type, k -> new ArrayList<>()).add(listener);
    }

    public void unsubscribe(EventType type, EventListener<?> listener) {
        listeners.get(type).remove(listener);
    }

    public void dispatchEvent(EventType type, EventContext<?> context) {
        listeners.computeIfAbsent(type, k -> new ArrayList<>()).forEach(listener -> listener.onEvent(type, context));
    }

    public <T extends EventContext<?>> EventContextHandler<T> registerContextHandler(EventContextHandler.BuilderCallback<T> configureBuilder, EventType... type) {
        var handler = configureBuilder.configure(EventContextHandler.createBuilder()).build();

        for (EventType eventType : type) {
            contextHandlers.put(eventType, handler);
        }

        return handler;
    }

    // TODO: find a better place for this
    public String getParsedMessage(EventType type, EventContext<?> context, String message) {
        if (contextHandlers.containsKey(type)) {
            var handler = contextHandlers.get(type);
            message = handler.formatMessage(context, message);
        }
        message = message.replace("%self%", Minecraft.getInstance().player.getDisplayName().getString());

        return message;
    }

    public interface EventListener<T> {

        void onEvent(EventType type, EventContext<T> event);
    }
}
