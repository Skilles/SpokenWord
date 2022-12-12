package com.skilles.spokenword.behaviors;

import com.skilles.spokenword.SpokenWord;
import com.skilles.spokenword.behaviors.annotations.BehaviorAction;
import com.skilles.spokenword.behaviors.annotations.BehaviorFilter;
import com.skilles.spokenword.behaviors.annotations.BehaviorMessage;
import com.skilles.spokenword.behaviors.annotations.BehaviorToggle;
import com.skilles.spokenword.behaviors.regex.RegexPair;
import com.skilles.spokenword.behaviors.regex.RegexParser;
import com.skilles.spokenword.config.SWConfigData;
import com.skilles.spokenword.exceptions.BehaviorException;
import com.skilles.spokenword.exceptions.ConfigException;
import com.skilles.spokenword.exceptions.UnknownBehaviorException;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class BehaviorManager
{

    private final Map<String, AbstractBehavior> behaviors;

    private final RegexParser regexParser;

    public BehaviorManager()
    {
        this.behaviors = new HashMap<>();
        this.regexParser = new RegexParser();
    }

    public void init(SWConfigData config)
    {
        behaviors.clear();
        initBehaviors(config);
    }

    public MessageQueryBuilder startQuery(String id)
    {
        return new MessageQueryBuilder(id);
    }

    @ClientOnly
    public boolean activate(String id)
    {
        return activate(id, new BehaviorContext(Minecraft.getInstance().player));
    }

    public void printBehaviors()
    {
        for (AbstractBehavior behavior : behaviors.values())
        {
            SpokenWord.LOGGER.info(behavior.toString());
        }
    }

    private boolean activate(String id, @Nullable BehaviorContext ctx)
    {
        AbstractBehavior behavior = behaviors.get(id);

        if (behavior == null)
        {
            throw new UnknownBehaviorException(id);
        }

        if (ctx == null && behavior instanceof MessageBehavior)
        {
            throw new BehaviorException("MessageBehavior requires a context");
        }

        SpokenWord.log("Behavior Activate [" + id + "] = " + behaviors.get(id).isEnabled());

        return behavior.activate(ctx);
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
            var filter = filters.get(id);

            if (ma.isAdvanced())
            {
                builder.setAdvanced();
            }

            if (filter != null)
            {
                builder.setFilters(filters.get(id));
            }

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
                behavior.setEnabled(bool);
                behaviors.put(id, behavior);
            }
        }
    }

    private void initMessageToggles(SWConfigData config, Reflections ref) throws IllegalAccessException
    {
        var toggleFields = ref.getFieldsAnnotatedWith(BehaviorToggle.class);

        for (var field : toggleFields)
        {
            var id = field.getAnnotation(BehaviorToggle.class).value();
            if (!behaviors.containsKey(id))
            {
                continue;
            }

            if (field.getType() == boolean.class)
            {
                behaviors.get(id).setEnabled(field.getBoolean(config.toggles));
            }
            else if (field.getType() == int.class)
            {
                var behavior = behaviors.get(id);
                var threshold = field.getInt(config.toggles);
                if (behavior instanceof AdvancedMessageBehavior amb)
                {
                    amb.setThreshold(threshold);
                }
                else
                {
                    throw new ConfigException("Behavior " + id + " is not an AdvancedMessageBehavior when it should be");
                }
                behavior.setEnabled(threshold != -1);
            }
            else
            {
                throw new ConfigException("Behavior " + id + " has unsupported type " + field.getType());
            }
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

    public class MessageQueryBuilder
    {
        private final String id;

        private String message;

        private int value;

        private Iterable<RegexPair> regex;

        private Player player;

        public MessageQueryBuilder(String id)
        {
            this.id = id;
            this.message = null;
            this.value = 0;
            this.player = Minecraft.getInstance().player;
        }

        public MessageQueryBuilder withMessage(String message)
        {
            this.message = message;

            return this;
        }

        public MessageQueryBuilder withValue(int value)
        {
            this.value = value;

            return this;
        }

        public MessageQueryBuilder withRegex(RegexPair... regex)
        {
            this.regex = List.of(regex);

            return this;
        }

        public MessageQueryBuilder withPlayer(Player player)
        {
            this.player = player;

            return this;
        }

        public boolean activate()
        {
            if (player == null)
            {
                throw new BehaviorException("Player cannot be null");
            }

            var ctx = new BehaviorContext(Optional.ofNullable(this.message), Optional.of(this.value), this.regex == null ? Optional.empty() :
                    Optional.of(RegexParser.getRegex(this.regex)), player);

            return BehaviorManager.this.activate(this.id, ctx);
        }
    }
}
