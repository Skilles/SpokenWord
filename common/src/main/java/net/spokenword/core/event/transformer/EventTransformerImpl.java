package net.spokenword.core.event.transformer;

import net.spokenword.SpokenWord;
import net.spokenword.core.event.context.EventContext;
import net.spokenword.core.format.BehaviorFilterFormatter;
import net.spokenword.core.format.BehaviorFilterOverride;
import net.spokenword.core.format.BehaviorMessageFormatter;

import java.util.List;

public final class EventTransformerImpl<T extends EventContext<TFilter>, TFilter> implements EventTransformer {

    private final BehaviorMessageFormatter<T> formatter;

    private final BehaviorFilterFormatter<?, TFilter> filterFormatter;

    private final BehaviorFilterOverride<TFilter> filterOverride;

    private final T context;

    EventTransformerImpl(T context, BehaviorMessageFormatter<T> formatter, BehaviorFilterFormatter<?, TFilter> filterFormatter, BehaviorFilterOverride<TFilter> filterOverride) {
        this.formatter = formatter;
        this.context = context;
        this.filterFormatter = filterFormatter;
        this.filterOverride = filterOverride;
    }

    @Override
    public String formatMessage(String message) {
        return formatter.parse(context, message);
    }

    @Override
    public boolean testFilter(List<?> filter) {
        if (filter.isEmpty()) {
            return true;
        }

        try {
            var filterList = (List<TFilter>) filter;
            if (filterFormatter != null) {
                filterList = (List<TFilter>) filterList.stream().map(filterFormatter::format).toList();
            }
            if (filterOverride != null) {
                return filterOverride.contains(filterList, context.getFilterable());
            }
            return filterList.contains(context.getFilterable());
        } catch (ClassCastException e) {
            SpokenWord.getLogger().error("Invalid filter type for context %s and transformer %s".formatted(context.getClass().getSimpleName(), this.getClass().getSimpleName()));
            return false;
        }
    }
}
