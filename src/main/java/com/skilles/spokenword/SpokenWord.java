package com.skilles.spokenword;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;

@Environment(EnvType.CLIENT)
public class SpokenWord implements ClientModInitializer, ModMenuApi {
	public static ModConfig configData;
	public static final String MOD_ID = "spokenword";

	public static void sendMessages(String playerName) {
		assert MinecraftClient.getInstance().player != null;
		for (String message: configData.textList) {
			message = message.replaceAll("%p", playerName);
			MinecraftClient.getInstance().player.networkHandler.sendPacket(
					new ChatMessageC2SPacket(message)
			);
		}
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
