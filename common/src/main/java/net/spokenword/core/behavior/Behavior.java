package net.spokenword.core.behavior;

import net.minecraft.client.Minecraft;
import net.spokenword.SpokenWord;
import net.spokenword.core.event.AbstractEventListener;
import net.spokenword.core.event.EventType;
import net.spokenword.core.event.transformer.EventTransformer;
import net.spokenword.core.format.MessageVariable;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Behavior<T> extends AbstractEventListener {

    private List<String> messages;

    @Nullable
    private List<T> filter;

    private final String name;

    public Behavior(String name, List<String> messages, @Nullable List<T> filter, EventType... eventType) {
        super(eventType);
        this.name = name;
        this.messages = messages;
        this.filter = filter;
    }

    @Override
    public void onEvent(EventTransformer transformer) {
        if (filter != null && transformer.testFilter(filter)) {
            return;
        }

        var localPlayer = Minecraft.getInstance().player;
        for (String message : messages) {
            message = transformer.formatMessage(MessageVariable.SELF.replace(message, localPlayer.getDisplayName().getString()));
            SpokenWord.getBehaviorManager().getChatSender().sendChatMessage(message);
        }
    }

    public void update(List<String> messages, @Nullable List<T> filter) {
        this.messages = messages;
        this.filter = filter;
    }

    @Override
    public String toString() {
        return name;
    }
}
