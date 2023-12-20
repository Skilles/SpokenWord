package net.spokenword.core.behavior;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class BehaviorUtil {

    public static void sendChatMessage(String message, @Nullable Player player) {
        if (player == null) {
            player = Minecraft.getInstance().player;
        }

        if (message.startsWith("/")) {
            ((LocalPlayer) player).connection.sendCommand(message.substring(1));
        } else {
            ((LocalPlayer) player).connection.sendChat(message.replaceAll("\"", ""));
        }
    }
}
