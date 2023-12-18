package net.spokenword.quilt;

import net.spokenword.fabriclike.SpokenWordFabricLike;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public class SpokenWordQuilt implements ModInitializer {
    @Override
    public void onInitialize(ModContainer mod) {
        SpokenWordFabricLike.init();
    }
}
