package net.spokenword.core.behavior;

import dev.architectury.event.events.client.ClientTickEvent;
import net.minecraft.client.Minecraft;
import net.spokenword.SpokenWord;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Uses a queue to avoid going over Minecraft's chat spam limit.
 */
public class ChatSender {

    private final Queue<String> messageQueue;

    private int currentTick = 0;

    private static final int messageTickAmount = 20;

    private static final int chatSpamTickCount = 200;

    ChatSender() {
        this.messageQueue = new LinkedList<>();
        ClientTickEvent.CLIENT_POST.register(client -> tick());
    }

    public void sendChatMessage(String message) {
        if (!SpokenWord.getConfig().antiSpamEnabled || canSafelySend()) {
            handleChatMessage(message);
            return;
        }
        messageQueue.add(message);
    }

    public void tick() {
        if (currentTick > 0) {
            --currentTick;
        }

        if (messageQueue.isEmpty()) {
            return;
        }

        if (canSafelySend()) {
            handleChatMessage(messageQueue.poll());
        }
    }

    private boolean canSafelySend() {
        return currentTick + messageTickAmount < chatSpamTickCount;
    }

    private void handleChatMessage(String message) {
        var player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        if (message.startsWith("/")) {
            player.connection.sendCommand(message.substring(1));
        } else {
            player.connection.sendChat(message.replaceAll("\"", ""));
        }
        currentTick += messageTickAmount;
    }
}
