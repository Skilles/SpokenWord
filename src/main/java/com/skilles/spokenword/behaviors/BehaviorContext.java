package com.skilles.spokenword.behaviors;

import com.skilles.spokenword.behaviors.regex.RegexPair;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public record BehaviorContext(Optional<String> message, Optional<Integer> value, Optional<Iterable<RegexPair>> regex, Player player)
{
    public BehaviorContext(Player player)
    {
        this(Optional.empty(), Optional.empty(), Optional.empty(), player);
    }
}
