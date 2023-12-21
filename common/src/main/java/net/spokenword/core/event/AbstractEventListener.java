package net.spokenword.core.event;

import net.spokenword.SpokenWord;
import net.spokenword.core.event.context.EventContext;

public abstract class AbstractEventListener<T> implements EventManager.EventListener<T> {

    private final EventType[] types;

    protected AbstractEventListener(EventType... types) {
        this.types = types;
    }

    public void subscribe() {
        for (EventType type : types) {
            SpokenWord.getEventManager().subscribe(type, this);
        }
    }

    public abstract void onEvent(EventType type, EventContext<T> event);

    public void unsubscribe() {
        for (EventType type : types) {
            SpokenWord.getEventManager().unsubscribe(type, this);
        }
    }
}
