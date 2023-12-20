package net.spokenword.mixin;

import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {

    @Inject(method = "destroyBlock", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Block;playerWillDestroy(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/entity/player/Player;)V"))
    void onDestroyBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        /*MixinUtil.handleMixin(MixinCommands::handleDestroyBlock, pos, Minecraft.getInstance().level.getBlockState(pos), cir);
        var blockState = Minecraft.getInstance().level.getBlockState(pos);
        MixinEvent.BREAK_BLOCK.invoke(new BlockEventArgs(pos, blockState));*/
    }

    @Inject(method = "performUseItemOn", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockState;use(Lnet/minecraft/world/level/Level;" +
                    "Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/phys/BlockHitResult;)" +
                    "Lnet/minecraft/world/InteractionResult;"),
            locals = LocalCapture.CAPTURE_FAILHARD)
    void onPlaceBlock(LocalPlayer localPlayer, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir, BlockPos blockPos, ItemStack itemStack, boolean bl, boolean bl2, BlockState blockState) {
        /*MixinEvent.USE_BLOCK.invoke(new BlockEventArgs(blockPos, blockState));
        MixinUtil.handleMixin(MixinCommands::handlePlaceBlock, blockPos, blockState, cir);*/
    }
}
