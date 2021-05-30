package com.skilles.spokenword;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;

import java.util.List;

@Environment(EnvType.CLIENT)
public class SpokenWord implements ClientModInitializer, ModMenuApi {
	public static ModConfig configData;
	public static final String MOD_ID = "spokenword";

	public static void sendMessages(String playerName, List<String> messageList) {
		assert MinecraftClient.getInstance().player != null;
		for (String message: messageList) {
			message = message.replaceAll("%p", playerName);
			MinecraftClient.getInstance().player.networkHandler.sendPacket(new ChatMessageC2SPacket(message));
		}
	}
	public static void sendMessages(List<String> messageList) {
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		assert player != null;
		for (String message: messageList) {
			message = message.replaceAll("%p", player.getDisplayName().getString());
			message = message.replaceAll("%s", player.getBlockPos().toShortString());
			player.networkHandler.sendPacket(new ChatMessageC2SPacket(message));
		}
		if(configData.respawn) player.requestRespawn();
	}
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> AutoConfig.getConfigScreen(ModConfig.class, parent).get();
	}
	@Override
	public void onInitializeClient() {
		AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
		configData = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
	}
}
