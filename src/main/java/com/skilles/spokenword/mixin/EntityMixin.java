package com.skilles.spokenword.mixin;

import com.skilles.spokenword.SpokenWord;
import com.skilles.spokenword.util.ConfigUtil;
import com.skilles.spokenword.util.Util;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.stat.StatHandler;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.skilles.spokenword.SpokenWord.log;
import static com.skilles.spokenword.util.ConfigUtil.containsEntity;
import static com.skilles.spokenword.util.Util.*;
import static com.skilles.spokenword.config.ConfigManager.*;

/**
 * Modes: 'On Owned'
 */
@Environment(EnvType.CLIENT)
@Mixin(TameableEntity.class)
public abstract class EntityMixin extends LivingEntity {

    protected EntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow public abstract boolean isTamed();

    @Inject(method = "onDeath", at = @At(value = "TAIL"))
    private void onEntityDeath(DamageSource source, CallbackInfo ci) {
        if(globalEnabled() && world.isClient() && modeConfig().onOwnedDeath && this.isTamed()) {
            sendMessages(this, OWNED_DEATH_LIST);
        }
    }
}
/**
 * Modes: 'On Owned', 'Mob Dies', TODO: 'Death'
 */
@Mixin(LivingEntity.class)
abstract class LivingEntityMixin extends Entity {

    @Shadow @Nullable public abstract LivingEntity getAttacker();

    @Shadow @Nullable private LivingEntity attacker;

    protected LivingEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "onDeath", at = @At(value = "TAIL"))
    private void onEntityDeath(DamageSource source, CallbackInfo ci) {
        if(world.isClient() && globalEnabled()) {
            if(modeConfig().onOwnedDeath && this.hasCustomName()) {
                sendMessages(this, OWNED_DEATH_LIST);
            } else if(modeConfig().onEntityDeath && (deathConfig().entityDeathList.contains("ANY") || containsEntity(this, ConfigUtil.ListModes.ALL))) {
                sendMessages(this, ENTITY_DEATH_LIST);
            }
        }
    }
}
