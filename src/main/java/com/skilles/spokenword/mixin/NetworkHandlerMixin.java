package com.skilles.spokenword.mixin;

import com.skilles.spokenword.SpokenWord;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.skilles.spokenword.SpokenWord.*;

@Mixin(ClientPlayNetworkHandler.class)
public class NetworkHandlerMixin {
    @Shadow private MinecraftClient client;

    @Inject(method = "onGameJoin", at = @At(value = "TAIL"))
    private void onGameJoin(GameJoinS2CPacket packet, CallbackInfo ci) {
        if(client.getServer() == null && configData.modes.onJoin) { // check for SMP
            sendMessages(configData.onJoinList);
        }
    }
}
