package net.spokenword.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.stats.Stat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Player.class)
public abstract class ClientPlayerMixin {

    @Shadow
    public int experienceLevel;

    @Inject(method = "giveExperienceLevels(I)V", at = @At("TAIL"))
    void onLevelUp(int levels, CallbackInfo ci) {
        if (levels > 0 && this.experienceLevel > 0) {

        }
    }

    @Inject(method = "awardStat(Lnet/minecraft/stats/Stat;)V", at = @At("TAIL"))
    void awardStat(Stat<?> stat, CallbackInfo ci) {
        var statName = stat.getType().getDisplayName().getString();
        if (statName.equals("Used Item")) {
            var item = (Item) stat.getValue();
        }
    }
}
