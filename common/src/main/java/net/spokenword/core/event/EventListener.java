package net.spokenword.core.event;

import net.spokenword.core.event.transformer.EventTransformer;

public interface EventListener {
    void onEvent(EventTransformer transformer);
}
