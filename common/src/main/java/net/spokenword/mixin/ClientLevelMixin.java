package net.spokenword.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {

    /**
     * Two mixins are needed to handle disconnect. This one handles the case where the player themselves disconnects.
     */
    @Inject(method = "disconnect", at = @At(value = "HEAD"))
    void onDisconnect(CallbackInfo ci) {

    }
}
