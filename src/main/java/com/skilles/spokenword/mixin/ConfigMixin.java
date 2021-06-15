package com.skilles.spokenword.mixin;

import com.skilles.spokenword.util.ConfigUtil;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import me.shedaniel.clothconfig2.gui.AbstractConfigScreen;
import me.shedaniel.clothconfig2.gui.entries.DropdownBoxEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.skilles.spokenword.SpokenWord.log;

/**
 * To clear/reinit temp lists when quitting without saving
 */
@Mixin(ConfirmScreen.class)
abstract class QuitMixin extends Screen {

    @Final
    @Shadow
    protected BooleanConsumer callback;

    @Shadow
    protected Text yesTranslated;

    @Shadow
    protected Text noTranslated;

    protected QuitMixin(Text title) {
        super(title);
    }

    @Inject(method = "init()V", at = @At(value = "TAIL"))
    private void quitInject(CallbackInfo ci) {
        if(((TranslatableText) title).getKey().equals("text.cloth-config.quit_config")) {
            children.clear();
            this.addButton(new ButtonWidget(this.width / 2 - 155, this.height / 6 + 96, 150, 20, this.yesTranslated, (buttonWidget) -> {
                this.callback.accept(true);
                ConfigUtil.tempAllEntityList.clear();
                ConfigUtil.tempHostileEntityList.clear();
                ConfigUtil.initEntities();
            }));
            this.addButton(new ButtonWidget(this.width / 2 - 155 + 160, this.height / 6 + 96, 150, 20, this.noTranslated, (buttonWidget) -> {
                this.callback.accept(false);
            }));

        }
    }
}
/**
 * To clear/reinit temp lists when pressing reset button
 */
@Pseudo
@Mixin(value = DropdownBoxEntry.class)
abstract class ResetMixin<String> {
    @Shadow protected ButtonWidget resetButton;
    @Shadow protected DropdownBoxEntry.SelectionElement<String> selectionElement;

    @Inject(method = "<init>(Lnet/minecraft/text/Text;Lnet/minecraft/text/Text;Ljava/util/function/Supplier;ZLjava/util/function/Supplier;Ljava/util/function/Consumer;Ljava/lang/Iterable;Lme/shedaniel/clothconfig2/gui/entries/DropdownBoxEntry$SelectionTopCellElement;Lme/shedaniel/clothconfig2/gui/entries/DropdownBoxEntry$SelectionCellCreator;)V", at = @At(value = "TAIL"))
    private void constructorInject(Text fieldName, @NotNull Text resetButtonKey, @Nullable Supplier<Optional<Text[]>> tooltipSupplier, boolean requiresRestart, @Nullable Supplier<String> defaultValue, @Nullable Consumer<String> saveConsumer, @Nullable Iterable<String> selections, DropdownBoxEntry.@NotNull SelectionTopCellElement<String> topRenderer, DropdownBoxEntry.@NotNull SelectionCellCreator<String> cellCreator, CallbackInfo ci) {
        if(Objects.equals(selections, ConfigUtil.modeToList(ConfigUtil.ListModes.HOSTILE))) {
            resetButton = new ButtonWidget(0, 0, MinecraftClient.getInstance().textRenderer.getWidth(resetButtonKey) + 6, 20, resetButtonKey, (widget) -> {
                selectionElement.getTopRenderer().setValue(defaultValue != null ? defaultValue.get() : null);
                ConfigUtil.tempHostileEntityList.clear();
            });
        } else if(Objects.equals(selections, ConfigUtil.modeToList(ConfigUtil.ListModes.ALL))) {
            resetButton = new ButtonWidget(0, 0, MinecraftClient.getInstance().textRenderer.getWidth(resetButtonKey) + 6, 20, resetButtonKey, (widget) -> {
                selectionElement.getTopRenderer().setValue(defaultValue != null ? defaultValue.get() : null);
                ConfigUtil.tempAllEntityList.clear();
            });
        }
    }
}
