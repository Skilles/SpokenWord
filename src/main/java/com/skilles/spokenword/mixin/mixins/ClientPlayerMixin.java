package com.skilles.spokenword.mixin.mixins;

import com.skilles.spokenword.mixin.handlers.MixinCommands;
import com.skilles.spokenword.mixin.handlers.MixinUtil;
import net.minecraft.world.entity.player.Player;
import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@ClientOnly
@Mixin(Player.class)
public abstract class ClientPlayerMixin
{

    @Shadow public int experienceLevel;

    @Inject(method = "giveExperienceLevels(I)V", at = @At("TAIL"))
    void onLevelUp(int levels, CallbackInfo ci)
    {
        if (levels > 0 && this.experienceLevel > 0)
        {
            MixinUtil.handleMixin(MixinCommands::handleLevelUp, this.experienceLevel, ci);
        }
    }
}
