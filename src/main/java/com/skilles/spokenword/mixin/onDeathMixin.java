package com.skilles.spokenword.mixin;

import com.skilles.spokenword.ModConfig;
import com.skilles.spokenword.SpokenWord;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.skilles.spokenword.SpokenWord.configData;
import static com.skilles.spokenword.SpokenWord.sendMessages;

@Mixin(ClientPlayerEntity.class)
public abstract class onDeathMixin {
	boolean sent;

	@Inject(method = "handleStatus(B)V",
			at = @At(value = "HEAD"))
	private void onDeathInject(byte status, CallbackInfo ci) {
		if(configData.death && configData.enabled && status == 3 && !sent) {
			sendMessages(MinecraftClient.getInstance().player.getDisplayName().asString());
			sent = true; // precaution for 3rd party server ticks
		} else if(status == 28) {
			sent = false;
		}
	}
}

