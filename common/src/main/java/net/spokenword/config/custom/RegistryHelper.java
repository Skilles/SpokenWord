package net.spokenword.config.custom;

import net.minecraft.ResourceLocationException;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class RegistryHelper<T> {

    private final DefaultedRegistry<T> registry;

    public RegistryHelper(DefaultedRegistry<T> registry) {
        this.registry = registry;
    }

    public boolean isRegistered(String identifier, Function<T, String> alternativeKeyGetter, @Nullable Predicate<T> filter) {
        try {
            // Test raw identifier
            ResourceLocation itemIdentifier = new ResourceLocation(identifier.toLowerCase());
            return registry.containsKey(itemIdentifier) && (filter == null || filter.test(registry.get(itemIdentifier)));
        } catch (ResourceLocationException e) {
            // Test alternative key
            return registry.entrySet().stream()
                           .filter(entry -> filter == null || filter.test(entry.getValue()))
                           .map(entry ->
                           {
                               String applied = alternativeKeyGetter.apply(entry.getValue());
                               System.out.printf("Applied: %s | Identifier: %s%n", applied, identifier);
                               return applied;
                           })
                           .anyMatch(key -> key.equalsIgnoreCase(identifier));
        }
    }

    /**
     * Looks up the item of the given identifier string.
     *
     * @param identifier EntityType identifier, either of the format "namespace:path" or "path". If no namespace is included,
     *                   the default vanilla namespace "minecraft" is used.
     * @return The item identified by the given string, or `EntityTypes.AIR` if the identifier is not known.
     */
    public T getFromName(String identifier) {
        return getFromName(identifier, null);
    }

    /**
     * Looks up the item of the given identifier string.
     *
     * @param identifier  Item identifier, either of the format "namespace:path" or "path". If no namespace is included,
     *                    the default vanilla namespace "minecraft" is used.
     * @param defaultType Fallback that gets returned if the identifier does not name a registered item.
     * @return The item identified by the given string, or the fallback if the identifier is not known.
     */
    public T getFromName(String identifier, T defaultType) {
        try {
            ResourceLocation itemIdentifier = new ResourceLocation(identifier.toLowerCase());
            if (registry.containsKey(itemIdentifier)) {
                return registry.get(itemIdentifier);
            }
        } catch (ResourceLocationException ignored) {
        }
        return defaultType;
    }

    /**
     * Returns a list of item identifiers matching the given string. The value matches an identifier if:
     * <li>No namespace is provided in the value and the value is a substring of the path segment of any identifier,
     * regardless of namespace.</li>
     * <li>A namespace is provided, equals the identifier's namespace, and the value is the begin of the identifier's
     * path segment.</li>
     *
     * @param value (partial) identifier, either of the format "namespace:path" or "path".
     * @return list of matching item identifiers; empty if the given string does not correspond to any known identifiers
     */
    public Stream<ResourceLocation> getMatchingIdentifiers(String value, Function<T, String> alternativeKeyGetter, @Nullable Predicate<T> filter) {
        int sep = value.indexOf(ResourceLocation.NAMESPACE_SEPARATOR);
        var filterPredicate = getFilterPredicate(value, sep, alternativeKeyGetter);

        if (filter != null) {
            filterPredicate = filterPredicate.and(identifier -> filter.test(registry.get(identifier)));
        }

        return registry.keySet().stream()
                       .filter(filterPredicate)
                       /*
                        Sort items as follows based on the given "value" string's path:
                        - if both items' paths begin with the entered string, sort the identifiers (including namespace)
                        - otherwise, if either of the items' path begins with the entered string, sort it to the left
                        - else neither path matches: sort by identifiers again

                        This allows the user to enter "diamond_ore" and match "minecraft:diamond_ore" before
                        "minecraft:deepslate_diamond_ore", even though the second is lexicographically smaller
                        */
                       .sorted((id1, id2) ->
                       {
                           String path = (sep == -1 ? value : value.substring(sep + 1));
                           boolean id1StartsWith = id1.getPath().startsWith(path);
                           boolean id2StartsWith = id2.getPath().startsWith(path);
                           if (id1StartsWith) {
                               if (id2StartsWith) {
                                   return id1.compareTo(id2);
                               }
                               return -1;
                           }
                           if (id2StartsWith) {
                               return 1;
                           }
                           return id1.compareTo(id2);
                       });
    }

    @NotNull
    private Predicate<ResourceLocation> getFilterPredicate(String value, int sep, Function<T, String> alternativeKeyGetter) {
        Predicate<ResourceLocation> filterPredicate;
        if (sep == -1) {
            filterPredicate = identifier ->
                    identifier.getPath().contains(value)
                            || alternativeKeyGetter.apply(registry.get(identifier)).toLowerCase().contains(value.toLowerCase());
        } else {
            String namespace = value.substring(0, sep);
            String path = value.substring(sep + 1);
            filterPredicate = identifier -> identifier.getNamespace().equals(namespace) && identifier.getPath().startsWith(path);
        }
        return filterPredicate;
    }
}
