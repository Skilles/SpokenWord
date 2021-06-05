package com.skilles.spokenword.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.CombatEventS2CPacket;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.skilles.spokenword.SpokenWord.log;
import static com.skilles.spokenword.config.ConfigManager.*;
import static com.skilles.spokenword.util.ConfigUtil.ListModes;
import static com.skilles.spokenword.util.ConfigUtil.containsEntity;
import static com.skilles.spokenword.util.Util.*;

/**
 * Modes: 'On Join', 'Death'
 */
@Mixin(ClientPlayNetworkHandler.class)
public class NetworkHandlerMixin {
    @Shadow private MinecraftClient client;

    @Shadow private ClientWorld world;

    @Inject(method = "onGameJoin", at = @At(value = "TAIL"))
    private void onGameJoin(GameJoinS2CPacket packet, CallbackInfo ci) {
        if(globalEnabled() && client.getServer() == null && modeConfig().onJoin) { // check for SMP
            sendMessages(ON_JOIN_LIST);
        }
    }
    @Inject(method = "onCombatEvent", at = @At(value = "TAIL"))
    private void onCombatInject(CombatEventS2CPacket packet, CallbackInfo ci) {
        if(globalEnabled() && modeConfig().death && packet.type == CombatEventS2CPacket.Type.ENTITY_DIED) {
            Entity attacker = world.getEntityById(packet.attackerEntityId);
            if(attacker != null) {
                if(attacker instanceof MobEntity && deathConfig().pve) {
                    if(deathConfig().entityKillList.contains("ANY") || containsEntity(attacker, ListModes.HOSTILE)) {
                        sendMessages(attacker, PVE_LIST);
                    } else {
                        log("Add mob: " + attacker.getType().getName().getString());
                    }
                } else if(attacker instanceof PlayerEntity && deathConfig().pvp) {
                    sendMessages(attacker.getDisplayName().getString(), PVP_LIST);
                }
            } else {
                sendMessages(new TranslatableText("selectWorld.world"), PVE_LIST);
            }
        }
    }
}
/**
 * Modes: 'On Break'
 * TODO: place block
 */
@Mixin(ClientPlayerInteractionManager.class)
class ClientPlayerInteractionMixin {
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "breakBlock", at = @At(value = "HEAD"))
    private void onBreakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        BlockState state = client.world.getBlockState(pos);
        if(globalEnabled() && modeConfig().onBreak && client.player.getMainHandStack().getItem().canMine(state, client.world, pos, client.player)) {
            sendMessages(state, BREAK_LIST);
        }
    }
}
