package net.spokenword.core.event.context.handler;

import net.spokenword.SpokenWord;
import net.spokenword.core.event.EventType;
import net.spokenword.core.event.context.*;

public class ContextHandlers {

    public static final EventContextHandler<BlockEventContext> BLOCK = SpokenWord.getEventManager()
                                                                                 .registerContextHandler(builder -> builder
                                                                                                 .withFormatter((context, message) -> message
                                                                                                         .replace("%block%", context.getSourceName())
                                                                                                         .replace("%pos%", context.getMeta("pos"))),
                                                                                         EventType.BLOCK_BREAK, EventType.BLOCK_PLACE);

    public static final EventContextHandler<EntityEventContext> ENTITY_DEATH = SpokenWord.getEventManager()
                                                                                         .registerContextHandler(builder -> builder
                                                                                                         .withFormatter((context, message) -> message
                                                                                                                 .replace("%entity%", context.getSourceName())),
                                                                                                 EventType.ENTITY_DEATH, EventType.OWNED_DEATH);

    public static final EventContextHandler<KilledEventContext> ENTITY_KILLED = SpokenWord.getEventManager()
                                                                                          .registerContextHandler(builder -> builder
                                                                                                          .withFormatter((context, message) -> message
                                                                                                                  .replace("%entity%", context.getSourceName())
                                                                                                                  .replace("%killer%", context.getTargetName())),
                                                                                                  EventType.KILLED_PVE, EventType.KILLED_PVP, EventType.SELF_DEATH_PVE, EventType.SELF_DEATH_PVP);

    public static final EventContextHandler<PlayerEventContext> PLAYER = SpokenWord.getEventManager()
                                                                                   .registerContextHandler(builder -> builder
                                                                                                   .withFormatter((context, message) -> message
                                                                                                           .replace("%player%", context.getSourceName())),
                                                                                           EventType.PLAYER_JOIN);

    public static final EventContextHandler<ChatEventContext> CHAT = SpokenWord.getEventManager()
                                                                               .registerContextHandler(builder -> builder
                                                                                               .withFormatter((context, message) -> message
                                                                                                       .replace("%player%", context.getSourceName())
                                                                                                       .replace("%message%", context.getTargetName())),
                                                                                       EventType.PLAYER_CHAT, EventType.PLAYER_MESSAGE);

    public static void register() {
        // NO-OP
    }
}
