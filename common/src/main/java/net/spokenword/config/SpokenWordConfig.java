package net.spokenword.config;

import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.autogen.Boolean;
import dev.isxander.yacl3.config.v2.api.autogen.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.spokenword.SpokenWord;
import net.spokenword.config.autoconfig.BlockListGroup;
import net.spokenword.config.autoconfig.EntityListGroup;
import net.spokenword.config.autoconfig.EventListenerOption;
import net.spokenword.config.autoconfig.StringListGroup;
import net.spokenword.core.event.EventType;

import java.util.List;

public class SpokenWordConfig {

    public static final String FILE_NAME = "spokenword.json5";

    private final String MAIN_CATEGORY = "main";
    private final String TOGGLES_CATEGORY = "toggles";
    private final String FILTERS_CATEGORY = "filters";
    private final String MESSAGES_CATEGORY = "messages";
    private final String ADVANCED_CATEGORY = "advanced";
    private final String SELF_GROUP = "self";
    private final String ENTITY_GROUP = "entity";
    private final String BLOCK_GROUP = "block";
    private final String ITEM_GROUP = "item";
    private final String CHAT_GROUP = "chat";
    private final String TRIGGER_GROUP = "trigger";
    private final String PLAYER_GROUP = "player";
    private final String MISC_CATEGORY = "misc";

    @AutoGen(category = MAIN_CATEGORY)
    @SerialEntry
    @Boolean(formatter = Boolean.Formatter.ON_OFF)
    @CustomName(SpokenWord.MOD_ID + ".config.globalEnabled")
    @CustomDescription(SpokenWord.MOD_ID + ".config.globalEnabled.desc")
    public boolean globalEnabled = true;

    @AutoGen(category = MAIN_CATEGORY)
    @SerialEntry
    @Boolean(formatter = Boolean.Formatter.ON_OFF)
    @CustomName(SpokenWord.MOD_ID + ".config.antiSpamEnabled")
    @CustomDescription(SpokenWord.MOD_ID + ".config.antiSpamEnabled.desc")
    public boolean antiSpamEnabled = true;

    @AutoGen(category = MAIN_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = StringListGroup.class, valueFactory = StringListGroup.class, addEntriesToBottom = true)
    @CustomName(SpokenWord.MOD_ID + ".config.ipFilter")
    @CustomDescription(SpokenWord.MOD_ID + ".config.ipFilter.desc")
    public List<String> ipFilter = List.of();

    @AutoGen(category = MESSAGES_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = StringListGroup.class, valueFactory = StringListGroup.class, addEntriesToBottom = true)
    @CustomName(SpokenWord.MOD_ID + ".config.onSelfJoinMessage")
    public List<String> onSelfJoinMessage = List.of("Hello it is me %player%!");

    @AutoGen(category = TOGGLES_CATEGORY, group = SELF_GROUP)
    @SerialEntry
    @MasterTickBox(value = "onSelfJoinMessage")
    @CustomName(SpokenWord.MOD_ID + ".config.onSelfJoinEnabled")
    @EventListenerOption(EventType.SELF_JOIN)
    public boolean onSelfJoinEnabled = false;

    @AutoGen(category = MESSAGES_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = StringListGroup.class, valueFactory = StringListGroup.class, addEntriesToBottom = true)
    @CustomName(SpokenWord.MOD_ID + ".config.onSelfLeaveMessage")
    public List<String> onSelfLeaveMessage = List.of("Goodbye, %player%!");

    @AutoGen(category = TOGGLES_CATEGORY, group = SELF_GROUP)
    @SerialEntry
    @MasterTickBox(value = "onSelfLeaveMessage")
    @CustomName(SpokenWord.MOD_ID + ".config.onSelfLeaveEnabled")
    @EventListenerOption(EventType.SELF_LEAVE)
    public boolean onSelfLeaveEnabled = false;

    @AutoGen(category = TOGGLES_CATEGORY, group = SELF_GROUP)
    @SerialEntry
    @MasterTickBox(value = "onKillPveMessage")
    @CustomName(SpokenWord.MOD_ID + ".config.onKillPveEnabled")
    @EventListenerOption(EventType.KILLED_PVE)
    public boolean onKillPveEnabled = false;

