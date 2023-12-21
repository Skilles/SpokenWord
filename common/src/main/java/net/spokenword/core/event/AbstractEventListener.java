package net.spokenword.core.event;

import net.spokenword.SpokenWord;
import net.spokenword.core.event.transformer.EventTransformer;

public abstract class AbstractEventListener implements EventListener {

    private final EventType[] types;

    protected AbstractEventListener(EventType... types) {
        this.types = types;
    }

    public void subscribe() {
        for (EventType type : types) {
            SpokenWord.getEventManager().subscribe(type, this);
        }
    }

    @Override
    public abstract void onEvent(EventTransformer transformer);

    public void unsubscribe() {
        for (EventType type : types) {
            SpokenWord.getEventManager().unsubscribe(type, this);
        }
    }
}
