package net.spokenword.core.event.context;

import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.world.entity.player.Player;

public class PlayerEventContext extends AbstractEventContext<String> {

    private final String playerName;

    public PlayerEventContext(Player player) {
        this(player.getDisplayName().getString());
    }

    public PlayerEventContext(PlayerInfo playerInfo) {
        this(playerInfo.getProfile().getName());
    }

    public PlayerEventContext(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public String getFilterable() {
        return playerName;
    }

    @Override
    public String getSourceName() {
        return playerName;
    }
}
