package net.spokenword.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.entity.Entity;
import net.spokenword.SpokenWord;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Environment(EnvType.CLIENT)
@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {

    @Inject(method = "handlePlayerCombatKill", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V", shift = At.Shift.BEFORE))
    void onPlayerCombatKill(ClientboundPlayerCombatKillPacket packet, CallbackInfo ci) {

    }

    // TODO find better injection point
    @Inject(method = "handleLogin", at = @At(value = "TAIL"))
    void onLogin(ClientboundLoginPacket packet, CallbackInfo ci) {
        /*var playerId = packet.playerId();
        var context = new EventContext();
        if (playerId == context.localPlayer().getId()) {
            EventManager.dispatchEvent(EventType.SELF_JOIN, new EventContext());
        } else {
            var player = Minecraft.getInstance().level.players().stream().filter(p -> p.getId() == playerId).findFirst();
            if (player.isEmpty()) {
                return;
            }
            EventManager.dispatchEvent(EventType.PLAYER_JOIN, EventContext.from(player.get()));
        }*/

    }

    @Inject(method = "handleEntityEvent", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/Entity;handleEntityEvent(B)V", shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    void onEntityEvent(ClientboundEntityEventPacket packet, CallbackInfo ci, Entity entity) {

    }

    @Inject(method = "handleSystemChat", at = @At(value = "TAIL"))
    void onChatMessage(ClientboundSystemChatPacket packet, CallbackInfo ci) {

    }

    @Inject(method = "onDisconnect", at = @At(value = "HEAD"))
    void onDisconnect(Component reason, CallbackInfo ci) {

    }

    @Inject(method = "handleBlockDestruction", at = @At(value = "HEAD"))
    void onBlockUpdate(ClientboundBlockDestructionPacket packet, CallbackInfo ci) {
        var pos = packet.getPos();
        var progress = packet.getProgress();
        SpokenWord.getLogger().info("Block update at " + pos + " with progress " + progress);

        // EventManager.dispatchEvent(EventType.BLOCK_UPDATE, new EventContext());
    }
}
