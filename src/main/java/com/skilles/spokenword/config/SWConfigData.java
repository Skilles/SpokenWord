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
        public boolean onPlayerJoin = false;

        @ConfigEntry.Gui.Tooltip
        @BehaviorMessage("otherJoin")
        public List<String> onPlayerJoinList = List.of("Welcome %s!");

        @ConfigEntry.Gui.Tooltip
        @BehaviorToggle("otherLeave")
        public boolean onPlayerLeave = false;

        @ConfigEntry.Gui.Tooltip
        @BehaviorMessage("otherLeave")
        public List<String> onPlayerLeaveList = List.of("You will be missed %s");

        @ConfigEntry.Gui.Tooltip
        @BehaviorToggle("selfJoin")
        public boolean onJoin = false;

        @ConfigEntry.Gui.Tooltip
        @BehaviorMessage("selfJoin")
        public List<String> onJoinList = List.of("I, %p, have joined the server");

        @ConfigEntry.Gui.Tooltip
        @BehaviorToggle("selfLeave")
        public boolean onLeave = false;

        @ConfigEntry.Gui.Tooltip
        @BehaviorMessage("selfLeave")
        public List<String> onLeaveList = List.of("I, %p, am leaving the server");

        @ConfigEntry.Gui.Tooltip
        @BehaviorToggle("selfDeath")
        public boolean onDeath = false;

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
        public boolean onOwnedDeath = false;

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
        public boolean onDeathPvp = false;

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
        public boolean onMessage = false;

        @ConfigEntry.Gui.Tooltip
        @BehaviorMessage("otherMessage")
        public List<String> onMessageList = List.of("/msg %s what's up");

        @BehaviorToggle("otherChat")
        @ConfigEntry.Gui.Tooltip
        public boolean onChat = false;

        @ConfigEntry.Gui.Tooltip
        @BehaviorMessage("otherChat")
        public List<String> onChatList = List.of("Oi!");
        
        @BehaviorToggle("blockPlace")
        @ConfigEntry.Gui.Tooltip
        public boolean onBlockPlace = false;

        @ConfigEntry.Gui.Tooltip
        @BehaviorMessage("blockPlace")
        public List<String> onBlockPlaceList = List.of("I have placed %b");

        @BehaviorToggle("dimChange")
        @ConfigEntry.Gui.Tooltip
        public boolean onDimChange = false;

        @ConfigEntry.Gui.Tooltip
        @BehaviorMessage("dimChange")
        public List<String> onDimChangeList = List.of("I am now in the %d dimension");

        @BehaviorToggle("killPvp")
        @ConfigEntry.Gui.Tooltip
        public boolean onKillPvp = false;

        @ConfigEntry.Gui.Tooltip
        @BehaviorMessage("killPvp")
        public List<String> onKillPvpList = List.of("I have slain %s");

        @BehaviorToggle("killPve")
        @ConfigEntry.Gui.Tooltip
        public boolean onKillPve = false;

        @ConfigEntry.Gui.Tooltip
        @BehaviorMessage("killPve")
        public List<String> onKillPveList = List.of("I have slain %e");

        /* TODO
            onRespawn
            onAdvancement
            onExplosion
            onItemDrop
            onItemPickup
            onVillagerInfected
         */

        @BehaviorToggle("reachLevel")
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = -1, max = 100)
        public int onReachLevel = -1;

        @ConfigEntry.Gui.Tooltip
        @BehaviorMessage(value = "reachLevel", isAdvanced = true)
        public List<String> onReachLevelList = List.of("I have reached level %l");
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
