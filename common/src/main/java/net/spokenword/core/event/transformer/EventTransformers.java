package net.spokenword.core.event.transformer;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.spokenword.SpokenWord;
import net.spokenword.core.event.EventType;
import net.spokenword.core.event.context.*;
import net.spokenword.core.format.MessageVariable;

public class EventTransformers {

    public static final EventTransformerFactory<BlockEventContext> BLOCK = SpokenWord.getEventManager()
                                                                                     .registerEventTransformer(builder -> builder
                                                                                                     .withFormatter((context, message) -> MessageVariable
                                                                                                             .builder(message)
                                                                                                             .replace(MessageVariable.BLOCK, context.getSourceName())
                                                                                                             .replace(MessageVariable.POSITION, context.getMeta("pos"))
                                                                                                             .build()),
                                                                                         EventType.BLOCK_BREAK, EventType.BLOCK_PLACE);

    public static final EventTransformerFactory<EntityEventContext> ENTITY_DEATH = SpokenWord.getEventManager()
                                                                                             .registerEventTransformer(builder -> builder
                                                                                                             .withFormatter((context, message) -> MessageVariable
                                                                                                                     .ENTITY.replace(message, context.getSourceName()))
                                                                                                             .withFilterFormatter(Entity::getType),
                                                                                                 EventType.ENTITY_DEATH, EventType.OWNED_DEATH);

    public static final EventTransformerFactory<KilledEventContext> ENTITY_KILLED = SpokenWord.getEventManager()
                                                                                              .registerEventTransformer(builder -> builder
                                                                                                              .withFormatter((context, message) -> {
                                                                                                                  var messageBuilder = MessageVariable
                                                                                                                          .builder(message)
                                                                                                                          .replace(MessageVariable.ENTITY, context.getSourceName())
                                                                                                                          .replace(MessageVariable.KILLER, context.getTargetName());
                                                                                                                  if (context.getFilterable().getType().equals(EntityType.PLAYER)) {
                                                                                                                      messageBuilder.replace(MessageVariable.PLAYER, context.getTargetName());
                                                                                                                  }
                                                                                                                  return messageBuilder.build();
                                                                                                              })
                                                                                                              .withFilterFormatter((entity) -> entity.getType().equals(EntityType.PLAYER) ? entity.getDisplayName().getString() : entity.getType()),
                                                                                                  EventType.KILLED_PVE, EventType.KILLED_PVP, EventType.SELF_DEATH_PVE, EventType.SELF_DEATH_PVP);

    public static final EventTransformerFactory<PlayerEventContext> PLAYER = SpokenWord.getEventManager()
                                                                                       .registerEventTransformer(builder -> builder
                                                                                                       .withFormatter((context, message) -> MessageVariable
                                                                                                               .PLAYER.replace(message, context.getSourceName())),
                                                                                               EventType.PLAYER_JOIN, EventType.PLAYER_LEAVE);

    public static final EventTransformerFactory<SimpleEventContext> PLAYER_KICKED = SpokenWord.getEventManager()
                                                                                              .registerEventTransformer(builder -> builder
                                                                                                              .withFormatter((context, message) -> MessageVariable
                                                                                                                      .REASON.replace(message, context.getMeta("reason"))),
                                                                                                      EventType.SELF_KICKED);

    public static final EventTransformerFactory<ChatEventContext> CHAT = SpokenWord.getEventManager()
                                                                                   .registerEventTransformer(builder -> builder
                                                                                                   .withFormatter((context, message) -> MessageVariable
                                                                                                           .builder(message)
                                                                                                           .replace(MessageVariable.PLAYER, context.getSourceName())
                                                                                                           .replace(MessageVariable.MESSAGE, context.getFilterable())
                                                                                                           .build())
                                                                                                   .withFilterFormatter((filterEntry) -> MessageVariable.SELF.replace(filterEntry, Minecraft.getInstance().player.getDisplayName().getString()))
                                                                                                   .withFilterOverride((filter, message) -> filter.stream().anyMatch((entry) -> message.toLowerCase().contains(entry.toLowerCase()))),
                                                                                       EventType.PLAYER_CHAT, EventType.PLAYER_MESSAGE);

    public static void register() {
        // NO-OP
    }
}
