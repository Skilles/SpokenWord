package net.spokenword.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.spokenword.SpokenWord;
import net.spokenword.core.event.EventType;
import net.spokenword.core.event.context.BlockEventContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Environment(EnvType.CLIENT)
@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {

    @Inject(method = "destroyBlock", at = @At(value = "TAIL"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    void onDestroyBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir, Level level, BlockState blockState, Block block, FluidState fluidState, boolean broken) {
        if (broken) {
            SpokenWord.getEventManager().dispatchEvent(EventType.BLOCK_BREAK, new BlockEventContext(block, pos));
        }
    }

    @Inject(method = "performUseItemOn", at = @At(value = "TAIL"),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    void onPlaceBlock(LocalPlayer player, InteractionHand hand, BlockHitResult result, CallbackInfoReturnable<InteractionResult> cir, BlockPos blockPos, ItemStack itemStack, InteractionResult interactionResult) {
        if (interactionResult != InteractionResult.SUCCESS) {
            return;
        }
        if (itemStack.getItem() instanceof BlockItem blockItem) {
            var block = blockItem.getBlock();
            SpokenWord.getEventManager().dispatchEvent(EventType.BLOCK_PLACE, new BlockEventContext(block, blockPos));
        }
    }
}
