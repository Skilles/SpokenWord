package com.skilles.spokenword.behaviors.regex;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public record RegexPair(Pattern pattern, String replacement)
{

    private static final Map<Character, Pattern> cachedPatterns = new HashMap<>();

    public RegexPair(char variable, String replacement)
    {
        this(cachedPatterns.computeIfAbsent(variable, v -> Pattern.compile("%" + variable)), replacement);
    }

    public String apply(String input)
    {
        return this.pattern.matcher(input).replaceAll(this.replacement);
    }

}
