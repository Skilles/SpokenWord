package com.skilles.spokenword.mixin.handlers;

import com.skilles.spokenword.SpokenWordClient;
import com.skilles.spokenword.behaviors.RegexPair;
import com.skilles.spokenword.config.ConfigUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class MixinCommands
{

    private static final ChatType CHAT_TYPE = BuiltinRegistries.CHAT_TYPE.get(ChatType.CHAT);

    private static final ChatType MESSAGE_TYPE = BuiltinRegistries.CHAT_TYPE.get(ChatType.MSG_COMMAND_INCOMING);

    public static void handleChatMessage(PlayerChatMessage message, ChatType.Bound bound, CallbackInfo ci)
    {
        if (message.signer().isSystem())
        {
            return;
        }

        if (message.signer().profileId().equals(Minecraft.getInstance().player.getUUID()))
        {
            return;
        }

        var sender = Minecraft.getInstance().level.getPlayerByUUID(message.signedHeader().sender());

        if (bound.chatType().equals(CHAT_TYPE))
        {
            SpokenWordClient.BEHAVIOR_MANAGER.activate(
                    "otherChat",
                    message.signedContent().plain(),
                    new RegexPair('s', sender.getDisplayName().getString())); // replace %s with sender name
        }
        else if (bound.chatType().equals(MESSAGE_TYPE))
        {
            SpokenWordClient.BEHAVIOR_MANAGER.activate(
                    "otherMessage",
                    message.signedContent().plain(),
                    new RegexPair('s', sender.getDisplayName().getString()));
        }
    }

    public static void handleSystemMessage(Component component, CallbackInfo ci)
    {
        if (!(component.getContents() instanceof TranslatableContents tc))
        {
            return;
        }

        String id = switch (tc.getKey())
        {
            case "multiplayer.player.joined" -> "otherJoin";
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

        SpokenWordClient.BEHAVIOR_MANAGER.activate(id, new RegexPair('s', playerName));
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
            SpokenWordClient.BEHAVIOR_MANAGER.activate("selfDeathPvp", new RegexPair('s', killer.getName().getString()));
        }
        else
        {
            SpokenWordClient.BEHAVIOR_MANAGER.activate("selfDeathPve", new RegexPair('e', killer.getType().getDescription().getString()));
        }
    }

    public static void handleDestroyBlock(BlockPos blockPos, CallbackInfo ci)
    {
        SpokenWordClient.BEHAVIOR_MANAGER.activate("blockBreak",
                new RegexPair('b', Minecraft.getInstance().level.getBlockState(blockPos).getBlock().getName().getString()));
    }

    public static void handleLogin(int playerId, CallbackInfo ci)
    {
        if (playerId == Minecraft.getInstance().player.getId())
        {
            SpokenWordClient.BEHAVIOR_MANAGER.activate("selfJoin");
        }
        else
        {
            SpokenWordClient.BEHAVIOR_MANAGER.activate("otherJoin", new RegexPair('p', Minecraft.getInstance().level.getEntity(playerId).getDisplayName().getString()));
        }
    }

    public static void handleEntityEvent(byte eventId, Entity entity, CallbackInfo ci)
    {
        if (eventId == 3)
        {
            var entityRegex = new RegexPair('e', entity.getType().getDescription().getString());

            SpokenWordClient.BEHAVIOR_MANAGER.activate("entityDeath", ConfigUtil.entityToString(entity.getType()), entityRegex);

            if (entity.hasCustomName())
            {
                SpokenWordClient.BEHAVIOR_MANAGER.activate("ownedEntityDeath", entityRegex); // TODO add hidden config options
            }
            else if (entity instanceof TamableAnimal animal && animal.isTame() && animal.isOwnedBy(Minecraft.getInstance().player))
            {
                SpokenWordClient.BEHAVIOR_MANAGER.activate("ownedEntityDeath", entityRegex);
            }
        }
    }

}
