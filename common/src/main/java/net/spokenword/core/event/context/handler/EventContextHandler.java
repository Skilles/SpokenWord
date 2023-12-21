package net.spokenword.core.event.context.handler;

import net.spokenword.core.event.context.EventContext;

public interface EventContextHandler<T extends EventContext<?>> {

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
     * @param context The context of the event
     * @param message The message to format
     * @return The formatted message
     */
    String formatMessage(T context, String message);

    @FunctionalInterface
    interface BehaviorMessageFormatter<T extends EventContext<?>> {
        String parse(T context, String message);
    }

    @FunctionalInterface
    interface BuilderCallback<T extends EventContext<?>> {
        Builder<T> configure(Builder<T> builder);
    }

    static <T extends EventContext<?>> Builder<T> createBuilder() {
        return new Builder<>();
    }

    class Builder<T extends EventContext<?>> {
        private BehaviorMessageFormatter<T> formatter;

        public Builder<T> withFormatter(BehaviorMessageFormatter<T> callback) {
            this.formatter = callback;
            return this;
        }

        public EventContextHandler<T> build() {
            return new EventContextHandlerImpl<>(formatter);
        }
    }
}