    @AutoGen(category = MESSAGES_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = StringListGroup.class, valueFactory = StringListGroup.class, addEntriesToBottom = true)
    @CustomName(SpokenWord.MOD_ID + ".config.onKillPvpMessage")
    public List<String> onKillPvpMessage = List.of("I have slain %player%");

    @AutoGen(category = FILTERS_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = StringListGroup.class, valueFactory = StringListGroup.class, addEntriesToBottom = true)
    @CustomName(SpokenWord.MOD_ID + ".config.onKillPvpFilter")
    public List<String> onKillPvpFilter = List.of();

    @AutoGen(category = TOGGLES_CATEGORY, group = SELF_GROUP)
    @SerialEntry
    @MasterTickBox(value = "onKillPvpMessage")
    @CustomName(SpokenWord.MOD_ID + ".config.onKillPvpEnabled")
    @EventListenerOption(EventType.KILLED_PVP)
    public boolean onKillPvpEnabled = false;

    @AutoGen(category = MESSAGES_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = StringListGroup.class, valueFactory = StringListGroup.class, addEntriesToBottom = true)
    @CustomName(SpokenWord.MOD_ID + ".config.onSelfDeathPveMessage")
    public List<String> onSelfDeathPveMessage = List.of("%entity% has killed me");

    @AutoGen(category = FILTERS_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = EntityListGroup.class, valueFactory = EntityListGroup.class, addEntriesToBottom = true)
    @CustomName(SpokenWord.MOD_ID + ".config.onSelfDeathPveFilter")
    public List<EntityType<?>> onSelfDeathPveFilter = List.of();

    @AutoGen(category = TOGGLES_CATEGORY, group = SELF_GROUP)
    @SerialEntry
    @MasterTickBox(value = "onSelfDeathPveMessage")
    @CustomName(SpokenWord.MOD_ID + ".config.onSelfDeathPveEnabled")
    @EventListenerOption(EventType.SELF_DEATH_PVE)
    public boolean onSelfDeathPveEnabled = false;

    @AutoGen(category = MESSAGES_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = StringListGroup.class, valueFactory = StringListGroup.class, addEntriesToBottom = true)
    @CustomName(SpokenWord.MOD_ID + ".config.onSelfDeathPvpMessage")
    public List<String> onSelfDeathPvpMessage = List.of("%player% has killed me");

    @AutoGen(category = FILTERS_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = StringListGroup.class, valueFactory = StringListGroup.class, addEntriesToBottom = true)
    @CustomName(SpokenWord.MOD_ID + ".config.onSelfDeathPvpFilter")
    public List<String> onSelfDeathPvpFilter = List.of();

    @AutoGen(category = TOGGLES_CATEGORY, group = SELF_GROUP)
    @SerialEntry
    @MasterTickBox(value = "onSelfDeathPvpMessage")
    @CustomName(SpokenWord.MOD_ID + ".config.onSelfDeathPvpEnabled")
    @EventListenerOption(EventType.SELF_DEATH_PVP)
    public boolean onSelfDeathPvpEnabled = false;

    @AutoGen(category = MESSAGES_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = StringListGroup.class, valueFactory = StringListGroup.class, addEntriesToBottom = true)
    @CustomName(SpokenWord.MOD_ID + ".config.onRespawnMessage")
    public List<String> onRespawnMessage = List.of("I am reborn!");

    @AutoGen(category = TOGGLES_CATEGORY, group = SELF_GROUP)
    @SerialEntry
    @MasterTickBox(value = "onRespawnMessage")
    @CustomName(SpokenWord.MOD_ID + ".config.onRespawnEnabled")
    @EventListenerOption(EventType.SELF_RESPAWN)
    public boolean onRespawnEnabled = false;

    @AutoGen(category = MESSAGES_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = StringListGroup.class, valueFactory = StringListGroup.class, addEntriesToBottom = true)
    @CustomName(SpokenWord.MOD_ID + ".config.onPlayerJoinMessage")
    public List<String> onPlayerJoinMessage = List.of("Welcome to the server, %player%!");

