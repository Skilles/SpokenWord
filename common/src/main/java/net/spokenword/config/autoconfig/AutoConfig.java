package net.spokenword.config.autoconfig;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import dev.isxander.yacl3.impl.controller.EnumControllerBuilderImpl;
import net.minecraft.network.chat.Component;
import net.spokenword.config.SpokenWordConfig;
import net.spokenword.config.exception.ConfigException;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AutoConfig {

    public static void initialize(SpokenWordConfig defaults, SpokenWordConfig config, YetAnotherConfigLib.Builder configBuilder) {
        var categories = new ArrayList<ConfigCategory>();

        try {
            var fields = config.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(AutoConfigCategory.class)) {
                    var category = field.getAnnotation(AutoConfigCategory.class);
                    var name = category.name();
                    var tooltip = category.tooltip();
                    var value = field.get(config);
                    var categoryBuilder = ConfigCategory.createBuilder();
                    categoryBuilder.name(Component.literal(name));
                    categoryBuilder.tooltip(Component.literal(tooltip));

                    if (configureCategoryOptions(value, categoryBuilder)) {
                        categories.add(categoryBuilder.build());
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new ConfigException(e);
        }
        configBuilder.categories(categories);
    }

    private static boolean configureCategoryOptions(Object root, ConfigCategory.Builder categoryBuilder) throws IllegalAccessException {
        var fields = root.getClass().getDeclaredFields();
        var groups = new ArrayList<OptionGroup>();

        // For fields with no groups
        configureGroupOptions(root, Optional.empty(), categoryBuilder);

        for (Field field : fields) {
            if (field.isAnnotationPresent(AutoConfigGroup.class)) {
                var group = field.getAnnotation(AutoConfigGroup.class);
                var name = group.name();
                var description = group.description();
                var value = field.get(root);
                var groupBuilder = OptionGroup.createBuilder();
                groupBuilder.name(Component.literal(name));
                groupBuilder.description(OptionDescription.of(Component.literal(description)));
                if (configureGroupOptions(value, Optional.of(groupBuilder), categoryBuilder)) {
                    groups.add(groupBuilder.build());
                }
            }
        }

        categoryBuilder.groups(groups);
        return !groups.isEmpty();
    }

    private static boolean configureGroupOptions(Object root, Optional<OptionGroup.Builder> groupBuilder, ConfigCategory.Builder categoryBuilder) throws IllegalAccessException {
        var fields = root.getClass().getDeclaredFields();
        var options = new ArrayList<Option<?>>();
        var listOptions = new ArrayList<OptionGroup>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(AutoConfigOption.class)) {
                var optionAnnotation = field.getAnnotation(AutoConfigOption.class);
                var name = optionAnnotation.name();
                var description = optionAnnotation.description();
                var type = field.getType();
                var isList = type == List.class;
                var value = field.get(root);
                if (isList) {
                    var listOption = createListOption(root, field, (List<Object>) value, name, description);

                    listOptions.add(listOption);
                } else {
                    var option = createOption(root, field, type, value, name, description);

                    options.add(option);
                }
            }
        }

        if (!options.isEmpty()) {
            groupBuilder.ifPresentOrElse(builder -> builder.options(options), () -> categoryBuilder.options(options));
        }

        // List options cannot be added to groups
        if (!listOptions.isEmpty()) {
            categoryBuilder.groups(listOptions);
        }

        return !options.isEmpty();
    }

    private static ListOption<Object> createListOption(Object root, Field field, List<Object> value, String name, String description) {
        var builder = ListOption.createBuilder();
        var innerClass = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];

        configureController(builder, innerClass);

        builder.initial(getDefaultListValue(innerClass));
        builder.binding(value, () ->
        {
            try {
                return (List<Object>) field.get(root);
            } catch (IllegalAccessException e) {
                throw new ConfigException(e);
            }
        }, newVal ->
        {
            try {
                field.set(root, newVal);
            } catch (IllegalAccessException e) {
                throw new ConfigException(e);
            }
        });
        builder.name(Component.literal(name));
        builder.description(OptionDescription.of(Component.literal(description)));

        return builder.build();
    }

    private static Option<Object> createOption(Object root, Field field, Class<?> type, Object value, String name, String description) {
        var builder = Option.createBuilder();
        configureController(builder, type);

        builder.binding(value, () ->
        {
            try {
                return field.get(root);
            } catch (IllegalAccessException e) {
                throw new ConfigException(e);
            }
        }, newVal ->
        {
            try {
                field.set(root, newVal);
            } catch (IllegalAccessException e) {
                throw new ConfigException(e);
            }
        });
        builder.name(Component.literal(name));
        builder.description(OptionDescription.of(Component.literal(description)));

        return builder.build();
    }

    private static void configureController(ListOption.Builder<?> builder, Class<?> type) {
        builder.controller((option) -> (ControllerBuilder) getControllerBuilder(option, type));
    }

    private static Object getDefaultListValue(Class<?> type) {
        if (type == Boolean.class) {
            return false;
        } else if (type == Integer.class) {
            return 0;
        } else if (type == Double.class) {
            return 0.0;
        } else if (type == Float.class) {
            return 0.0f;
        } else if (type == String.class) {
            return "";
        } else if (type == Enum.class) {
            return null;
        } else {
            throw new RuntimeException("Unsupported type: " + type);
        }
    }

    private static void configureController(Option.Builder<?> builder, Class<?> type) {
        builder.controller((option) -> (ControllerBuilder) getControllerBuilder(option, type));
    }

    private static ControllerBuilder<?> getControllerBuilder(Option<?> option, Class<?> type) {
        if (type == boolean.class || type == Boolean.class) {
            return BooleanControllerBuilder.create((Option<Boolean>) option);
        } else if (type == int.class || type == Integer.class) {
            return IntegerFieldControllerBuilder.create((Option<Integer>) option);
        } else if (type == double.class || type == Double.class) {
            return DoubleFieldControllerBuilder.create((Option<Double>) option);
        } else if (type == float.class || type == Float.class) {
            return FloatFieldControllerBuilder.create((Option<Float>) option);
        } else if (type == String.class) {
            return StringControllerBuilder.create((Option<String>) option);
        } else if (type == Enum.class) {
            return new EnumControllerBuilderImpl(option);
        } else {
            throw new RuntimeException("Unsupported type: " + type);
        }
    }
}
