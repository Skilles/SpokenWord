package net.spokenword.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "die", at = @At(value = "TAIL"))
    void onEntityEvent(DamageSource source, CallbackInfo ci) {
        // var args = new EntityKilledEventArgs(source.getEntity(), source.getDirectEntity(), source.type());
        if (source.getEntity() instanceof Player) {

        } else {

        }
    }
}
