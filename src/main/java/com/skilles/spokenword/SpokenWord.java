package com.skilles.spokenword;

import com.google.common.collect.Maps;
import com.skilles.spokenword.config.ModConfig;
import com.skilles.spokenword.util.ConfigUtil;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.FurnaceMinecartEntity;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class SpokenWord implements ClientModInitializer {

	public static Logger LOGGER = LogManager.getLogger();
	public static void log(Level level, String message) {
		LOGGER.log(level, "[SpokenWord] "+message);
	}
	public static void log(String message) {
		log(Level.INFO, message);
	}
	public static void log(Object message) {
		log(String.valueOf(message));
	}

	@Override
	public void onInitializeClient() {
		/*ConfigManager.registerConfig();
		ConfigManager.loadConfig();
		AutoConfig.getConfigHolder(ModConfig.class).registerSaveListener((manager, data) -> {
			return ActionResult.SUCCESS;
		});*/
		ModConfig.loadModConfig();
		ConfigUtil.initEntities();
	}
	public enum HostileTypes {
		ZOMBIE(EntityType.ZOMBIE),
		SKELETON(EntityType.SKELETON),
		ENDERMAN(EntityType.ENDERMAN),
		SLIME(EntityType.SLIME),
		PILLAGER(EntityType.PILLAGER),
		GUARDIAN(EntityType.GUARDIAN),
		SILVERFISH(EntityType.SILVERFISH),
		SPIDER(EntityType.SPIDER),
		BLAZE(EntityType.BLAZE),
		CREEPER(EntityType.CREEPER),
		WITCH(EntityType.WITCH),
		VEX(EntityType.VEX),
		RAVAGER(EntityType.RAVAGER),
		VINDICATOR(EntityType.VINDICATOR),
		ENDERMITE(EntityType.ENDERMITE),
		EVOKER(EntityType.EVOKER),
		HOGLIN(EntityType.HOGLIN),
		PIGLIN(EntityType.PIGLIN),
		WITHER(EntityType.WITHER),
		ENDERDRAGON(EntityType.ENDER_DRAGON),
		GHAST(EntityType.GHAST),
		HUSK(EntityType.HUSK),
		MAGMACUBE(EntityType.MAGMA_CUBE),
		DROWNED(EntityType.DROWNED),
		STRAY(EntityType.STRAY),
		CAVESPIDER(EntityType.CAVE_SPIDER),
		ZOGLIN(EntityType.ZOGLIN),
		ZOMBIEVILLAGER(EntityType.ZOMBIE_VILLAGER),
		ELDERGUARDIAN(EntityType.ELDER_GUARDIAN),
		ANY(EntityType.FURNACE_MINECART);

		HostileTypes(EntityType<?> type) {
		}
	}
	public enum EntityTypes {
		ZOMBIE(EntityType.ZOMBIE),
		SKELETON(EntityType.SKELETON),
		ENDERMAN(EntityType.ENDERMAN),
		SLIME(EntityType.SLIME),
		PILLAGER(EntityType.PILLAGER),
		GUARDIAN(EntityType.GUARDIAN),
		SILVERFISH(EntityType.SILVERFISH),
		SPIDER(EntityType.SPIDER),
		BLAZE(EntityType.BLAZE),
		CREEPER(EntityType.CREEPER),
		WITCH(EntityType.WITCH),
		VEX(EntityType.VEX),
		RAVAGER(EntityType.RAVAGER),
		VINDICATOR(EntityType.VINDICATOR),
		ENDERMITE(EntityType.ENDERMITE),
		EVOKER(EntityType.EVOKER),
		HOGLIN(EntityType.HOGLIN),
		PIGLIN(EntityType.PIGLIN),
		ZOGLIN(EntityType.ZOGLIN),
		WITHER(EntityType.WITHER),
		ENDERDRAGON(EntityType.ENDER_DRAGON),
		GHAST(EntityType.GHAST),
		HUSK(EntityType.HUSK),
		MAGMACUBE(EntityType.MAGMA_CUBE),
		DROWNED(EntityType.DROWNED),
		STRAY(EntityType.STRAY),
		CAVESPIDER(EntityType.CAVE_SPIDER),
		ZOMBIEVILLAGER(EntityType.ZOMBIE_VILLAGER),
		ELDERGUARDIAN(EntityType.ELDER_GUARDIAN),
		VILLAGER(EntityType.VILLAGER),
		OCELOT(EntityType.OCELOT),
		RABBIT(EntityType.RABBIT),
		HORSE(EntityType.HORSE),
		LLAMA(EntityType.LLAMA),
		PANDA(EntityType.PANDA),
		WOLF(EntityType.WOLF),
		BEE(EntityType.BEE),
		STRIDER(EntityType.STRIDER),
		TURTLE(EntityType.TURTLE),
		FISH(EntityType.TROPICAL_FISH),
		PUFFERFISH(EntityType.PUFFERFISH),
		PARROT(EntityType.PARROT),
		MOOSHROOM(EntityType.MOOSHROOM),
		ANY(EntityType.FURNACE_MINECART);
		EntityTypes(EntityType<?> type) {
			this.type = type;
		}
		private final EntityType<?> type;
		public EntityType<?> getType() {
			return this.type;
		}
		@Nullable
		public static EntityTypes fromString(String string) {
			for(EntityTypes type : values()) {
				if(type.name().equalsIgnoreCase(string)) return type;
			}
			return null;
		}
		private static final Map<EntityType<?>, EntityTypes> LOOKUP = Maps.newHashMapWithExpectedSize(EntityTypes.values().length);
		static {
			for (EntityTypes type : EntityTypes.values()) {
				LOOKUP.put(type.getType(), type);
			}
		}
		@Nullable
		public static EntityTypes fromType(EntityType<?> type) {
			return LOOKUP.getOrDefault(type, null);
		}
	}
}
