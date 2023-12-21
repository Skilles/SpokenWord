package net.spokenword.core.event.transformer;

import net.spokenword.core.event.context.EventContext;
import net.spokenword.core.format.BehaviorFilterFormatter;
import net.spokenword.core.format.BehaviorMessageFormatter;

import java.util.List;

public final class EventTransformerImpl<T extends EventContext<TFilter>, TFilter> implements EventTransformer {

    private final BehaviorMessageFormatter<T> formatter;

    private final BehaviorFilterFormatter<?, TFilter> filterFormatter;

    private final T context;

    EventTransformerImpl(T context, BehaviorMessageFormatter<T> formatter, BehaviorFilterFormatter<?, TFilter> filterFormatter) {
        this.formatter = formatter;
        this.context = context;
        this.filterFormatter = filterFormatter;
    }

    @Override
    public String formatMessage(String message) {
        return formatter.parse(context, message);
    }

    @Override
    public boolean testFilter(List<?> filter) {
        return !filter.isEmpty() && !filter.contains(filterFormatter.format(context.getFilterable()));
    }
}
