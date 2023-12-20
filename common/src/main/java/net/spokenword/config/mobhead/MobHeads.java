package net.spokenword.config.mobhead;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.spokenword.SpokenWord;

import java.util.HashMap;

public class MobHeads {

    private static final HashMap<EntityType<?>, MobHead> mobHeads = new HashMap<>();

    private static final HashMap<EntityType<?>, Item> mobHeadItems = new HashMap<>() {
        {
            put(EntityType.ZOMBIE, Items.ZOMBIE_HEAD);
            put(EntityType.GIANT, Items.ZOMBIE_HEAD);
            put(EntityType.SKELETON, Items.SKELETON_SKULL);
            put(EntityType.CREEPER, Items.CREEPER_HEAD);
            put(EntityType.WITHER_SKELETON, Items.WITHER_SKELETON_SKULL);
            put(EntityType.ENDER_DRAGON, Items.DRAGON_HEAD);
            put(EntityType.PIGLIN, Items.PIGLIN_HEAD);
        }
    };

    private static final ItemStack defaultMobHead = Items.PLAYER_HEAD.getDefaultInstance();

    public static ItemStack getMobHead(EntityType<?> type) {


        if (mobHeadItems.containsKey(type)) {
            return mobHeadItems.get(type).getDefaultInstance();
        }

        if (mobHeads.containsKey(type)) {
            return mobHeads.get(type).toItemStack();
        }

        SpokenWord.LOGGER.warning("No mob head found for entity type " + type.toShortString());

        return defaultMobHead;
    }

    protected static void registerHead(EntityType<?> type, MobHead mobHead) {
        mobHeads.put(type, mobHead);
    }

    protected static void reload() {
        mobHeads.clear();
    }

    public record MobHead(int[] uuid, String data) {

        public ItemStack toItemStack() {
            var itemStack = Items.PLAYER_HEAD.getDefaultInstance();

            var skullCompoundTag = new CompoundTag();
            var propertiesCompoundTag = new CompoundTag();
            var texturesListTag = new ListTag();
            var texturesCompoundTag = new CompoundTag();

            var texturesValueStringTag = StringTag.valueOf(data);

            texturesCompoundTag.put("Value", texturesValueStringTag);
            texturesListTag.add(texturesCompoundTag);
            propertiesCompoundTag.put("textures", texturesListTag);
            skullCompoundTag.put("Properties", propertiesCompoundTag);
            skullCompoundTag.put("Id", new IntArrayTag(uuid));
            itemStack.addTagElement("SkullOwner", skullCompoundTag);

            return itemStack;
        }
    }
}
