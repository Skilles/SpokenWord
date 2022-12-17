package com.skilles.spokenword;

import com.mojang.blaze3d.platform.InputConstants;
import com.skilles.spokenword.behaviors.BehaviorManager;
import com.skilles.spokenword.config.SWConfig;
import com.skilles.spokenword.config.SWConfigData;
import com.skilles.spokenword.config.custom.dropdown.entity.EntityDropdown;
import com.skilles.spokenword.config.custom.dropdown.entity.EntitySelectionGuiProvider;
import com.skilles.spokenword.config.custom.regex.RegexGuiProvider;
import com.skilles.spokenword.config.custom.regex.RegexList;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.gui.registry.GuiRegistry;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.lifecycle.api.client.event.ClientTickEvents;

@ClientOnly
public class SpokenWordClient implements ClientModInitializer, ModMenuApi
{

    public static BehaviorManager BEHAVIOR_MANAGER;

    private static KeyMapping DEBUG_PRINT_BEHAVIORS_KEYBINDING;

    @Override
    public void onInitializeClient(ModContainer mod)
    {
        GuiRegistry registry = AutoConfig.getGuiRegistry(SWConfigData.class);

        registry.registerAnnotationProvider(new EntitySelectionGuiProvider(), EntityDropdown.class);
        registry.registerAnnotationProvider(new RegexGuiProvider(), RegexList.class);

        BEHAVIOR_MANAGER = new BehaviorManager();

        DEBUG_PRINT_BEHAVIORS_KEYBINDING = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.spokenword.debug_print_behaviors",
            InputConstants.Type.KEYSYM,
                        InputConstants.KEY_NUMPAD1, "key.categories.misc"));

        ClientTickEvents.END.register(client ->
        {
            while (DEBUG_PRINT_BEHAVIORS_KEYBINDING.consumeClick())
            {
                BEHAVIOR_MANAGER.printBehaviors();
            }
        });

        BEHAVIOR_MANAGER.init(SWConfig.get());
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory()
    {
        return parent -> AutoConfig.getConfigScreen(SWConfigData.class, parent).get();
    }

}
