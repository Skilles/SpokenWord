package com.skilles.spokenword.mixin.handlers;

import com.skilles.spokenword.SpokenWord;
import com.skilles.spokenword.SpokenWordClient;
import com.skilles.spokenword.behaviors.regex.RegexPair;
import com.skilles.spokenword.config.ConfigUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@ClientOnly
public class MixinCommands
{

    private static final ChatType CHAT_TYPE;

    private static final ChatType MESSAGE_TYPE;

    static
    {
        var lookup = VanillaRegistries.createLookup().lookup(Registries.CHAT_TYPE).get();
        CHAT_TYPE = lookup.get(ChatType.CHAT).get().value();
        MESSAGE_TYPE = lookup.get(ChatType.MSG_COMMAND_INCOMING).get().value();
    }

    public static void handleChatMessage(PlayerChatMessage message, ChatType.Bound bound, CallbackInfo ci)
    {
        if (message.isSystem() ||
                Minecraft.getInstance().player == null ||
                message.sender().equals(Minecraft.getInstance().player.getUUID()))
        {
            return;
        }

        var sender = Minecraft.getInstance().level.getPlayerByUUID(message.sender());

        String id;

        if (bound.chatType().equals(CHAT_TYPE) || bound.chatType().chat().translationKey().equals("%s"))
        {
            id = "otherChat";
        }
        else if (bound.chatType().equals(MESSAGE_TYPE))
        {
           id = "otherMessage";
        }
        else
        {
            SpokenWord.LOGGER.info("Unknown chat type: " + bound.chatType().chat().translationKey());
            return;
        }

        SpokenWordClient.BEHAVIOR_MANAGER
                .startQuery(id)
                .withMessage(message.signedContent())
                .withRegex(new RegexPair('s', sender.getDisplayName().getString()))
                .activate();
    }

    public static void handleSystemMessage(Component component, CallbackInfo ci)
    {
        if (!(component.getContents() instanceof TranslatableContents tc))
        {
            if (Minecraft.getInstance().getCurrentServer() != null && !component.getSiblings().isEmpty())
            {
                // TODO
            }
            return;
        }

        String id = switch (tc.getKey())
        {
            case "multiplayer.player.joined", "multiplayer.player.joined.renamed" -> "otherJoin";
            case "multiplayer.player.left" -> "otherLeave"; // TODO allow override in config for custom server messages
            default -> null;
        };

        if (id == null)
        {
            return;
        }

        var playerName = tc.getArgument(0).getString();
        var localName = Minecraft.getInstance().player.getDisplayName().getString();

        if (playerName.equals(localName))
        {
            return;
        }

        SpokenWordClient.BEHAVIOR_MANAGER
                .startQuery(id)
                .withRegex(new RegexPair('s', playerName))
                .activate();
    }

    public static void handlePlayerCombatKill(int playerId, int killerId, CallbackInfo ci)
    {
        if (playerId != Minecraft.getInstance().player.getId())
        {
            return;
        }

        SpokenWordClient.BEHAVIOR_MANAGER.activate("respawn");
        SpokenWordClient.BEHAVIOR_MANAGER.activate("selfDeath");

        if (killerId == -1)
        {
            return;
        }

        var killer = Minecraft.getInstance().level.getEntity(killerId);

        if (killer.getType() == EntityType.PLAYER)
        {
            SpokenWordClient.BEHAVIOR_MANAGER
                    .startQuery("otherDeath")
                    .withRegex(new RegexPair('s', killer.getDisplayName().getString()))
                    .activate();
        }
        else
        {
            SpokenWordClient.BEHAVIOR_MANAGER
                    .startQuery("selfDeathPve")
                    .withRegex(new RegexPair('e', killer.getType().getDescription().getString()))
                    .activate();
        }
    }

    public static void handleDestroyBlock(BlockPos blockPos, CallbackInfo ci)
    {
        SpokenWordClient.BEHAVIOR_MANAGER
                .startQuery("blockBreak")
                .withRegex(new RegexPair('b', Minecraft.getInstance().level.getBlockState(blockPos).getBlock().getName().getString()))
                .activate();
    }

    public static void handleLogin(int playerId, CallbackInfo ci)
    {
        if (playerId == Minecraft.getInstance().player.getId())
        {
            SpokenWordClient.BEHAVIOR_MANAGER.activate("selfJoin");
        }
        else
        {
            SpokenWordClient.BEHAVIOR_MANAGER.activate("otherJoin");
        }
    }

    public static void handleDisconnect(Component reason, CallbackInfo ci)
    {
        SpokenWordClient.BEHAVIOR_MANAGER
                .startQuery("selfLeave")
                .withRegex(new RegexPair('r', reason.getString()))
                .activate();

        // wait a bit to allow the message to be sent
        try
        {
            Thread.sleep(5);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void handleEntityEvent(byte eventId, Entity entity, CallbackInfo ci)
    {
        if (eventId == 3 && !entity.getType().equals(EntityType.PLAYER))
        {
            var entityRegex = new RegexPair('e', entity.getType().getDescription().getString());

            SpokenWordClient.BEHAVIOR_MANAGER
                    .startQuery("entityDeath")
                    .withMessage(ConfigUtil.entityToString(entity.getType()))
                    .withRegex(entityRegex)
                    .activate();

            if (entity.hasCustomName())
            {

                SpokenWordClient.BEHAVIOR_MANAGER
                        .startQuery("ownedEntityDeath")
                        .withRegex(entityRegex)
                        .activate(); // TODO add hidden config options
            }
            else if (entity instanceof TamableAnimal animal && animal.isTame() && animal.isOwnedBy(Minecraft.getInstance().player))
            {
                SpokenWordClient.BEHAVIOR_MANAGER
                        .startQuery("ownedEntityDeath")
                        .withRegex(entityRegex)
                        .activate();
            }
        }
    }

    public static void handleLevelUp(int newLevel, CallbackInfo ci)
    {
        SpokenWordClient.BEHAVIOR_MANAGER
                .startQuery("reachLevel")
                .withValue(newLevel)
                .withRegex(new RegexPair('l', String.valueOf(newLevel)))
                .activate();
    }

}
