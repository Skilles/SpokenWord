package net.spokenword.core.event.context;

import net.minecraft.world.entity.player.Player;

public class PlayerEventContext extends EntityEventContext {


    public PlayerEventContext(Player player) {
        super(player);
    }
}
