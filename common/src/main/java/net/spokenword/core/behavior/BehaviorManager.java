package net.spokenword.core.behavior;

import com.google.common.collect.ImmutableList;
import net.spokenword.SpokenWord;
import net.spokenword.config.autoconfig.EventListenerOption;
import net.spokenword.config.exception.ConfigException;
import net.spokenword.core.event.EventType;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BehaviorManager {

    private static BehaviorManager INSTANCE;

    private List<Behavior<?>> behaviors;

    private boolean initialized = false;

    public BehaviorManager() {
        INSTANCE = this;
        this.behaviors = new ArrayList<>();
    }

    private void subscribeBehaviors() {
        behaviors.forEach(Behavior::subscribe);
    }

    public void reset() {
        updateBehaviors();
        if (!initialized) {
            // Freeze the list
            this.behaviors = ImmutableList.copyOf(behaviors);
            subscribeBehaviors();
            initialized = true;
        }
    }

    public void refreshBehaviors() {
        SpokenWord.getLogger().info("Refreshing behaviors...");
        INSTANCE.reset();
    }

    private void updateBehaviors() {
        var config = SpokenWord.getConfig();
        var fields = config.getClass().getDeclaredFields();
        var enabledFields = new HashMap<String, Field>();
        var messagesFields = new HashMap<String, Field>();
        var filterFields = new HashMap<String, Field>();

        var isUpdate = !behaviors.isEmpty();

        // Find and separate the enabled, message, and filter fields
        for (var field : fields) {
            var name = field.getName();

            // Skip private and static fields
            var modifiers = field.getModifiers();
            if (Modifier.isStatic(modifiers) || !Modifier.isPublic(modifiers)) {
                continue;
            }
            String id;
            if (name.endsWith("Enabled")) {
                id = name.substring(0, name.length() - "Enabled".length());
                enabledFields.put(id, field);
            } else if (name.endsWith("Message")) {
                id = name.substring(0, name.length() - "Message".length());
                messagesFields.put(id, field);
            } else if (name.endsWith("Filter")) {
                id = name.substring(0, name.length() - "Filter".length());
                filterFields.put(id, field);
            }
        }

        for (var id : messagesFields.keySet()) {
            var enabledField = enabledFields.get(id);
            var messagesField = messagesFields.get(id);
            var filterField = filterFields.get(id);

            if (enabledField == null) {
                throw new ConfigException("Missing field " + id + "Enabled" + " in config");
            }

            try {
                var enabled = enabledField.getBoolean(config);

                if (!enabled) {
                    continue;
                }

                var eventListener = enabledField.getAnnotation(EventListenerOption.class);

                EventType[] eventTypes = new EventType[0];
                if (eventListener != null) {
                    eventTypes = eventListener.value();
                }

                var messages = (List<String>) messagesField.get(config);
                var filter = filterField == null ? null : (List) filterField.get(config);

                if (!isUpdate) {
                    behaviors.add(new Behavior<>(id, messages, filter, eventTypes));
                    continue;
                }

                behaviors.stream().filter(b -> b.toString().equals(id)).findFirst().ifPresent(b -> b.update(messages, filter));
            } catch (IllegalAccessException e) {
                throw new ConfigException(e);
            }
        }
    }
}
