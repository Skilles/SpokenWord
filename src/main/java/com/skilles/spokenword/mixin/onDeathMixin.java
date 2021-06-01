package com.skilles.spokenword.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.skilles.spokenword.config.ConfigManager.*;
import static com.skilles.spokenword.util.Util.*;

@Deprecated
@Mixin(ClientPlayerEntity.class)
public abstract class onDeathMixin {
	boolean sent;

	@Inject(method = "handleStatus(B)V",
			at = @At(value = "HEAD"))
	private void onDeathInject(byte status, CallbackInfo ci) {
		if(modeConfig().death && globalEnabled() && status == 3 && !sent) {
			sendMessages(PVE_LIST);
			sent = true; // precaution for 3rd party server ticks
		} else if(status == 28) {
			sent = false;
		}
	}
}

