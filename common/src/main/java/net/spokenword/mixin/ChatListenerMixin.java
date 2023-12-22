package net.spokenword.mixin;

import com.mojang.authlib.GameProfile;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.chat.ChatListener;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.PlayerChatMessage;
import net.spokenword.SpokenWord;
import net.spokenword.core.event.EventType;
import net.spokenword.core.event.context.ChatEventContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ChatListener.class)
public class ChatListenerMixin {

    // Used in vanilla SMP servers
    @Inject(method = "handlePlayerChatMessage(Lnet/minecraft/network/chat/PlayerChatMessage;Lcom/mojang/authlib/GameProfile;Lnet/minecraft/network/chat/ChatType$Bound;)V", at = @At(value = "TAIL"))
    void handlePlayerChatMessage(PlayerChatMessage chatMessage, GameProfile gameProfile, ChatType.Bound boundChatType, CallbackInfo ci) {
        SpokenWord.getLogger().info("Player chat message: " + chatMessage.signedContent());
        if (gameProfile.getId().equals(Minecraft.getInstance().player.getUUID())) {
            return;
        }
        if (boundChatType.chatType().chat().translationKey().equals("commands.message.display.incoming")) {
            SpokenWord.getEventManager().dispatchEvent(EventType.PLAYER_MESSAGE, new ChatEventContext(chatMessage.signedContent(), gameProfile.getName()));
            return;
        }
        SpokenWord.getEventManager().dispatchEvent(EventType.PLAYER_CHAT, new ChatEventContext(chatMessage.signedContent(), gameProfile.getName()));
    }
}
