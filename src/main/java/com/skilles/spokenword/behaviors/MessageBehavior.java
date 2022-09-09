package com.skilles.spokenword.behaviors;

import java.util.Collection;
import java.util.List;

public class MessageBehavior extends AbstractBehavior
{

    private final Collection<String> messages;

    private final Collection<String> filters;

    public MessageBehavior(Collection<String> messages, Collection<String> filters)
    {
        super();
        this.messages = messages;
        this.filters = filters;
    }

    public boolean matchesFilter(String message, RegexPair... regex)
    {
        return filters.stream().anyMatch(filter -> message.contains(getParsedMessage(filter, regex))); // TODO add config option for regex input
    }

    @Override
    public void activate(RegexPair... regex)
    {
        sendMessages(regex);
    }

    private void sendMessages(RegexPair... regex)
    {
        messages.forEach(message -> sendMessage(getParsedMessage(message, regex)));
    }

    private static String getParsedMessage(String message, RegexPair... regex)
    {
        for (RegexPair pair : regex)
        {
            message = pair.apply(message);
        }
        return message;
    }

    private static void sendMessage(String message)
    {
        if (message.startsWith("/"))
        {
            getPlayer().commandSigned(message.substring(1), null);
        }
        else
        {
            getPlayer().chatSigned(message.replaceAll("\"", ""), null);
        }
    }

    static class Builder
    {

        private Collection<String> messages;

        private Collection<String> filters;

        public Builder()
        {
            messages = List.of();
            filters = List.of();
        }

        public Builder setMessages(Collection<String> messages)
        {
            this.messages = messages;

            return this;
        }

        public Builder setMessages(String message)
        {
            this.messages = List.of(message);

            return this;
        }

        public Builder setFilters(Collection<String> filters)
        {
            this.filters = filters;

            return this;
        }

        public Builder setFilters(String filter)
        {
            this.filters = List.of(filter);

            return this;
        }

        public MessageBehavior build()
        {
            return new MessageBehavior(messages, filters);
        }

    }

}
