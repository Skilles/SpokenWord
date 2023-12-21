package net.spokenword.core.event.context;

import java.util.Map;

public class SimpleEventContext extends AbstractEventContext<Void> {
    SimpleEventContext() {

    }

    SimpleEventContext(Map<String, String> metadata) {
        super(metadata);
    }
}
