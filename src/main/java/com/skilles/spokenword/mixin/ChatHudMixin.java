package com.skilles.spokenword.mixin;

import com.skilles.spokenword.util.Util;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHudListener;
import net.minecraft.network.MessageType;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.UUID;

import static com.skilles.spokenword.SpokenWord.log;
import static com.skilles.spokenword.config.ConfigManager.*;
import static com.skilles.spokenword.util.Util.*;

/**
 * Modes: 'On Chat/Message', 'Player Join'
 */
@Environment(EnvType.CLIENT)
@Mixin(ChatHudListener.class)
public class ChatHudMixin {
    @Inject(method = "onChatMessage(Lnet/minecraft/network/MessageType;Lnet/minecraft/text/Text;Ljava/util/UUID;)V", at = @At(value = "HEAD"))
    void inject(MessageType messageType, Text message, UUID senderUuid, CallbackInfo ci) {
        if(globalEnabled()) {
            if (message instanceof TranslatableText) {
                TranslatableText tMessage = (TranslatableText) message;
                try {
                    String playerName = ((LiteralText) tMessage.getArgs()[0]).getString();
                    String currentPlayer = MinecraftClient.getInstance().player.getDisplayName().getString();
                    if (modeConfig().playerjoin && tMessage.getKey().contains("multiplayer.player.joined")) {
                        if (!playerName.equalsIgnoreCase(currentPlayer) && !message.asString().contains(currentPlayer))
                            sendMessages(playerName, PLAYER_JOIN_LIST);
                    } else if (messageType.equals(MessageType.CHAT) && chatConfig().onChat && tMessage.getKey().equals("chat.type.text")) {
                        log(tMessage);
                        log(playerName);
                        if (!playerName.equalsIgnoreCase(currentPlayer) && Util.containsCriteria(currentPlayer, tMessage, CHAT_LIST))
                            sendMessages(playerName, CHAT_LIST);
                    } else if (chatConfig().onMessage && tMessage.getKey().equals("commands.message.display.incoming")) {
                        if (!playerName.equalsIgnoreCase(currentPlayer) && Util.containsCriteria(currentPlayer, tMessage, MESSAGE_LIST))
                            sendMessages(playerName, MESSAGE_LIST);
                    }
                } catch (ClassCastException e){
                    log(Level.ERROR, "NULL attacker");
                }
            }
        }
    }
}