    @AutoGen(category = TOGGLES_CATEGORY, group = PLAYER_GROUP)
    @SerialEntry
    @MasterTickBox(value = "onPlayerJoinMessage")
    @CustomName(SpokenWord.MOD_ID + ".config.onPlayerJoinEnabled")
    @EventListenerOption(EventType.PLAYER_JOIN)
    public boolean onPlayerJoinEnabled = false;

    @AutoGen(category = MESSAGES_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = StringListGroup.class, valueFactory = StringListGroup.class, addEntriesToBottom = true)
    @CustomName(SpokenWord.MOD_ID + ".config.onPlayerLeaveMessage")
    public List<String> onPlayerLeaveMessage = List.of("Goodbye, %player%!");

    @AutoGen(category = TOGGLES_CATEGORY, group = PLAYER_GROUP)
    @SerialEntry
    @MasterTickBox(value = "onPlayerLeaveMessage")
    @CustomName(SpokenWord.MOD_ID + ".config.onPlayerLeaveEnabled")
    @EventListenerOption(EventType.PLAYER_LEAVE)
    public boolean onPlayerLeaveEnabled = false;

    @AutoGen(category = MESSAGES_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = StringListGroup.class, valueFactory = StringListGroup.class, addEntriesToBottom = true)
    @CustomName(SpokenWord.MOD_ID + ".config.onBlockBreakMessage")
    public List<String> onBlockBreakMessage = List.of("I have broken %block%!");

    @AutoGen(category = FILTERS_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = BlockListGroup.class, valueFactory = BlockListGroup.class, addEntriesToBottom = true)
    @CustomName(SpokenWord.MOD_ID + ".config.onBlockBreakFilter")
    public List<Block> onBlockBreakFilter = List.of(Blocks.GRASS_BLOCK);

    @AutoGen(category = TOGGLES_CATEGORY, group = BLOCK_GROUP)
    @SerialEntry
    @MasterTickBox(value = "onBlockBreakMessage")
    @EventListenerOption(EventType.BLOCK_BREAK)
    @CustomName(SpokenWord.MOD_ID + ".config.onBlockBreakEnabled")
    public boolean onBlockBreakEnabled = false;

    @AutoGen(category = MESSAGES_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = StringListGroup.class, valueFactory = StringListGroup.class, addEntriesToBottom = true)
    @CustomName(SpokenWord.MOD_ID + ".config.onBlockPlaceMessage")
    public List<String> onBlockPlaceMessage = List.of("I have placed %block%!");

    @AutoGen(category = FILTERS_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = BlockListGroup.class, valueFactory = BlockListGroup.class, addEntriesToBottom = true)
    @CustomName(SpokenWord.MOD_ID + ".config.onBlockPlaceFilter")
    public List<Block> onBlockPlaceFilter = List.of(Blocks.GRASS_BLOCK);

    @AutoGen(category = TOGGLES_CATEGORY, group = BLOCK_GROUP)
    @SerialEntry
    @MasterTickBox(value = "onBlockPlaceMessage")
    @EventListenerOption(EventType.BLOCK_PLACE)
    @CustomName(SpokenWord.MOD_ID + ".config.onBlockPlaceEnabled")
    public boolean onBlockPlaceEnabled = false;

    @AutoGen(category = MESSAGES_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = StringListGroup.class, valueFactory = StringListGroup.class, addEntriesToBottom = true)
    @CustomName(SpokenWord.MOD_ID + ".config.onOwnedDeathMessage")
    public List<String> onOwnedDeathMessage = List.of("%entity% has died. RIP");

    @AutoGen(category = TOGGLES_CATEGORY, group = ENTITY_GROUP)
    @SerialEntry
    @MasterTickBox(value = "onOwnedDeathMessage")
    @CustomName(SpokenWord.MOD_ID + ".config.onOwnedDeathEnabled")
    @EventListenerOption(EventType.OWNED_DEATH)
    public boolean onOwnedDeathEnabled = false;

