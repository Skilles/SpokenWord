package com.skilles.spokenword;

import com.skilles.spokenword.config.SWConfig;
import com.skilles.spokenword.config.SWConfigData;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public class SpokenWord implements ModInitializer
{

    public static final EntityType<?> DEFAULT_ENTITY = EntityType.LEASH_KNOT;

    public static Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize(ModContainer mod)
    {
        var holder = AutoConfig.register(SWConfigData.class, GsonConfigSerializer::new);

        SWConfig.set(holder.getConfig());

        AutoConfig.getConfigHolder(SWConfigData.class).registerSaveListener((manager, data) ->
        {
            SWConfig.onSaveConfig(manager, data);
            return InteractionResult.SUCCESS;
        });

        AutoConfig.getConfigHolder(SWConfigData.class).registerLoadListener((manager, newData) ->
        {
            SWConfig.onLoadConfig(manager, newData);
            return InteractionResult.SUCCESS;
        });
    }

    public static void log(Object message)
    {
        log(String.valueOf(message));
    }

    public static void log(String message)
    {
        log(Level.INFO, message);
    }

    public static void log(Level level, String message)
    {
        LOGGER.log(level, "[SpokenWord] " + message);
    }

}
