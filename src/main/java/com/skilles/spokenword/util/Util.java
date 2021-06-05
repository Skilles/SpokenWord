package com.skilles.spokenword.util;

import com.skilles.spokenword.config.ConfigManager;
import com.skilles.spokenword.config.ModConfig;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;

import static com.skilles.spokenword.SpokenWord.*;
import static com.skilles.spokenword.config.ConfigManager.*;
import static com.skilles.spokenword.config.ConfigManager.getList;
import static com.skilles.spokenword.config.ConfigManager.modeConfig;

public class Util {
    public static final Identifier PVP_LIST = id("pvp_list");
    public static final Identifier PVE_LIST = id("pve_list");
    public static final Identifier PLAYER_JOIN_LIST = id("join_list");
    public static final Identifier ON_JOIN_LIST = id("on_join_list");
    public static final Identifier BREAK_LIST = id("break_list");
    public static final Identifier OWNED_DEATH_LIST = id("owned_death_list");
    public static final Identifier ENTITY_DEATH_LIST = id("entity_death_list");
    public static final Identifier CHAT_LIST = id("chat_list");
    public static final Identifier MESSAGE_LIST = id("message_list");

    public static HashSet<EntityType<?>> typesToAdd = new HashSet<>();

    public static final String MOD_ID = "spokenword";

    public static Identifier id(String identifier) {
        return new Identifier(MOD_ID, identifier);
    }

    public static boolean containsCriteria(String playerName, TranslatableText message, Identifier list) {
        String messageString = message.getString();
        return getCriteria(list).stream().anyMatch(criteria -> messageString.contains(criteria.replace("%p", playerName)));
    }
    private static void requestRespawn() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        assert player != null;
        if(autoRespawn()) player.requestRespawn();
    }
    // TODO: merge with Entity sendMessages
    public static void sendMessages(String playerName, Identifier id) {
        assert MinecraftClient.getInstance().player != null;
        List<String> list = getList(id);
        list.stream().map(message -> message.replaceAll("%p", playerName)).forEachOrdered(message -> MinecraftClient.getInstance().player.networkHandler.sendPacket(new ChatMessageC2SPacket(message)));
        requestRespawn();
    }
    public static void sendMessages(Text text, Identifier id) {
        assert MinecraftClient.getInstance().player != null;
        List<String> list = getList(id);
        list.stream().map(message -> message.replaceAll("%p", text.getString()).replaceAll("%e", text.getString())).forEachOrdered(message -> MinecraftClient.getInstance().player.networkHandler.sendPacket(new ChatMessageC2SPacket(message)));
        requestRespawn();
    }
    public static void sendMessages(BlockState state, Identifier id) {
        assert MinecraftClient.getInstance().player != null;
        List<String> list = getList(id);
        list.stream().map(message -> message.replaceAll("%b", state.getBlock().getName().getString())).forEachOrdered(message -> MinecraftClient.getInstance().player.networkHandler.sendPacket(new ChatMessageC2SPacket(message)));
    }
    public static void sendMessages(Entity entity, Identifier id) {
        List<String> list = getList(id);
        list.stream().map(message -> Matcher.quoteReplacement(message).replaceAll("\\%e", entity.getDisplayName().getString())).forEachOrdered(message -> MinecraftClient.getInstance().player.networkHandler.sendPacket(new ChatMessageC2SPacket(message)));
        requestRespawn();
    }
    public static void sendMessages(Identifier id) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        List<String> list = getList(id);
        assert player != null;
        list.stream().map(message -> message.replaceAll("%p", player.getDisplayName().getString()).replaceAll("%s", player.getBlockPos().toShortString())).map(ChatMessageC2SPacket::new).forEachOrdered(player.networkHandler::sendPacket);
    }
}
