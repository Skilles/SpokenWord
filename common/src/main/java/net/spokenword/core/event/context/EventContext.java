package net.spokenword.core.event.context;

import net.spokenword.SpokenWord;
import net.spokenword.core.event.EventType;

public interface EventContext<T> {

    T getFilterable();

    String getSourceName();

    String getTargetName();

    String getMeta(String key);

    default String parseMessage(EventType type, String message) {
        return SpokenWord.getEventManager().getParsedMessage(type, this, message);
    }

    static EventContext<Void> simple() {
        return new SimpleEventContext();
    }
}
