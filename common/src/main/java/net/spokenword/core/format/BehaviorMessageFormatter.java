package net.spokenword.core.format;

import net.spokenword.core.event.context.EventContext;

@FunctionalInterface
public interface BehaviorMessageFormatter<T extends EventContext<?>> {
    String parse(T context, String message);
}
