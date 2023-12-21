package net.spokenword.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.spokenword.SpokenWord;
import net.spokenword.core.event.EventType;
import net.spokenword.core.event.context.EventContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClientLevel.class)
public class ClientLevelMixin {

    /**
     * Two mixins are needed to handle disconnect. This one handles the case where the player themselves disconnects.
     */
    @Inject(method = "disconnect", at = @At(value = "HEAD"))
    void onDisconnect(CallbackInfo ci) {
        SpokenWord.getEventManager().dispatchEvent(EventType.SELF_LEAVE, EventContext.simple());
        // wait a bit to allow the message to be sent
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
