package com.skilles.spokenword.behaviors;

import com.skilles.spokenword.behaviors.regex.RegexPair;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

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

    public boolean matchesFilter(@NotNull String message, Iterable<RegexPair> regex)
    {
        // for regex input
        return filters.stream().anyMatch(filter -> message.contains(getParsedMessage(filter, regex))); // TODO add config option
    }

    @Override
    public void onActivate(BehaviorContext ctx)
    {
        var regex = ctx.regex().orElseThrow();

        if (filters.isEmpty() || matchesFilter(ctx.message().orElseThrow(), regex))
        {
            sendMessages(ctx.player(), regex);
        }
    }

    private void sendMessages(Player player, Iterable<RegexPair> regex)
    {
        messages.forEach(message -> sendMessage(player, getParsedMessage(message, regex)));
    }

    private static String getParsedMessage(String message, Iterable<RegexPair> regex)
    {
        for (RegexPair pair : regex)
        {
            message = pair.apply(message);
        }
        return message;
    }

    private static void sendMessage(Player player, String message)
    {
        if (message.startsWith("/"))
        {
            ((LocalPlayer)player).connection.sendCommand(message.substring(1));
        }
        else
        {
            ((LocalPlayer)player).connection.sendChat(message.replaceAll("\"", ""));
        }
    }

    static class Builder
    {

        private Collection<String> messages;

        private Collection<String> filters;

        private boolean advanced;

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

        public Builder setAdvanced()
        {
            this.advanced = true;

            return this;
        }

        public MessageBehavior build()
        {
            return advanced ? new AdvancedMessageBehavior(messages, filters) : new MessageBehavior(messages, filters);
        }
    }
}
