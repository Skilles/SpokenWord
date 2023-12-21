package net.spokenword.core.behavior;

import net.spokenword.SpokenWord;
import net.spokenword.config.autoconfig.EventListenerOption;
import net.spokenword.config.exception.ConfigException;
import net.spokenword.core.event.EventType;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConfigBehaviorFactory {

    public List<Behavior<?>> createBehaviors() {
        var config = SpokenWord.getConfig();
        var fields = config.getClass().getDeclaredFields();
        var enabledFields = new HashMap<String, Field>();
        var messagesFields = new HashMap<String, Field>();
        var filterFields = new HashMap<String, Field>();

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

        // Create behaviors from the fields
        var behaviors = new ArrayList<Behavior<?>>();
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

                behaviors.add(new Behavior<>(id, messages, filter, eventTypes));
            } catch (IllegalAccessException e) {
                throw new ConfigException(e);
            }
        }

        return behaviors;
    }
}
