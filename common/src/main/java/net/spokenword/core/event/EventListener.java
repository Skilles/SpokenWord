package net.spokenword.core.event;

public abstract class EventListener {

    private final EventType[] types;

    protected EventListener(EventType... types) {
        this.types = types;
    }

    public void subscribe() {
        for (EventType type : types) {
            EventManager.subscribe(type, this::onEvent);
        }
    }

    public abstract void onEvent(EventContext event);

    public void unsubscribe() {
        for (EventType type : types) {
            EventManager.unsubscribe(type, this::onEvent);
        }
    }
}
