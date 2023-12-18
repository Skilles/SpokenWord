package net.spokenword.fabric;

import net.spokenword.fabriclike.SpokenWordFabricLike;
import net.fabricmc.api.ModInitializer;

public class SpokenWordFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        SpokenWordFabricLike.init();
    }
}
