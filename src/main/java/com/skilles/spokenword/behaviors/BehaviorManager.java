package com.skilles.spokenword.behaviors;

import com.skilles.spokenword.behaviors.annotations.BehaviorAction;
import com.skilles.spokenword.behaviors.annotations.BehaviorFilter;
import com.skilles.spokenword.behaviors.annotations.BehaviorMessage;
import com.skilles.spokenword.behaviors.annotations.BehaviorToggle;
import com.skilles.spokenword.config.SWConfigData;
import com.skilles.spokenword.exceptions.ConfigException;
import com.skilles.spokenword.exceptions.UnknownBehaviorException;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BehaviorManager
{

    private final Map<String, AbstractBehavior> behaviors;

    private final Map<String, Boolean> toggles;

    public BehaviorManager()
    {
        this.behaviors = new HashMap<>();
        this.toggles = new HashMap<>();
    }

    public void init(SWConfigData config)
    {
        behaviors.clear();
        toggles.clear();
        initBehaviors(config);
    }

    public boolean activate(String id, RegexPair... args)
    {
        return activate(id, null, args);
    }

    public boolean activate(String id, @Nullable String message, RegexPair... args)
    {
        AbstractBehavior behavior = behaviors.get(id);

        if (behavior == null)
        {
            throw new UnknownBehaviorException(id);
        }

        if (toggles.get(id))
        {
            args = ArrayUtils.add(args, new RegexPair('p', Minecraft.getInstance().player.getDisplayName().getString()));

            if (message != null && behavior instanceof MessageBehavior mb && !mb.matchesFilter(message, args))
            {
                return false;
            }

            behavior.activate(args);
            return true;
        }

        return false;
    }

    private void initBehaviors(SWConfigData config)
    {
        var reflections = new Reflections(SWConfigData.class, Scanners.FieldsAnnotated);

        try
        {
            initMessageBehaviors(config, reflections, getFilters(config, reflections));

            initActionBehaviors(config, reflections);

            initMessageToggles(config, reflections);
        }
        catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e)
        {
            throw new ConfigException("Error accessing config fields", e);
        }
    }

    private void initMessageBehaviors(SWConfigData config, Reflections ref, HashMap<String, Collection<String>> filters) throws IllegalAccessException
    {
        var messageFields = ref.getFieldsAnnotatedWith(BehaviorMessage.class);
        for (var field : messageFields)
        {
            var ma = field.getAnnotation(BehaviorMessage.class);
            var id = ma.value();
            var value = field.get(config.toggles);
            var builder = new MessageBehavior.Builder();

            builder.setFilters(filters.get(id));

            if (value instanceof Collection<?> list)
            {
                builder.setMessages((Collection<String>) list);
            }
            else if (value instanceof String string)
            {
                builder.setMessages(string);
            }

            behaviors.put(id, builder.build());
        }
    }

    private void initActionBehaviors(SWConfigData config, Reflections ref) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException
    {
        var actionFields = ref.getFieldsAnnotatedWith(BehaviorAction.class);
        for (var field : actionFields)
        {
            var value = field.get(config.actions);
            var annotation = field.getAnnotation(BehaviorAction.class);
            var id = annotation.id();

            if (value instanceof Boolean bool)
            {
                var behavior = annotation.behavior().getConstructor().newInstance();
                behaviors.put(id, behavior);
                toggles.put(id, bool);
            }
        }
    }

    private void initMessageToggles(SWConfigData config, Reflections ref) throws IllegalAccessException
    {
        var toggleFields = ref.getFieldsAnnotatedWith(BehaviorToggle.class);

        for (var field : toggleFields)
        {
            var id = field.getAnnotation(BehaviorToggle.class).value();
            toggles.put(id, field.getBoolean(config.toggles));
        }
    }

    private static HashMap<String, Collection<String>> getFilters(SWConfigData config, Reflections ref) throws IllegalAccessException
    {
        var filterFields = ref.getFieldsAnnotatedWith(BehaviorFilter.class);
        var filters = new HashMap<String, Collection<String>>();

        for (var field : filterFields)
        {
            var id = field.getAnnotation(BehaviorFilter.class).value();
            var value = field.get(config.filters);
            filters.put(id, (Collection<String>) value);
        }

        return filters;
    }

}
