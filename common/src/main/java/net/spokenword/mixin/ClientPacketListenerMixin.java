package net.spokenword.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerCombatKillPacket;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.spokenword.SpokenWord;
import net.spokenword.core.event.EventType;
import net.spokenword.core.event.context.EventContext;
import net.spokenword.core.event.context.KilledEventContext;
import net.spokenword.core.event.context.PlayerEventContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Environment(EnvType.CLIENT)
@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {

    @Inject(method = "handlePlayerCombatKill", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    void onPlayerCombatKill(ClientboundPlayerCombatKillPacket packet, CallbackInfo ci, Entity player) {
        SpokenWord.getLogger().info("Player combat kill: " + packet.getPlayerId() + " " + packet.getMessage().getString());
        var killer = ((LocalPlayer) player).getLastDamageSource().getEntity();
        var context = new KilledEventContext(killer, player);
        if (killer instanceof Player) {
            SpokenWord.getEventManager().dispatchEvent(EventType.SELF_DEATH_PVP, context);
        } else {
            SpokenWord.getEventManager().dispatchEvent(EventType.SELF_DEATH_PVE, context);
        }
    }

    // TODO find better injection point
    @Inject(method = "handleLogin", at = @At(value = "TAIL"))
    void onLogin(ClientboundLoginPacket packet, CallbackInfo ci) {
        var playerId = packet.playerId();
        if (playerId == Minecraft.getInstance().player.getId()) {
            SpokenWord.getEventManager().dispatchEvent(EventType.SELF_JOIN, EventContext.simple());
        } else {
            Minecraft.getInstance().level.players()
                                         .stream()
                                         .filter(p -> p.getId() == playerId)
                                         .findFirst()
                                         .ifPresent(p -> SpokenWord.getEventManager().dispatchEvent(EventType.PLAYER_JOIN, new PlayerEventContext(p)));
        }

    }

    @Inject(method = "handleEntityEvent", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/Entity;handleEntityEvent(B)V", shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    void onEntityEvent(ClientboundEntityEventPacket packet, CallbackInfo ci, Entity entity) {

    }

    @Inject(method = "handleSystemChat", at = @At(value = "TAIL"))
    void onChatMessage(ClientboundSystemChatPacket packet, CallbackInfo ci) {
        SpokenWord.getLogger().info("System chat message: " + packet.content().getString());
    }

    @Inject(method = "onDisconnect", at = @At(value = "HEAD"))
    void onDisconnect(Component reason, CallbackInfo ci) {
        SpokenWord.getEventManager().dispatchEvent(EventType.SELF_KICKED, EventContext.simple("reason", reason.getString()));
        // wait a bit to allow the message to be sent
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
