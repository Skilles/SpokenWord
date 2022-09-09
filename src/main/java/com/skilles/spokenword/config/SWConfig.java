package com.skilles.spokenword.config;

import com.skilles.spokenword.config.custom.entity.ListModes;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EntityType;

import java.util.Collection;

public class SWConfig
{

    private static SWConfigData CONFIG;

    public static void set(SWConfigData config)
    {
        CONFIG = config;
    }

    public static SWConfigData get()
    {
        return CONFIG;
    }

    public static boolean isEnabled()
    {
        return CONFIG.general.globalEnable && isEnabledServer();
    }

    public static boolean isEnabledServer()
    {
        var whitelistedServers = CONFIG.general.ipFilter;

        if (whitelistedServers.size() == 0)
        {
            return true;
        }

        var server = Minecraft.getInstance().getCurrentServer();

        if (server == null)
        {
            return true;
        }

        return whitelistedServers.contains(server.ip);
    }

    public static void saveEntities(Collection<EntityType<?>> entities, ListModes mode)
    {
        var entityNames = ConfigUtil.entitiesToKeys(entities);

        if (mode.equals(ListModes.ALL))
        {
            CONFIG.filters.onEntityDeathFilter = entityNames;
        }
        else
        {
            CONFIG.filters.onDeathPveFilter = entityNames;
        }
    }

}
