package com.skilles.spokenword.mixin;

import com.mojang.authlib.properties.PropertyMap;
import com.mojang.authlib.yggdrasil.response.HasJoinedMinecraftServerResponse;
import com.skilles.spokenword.ModConfig;
import com.skilles.spokenword.SpokenWord;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.ClientChatListener;
import net.minecraft.client.gui.hud.ChatHudListener;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.MessageType;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.UUID;

import static com.skilles.spokenword.SpokenWord.configData;

@Environment(EnvType.CLIENT)
@Mixin(ChatHudListener.class)
public class ChatHudMixin {
    @Inject(method = "onChatMessage(Lnet/minecraft/network/MessageType;Lnet/minecraft/text/Text;Ljava/util/UUID;)V", at = @At(value = "HEAD"))
    void inject(MessageType messageType, Text message, UUID senderUuid, CallbackInfo ci) {
        if(configData.join && configData.enabled && message instanceof TranslatableText) {
            TranslatableText tMessage = (TranslatableText) message;
            if (tMessage.getKey().contains("multiplayer.player.joined")) {
                String playerName = ((LiteralText) tMessage.getArgs()[0]).asString();
                SpokenWord.sendMessages(playerName);
            }
        }
    }
}
