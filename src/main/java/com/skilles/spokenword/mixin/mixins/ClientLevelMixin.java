package com.skilles.spokenword.mixin.mixins;

import com.skilles.spokenword.mixin.handlers.MixinCommands;
import com.skilles.spokenword.mixin.handlers.MixinUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClientLevel.class)
public class ClientLevelMixin
{

    /**
     * Two mixins needed to handle disconnect (one for leave and one for kick)
     */
    @Inject(method = "disconnect", at = @At(value = "HEAD"))
    void onDisconnect(CallbackInfo ci)
    {
        MixinUtil.handleMixin(MixinCommands::handleDisconnect, Component.translatable("multiplayer.status.quitting"), ci);
    }
}
