package net.spokenword.core.event.transformer;

import net.spokenword.core.event.context.EventContext;
import net.spokenword.core.format.BehaviorFilterFormatter;
import net.spokenword.core.format.BehaviorMessageFormatter;

@FunctionalInterface
public interface EventTransformerFactory<T extends EventContext<?>> {

    EventTransformerImpl<T, ?> create(T context);

    static <T extends EventContext<TFilter>, TFilter> EventTransformerFactory<T> createTransformer(BehaviorMessageFormatter<T> formatter, BehaviorFilterFormatter<?, TFilter> filterFormatter) {
        return (context) -> new EventTransformerImpl<>(context, formatter, filterFormatter);
    }
}
