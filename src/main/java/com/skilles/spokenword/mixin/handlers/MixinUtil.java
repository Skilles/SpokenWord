package com.skilles.spokenword.mixin.handlers;

import com.skilles.spokenword.config.ConfigUtil;
import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@ClientOnly
public class MixinUtil
{

    public static <T, U> void handleMixin(TriConsumer<T, U, CallbackInfo> func, T arg1, U arg2, CallbackInfo ci)
    {
        if (ConfigUtil.isEnabled())
        {
            func.accept(arg1, arg2, ci);
        }
    }

    public static <T> void handleMixin(BiConsumer<T, CallbackInfo> func, T arg1, CallbackInfo ci)
    {
        if (ConfigUtil.isEnabled())
        {
            func.accept(arg1, ci);
        }
    }

    public static void handleMixin(Consumer<CallbackInfo> func, CallbackInfo ci)
    {
        if (ConfigUtil.isEnabled())
        {
            func.accept(ci);
        }
    }

    @FunctionalInterface
    public interface TriConsumer<A, B, C>
    {

        void accept(A a, B b, C c);

    }

}
