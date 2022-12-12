package com.skilles.spokenword.behaviors.regex;

import net.minecraft.client.Minecraft;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class RegexParser
{
    private static final Map<Character, Supplier<String>> sharedRegex = new HashMap<>() {{
        put('p', () -> Minecraft.getInstance().player.getDisplayName().getString());
    }};

    private static List<RegexPair> getSharedRegex()
    {
        return sharedRegex.entrySet().stream().map(entry -> new RegexPair(entry.getKey(), entry.getValue().get())).collect(Collectors.toList());
    }

    public static List<RegexPair> getRegex(Iterable<RegexPair> regex)
    {
        var output = getSharedRegex();
        for (var regexPair : regex)
        {
            output.add(regexPair);
        }
        return output;
    }
}
