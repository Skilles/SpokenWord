package net.spokenword.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.level.Level;
import net.spokenword.SpokenWord;
import net.spokenword.core.event.EventType;
import net.spokenword.core.event.context.EntityEventContext;
import net.spokenword.core.event.context.KilledEventContext;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow
    private @Nullable DamageSource lastDamageSource;

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "die", at = @At(value = "TAIL"))
    void onDeath(DamageSource source, CallbackInfo ci) {
        if (!this.level().isClientSide) {
            return;
        }

        var killer = this.lastDamageSource == null ? null : this.lastDamageSource.getEntity();
        var killedContext = new KilledEventContext(killer, this);


        var eventManager = SpokenWord.getEventManager();

        var isPlayer = this.getType().equals(EntityType.PLAYER);

        var isKillerLocal = killer instanceof LocalPlayer;

        if (isKillerLocal) {
            // Something else died and we killed it
            if (isPlayer) {
                eventManager.dispatchEvent(EventType.KILLED_PVP, killedContext);
            } else {
                eventManager.dispatchEvent(EventType.KILLED_PVE, killedContext);
            }
        }

        if (!isPlayer) {
            // Something else died and it's not a player
            var entityContext = new EntityEventContext(this);
            eventManager.dispatchEvent(EventType.ENTITY_DEATH, entityContext);
            if (this instanceof OwnableEntity ownableEntity && ownableEntity.getOwner().equals(Minecraft.getInstance().player)) {
                eventManager.dispatchEvent(EventType.OWNED_DEATH, entityContext);
            }
        }
    }
}
