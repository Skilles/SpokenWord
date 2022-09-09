package com.skilles.spokenword.config;

import com.skilles.spokenword.behaviors.actions.RespawnAction;
import com.skilles.spokenword.behaviors.annotations.BehaviorAction;
import com.skilles.spokenword.behaviors.annotations.BehaviorFilter;
import com.skilles.spokenword.behaviors.annotations.BehaviorMessage;
import com.skilles.spokenword.behaviors.annotations.BehaviorToggle;
import com.skilles.spokenword.config.custom.entity.EntityDropdown;
import com.skilles.spokenword.config.custom.entity.ListModes;
import com.skilles.spokenword.config.custom.regex.RegexList;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.minecraft.world.entity.EntityType;

import java.util.List;

@Config(name = "spokenword")
public class SWConfigData implements ConfigData
{

    @ConfigEntry.Category("general")
    @ConfigEntry.Gui.TransitiveObject
    public GeneralGroup general = new GeneralGroup();

    @ConfigEntry.Category("messages")
    @ConfigEntry.Gui.CollapsibleObject
    public Toggles toggles = new Toggles();

    @ConfigEntry.Category("messages")
    @ConfigEntry.Gui.CollapsibleObject
    public Filters filters = new Filters();

    @ConfigEntry.Category("actions")
    @ConfigEntry.Gui.TransitiveObject
    public Actions actions = new Actions();

    public static class GeneralGroup
    {

        @ConfigEntry.Gui.Tooltip
        public boolean globalEnable = true;

        @ConfigEntry.Gui.Tooltip

        public List<String> ipFilter = List.of();

    }

    public static class Actions
    {

        @ConfigEntry.Gui.Tooltip
        @BehaviorAction(id = "respawn", behavior = RespawnAction.class)
        public boolean respawn = false;

    }

    public static class Toggles
    {

        @ConfigEntry.Gui.Tooltip
        @BehaviorToggle("otherJoin")
        public boolean onPlayerJoin = true;

        @ConfigEntry.Gui.Tooltip
        @BehaviorMessage("otherJoin")
        public List<String> onPlayerJoinList = List.of("Welcome %s!");

        @ConfigEntry.Gui.Tooltip
        @BehaviorToggle("selfJoin")
        public boolean onJoin = false;

        @ConfigEntry.Gui.Tooltip
        @BehaviorMessage("selfJoin")
        public List<String> onJoinList = List.of("I, %p, have joined the server");

        @ConfigEntry.Gui.Tooltip
        @BehaviorToggle("selfDeath")
        public boolean onDeath = true;

        @ConfigEntry.Gui.Tooltip
        @BehaviorMessage("selfDeath")
        public List<String> onDeathList = List.of();

        @ConfigEntry.Gui.Tooltip
        @BehaviorToggle("blockBreak")
        public boolean onBreak = false;

        @ConfigEntry.Gui.Tooltip
        @BehaviorMessage("blockBreak")
        public List<String> onBreakList = List.of("I have broken %b");

        @ConfigEntry.Gui.Tooltip
        @BehaviorToggle("ownedEntityDeath")
        public boolean onOwnedDeath = true;

        @ConfigEntry.Gui.Tooltip
        @BehaviorMessage("ownedEntityDeath")
        public List<String> onOwnedDeathList = List.of("%e has died. RIP");

        @ConfigEntry.Gui.Tooltip
        @BehaviorToggle("entityDeath")
        public boolean onEntityDeath = false;

        @ConfigEntry.Gui.Tooltip
        @BehaviorMessage("entityDeath")
        public List<String> onEntityDeathList = List.of("%e has died");

        @ConfigEntry.Gui.Tooltip
        @BehaviorToggle("selfDeathPvp")
        public boolean onDeathPvp = true;

        @ConfigEntry.Gui.Tooltip
        @BehaviorMessage("selfDeathPvp")
        public List<String> onDeathPvpList = List.of("%p has killed me");

        @ConfigEntry.Gui.Tooltip
        @BehaviorToggle("selfDeathPve")
        public boolean onDeathPve = false;

        @ConfigEntry.Gui.Tooltip
        @BehaviorMessage("selfDeathPve")
        public List<String> onDeathPveList = List.of("%e has killed me");

        @ConfigEntry.Gui.Tooltip
        @BehaviorToggle("otherMessage")
        public boolean onMessage = true;

        @ConfigEntry.Gui.Tooltip
        @BehaviorMessage("otherMessage")
        public List<String> onMessageList = List.of("/msg %s what's up");

        @BehaviorToggle("otherChat")
        @ConfigEntry.Gui.Tooltip
        public boolean onChat = false;

        @ConfigEntry.Gui.Tooltip
        @BehaviorMessage("otherChat")
        public List<String> onChatList = List.of("Oi!");

    }

    public static class Filters
    {

        @BehaviorFilter("entityDeath")
        @EntityDropdown(mode = ListModes.ALL)
        @ConfigEntry.Gui.PrefixText
        @ConfigEntry.Gui.Tooltip
        public List<String> onEntityDeathFilter = List.of(ConfigUtil.entityToString(EntityType.VILLAGER));

        @BehaviorFilter("selfDeathPve")
        @EntityDropdown(mode = ListModes.HOSTILE)
        @ConfigEntry.Gui.PrefixText
        @ConfigEntry.Gui.Tooltip
        public List<String> onDeathPveFilter = List.of();

        @BehaviorFilter("otherChat")
        @RegexList
        @ConfigEntry.Gui.Tooltip
        public List<String> onChatFilter = List.of("%p");

        @BehaviorFilter("otherMessage")
        @RegexList
        @ConfigEntry.Gui.Tooltip
        public List<String> onMessageFilter = List.of("%p");

    }

}
