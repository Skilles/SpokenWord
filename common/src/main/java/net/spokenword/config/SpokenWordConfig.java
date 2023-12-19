package net.spokenword.config;

import dev.architectury.platform.Platform;
import dev.isxander.yacl3.api.controller.DropdownStringControllerBuilder;
import dev.isxander.yacl3.config.ConfigEntry;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.autogen.*;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.spokenword.SpokenWord;
import net.spokenword.config.autoconfig.*;

import java.lang.Boolean;
import java.util.Collections;
import java.util.List;

public class SpokenWordConfig
{

    /*@AutoConfigCategory(name = "Test Category", tooltip = "This is a test category tooltip")
    @SerialEntry
    public TestConfigCategory testCategory = new TestConfigCategory();*/

    private final String MAIN_CATEGORY = "main";

    @AutoGen(category = MAIN_CATEGORY)
    @Dropdown(values = {"Option 1", "Option 2", "Option 3"})
    public String autogenString = "Option 1";

    @AutoGen(category = MAIN_CATEGORY)
    @ListGroup(controllerFactory = EntityListGroup.class, valueFactory = EntityListGroup.class)
    @TargetEntity(hidePassives = true)
    public List<EntityType<?>> autogenEntityValues = List.of(EntityType.ZOMBIE);

    @AutoGen(category = MAIN_CATEGORY)
    @ListGroup(controllerFactory = ItemListGroup.class, valueFactory = ItemListGroup.class)
    public List<Item> autogenStringValues = List.of(Item.byId(1));

    @AutoGen(category = MAIN_CATEGORY)
    @MasterTickBox(value = "autogenStringValues")
    public boolean autogenBoolean = true;


    public class TestConfigCategory {

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
    }


}
