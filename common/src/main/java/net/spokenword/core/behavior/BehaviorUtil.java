package net.spokenword.core.behavior;

import net.spokenword.SpokenWord;
import net.spokenword.core.event.EventType;
import net.spokenword.core.event.context.ChatEventContext;

import java.util.List;
import java.util.regex.Pattern;

public class BehaviorUtil {

    public static void handleSystemChatEvent(List<String> triggers, EventType eventType, String chatMessage) {
        for (var trigger : triggers) {
            var triggerPattern = Pattern.compile(trigger);
            var matcher = triggerPattern.matcher(chatMessage);
            if (matcher.find()) {
                var playerName = matcher.group("name");
                var message = matcher.group("msg");
                if (playerName == null || playerName.isEmpty() || message == null || message.isEmpty()) {
                    SpokenWord.getLogger().warn("Invalid trigger pattern '%s' for event %s".formatted(trigger, eventType));
                    return;
                }
                SpokenWord.getEventManager().dispatchEvent(eventType, new ChatEventContext(message, playerName));
                return;
            }
        }
    }
}
