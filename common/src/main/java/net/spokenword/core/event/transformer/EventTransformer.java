package net.spokenword.core.event.transformer;

import net.spokenword.core.event.context.EventContext;
import net.spokenword.core.format.BehaviorFilterFormatter;
import net.spokenword.core.format.BehaviorFilterOverride;
import net.spokenword.core.format.BehaviorMessageFormatter;

import java.util.List;

public interface EventTransformer {

    /**
     * Format a message with the given context
     * Possible variables are:
     * - %player%: The player name that triggered the event
     * - %killer%: The entity that killed the player
     * - %entity%: The entity that triggered the event
     * - %item%: The item that triggered the event
     * - %block%: The block that triggered the event
     * - %self%: The current player name
     *
     * @param message The message to format
     * @return The formatted message
     */
    String formatMessage(String message);

    boolean testFilter(List<?> filter);

    static <T extends EventContext<TFilter>, TFilter> Builder<T, TFilter> createBuilder() {
        return new Builder<>();
    }

    @FunctionalInterface
    interface BuilderCallback<T extends EventContext<TFilter>, TFilter> {
        Builder<T, TFilter> configure(Builder<T, TFilter> builder);
    }

    class Builder<T extends EventContext<TFilter>, TFilter> {
        private BehaviorMessageFormatter<T> formatter;

        private BehaviorFilterFormatter<?, TFilter> filterFormatter;

        private BehaviorFilterOverride<TFilter> filterOverride;

        public Builder<T, TFilter> withFormatter(BehaviorMessageFormatter<T> callback) {
            if (this.formatter == null) {
                this.formatter = callback;
                return this;
            }
            this.formatter = (context, message) -> callback.parse(context, formatter.parse(context, message));
            return this;
        }

        public <TValue> Builder<T, TFilter> withFilterFormatter(BehaviorFilterFormatter<TValue, TFilter> callback) {
            if (this.filterFormatter == null) {
                this.filterFormatter = callback;
                return this;
            }
            this.filterFormatter = (filterable) -> (T) filterable;
            return this;
        }

        public Builder<T, TFilter> withFilterOverride(BehaviorFilterOverride<TFilter> callback) {
            this.filterOverride = callback;
            return this;
        }

        public EventTransformerFactory<T> build() {
            if (this.formatter == null) {
                this.formatter = (context, message) -> message;
            }
            return EventTransformerFactory.createTransformer(formatter, filterFormatter, filterOverride);
        }
    }
}
