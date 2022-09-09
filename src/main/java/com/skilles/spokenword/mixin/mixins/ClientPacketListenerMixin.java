package com.skilles.spokenword.mixin.mixins;

import com.skilles.spokenword.mixin.handlers.MixinCommands;
import com.skilles.spokenword.mixin.handlers.MixinUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;

@Environment(EnvType.CLIENT)
@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin
{

    @Inject(method = "handlePlayerCombatKill", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V", shift = At.Shift.BEFORE))
    public void onPlayerCombatKill(ClientboundPlayerCombatKillPacket packet, CallbackInfo ci)
    {
        MixinUtil.handleMixin(MixinCommands::handlePlayerCombatKill, packet.getPlayerId(), packet.getKillerId(), ci);
    }

    @Inject(method = "handleLogin", at = @At(value = "TAIL"))
    public void onLogin(ClientboundLoginPacket packet, CallbackInfo ci)
    {
        MixinUtil.handleMixin(MixinCommands::handleLogin, packet.playerId(), ci); // TODO find better injection point
    }

    @Inject(method = "handleEntityEvent", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/Entity;handleEntityEvent(B)V", shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    public void onEntityEvent(ClientboundEntityEventPacket packet, CallbackInfo ci, Entity entity)
    {
        MixinUtil.handleMixin(MixinCommands::handleEntityEvent, packet.getEventId(), entity, ci);
    }

    @SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "OptionalGetWithoutIsPresent"})
    @Inject(method = "handlePlayerChat", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/multiplayer/chat/ChatListener;handleChatMessage(Lnet/minecraft/network/chat/PlayerChatMessage;Lnet/minecraft/network/chat/ChatType$Bound;)V"),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    void onChatMessage(ClientboundPlayerChatPacket packet, CallbackInfo ci, Optional<ChatType.Bound> optional)
    {
        MixinUtil.handleMixin(MixinCommands::handleChatMessage, packet.message(), optional.get(), ci);
    }

    @Inject(method = "handleSystemChat", at = @At(value = "TAIL"))
    void onChatMessage(ClientboundSystemChatPacket packet, CallbackInfo ci)
    {
        MixinUtil.handleMixin(MixinCommands::handleSystemMessage, packet.content(), ci);
    }

}
