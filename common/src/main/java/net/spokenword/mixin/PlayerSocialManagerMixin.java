package net.spokenword.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.client.gui.screens.social.PlayerSocialManager;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.spokenword.SpokenWord;
import net.spokenword.core.event.EventType;
import net.spokenword.core.event.context.PlayerEventContext;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.UUID;

@Environment(EnvType.CLIENT)
@Mixin(PlayerSocialManager.class)
public class PlayerSocialManagerMixin {

    @Shadow
    @Final
    private Map<String, UUID> discoveredNamesToUUID;

    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "addPlayer(Lnet/minecraft/client/multiplayer/PlayerInfo;)V", at = @At(value = "TAIL"))
    public void onPlayerJoin(PlayerInfo playerInfo, CallbackInfo ci) {
        if (minecraft.screen instanceof ReceivingLevelScreen) {
            // This is called when initially joining a world
            return;
        }
        SpokenWord.getEventManager().dispatchEvent(EventType.PLAYER_JOIN, new PlayerEventContext(playerInfo));
    }

    @Inject(method = "removePlayer(Ljava/util/UUID;)V", at = @At(value = "TAIL"))
    public void onPlayerLeave(UUID uuid, CallbackInfo ci) {
        this.discoveredNamesToUUID.entrySet()
                                  .stream()
                                  .filter(entry -> entry.getValue().equals(uuid))
                                  .findAny()
                                  .ifPresent(entry -> SpokenWord.getEventManager().dispatchEvent(EventType.PLAYER_LEAVE, new PlayerEventContext(entry.getKey())));
    }
}
