package net.spokenword.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.player.LocalPlayer;
import net.spokenword.SpokenWord;
import net.spokenword.core.event.EventType;
import net.spokenword.core.event.context.EventContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {

    // respawn does not call super, so we can't use PlayerMixin
    @Inject(method = "respawn", at = @At("TAIL"))
    void onRespawn(CallbackInfo ci) {
        SpokenWord.getEventManager().dispatchEvent(EventType.SELF_RESPAWN, EventContext.simple());
    }
}
