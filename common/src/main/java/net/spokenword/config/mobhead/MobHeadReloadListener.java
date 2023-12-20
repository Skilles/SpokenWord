package net.spokenword.config.mobhead;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dev.architectury.registry.ReloadListenerRegistry;
import net.minecraft.ResourceLocationException;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.spokenword.SpokenWord;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class MobHeadReloadListener implements PreparableReloadListener {

    private static final ResourceLocation MOB_HEADS = new ResourceLocation(SpokenWord.MOD_ID, "data/mob_heads.json");

    public static void register() {
        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, new MobHeadReloadListener(), new ResourceLocation(SpokenWord.MOD_ID, "mob_heads"));
    }

    @Override
    public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
        return CompletableFuture.runAsync(() ->
        {
            var mobHeadResource = resourceManager.getResource(MOB_HEADS);
            if (mobHeadResource.isEmpty()) {
                SpokenWord.LOGGER.warning("Could not find mob heads resource file");
                return;
            }

            try (var reader = mobHeadResource.get().openAsReader()) {
                Gson gson = new Gson();

                var typeToken = new TypeToken<Map<String, MobHeads.MobHead>>() {
                }.getType();
                Map<String, MobHeads.MobHead> data = gson.fromJson(reader, typeToken);

                MobHeads.reload();

                for (var entry : data.entrySet()) {
                    try {
                        var key = entry.getKey();
                        var keyParts = key.split(":");
                        var namespace = keyParts[0];
                        var keyPath = keyParts[1];
                        if (Character.isDigit(keyPath.charAt(0))) {
                            // Remove the numbers and _ at the beginning of the key
                            keyPath = keyPath.substring(keyPath.indexOf('_') + 1);
                            key = namespace + ":" + keyPath;
                        }

                        var resourceLocation = new ResourceLocation(key);

                        if (!BuiltInRegistries.ENTITY_TYPE.containsKey(resourceLocation)) {
                            SpokenWord.LOGGER.warning("Found invalid entry " + entry.getKey());
                            continue;
                        }

                        var entityType = BuiltInRegistries.ENTITY_TYPE.get(resourceLocation);
                        var mobHead = entry.getValue();
                        MobHeads.registerHead(entityType, mobHead);
                    } catch (ResourceLocationException e) {
                        SpokenWord.LOGGER.warning("Could not find entity type " + entry.getKey());
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, backgroundExecutor).thenCompose(preparationBarrier::wait);
    }
}
