package net.spokenword.config;

import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.autogen.AutoGen;
import dev.isxander.yacl3.config.v2.api.autogen.Boolean;
import dev.isxander.yacl3.config.v2.api.autogen.ListGroup;
import dev.isxander.yacl3.config.v2.api.autogen.MasterTickBox;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.spokenword.config.autoconfig.BlockListGroup;
import net.spokenword.config.autoconfig.EntityListGroup;
import net.spokenword.config.autoconfig.StringListGroup;

import java.util.List;

public class SpokenWordConfig
{
    private final String MAIN_CATEGORY = "main";
    private final String TOGGLES_CATEGORY = "toggles";
    private final String FILTERS_CATEGORY = "filters";
    private final String MESSAGES_CATEGORY = "messages";
    private final String ENTITY_GROUP = "entity";
    private final String BLOCK_GROUP = "block";
    private final String ITEM_GROUP = "item";
    private final String CHAT_GROUP = "chat";
    private final String PLAYER_GROUP = "player";
    private final String MISC_CATEGORY = "misc";

    @AutoGen(category = MAIN_CATEGORY)
    @SerialEntry
    @Boolean(formatter = Boolean.Formatter.ON_OFF)
    public boolean globalEnabled = true;

    @AutoGen(category = MESSAGES_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = StringListGroup.class, valueFactory = StringListGroup.class, addEntriesToBottom = true)
    public List<String> onPlayerJoinMessage = List.of("Welcome to the server, %player%!");

    @AutoGen(category = TOGGLES_CATEGORY, group = PLAYER_GROUP)
    @SerialEntry
    @MasterTickBox(value = "onPlayerJoinMessage")
    public boolean onPlayerJoinEnabled = false;

    @AutoGen(category = MESSAGES_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = StringListGroup.class, valueFactory = StringListGroup.class, addEntriesToBottom = true)
    public List<String> onPlayerLeaveMessage = List.of("Goodbye, %player%!");

    @AutoGen(category = TOGGLES_CATEGORY, group = PLAYER_GROUP)
    @SerialEntry
    @MasterTickBox(value = "onPlayerLeaveMessage")
    public boolean onPlayerLeaveEnabled = false;

    @AutoGen(category = MESSAGES_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = StringListGroup.class, valueFactory = StringListGroup.class, addEntriesToBottom = true)
    public List<String> onSelfJoinMessage = List.of("Hello it is me %player%!");

    @AutoGen(category = TOGGLES_CATEGORY, group = PLAYER_GROUP)
    @SerialEntry
    @MasterTickBox(value = "onSelfJoinMessage")
    public boolean onSelfJoinEnabled = false;

    @AutoGen(category = MESSAGES_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = StringListGroup.class, valueFactory = StringListGroup.class, addEntriesToBottom = true)
    public List<String> onSelfLeaveMessage = List.of("Goodbye, %player%!");

    @AutoGen(category = TOGGLES_CATEGORY, group = PLAYER_GROUP)
    @SerialEntry
    @MasterTickBox(value = "onSelfLeaveMessage")
    public boolean onSelfLeaveEnabled = false;

    @AutoGen(category = MESSAGES_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = StringListGroup.class, valueFactory = StringListGroup.class, addEntriesToBottom = true)
    public List<String> onSelfDeathMessage = List.of("I have died %player%!");

    @AutoGen(category = TOGGLES_CATEGORY, group = PLAYER_GROUP)
    @SerialEntry
    @MasterTickBox(value = "onSelfDeathMessage")
    public boolean onSelfDeathEnabled = false;

    @AutoGen(category = MESSAGES_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = StringListGroup.class, valueFactory = StringListGroup.class, addEntriesToBottom = true)
    public List<String> onBlockBreakMessage = List.of("I have broken %block%!");

    @AutoGen(category = FILTERS_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = BlockListGroup.class, valueFactory = BlockListGroup.class, addEntriesToBottom = true)
    public List<Block> onBlockBreakFilter = List.of(Blocks.GRASS_BLOCK);

    @AutoGen(category = TOGGLES_CATEGORY, group = BLOCK_GROUP)
    @SerialEntry
    @MasterTickBox(value = "onBlockBreakMessage")
    public boolean onBlockBreakEnabled = false;

    @AutoGen(category = MESSAGES_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = StringListGroup.class, valueFactory = StringListGroup.class, addEntriesToBottom = true)
    public List<String> onBlockPlaceMessage = List.of("I have placed %block%!");

    @AutoGen(category = FILTERS_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = BlockListGroup.class, valueFactory = BlockListGroup.class, addEntriesToBottom = true)
    public List<Block> onBlockPlaceFilter = List.of(Blocks.GRASS_BLOCK);

    @AutoGen(category = TOGGLES_CATEGORY, group = BLOCK_GROUP)
    @SerialEntry
    @MasterTickBox(value = "onBlockPlaceMessage")
    public boolean onBlockPlaceEnabled = false;

    @AutoGen(category = MESSAGES_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = StringListGroup.class, valueFactory = StringListGroup.class, addEntriesToBottom = true)
    public List<String> onOwnedDeathMessage = List.of("%entity% has died. RIP");

    @AutoGen(category = TOGGLES_CATEGORY, group = ENTITY_GROUP)
    @SerialEntry
    @MasterTickBox(value = "onOwnedDeathMessage")
    public boolean onOwnedDeathEnabled = false;

