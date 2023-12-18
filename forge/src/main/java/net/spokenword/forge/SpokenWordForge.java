package net.spokenword.forge;

import dev.architectury.platform.forge.EventBuses;
import net.spokenword.SpokenWord;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SpokenWord.MOD_ID)
public class SpokenWordForge
{
    public SpokenWordForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(SpokenWord.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        SpokenWord.init();
    }
}
