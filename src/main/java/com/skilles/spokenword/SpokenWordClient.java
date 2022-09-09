package com.skilles.spokenword;

import com.skilles.spokenword.behaviors.BehaviorManager;
import com.skilles.spokenword.config.SWConfig;
import com.skilles.spokenword.config.SWConfigData;
import com.skilles.spokenword.config.custom.entity.EntityDropdown;
import com.skilles.spokenword.config.custom.entity.EntityGuiProvider;
import com.skilles.spokenword.config.custom.regex.RegexGuiProvider;
import com.skilles.spokenword.config.custom.regex.RegexList;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.gui.registry.GuiRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

@Environment(EnvType.CLIENT)
public class SpokenWordClient implements ClientModInitializer, ModMenuApi
{

    public static BehaviorManager BEHAVIOR_MANAGER;

    @Override
    public void onInitializeClient(ModContainer mod)
    {
        GuiRegistry registry = AutoConfig.getGuiRegistry(SWConfigData.class);

        registry.registerAnnotationProvider(new EntityGuiProvider(), EntityDropdown.class);
        registry.registerAnnotationProvider(new RegexGuiProvider(), RegexList.class);

        BEHAVIOR_MANAGER = new BehaviorManager();

        BEHAVIOR_MANAGER.init(SWConfig.get());
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory()
    {
        return parent -> AutoConfig.getConfigScreen(SWConfigData.class, parent).get();
    }

}