    @AutoGen(category = MESSAGES_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = StringListGroup.class, valueFactory = StringListGroup.class, addEntriesToBottom = true)
    public List<String> onEntityDeathMessage = List.of("%entity% has died");

    @AutoGen(category = FILTERS_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = EntityListGroup.class, valueFactory = EntityListGroup.class, addEntriesToBottom = true)
    public List<EntityType<?>> onEntityDeathFilter = List.of();

    @AutoGen(category = TOGGLES_CATEGORY, group = ENTITY_GROUP)
    @SerialEntry
    @MasterTickBox(value = "onEntityDeathMessage")
    public boolean onEntityDeathEnabled = false;

    @AutoGen(category = MESSAGES_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = StringListGroup.class, valueFactory = StringListGroup.class, addEntriesToBottom = true)
    public List<String> onKillPveMessage = List.of("I have killed %entity%");

    @AutoGen(category = FILTERS_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = EntityListGroup.class, valueFactory = EntityListGroup.class, addEntriesToBottom = true)
    public List<EntityType<?>> onKillPveFilter = List.of();

    @AutoGen(category = TOGGLES_CATEGORY, group = ENTITY_GROUP)
    @SerialEntry
    @MasterTickBox(value = "onKillPveMessage")
    public boolean onKillPveEnabled = false;

    @AutoGen(category = MESSAGES_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = StringListGroup.class, valueFactory = StringListGroup.class, addEntriesToBottom = true)
    public List<String> onKillPvpMessage = List.of("I have slain %player%");

    @AutoGen(category = FILTERS_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = StringListGroup.class, valueFactory = StringListGroup.class, addEntriesToBottom = true)
    public List<String> onKillPvpFilter = List.of();

    @AutoGen(category = TOGGLES_CATEGORY, group = PLAYER_GROUP)
    @SerialEntry
    @MasterTickBox(value = "onKillPvpMessage")
    public boolean onKillPvpEnabled = false;

    @AutoGen(category = MESSAGES_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = StringListGroup.class, valueFactory = StringListGroup.class, addEntriesToBottom = true)
    public List<String> onSelfDeathPveMessage = List.of("%entity% has killed me");

    @AutoGen(category = FILTERS_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = EntityListGroup.class, valueFactory = EntityListGroup.class, addEntriesToBottom = true)
    public List<EntityType<?>> onSelfDeathPveFilter = List.of();

    @AutoGen(category = TOGGLES_CATEGORY, group = ENTITY_GROUP)
    @SerialEntry
    @MasterTickBox(value = "onSelfDeathPveMessage")
    public boolean onSelfDeathPveEnabled = false;

    @AutoGen(category = MESSAGES_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = StringListGroup.class, valueFactory = StringListGroup.class, addEntriesToBottom = true)
    public List<String> onSelfDeathPvpMessage = List.of("%player% has killed me");

    @AutoGen(category = TOGGLES_CATEGORY, group = PLAYER_GROUP)
    @SerialEntry
    @MasterTickBox(value = "onSelfDeathPvpMessage")
    public boolean onSelfDeathPvpEnabled = false;

    @AutoGen(category = MESSAGES_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = StringListGroup.class, valueFactory = StringListGroup.class, addEntriesToBottom = true)
    public List<String> onChatMessage = List.of("Hello %player%!");

    @AutoGen(category = FILTERS_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = StringListGroup.class, valueFactory = StringListGroup.class, addEntriesToBottom = true)
    public List<String> onChatFilter = List.of();

    @AutoGen(category = TOGGLES_CATEGORY, group = CHAT_GROUP)
    @SerialEntry
    @MasterTickBox(value = "onChatMessage")
    public boolean onChatEnabled = false;

    @AutoGen(category = MESSAGES_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = StringListGroup.class, valueFactory = StringListGroup.class, addEntriesToBottom = true)
    public List<String> onMessageMessage = List.of("/tell %player% Hello %player%!");

    @AutoGen(category = FILTERS_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = StringListGroup.class, valueFactory = StringListGroup.class, addEntriesToBottom = true)
    public List<String> onMessageFilter = List.of();

    @AutoGen(category = TOGGLES_CATEGORY, group = CHAT_GROUP)
    @SerialEntry
    @MasterTickBox(value = "onMessageMessage")
    public boolean onMessageEnabled = false;

    @AutoGen(category = MESSAGES_CATEGORY)
    @SerialEntry
    @ListGroup(controllerFactory = StringListGroup.class, valueFactory = StringListGroup.class, addEntriesToBottom = true)
    public List<String> onRespawnMessage = List.of("I am reborn!");

    @AutoGen(category = TOGGLES_CATEGORY, group = PLAYER_GROUP)
    @SerialEntry
    @MasterTickBox(value = "onRespawnMessage")
    public boolean onRespawnEnabled = false;




    /*public class TestConfigCategory {

        @AutoConfigOption(name = "Test option 1", description = "This is a test description 1")
        @SerialEntry
        public String testStringOption = "test";

        @AutoConfigOption(name = "Test Boolean", description = "This is a test boolean description")
        @SerialEntry
        public List<Boolean> testBoolean = Collections.singletonList(false);

        @AutoConfigGroup(name = "Test Group", description = "This is a test group description")
        @SerialEntry
        public TestConfigGroup testGroup = new TestConfigGroup();


        public class TestConfigGroup {

            @AutoConfigOption(name = "Test option 2", description = "This is a test description 2")
            @SerialEntry
            public Boolean testStringOption = true;

        }
    }*/
}
