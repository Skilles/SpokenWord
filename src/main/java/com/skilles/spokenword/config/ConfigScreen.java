package com.skilles.spokenword.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.Optional;

import static com.skilles.spokenword.SpokenWord.log;
import static com.skilles.spokenword.config.ConfigManager.*;
import static com.skilles.spokenword.config.ConfigManager.chatConfig;
import static com.skilles.spokenword.config.ConfigPojo.*;
import static com.skilles.spokenword.util.ConfigUtil.*;
import static com.skilles.spokenword.util.Util.*;

public class ConfigScreen {
    public static Screen getConfigScreen(Screen parentScreen) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parentScreen)
                .setDefaultBackgroundTexture(new Identifier("minecraft:textures/block/bee_nest_side.png"))
                .setTitle(new TranslatableText("config.spokenword.title"));

        builder.setGlobalized(true);
        builder.setGlobalizedExpanded(false);

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigCategory general = builder.getOrCreateCategory(new TranslatableText("config.spokenword.category.general"));

        general.addEntry(getBooleanEntry("globalenable", generalGroup.globalEnable, true, entryBuilder)
                .setSaveConsumer((value) -> { generalGroup.globalEnable = value; })
                .build());
        general.addEntry(getBooleanEntry("respawn", generalGroup.respawn, false, entryBuilder)
                .setTooltip(Optional.of(new Text[]{
                        new TranslatableText("config.spokenword.mode.respawn.tooltip1"),
                        new TranslatableText("config.spokenword.mode.respawn.tooltip2").formatted(Formatting.RED)}))
                .setSaveConsumer((value) -> { generalGroup.respawn = value; })
                .build());
        general.addEntry(entryBuilder.startStrList(new TranslatableText("config.spokenword.list.ipfilter"), generalConfig().ipFilter)
                .setSaveConsumer((value) -> {
                    generalConfig().ipFilter = value;
                })
                .setTooltip(new TranslatableText("config.spokenword.list.ipfilter.tooltip"))
                .build());

        ConfigCategory modes = builder.getOrCreateCategory(new TranslatableText("config.spokenword.category.modes"));

        modes.addEntry(getBooleanEntry("playerjoin", modeGroup.playerjoin, true, entryBuilder)
                .setSaveConsumer((value) -> { modeGroup.playerjoin = value; })
                .build());
        modes.addEntry(getBooleanEntry("death", modeGroup.death, true, entryBuilder)
                .setSaveConsumer((value) -> { modeGroup.death = value; })
                .build());
        modes.addEntry(getBooleanEntry("onjoin", modeGroup.onJoin, false, entryBuilder)
                .setSaveConsumer((value) -> { modeGroup.onJoin = value; })
                .build());
        modes.addEntry(getBooleanEntry("onowned", modeGroup.onOwnedDeath, true, entryBuilder)
                .setSaveConsumer((value) -> { modeGroup.onOwnedDeath = value; })
                .build());
        modes.addEntry(getBooleanEntry("onbreak", modeGroup.onBreak, false, entryBuilder)
                .setSaveConsumer((value) -> { modeGroup.onBreak = value; })
                .build());

        ConfigCategory advanced = builder.getOrCreateCategory(new TranslatableText("config.spokenword.category.advanced"));

        // Death category
        SubCategoryBuilder deathCategory = entryBuilder.startSubCategory(new TranslatableText("config.spokenword.subcategory.death"));
        deathCategory.add(getBooleanEntry("pvp", deathGroup.pvp, true, entryBuilder)
                .setSaveConsumer((value) -> { deathGroup.pvp = value; })
                .build());
        deathCategory.add(getBooleanEntry("pve", deathGroup.pve, true, entryBuilder)
                .setSaveConsumer((value) -> { deathGroup.pve = value; })
                .build());
        deathCategory.add(getEntitySelector("kill", listToString(deathGroup.entityKillList), ListModes.HOSTILE, entryBuilder));
        deathCategory.add(getBooleanEntry("entitydeath", modeGroup.onEntityDeath, false, entryBuilder)
                .setSaveConsumer((value) -> { modeGroup.onEntityDeath = value; })
                .build());
        deathCategory.add(getEntitySelector("death", listToString(deathGroup.entityDeathList), ListModes.ALL, entryBuilder));

        // Chat category
        SubCategoryBuilder chatCategory = entryBuilder.startSubCategory(new TranslatableText("config.spokenword.subcategory.chat"));
        chatCategory.add(getBooleanEntry("onchat", chatGroup.onChat, false, entryBuilder)
                .setSaveConsumer((value) -> { chatGroup.onChat = value; })
                .build());
        chatCategory.add(entryBuilder.startStrList(new TranslatableText("config.spokenword.list.chatcriteria"), chatConfig().chatCriteria)
                .setSaveConsumer((value) -> {
                    chatConfig().chatCriteria = value;
                })
                .setTooltip(new TranslatableText("config.spokenword.list.chatcriteria.tooltip"))
                .build());
        chatCategory.add(getBooleanEntry("onmessage", chatGroup.onMessage, true, entryBuilder)
                .setSaveConsumer((value) -> { chatGroup.onMessage = value; })
                .build());
        chatCategory.add(entryBuilder.startStrList(new TranslatableText("config.spokenword.list.messagecriteria"), chatConfig().messageCriteria)
                .setSaveConsumer((value) -> {
                    chatConfig().messageCriteria = value;
                })
                .setTooltip(new TranslatableText("config.spokenword.list.messagecriteria.tooltip"))
                .build());
        chatCategory.add(getBooleanEntry("gg", chatGroup.gg, false, entryBuilder)
                .setSaveConsumer((value) -> {
                    chatGroup.gg = value;
                })
                .build());
        chatCategory.add(entryBuilder.startStrField(new TranslatableText("config.spokenword.ggmessage"), chatConfig().ggMessage)
                .setDefaultValue("gg")
                .setTooltip(new TranslatableText("config.spokenword.ggmessage.tooltip"))
                .setSaveConsumer((value) -> {
                    chatConfig().ggMessage = value;
                })
                .build());
        advanced.addEntry(deathCategory.build());
        advanced.addEntry(chatCategory.build());

        ConfigCategory lists = builder.getOrCreateCategory(new TranslatableText("config.spokenword.category.lists"));

        lists.addEntry(entryBuilder.startTextDescription(new TranslatableText("config.spokenword.category.lists.description")).build());
        lists.addEntry(entryBuilder.startStrList(new TranslatableText("config.spokenword.list.playerjoin"), getList(PLAYER_JOIN_LIST))
                .setSaveConsumer((value) -> {
                    listConfig().playerJoinList = value;
                })
                .setTooltip(new TranslatableText("config.spokenword.list.playerjoin.tooltip"))
                .build());
        lists.addEntry(entryBuilder.startStrList(new TranslatableText("config.spokenword.list.onjoin"), getList(ON_JOIN_LIST))
                .setSaveConsumer((value) -> {
                    listConfig().onJoinList = value;
                })
                .setTooltip(new TranslatableText("config.spokenword.list.onjoin.tooltip"))
                .build());
        lists.addEntry(entryBuilder.startStrList(new TranslatableText("config.spokenword.list.break"), getList(BREAK_LIST))
                .setSaveConsumer((value) -> {
                    listConfig().breakList = value;
                })
                .setTooltip(new TranslatableText("config.spokenword.list.break.tooltip"))
                .build());
        lists.addEntry(entryBuilder.startStrList(new TranslatableText("config.spokenword.list.pvp"), getList(PVP_LIST))
                .setSaveConsumer((value) -> {
                    listConfig().pvpList = value;
                })
                .setTooltip(new TranslatableText("config.spokenword.list.pvp.tooltip"))
                .build());
        lists.addEntry(entryBuilder.startStrList(new TranslatableText("config.spokenword.list.pve"), getList(PVE_LIST))
                .setSaveConsumer((value) -> {
                    listConfig().pveList = value;
                })
                .setTooltip(new TranslatableText("config.spokenword.list.pve.tooltip"))
                .build());
        lists.addEntry(entryBuilder.startStrList(new TranslatableText("config.spokenword.list.owned"), getList(OWNED_DEATH_LIST))
                .setSaveConsumer((value) -> {
                    listConfig().ownDeathList = value;
                })
                .setTooltip(new TranslatableText("config.spokenword.list.owned.tooltip"))
                .build());
        lists.addEntry(entryBuilder.startStrList(new TranslatableText("config.spokenword.list.entitydeath"), getList(ENTITY_DEATH_LIST))
                .setSaveConsumer((value) -> {
                    listConfig().entityDiedList = value;
                })
                .setTooltip(new TranslatableText("config.spokenword.list.entitydeath.tooltip"))
                .build());
        lists.addEntry(entryBuilder.startStrList(new TranslatableText("config.spokenword.list.chatlist"), getList(CHAT_LIST))
                .setSaveConsumer((value) -> {
                    listConfig().chatList = value;
                })
                .setTooltip(new TranslatableText("config.spokenword.list.chatlist.tooltip"))
                .build());
        lists.addEntry(entryBuilder.startStrList(new TranslatableText("config.spokenword.list.messagelist"), getList(MESSAGE_LIST))
                .setSaveConsumer((value) -> {
                    listConfig().messageList = value;
                })
                .setTooltip(new TranslatableText("config.spokenword.list.messagelist.tooltip"))
                .build());

        builder.setSavingRunnable(ModConfig::saveConfig);

        return builder.build();
    }
}