    @AutoGen(category = MESSAGES_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = StringListGroup.class, valueFactory = StringListGroup.class, addEntriesToBottom = true)
    @CustomName(SpokenWord.MOD_ID + ".config.onEntityDeathMessage")
    public List<String> onEntityDeathMessage = List.of("%entity% has died");

    @AutoGen(category = FILTERS_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = EntityListGroup.class, valueFactory = EntityListGroup.class, addEntriesToBottom = true)
    @CustomName(SpokenWord.MOD_ID + ".config.onEntityDeathFilter")
    public List<EntityType<?>> onEntityDeathFilter = List.of();

    @AutoGen(category = TOGGLES_CATEGORY, group = ENTITY_GROUP)
    @SerialEntry
    @MasterTickBox(value = "onEntityDeathMessage")
    @CustomName(SpokenWord.MOD_ID + ".config.onEntityDeathEnabled")
    @EventListenerOption(EventType.ENTITY_DEATH)
    public boolean onEntityDeathEnabled = false;

    @AutoGen(category = MESSAGES_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = StringListGroup.class, valueFactory = StringListGroup.class, addEntriesToBottom = true)
    @CustomName(SpokenWord.MOD_ID + ".config.onKillPveMessage")
    public List<String> onKillPveMessage = List.of("I have killed %entity%");

    @AutoGen(category = FILTERS_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = EntityListGroup.class, valueFactory = EntityListGroup.class, addEntriesToBottom = true)
    @CustomName(SpokenWord.MOD_ID + ".config.onKillPveFilter")
    public List<EntityType<?>> onKillPveFilter = List.of();

    @AutoGen(category = MESSAGES_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = StringListGroup.class, valueFactory = StringListGroup.class, addEntriesToBottom = true)
    @CustomName(SpokenWord.MOD_ID + ".config.onChatMessage")
    public List<String> onChatMessage = List.of("Hello %player%!");

    @AutoGen(category = FILTERS_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = StringListGroup.class, valueFactory = StringListGroup.class, addEntriesToBottom = true)
    @CustomName(SpokenWord.MOD_ID + ".config.onChatFilter")
    public List<String> onChatFilter = List.of();

    @AutoGen(category = ADVANCED_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = StringListGroup.class, valueFactory = StringListGroup.class, addEntriesToBottom = true)
    @CustomName(SpokenWord.MOD_ID + ".config.onChatTrigger")
    public List<String> onChatTrigger = List.of();

    @AutoGen(category = TOGGLES_CATEGORY, group = CHAT_GROUP)
    @SerialEntry
    @MasterTickBox(value = "onChatMessage")
    @CustomName(SpokenWord.MOD_ID + ".config.onChatEnabled")
    @EventListenerOption(EventType.PLAYER_CHAT)
    public boolean onChatEnabled = false;

    @AutoGen(category = MESSAGES_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = StringListGroup.class, valueFactory = StringListGroup.class, addEntriesToBottom = true)
    @CustomName(SpokenWord.MOD_ID + ".config.onMessageMessage")
    public List<String> onMessageMessage = List.of("/tell %player% Hello %player%!");

    @AutoGen(category = FILTERS_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = StringListGroup.class, valueFactory = StringListGroup.class, addEntriesToBottom = true)
    @CustomName(SpokenWord.MOD_ID + ".config.onMessageFilter")
    public List<String> onMessageFilter = List.of();

    @AutoGen(category = ADVANCED_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = StringListGroup.class, valueFactory = StringListGroup.class, addEntriesToBottom = true)
    @CustomName(SpokenWord.MOD_ID + ".config.onMessageTrigger")
    public List<String> onMessageTrigger = List.of();

    @AutoGen(category = TOGGLES_CATEGORY, group = CHAT_GROUP)
    @SerialEntry
    @MasterTickBox(value = "onMessageFilter")
    @CustomName(SpokenWord.MOD_ID + ".config.onMessageEnabled")
    @EventListenerOption(EventType.PLAYER_MESSAGE)
    public boolean onMessageEnabled = false;
}
