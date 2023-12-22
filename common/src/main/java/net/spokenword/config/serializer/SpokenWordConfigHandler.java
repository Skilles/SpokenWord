package net.spokenword.config.serializer;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.config.v2.api.*;
import dev.isxander.yacl3.config.v2.api.autogen.AutoGen;
import dev.isxander.yacl3.config.v2.api.autogen.OptionAccess;
import dev.isxander.yacl3.config.v2.impl.ConfigFieldImpl;
import dev.isxander.yacl3.config.v2.impl.ReflectionFieldAccess;
import dev.isxander.yacl3.config.v2.impl.autogen.OptionAccessImpl;
import dev.isxander.yacl3.config.v2.impl.autogen.OptionFactoryRegistry;
import dev.isxander.yacl3.config.v2.impl.autogen.YACLAutoGenException;
import dev.isxander.yacl3.impl.utils.YACLConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.spokenword.SpokenWord;
import net.spokenword.config.SpokenWordConfig;
import org.apache.commons.lang3.Validate;

import java.lang.reflect.Constructor;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SpokenWordConfigHandler implements ConfigClassHandler<SpokenWordConfig> {

    private final ResourceLocation id;
    private final ConfigSerializer<SpokenWordConfig> serializer;
    private final ConfigFieldImpl<?>[] fields;

    private SpokenWordConfig instance;
    private final SpokenWordConfig defaults;
    private final Constructor<SpokenWordConfig> noArgsConstructor;

    public SpokenWordConfigHandler(Function<ConfigClassHandler<SpokenWordConfig>, ConfigSerializer<SpokenWordConfig>> serializerFactory) {
        this.id = new ResourceLocation(SpokenWord.MOD_ID, "config");

        try {
            noArgsConstructor = SpokenWordConfig.class.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new YACLAutoGenException("Failed to find no-args constructor for config class %s.".formatted(SpokenWordConfig.class.getName()), e);
        }
        this.instance = createNewObject();
        this.defaults = createNewObject();

        this.fields = Arrays.stream(SpokenWordConfig.class.getDeclaredFields())
                            .peek(field -> field.setAccessible(true))
                            .filter(field -> field.isAnnotationPresent(SerialEntry.class) || field.isAnnotationPresent(AutoGen.class))
                            .map(field -> new ConfigFieldImpl<>(new ReflectionFieldAccess<>(field, instance), new ReflectionFieldAccess<>(field, defaults), this, field.getAnnotation(SerialEntry.class), field.getAnnotation(AutoGen.class)))
                            .toArray(ConfigFieldImpl[]::new);
        this.serializer = serializerFactory.apply(this);
    }

    @Override
    public SpokenWordConfig instance() {
        return this.instance;
    }

    @Override
    public SpokenWordConfig defaults() {
        return this.defaults;
    }

    @Override
    public Class<SpokenWordConfig> configClass() {
        return SpokenWordConfig.class;
    }

    @Override
    public ConfigFieldImpl<?>[] fields() {
        return this.fields;
    }

    @Override
    public ResourceLocation id() {
        return this.id;
    }

    @Override
    public boolean supportsAutoGen() {
        return true;
    }

    @Override
    public YetAnotherConfigLib generateGui() {
        boolean hasAutoGenFields = Arrays.stream(fields()).anyMatch(field -> field.autoGen().isPresent());

        if (!hasAutoGenFields) {
            throw new YACLAutoGenException("No fields in this config class are annotated with @AutoGen. You must annotate at least one field with @AutoGen to generate a GUI.");
        }

        OptionAccessImpl storage = new OptionAccessImpl();
        Map<String, CategoryAndGroups> categories = new LinkedHashMap<>();
        for (ConfigField<?> configField : fields()) {
            configField.autoGen().ifPresent(autoGen -> {
                CategoryAndGroups groups = categories.computeIfAbsent(
                        autoGen.category(),
                        k -> new CategoryAndGroups(
                                ConfigCategory.createBuilder()
                                              .name(Component.translatable("%s.category.%s".formatted(id().toLanguageKey(), k))),
                                new LinkedHashMap<>()
                        )
                );
                var group = groups.groups().computeIfAbsent(autoGen.group().orElse(""), k -> {
                    if (k.isEmpty()) {
                        return groups.category();
                    }
                    return OptionGroup.createBuilder()
                                      .name(Component.translatable("%s.category.%s.group.%s".formatted(id().toLanguageKey(), autoGen.category(), k)));
                });

                Option<?> option;
                try {
                    option = createOption(configField, storage);
                } catch (Exception e) {
                    throw new YACLAutoGenException("Failed to create option for field '%s'".formatted(configField.access().name()), e);
                }

                storage.putOption(configField.access().name(), option);
                if (option instanceof ListOption<?> listOption) {
                    groups.groups.put(listOption.name().getString(), listOption);
                } else if (group instanceof OptionAddable optionAddable) {
                    optionAddable.option(option);
                }
            });
        }
        storage.checkBadOperations();
        categories.values().forEach(CategoryAndGroups::finaliseGroups);

        YetAnotherConfigLib.Builder yaclBuilder = YetAnotherConfigLib.createBuilder()
                                                                     .save(this::save)
                                                                     .title(Component.translatable("%s.title".formatted(this.id().toLanguageKey())));
        categories.values().forEach(category -> yaclBuilder.category(category.category().build()));

        return yaclBuilder.build();
    }

    private <U> Option<U> createOption(ConfigField<U> configField, OptionAccess storage) {
        return OptionFactoryRegistry.createOption(((ReflectionFieldAccess<?>) configField.access()).field(), configField, storage)
                                    .orElseThrow(() -> new YACLAutoGenException("Failed to create option for field %s".formatted(configField.access().name())));
    }

    @Override
    public ConfigSerializer<SpokenWordConfig> serializer() {
        return this.serializer;
    }

    @Override
    public boolean load() {
        // create a new instance to load into
        SpokenWordConfig newInstance = createNewObject();

        // create field accesses for the new object
        Map<ConfigFieldImpl<?>, ReflectionFieldAccess<?>> accessBufferImpl = Arrays.stream(fields())
                                                                                   .map(field -> new AbstractMap.SimpleImmutableEntry<>(
                                                                                           field,
                                                                                           new ReflectionFieldAccess<>(field.access().field(), newInstance)
                                                                                   ))
                                                                                   .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        // convert the map into API safe field accesses
        Map<ConfigField<?>, FieldAccess<?>> accessBuffer = accessBufferImpl.entrySet().stream()
                                                                           .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        // attempt to load the config
        ConfigSerializer.LoadResult loadResult = ConfigSerializer.LoadResult.FAILURE;
        Throwable error = null;
        try {
            loadResult = this.serializer().loadSafely(accessBuffer);
        } catch (Throwable e) {
            // handle any errors later in the loadResult switch case
            error = e;
        }

        switch (loadResult) {
            case DIRTY:
            case SUCCESS:
                // replace the instance with the newly created one
                this.instance = newInstance;
                for (ConfigFieldImpl<?> field : fields()) {
                    // update the field accesses to point to the correct object
                    ((ConfigFieldImpl<Object>) field).setFieldAccess((ReflectionFieldAccess<Object>) accessBufferImpl.get(field));
                }

                if (loadResult == ConfigSerializer.LoadResult.DIRTY) {
                    // if the load result is dirty, we need to save the config again
                    this.save();
                }
            case NO_CHANGE:
                return true;
            case FAILURE:
                YACLConstants.LOGGER.error(
                        "Unsuccessful load of config class '{}'. The load will be abandoned and config remains unchanged.",
                        SpokenWordConfig.class.getSimpleName(), error
                );
        }

        return false;
    }

    @Override
    public void save() {
        serializer().save();
        SpokenWord.getBehaviorManager().refreshBehaviors();
    }

    private SpokenWordConfig createNewObject() {
        try {
            return noArgsConstructor.newInstance();
        } catch (Exception e) {
            throw new YACLAutoGenException("Failed to create instance of config class '%s' with no-args constructor.".formatted(SpokenWordConfig.class.getName()), e);
        }
    }

    private record CategoryAndGroups(ConfigCategory.Builder category, Map<String, Object> groups) {
        private void finaliseGroups() {
            groups.forEach((name, group) -> {
                if (group instanceof OptionGroup.Builder groupBuilder) {
                    category.group(groupBuilder.build());
                } else if (group instanceof ListOption<?> listOption) {
                    category.group(listOption);
                }
            });
        }
    }

    public static ConfigClassHandler.Builder<SpokenWordConfig> createBuilder() {
        return new Builder();
    }

    public static class Builder implements ConfigClassHandler.Builder<SpokenWordConfig> {

        private Function<ConfigClassHandler<SpokenWordConfig>, ConfigSerializer<SpokenWordConfig>> serializerFactory;

        public Builder id(ResourceLocation id) {
            return this;
        }

        public Builder serializer(Function<ConfigClassHandler<SpokenWordConfig>, ConfigSerializer<SpokenWordConfig>> serializerFactory) {
            this.serializerFactory = serializerFactory;
            return this;
        }

        public ConfigClassHandler<SpokenWordConfig> build() {
            Validate.notNull(serializerFactory, "serializerFactory must not be null");

            return new SpokenWordConfigHandler(serializerFactory);
        }
    }
}
